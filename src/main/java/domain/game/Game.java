package domain.game;

import java.io.File;
import java.io.IOException;
import java.time.temporal.ValueRange;
import java.util.*;
import java.util.Map.Entry;

import domain.bank.Bank;
import domain.bank.Resource;
import domain.building.DistrictType;
import domain.devcarddeck.DevCard;
import domain.devcarddeck.DevelopmentCardDeck;
import domain.devcarddeck.EmptyDevCardDeckException;
import domain.gameboard.GameBoard;
import domain.gameboard.Terrain;
import domain.gameboard.Tile;
import domain.player.Player;
import data.*;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import domain.graphs.Road;
import domain.graphs.Vertex;
import domain.graphs.GameboardGraph;

public class Game implements Restorable {
    private final GameBoard gameBoard;
    private final GameboardGraph gameboardGraph;
    private final DevelopmentCardDeck deck;
    private final Bank bank;
    private boolean setup = true;
    private static final int ROBBER_NUM = 7;
    private final ValueRange range = java.time.temporal.ValueRange.of(0, 18);

     /**
     * Creates a new game object
     * @param gb the gameBoard
     * @param vg the vertexGraph
     * @param devDeck the development card deck
     */
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public Game(final GameBoard gb, final GameboardGraph vg, final DevelopmentCardDeck devDeck, final Bank resourceBank) {
        this.gameBoard = gb;
        this.gameboardGraph = vg;
        this.deck = devDeck;
        this.bank = resourceBank;
    }

    /**
     * Tries to place a settlement
     * @param vertex the settlement location
     * @param player the player placing the vertex
     * @throws InvalidPlacementException if the placement is invalid
     * @throws NotEnoughResourcesException if it is not setup and the player cant afford the settlement
     */
    public void placeSettlement(final int vertex, final Player player) throws InvalidPlacementException, NotEnoughResourcesException {
        Vertex v = gameboardGraph.getVertex(vertex);
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
        this.gameboardGraph.giveLongestRoadCard();
    }

    public Set<Integer> getBuildableVertexes(final Player player) {
        Set<Integer> buildableVertexes = gameboardGraph.getBuildableVertexes();
        if (setup) {
            return buildableVertexes;
        }

        Set<Integer> accessibleVertexes = new HashSet<>();
        for (Integer v : buildableVertexes) {
            if (gameboardGraph.getVertex(v).isAdjacentToFriendlyRoad(player)) {
                accessibleVertexes.add(v);
            }
        }

        return accessibleVertexes;
    }

    /**
     * Tries to place a settlement
     * @param road the road location
     * @param vertex the adjacent settlement
     * @param player the player placing the vertex
     * @throws InvalidPlacementException if the placement is invalid
     * @throws NotEnoughResourcesException if it is not setup and the player cant afford the road
     */
    public void placeRoad(final int road, final int vertex, final Player player) throws InvalidPlacementException, NotEnoughResourcesException {
        Vertex v =  gameboardGraph.getVertex(vertex);
        Road r = gameboardGraph.getRoad(road);
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
        this.gameboardGraph.giveLongestRoadCard();
    }

    public Set<Integer> getBuildableRoads(final Player player, final int lastPlacedVertex) {

        if (setup) {
            return gameboardGraph.getBuildableRoadsSetup(lastPlacedVertex);
        } else {
          return gameboardGraph.getBuildableRoadsRegularPlay(player);
        }
    }

    /**
     * Distributes resources to the player based on the setup state and a condition
     * this condition is interpreted as a vertex if in setup or a die value if not in setup
     * @param player the player
     * @param condition the vertex or die value
     */
    public void distributeResources(final Player player, final int condition) {
       Resource[] unadjustedHarvest = (setup) ? resourcesFromVertex(player, condition) : resourcesFromDie(player, condition);
       Resource[] adjustedHarvest = player.harvestBooster.getAdjustedHarvest(unadjustedHarvest);

       if (player.addResources(adjustedHarvest)) {
           bank.removeResources(adjustedHarvest);
       }
    }

    /**
     * Calculates resources from vertex
     * @param player the player
     * @param vertex the vertex just built to get resources from
     * @return Resource[] an array of resource enums to give to player
     */
    Resource[] resourcesFromVertex(final Player player, final int vertex) {
        ArrayList<Resource> resources = new ArrayList<>();
        //Checks what tiles the last placed vertex gets resources from
        for (Tile tile : this.gameBoard.getTiles()) {
            List<Integer> tileVertexes = tile.getVertexIDs();
            if (tile.getTerrain() != Terrain.DESERT) {
                for (int i = 0; i < tile.getVertexIDs().size(); i++) {
                    if (vertex == tileVertexes.get(i) && gameboardGraph.getVertex(tileVertexes.get(i)).getOwner() == player) {
                        resources.add(tile.getResource());
                    }
                }
            }
        }
        return resources.toArray(new Resource[0]);
    }

    /**
     * Calculates resources based on the current die value
     * @param player the player
     * @param die the die value just rolled
     * @return Resource[] an array of resource enums to give to player
     */
    Resource[] resourcesFromDie(final Player player, final int die) {
        ArrayList<Resource> resources = new ArrayList<>();
        if (die == ROBBER_NUM) {
            return new Resource[0];
        }
        for (Tile tile : this.gameBoard.getTiles()) {
            List<Integer> tileVertexes = tile.getVertexIDs();
            if (tile.getTerrain() != Terrain.DESERT && tile.getDieNumber() == die) {
                for (int i = 0; i < tile.getVertexIDs().size(); i++) {
                    Vertex curVertex = gameboardGraph.getVertex(tileVertexes.get(i));
                    if (curVertex.getOwner() == player) {
                        for (int numResources = 0; numResources < curVertex.getYield(tile.getResource()); numResources++) {
                            resources.add(tile.getResource());
                        }
                    }
                }
            }
        }

        return resources.toArray(new Resource[0]);
    }

    public void updateLoanDueTimes(final Player currentPlayer) {
        this.bank.updateLoanDueTimes(currentPlayer);
    }

    public void takeOutLoan(final Player currentPlayer, final Resource[] resources) throws NotEnoughResourcesException {
        this.bank.takeOutLoan(currentPlayer, resources);
    }

    /**
     * Tries to Upgrade a settlement from a player and a vertexID
     */
    public void upgradeSettlement(final Player player, final int vertexId) throws InvalidPlacementException, NotEnoughResourcesException {
        Vertex vertex = gameboardGraph.getVertex(vertexId);
        if (!vertex.isUpgradableBy(player)) {
            throw new InvalidPlacementException();
        }
        if (!player.canUpgradeSettlementToCity()) {
            throw new NotEnoughResourcesException();
        }
        vertex.upgradeToCity(player);
    }

    /**
     * Builds a district on an established building on the given vertex if the player owns the building
     * and a district doesn't already exist. Each district provides its own unique +3 resource bonus
     * to resource yields
     * @param player the player building the district. Must already own a building on the vertex
     * @param vertexId the vertex to build a district on
     * @param type the district type to build
     */
    public void buildDistrictOnVertex(final Player player, final int vertexId, final DistrictType type) throws NotEnoughResourcesException, InvalidPlacementException {
        if (!player.hand.removeResources(type.getDistrictCost())) {
            throw new NotEnoughResourcesException();
        }
        Vertex vertex = gameboardGraph.getVertex(vertexId);
        try {
            vertex.buildDistrict(player, type);
        } catch (InvalidPlacementException e) {
            player.addResources(type.districtCost);
            throw new InvalidPlacementException();
        }
    }

    public DistrictType getDistrictTypeForVertex(final int vertex) {
        Vertex v = gameboardGraph.getVertex(vertex);
        return v.getBuilding().getDistrict();
    }

    /**
     * Allows the player to buy a dev card if they have enough resources
     */
    public void buyDevCard(final Player player) throws NotEnoughResourcesException, EmptyDevCardDeckException {
        DevCard card;
        card = this.deck.draw();
        if (!player.purchaseDevCard(card)) {
            this.deck.returnToDeck(card);
            throw new NotEnoughResourcesException();
        }
    }

    /**
     * Transitions the game from setup to regular play mode
     */
    public void endSetup() {
        this.setup = false;
    }

    public boolean getIsSetup() {
        return this.setup;
    }

    /**
     * Tries to drop cards from players based on the input hashmap
     * @throws IllegalArgumentException when player doesn't have the corresponding resources
     */
    public void dropCards(final HashMap<Player, Resource[]> resourceMap) throws IllegalArgumentException {
        for (Entry<Player, Resource[]> entry : resourceMap.entrySet()) {
            Player player = entry.getKey();
            Resource[] toRemove = entry.getValue();
            if (toRemove.length != 0) {
                if (!player.hand.removeResources(toRemove)) {
                    throw new IllegalArgumentException("DropCards was called on player with not enough cards - synchronization issue");
                }
            }
        }
    }

    /**
     * Tries to move the robber to the tile with the given ID
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
     */
    @SuppressFBWarnings("PREDICTABLE_RANDOM")
    public void stealFromPlayer(final Player robber, final Player robbed) throws NotEnoughResourcesException {
        if (robbed.hand.getResourceCount() == 0) {
            throw new NotEnoughResourcesException();
        }
        //Get all possible resources
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
     */
    public Player[] getPlayersFromTile(final int tileID) throws IllegalArgumentException {
        if (!range.isValidIntValue(tileID)) {
            throw new IllegalArgumentException("Bad tile num");
        }
        ArrayList<Player> pList = new ArrayList<>();
        for (int vertex : gameBoard.getTileVertexIDs(tileID)) {
            Vertex location = gameboardGraph.getVertex(vertex);
            if (location.isOccupied()) {
                Player owner = location.getOwner();
                if (!pList.contains(owner)) {
                    pList.add(owner);
                }
            }
        }
        return pList.toArray(new Player[0]);
    }

    public Set<Integer> getValidRobberSpots(final Player player) {
        Set<Integer> robberSpots = new HashSet<>();
        for (Tile tile : gameBoard.getTiles()) {
            for (Integer v : tile.getVertexIDs()) {
                if (gameboardGraph.getVertex(v).ownedByEnemyOf(player)) {
                    robberSpots.add(tile.getTileNumber());
                }
            }
        }

        return robberSpots;
    }

    /**
     * Method that plays tries to play the monopoly card
     */
    public void playMonopoly(final Player robber, final Player[] playersToRob, final Resource resourceToRob) throws CardNotPlayableException {
        if (!robber.useDevCard(DevCard.MONOPOLY)) {
            throw new CardNotPlayableException();
        }
        for (Player p : playersToRob) {
            int amount = p.hand.getResourceCount(resourceToRob);
            if (amount != 0) {
                p.hand.removeResource(resourceToRob, amount);
                robber.hand.addResource(resourceToRob, amount);
            }
        }
    }

    /**
     * Method that tries to play the year of plenty card
     */
    public void playYearOfPlenty(final Player player, final Resource resource1, final Resource resource2) throws NotEnoughResourcesException, CardNotPlayableException {
        if (!player.useDevCard(DevCard.PLENTY)) {
            throw new CardNotPlayableException();
        }
        int amount1 = bank.getResourceAmount(resource1);
        int amount2 = bank.getResourceAmount(resource2);
        if (amount1 == 0 || amount2 == 0) {
            player.hand.addDevelopmentCard(DevCard.PLENTY); // re add the dev card because it wasn't used
            throw new NotEnoughResourcesException();
        }
        Resource[] resources = {resource1, resource2};
        bank.removeResources(resources);
        player.addResources(resources);

    }

    // -----------------------------------
    //
    // Restorable implementation
    //
    // -----------------------------------

    public class GameMemento implements Memento {

        // simple fields
        private final boolean setup;

        // sub mementos
        private final Memento gameBoardMemento;
        private final Memento gameboardGraphMemento;
        private final Memento deckMemento;
        private final Memento bankMemento;

        // Storage Constants
        private static final String TARGET_FILE_NAME = "game.txt";
        private static final String GAME_BOARD_SUBFOLDER_NAME = "GameBoard";
        private static final String GAMEBOARD_GRAPH_SUBFOLDER_NAME = "Graphs";
        private static final String DECK_SUBFOLDER_NAME = "Deck";
        private static final String BANK_SUBFOLDER_NAME = "Bank";

        // Field Keys
        private static final String SETUP = "Setup";

        private GameMemento() {
            // simple fields
            this.setup = Game.this.setup;

            // sub mementos
            this.gameBoardMemento = Game.this.gameBoard.createMemento();
            this.gameboardGraphMemento = Game.this.gameboardGraph.createMemento();
            this.deckMemento = Game.this.deck.createMemento();
            this.bankMemento = Game.this.bank.createMemento();
        }

        @SuppressFBWarnings("EI_EXPOSE_REP2")
        public GameMemento(final File folder) {
            // Create a MementoReader for reading memento data
            MementoReader reader = new MementoReader(folder, TARGET_FILE_NAME);

            // Read simple fields from the file
            this.setup = Boolean.parseBoolean(reader.readField(SETUP));

            // Read sub-mementos from the appropriate sub-folders
            File gameBoardSubFolder = reader.getSubFolder(GAME_BOARD_SUBFOLDER_NAME);
            this.gameBoardMemento = Game.this.gameBoard.new GameBoardMemento(gameBoardSubFolder);

            File gameboardGraphSubFolder = reader.getSubFolder(GAMEBOARD_GRAPH_SUBFOLDER_NAME);
            this.gameboardGraphMemento = Game.this.gameboardGraph.new GameboardGraphMemento(gameboardGraphSubFolder);

            File deckSubFolder = reader.getSubFolder(DECK_SUBFOLDER_NAME);
            this.deckMemento = Game.this.deck.new DevCardDeckMemento(deckSubFolder);

            File bankSubFolder = reader.getSubFolder(BANK_SUBFOLDER_NAME);
            this.bankMemento = Game.this.bank.new BankMemento(bankSubFolder);
        }

        public void save(final File folder) throws IOException {
            // Create a MementoWriter for writing memento data
            MementoWriter writer = new MementoWriter(folder, TARGET_FILE_NAME);

            // Write simple fields to the file
            writer.writeField(SETUP, Boolean.toString(setup));

            // Save sub mementos' state
            File gameBoardSubFolder = writer.getSubFolder(GAME_BOARD_SUBFOLDER_NAME);
            gameBoardMemento.save(gameBoardSubFolder);

            File gameboardGraphSubFolder = writer.getSubFolder(GAMEBOARD_GRAPH_SUBFOLDER_NAME);
            gameboardGraphMemento.save(gameboardGraphSubFolder);

            File deckSubFolder = writer.getSubFolder(DECK_SUBFOLDER_NAME);
            deckMemento.save(deckSubFolder);

            File bankSubFolder = writer.getSubFolder(BANK_SUBFOLDER_NAME);
            bankMemento.save(bankSubFolder);
        }

        public void restore() {
            // Restore simple fields
            Game.this.setup = this.setup;

            // Restore sub mementos
            gameBoardMemento.restore();
            gameboardGraphMemento.restore();
            deckMemento.restore();
            bankMemento.restore();
        }
    }

    @Override
    public Memento createMemento() {
        return new GameMemento();
    }
}

