package gamedatastructures;

import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import graphs.Road;
import graphs.RoadGraph;
import graphs.Vertex;
import graphs.VertexGraph;

public class Game {
    private final GameBoard gameBoard;
    private final VertexGraph vertexes;
    private final RoadGraph roads;
    private final DevelopmentCardDeck deck;
    boolean setup = true;
    private static final int ROBBERNUM = 7;
    private final ValueRange range = java.time.temporal.ValueRange.of(0, 18);
     /**
     * Creates a new game object
     * @param gb the gameBoard
     * @param vg the vertexGraph
     * @param rg the roadGraph
     * @param deck the devcard deck
     */
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public Game(final GameBoard gb, final VertexGraph vg, final RoadGraph rg, final DevelopmentCardDeck dcd) {
        this.gameBoard = gb;
        this.vertexes = vg;
        this.roads = rg;
        this.deck = dcd;
    }
     /**
     * Tries to place a settlement
     * @param vertex the settlement location
     * @param player the player placing the vertex
     * @throws InvalidPlacementException if the placement is invalid
     * @throws NotEnoughResourcesException if its not setup and the player cant afford the settlement
     */
    public void placeSettlement(final int vertex, final Player player) throws InvalidPlacementException, NotEnoughResourcesException {
        Vertex v = vertexes.getVertex(vertex);
        if (!v.isBuildable()) {
            throw new InvalidPlacementException();
        }

        if (!setup && !v.isAdjacentToFriendlyRoad(player)) {
            throw new InvalidPlacementException();
        }

        if (!setup && !player.purchaseSettlement()) {
            throw new NotEnoughResourcesException();
        }

        if (setup) { // need this one because the other one won't be used during setup
            player.placeSettlementSetup();
        }

        v.build(player);

        // tell the road to figure out who needs he card, we'll test this later
        this.roads.giveLongestRoadCard();
    }
      /**
     * Tries to place a settlement
     * @param road the road location
     * @param vertex the adjacent settlement
     * @param player the player placing the vertex
     * @throws InvalidPlacementException if the placement is invalid
     * @throws NotEnoughResourcesException if its not setup and the player cant afford the road
     */
    public void placeRoad(final int road, final int vertex, final Player player) throws InvalidPlacementException, NotEnoughResourcesException {
        Vertex v =  vertexes.getVertex(vertex);
        Road r = roads.getRoad(road);
        if (setup) {
            if (!r.isBuildable() || !r.isAdjacentTo(v)) {
                throw new InvalidPlacementException();
            }
            r.setOwner(player);
            player.placeRoadSetup();
        } else {
            if (!r.isBuildableBy(player)) {
                throw new InvalidPlacementException();
            }
            if (!player.purchaseRoad()) {
                throw new NotEnoughResourcesException();
            }
            r.setOwner(player);
        }

        // tell the road to figure out who needs he card, we'll test this later
        this.roads.giveLongestRoadCard();
    }
     /**
     * Distributes resources to the player based on the setup state and a condition
     * this condition is interperted as a vertex if in setup or a die value if not in setup
     * @param player the player
     * @param condition the vertex or die value
     */
    public void distributeResources(final Player player, final int condition) {
       player.hand.addResources((setup) ? resourcesFromVertex(player, condition) : resourcesFromDie(player, condition));
    }
    /**
     * Calculates resources from vertex
     * @param player the player
     * @param vertex the vertex just built to get resources from
     * @return Resource[] an array of resource enums to give to player
     */
    Resource[] resourcesFromVertex(final Player player, final int vertex) {
        ArrayList<Resource> resources = new ArrayList<Resource>();
        //Checks what tiles the last placed vertex gets resoures from
        for (Tile tile : this.gameBoard.getTiles()) {
            int[] tileVertexes = tile.getVertexIDs();
            if (tile.getTerrain() != Terrain.DESERT) {
                for (int i = 0; i < tile.getVertexIDs().length; i++) {
                    if (vertex == tileVertexes[i] && vertexes.getVertex(tileVertexes[i]).getOwner() == player) {
                        resources.add(tile.getResource());
                    }
                }
            }
        }
        return resources.toArray(new Resource[resources.size()]);
    }
    /**
     * Calculates resources based on the current die value
     * @param player the player
     * @param die the die value just rolled
     * @return Resource[] an array of resource enums to give to player
     */
    Resource[] resourcesFromDie(final Player player, final int die) {
        ArrayList<Resource> resources = new ArrayList<Resource>();
        if (die == ROBBERNUM) {
            return new Resource[0];
        }
        for (Tile tile : this.gameBoard.getTiles()) {
            int[] tileVertexes = tile.getVertexIDs();
            if (tile.getTerrain() != Terrain.DESERT && tile.getDieNumber() == die) {
                for (int i = 0; i < tile.getVertexIDs().length; i++) {
                    Vertex curVertex = vertexes.getVertex(tileVertexes[i]);
                    // TODO: Handle figuring out how much to hand out of a resource in Building
                    if (curVertex.getOwner() == player) {
                        if (curVertex.getIsCity()) {
                            resources.add(tile.getResource());
                        }
                        resources.add(tile.getResource());
                    }
                }
            }
        }
        return resources.toArray(new Resource[resources.size()]);
    }
    /**
     * Tries to Upgrade a settlement from a player and a vertexID
     * @param mockPlayer
     * @param vertexId
     */
    // TODO: Rename this to reflect being a city upgrade and change inner logic to use Building code
    public void upgradeSettlement(final Player player, final int vertexId) throws InvalidPlacementException, NotEnoughResourcesException {
        Vertex vertex = vertexes.getVertex(vertexId);
        if (!vertex.isUpgradableBy(player)) {
            throw new InvalidPlacementException();
        }
        if (!player.upgradeSettlementToCity()) {
            throw new NotEnoughResourcesException();
        }
        vertex.setIsCity(true);
    }

    // TODO: Add method here to add a location add-on to the building on the given vertex

    /**
     * Allows the player to buy a devcard if they have enough resources
     * @param player
     * @throws NotEnoughResourcesException
     * @throws EmptyDevCardDeckException
     */
    public void buyDevCard(final Player player) throws NotEnoughResourcesException, EmptyDevCardDeckException {
        DevCard card;
        try {
            card = this.deck.draw();
        } catch (EmptyDevCardDeckException e) {
            throw e;
        }
        if (!player.purchaseDevCard(card)) {
            this.deck.returnToDeck(card);
            throw new NotEnoughResourcesException();
        }
    }
    /**
     * Sets setuo to false
     * @return false
     */
    public boolean endSetup() {
        this.setup = false;
        return false;
    }

    public boolean getIsSetup() {
        return this.setup;
    }

    /**
     * Tries to drop cards from players based on the input hashmap
     * @param HashMap<Player,Resource[]> map
     * @throws IllegalArgumentException when player doesnt have the corresponding resources
     */
    public void dropCards(final HashMap<Player, Resource[]> map) throws IllegalArgumentException {
        for (Entry<Player, Resource[]> entry : map.entrySet()) {
            Player player = entry.getKey();
            Resource[] toRemove = entry.getValue();
            if (toRemove.length != 0) {
                if (!player.hand.removeResources(toRemove)) {
                    throw new IllegalArgumentException("DropCards was called on player with not enough cards - desync issue");
                }
            }
        }
    }
    /**
     * Tries to move the robber to the tile with the given ID
     * @param tileNum
     * @throws InvalidPlacementException
     */
    public void moveRobber(final int tileId) throws InvalidPlacementException {
        if (!range.isValidIntValue(tileId)) {
            throw new RuntimeException("Bad tile num");
        }
        Tile newTile = this.gameBoard.getTiles()[tileId];
        Tile curTile = this.gameBoard.getRobberTile();
        if (curTile == newTile) {
            throw new InvalidPlacementException();
        }
        newTile.setRobber(true);
        curTile.setRobber(false);
        gameBoard.setRobberTile(newTile);
    }
    /**
     * Allows the robber player to steal a resource from the robbed player given they have enough
     * @param robber
     * @param robbed
     * @throws NotEnoughResourcesException
     */
    @SuppressFBWarnings("PREDICTABLE_RANDOM")
    public void stealFromPlayer(final Player robber, final Player robbed) throws NotEnoughResourcesException {
        if (robbed.hand.getResourceCardCount() == 0) {
            throw new NotEnoughResourcesException();
        }
        //Get all posible resources
        Resource[] robbedHand = robbed.hand.getResourceTypes();
        //"Randomly" pick a resource
        int index = (int) (Math.random() * robbedHand.length);
        Resource stolen = robbedHand[index];
        //Adjust
        robbed.hand.removeResource(stolen, 1);
        robber.hand.addResource(stolen, 1);
    }
    /**
     * Gets the list of players who own a settlement on the given tile
     * @param tileID
     * @return
     * @throws IllegalArgumentException
     */
    public Player[] getPlayersFromTile(final int tileID) throws IllegalArgumentException {
        if (!range.isValidIntValue(tileID)) {
            throw new IllegalArgumentException("Bad tile num");
        }
        ArrayList<Player> pList = new ArrayList<Player>();
        for (int vertex : gameBoard.getTileVertexIDs(tileID)) {
            Vertex location = vertexes.getVertex(vertex);
            if (location.isOccupied()) {
                Player owner = location.getOwner();
                if (!pList.contains(owner)) {
                    pList.add(owner);
                }
            }
        }
        return pList.toArray(new Player[pList.size()]);
    }

    /**
     * Method that plays tries to play the monopoly card
     *
     * @param robber
     * @param playersToRob
     * @param resourceToRob
     * @throws CardNotPlayableException
     */
    public void playMonopoly(final Player robber, final Player[] playersToRob, final Resource resourceToRob) throws CardNotPlayableException {
        if (!robber.useDevCard(DevCard.MONOPOLY)) {
            throw new CardNotPlayableException();
        }
        for (Player p : playersToRob) {
            int amount = p.hand.getResourceCardAmount(resourceToRob);
            if (amount != 0) {
                p.hand.removeResource(resourceToRob, amount);
                robber.hand.addResource(resourceToRob, amount);
            }
        }
    }

    /**
     * Method that tries to play the year of plenty card
     *
     * @param mockedPlayer1
     * @param resource1
     * @param resource2
     * @throws NotEnoughResourcesException
     * @throws CardNotPlayableException
     */
    public void playYearOfPlenty(final Player player, final Resource resource1, final Resource resource2) throws NotEnoughResourcesException, CardNotPlayableException {
        if (!player.useDevCard(DevCard.PLENTY)) {
            throw new CardNotPlayableException();
        }
        Bank bank =  Bank.getInstance();
        int amount1 = bank.getResourceAmount(resource1);
        int amount2 = bank.getResourceAmount(resource2);
        if (amount1 == 0 || amount2 == 0) {
            player.hand.addDevelopmentCard(DevCard.PLENTY); // re add the dev card because it wasn't used
            throw new NotEnoughResourcesException();
        }
        Resource[] resources = {resource1, resource2};
        bank.removeResources(resources);
        player.hand.addResources(resources);

    }
}

