package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import org.junit.jupiter.api.Test;

import controller.Controller;
import gamedatastructures.DevelopmentCardDeck;
import gamedatastructures.Game;
import gamedatastructures.GameBoard;
import gamedatastructures.GameType;
import gamedatastructures.Player;
import gamedatastructures.Resource;
import graphs.RoadGraph;
import graphs.VertexGraph;

/**
 * The purpose of this test class is to test feature 14 (F14):
 *      Require the players to drop half their deck on a 7
 */
public class F14Test {
    private static final String GAMEBOARD_LAYOUT_FILE = "src/main/java/gamedatastructures/TileLayout.txt";

    private static final String ROAD_TO_ROAD_LAYOUT_FILE = "src/main/java/graphs/RoadToRoadLayout.txt";
    private static final String ROAD_TO_VERTEX_LAYOUT_FILE = "src/main/java/graphs/RoadToVertexLayout.txt";
    private static final String VERTEX_TO_VERTEX_LAYOUT_FILE = "src/main/java/graphs/VertexToVertexLayout.txt";
    private static final String VERTEX_TO_ROAD_LAYOUT_FILE = "src/main/java/graphs/VertexToRoadLayout.txt";
    private static final String VERTEX_TO_PORT_LAYOUT_FILE = "src/main/java/graphs/VertexToPortLayout.txt";
    
    @Test
    public void testRoll7DropResources() {

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

        // Players
        Player player1 = new Player(1);
        //player one gets 0
        Player player2 = new Player(2);
        //player two gets 7
        Player player3 = new Player(3);
        //player three gets 8
        Player player4 = new Player(4);
        //player 4 gets 18
        Player[] players = {player1, player2, player3,player4};

        // other things dependent on these things
        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(gameType, GAMEBOARD_LAYOUT_FILE);
        Game game = new Game(gameBoard, vertexes, roads, devCardDeck);
        Controller controller = new Controller(game, players, gameType);

        //Based on beginner locations these are the resources each player should recieve after setup
        Resource[] resources1 = {Resource.GRAIN,Resource.BRICK,Resource.LUMBER};
        Resource[] resources2 = {Resource.GRAIN,Resource.GRAIN,Resource.ORE};
        Resource[] resources3 = {Resource.BRICK,Resource.LUMBER,Resource.ORE};
        Resource[] resources4 = {Resource.LUMBER,Resource.LUMBER,Resource.GRAIN};
        //now remove them
        player1.hand.removeResources(resources1);
        player2.hand.removeResources(resources2);
        player3.hand.removeResources(resources3);
        player4.hand.removeResources(resources4);
        //add back the amount we want for our test
        player2.hand.addResource(Resource.BRICK, 7);
        player3.hand.addResource(Resource.BRICK, 8);
        player4.hand.addResource(Resource.BRICK, 18);
        //create the expected hash map with resources as theyd come from gui(Only players with 8+ lose cards)
        HashMap<Integer,Resource[]> resourcesMap = new HashMap<>();
        resourcesMap.put(3,new Resource[]{Resource.BRICK,Resource.BRICK,Resource.BRICK,Resource.BRICK});
        resourcesMap.put(4,new Resource[]{Resource.BRICK,Resource.BRICK,Resource.BRICK,Resource.BRICK,
                                            Resource.BRICK,Resource.BRICK,Resource.BRICK,Resource.BRICK,Resource.BRICK});

        int p1Before = player1.hand.getResourceCardCount();
        int p2Before = player2.hand.getResourceCardCount();
        int p3before = player3.hand.getResourceCardCount();
        int p4Before = player4.hand.getResourceCardCount();

        //Method called by gui when it detects a 7
        controller.dropResources(resourcesMap);

        int p1After = player1.hand.getResourceCardCount();
        int p2After = player2.hand.getResourceCardCount();
        int p3After = player3.hand.getResourceCardCount();
        int p4After = player4.hand.getResourceCardCount();
        
        assertEquals(p1Before,0);
        assertEquals(p1After,0);
        assertEquals(p2Before,7);
        assertEquals(p2After,7);
        assertEquals(p3before,8);
        assertEquals(p3After,4);
        assertEquals(p4Before,18);
        assertEquals(p4After,9);
    }
  }

