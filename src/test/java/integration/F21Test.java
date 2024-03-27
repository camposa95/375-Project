package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicReference;

import data.GameLoader;
import org.junit.jupiter.api.Assertions;
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
import domain.graphs.Road;
import domain.graphs.RoadGraph;
import domain.graphs.VertexGraph;

/**
 * The purpose of this test class is to test feature 21 (F21):
 *      Ability for a Player to collect 2 Victory Points for having the Longest Road,
 *      longer than 5 road pieces. Another player can steal these points if they build a longer road.
 */
public class F21Test {

    private static final Resource[] RESOURCES_FOR_ROAD = {Resource.BRICK, Resource.LUMBER};
    private static final Resource[] RESOURCES_FOR_SETTLMENT = {Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN};

    private static final int POINTS_FROM_SETUP = 2;
    private static final int POINTS_FOR_SETTLMENT = 1;
    private static final int POINTS_FOR_LONGEST_ROAD = 2;
    
    @Test
    public void testLongestRoadNoOneEligible() {
        // ---------------------- Here are some basic wiring needed that would be done by main ------------------------------
        
        // Here we use begineer game to skip through to the regular gameplay
        GameType gameType = GameType.Beginner;
        VertexGraph vertexes = new VertexGraph(gameType);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        // Players. Note: 3 players is enough for our purposes here
        Player player1 = new Player(1);
        Player player2 = new Player(2);
        Player player3 = new Player(3);
        Player player4 = new Player(4);

        Player[] players = {player1, player2, player3, player4};

        // other things dependent on these things
        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        Game game = new Game(gameBoard, vertexes, roads, devCardDeck);
        
        // Assert that the begineer setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            controllerRef.set(new Controller(game, players, gameType));
        }, "Setup while loop timed out");

        // Note: we assume everything about setup was correct because it was tested earlier

        // Note: at this point the players would have gotten some starter resources during the 
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the players's hand and assert that the players have zero resources to controll
        // the test more
        for (Player player: players) {
            for (Resource resource: Resource.values()) {
                if (resource != Resource.ANY) { // skip this one used for trading
                    int count = player.hand.getResourceCardAmount(resource);
                    if (count > 0) {
                        player.hand.removeResource(resource, count);
                    }
                }
            }
            assertEquals(0, player.hand.getResourceCardCount());
        }

        // -------------------------- Start of Actual Test Stuff ---------------------------

        // here no one has it so we assert on those states
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
        // ---------------------- Here are some basic wiring needed that would be done by main ------------------------------
        
        // Here we use begineer game to skip through to the regular gameplay
        GameType gameType = GameType.Beginner;
        VertexGraph vertexes = new VertexGraph(gameType);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        // Players. Note: 3 players is enough for our purposes here
        Player player1 = new Player(1);
        Player player2 = new Player(2);
        Player player3 = new Player(3);
        Player player4 = new Player(4);

        Player[] players = {player1, player2, player3, player4};

        // other things dependent on these things
        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        Game game = new Game(gameBoard, vertexes, roads, devCardDeck);
        
        // Assert that the begineer setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            controllerRef.set(new Controller(game, players, gameType));
        }, "Setup while loop timed out");
        Controller controller = controllerRef.get();

        // Note: we assume everything about setup was correct because it was tested earlier

        // Note: at this point the players would have gotten some starter resources during the 
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the players's hand and assert that the players have zero resources to controll
        // the test more
        for (Player player: players) {
            for (Resource resource: Resource.values()) {
                if (resource != Resource.ANY) { // skip this one used for trading
                    int count = player.hand.getResourceCardAmount(resource);
                    if (count > 0) {
                        player.hand.removeResource(resource, count);
                    }
                }
            }
            assertEquals(0, player.hand.getResourceCardCount());
        }

        // -------------------------- Start of Actual Test Stuff ---------------------------
        // here no one has it so we assert on those states
        assertFalse(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());

        // -------------------------- Player 1 build enough to get he longes road ---------------------------
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
        // ---------------------- Here are some basic wiring needed that would be done by main ------------------------------
        
        // Here we use begineer game to skip through to the regular gameplay
        GameType gameType = GameType.Beginner;
        VertexGraph vertexes = new VertexGraph(gameType);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        // Players. Note: 3 players is enough for our purposes here
        Player player1 = new Player(1);
        Player player2 = new Player(2);
        Player player3 = new Player(3);
        Player player4 = new Player(4);

        Player[] players = {player1, player2, player3, player4};

        // other things dependent on these things
        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        Game game = new Game(gameBoard, vertexes, roads, devCardDeck);
        
        // Assert that the begineer setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            controllerRef.set(new Controller(game, players, gameType));
        }, "Setup while loop timed out");
        Controller controller = controllerRef.get();

        // Note: we assume everything about setup was correct because it was tested earlier

        // Note: at this point the players would have gotten some starter resources during the 
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the players's hand and assert that the players have zero resources to controll
        // the test more
        for (Player player: players) {
            for (Resource resource: Resource.values()) {
                if (resource != Resource.ANY) { // skip this one used for trading
                    int count = player.hand.getResourceCardAmount(resource);
                    if (count > 0) {
                        player.hand.removeResource(resource, count);
                    }
                }
            }
            assertEquals(0, player.hand.getResourceCardCount());
        }

        // -------------------------- Start of Actual Test Stuff ---------------------------
        // here no one has it so we assert on those states
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
        // ---------------------- Here are some basic wiring needed that would be done by main ------------------------------
        
        // Here we use begineer game to skip through to the regular gameplay
        GameType gameType = GameType.Beginner;
        VertexGraph vertexes = new VertexGraph(gameType);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        // Players. Note: 3 players is enough for our purposes here
        Player player1 = new Player(1);
        Player player2 = new Player(2);
        Player player3 = new Player(3);
        Player player4 = new Player(4);

        Player[] players = {player1, player2, player3, player4};

        // other things dependent on these things
        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        Game game = new Game(gameBoard, vertexes, roads, devCardDeck);
        
        // Assert that the begineer setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            controllerRef.set(new Controller(game, players, gameType));
        }, "Setup while loop timed out");
        Controller controller = controllerRef.get();

        // Note: we assume everything about setup was correct because it was tested earlier

        // Note: at this point the players would have gotten some starter resources during the 
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the players's hand and assert that the players have zero resources to controll
        // the test more
        for (Player player: players) {
            for (Resource resource: Resource.values()) {
                if (resource != Resource.ANY) { // skip this one used for trading
                    int count = player.hand.getResourceCardAmount(resource);
                    if (count > 0) {
                        player.hand.removeResource(resource, count);
                    }
                }
            }
            assertEquals(0, player.hand.getResourceCardCount());
        }

        // -------------------------- Start of Actual Test Stuff ---------------------------
        // here no one has it so we assert on those states
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
        
        // Here we use begineer game to skip through to the regular gameplay
        GameType gameType = GameType.Beginner;
        VertexGraph vertexes = new VertexGraph(gameType);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        // Players. Note: 3 players is enough for our purposes here
        Player player1 = new Player(1);
        Player player2 = new Player(2);
        Player player3 = new Player(3);
        Player player4 = new Player(4);

        Player[] players = {player1, player2, player3, player4};

        // other things dependent on these things
        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        Game game = new Game(gameBoard, vertexes, roads, devCardDeck);
        
        // Assert that the begineer setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            controllerRef.set(new Controller(game, players, gameType));
        }, "Setup while loop timed out");
        Controller controller = controllerRef.get();

        // Note: we assume everything about setup was correct because it was tested earlier

        // Note: at this point the players would have gotten some starter resources during the 
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the players's hand and assert that the players have zero resources to controll
        // the test more
        for (Player player: players) {
            for (Resource resource: Resource.values()) {
                if (resource != Resource.ANY) { // skip this one used for trading
                    int count = player.hand.getResourceCardAmount(resource);
                    if (count > 0) {
                        player.hand.removeResource(resource, count);
                    }
                }
            }
            assertEquals(0, player.hand.getResourceCardCount());
        }

        // -------------------------- Start of Actual Test Stuff ---------------------------
        // here no one has it so we assert on those states
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

        // at this point player 1 should have a longes path of 9
        assertEquals(9, roads.getLongestPath(
                            roads.getRoad(23),
                            player1,
                            new HashSet<HashSet<Road>>(),
                            new HashSet<Road>(),
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

        // build the settlment to break player1's path
        controller.getCurrentPlayer().hand.addResources(RESOURCES_FOR_SETTLMENT);
        controller.setState(GameState.BUILD_SETTLEMENT);
        assertEquals(SuccessCode.SUCCESS, controller.clickedVertex(22));

        // at this point player 2 should have a longest path of 5
        assertEquals(5, roads.getLongestPath(
                            roads.getRoad(64),
                            player2,
                            new HashSet<HashSet<Road>>(),
                            new HashSet<Road>(),
                            vertexes.getVertex(51)));

        // at this point player 1 should have a longest path of 6
        assertEquals(6, roads.getLongestPath(
                            roads.getRoad(23),
                            player1,
                            new HashSet<HashSet<Road>>(),
                            new HashSet<Road>(),
                            vertexes.getVertex(16)));

        // at this point player 1 should have another path of 3
        assertEquals(3, roads.getLongestPath(
                            roads.getRoad(37),
                            player1,
                            new HashSet<HashSet<Road>>(),
                            new HashSet<Road>(),
                            vertexes.getVertex(35)));


        // player 1 should still have the longest road card
        assertTrue(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_LONGEST_ROAD, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_SETTLMENT, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());
    }

    @Test
    public void testLongestRoadPlayerBrokenAndOvertaken() {
        // ---------------------- Here are some basic wiring needed that would be done by main ------------------------------
        
        // Here we use begineer game to skip through to the regular gameplay
        GameType gameType = GameType.Beginner;
        VertexGraph vertexes = new VertexGraph(gameType);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        // Players. Note: 3 players is enough for our purposes here
        Player player1 = new Player(1);
        Player player2 = new Player(2);
        Player player3 = new Player(3);
        Player player4 = new Player(4);

        Player[] players = {player1, player2, player3, player4};

        // other things dependent on these things
        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        Game game = new Game(gameBoard, vertexes, roads, devCardDeck);
        
        // Assert that the begineer setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            controllerRef.set(new Controller(game, players, gameType));
        }, "Setup while loop timed out");
        Controller controller = controllerRef.get();

        // Note: we assume everything about setup was correct because it was tested earlier

        // Note: at this point the players would have gotten some starter resources during the 
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the players's hand and assert that the players have zero resources to controll
        // the test more
        for (Player player: players) {
            for (Resource resource: Resource.values()) {
                if (resource != Resource.ANY) { // skip this one used for trading
                    int count = player.hand.getResourceCardAmount(resource);
                    if (count > 0) {
                        player.hand.removeResource(resource, count);
                    }
                }
            }
            assertEquals(0, player.hand.getResourceCardCount());
        }

        // -------------------------- Start of Actual Test Stuff ---------------------------
        // here no one has it so we assert on those states
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

        // at this point player 1 should have a longes path of 7
        assertEquals(7, roads.getLongestPath(
                            roads.getRoad(25),
                            player1,
                            new HashSet<HashSet<Road>>(),
                            new HashSet<Road>(),
                            vertexes.getVertex(18)));

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

        // build the settlment to break player1's path
        controller.getCurrentPlayer().hand.addResources(RESOURCES_FOR_SETTLMENT);
        controller.setState(GameState.BUILD_SETTLEMENT);
        assertEquals(SuccessCode.SUCCESS, controller.clickedVertex(22));

        // at this point player 2 should have a longest path of 5
        assertEquals(5, roads.getLongestPath(
                            roads.getRoad(64),
                            player2,
                            new HashSet<HashSet<Road>>(),
                            new HashSet<Road>(),
                            vertexes.getVertex(51)));

        // at this point player 1 should have a longest path of 4
        assertEquals(4, roads.getLongestPath(
                            roads.getRoad(25),
                            player1,
                            new HashSet<HashSet<Road>>(),
                            new HashSet<Road>(),
                            vertexes.getVertex(18)));

        // at this point player 1 should have another path of 3
        assertEquals(3, roads.getLongestPath(
                            roads.getRoad(37),
                            player1,
                            new HashSet<HashSet<Road>>(),
                            new HashSet<Road>(),
                            vertexes.getVertex(35)));


        // player 2 should have the card now
        assertFalse(player1.hasLongestRoad());
        assertTrue(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_SETTLMENT + POINTS_FOR_LONGEST_ROAD, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());
    }

    @Test
    public void testLongestRoadPlayerBrokenAndTied() {
        // ---------------------- Here are some basic wiring needed that would be done by main ------------------------------
        
        // Here we use begineer game to skip through to the regular gameplay
        GameType gameType = GameType.Beginner;
        VertexGraph vertexes = new VertexGraph(gameType);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        // Players. Note: 3 players is enough for our purposes here
        Player player1 = new Player(1);
        Player player2 = new Player(2);
        Player player3 = new Player(3);
        Player player4 = new Player(4);

        Player[] players = {player1, player2, player3, player4};

        // other things dependent on these things
        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        Game game = new Game(gameBoard, vertexes, roads, devCardDeck);
        
        // Assert that the begineer setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            controllerRef.set(new Controller(game, players, gameType));
        }, "Setup while loop timed out");
        Controller controller = controllerRef.get();

        // Note: we assume everything about setup was correct because it was tested earlier

        // Note: at this point the players would have gotten some starter resources during the 
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the players's hand and assert that the players have zero resources to controll
        // the test more
        for (Player player: players) {
            for (Resource resource: Resource.values()) {
                if (resource != Resource.ANY) { // skip this one used for trading
                    int count = player.hand.getResourceCardAmount(resource);
                    if (count > 0) {
                        player.hand.removeResource(resource, count);
                    }
                }
            }
            assertEquals(0, player.hand.getResourceCardCount());
        }

        // -------------------------- Start of Actual Test Stuff ---------------------------
        // here no one has it so we assert on those states
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

        // at this point player 1 should have a longes path of 8
        assertEquals(8, roads.getLongestPath(
                            roads.getRoad(24),
                            player1,
                            new HashSet<HashSet<Road>>(),
                            new HashSet<Road>(),
                            vertexes.getVertex(17)));

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

        // build the settlment to break player1's path
        controller.getCurrentPlayer().hand.addResources(RESOURCES_FOR_SETTLMENT);
        controller.setState(GameState.BUILD_SETTLEMENT);
        assertEquals(SuccessCode.SUCCESS, controller.clickedVertex(22));

        // at this point player 2 should have a longest path of 5
        assertEquals(5, roads.getLongestPath(
                            roads.getRoad(64),
                            player2,
                            new HashSet<HashSet<Road>>(),
                            new HashSet<Road>(),
                            vertexes.getVertex(51)));

        // at this point player 1 should have a longest path of 6
        assertEquals(5, roads.getLongestPath(
                            roads.getRoad(24),
                            player1,
                            new HashSet<HashSet<Road>>(),
                            new HashSet<Road>(),
                            vertexes.getVertex(17)));

        // at this point player 1 should have another path of 3
        assertEquals(3, roads.getLongestPath(
                            roads.getRoad(37),
                            player1,
                            new HashSet<HashSet<Road>>(),
                            new HashSet<Road>(),
                            vertexes.getVertex(35)));


        // player 1 should still have the longest road card
        assertTrue(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_LONGEST_ROAD, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_SETTLMENT, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());
    }

    @Test
    public void testLongestRoadPlayerBrokenAndTwoOtherTie() {
        // ---------------------- Here are some basic wiring needed that would be done by main ------------------------------
        
        // Here we use begineer game to skip through to the regular gameplay
        GameType gameType = GameType.Beginner;
        VertexGraph vertexes = new VertexGraph(gameType);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        // Players. Note: 3 players is enough for our purposes here
        Player player1 = new Player(1);
        Player player2 = new Player(2);
        Player player3 = new Player(3);
        Player player4 = new Player(4);

        Player[] players = {player1, player2, player3, player4};

        // other things dependent on these things
        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        Game game = new Game(gameBoard, vertexes, roads, devCardDeck);
        
        // Assert that the begineer setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            controllerRef.set(new Controller(game, players, gameType));
        }, "Setup while loop timed out");
        Controller controller = controllerRef.get();

        // Note: we assume everything about setup was correct because it was tested earlier

        // Note: at this point the players would have gotten some starter resources during the 
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the players's hand and assert that the players have zero resources to controll
        // the test more
        for (Player player: players) {
            for (Resource resource: Resource.values()) {
                if (resource != Resource.ANY) { // skip this one used for trading
                    int count = player.hand.getResourceCardAmount(resource);
                    if (count > 0) {
                        player.hand.removeResource(resource, count);
                    }
                }
            }
            assertEquals(0, player.hand.getResourceCardCount());
        }

        // -------------------------- Start of Actual Test Stuff ---------------------------
        // here no one has it so we assert on those states
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

        // at this point player 1 should have a longest path of 7
        assertEquals(7, roads.getLongestPath(
                            roads.getRoad(25),
                            player1,
                            new HashSet<HashSet<Road>>(),
                            new HashSet<Road>(),
                            vertexes.getVertex(18)));

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

        // at this point player 2 should have a longest path of 5
        assertEquals(5, roads.getLongestPath(
                            roads.getRoad(64),
                            player2,
                            new HashSet<HashSet<Road>>(),
                            new HashSet<Road>(),
                            vertexes.getVertex(51)));


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

        // at this point player 4 should have a longest path of 5
        assertEquals(5, roads.getLongestPath(
                            roads.getRoad(18),
                            player4,
                            new HashSet<HashSet<Road>>(),
                            new HashSet<Road>(),
                            vertexes.getVertex(17)));

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


        // build the settlment to break player1's path
        controller.getCurrentPlayer().hand.addResources(RESOURCES_FOR_SETTLMENT);
        controller.setState(GameState.BUILD_SETTLEMENT);
        assertEquals(SuccessCode.SUCCESS, controller.clickedVertex(22));

        // at this point player 2 should have a longest path of 5
        assertEquals(5, roads.getLongestPath(
                            roads.getRoad(64),
                            player2,
                            new HashSet<HashSet<Road>>(),
                            new HashSet<Road>(),
                            vertexes.getVertex(51)));

        // at this point player 4 should have a longest path of 5
        assertEquals(5, roads.getLongestPath(
                            roads.getRoad(18),
                            player4,
                            new HashSet<HashSet<Road>>(),
                            new HashSet<Road>(),
                            vertexes.getVertex(17)));

        // at this point player 1 should have a longest path of 4
        assertEquals(4, roads.getLongestPath(
                            roads.getRoad(25),
                            player1,
                            new HashSet<HashSet<Road>>(),
                            new HashSet<Road>(),
                            vertexes.getVertex(18)));

        // at this point player 1 should have another path of 3
        assertEquals(3, roads.getLongestPath(
                            roads.getRoad(37),
                            player1,
                            new HashSet<HashSet<Road>>(),
                            new HashSet<Road>(),
                            vertexes.getVertex(35)));

        
        // since player 1 lost the longest road card because they were
        // overtaken they lose the card. But because neigther player 2
        // or 4 has the longest road (they are tied), nobody gets the card
        assertFalse(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_SETTLMENT, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());
    }

    @Test
    public void testLongestRoadPlayerBrokenAndEverybodyShort() {
        // ---------------------- Here are some basic wiring needed that would be done by main ------------------------------
        
        // Here we use begineer game to skip through to the regular gameplay
        GameType gameType = GameType.Beginner;
        VertexGraph vertexes = new VertexGraph(gameType);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        // Players. Note: 3 players is enough for our purposes here
        Player player1 = new Player(1);
        Player player2 = new Player(2);
        Player player3 = new Player(3);
        Player player4 = new Player(4);

        Player[] players = {player1, player2, player3, player4};

        // other things dependent on these things
        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        Game game = new Game(gameBoard, vertexes, roads, devCardDeck);
        
        // Assert that the begineer setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            controllerRef.set(new Controller(game, players, gameType));
        }, "Setup while loop timed out");
        Controller controller = controllerRef.get();

        // Note: we assume everything about setup was correct because it was tested earlier

        // Note: at this point the players would have gotten some starter resources during the 
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the players's hand and assert that the players have zero resources to controll
        // the test more
        for (Player player: players) {
            for (Resource resource: Resource.values()) {
                if (resource != Resource.ANY) { // skip this one used for trading
                    int count = player.hand.getResourceCardAmount(resource);
                    if (count > 0) {
                        player.hand.removeResource(resource, count);
                    }
                }
            }
            assertEquals(0, player.hand.getResourceCardCount());
        }

        // -------------------------- Start of Actual Test Stuff ---------------------------
        // here no one has it so we assert on those states
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

        // at this point player 1 should have a longes path of 7
        assertEquals(7, roads.getLongestPath(
                            roads.getRoad(25),
                            player1,
                            new HashSet<HashSet<Road>>(),
                            new HashSet<Road>(),
                            vertexes.getVertex(18)));

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

        // build the settlment to break player1's path
        controller.getCurrentPlayer().hand.addResources(RESOURCES_FOR_SETTLMENT);
        controller.setState(GameState.BUILD_SETTLEMENT);
        assertEquals(SuccessCode.SUCCESS, controller.clickedVertex(22));

        // at this point player 2 should have a longest path of 5
        assertEquals(4, roads.getLongestPath(
                            roads.getRoad(58),
                            player2,
                            new HashSet<HashSet<Road>>(),
                            new HashSet<Road>(),
                            vertexes.getVertex(43)));

        // at this point player 1 should have a longest path of 4
        assertEquals(4, roads.getLongestPath(
                            roads.getRoad(25),
                            player1,
                            new HashSet<HashSet<Road>>(),
                            new HashSet<Road>(),
                            vertexes.getVertex(18)));

        // at this point player 1 should have another path of 3
        assertEquals(3, roads.getLongestPath(
                            roads.getRoad(37),
                            player1,
                            new HashSet<HashSet<Road>>(),
                            new HashSet<Road>(),
                            vertexes.getVertex(35)));


        // nobody should have the card now
        assertFalse(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_SETTLMENT, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());
    }

    @Test
    public void testLongestRoadPlayerBrokenAndTwoOtherTieThenClaim() {
        // ---------------------- Here are some basic wiring needed that would be done by main ------------------------------
        
        // Here we use begineer game to skip through to the regular gameplay
        GameType gameType = GameType.Beginner;
        VertexGraph vertexes = new VertexGraph(gameType);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        // Players. Note: 3 players is enough for our purposes here
        Player player1 = new Player(1);
        Player player2 = new Player(2);
        Player player3 = new Player(3);
        Player player4 = new Player(4);

        Player[] players = {player1, player2, player3, player4};

        // other things dependent on these things
        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        Game game = new Game(gameBoard, vertexes, roads, devCardDeck);
        
        // Assert that the begineer setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            controllerRef.set(new Controller(game, players, gameType));
        }, "Setup while loop timed out");
        Controller controller = controllerRef.get();

        // Note: we assume everything about setup was correct because it was tested earlier

        // Note: at this point the players would have gotten some starter resources during the 
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the players's hand and assert that the players have zero resources to controll
        // the test more
        for (Player player: players) {
            for (Resource resource: Resource.values()) {
                if (resource != Resource.ANY) { // skip this one used for trading
                    int count = player.hand.getResourceCardAmount(resource);
                    if (count > 0) {
                        player.hand.removeResource(resource, count);
                    }
                }
            }
            assertEquals(0, player.hand.getResourceCardCount());
        }

        // -------------------------- Start of Actual Test Stuff ---------------------------
        // here no one has it so we assert on those states
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

        // at this point player 1 should have a longest path of 7
        assertEquals(7, roads.getLongestPath(
                            roads.getRoad(25),
                            player1,
                            new HashSet<HashSet<Road>>(),
                            new HashSet<Road>(),
                            vertexes.getVertex(18)));

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

        // at this point player 2 should have a longest path of 5
        assertEquals(5, roads.getLongestPath(
                            roads.getRoad(64),
                            player2,
                            new HashSet<HashSet<Road>>(),
                            new HashSet<Road>(),
                            vertexes.getVertex(51)));


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

        // at this point player 4 should have a longest path of 5
        assertEquals(5, roads.getLongestPath(
                            roads.getRoad(18),
                            player4,
                            new HashSet<HashSet<Road>>(),
                            new HashSet<Road>(),
                            vertexes.getVertex(17)));

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


        // build the settlment to break player1's path
        controller.getCurrentPlayer().hand.addResources(RESOURCES_FOR_SETTLMENT);
        controller.setState(GameState.BUILD_SETTLEMENT);
        assertEquals(SuccessCode.SUCCESS, controller.clickedVertex(22));

        // at this point player 2 should have a longest path of 5
        assertEquals(5, roads.getLongestPath(
                            roads.getRoad(64),
                            player2,
                            new HashSet<HashSet<Road>>(),
                            new HashSet<Road>(),
                            vertexes.getVertex(51)));

        // at this point player 4 should have a longest path of 5
        assertEquals(5, roads.getLongestPath(
                            roads.getRoad(18),
                            player4,
                            new HashSet<HashSet<Road>>(),
                            new HashSet<Road>(),
                            vertexes.getVertex(17)));

        // at this point player 1 should have a longest path of 4
        assertEquals(4, roads.getLongestPath(
                            roads.getRoad(25),
                            player1,
                            new HashSet<HashSet<Road>>(),
                            new HashSet<Road>(),
                            vertexes.getVertex(18)));

        // at this point player 1 should have another path of 3
        assertEquals(3, roads.getLongestPath(
                            roads.getRoad(37),
                            player1,
                            new HashSet<HashSet<Road>>(),
                            new HashSet<Road>(),
                            vertexes.getVertex(35)));

        
        // since player 1 lost the longest road card because they were
        // overtaken they lose the card. But because neigther player 2
        // or 4 has the longest road (they are tied), nobody gets the card
        assertFalse(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_SETTLMENT, player2.getVictoryPoints());
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

        // at this point player 4 should have a longest path of 6
        assertEquals(6, roads.getLongestPath(
                            roads.getRoad(23),
                            player4,
                            new HashSet<HashSet<Road>>(),
                            new HashSet<Road>(),
                            vertexes.getVertex(16)));

        // at this point player 2 should have a longest path of 5
        assertEquals(5, roads.getLongestPath(
                            roads.getRoad(64),
                            player2,
                            new HashSet<HashSet<Road>>(),
                            new HashSet<Road>(),
                            vertexes.getVertex(51)));

        // at this point player 1 should have a longest path of 4
        assertEquals(4, roads.getLongestPath(
                            roads.getRoad(25),
                            player1,
                            new HashSet<HashSet<Road>>(),
                            new HashSet<Road>(),
                            vertexes.getVertex(18)));

        // at this point player 1 should have another path of 3
        assertEquals(3, roads.getLongestPath(
                            roads.getRoad(37),
                            player1,
                            new HashSet<HashSet<Road>>(),
                            new HashSet<Road>(),
                            vertexes.getVertex(35)));


        // player 4 should have the card now
        assertFalse(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertTrue(player4.hasLongestRoad());

        assertEquals(POINTS_FROM_SETUP, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_SETTLMENT, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_LONGEST_ROAD, player4.getVictoryPoints());
    }
}
