package domain.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import domain.bank.Resource;
import domain.building.DistrictType;
import domain.devcarddeck.DevCard;
import domain.devcarddeck.EmptyDevCardDeckException;
import domain.game.*;
import domain.player.BoostType;
import domain.player.Player;
import data.*;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import static domain.controller.GamePhase.SETUP;
import static domain.controller.GameState.*;
import static domain.controller.SuccessCode.UNDEFINED;

public class Controller implements Restorable {
    private final Game game;
    private Player currentPlayer;
    private GamePhase gamePhase;
    private GameState gameState;
    private final Player[] playerArr;
    private int currentPlayerNum;
    public int lastPlacedVertex; // Used to ensure road is placed next to new settlement in setup phase
    private boolean devCardsEnabled;

    private final Random random;

    // Lots of constants to set up the beginner game mode
    private static final Map<Integer, Integer[]> PLAYER_NUM_TO_STARTER_LOCATIONS;
    // here the array represents the starter locations given to a player when
    // the beginner game mode is selected. The entries are in the following order:
    // {1stSettlement, 1stRoad, 2ndSettlement, 2ndRoad}
    private static final Integer[] PLAYER_1_LOCATIONS = {35, 37, 19, 25};
    private static final Integer[] PLAYER_2_LOCATIONS = {13, 15, 42, 58};
    private static final Integer[] PLAYER_3_LOCATIONS = {44, 52, 40, 56};
    private static final Integer[] PLAYER_4_LOCATIONS = {10, 13, 29, 41};
    private static final int FIRST_SETTLEMENT_INDEX = 0;
    private static final int FIRST_ROAD_INDEX = 1;
    private static final int SECOND_SETTLEMENT_INDEX = 2;
    private static final int SECOND_ROAD_INDEX = 3;
    private static final int PLAYER_1_NUM = 1;
    private static final int PLAYER_2_NUM = 2;
    private static final int PLAYER_3_NUM = 3;
    private static final int PLAYER_4_NUM = 4;
    private static final Integer MIN_KNIGHTS_FOR_LARGEST_ARMY = 3;
    private static final int POINTS_FOR_WIN = 10;
    private static final int MIN_DIE = 1;
    private static final int MAX_DIE = 7;
    static {

        // Map the locations to the players
        // Make the map unmodifiable
        PLAYER_NUM_TO_STARTER_LOCATIONS = Map.of(PLAYER_1_NUM, PLAYER_1_LOCATIONS,
                                                PLAYER_2_NUM, PLAYER_2_LOCATIONS,
                                                PLAYER_3_NUM, PLAYER_3_LOCATIONS,
                                                PLAYER_4_NUM, PLAYER_4_LOCATIONS);
    }

    /**
     * Create a Controller for the game. And sets up the players so player 1
     * goes first by default.
     * <p>
     * Note: Does not initialize to a specific state, however the controller
     * states can be set manually to fix a specific game type.
     *
     * @param gameType, the type of game to play, Beginner will do the setup phase automatically
     * @param mainGame, the game object that calls various actions
     * @param players,  the player objects in the game
     */
    @SuppressFBWarnings({"EI_EXPOSE_REP2", "PREDICTABLE_RANDOM"})
    public Controller(final Game mainGame, final Player[] players, final GameType gameType) {
        this(mainGame, players, gameType, new Random());
    }

    /**
     * Separate Helper constructor only to be used for testing of mocked
     * randomness
     * @param randomGenerator, Random object that can be controlled externally
     */
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public Controller(final Game mainGame, final Player[] players, final GameType gameType, final Random randomGenerator) {
        this.game = mainGame;
        this.playerArr = players;
        this.currentPlayerNum = 0;
        this.currentPlayer = players[0];

        this.gamePhase = SETUP;
        this.gameState = FIRST_SETTLEMENT;
        this.devCardsEnabled = true;

        if (gameType == GameType.Beginner) {
            this.doBeginnerSetup();
        }

        this.random = randomGenerator;
    }

    /**
     * This method sets up the players' starter locations for a beginner game.
     * It does this by essentially running through an entire advanced game
     * setup phase automatically with hardcoded start location ids.
     */
    private void doBeginnerSetup() {
        while (gamePhase != GamePhase.REGULAR_PLAY) { // stop if the action fails or we finish
            Integer[] locations = PLAYER_NUM_TO_STARTER_LOCATIONS.get(currentPlayer.getPlayerNum());

            if (gameState == FIRST_SETTLEMENT) {
                this.clickedVertex(locations[FIRST_SETTLEMENT_INDEX]);
            } else if (gameState == GameState.FIRST_ROAD) {
                this.clickedRoad(locations[FIRST_ROAD_INDEX]);
            } else if (gameState == SECOND_SETTLEMENT) {
                this.clickedVertex(locations[SECOND_SETTLEMENT_INDEX]);
            } else {
                this.clickedRoad(locations[SECOND_ROAD_INDEX]);
            }
        }
    }

    // -------------------------------------------------------------------------------
    //
    // Clicked Vertex Related Methods:
    //
    // -------------------------------------------------------------------------------

    /**
     * This tries to complete a click on a vertex based on the current
     * state of the game.
     * <p>
     * Note: You must set the appropriate phase and states before calling this
     * method.
     * For Setup, the states will automatically update after clicks, during regular
     * play
     * this is mostly manual
     * <p>
     * Also Note: This may crash the game is states are set incorrectly, this is on
     * purpose.
     */
    public SuccessCode clickedVertex(final int vertexId) {
        SuccessCode result;
        if (gamePhase == SETUP) {
            result = clickedVertexSetup(vertexId, currentPlayer);
        } else {
            result = clickedVertexRegularPlay(vertexId, currentPlayer);
        }

        if (currentPlayer.getVictoryPoints() >= POINTS_FOR_WIN) {
            result = SuccessCode.GAME_WIN;
        }

        return result;
    }

    private SuccessCode clickedVertexRegularPlay(final int vertexId, final Player player) {

        return switch (gameState) {
            // normal cases
            case BUILD_SETTLEMENT -> clickedPlaceSettlement(vertexId, player);
            case UPGRADE_SETTLEMENT -> clickedUpgradeSettlement(vertexId, player);
            // bad cases
            case FIRST_ROAD, SECOND_ROAD, FIRST_SETTLEMENT, SECOND_SETTLEMENT ->
                    throw new IllegalStateException("Setup state appeared during regular play");

            default -> UNDEFINED;
        };
    }

    private SuccessCode clickedPlaceSettlement(final int vertexId, final Player player) {
        try {
            game.placeSettlement(vertexId, player);

            this.setState(GameState.DEFAULT);
            return SuccessCode.SUCCESS;
        } catch (InvalidPlacementException e) {
            return SuccessCode.INVALID_PLACEMENT;
        } catch (NotEnoughResourcesException e) {
            return SuccessCode.INSUFFICIENT_RESOURCES;
        }
    }

    private SuccessCode clickedUpgradeSettlement(final int vertexId, final Player player) {
        try {
            game.upgradeSettlement(player, vertexId);
            this.setState(GameState.DEFAULT);
            return SuccessCode.SUCCESS;
        } catch (NotEnoughResourcesException e) {
            return SuccessCode.INSUFFICIENT_RESOURCES;
        } catch (InvalidPlacementException e) {
            return SuccessCode.INVALID_PLACEMENT;
        }
    }

    /**
     * This tries to complete a click on a vertex, based on the current
     * state of the game, assuming the game is currently in the setup phase.
     */
    private SuccessCode clickedVertexSetup(final int vertexId, final Player player) {
        if (gameState == FIRST_SETTLEMENT) {
            try {
                game.placeSettlement(vertexId, player);

                setState(FIRST_ROAD);
                lastPlacedVertex = vertexId;

                return SuccessCode.SUCCESS;
            } catch (InvalidPlacementException e) {
                return SuccessCode.INVALID_PLACEMENT;
            } catch (NotEnoughResourcesException e) {
                throw new IllegalStateException("Controller and Game states not synchronized");
            }
        }

        if (gameState == SECOND_SETTLEMENT) {
            try {
                game.placeSettlement(vertexId, player);
                game.distributeResources(player, vertexId);

                setState(SECOND_ROAD);
                lastPlacedVertex = vertexId;
                return SuccessCode.SUCCESS;
            } catch (InvalidPlacementException e) {
                return SuccessCode.INVALID_PLACEMENT;
            } catch (NotEnoughResourcesException e) {
                throw new IllegalStateException("Controller and Game states not synchronized");
            }
        }

        return UNDEFINED;
    }

    public Set<Integer> getBuildableVertexes() {
        return game.getBuildableVertexes(currentPlayer);
    }

    // -------------------------------------------------------------------------------
    //
    // Clicked Road Related Methods:
    //
    // -------------------------------------------------------------------------------

    /**
     * This tries to complete a click on a road, based on the current
     * state of the game.
     */
    public SuccessCode clickedRoad(final int roadId) {
        Player player = this.getCurrentPlayer();

        SuccessCode result;
        if (gamePhase == SETUP) {
            result = this.clickedRoadSetup(roadId, player);
        } else {
            result = this.clickedRoadRegularPlay(roadId, player);
        }

        if (player.getVictoryPoints() >= POINTS_FOR_WIN) {
            result = SuccessCode.GAME_WIN;
        }

        return result;
    }

    /**
     * This tries to complete a click on a road-based on the current
     * state of the game, assuming the game is currently in the regular player
     * phase.
     */
    private SuccessCode clickedRoadRegularPlay(final int roadId, final Player player) {
        switch (gameState) {
            case BUILD_ROAD -> {
                return clickedPlaceRoad(roadId, player);
            }
            case ROAD_BUILDING_1 -> {
                return clickedRoadFirstRoadBuilding(roadId, player);
            }
            case ROAD_BUILDING_2 -> {
                return clickedRoadSecondRoadBuilding(roadId, player);
            }
            case FIRST_ROAD, SECOND_ROAD, FIRST_SETTLEMENT, SECOND_SETTLEMENT -> throw new IllegalStateException("Setup state appeared during regular play");
            default -> {
                return UNDEFINED;
            }
        }
    }

    private SuccessCode clickedRoadSecondRoadBuilding(final int roadId, final Player player) {
        Resource[] resourcesForRoad = {Resource.BRICK, Resource.LUMBER};
        try {
            // here we add resources to the player to give the appearance of free road building
            // note these are not coming from the bank
            player.addResources(resourcesForRoad);


            game.placeRoad(roadId, this.lastPlacedVertex, player);
            this.setState(GameState.DEFAULT);
            return SuccessCode.SUCCESS;
        } catch (InvalidPlacementException e) {
            // remove the resources on failure so they player doesn't accidentally get new resources
            player.hand.removeResources(resourcesForRoad);
            return SuccessCode.INVALID_PLACEMENT;
        } catch (NotEnoughResourcesException e) {
            // remove the resources on failure so they player doesn't accidentally get new resources
            player.hand.removeResources(resourcesForRoad);
            this.setState(GameState.DEFAULT);
            return SuccessCode.INSUFFICIENT_RESOURCES;
        }
    }

    private SuccessCode clickedRoadFirstRoadBuilding(final int roadId, final Player player) {
        Resource[] resourcesForRoad = {Resource.BRICK, Resource.LUMBER};
        try {
            // here we add resources to the player to give the appearance of free road building
            // note these are not coming from the bank
            player.addResources(resourcesForRoad);


            game.placeRoad(roadId, this.lastPlacedVertex, player);
            setState(GameState.ROAD_BUILDING_2);
            return SuccessCode.SUCCESS;
        } catch (InvalidPlacementException e) {
            // remove the resources on failure so they player doesn't accidentally get new resources
            player.hand.removeResources(resourcesForRoad);
            return SuccessCode.INVALID_PLACEMENT;
        } catch (NotEnoughResourcesException e) {
            // remove the resources on failure so they player doesn't accidentally get new resources
            player.hand.removeResources(resourcesForRoad);
            this.setState(GameState.DEFAULT);
            return SuccessCode.INSUFFICIENT_RESOURCES;
        }
    }

    private SuccessCode clickedPlaceRoad(final int roadId, final Player player) {
        try {
            game.placeRoad(roadId, this.lastPlacedVertex, player);
            setState(GameState.DEFAULT);
            return SuccessCode.SUCCESS;
        } catch (InvalidPlacementException e) {
            return SuccessCode.INVALID_PLACEMENT;
        } catch (NotEnoughResourcesException e) {
            return SuccessCode.INSUFFICIENT_RESOURCES;
        }
    }

    /**
     * This tries to complete a click on a road, based on the current
     * state of the game, assuming the game is currently in the setup phase.
     */
    private SuccessCode clickedRoadSetup(final int roadId, final Player player) {
        if (gameState == GameState.FIRST_ROAD) {
            try {
                game.placeRoad(roadId, this.lastPlacedVertex, player);
                setState(SECOND_SETTLEMENT);
                if (playerArr[playerArr.length - 1] != player) { // if we are not the last player
                    currentPlayer = playerArr[++currentPlayerNum];
                    setState(FIRST_SETTLEMENT);
                }
                return SuccessCode.SUCCESS;
            } catch (InvalidPlacementException e) {
                return SuccessCode.INVALID_PLACEMENT;
            } catch (NotEnoughResourcesException e) {
                throw new IllegalStateException("Controller and Game states not synchronized");
            }
        }
        if (gameState == SECOND_ROAD) {
            try {
                game.placeRoad(roadId, lastPlacedVertex, player);
                if (playerArr[0] != player) { // not the first player
                    currentPlayer = playerArr[--currentPlayerNum];
                    setState(SECOND_SETTLEMENT);
                } else { // first player so we are at the end of setup
                    setState(GameState.TURN_START);
                    setPhase(GamePhase.REGULAR_PLAY);
                    game.endSetup();
                }
                return SuccessCode.SUCCESS;
            } catch (InvalidPlacementException e) {
                return SuccessCode.INVALID_PLACEMENT;
            } catch (NotEnoughResourcesException e) {
                throw new IllegalStateException("Controller and Game states not synchronized");
            }
        }
        return UNDEFINED;
    }

    public Set<Integer> getBuildableRoads() {
        return game.getBuildableRoads(currentPlayer, lastPlacedVertex);
    }

    // -------------------------------------------------------------------------------
    //
    // Setters and Getter Methods:
    //
    // -------------------------------------------------------------------------------

    /**
     * Helper method mainly used to set up the controller for testing purposes
     * <p>
     * Warning: This does not correctly change the players array. Or
     * currentPlayerNum.
     * This in only public for testing integration testing, you should not be using
     * this
     * outside of testing.
     *
     * @param player who's turn it is at time of click
     */
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setCurrentPlayer(final Player player) {
        this.currentPlayer = player;
    }

    /**
     * Simple getter for the players in this game.
     * Primarily used by gui during robber actions.
     *
     * @return clone of Array of the players in this game
     */
    public Player[] getPlayerArr() {
        return playerArr.clone();
    }

    /**
     * Helper method that gets the current player.
     * Mainly used for asserting that the player changed correctly
     * at the end of a turn.
     */
    @SuppressFBWarnings("EI_EXPOSE_REP")
    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    /**
     * Simple helper method that gets the Player with the specified number.
     * <p>
     * Warning: Returns null if no player with the specified number is found
     *
     * @return Player with id equal to key
     */
    private Player getPlayerById(final int number) {

        for (Player player : this.playerArr) {
            if (player.getPlayerNum() == number) {
                return player;
            }
        }
        return null;
    }

    /**
     * Helper method to set the phase of the controller.
     * Mainly used during testing to set up cases.
     */
    public void setPhase(final GamePhase phase) {
        this.gamePhase = phase;
    }

    /**
     * Helper method to get the current phase of the game.
     * Mainly used to assert that phases changed or didn't change correctly.
     */
    public GamePhase getPhase() {
        return this.gamePhase;
    }

    /**
     * Helper method to set the state of the controller.
     * Mainly used during testing to set up cases.
     *
     * @param state the game state to test (whatever action the player is trying to
     *              complete)
     */
    public void setState(final GameState state) {
        this.gameState = state;
    }

    /**
     * Helper method to get the current state of the game.
     * Mainly used to assert that states changed or didn't change correctly.
     */
    public GameState getState() {
        return this.gameState;
    }

    // -------------------------------------------------------------------------------
    //
    // Turn Flow and robber related stuff
    //
    // -------------------------------------------------------------------------------

    /**
     * Method used to increment the current player field to the next player in
     * logical order. If the last player just went, and they end their turn, this
     * method should set the current player to the first player again.
     */
    void incrementPlayer() {
        currentPlayerNum++;
        if (currentPlayerNum >= playerArr.length) {
            currentPlayerNum = 0;
        }
        currentPlayer = playerArr[currentPlayerNum];
    }

    /**
     * Ends the current players turn if we are able to do so. We are only
     * able to end the current players turn if no other actions are still
     * in progress. I.e. the gameState is DEFAULT. If a player wants to end
     * their turn they must first cancel whatever action they are currently
     * in. If their action is non-cancellable they must complete it.
     * <p>
     * Upon ending turn. The controller will set the current player to the next
     * player in line, set the state to TURN_START to help determine what
     * actions are and aren't valid, will set the devCardsEnabled flag to false,
     * and finally return SUCCESS.
     * <p>
     * Upon failure returns UNDEFINED.
     */
    public SuccessCode endTurn() {
        if (this.gameState == GameState.DEFAULT) {
            this.currentPlayer.addBoughtCardsToHand();
            this.game.updateLoanDueTimes(this.currentPlayer);
            this.incrementPlayer();
            this.setState(GameState.TURN_START);
            this.setDevCardsEnabled(true);

            // check if the new player to go won
            if (currentPlayer.getVictoryPoints() >= POINTS_FOR_WIN) {
                return SuccessCode.GAME_WIN;
            }

            return SuccessCode.SUCCESS;
        } else {
            return UNDEFINED;
        }
    }

    /**
     * Sets the current die value to a random number between 2-12 (two 6 sided die)
     */
    public int rollDice() {
        int die1 = random.nextInt(MIN_DIE, MAX_DIE);
        int die2 = random.nextInt(MIN_DIE, MAX_DIE);
        int die = die1 + die2;
        for (Player player: this.playerArr) {
            this.game.distributeResources(player, die);
        }

        return die;
    }

    public WeatherEvent createWeatherEvent() {
        Resource resource = getRandomResource();
        BoostType boostType = getRandomBoostType();
        boolean forEveryone = random.nextBoolean();

        if (forEveryone) { // add new random weather event for everyone
            for (Player p: this.playerArr) {
                p.harvestBooster.setBoost(resource, boostType);
            }
        } else { // only add for the person who rolled
            this.currentPlayer.harvestBooster.setBoost(resource, boostType);
        }

        return new WeatherEvent(resource, boostType, forEveryone);
    }

    public record WeatherEvent(Resource resource, BoostType boostType, boolean forEveryone) {
    }

    private Resource getRandomResource() {
        Resource[] resourceTypes = {Resource.BRICK, Resource.LUMBER, Resource.ORE, Resource.GRAIN, Resource.WOOL};

        int randomIndex = random.nextInt(resourceTypes.length);
        return resourceTypes[randomIndex];
    }

    private BoostType getRandomBoostType() {
        BoostType[] boostTypes = {BoostType.ZERO, BoostType.DOUBLE};

        int randomIndex = random.nextInt(boostTypes.length);
        return boostTypes[randomIndex];
    }

    /**
     * Method that receives a map of playerIds to Resource[], parses the Ids
     * to actual player objects and then passes the info to Game to fulfil the
     * request.
     */
    public void dropResources(final HashMap<Integer, Resource[]> playerIdToResourceMap) {

        HashMap<Player, Resource[]> playerToResourceMap = new HashMap<>();

        for (Entry<Integer, Resource[]> entry : playerIdToResourceMap.entrySet()) {
            Player player = this.getPlayerById(entry.getKey());

            if (player == null) {
                throw new IllegalArgumentException("Player ID does not exists");
            }

            playerToResourceMap.put(player, entry.getValue());
        }

        this.game.dropCards(playerToResourceMap);
    }

    /**
     * Helper method that simply passes the request to game and then returns the
     * result.
     * Request is for getting the players that have settlements on the give tileID
     */
    public Player[] getPlayersOnTile(final int tileId) {
        return game.getPlayersFromTile(tileId);
    }

    /**
     * Method that tries the move the robber to the specified location
     * and returns a code whether it succeeded or the reason it failed
     */
    public SuccessCode moveRobber(final int tileID) {
        try {
            game.moveRobber(tileID);
            return SuccessCode.SUCCESS;
        } catch (InvalidPlacementException e) {
            return SuccessCode.INVALID_PLACEMENT;
        }
    }

    /**
     * Method that robs from the given player. Converts the playerId to an actual
     * player,
     * and then does som parameter validation. Will crash with illegal argument
     * exception
     * if the given playerId matched the playerId of the player who tried to rob
     */
    public SuccessCode robPlayer(final int playerId) {
        Player playerToRob = getPlayerById(playerId);
        if (playerToRob == null) {
            throw new IllegalArgumentException("Player to rob does not exist");
        } else if (playerToRob == currentPlayer) {
            throw new IllegalArgumentException("Cannot Rob Yourself");
        }

        try {
            game.stealFromPlayer(currentPlayer, playerToRob);
            return SuccessCode.SUCCESS;
        } catch (NotEnoughResourcesException e) {
            return SuccessCode.INSUFFICIENT_RESOURCES;
        }
    }

    // -------------------------------------------------------------------------------
    //
    // Trading
    //
    // -------------------------------------------------------------------------------

    /**
     * Method that is called from gui to trade between the current player and the
     * tradePartner player.
     * This method does not require a specific state to be set first.
     * <p>
     * Note: this will crash the game with an IllegalArgumentException if the
     * tradePartner is the current player.
     */
    public SuccessCode tradeWithPlayer(final Player tradePartner, final Resource[] giving, final Resource[] receiving) {
        if (this.currentPlayer == tradePartner) {
            throw new IllegalArgumentException();
        }
        if (giving.length == 0 || receiving.length == 0) {
            return SuccessCode.INSUFFICIENT_RESOURCES;
        }
        if (this.currentPlayer.tradeResources(tradePartner, giving, receiving)) {
            return SuccessCode.SUCCESS;
        }
        return UNDEFINED;
    }

    /**
     * Method that is called from gui to trade the giving resource type to
     * the bank for the receiving resource type.
     * This method does not require a specific state to be set first.
     */
    public SuccessCode tradeWithBank(final Resource giving, final Resource receiving) {
        if (this.currentPlayer.tradeWithBank(giving, receiving)) {
            return SuccessCode.SUCCESS;
        }
        return SuccessCode.INSUFFICIENT_RESOURCES;
    }

    public void takeOutLoan(final Resource[] resources) throws NotEnoughResourcesException {
        this.game.takeOutLoan(this.currentPlayer, resources);
    }

    // -------------------------------------------------------------------------------
    //
    // Dev card related stuff
    //
    // -------------------------------------------------------------------------------

    /**
     * Method that is called from gui to buy a dev card for the current player.
     * This method does not require a specific state to be set first.
     * <p>
     * Note: this will crash the game with an IllegalStateException if this is
     * called during setup.
     */
    public SuccessCode clickedBuyDevCard() {

        if (this.gamePhase == SETUP) {
            throw new IllegalStateException("Clicked buy dev card during setup");
        }
        try {
            this.game.buyDevCard(currentPlayer);

            if (currentPlayer.getVictoryPoints() >= POINTS_FOR_WIN) {
                return SuccessCode.GAME_WIN;
            }

            return SuccessCode.SUCCESS;
        } catch (NotEnoughResourcesException e) {
            return SuccessCode.INSUFFICIENT_RESOURCES;
        } catch (EmptyDevCardDeckException e) {
            return SuccessCode.EMPTY_DEV_CARD_DECK;
        }
    }

    /**
     * Simple setter for the flag that tracks whether we can play dev cards
     * currently or not
     */
    public void setDevCardsEnabled(final boolean b) {
        this.devCardsEnabled = b;
    }

    /**
     * Gets the flag that tells whether we can play dev cards or not at the
     * moment
     */
    public boolean getDevCardsEnabled() {
        return this.devCardsEnabled;
    }

    /**
     * Method that parses the given arguments and tells the game to fulfill the
     * players request
     * If the player cannot play the card will return insufficient resources
     * and success if it can and it succeeded
     */
    public SuccessCode playMonopolyCard(final Resource resourceToRob) {
        if (!devCardsEnabled) {
            return SuccessCode.CANNOT_PLAY_CARD;
        }

        Player[] playersToRob = this.getPlayersExcept(currentPlayer);
        try {
            game.playMonopoly(currentPlayer, playersToRob, resourceToRob);
            setDevCardsEnabled(false);
            return SuccessCode.SUCCESS;
        } catch (CardNotPlayableException e) {
            return SuccessCode.CANNOT_PLAY_CARD;
        }
    }

    /**
     * Helper method that gets all the players in the game except the given player.
     * Primarily used to get the players to rob during the monopoly card parameter
     * generation
     */
    private Player[] getPlayersExcept(final Player player) {
        Player[] newArray = new Player[playerArr.length - 1];
        int index = 0;

        for (Player value : playerArr) {
            if (!value.equals(player)) {
                newArray[index] = value;
                index++;
            }
        }

        return newArray;
    }

    public SuccessCode buildDistrict(final int vertexId, final DistrictType type) {
        try {
            game.buildDistrictOnVertex(currentPlayer, vertexId, type);
            return SuccessCode.SUCCESS;
        } catch (NotEnoughResourcesException e) {
            return SuccessCode.INSUFFICIENT_RESOURCES;
        } catch (InvalidPlacementException e) {
            return  SuccessCode.INVALID_PLACEMENT;
        }
    }

    public DistrictType getDistrictTypeForVertex(final int vertex) {
        return game.getDistrictTypeForVertex(vertex);
    }

    /**
     * This method tries to play the year of plenty card.
     * Returns success if playing the card was successful
     * Returns insufficient resources if the player cannot play the card
     * or if the bank ran out of the requested resources
     */
    public SuccessCode playYearOfPlenty(final Resource resource1, final Resource resource2) {
        if (!devCardsEnabled) {
            return SuccessCode.CANNOT_PLAY_CARD;
        }

        try {
            game.playYearOfPlenty(currentPlayer, resource1, resource2);
            setDevCardsEnabled(false);
            return SuccessCode.SUCCESS;
        } catch (NotEnoughResourcesException e) {
            return SuccessCode.INSUFFICIENT_RESOURCES;
        } catch (CardNotPlayableException e) {
            return SuccessCode.CANNOT_PLAY_CARD;
        }
    }

    /**
     * Method that plays the knight card.
     * Waning: this method only checks if the player can play it and if so
     * increments the num knights and redistributes the largest army card if
     * necessary. This does not do the robber related stuff for knight. Those will
     * need to be called separately
     */
    public SuccessCode playKnightCard() {
        if (!devCardsEnabled) {
            return SuccessCode.CANNOT_PLAY_CARD;
        }

        if (!currentPlayer.useDevCard(DevCard.KNIGHT)) {
            return SuccessCode.CANNOT_PLAY_CARD;
        }

        currentPlayer.incrementNumKnights();
        setDevCardsEnabled(false);
        distributeLargestArmyCard();

        if (currentPlayer.getVictoryPoints() >= POINTS_FOR_WIN) {
            return SuccessCode.GAME_WIN;
        }

        return SuccessCode.SUCCESS;
    }

    public Set<Integer> getValidRobberSpots() {
        return game.getValidRobberSpots(currentPlayer);
    }

    /**
     * Method that plays the road building card.
     * Warning, this simply sets up the controller for the subsequent clicks
     * and removes the card from the player if they have it
     */
    public SuccessCode useRoadBuildingCard() {
        if (!devCardsEnabled || !currentPlayer.useDevCard(DevCard.ROAD)) {
            return SuccessCode.CANNOT_PLAY_CARD;
        }

        setDevCardsEnabled(false);
        setState(GameState.ROAD_BUILDING_1);

        return SuccessCode.SUCCESS;
    }

    /**
     * Method that will give the correct player the largest army card according to the game rules
     */
    public void distributeLargestArmyCard() {
        Player playerWithLargestArmy = null;

        // first store everybody's number of knights in a map
        HashMap<Player, Integer> playerToNumKnightsMap = new HashMap<>();
        for (Player player: playerArr) {
            playerToNumKnightsMap.put(player, player.getNumKnightsPlayed());
        }

        // try to find someone who already has the largest army
        for (Player player: playerArr) {
            if (player.hasLargestArmy()) {
                playerWithLargestArmy = player;
            }
        }

        // if no one had the largest army try to give it to the first person who needs it
        if (playerWithLargestArmy == null) {
            for (Player player: playerArr) {
                if (playerToNumKnightsMap.get(player) >= MIN_KNIGHTS_FOR_LARGEST_ARMY) {
                    player.giveLargestArmyCard();
                }
            }
        } else { // someone had the largest army already so give it to a new player if they have a larger army
            for (Player player: playerArr) {
                if (player != playerWithLargestArmy) { // don't compare against self to avoid weirdness
                    if (playerToNumKnightsMap.get(player) > playerToNumKnightsMap.get(playerWithLargestArmy)) {
                        player.giveLargestArmyCard();
                        playerWithLargestArmy.removeLargestArmyCard();
                    }
                }
            }
        }
    }

    // -----------------------------------
    //
    // Restorable implementation
    //
    // -----------------------------------

    public class ControllerMemento implements Memento {

        // simple fields
        private final GamePhase gamePhase;
        private final GameState gameState;
        private final int currentPlayerNum;
        private final Player currentPlayer;
        private final int lastPlacedVertex;
        private final boolean devCardsEnabled;

        // sub mementos
        private final Memento gameMemento;
        private final Memento[] playerMementos;


        // Storage Constants
        private static final String TARGET_FILE_NAME = "controller.txt";
        private static final String GAME_SUBFOLDER_NAME = "Game";
        private static final String PLAYER_SUBFOLDER_PREFIX = "Player";

        // Field Keys
        // Storage Constants - Field Keys
        private static final String GAME_PHASE = "GamePhase";
        private static final String GAME_STATE = "GameState";
        private static final String CURRENT_PLAYER_NUM = "CurrentPlayerNum";
        private static final String LAST_PLACED_VERTEX = "LastPlacedVertex";
        private static final String DEV_CARDS_ENABLED = "DevCardsEnabled";

        private ControllerMemento() {
            // simple fields
            this.gamePhase = Controller.this.gamePhase;
            this.gameState = Controller.this.gameState;
            this.currentPlayerNum = Controller.this.currentPlayerNum;
            this.currentPlayer = Controller.this.currentPlayer;
            this.lastPlacedVertex = Controller.this.lastPlacedVertex;
            this.devCardsEnabled = Controller.this.devCardsEnabled;

            // sub mementos
            this.gameMemento = Controller.this.game.createMemento();

            this.playerMementos = new Memento[Controller.this.playerArr.length];
            for (int i = 0; i < Controller.this.playerArr.length; i++) {
                this.playerMementos[i] = Controller.this.playerArr[i].createMemento();
            }
        }

        @SuppressFBWarnings("EI_EXPOSE_REP2")
        public ControllerMemento(final File folder) {
            // Create a MementoReader for reading memento data
            MementoReader reader = new MementoReader(folder, TARGET_FILE_NAME);

            // Read simple fields from the file
            this.gamePhase = GamePhase.valueOf(reader.readField(GAME_PHASE));
            this.gameState = GameState.valueOf(reader.readField(GAME_STATE));
            this.currentPlayerNum = Integer.parseInt(reader.readField(CURRENT_PLAYER_NUM));
            this.currentPlayer = GameLoader.getInstance().getPlayerByNum(this.currentPlayerNum + 1);
            this.lastPlacedVertex = Integer.parseInt(reader.readField(LAST_PLACED_VERTEX));
            this.devCardsEnabled = Boolean.parseBoolean(reader.readField(DEV_CARDS_ENABLED));

            // Read sub-mementos from the appropriate sub-folders
            File gameSubFolder = reader.getSubFolder(GAME_SUBFOLDER_NAME);
            this.gameMemento = Controller.this.game.new GameMemento(gameSubFolder);

            this.playerMementos = new Memento[Controller.this.playerArr.length];
            for (int i = 0; i < this.playerMementos.length; i++) {
                File playerSubFolder = reader.getSubFolder(PLAYER_SUBFOLDER_PREFIX + (i + 1));
                this.playerMementos[i] = Controller.this.playerArr[i].new PlayerMemento(playerSubFolder);
            }
        }

        public void save(final File folder) throws IOException {
            // Create a MementoWriter for writing memento data
            MementoWriter writer = new MementoWriter(folder, TARGET_FILE_NAME);

            // Write simple fields to the file
            writer.writeField(GAME_PHASE, gamePhase.toString());
            writer.writeField(GAME_STATE, gameState.toString());
            writer.writeField(CURRENT_PLAYER_NUM, Integer.toString(currentPlayerNum));
            writer.writeField(LAST_PLACED_VERTEX, Integer.toString(lastPlacedVertex));
            writer.writeField(DEV_CARDS_ENABLED, Boolean.toString(devCardsEnabled));

            // delegate to sub mementos
            // Save game memento's state to the appropriate subfolder
            File gameSubFolder = writer.getSubFolder(GAME_SUBFOLDER_NAME);
            gameMemento.save(gameSubFolder);

            // Save player mementos to separate sub-folders
            for (int i = 0; i < playerMementos.length; i++) {
                // Create a subfolder for each player's memento
                File playerSubFolder = writer.getSubFolder(PLAYER_SUBFOLDER_PREFIX + (i + 1));
                playerMementos[i].save(playerSubFolder);
            }
        }

        public void restore() {
            // simple field restoration
            Controller.this.gamePhase = this.gamePhase;
            Controller.this.gameState = this.gameState;
            Controller.this.currentPlayerNum = this.currentPlayerNum;
            Controller.this.lastPlacedVertex = this.lastPlacedVertex;
            Controller.this.devCardsEnabled = this.devCardsEnabled;
            Controller.this.currentPlayer = this.currentPlayer;

            // Delegate restoration to sub mementos
            this.gameMemento.restore();
            for (Memento playerMemento: this.playerMementos) {
                playerMemento.restore();
            }
        }
    }

    @Override
    public Memento createMemento() {
        return new ControllerMemento();
    }
}
