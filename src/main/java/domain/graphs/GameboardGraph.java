package domain.graphs;

import java.io.File;
import java.io.IOException;
import java.util.*;

import data.*;
import domain.player.Player;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import domain.game.GameType;
import domain.bank.Resource;

/**
 * Graph of the locations on the map
 */
public class GameboardGraph implements Restorable {

    public static final int NUM_VERTICES = 54;
    public static final int NUM_PORTS = 9;
    public static final int NUM_ROADS = 72;
    private static final Integer MIN_ROADS_FOR_CARD = 5;
    private final Resource[] portResources = {Resource.ANY, Resource.GRAIN, Resource.WOOL, Resource.ANY, Resource.ORE, Resource.ANY, Resource.ANY, Resource.BRICK, Resource.LUMBER};
    private final Vertex[] vertexes;
    private final Port[] ports;
    private final Road[] roads;

    /**
     * Creates a new vertex graph with the default number of vertices.
     * <p>
     * Note: Adjacency must be initialized separately by calling
     * the appropriate initializer methods
     */
    public GameboardGraph(final GameType gameType) {
        this.vertexes = new Vertex[NUM_VERTICES];
        this.ports = new Port[NUM_PORTS];

        for (int i = 0; i < NUM_VERTICES; i++) {
            this.vertexes[i] = new Vertex(i);
        }

        if (gameType == GameType.Advanced) {
            Collections.shuffle(Arrays.asList(this.portResources));
        }

        for (int i = 0; i < NUM_PORTS; i++) {
            this.ports[i] = new Port(i, portResources[i]);
        }

        this.roads = new Road[NUM_ROADS];

        for (int i = 0; i < NUM_ROADS; i++) {
            this.roads[i] = new Road(i);
        }
    }

    /**
     * Returns the vertex with the specified ID.
     * @param locationId the ID of the vertex to retrieve
     * @return the vertex with the specified ID
     * @throws IllegalArgumentException if the specified ID is not within the valid
     *                                  range
     */
    public Vertex getVertex(final Integer locationId) {

        if (locationId >= NUM_VERTICES || locationId < 0) {
            throw new IllegalArgumentException("Invalid locationId; Try[0, 53]");
        }

        return this.vertexes[locationId];
    }

    /**
     * Returns the Port with the specified ID.
     * @param locationId the ID of the Port to retrieve
     * @return the Port with the specified ID
     * @throws IllegalArgumentException if the specified ID is not within the valid
     *                                  range
     */
    public Port getPort(final int locationId) {

        if (locationId >= NUM_PORTS || locationId < 0) {
            throw new IllegalArgumentException("Invalid locationId; Try[0, 8]");
        }

        return this.ports[locationId];
    }

    /**
     * Returns the order of the ports' resources.
     * <p>
     * Mainly used to help determine in testing if the ports were shuffled.
     *
     * @return and array of the resource of the ports in order according to the game diagram
     */
    public Resource[] getOrder() {

        Resource[] resourceOrder = new Resource[NUM_PORTS];
        for (int i = 0; i < NUM_PORTS; i++) {
            resourceOrder[i] = this.ports[i].getResource();
        }

        return resourceOrder;
    }

    /**
     * Gets the road with the specified location ID.
     *
     * @param locationId the locationId of the Road to get
     * @return the road with the specified locationID
     * @throws IllegalArgumentException if the location ID is out of bounds
     */
    public Road getRoad(final Integer locationId) {
        if (locationId >= NUM_ROADS || locationId < 0) {
            throw new IllegalArgumentException("LocationId out of bounds: Try [0, 72]");
        }

        return this.roads[locationId];
    }

    // ----------------------------------------------------------------------------
    //
    // Longest road stuff
    //
    // ----------------------------------------------------------------------------

    /**
     * Function that calculates the longest path given the starting road and origin vertex of the path.
     */
    public int getLongestPath(final Road startingRoad, final Player player, final HashSet<HashSet<Road>> paths, final HashSet<Road> visitedRoads, final Vertex origin) {
        if (startingRoad.getOwner() != player) {
            throw new IllegalStateException("Road does not own player, check calling method");
        }

        if (origin.ownedByEnemyOf(player)) {
            return -1; // stop here before we add ourselves to the path because we are blocked off
        }

        // add ourselves to the current path and the path to the list of paths
        visitedRoads.add(startingRoad);
        paths.add(visitedRoads);

        Vertex tail = startingRoad.getTail(origin);
        for (Road neighbor: tail.getAdjacentRoads()) { // now we only have 2 options to branch off of
            if (neighbor.getOwner() == player && !visitedRoads.contains(neighbor)) {

                HashSet<Road> newPath = new HashSet<>(visitedRoads);
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
     * <p>
     * Note: Doesn't really give a player the card. But sets a flag in the player indicating it owns it and
     * also increment and decrements victory points accordingly.
     * <p>
     * Also Note: This method is assuming it is called after a Road or Vertex was built. This is to ensure
     * that the special cases of transfer-ship are correctly set up.
     */
    public void giveLongestRoadCard() {
        // Code that maps players to their longest paths
        Set<Player> players = this.getPlayers();
        HashMap<Player, Integer> playerToLongestPathMap = new HashMap<>();
        for (Player player: players) {

            Set<Vertex> origins = this.getOrigins(player); // get starting points for our searches

            int longest = 0;
            for (Vertex origin: origins) { // loop though all the path origin vertexes

                // Note: looping though all of these is fine because only 1 will be the longest and if it is a vertex not connected
                // to anything then it simply be skipped
                for (Road startingRoad: origin.getAdjacentRoads()) {
                    if (startingRoad.getOwner() == player) { // make sure we only start at Roads owned by the player
                        int pathLength = this.getLongestPath(startingRoad, player, new HashSet<>(), new HashSet<>(), origin);
                        longest = Math.max(pathLength, longest);
                    }
                }
            }

            playerToLongestPathMap.put(player, longest); // add the longest path to the map of players to their longest path
        }

        // ----------------------------------------------------------------------------
        //
        // Code that uses the map to distribute the longest Road card
        //
        // ----------------------------------------------------------------------------

        // find out who currently has the longest road before transferring
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
        if (tempPlayerWithLongestRoad == playerWithCard) { // we know the player who originally had it was not overtaken
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
     * Helper method to tell us if anybody has a road at minimum 5 in length
     */
    private boolean somebodyHasLongEnoughRoad(final HashMap<Player, Integer> playerToLongestPathMap) {
        boolean somebodyIsAbove5 = false;
        for (Map.Entry<Player, Integer> entry: playerToLongestPathMap.entrySet()) {
            if (entry.getValue() >= MIN_ROADS_FOR_CARD) {
                somebodyIsAbove5 = true;
                break;
            }
        }

        return somebodyIsAbove5;
    }

    /**
     * Helper method that looks through the given map and returns the player with the longest path
     */
    private Player getPlayerWithLongestRoad(final HashMap<Player, Integer> playerToLongestPathMap) {

        Player playerWithLongestRoad = null;
        int longestRoad = MIN_ROADS_FOR_CARD;
        for (Map.Entry<Player, Integer> entry: playerToLongestPathMap.entrySet()) {
            if (entry.getValue() >= longestRoad) {
                playerWithLongestRoad = entry.getKey();
                longestRoad = entry.getValue();
            }
        }

        return playerWithLongestRoad;
    }

    /**
     * Helper method that tells if two players have the same max path length
     */
    private boolean twoPlayersTiedForLongestRoad(final Set<Player> players, final HashMap<Player, Integer> playerToLongestPathMap) {
        for (Player player1: players) {
            for (Player player2: players) { // test all combination of players
                if (playerToLongestPathMap.get(player1) >= MIN_ROADS_FOR_CARD) { // only care about ties if the tie is eligible for longest road
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
        HashSet<Vertex> origins = new HashSet<>();

        for (Road road: this.roads) { // note we don't have to enforce that this be a road adjacent to the player because that is checked later
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

        HashSet<Player> players = new HashSet<>();
        for (Road road: this.roads) {
            Player potentialNewPlayer = road.getOwner();

            if (potentialNewPlayer != null) {
                players.add(potentialNewPlayer);
            }
        }

        return players;
    }

    // -----------------------------------
    //
    // Restorable implementation
    //
    // -----------------------------------

    public class GameboardGraphMemento implements Memento {

        // simple fields
        private final Resource[] portResources;

        // sub mementos
        private final Memento[] vertexMementos;
        private final Memento[] portMementos;
        private final Memento[] roadMementos;


        // Storage Constants
        private static final String TARGET_FILE_NAME = "vertexgraph.txt";
        private static final String VERTEX_SUBFOLDER_PREFIX = "Vertex";
        private static final String PORT_SUBFOLDER_PREFIX = "Port";
        private static final String ROAD_SUBFOLDER_PREFIX = "Road";


        // Field Keys
        private static final String PORT_RESOURCES = "PortResources";

        private GameboardGraphMemento() {
            // simple fields
            this.portResources = Arrays.copyOf(GameboardGraph.this.portResources, GameboardGraph.this.portResources.length);

            // sub mementos
            this.vertexMementos = new Memento[GameboardGraph.this.vertexes.length];
            for (int i = 0; i < GameboardGraph.this.vertexes.length; i++) {
                this.vertexMementos[i] = GameboardGraph.this.vertexes[i].createMemento();
            }

            this.portMementos = new Memento[GameboardGraph.this.ports.length];
            for (int i = 0; i < GameboardGraph.this.ports.length; i++) {
                this.portMementos[i] = GameboardGraph.this.ports[i].createMemento();
            }

            this.roadMementos = new Memento[GameboardGraph.this.roads.length];
            for (int i = 0; i < GameboardGraph.this.roads.length; i++) {
                this.roadMementos[i] = GameboardGraph.this.roads[i].createMemento();
            }
        }

        @SuppressFBWarnings("EI_EXPOSE_REP2")
        public GameboardGraphMemento(final File folder) {
            // Create a MementoReader for reading memento data
            MementoReader reader = new MementoReader(folder, TARGET_FILE_NAME);

            // Read simple fields from the file
            this.portResources = parsePortResources(reader.readField(PORT_RESOURCES));

            // Read sub-mementos from the appropriate sub-folders
            this.vertexMementos = new Memento[GameboardGraph.this.vertexes.length];
            for (int i = 0; i < this.vertexMementos.length; i++) {
                File vertexSubFolder = reader.getSubFolder(VERTEX_SUBFOLDER_PREFIX + i);
                this.vertexMementos[i] = GameboardGraph.this.vertexes[i].new VertexMemento(vertexSubFolder);
            }

            this.portMementos = new Memento[GameboardGraph.this.ports.length];
            for (int i = 0; i < this.portMementos.length; i++) {
                File portSubFolder = reader.getSubFolder(PORT_SUBFOLDER_PREFIX + i);
                this.portMementos[i] = GameboardGraph.this.ports[i].new PortMemento(portSubFolder);
            }

            this.roadMementos = new Memento[GameboardGraph.this.roads.length];
            for (int i = 0; i < this.roadMementos.length; i++) {
                File roadSubFolder = reader.getSubFolder(ROAD_SUBFOLDER_PREFIX + i);
                this.roadMementos[i] = GameboardGraph.this.roads[i].new RoadMemento(roadSubFolder);
            }
        }

        // Helper method to parse port resources
        private Resource[] parsePortResources(final String portResourcesString) {
            String[] resourceNames = portResourcesString.substring(1, portResourcesString.length() - 1).split(", ");

            Resource[] resources = new Resource[resourceNames.length];
            for (int i = 0; i < resourceNames.length; i++) {
                resources[i] = Resource.valueOf(resourceNames[i].trim());
            }

            return resources;
        }

        public void save(final File folder) throws IOException {
            // Create a MementoWriter for writing memento data
            MementoWriter writer = new MementoWriter(folder, TARGET_FILE_NAME);

            // Write simple fields to the file
            writer.writeField(PORT_RESOURCES, Arrays.toString(portResources));

            // Save sub mementos' state
            for (int i = 0; i < vertexMementos.length; i++) {
                // Create a subfolder for each vertex's memento
                File vertexSubFolder = writer.getSubFolder(VERTEX_SUBFOLDER_PREFIX + i);
                vertexMementos[i].save(vertexSubFolder);
            }

            for (int i = 0; i < portMementos.length; i++) {
                // Create a subfolder for each port's memento
                File portSubFolder = writer.getSubFolder(PORT_SUBFOLDER_PREFIX + i);
                portMementos[i].save(portSubFolder);
            }

            for (int i = 0; i < roadMementos.length; i++) {
                // Create a subfolder for each road's memento
                File roadSubFolder = writer.getSubFolder(ROAD_SUBFOLDER_PREFIX + i);
                roadMementos[i].save(roadSubFolder);
            }
        }

        public void restore() {
            // Restore simple fields
            System.arraycopy(this.portResources, 0, GameboardGraph.this.portResources, 0, this.portResources.length);

            // Restore sub mementos
            for (Memento vertexMemento : vertexMementos) {
                vertexMemento.restore();
            }

            for (Memento portMemento : portMementos) {
                portMemento.restore();
            }

            for (Memento roadMemento : roadMementos) {
                roadMemento.restore();
            }
        }
    }

    @Override
    public Memento createMemento() {
        return new GameboardGraphMemento();
    }
}
