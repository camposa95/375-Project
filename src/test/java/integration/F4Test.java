package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import controller.Controller;
import controller.GamePhase;
import controller.GameState;
import controller.SuccessCode;
import gamedatastructures.DevelopmentCardDeck;
import gamedatastructures.Game;
import gamedatastructures.GameBoard;
import gamedatastructures.GameType;
import gamedatastructures.Player;
import graphs.RoadGraph;
import graphs.VertexGraph;

/**
 * The purpose of this test class is to test feature 4 (F4):
 *      Require the players to place down 2x (1 settlement, 1 road)
 *      during a set-up phase, the first set in player, the second 
 *      set in reverse play order.
 */
public class F4Test {
    private static final String GAMEBOARD_LAYOUT_FILE = "src/main/java/gamedatastructures/TileLayout.txt";

    private static final String ROAD_TO_ROAD_LAYOUT_FILE = "src/main/java/graphs/RoadToRoadLayout.txt";
    private static final String ROAD_TO_VERTEX_LAYOUT_FILE = "src/main/java/graphs/RoadToVertexLayout.txt";
    private static final String VERTEX_TO_VERTEX_LAYOUT_FILE = "src/main/java/graphs/VertexToVertexLayout.txt";
    private static final String VERTEX_TO_ROAD_LAYOUT_FILE = "src/main/java/graphs/VertexToRoadLayout.txt";
    private static final String VERTEX_TO_PORT_LAYOUT_FILE = "src/main/java/graphs/VertexToPortLayout.txt";
    
    @Test
    public void testF4Advanced() {

        // Here are some basic wiring needed that would be done by main
        // declare some constants up here
        GameType gameType = GameType.Advanced;

        // graphs
        VertexGraph vertexes = new VertexGraph();
        RoadGraph roads = new RoadGraph();
        vertexes.initializeVertexToVertexAdjacency(VERTEX_TO_VERTEX_LAYOUT_FILE);
        vertexes.initializeVertexToRoadAdjacency(roads, VERTEX_TO_ROAD_LAYOUT_FILE);
        vertexes.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, gameType);
        roads.initializeRoadToRoadAdjacency(ROAD_TO_ROAD_LAYOUT_FILE);
        roads.initializeRoadToVertexAdjacency(vertexes, ROAD_TO_VERTEX_LAYOUT_FILE);

        // Players. Note: 3 players is enough for our purposes here
        Player player1 = new Player(1);
        Player player2 = new Player(2);
        Player player3 = new Player(3);
        Player[] players = {player1, player2, player3};

        // other things dependent on these things
        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(gameType, GAMEBOARD_LAYOUT_FILE);
        Game game = new Game(gameBoard, vertexes, roads, devCardDeck);
        Controller controller = new Controller(game, players, gameType);


        // -------------------------- Start of Actual Test Stuff ---------------------------

        // declare some constants for us to use
        int player1FirstSettlement = 18;
        int player1FirstRoad = 25;
        
        int player2FirstSettlement = 12;
        int player2FirstRoad = 14;

        int player3FirstSettlement = 44;
        int player3FirstRoad = 52;
        // reverse order now
        int player3SecondSettlement = 42;
        int player3SecondRoad = 58;

        int player2SecondSettlement = 22;
        int player2SecondRoad = 36;

        int player1SecondSettlement = 30;
        int player1SecondRoad = 42;

        // Assert that game phase and state are correct on start up
        assertEquals(GamePhase.SETUP, controller.getPhase());
        assertEquals(GameState.FIRST_SETTLEMENT, controller.getState());
        assertEquals(player1, controller.getCurrentPlayer());


        // -------------------------- first person picks their first settlement -----------------------------
        // before
        assertEquals(5, player1.getNumSettlements()); // player starts with 5 settlements
        assertEquals(0, player1.getVictoryPoints()); // player starts with 0 points
        assertEquals(null, vertexes.getVertex(player1FirstSettlement).getOwner()); // correct vertex is unowned

        // after
        assertEquals(SuccessCode.SUCCESS, controller.clickedVertex(player1FirstSettlement)); // the method succeeds
        assertEquals(4, player1.getNumSettlements()); // player used a settlment
        assertEquals(1, player1.getVictoryPoints()); // player gained 1 victory points
        assertEquals(player1, vertexes.getVertex(player1FirstSettlement).getOwner()); // correct vertex is owned by the player
        
        assertEquals(true, game.getIsSetup()); // game is also in setup
        assertEquals(GamePhase.SETUP, controller.getPhase()); // still in setup
        assertEquals(GameState.FIRST_ROAD, controller.getState()); // moved to firstRoad
        assertEquals(player1, controller.getCurrentPlayer()); // player 1 still needs to go


        //  -------------------------- first person picks their first Road -----------------------------------------
        // before
        assertEquals(15, player1.getNumRoads()); // player starts with 15 roads

        // after
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(player1FirstRoad)); // the method succeeds
        assertEquals(14, player1.getNumRoads()); // player used a Road
        assertEquals(player1, roads.getRoad(player1FirstRoad).getOwner()); // correct Road is owned by the player
        
        assertEquals(true, game.getIsSetup()); // game is also in setup
        assertEquals(GamePhase.SETUP, controller.getPhase()); // still in setup
        assertEquals(GameState.FIRST_SETTLEMENT, controller.getState()); // moved to firstSettlement
        assertEquals(player2, controller.getCurrentPlayer()); // player2's turn now


        // -------------------------- Second person picks their first settlement -----------------------------
        // before
        assertEquals(5, player2.getNumSettlements()); // player starts with 5 settlements
        assertEquals(0, player2.getVictoryPoints()); // player starts with 0 points
        assertEquals(null, vertexes.getVertex(player2FirstSettlement).getOwner()); // correct vertex is unowned

        // after
        assertEquals(SuccessCode.SUCCESS, controller.clickedVertex(player2FirstSettlement)); // the method succeeds
        assertEquals(4, player2.getNumSettlements()); // player used a settlment
        assertEquals(1, player2.getVictoryPoints()); // player gained 1 victory points
        assertEquals(player2, vertexes.getVertex(player2FirstSettlement).getOwner()); // correct vertex is owned by the player
        
        assertEquals(true, game.getIsSetup()); // game is also in setup
        assertEquals(GamePhase.SETUP, controller.getPhase()); // still in setup
        assertEquals(GameState.FIRST_ROAD, controller.getState()); // moved to firstRoad
        assertEquals(player2, controller.getCurrentPlayer()); // player 2 still needs to go


        //  -------------------------- second person picks their first Road -----------------------------------------
        // before
        assertEquals(15, player2.getNumRoads()); // player starts with 15 roads

        // after
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(player2FirstRoad)); // the method succeeds
        assertEquals(14, player2.getNumRoads()); // player used a Road
        assertEquals(player2, roads.getRoad(player2FirstRoad).getOwner()); // correct Road is owned by the player
        
        assertEquals(true, game.getIsSetup()); // game is also in setup
        assertEquals(GamePhase.SETUP, controller.getPhase()); // still in setup
        assertEquals(GameState.FIRST_SETTLEMENT, controller.getState()); // moved to firstSettlement
        assertEquals(player3, controller.getCurrentPlayer()); // player3's turn now


        // -------------------------- 3rd person picks their first settlement -----------------------------
        // before
        assertEquals(5, player3.getNumSettlements()); // player starts with 5 settlements
        assertEquals(0, player3.getVictoryPoints()); // player starts with 0 points
        assertEquals(null, vertexes.getVertex(player3FirstSettlement).getOwner()); // correct vertex is unowned

        // after
        assertEquals(SuccessCode.SUCCESS, controller.clickedVertex(player3FirstSettlement)); // the method succeeds
        assertEquals(4, player3.getNumSettlements()); // player used a settlment
        assertEquals(1, player3.getVictoryPoints()); // player gained 1 victory points
        assertEquals(player3, vertexes.getVertex(player3FirstSettlement).getOwner()); // correct vertex is owned by the player
        
        assertEquals(true, game.getIsSetup()); // game is also in setup
        assertEquals(GamePhase.SETUP, controller.getPhase()); // still in setup
        assertEquals(GameState.FIRST_ROAD, controller.getState()); // moved to firstRoad
        assertEquals(player3, controller.getCurrentPlayer()); // player 3 still needs to go


        //  -------------------------- 3rd person picks their first Road -----------------------------------------
        // before
        assertEquals(15, player3.getNumRoads()); // player starts with 15 roads

        // after
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(player3FirstRoad)); // the method succeeds
        assertEquals(14, player3.getNumRoads()); // player used a Road
        assertEquals(player3, roads.getRoad(player3FirstRoad).getOwner()); // correct Road is owned by the player
        
        assertEquals(true, game.getIsSetup()); // game is also in setup
        assertEquals(GamePhase.SETUP, controller.getPhase()); // still in setup
        assertEquals(GameState.SECOND_SETTLEMENT, controller.getState()); // moved to SecondSettlement
        assertEquals(player3, controller.getCurrentPlayer()); // player3's goes again in reverse order


        // -------------------------- 3rd person picks their Second settlement -----------------------------
        // before
        assertEquals(4, player3.getNumSettlements()); // player starts with 4 settlements
        assertEquals(1, player3.getVictoryPoints()); // player starts with 1 points
        assertEquals(null, vertexes.getVertex(player3SecondSettlement).getOwner()); // correct vertex is unowned

        // after
        assertEquals(SuccessCode.SUCCESS, controller.clickedVertex(player3SecondSettlement)); // the method succeeds
        assertEquals(3, player3.getNumSettlements()); // player used a settlment
        assertEquals(2, player3.getVictoryPoints()); // player gained 1 victory points
        assertEquals(player3, vertexes.getVertex(player3SecondSettlement).getOwner()); // correct vertex is owned by the player
        
        assertEquals(true, game.getIsSetup()); // game is also in setup
        assertEquals(GamePhase.SETUP, controller.getPhase()); // still in setup
        assertEquals(GameState.SECOND_ROAD, controller.getState()); // moved to SecondRoad
        assertEquals(player3, controller.getCurrentPlayer()); // player 3 still needs to go

        //  -------------------------- 3rd person picks their Second Road -----------------------------------------
        // before
        assertEquals(14, player3.getNumRoads()); // player starts with 14 roads

        // after
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(player3SecondRoad)); // the method succeeds
        assertEquals(13, player3.getNumRoads()); // player used a Road
        assertEquals(player3, roads.getRoad(player3SecondRoad).getOwner()); // correct Road is owned by the player
        
        assertEquals(true, game.getIsSetup()); // game is also in setup
        assertEquals(GamePhase.SETUP, controller.getPhase()); // still in setup
        assertEquals(GameState.SECOND_SETTLEMENT, controller.getState()); // moved to SecondSettlement
        assertEquals(player2, controller.getCurrentPlayer()); // player3's goes again in reverse order


        // -------------------------- Second person picks their Second settlement -----------------------------
        // before
        assertEquals(4, player2.getNumSettlements()); // player starts with 4 settlements
        assertEquals(1, player2.getVictoryPoints()); // player starts with 1 points
        assertEquals(null, vertexes.getVertex(player2SecondSettlement).getOwner()); // correct vertex is unowned

        // after
        assertEquals(SuccessCode.SUCCESS, controller.clickedVertex(player2SecondSettlement)); // the method succeeds
        assertEquals(3, player2.getNumSettlements()); // player used a settlment
        assertEquals(2, player2.getVictoryPoints()); // player gained 1 victory points
        assertEquals(player2, vertexes.getVertex(player2SecondSettlement).getOwner()); // correct vertex is owned by the player
        
        assertEquals(true, game.getIsSetup()); // game is also in setup
        assertEquals(GamePhase.SETUP, controller.getPhase()); // still in setup
        assertEquals(GameState.SECOND_ROAD, controller.getState()); // moved to SecondRoad
        assertEquals(player2, controller.getCurrentPlayer()); // player 2 still needs to go

        //  -------------------------- Second person picks their Second Road -----------------------------------------
        // before
        assertEquals(14, player2.getNumRoads()); // player starts with 14 roads

        // after
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(player2SecondRoad)); // the method succeeds
        assertEquals(13, player2.getNumRoads()); // player used a Road
        assertEquals(player2, roads.getRoad(player2SecondRoad).getOwner()); // correct Road is owned by the player
        
        assertEquals(true, game.getIsSetup()); // game is also in setup
        assertEquals(GamePhase.SETUP, controller.getPhase()); // still in setup
        assertEquals(GameState.SECOND_SETTLEMENT, controller.getState()); // moved to SecondSettlement
        assertEquals(player1, controller.getCurrentPlayer()); // player3's goes again in reverse order


        // -------------------------- First person picks their Second settlement -----------------------------
        // before
        assertEquals(4, player1.getNumSettlements()); // player starts with 4 settlements
        assertEquals(1, player1.getVictoryPoints()); // player starts with 1 points
        assertEquals(null, vertexes.getVertex(player1SecondSettlement).getOwner()); // correct vertex is unowned

        // after
        assertEquals(SuccessCode.SUCCESS, controller.clickedVertex(player1SecondSettlement)); // the method succeeds
        assertEquals(3, player1.getNumSettlements()); // player used a settlment
        assertEquals(2, player1.getVictoryPoints()); // player gained 1 victory points
        assertEquals(player1, vertexes.getVertex(player1SecondSettlement).getOwner()); // correct vertex is owned by the player
        
        assertEquals(true, game.getIsSetup()); // game is also in setup
        assertEquals(GamePhase.SETUP, controller.getPhase()); // still in setup
        assertEquals(GameState.SECOND_ROAD, controller.getState()); // moved to SecondRoad
        assertEquals(player1, controller.getCurrentPlayer()); // player 1 still needs to go

        //  -------------------------- First person picks their Second Road -----------------------------------------
        // before
        assertEquals(14, player1.getNumRoads()); // player starts with 14 roads

        // after
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(player1SecondRoad)); // the method succeeds
        assertEquals(13, player1.getNumRoads()); // player used a Road
        assertEquals(player1, roads.getRoad(player1SecondRoad).getOwner()); // correct Road is owned by the player
        
        assertEquals(false, game.getIsSetup()); // game has also moved away from setup
        assertEquals(GamePhase.REGULAR_PLAY, controller.getPhase()); // Moved to regularPlay
        assertEquals(GameState.TURN_START, controller.getState()); // set to begining of turn
        assertEquals(player1, controller.getCurrentPlayer()); // player1 starts the regular play off
    }

    @Test
    public void testF4Begineer4player() {

        // Here are some basic wiring needed that would be done by main
        // declare some constants up here
        GameType gameType = GameType.Beginner;

        // graphs
        VertexGraph vertexes = new VertexGraph();
        RoadGraph roads = new RoadGraph();
        vertexes.initializeVertexToVertexAdjacency(VERTEX_TO_VERTEX_LAYOUT_FILE);
        vertexes.initializeVertexToRoadAdjacency(roads, VERTEX_TO_ROAD_LAYOUT_FILE);
        vertexes.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, gameType);
        roads.initializeRoadToRoadAdjacency(ROAD_TO_ROAD_LAYOUT_FILE);
        roads.initializeRoadToVertexAdjacency(vertexes, ROAD_TO_VERTEX_LAYOUT_FILE);

        // Players. Note: 3 players is enough for our purposes here
        Player player1 = new Player(1);
        Player player2 = new Player(2);
        Player player3 = new Player(3);
        Player player4 = new Player(4);

        Player[] players = {player1, player2, player3, player4};

        // other things dependent on these things
        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(gameType, GAMEBOARD_LAYOUT_FILE);
        Game game = new Game(gameBoard, vertexes, roads, devCardDeck);
        
        // Assert that the begineer setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            controllerRef.set(new Controller(game, players, gameType));
        }, "Setup while loop timed out");
        Controller controller = controllerRef.get();


        // -------------------------- Start of Actual Test Stuff ---------------------------

        // declare some constants for us to use
        int player1FirstSettlement = 35;
        int player1FirstRoad = 37;
        int player1SecondSettlement = 19;
        int player1SecondRoad = 25;
        
        int player2FirstSettlement = 13;
        int player2FirstRoad = 15;
        int player2SecondSettlement = 42;
        int player2SecondRoad = 58;

        int player3FirstSettlement = 44;
        int player3FirstRoad = 52;
        int player3SecondSettlement = 40;
        int player3SecondRoad = 56;

        int player4FirstSettlement = 10;
        int player4FirstRoad = 13;
        int player4SecondSettlement = 29;
        int player4SecondRoad = 41;

        // Now we assert that the players had their starting stuff auto generated

        // --------------------------------- Player 1 stuff -------------------------------------------------------

        // assert player has correct stats
        assertEquals(3, player1.getNumSettlements());
        assertEquals(2, player1.getVictoryPoints());

        // assert player has correct locations
        assertEquals(player1, vertexes.getVertex(player1FirstSettlement).getOwner());
        assertEquals(player1, roads.getRoad(player1FirstRoad).getOwner());
        assertEquals(player1, vertexes.getVertex(player1SecondSettlement).getOwner());
        assertEquals(player1, roads.getRoad(player1SecondRoad).getOwner());


        // --------------------------------- Player 2 stuff -------------------------------------------------------

        // assert player has correct stats
        assertEquals(3, player2.getNumSettlements());
        assertEquals(2, player2.getVictoryPoints());

        // assert player has correct locations
        assertEquals(player2, vertexes.getVertex(player2FirstSettlement).getOwner());
        assertEquals(player2, roads.getRoad(player2FirstRoad).getOwner());
        assertEquals(player2, vertexes.getVertex(player2SecondSettlement).getOwner());
        assertEquals(player2, roads.getRoad(player2SecondRoad).getOwner());


        // --------------------------------- Player 3 stuff -------------------------------------------------------

        // assert player has correct stats
        assertEquals(3, player3.getNumSettlements());
        assertEquals(2, player3.getVictoryPoints());

        // assert player has correct locations
        assertEquals(player3, vertexes.getVertex(player3FirstSettlement).getOwner());
        assertEquals(player3, roads.getRoad(player3FirstRoad).getOwner());
        assertEquals(player3, vertexes.getVertex(player3SecondSettlement).getOwner());
        assertEquals(player3, roads.getRoad(player3SecondRoad).getOwner());


        // --------------------------------- Player 4 stuff -------------------------------------------------------

        // assert player has correct stats
        assertEquals(3, player4.getNumSettlements());
        assertEquals(2, player4.getVictoryPoints());

        // assert player has correct locations
        assertEquals(player4, vertexes.getVertex(player4FirstSettlement).getOwner());
        assertEquals(player4, roads.getRoad(player4FirstRoad).getOwner());
        assertEquals(player4, vertexes.getVertex(player4SecondSettlement).getOwner());
        assertEquals(player4, roads.getRoad(player4SecondRoad).getOwner());


        // Assert that game phase and state are correct on start up
        assertEquals(GamePhase.REGULAR_PLAY, controller.getPhase());
        assertEquals(GameState.TURN_START, controller.getState());
        assertEquals(player1, controller.getCurrentPlayer());
    }

    @Test
    public void testF4Begineer2player() {

        // Here are some basic wiring needed that would be done by main
        // declare some constants up here
        GameType gameType = GameType.Beginner;

        // graphs
        VertexGraph vertexes = new VertexGraph();
        RoadGraph roads = new RoadGraph();
        vertexes.initializeVertexToVertexAdjacency(VERTEX_TO_VERTEX_LAYOUT_FILE);
        vertexes.initializeVertexToRoadAdjacency(roads, VERTEX_TO_ROAD_LAYOUT_FILE);
        vertexes.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, gameType);
        roads.initializeRoadToRoadAdjacency(ROAD_TO_ROAD_LAYOUT_FILE);
        roads.initializeRoadToVertexAdjacency(vertexes, ROAD_TO_VERTEX_LAYOUT_FILE);

        // Players. Note: 3 players is enough for our purposes here
        Player player1 = new Player(1);
        Player player2 = new Player(2);

        Player[] players = {player1, player2};

        // other things dependent on these things
        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(gameType, GAMEBOARD_LAYOUT_FILE);
        Game game = new Game(gameBoard, vertexes, roads, devCardDeck);
        
        // Assert that the begineer setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            controllerRef.set(new Controller(game, players, gameType));
        }, "Setup while loop timed out");
        Controller controller = controllerRef.get();


        // -------------------------- Start of Actual Test Stuff ---------------------------

        // declare some constants for us to use
        int player1FirstSettlement = 35;
        int player1FirstRoad = 37;
        int player1SecondSettlement = 19;
        int player1SecondRoad = 25;
        
        int player2FirstSettlement = 13;
        int player2FirstRoad = 15;
        int player2SecondSettlement = 42;
        int player2SecondRoad = 58;

        int player3FirstSettlement = 44;
        int player3FirstRoad = 52;
        int player3SecondSettlement = 40;
        int player3SecondRoad = 56;

        int player4FirstSettlement = 10;
        int player4FirstRoad = 13;
        int player4SecondSettlement = 29;
        int player4SecondRoad = 41;

        // Now we assert that the players had their starting stuff auto generated

        // --------------------------------- Player 1 stuff -------------------------------------------------------

        // assert player has correct stats
        assertEquals(3, player1.getNumSettlements());
        assertEquals(2, player1.getVictoryPoints());

        // assert player has correct locations
        assertEquals(player1, vertexes.getVertex(player1FirstSettlement).getOwner());
        assertEquals(player1, roads.getRoad(player1FirstRoad).getOwner());
        assertEquals(player1, vertexes.getVertex(player1SecondSettlement).getOwner());
        assertEquals(player1, roads.getRoad(player1SecondRoad).getOwner());


        // --------------------------------- Player 2 stuff -------------------------------------------------------

        // assert player has correct stats
        assertEquals(3, player2.getNumSettlements());
        assertEquals(2, player2.getVictoryPoints());

        // assert player has correct locations
        assertEquals(player2, vertexes.getVertex(player2FirstSettlement).getOwner());
        assertEquals(player2, roads.getRoad(player2FirstRoad).getOwner());
        assertEquals(player2, vertexes.getVertex(player2SecondSettlement).getOwner());
        assertEquals(player2, roads.getRoad(player2SecondRoad).getOwner());


        // ------------------- Assert that starter locations of Non-existant players are untouched ----------------

        // player 3
        assertEquals(null, vertexes.getVertex(player3FirstSettlement).getOwner());
        assertEquals(null, roads.getRoad(player3FirstRoad).getOwner());
        assertEquals(null, vertexes.getVertex(player3SecondSettlement).getOwner());
        assertEquals(null, roads.getRoad(player3SecondRoad).getOwner());

        // player 4
        assertEquals(null, vertexes.getVertex(player4FirstSettlement).getOwner());
        assertEquals(null, roads.getRoad(player4FirstRoad).getOwner());
        assertEquals(null, vertexes.getVertex(player4SecondSettlement).getOwner());
        assertEquals(null, roads.getRoad(player4SecondRoad).getOwner());


        // Assert that game phase and state are correct on start up
        assertEquals(GamePhase.REGULAR_PLAY, controller.getPhase());
        assertEquals(GameState.TURN_START, controller.getState());
        assertEquals(player1, controller.getCurrentPlayer());
    }
}
