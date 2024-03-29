package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import controller.Controller;
import controller.GameState;
import controller.SuccessCode;
import gamedatastructures.Bank;
import gamedatastructures.DevCard;
import gamedatastructures.DevelopmentCardDeck;
import gamedatastructures.Game;
import gamedatastructures.GameBoard;
import gamedatastructures.GameType;
import gamedatastructures.Player;
import gamedatastructures.Resource;
import graphs.RoadGraph;
import graphs.VertexGraph;


/**
 * The purpose of this test class is to test feature 15 (F15):
 *   Ability for the Player to move the robber to a new space
 *   and collect a random resource from any player on the new space,
 *   when the player rolls a 7.
 */
public class F15Test {
    
    private static final String GAMEBOARD_LAYOUT_FILE = "src/main/java/gamedatastructures/TileLayout.txt";

    private static final String ROAD_TO_ROAD_LAYOUT_FILE = "src/main/java/graphs/RoadToRoadLayout.txt";
    private static final String ROAD_TO_VERTEX_LAYOUT_FILE = "src/main/java/graphs/RoadToVertexLayout.txt";
    private static final String VERTEX_TO_VERTEX_LAYOUT_FILE = "src/main/java/graphs/VertexToVertexLayout.txt";
    private static final String VERTEX_TO_ROAD_LAYOUT_FILE = "src/main/java/graphs/VertexToRoadLayout.txt";
    private static final String VERTEX_TO_PORT_LAYOUT_FILE = "src/main/java/graphs/VertexToPortLayout.txt";
    
    @Test
    public void moveRobberAndRobAPlayer() {
        // ---------------------- Here are some basic wiring needed that would be done by main ------------------------------
        
        // Here we use begineer game to skip through to the regular gameplay
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
        Bank.getInstance().resetBank();
        Controller controller = new Controller(game, players, gameType);

        // -------------------------- Start of Actual Test Stuff ---------------------------
               
        //Reminder of resources that each player has after basic setup
        //Resource[] resources1 = {Resource.GRAIN,Resource.BRICK,Resource.LUMBER};
        //Resource[] resources2 = {Resource.GRAIN,Resource.GRAIN,Resource.ORE};
        //Resource[] resources3 = {Resource.BRICK,Resource.LUMBER,Resource.ORE};
        //Resource[] resources4 = {Resource.LUMBER,Resource.LUMBER,Resource.GRAIN};

        //check state before
        assertEquals(9,gameBoard.getRobberTile().getTileNumber());
        assertEquals(3, player1.hand.getResourceCardCount());
        assertEquals(3, player2.hand.getResourceCardCount());

        int newRobber = 13;
        //The player on the new robber tile
        int robbedPlayer = 2;
        //the gui will call these two methods
        assertEquals(SuccessCode.SUCCESS, controller.moveRobber(newRobber));
        assertEquals(SuccessCode.SUCCESS, controller.robPlayer(robbedPlayer));
        
        //check that the robber moved
        assertEquals(newRobber,gameBoard.getRobberTile().getTileNumber());
        //check that the hands are correct
        assertEquals(4, player1.hand.getResourceCardCount());
        assertEquals(2, player2.hand.getResourceCardCount());
    }
    @Test
    public void tryMoveRobberAndRobAPlayerAndFail() {
        // ---------------------- Here are some basic wiring needed that would be done by main ------------------------------
        
        // Here we use begineer game to skip through to the regular gameplay
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
        Bank.getInstance().resetBank();
        Controller controller = new Controller(game, players, gameType);

        // -------------------------- Start of Actual Test Stuff ---------------------------
               
        //Reminder of resources that each player has after basic setup
        //Resource[] resources1 = {Resource.GRAIN,Resource.BRICK,Resource.LUMBER};
        //Resource[] resources2 = {Resource.GRAIN,Resource.GRAIN,Resource.ORE};
        //Resource[] resources3 = {Resource.BRICK,Resource.LUMBER,Resource.ORE};
        //Resource[] resources4 = {Resource.LUMBER,Resource.LUMBER,Resource.GRAIN};

        //check state before
        assertEquals(9,gameBoard.getRobberTile().getTileNumber());
        assertEquals(3, player1.hand.getResourceCardCount());

        int newRobber = 9;
        //This time we call with an invalid player(cur player)
        int robbedPlayer = 1;
        //the gui will call these two methods
        //This time the robber tile is the same
        assertEquals(SuccessCode.INVALID_PLACEMENT, controller.moveRobber(newRobber));
        assertThrows(IllegalArgumentException.class,()->{controller.robPlayer(robbedPlayer);});
        
        //check that the robber didnt move
        assertEquals(newRobber,gameBoard.getRobberTile().getTileNumber());
        //check that the hands are correct
        assertEquals(3, player1.hand.getResourceCardCount());
       
    }
}