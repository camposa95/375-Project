package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import controller.Controller;
import controller.GameState;
import gamedatastructures.DevelopmentCardDeck;
import gamedatastructures.Game;
import gamedatastructures.GameBoard;
import gamedatastructures.GameType;
import gamedatastructures.Player;
import gamedatastructures.Resource;
import graphs.RoadGraph;
import graphs.VertexGraph;


/**
 * The purpose of this test class is to test feature 13 (F13):
 *      Unlock better trades with the bank for Players with settlements built on ports.
 */
public class F13Test {
    
    private static final String GAMEBOARD_LAYOUT_FILE = "src/main/java/gamedatastructures/TileLayout.txt";

    private static final String ROAD_TO_ROAD_LAYOUT_FILE = "src/main/java/graphs/RoadToRoadLayout.txt";
    private static final String ROAD_TO_VERTEX_LAYOUT_FILE = "src/main/java/graphs/RoadToVertexLayout.txt";
    private static final String VERTEX_TO_VERTEX_LAYOUT_FILE = "src/main/java/graphs/VertexToVertexLayout.txt";
    private static final String VERTEX_TO_ROAD_LAYOUT_FILE = "src/main/java/graphs/VertexToRoadLayout.txt";
    private static final String VERTEX_TO_PORT_LAYOUT_FILE = "src/main/java/graphs/VertexToPortLayout.txt";
    
    @Test
    public void testBuildSettlmentNoPort() {
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
        
        // Assert that the begineer setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            controllerRef.set(new Controller(game, players, gameType));
        }, "Setup while loop timed out");
        Controller controller = controllerRef.get();

        // -------------------------- Start of Actual Test Stuff ---------------------------
        // Note: we assume everything about setup was correct because it was tested earlier

        // Note: at this point the players would have gotten some starter resources during the 
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the player1's hand and assert that the player has zero resources so we can
        // better test on the specific cases.
        for (Resource resource: Resource.values()) {
            if (resource != Resource.ANY) { // skip this one used for trading
                int count = player1.hand.getResourceCardAmount(resource);
                if (count > 0) {
                    player1.hand.removeResource(resource, count);
                }
            }
        }
        assertEquals(0, player1.hand.getResourceCardCount());
    
        // now make sure the player has the enough resources
        Resource[] resourcesForSettlment = {Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN};
        player1.hand.addResources(resourcesForSettlment);

        // set up the controller for the click
        controller.setState(GameState.BUILD_SETTLEMENT);
        // Note controller should already default to currentPlayer == to player1
        // and we should already be in regular play

        // give the player another road so we can follow the network rule
        roads.getRoad(26).setOwner(player1);
        roads.getRoad(27).setOwner(player1);

        // get its trade boosts before the build
        Resource[] boostsBefore = player1.getTradeBoosts();
        // assert that the player had not boosts before
        Resource[] expectedBoostsBefore = {};
        assertTrue(Arrays.equals(expectedBoostsBefore, boostsBefore));

        // here is the actual click
        int newVertexId = 21; // use a vertex not next to a ports
        controller.clickedVertex(newVertexId); // click should succeed
        // Note: we don't need to assert on states and such here because that
        // is outside of the scope of this tests

        // assert that the player didn't gain a boost
        Resource[] expectedBoostsAfter = {};
        Resource[] boostsAfter = player1.getTradeBoosts();
        assertTrue(Arrays.equals(expectedBoostsAfter, boostsAfter));
    }

    @Test
    public void testBuildSettlmentHasSpecialPort() {
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
        
        // Assert that the begineer setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            controllerRef.set(new Controller(game, players, gameType));
        }, "Setup while loop timed out");
        Controller controller = controllerRef.get();

        // -------------------------- Start of Actual Test Stuff ---------------------------
        // Note: we assume everything about setup was correct because it was tested earlier

        // Note: at this point the players would have gotten some starter resources during the 
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the player1's hand and assert that the player has zero resources so we can
        // better test on the specific cases.
        for (Resource resource: Resource.values()) {
            if (resource != Resource.ANY) { // skip this one used for trading
                int count = player1.hand.getResourceCardAmount(resource);
                if (count > 0) {
                    player1.hand.removeResource(resource, count);
                }
            }
        }
        assertEquals(0, player1.hand.getResourceCardCount());
    
        // now make sure the player has the enough resources
        Resource[] resourcesForSettlment = {Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN};
        player1.hand.addResources(resourcesForSettlment);

        // set up the controller for the click
        controller.setState(GameState.BUILD_SETTLEMENT);
        // Note controller should already default to currentPlayer == to player1
        // and we should already be in regular play

        // give the player another road so we can get to the special port
        roads.getRoad(24).setOwner(player1);

        // get its trade boosts before the build
        Resource[] boostsBefore = player1.getTradeBoosts();
        // assert that the player had not boosts before
        Resource[] expectedBoostsBefore = {};
        assertTrue(Arrays.equals(expectedBoostsBefore, boostsBefore));

        // here is the actual click
        int newVertexId = 17; // use a vertex not next to a ports
        controller.clickedVertex(newVertexId); // click should succeed
        // Note: we don't need to assert on states and such here because that
        // is outside of the scope of this tests

        // assert that the player didn't gain a boost
        Resource[] expectedBoostsAfter = {Resource.LUMBER};
        Resource[] boostsAfter = player1.getTradeBoosts();
        assertTrue(Arrays.equals(expectedBoostsAfter, boostsAfter));
    }

    @Test
    public void testBuildSettlmentHasGenericPort() {
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
        
        // Assert that the begineer setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            controllerRef.set(new Controller(game, players, gameType));
        }, "Setup while loop timed out");
        Controller controller = controllerRef.get();

        // -------------------------- Start of Actual Test Stuff ---------------------------
        // Note: we assume everything about setup was correct because it was tested earlier

        // Note: at this point the players would have gotten some starter resources during the 
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the player1's hand and assert that the player has zero resources so we can
        // better test on the specific cases.
        for (Resource resource: Resource.values()) {
            if (resource != Resource.ANY) { // skip this one used for trading
                int count = player1.hand.getResourceCardAmount(resource);
                if (count > 0) {
                    player1.hand.removeResource(resource, count);
                }
            }
        }
        assertEquals(0, player1.hand.getResourceCardCount());
    
        // now make sure the player has the enough resources
        Resource[] resourcesForSettlment = {Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN};
        player1.hand.addResources(resourcesForSettlment);

        // set up the controller for the click
        controller.setState(GameState.BUILD_SETTLEMENT);
        // Note controller should already default to currentPlayer == to player1
        // and we should already be in regular play

        // give the player another road so we can get to the special port
        roads.getRoad(19).setOwner(player1);
        roads.getRoad(11).setOwner(player1);
        roads.getRoad(6).setOwner(player1);

        // get its trade boosts before the build
        Resource[] boostsBefore = player1.getTradeBoosts();
        // assert that the player had not boosts before
        Resource[] expectedBoostsBefore = {};
        assertTrue(Arrays.equals(expectedBoostsBefore, boostsBefore));

        // here is the actual click
        int newVertexId = 0; // use a vertex next to a generic port
        controller.clickedVertex(newVertexId); // click should succeed
        // Note: we don't need to assert on states and such here because that
        // is outside of the scope of this tests

        // assert that the player didn't gain a boost
        Resource[] expectedBoostsAfter = {Resource.ANY};
        Resource[] boostsAfter = player1.getTradeBoosts();
        assertTrue(Arrays.equals(expectedBoostsAfter, boostsAfter));
    }
}
