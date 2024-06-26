package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import data.GameLoader;
import domain.bank.Bank;
import domain.player.HarvestBooster;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.controller.Controller;
import domain.controller.GameState;
import domain.controller.SuccessCode;
import domain.devcarddeck.DevCard;
import domain.devcarddeck.DevelopmentCardDeck;
import domain.game.Game;
import domain.gameboard.GameBoard;
import domain.game.GameType;
import domain.player.Player;
import domain.bank.Resource;
import domain.graphs.GameboardGraph;

/**
 * The purpose of this test class is to test feature 23 (F23):
 *      F23: Track player points for various moves. One point for each settlement 
 *      built Two points for each city built One or two points for certain development
 *      cards Points earned from special victory point cards
 */
public class F23Test {

    private static final int POINTS_FROM_SETUP = 2;
    private static final int POINTS_FOR_SETTLEMENT = 1;
    private static final int POINTS_FOR_CITY = 2;
    private static final int POINTS_FOR_LONGEST_ROAD = 2;
    private static final int POINTS_FOR_LARGEST_ARMY = 2;
    private static final int POINTS_FOR_VICTORY_POINT_CARD = 1;

    private static final Resource[] RESOURCES_FOR_SETTLEMENT = {Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN};
    private static final Resource[] RESOURCES_FOR_ROAD = {Resource.BRICK, Resource.LUMBER};
    private static final Resource[] RESOURCES_FOR_CITY = {Resource.ORE, Resource.ORE, Resource.ORE, Resource.GRAIN, Resource.GRAIN};
    private static final Resource[] RESOURCES_FOR_DEV_CARD = {Resource.ORE, Resource.WOOL, Resource.GRAIN};

    private void loopToBeginning(final Controller controller) {
        for (int i = 0; i < 4; i++) {
            controller.setState(GameState.DEFAULT);
            assertEquals(SuccessCode.SUCCESS, controller.endTurn());
        }
    }

    GameboardGraph gameboardGraph;
    Bank bank;
    Player player1;
    Player player2;
    Player player3;
    Player player4;
    Player[] players;
    Controller controller;

    @BeforeEach
    public void createGameObjects() {
        GameType gameType = GameType.Beginner;
        gameboardGraph = new GameboardGraph(gameType);
        GameLoader.initializeGraphs(gameboardGraph);

        bank = new Bank();
        player1 = new Player(1, new HarvestBooster(), bank);
        player2 = new Player(2, new HarvestBooster(), bank);
        player3 = new Player(3, new HarvestBooster(), bank);
        player4 = new Player(4, new HarvestBooster(), bank);
        players = new Player[]{player1, player2, player3, player4};

        // other things dependent on these things
        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        Game game = new Game(gameBoard, gameboardGraph, devCardDeck, bank);

        // Assert that the beginner setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> controllerRef.set(new Controller(game, players, gameType)), "Setup while loop timed out");
        controller = controllerRef.get();

        for (Player p: players) {
            p.hand.clearResources();
        }
        bank.reset();
    }

    @Test
    public void testVictoryPointsSettlement() {
        // make sure player 1 is going
        assertEquals(player1, controller.getCurrentPlayer());

        // assert on points before the build
        assertEquals(POINTS_FROM_SETUP, player1.getVictoryPoints());

        // give the player enough resources to build a road
        player1.hand.addResources(RESOURCES_FOR_ROAD);

        // player builds a road, so we can follow the distance rule
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(24));

        // make sure we didn't gain points form building a road
        assertEquals(POINTS_FROM_SETUP, player1.getVictoryPoints());


        // give the player enough resources for the settlement
        player1.hand.addResources(RESOURCES_FOR_SETTLEMENT);

        // player builds the settlement
        controller.setState(GameState.BUILD_SETTLEMENT);
        assertEquals(SuccessCode.SUCCESS, controller.clickedVertex(17));

        // assert that we gained the right amount of points
        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_SETTLEMENT, player1.getVictoryPoints());
    }

    @Test
    public void testVictoryPointsCity() {
        // make sure player 1 is going
        assertEquals(player1, controller.getCurrentPlayer());

        // assert on points before the build
        assertEquals(POINTS_FROM_SETUP, player1.getVictoryPoints());

        // give the player enough resources to build a road
        player1.hand.addResources(RESOURCES_FOR_ROAD);

        // player builds a road, so we can follow the distance rule
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(24));

        // make sure we didn't gain points form building a road
        assertEquals(POINTS_FROM_SETUP, player1.getVictoryPoints());


        // give the player enough resources for the settlement
        player1.hand.addResources(RESOURCES_FOR_SETTLEMENT);

        // player builds the settlement
        controller.setState(GameState.BUILD_SETTLEMENT);
        assertEquals(SuccessCode.SUCCESS, controller.clickedVertex(17));

        // assert that we gained the right amount of points
        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_SETTLEMENT, player1.getVictoryPoints());


        // give the player enough resources for a city
        player1.hand.addResources(RESOURCES_FOR_CITY);

        // player upgrades the previous settlement to a city
        controller.setState(GameState.UPGRADE_SETTLEMENT);
        assertEquals(SuccessCode.SUCCESS, controller.clickedVertex(17));

        // make sure we didn't gain points form building a road
        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_CITY, player1.getVictoryPoints());
    }

    @Test
    public void testVictoryPointsLongestRoad() {
        // make sure player doesn't start with the longest road
        assertFalse(player1.hasLongestRoad());

        // points before the builds
        assertEquals(POINTS_FROM_SETUP, player1.getVictoryPoints());

        // -------------------------- Player 1 build enough to get he longest road ---------------------------
        // Note here the controller default to player 1 first
        // give the player enough resources to build 4 roads
        for (int i = 0; i < 4; i++) {
            player1.hand.addResources(RESOURCES_FOR_ROAD);
        }
        // build some roads
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(26));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(27));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(28));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(29));


        // player 1 should have the longest road card
        assertTrue(player1.hasLongestRoad());

        // player should have gained the points
        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_LONGEST_ROAD, player1.getVictoryPoints());
    }

    @Test
    public void testVictoryPointsLargestArmy() {
        // player doesn't start with it
        assertFalse(player1.hasLargestArmy());
        assertEquals(POINTS_FROM_SETUP, player1.getVictoryPoints());

        // Make sure we are on player1
        assertEquals(player1, controller.getCurrentPlayer());

        // give the player some resources
        player1.hand.addResources(RESOURCES_FOR_DEV_CARD);
        player1.hand.addResources(RESOURCES_FOR_DEV_CARD);
        player1.hand.addResources(RESOURCES_FOR_DEV_CARD);

        // player buys three cards
        assertTrue(player1.purchaseDevCard(DevCard.KNIGHT));
        assertTrue(player1.purchaseDevCard(DevCard.KNIGHT));
        assertTrue(player1.purchaseDevCard(DevCard.KNIGHT));

        // can't use bought card on same turn, so end turn until we are back at player 1
        loopToBeginning(controller);
        assertEquals(player1, controller.getCurrentPlayer());
        assertEquals(SuccessCode.SUCCESS, controller.playKnightCard());
        
        // can only use one card per turn so loop back to beginning
        loopToBeginning(controller);
        assertEquals(player1, controller.getCurrentPlayer());
        assertEquals(SuccessCode.SUCCESS, controller.playKnightCard());

        // can only use one card per turn so loop back to beginning
        loopToBeginning(controller);
        assertEquals(player1, controller.getCurrentPlayer());
        assertEquals(SuccessCode.SUCCESS, controller.playKnightCard());


        // player should now have the largest army card
        assertEquals(3, player1.getNumKnightsPlayed());
        assertTrue(player1.hasLargestArmy());
        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_LARGEST_ARMY, player1.getVictoryPoints());
    }

    @Test
    public void testVictoryPointsCard() {
        // make sure player 1 is going
        assertEquals(player1, controller.getCurrentPlayer());

        // assert on points before the card
        assertEquals(POINTS_FROM_SETUP, player1.getVictoryPoints());

        // give the player enough resources to buy the card
        player1.hand.addResources(RESOURCES_FOR_DEV_CARD);

        // player buys gets the victory point dev card
        player1.purchaseDevCard(DevCard.VICTORY);

        // make sure we didn't gain points form building a road
        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_VICTORY_POINT_CARD, player1.getVictoryPoints());
    }
}
