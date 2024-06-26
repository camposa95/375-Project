package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.HashSet;
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
import domain.devcarddeck.DevelopmentCardDeck;
import domain.game.Game;
import domain.gameboard.GameBoard;
import domain.game.GameType;
import domain.player.Player;
import domain.bank.Resource;
import domain.graphs.GameboardGraph;

/**
 * The purpose of this test class is to test feature 21 (F21):
 *      Ability for a Player to collect 2 Victory Points for having the Longest Road,
 *      longer than 5 road pieces. Another player can steal these points if they build a longer road.
 */
public class F21Test {

    private static final Resource[] RESOURCES_FOR_ROAD = {Resource.BRICK, Resource.LUMBER};
    private static final Resource[] RESOURCES_FOR_SETTLEMENT = {Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN};

    private static final int POINTS_FROM_SETUP = 2;
    private static final int POINTS_FOR_SETTLEMENT = 1;
    private static final int POINTS_FOR_LONGEST_ROAD = 2;

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
    public void testLongestRoadNoOneEligible() {
        // here no one has it, so we assert on those states
        assertFalse(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());
    }

    @Test
    public void testLongestRoadPlayerGetsItFirst() {
        // here no one has it, so we assert on those states
        assertFalse(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());

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
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_LONGEST_ROAD, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());
    }

    @Test
    public void testLongestRoadPlayerGetsTied() {
        // here no one has it, so we assert on those states
        assertFalse(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());

        // -------------------------- Player 1 build enough to get the longest road ---------------------------
        // Note here the controller default to player 1 first
        // give the player enough resources to build 4 roads
        for (int i = 0; i < 4; i++) {
            player1.hand.addResources(RESOURCES_FOR_ROAD);
        }
        // build some roads make sure they succeed
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
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_LONGEST_ROAD, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());

        // -------------------------- Player 2 build enough to tie player 1 ---------------------------
        // move to next person
        controller.endTurn();

        // give the player enough resources to build 4 roads
        for (int i = 0; i < 4; i++) {
            controller.getCurrentPlayer().hand.addResources(RESOURCES_FOR_ROAD);
        }
        // build some roads
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(8));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(3));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(16));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(17));


        // player 1 should still have the longest road card
        assertTrue(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_LONGEST_ROAD, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());
    }

    @Test
    public void testLongestRoadPlayerGetsOvertaken() {
        // here no one has it, so we assert on those states
        assertFalse(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());

        // -------------------------- Player 1 build enough to get the longest road ---------------------------
        // Note here the controller default to player 1 first
        // give the player enough resources to build 4 roads
        for (int i = 0; i < 4; i++) {
            player1.hand.addResources(RESOURCES_FOR_ROAD);
        }
        // build some roads make sure they succeed
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
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_LONGEST_ROAD, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());

        // -------------------------- Player 2 build enough to tie player 1 ---------------------------
        // move to next person
        controller.endTurn();
        assertEquals(controller.getCurrentPlayer(), player2);

        // give the player enough resources to build 4 roads
        for (int i = 0; i < 4; i++) {
            controller.getCurrentPlayer().hand.addResources(RESOURCES_FOR_ROAD);
        }
        // build some roads
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(8));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(3));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(16));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(17));


        // player 1 should still have the longest road card
        assertTrue(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_LONGEST_ROAD, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());

        // -------------------------- Player 2 builds another to overtake ---------------------------
        controller.getCurrentPlayer().hand.addResources(RESOURCES_FOR_ROAD);
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(22));

        // player 2 should have the longest road card now
        assertFalse(player1.hasLongestRoad());
        assertTrue(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_LONGEST_ROAD, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());
    }

    @Test
    public void testLongestRoadPlayerBrokenButStillLongest() {
        // ---------------------- Here are some basic wiring needed that would be done by main ------------------------------
        
        // Here we use beginner game to skip through to the regular gameplay
        GameType gameType = GameType.Beginner;
        GameboardGraph vertexes = new GameboardGraph(gameType);
        GameLoader.initializeGraphs(vertexes);

        Bank bank = new Bank();
        Player player1 = new Player(1, new HarvestBooster(), bank);
        Player player2 = new Player(2, new HarvestBooster(), bank);
        Player player3 = new Player(3, new HarvestBooster(), bank);
        Player player4 = new Player(4, new HarvestBooster(), bank);
        Player[] players = {player1, player2, player3, player4};

        // other things dependent on these things
        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        Game game = new Game(gameBoard, vertexes, devCardDeck, bank);
        
        // Assert that the beginner setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> controllerRef.set(new Controller(game, players, gameType)), "Setup while loop timed out");
        Controller controller = controllerRef.get();

        // Note: we assume everything about setup was correct because it was tested earlier

        // Note: at this point the players would have gotten some starter resources during the 
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the players' hand and assert that the players have zero resources to control
        // the test more
        for (Player player: players) {
            for (Resource resource: Resource.values()) {
                if (resource != Resource.ANY) { // skip this one used for trading
                    int count = player.hand.getResourceCount(resource);
                    if (count > 0) {
                        player.hand.removeResource(resource, count);
                    }
                }
            }
            assertEquals(0, player.hand.getResourceCount());
        }

        // -------------------------- Start of Actual Test Stuff ---------------------------
        // here no one has it, so we assert on those states
        assertFalse(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());

        // -------------------------- Player 1 build enough to get the longest road ---------------------------
        // Note here the controller default to player 1 first
        // give the player enough resources to build 7 roads
        for (int i = 0; i < 7; i++) {
            player1.hand.addResources(RESOURCES_FOR_ROAD);
        }
        // build some roads make sure they succeed
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(26));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(27));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(28));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(29));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(30));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(24));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(23));

        // at this point player 1 should have the longest path of 9
        assertEquals(9, vertexes.getLongestPath(
                            vertexes.getRoad(23),
                            player1,
                new HashSet<>(),
                new HashSet<>(),
                            vertexes.getVertex(16)));

        // player 1 should have the longest road card
        assertTrue(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_LONGEST_ROAD, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());

        // -------------------------- Player 2 build and breaks player 1 ---------------------------
        // move to next person
        controller.endTurn();
        assertEquals(player2, controller.getCurrentPlayer());

        // give the player enough resources to build 4 roads
        for (int i = 0; i < 4; i++) {
            controller.getCurrentPlayer().hand.addResources(RESOURCES_FOR_ROAD);
        }
        // build some roads
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(51));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(44));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(36));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(64));

        // build the settlement to break player1's path
        controller.getCurrentPlayer().hand.addResources(RESOURCES_FOR_SETTLEMENT);
        controller.setState(GameState.BUILD_SETTLEMENT);
        assertEquals(SuccessCode.SUCCESS, controller.clickedVertex(22));

        // at this point player 2 should have the longest path of 5
        assertEquals(5, vertexes.getLongestPath(
                vertexes.getRoad(64),
                            player2,
                new HashSet<>(),
                new HashSet<>(),
                            vertexes.getVertex(51)));

        // at this point player 1 should have the longest path of 6
        assertEquals(6, vertexes.getLongestPath(
                vertexes.getRoad(23),
                            player1,
                new HashSet<>(),
                new HashSet<>(),
                            vertexes.getVertex(16)));

        // at this point player 1 should have another path of 3
        assertEquals(3, vertexes.getLongestPath(
                vertexes.getRoad(37),
                            player1,
                new HashSet<>(),
                new HashSet<>(),
                            vertexes.getVertex(35)));


        // player 1 should still have the longest road card
        assertTrue(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_LONGEST_ROAD, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_SETTLEMENT, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());
    }

    @Test
    public void testLongestRoadPlayerBrokenAndOvertaken() {
        // here no one has it, so we assert on those states
        assertFalse(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());

        // ------------------- Player 1 build enough to get the longest road ---------------------------
        // Note here the controller default to player 1 first
        // give the player enough resources to build 7 roads
        for (int i = 0; i < 7; i++) {
            player1.hand.addResources(RESOURCES_FOR_ROAD);
        }
        // build some roads make sure they succeed
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(26));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(27));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(28));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(29));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(30));

        // at this point player 1 should have the longest path of 7
        assertEquals(7, gameboardGraph.getLongestPath(
                            gameboardGraph.getRoad(25),
                            player1,
                new HashSet<>(),
                new HashSet<>(),
                            gameboardGraph.getVertex(18)));

        // player 1 should have the longest road card
        assertTrue(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_LONGEST_ROAD, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());

        // ----------------------- Player 2 build and breaks player 1 and overtakes ---------------------------
        // move to next person
        controller.endTurn();
        assertEquals(player2, controller.getCurrentPlayer());

        // give the player enough resources to build 4 roads
        for (int i = 0; i < 4; i++) {
            controller.getCurrentPlayer().hand.addResources(RESOURCES_FOR_ROAD);
        }
        // build some roads
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(51));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(44));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(36));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(64));

        // build the settlement to break player1's path
        controller.getCurrentPlayer().hand.addResources(RESOURCES_FOR_SETTLEMENT);
        controller.setState(GameState.BUILD_SETTLEMENT);
        assertEquals(SuccessCode.SUCCESS, controller.clickedVertex(22));

        // at this point player 2 should have the longest path of 5
        assertEquals(5, gameboardGraph.getLongestPath(
                            gameboardGraph.getRoad(64),
                            player2,
                new HashSet<>(),
                new HashSet<>(),
                            gameboardGraph.getVertex(51)));

        // at this point player 1 should have the longest path of 4
        assertEquals(4, gameboardGraph.getLongestPath(
                            gameboardGraph.getRoad(25),
                            player1,
                new HashSet<>(),
                new HashSet<>(),
                            gameboardGraph.getVertex(18)));

        // at this point player 1 should have another path of 3
        assertEquals(3, gameboardGraph.getLongestPath(
                            gameboardGraph.getRoad(37),
                            player1,
                new HashSet<>(),
                new HashSet<>(),
                            gameboardGraph.getVertex(35)));


        // player 2 should have the card now
        assertFalse(player1.hasLongestRoad());
        assertTrue(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_SETTLEMENT + POINTS_FOR_LONGEST_ROAD, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());
    }

    @Test
    public void testLongestRoadPlayerBrokenAndTied() {
        // here no one has it, so we assert on those states
        assertFalse(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());

        // -------------------------- Player 1 build enough to get the longest road ---------------------------
        // Note here the controller default to player 1 first
        // give the player enough resources to build 7 roads
        for (int i = 0; i < 7; i++) {
            player1.hand.addResources(RESOURCES_FOR_ROAD);
        }
        // build some roads make sure they succeed
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(26));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(27));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(28));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(29));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(30));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(24));

        // at this point player 1 should have the longest path of 8
        assertEquals(8, gameboardGraph.getLongestPath(
                            gameboardGraph.getRoad(24),
                            player1,
                new HashSet<>(),
                new HashSet<>(),
                            gameboardGraph.getVertex(17)));

        // player 1 should have the longest road card
        assertTrue(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_LONGEST_ROAD, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());

        // -------------------------- Player 2 build and breaks player 1 ---------------------------
        // move to next person
        controller.endTurn();
        assertEquals(player2, controller.getCurrentPlayer());

        // give the player enough resources to build 4 roads
        for (int i = 0; i < 4; i++) {
            controller.getCurrentPlayer().hand.addResources(RESOURCES_FOR_ROAD);
        }
        // build some roads
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(51));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(44));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(36));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(64));

        // build the settlement to break player1's path
        controller.getCurrentPlayer().hand.addResources(RESOURCES_FOR_SETTLEMENT);
        controller.setState(GameState.BUILD_SETTLEMENT);
        assertEquals(SuccessCode.SUCCESS, controller.clickedVertex(22));

        // at this point player 2 should have the longest path of 5
        assertEquals(5, gameboardGraph.getLongestPath(
                            gameboardGraph.getRoad(64),
                            player2,
                new HashSet<>(),
                new HashSet<>(),
                            gameboardGraph.getVertex(51)));

        // at this point player 1 should have the longest path of 6
        assertEquals(5, gameboardGraph.getLongestPath(
                            gameboardGraph.getRoad(24),
                            player1,
                new HashSet<>(),
                new HashSet<>(),
                            gameboardGraph.getVertex(17)));

        // at this point player 1 should have another path of 3
        assertEquals(3, gameboardGraph.getLongestPath(
                            gameboardGraph.getRoad(37),
                            player1,
                new HashSet<>(),
                new HashSet<>(),
                            gameboardGraph.getVertex(35)));


        // player 1 should still have the longest road card
        assertTrue(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_LONGEST_ROAD, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_SETTLEMENT, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());
    }

    @Test
    public void testLongestRoadPlayerBrokenAndTwoOtherTie() {
        // here no one has it, so we assert on those states
        assertFalse(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());

        // ------------------- Player 1 build enough to get the longest road ---------------------------
        // Note here the controller default to player 1 first
        // give the player enough resources to build 7 roads
        for (int i = 0; i < 7; i++) {
            player1.hand.addResources(RESOURCES_FOR_ROAD);
        }
        // build some roads make sure they succeed
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(26));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(27));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(28));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(29));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(30));

        // at this point player 1 should have the longest path of 7
        assertEquals(7, gameboardGraph.getLongestPath(
                            gameboardGraph.getRoad(25),
                            player1,
                new HashSet<>(),
                new HashSet<>(),
                            gameboardGraph.getVertex(18)));

        // player 1 should have the longest road card
        assertTrue(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_LONGEST_ROAD, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());

        // ----------------------- Player 2 build till 5 ---------------------------
        // move to next person
        controller.endTurn();
        assertEquals(player2, controller.getCurrentPlayer());

        // give the player enough resources to build 4 roads
        for (int i = 0; i < 4; i++) {
            controller.getCurrentPlayer().hand.addResources(RESOURCES_FOR_ROAD);
        }
        // build some roads
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(51));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(44));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(36));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(64));

        // at this point player 2 should have the longest path of 5
        assertEquals(5, gameboardGraph.getLongestPath(
                            gameboardGraph.getRoad(64),
                            player2,
                new HashSet<>(),
                new HashSet<>(),
                            gameboardGraph.getVertex(51)));


        // player 1 should have the card still
        assertTrue(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_LONGEST_ROAD, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());


        // move us to player 4
        assertEquals(SuccessCode.SUCCESS, controller.endTurn());
        assertEquals(player3, controller.getCurrentPlayer());
        controller.setState(GameState.DEFAULT); // imagine we did normal stuff in player 3's turn that is not relevant
        assertEquals(SuccessCode.SUCCESS, controller.endTurn());
        assertEquals(player4, controller.getCurrentPlayer());

        // give the player enough resources to build 4 roads
        for (int i = 0; i < 4; i++) {
            controller.getCurrentPlayer().hand.addResources(RESOURCES_FOR_ROAD);
        }
        // build some roads
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(12));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(11));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(10));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(18));

        // at this point player 4 should have the longest path of 5
        assertEquals(5, gameboardGraph.getLongestPath(
                            gameboardGraph.getRoad(18),
                            player4,
                new HashSet<>(),
                new HashSet<>(),
                            gameboardGraph.getVertex(17)));

        // player 1 should have the card still
        assertTrue(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_LONGEST_ROAD, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());


        // move us to player 2
        assertEquals(SuccessCode.SUCCESS, controller.endTurn());
        assertEquals(player1, controller.getCurrentPlayer());
        controller.setState(GameState.DEFAULT); // imagine we did normal stuff in player 1's turn that is not relevant
        assertEquals(SuccessCode.SUCCESS, controller.endTurn());
        assertEquals(player2, controller.getCurrentPlayer());


        // build the settlement to break player1's path
        controller.getCurrentPlayer().hand.addResources(RESOURCES_FOR_SETTLEMENT);
        controller.setState(GameState.BUILD_SETTLEMENT);
        assertEquals(SuccessCode.SUCCESS, controller.clickedVertex(22));

        // at this point player 2 should have the longest path of 5
        assertEquals(5, gameboardGraph.getLongestPath(
                            gameboardGraph.getRoad(64),
                            player2,
                new HashSet<>(),
                new HashSet<>(),
                            gameboardGraph.getVertex(51)));

        // at this point player 4 should have the longest path of 5
        assertEquals(5, gameboardGraph.getLongestPath(
                            gameboardGraph.getRoad(18),
                            player4,
                new HashSet<>(),
                new HashSet<>(),
                            gameboardGraph.getVertex(17)));

        // at this point player 1 should have the longest path of 4
        assertEquals(4, gameboardGraph.getLongestPath(
                            gameboardGraph.getRoad(25),
                            player1,
                new HashSet<>(),
                new HashSet<>(),
                            gameboardGraph.getVertex(18)));

        // at this point player 1 should have another path of 3
        assertEquals(3, gameboardGraph.getLongestPath(
                            gameboardGraph.getRoad(37),
                            player1,
                new HashSet<>(),
                new HashSet<>(),
                            gameboardGraph.getVertex(35)));

        
        // since player 1 lost the longest road card because they were
        // overtaken they lose the card. But because neither player 2
        // nor 4 has the longest road (they are tied), nobody gets the card
        assertFalse(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_SETTLEMENT, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());
    }

    @Test
    public void testLongestRoadPlayerBrokenAndEverybodyShort() {
        // here no one has it, so we assert on those states
        assertFalse(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());

        // ------------------- Player 1 build enough to get the longest road ---------------------------
        // Note here the controller default to player 1 first
        // give the player enough resources to build 7 roads
        for (int i = 0; i < 7; i++) {
            player1.hand.addResources(RESOURCES_FOR_ROAD);
        }
        // build some roads make sure they succeed
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(26));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(27));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(28));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(29));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(30));

        // at this point player 1 should have the longest path of 7
        assertEquals(7, gameboardGraph.getLongestPath(
                            gameboardGraph.getRoad(25),
                            player1,
                new HashSet<>(),
                new HashSet<>(),
                            gameboardGraph.getVertex(18)));

        // player 1 should have the longest road card
        assertTrue(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_LONGEST_ROAD, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());

        // ----------------------- Player 2 build and breaks player 1 but is short ---------------------------
        // move to next person
        controller.endTurn();
        assertEquals(player2, controller.getCurrentPlayer());

        // give the player enough resources to build 4 roads
        for (int i = 0; i < 4; i++) {
            controller.getCurrentPlayer().hand.addResources(RESOURCES_FOR_ROAD);
        }
        // build some roads
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(51));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(44));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(36));

        // build the settlement to break player1's path
        controller.getCurrentPlayer().hand.addResources(RESOURCES_FOR_SETTLEMENT);
        controller.setState(GameState.BUILD_SETTLEMENT);
        assertEquals(SuccessCode.SUCCESS, controller.clickedVertex(22));

        // at this point player 2 should have the longest path of 5
        assertEquals(4, gameboardGraph.getLongestPath(
                            gameboardGraph.getRoad(58),
                            player2,
                new HashSet<>(),
                new HashSet<>(),
                            gameboardGraph.getVertex(43)));

        // at this point player 1 should have the longest path of 4
        assertEquals(4, gameboardGraph.getLongestPath(
                            gameboardGraph.getRoad(25),
                            player1,
                new HashSet<>(),
                new HashSet<>(),
                            gameboardGraph.getVertex(18)));

        // at this point player 1 should have another path of 3
        assertEquals(3, gameboardGraph.getLongestPath(
                            gameboardGraph.getRoad(37),
                            player1,
                new HashSet<>(),
                new HashSet<>(),
                            gameboardGraph.getVertex(35)));


        // nobody should have the card now
        assertFalse(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_SETTLEMENT, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());
    }

    @Test
    public void testLongestRoadPlayerBrokenAndTwoOtherTieThenClaim() {
        // here no one has it, so we assert on those states
        assertFalse(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());

        // ------------------- Player 1 build enough to get the longest road ---------------------------
        // Note here the controller default to player 1 first
        // give the player enough resources to build 7 roads
        for (int i = 0; i < 7; i++) {
            player1.hand.addResources(RESOURCES_FOR_ROAD);
        }
        // build some roads make sure they succeed
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(26));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(27));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(28));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(29));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(30));

        // at this point player 1 should have the longest path of 7
        assertEquals(7, gameboardGraph.getLongestPath(
                            gameboardGraph.getRoad(25),
                            player1,
                new HashSet<>(),
                new HashSet<>(),
                            gameboardGraph.getVertex(18)));

        // player 1 should have the longest road card
        assertTrue(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_LONGEST_ROAD, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());

        // ----------------------- Player 2 build till 5 ---------------------------
        // move to next person
        controller.endTurn();
        assertEquals(player2, controller.getCurrentPlayer());

        // give the player enough resources to build 4 roads
        for (int i = 0; i < 4; i++) {
            controller.getCurrentPlayer().hand.addResources(RESOURCES_FOR_ROAD);
        }
        // build some roads
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(51));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(44));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(36));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(64));

        // at this point player 2 should have the longest path of 5
        assertEquals(5, gameboardGraph.getLongestPath(
                            gameboardGraph.getRoad(64),
                            player2,
                new HashSet<>(),
                new HashSet<>(),
                            gameboardGraph.getVertex(51)));


        // player 1 should have the card still
        assertTrue(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_LONGEST_ROAD, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());


        // move us to player 4
        assertEquals(SuccessCode.SUCCESS, controller.endTurn());
        assertEquals(player3, controller.getCurrentPlayer());
        controller.setState(GameState.DEFAULT); // imagine we did normal stuff in player 3's turn that is not relevant
        assertEquals(SuccessCode.SUCCESS, controller.endTurn());
        assertEquals(player4, controller.getCurrentPlayer());

        // give the player enough resources to build 4 roads
        for (int i = 0; i < 4; i++) {
            controller.getCurrentPlayer().hand.addResources(RESOURCES_FOR_ROAD);
        }
        // build some roads
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(12));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(11));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(10));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(18));

        // at this point player 4 should have the longest path of 5
        assertEquals(5, gameboardGraph.getLongestPath(
                            gameboardGraph.getRoad(18),
                            player4,
                new HashSet<>(),
                new HashSet<>(),
                            gameboardGraph.getVertex(17)));

        // player 1 should have the card still
        assertTrue(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_LONGEST_ROAD, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());


        // move us to player 2
        assertEquals(SuccessCode.SUCCESS, controller.endTurn());
        assertEquals(player1, controller.getCurrentPlayer());
        controller.setState(GameState.DEFAULT); // imagine we did normal stuff in player 1's turn that is not relevant
        assertEquals(SuccessCode.SUCCESS, controller.endTurn());
        assertEquals(player2, controller.getCurrentPlayer());


        // build the settlement to break player1's path
        controller.getCurrentPlayer().hand.addResources(RESOURCES_FOR_SETTLEMENT);
        controller.setState(GameState.BUILD_SETTLEMENT);
        assertEquals(SuccessCode.SUCCESS, controller.clickedVertex(22));

        // at this point player 2 should have the longest path of 5
        assertEquals(5, gameboardGraph.getLongestPath(
                            gameboardGraph.getRoad(64),
                            player2,
                new HashSet<>(),
                new HashSet<>(),
                            gameboardGraph.getVertex(51)));

        // at this point player 4 should have the longest path of 5
        assertEquals(5, gameboardGraph.getLongestPath(
                            gameboardGraph.getRoad(18),
                            player4,
                new HashSet<>(),
                new HashSet<>(),
                            gameboardGraph.getVertex(17)));

        // at this point player 1 should have the longest path of 4
        assertEquals(4, gameboardGraph.getLongestPath(
                            gameboardGraph.getRoad(25),
                            player1,
                new HashSet<>(),
                new HashSet<>(),
                            gameboardGraph.getVertex(18)));

        // at this point player 1 should have another path of 3
        assertEquals(3, gameboardGraph.getLongestPath(
                            gameboardGraph.getRoad(37),
                            player1,
                new HashSet<>(),
                new HashSet<>(),
                            gameboardGraph.getVertex(35)));

        
        // since player 1 lost the longest road card because they were
        // overtaken they lose the card. But because neither player 2
        // nor 4 has the longest road (they are tied), nobody gets the card
        assertFalse(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_SETTLEMENT, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());

        // move us to player 4
        assertEquals(SuccessCode.SUCCESS, controller.endTurn());
        assertEquals(player3, controller.getCurrentPlayer());
        controller.setState(GameState.DEFAULT); // imagine we did normal stuff in player 3's turn that is not relevant
        assertEquals(SuccessCode.SUCCESS, controller.endTurn());
        assertEquals(player4, controller.getCurrentPlayer());

        // give the player enough resources to build a road
        controller.getCurrentPlayer().hand.addResources(RESOURCES_FOR_ROAD);

        // build the road
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(23));

        // at this point player 4 should have the longest path of 6
        assertEquals(6, gameboardGraph.getLongestPath(
                            gameboardGraph.getRoad(23),
                            player4,
                new HashSet<>(),
                new HashSet<>(),
                            gameboardGraph.getVertex(16)));

        // at this point player 2 should have the longest path of 5
        assertEquals(5, gameboardGraph.getLongestPath(
                            gameboardGraph.getRoad(64),
                            player2,
                new HashSet<>(),
                new HashSet<>(),
                            gameboardGraph.getVertex(51)));

        // at this point player 1 should have the longest path of 4
        assertEquals(4, gameboardGraph.getLongestPath(
                            gameboardGraph.getRoad(25),
                            player1,
                new HashSet<>(),
                new HashSet<>(),
                            gameboardGraph.getVertex(18)));

        // at this point player 1 should have another path of 3
        assertEquals(3, gameboardGraph.getLongestPath(
                            gameboardGraph.getRoad(37),
                            player1,
                new HashSet<>(),
                new HashSet<>(),
                            gameboardGraph.getVertex(35)));


        // player 4 should have the card now
        assertFalse(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertTrue(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_SETTLEMENT, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_LONGEST_ROAD, player4.getVictoryPoints());
    }
}
