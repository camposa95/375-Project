package graphs;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.Map.Entry;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import gamedatastructures.Player;

/**
 * Graph for the Roads on the map
 */
public class RoadGraph {

    private static final int NUM_ROADS = 72;
    private static final int MAX_ADJACENT_ROADS = 4;
    private static final int MAX_ADJACENT_VERTEXES = 2;
    private static final Integer MIN_ROADS_FOR_CARD = 5;

    private Road[] roads;

    /**
     * Creates a new RoadGrapgh with the default number of Roads
     *
     * Note: Adjacency must be initialized separatly by calling
     * the appropriate initializer methods
     */
    public RoadGraph() {
        this.roads = new Road[NUM_ROADS];

        for (int i = 0; i < NUM_ROADS; i++) {
            this.roads[i] = new Road(i);
        }
    }

    /**
     * Gets the road with the specified location ID.
     *
     * @param locationId the locationId of the Road to get
     * @return the road with the specified locationID
     * @throws IllegalArgumentException if the location ID is out of bounds
     */
    public Road getRoad(final int locationId) {
        if (locationId >= NUM_ROADS || locationId < 0) {
            throw new IllegalArgumentException("LocationId out of bounds: Try [0, 72]");
        }

        return this.roads[locationId];
    }

    /**
     * Reads though the layout file and initializes the graph with the defined
     * adjacency relationships
     */
    @SuppressFBWarnings("PATH_TRAVERSAL_IN")
    public void initializeRoadToRoadAdjacency(final String filePath) {
        File roadLayout = new File(filePath);
        try (Scanner scanner = new Scanner(roadLayout, StandardCharsets.UTF_8.name())) {
            int roadToInitializeId = 0;
            Road roadToInitialize;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(" ");
                Road[] adjacentRoadsBuffer = new Road[MAX_ADJACENT_ROADS];

                for (int i = 0; i < values.length; i++) {
                    int roadId = Integer.parseInt(values[i]);
                    adjacentRoadsBuffer[i] = getRoad(roadId);
                }

                roadToInitialize = getRoad(roadToInitializeId);
                Road[] adjacentRoads = Arrays.copyOf(adjacentRoadsBuffer, values.length);
                roadToInitialize.setAdjacentRoads(adjacentRoads);
                roadToInitializeId++;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Incorrect filename/path when initialized");
        }
    }

    /**
     * Reads though the RoadToVertexlayout file and initializes the graph with the defined
     * adjacency relationships
     */
    @SuppressFBWarnings("PATH_TRAVERSAL_IN")
    public void initializeRoadToVertexAdjacency(final VertexGraph vertexes, final String filePath) {
        File roadToVertexLayout = new File(filePath);
        try (Scanner scanner = new Scanner(roadToVertexLayout, StandardCharsets.UTF_8.name())) {
            int roadToInitializeId = 0;
            Road roadToInitialize;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(" ");
                Vertex[] adjacentVertexesBuffer = new Vertex[MAX_ADJACENT_VERTEXES];

                for (int i = 0; i < values.length; i++) {
                    int vertexId = Integer.parseInt(values[i]);
                    adjacentVertexesBuffer[i] = vertexes.getVertex(vertexId);
                }

                roadToInitialize = getRoad(roadToInitializeId);
                Vertex[] adjacentVertexes = Arrays.copyOf(adjacentVertexesBuffer, values.length);
                roadToInitialize.setAdjacentVertexes(adjacentVertexes);
                roadToInitializeId++;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Incorrect filename/path when initialized");
        }
    }

    // ----------------------------------------------------------------------------
    //
    // Longest road stuff
    //
    // ----------------------------------------------------------------------------

    /**
     * Function that calculates the longest path given the starting road and origin vertex of the path.
     *
     * @param startingRoad
     * @param player
     * @param paths
     * @param visitedRoads
     * @param origin
     * @return
     */
    public int getLongestPath(final Road startingRoad, final Player player, final HashSet<HashSet<Road>> paths, final HashSet<Road> visitedRoads, final Vertex origin) {
        if (startingRoad.getOwner() != player) {
            throw new IllegalStateException("Road does not own player, check calling method");
        }

        if (origin.ownedByEnemyOf(player)) {
            return -1; // stop here before we add ourself to the path because we are blocked off
        }

        // add ourselves to the current path and the path to the list of paths
        visitedRoads.add(startingRoad);
        paths.add(visitedRoads);

        Vertex tail = startingRoad.getTail(origin);
        for (Road neighbor: tail.getAdjacentRoads()) { // now we only have 2 options to branch off of
            if (neighbor.getOwner() == player && !visitedRoads.contains(neighbor)) {

                HashSet<Road> newPath = new HashSet<Road>(visitedRoads);
                getLongestPath(neighbor, player, paths, newPath, tail); // origin of the next one is the tail of this one
            }
        }

        int longestLength = 0;
        for (HashSet<Road> path: paths) {
            longestLength = Math.max(longestLength, path.size());
        }

        return longestLength;
    }

    /**
     * Method that gives the correct player the longest road card according to the game rules
     *
     * Note: Doesn't really give a player the card. But sets a flag in the player indicatig it owns it and
     * also increment and degrements victory points accordingly.
     *
     * Also Note: This method is assuming it is called after a Road or Vertex was built. This is to ensure
     * that the special cases of transfership are correctly set up.
     */
    public void giveLongestRoadCard() {
        // Code that maps players to their longest paths
        Set<Player> players = this.getPlayers();
        HashMap<Player, Integer> playerToLongestPathMap = new HashMap<Player, Integer>();
        for (Player player: players) {

            Set<Vertex> origins = this.getOrigins(player); // get starting points for our searches

            int longest = 0;
            for (Vertex origin: origins) { // loop though all the path origin vertexes

                // Note: looping though all of these is fine becuase only 1 will be the longest and if it is a vertex not connected
                // to anything then it simply be skipped
                for (Road startingRoad: origin.getAdjacentRoads()) {
                    if (startingRoad.getOwner() == player) { // make sure we only start at Roads owned by the player
                        int pathLength = this.getLongestPath(startingRoad, player, new HashSet<HashSet<Road>>(), new HashSet<Road>(), origin);
                        longest = Math.max(pathLength, longest);
                    }
                }
            }

            playerToLongestPathMap.put(player, longest); // add the longest path to the map of players to their longes path
        }

        // ----------------------------------------------------------------------------
        //
        // Code that uses the map to distribute the longest Road card
        //
        // ----------------------------------------------------------------------------

        // find out who currently has the longest road before transfering
        Player playerWithCard = null;
        for (Player player: players) {
            if (player.hasLongestRoad()) {
                playerWithCard = player;
            }
        }

        // if no one had the card we simply give it to the first person with road length 5 or no one if they are all short
        if (playerWithCard == null) {
            Player newPlayerWithCard = getPlayerWithLongestRoad(playerToLongestPathMap);
            if (newPlayerWithCard != null) {
                newPlayerWithCard.giveLongestRoadCard();
            }

            return; // no one had it and no one got it now
        }


        // -----------------------------------------------------------------------
        //
        // If we are here then somebody already has the longest road card
        //
        // -----------------------------------------------------------------------

        if (!somebodyHasLongEnoughRoad(playerToLongestPathMap)) { // no one has a long enough road
            playerWithCard.removeLongestRoadCard(); // no one gets it
            return;
        }

        players.remove(playerWithCard); // so we don't compare against ourselves
        // Since we are here we know somebody already had the card so nulls are gone
        Player tempPlayerWithLongestRoad = playerWithCard;
        int longestRoad = playerToLongestPathMap.get(playerWithCard);
        for (Player player: players) {
            if (playerToLongestPathMap.get(player) > longestRoad) {
                tempPlayerWithLongestRoad = player;
            }
        }
        if (tempPlayerWithLongestRoad == playerWithCard) { // we know the player who origionally had is was not overtaken
            return; // stop here
        }

        if (twoPlayersTiedForLongestRoad(players, playerToLongestPathMap)) {
            playerWithCard.removeLongestRoadCard(); // nobody gets it now
        } else { // somebody strictly overtook the previous owner of the card.
            Player newLongestRoadOwner = getPlayerWithLongestRoad(playerToLongestPathMap);
            playerWithCard.removeLongestRoadCard();
            newLongestRoadOwner.giveLongestRoadCard();
        }
    }

    /**
     * Helper method to tell us if anybody has a road at minumum 5 in length
     *
     * @param playerToLongestPathMap
     * @return
     */
    private boolean somebodyHasLongEnoughRoad(final HashMap<Player, Integer> playerToLongestPathMap) {
        Boolean somebodyIsAbove5 = false;
        for (Entry<Player, Integer> entry: playerToLongestPathMap.entrySet()) {
            if (entry.getValue() >= MIN_ROADS_FOR_CARD) {
                somebodyIsAbove5 = true;
            }
        }

        return somebodyIsAbove5;
    }

    /**
     * Helper method that looks throught the given map and returns the player with the longest path
     *
     * @param playerToLongestPathMap
     * @return
     */
    private Player getPlayerWithLongestRoad(final HashMap<Player, Integer> playerToLongestPathMap) {

        Player playerWithLongestRoad = null;
        int longestRoad = MIN_ROADS_FOR_CARD;
        for (Entry<Player, Integer> entry: playerToLongestPathMap.entrySet()) {
            if (entry.getValue() >= longestRoad) {
                playerWithLongestRoad = entry.getKey();
                longestRoad = entry.getValue();
            }
        }

        return playerWithLongestRoad;
    }

    /**
     * Helper method that tells if two players have the same max path length
     * @param players
     * @param playerToLongestPathMap
     * @return
     */
    private boolean twoPlayersTiedForLongestRoad(final Set<Player> players, final HashMap<Player, Integer> playerToLongestPathMap) {
        for (Player player1: players) {
            for (Player player2: players) { // test all combination of players
                if (playerToLongestPathMap.get(player1) >= MIN_ROADS_FOR_CARD) { // only care about ties if the tie is eligible for longes road
                    if (player1 != player2) { // don't want to compare the same object would defeat the purpose of this check
                        if (playerToLongestPathMap.get(player1).equals(playerToLongestPathMap.get(player2))) { // both players got the same max road length
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Helper method that finds all vertexes that are part of the players road graph and not
     * owned by an enemy
     */
    private Set<Vertex> getOrigins(final Player player) {
        HashSet<Vertex> origins = new HashSet<Vertex>();

        for (Road road: this.roads) { // note we don't have to enfore that this be a road adjacent to the player because that is checked later
            for (Vertex vertex: road.getAdjacentVertexes()) {
                if (!vertex.ownedByEnemyOf(player)) {
                    origins.add(vertex);
                }
            }
        }

        return origins;
    }

    /**
     * Helper method that returns the set of player in the game by extracting the owners of the roads
     * held in this graph
     *
     * @return set of players in the game
     */
    private Set<Player> getPlayers() {

        HashSet<Player> players = new HashSet<Player>();
        for (Road road: this.roads) {
            Player potentialNewPlayer = road.getOwner();

            if (potentialNewPlayer != null) {
                players.add(potentialNewPlayer);
            }
        }

        return players;
    }
}
