package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import controller.Controller;
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
 * The purpose of this test class is to test feature 20 (F20):
 *    Monopoly Development Card: Allow the player to claim all resources
 *    of a single type from the hands of all other players, when they play this card during their turn.
 */
public class F20Test {
    
    private static final String GAMEBOARD_LAYOUT_FILE = "src/main/java/gamedatastructures/TileLayout.txt";

    private static final String ROAD_TO_ROAD_LAYOUT_FILE = "src/main/java/graphs/RoadToRoadLayout.txt";
    private static final String ROAD_TO_VERTEX_LAYOUT_FILE = "src/main/java/graphs/RoadToVertexLayout.txt";
    private static final String VERTEX_TO_VERTEX_LAYOUT_FILE = "src/main/java/graphs/VertexToVertexLayout.txt";
    private static final String VERTEX_TO_ROAD_LAYOUT_FILE = "src/main/java/graphs/VertexToRoadLayout.txt";
    private static final String VERTEX_TO_PORT_LAYOUT_FILE = "src/main/java/graphs/VertexToPortLayout.txt";
    
    @Test
    public void testMonopolyWorks() {
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
        controller.setCurrentPlayer(player3);
        //Reminder of resources that each player has after basic setup
        //Resource[] resources1 = {Resource.GRAIN,Resource.BRICK,Resource.LUMBER};
        //Resource[] resources2 = {Resource.GRAIN,Resource.GRAIN,Resource.ORE};
        //Resource[] resources3 = {Resource.BRICK,Resource.LUMBER,Resource.ORE};
        //Resource[] resources4 = {Resource.LUMBER,Resource.LUMBER,Resource.GRAIN};


        // directly add the card to the hand.
        // Note this is not the same as buying the card which would not allow us to use the card we bought during the turn
        player3.hand.addDevelopmentCard(DevCard.MONOPOLY);

        Resource resource = Resource.GRAIN;

        // play the card to start us off
        assertEquals(SuccessCode.SUCCESS, controller.playMonopolyCard(resource));

        // assert we got the resources
        assertEquals(4, player3.hand.getResourceCardAmount(resource));
       

        // assert we used the card
        assertEquals(0, player3.hand.devCards.get(DevCard.MONOPOLY));
    }

    @Test
    public void testMonopolyNoCard() {
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
        Controller controller = new Controller(game, players, gameType);

        // -------------------------- Start of Actual Test Stuff ---------------------------
        controller.setCurrentPlayer(player3);
        //Reminder of resources that each player has after basic setup
        //Resource[] resources1 = {Resource.GRAIN,Resource.BRICK,Resource.LUMBER};
        //Resource[] resources2 = {Resource.GRAIN,Resource.GRAIN,Resource.ORE};
        //Resource[] resources3 = {Resource.BRICK,Resource.LUMBER,Resource.ORE};
        //Resource[] resources4 = {Resource.LUMBER,Resource.LUMBER,Resource.GRAIN};

       
        // don't give the player the card
       
        Resource resource = Resource.GRAIN;

        // play the card to start us off
        assertEquals(SuccessCode.CANNOT_PLAY_CARD, controller.playMonopolyCard(resource));

        // assert we didnt get the resources
        assertEquals(0, player3.hand.getResourceCardAmount(resource));
       
    }

}