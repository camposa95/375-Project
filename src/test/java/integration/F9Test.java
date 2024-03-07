package integration;

import controller.Controller;
import controller.SuccessCode;
import gamedatastructures.*;
import graphs.RoadGraph;
import graphs.VertexGraph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

//F9: Ability for a player to trade resources with another player during their turn
public class F9Test {

    private static final String GAMEBOARD_LAYOUT_FILE = "src/main/java/gamedatastructures/TileLayout.txt";
    private static final String ROAD_TO_ROAD_LAYOUT_FILE = "src/main/java/graphs/RoadToRoadLayout.txt";
    private static final String ROAD_TO_VERTEX_LAYOUT_FILE = "src/main/java/graphs/RoadToVertexLayout.txt";
    private static final String VERTEX_TO_VERTEX_LAYOUT_FILE = "src/main/java/graphs/VertexToVertexLayout.txt";
    private static final String VERTEX_TO_ROAD_LAYOUT_FILE = "src/main/java/graphs/VertexToRoadLayout.txt";
    private static final String VERTEX_TO_PORT_LAYOUT_FILE = "src/main/java/graphs/VertexToPortLayout.txt";

    //Test: successful trade between two players
    @Test
    public void testTradeCardsBetweenTwoPlayers_Successful(){
        GameType gameType = GameType.Beginner;
        VertexGraph vertices = new VertexGraph();
        RoadGraph roads = new RoadGraph();

        vertices.initializeVertexToVertexAdjacency(VERTEX_TO_VERTEX_LAYOUT_FILE);
        vertices.initializeVertexToRoadAdjacency(roads, VERTEX_TO_ROAD_LAYOUT_FILE);
        vertices.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, gameType);
        roads.initializeRoadToRoadAdjacency(ROAD_TO_ROAD_LAYOUT_FILE);
        roads.initializeRoadToVertexAdjacency(vertices, ROAD_TO_VERTEX_LAYOUT_FILE);

        Player player1 = new Player(1);
        Player player2 = new Player(2);
        Player[] players = {player1, player2};

        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(gameType, GAMEBOARD_LAYOUT_FILE);
        Game game = new Game(gameBoard, vertices, roads, devCardDeck);
        Bank.getInstance().resetBank();

        // Assert that the beginner setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            controllerRef.set(new Controller(game, players, gameType));
        }, "Setup while loop timed out");
        Controller controller = controllerRef.get();

        //Beginning of actual test
        Resource[] player1Resources = {
                Resource.LUMBER,
                Resource.BRICK
        };
        Resource[] player2Resources = {
                Resource.WOOL,
                Resource.ORE,
                Resource.ORE
        };

        // Note: at this point the players would have gotten some starter resources during the
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the player1's hand and assert that the player has zero resources because the
        // longest road card doesn't need resources because it is free
        for (Resource resource: Resource.values()) {
            if (resource != Resource.ANY) { // skip this one used for trading
                int count = player1.hand.getResourceCardAmount(resource);
                if (count > 0) {
                    player1.hand.removeResource(resource, count);
                }
            }
        }
        assertEquals(0, player1.hand.getResourceCardCount());

        for (Resource resource: Resource.values()) {
            if (resource != Resource.ANY) { // skip this one used for trading
                int count = player2.hand.getResourceCardAmount(resource);
                if (count > 0) {
                    player2.hand.removeResource(resource, count);
                }
            }
        }
        assertEquals(0, player2.hand.getResourceCardCount());

        int numCardsBefore1 = player1.hand.getResourceCardCount();
        int numCardsBefore2 = player2.hand.getResourceCardCount();

        player1.hand.addResources(player1Resources);
        player2.hand.addResources(player2Resources);

        SuccessCode success = controller.tradeWithPlayer(player2, player1Resources, player2Resources);
        assertEquals(SuccessCode.SUCCESS, success);
        assertEquals(numCardsBefore1+3, player1.hand.getResourceCardCount());
        assertEquals(numCardsBefore2+2, player2.hand.getResourceCardCount());
    }

    //Test: trade between two players where the first player does not have
    // enough resources to complete the trade
    @Test
    public void testTradeCardsBetweenTwoPlayers_Fail_FirstPlayerNotEnoughResources(){
        GameType gameType = GameType.Beginner;
        VertexGraph vertices = new VertexGraph();
        RoadGraph roads = new RoadGraph();

        vertices.initializeVertexToVertexAdjacency(VERTEX_TO_VERTEX_LAYOUT_FILE);
        vertices.initializeVertexToRoadAdjacency(roads, VERTEX_TO_ROAD_LAYOUT_FILE);
        vertices.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, gameType);
        roads.initializeRoadToRoadAdjacency(ROAD_TO_ROAD_LAYOUT_FILE);
        roads.initializeRoadToVertexAdjacency(vertices, ROAD_TO_VERTEX_LAYOUT_FILE);

        Player player1 = new Player(1);
        Player player2 = new Player(2);
        Player[] players = {player1, player2};

        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(gameType, GAMEBOARD_LAYOUT_FILE);
        Game game = new Game(gameBoard, vertices, roads, devCardDeck);
        Bank.getInstance().resetBank();

        // Assert that the beginner setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            controllerRef.set(new Controller(game, players, gameType));
        }, "Setup while loop timed out");
        Controller controller = controllerRef.get();

        Resource[] player1HandResources = {
                Resource.LUMBER,
                Resource.BRICK
        };
        Resource[] player2HandResources = {
                Resource.WOOL,
                Resource.ORE,
                Resource.ORE
        };

        // Note: at this point the players would have gotten some starter resources during the
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the player1's hand and assert that the player has zero resources because the
        // longest road card doesn't need resources because it is free
        for (Resource resource: Resource.values()) {
            if (resource != Resource.ANY) { // skip this one used for trading
                int count = player1.hand.getResourceCardAmount(resource);
                if (count > 0) {
                    player1.hand.removeResource(resource, count);
                }
            }
        }
        assertEquals(0, player1.hand.getResourceCardCount());

        for (Resource resource: Resource.values()) {
            if (resource != Resource.ANY) { // skip this one used for trading
                int count = player2.hand.getResourceCardAmount(resource);
                if (count > 0) {
                    player2.hand.removeResource(resource, count);
                }
            }
        }
        assertEquals(0, player2.hand.getResourceCardCount());

        player1.hand.addResources(player1HandResources);
        player2.hand.addResources(player2HandResources);

        int numCardsBefore1 = player1.hand.getResourceCardCount();
        int numCardsBefore2 = player2.hand.getResourceCardCount();

        Resource[] player1TradeResources = {
                Resource.LUMBER,
                Resource.LUMBER,
                Resource.BRICK
        };

        Resource[] player2TradeResources = {
                Resource.WOOL,
                Resource.ORE,
                Resource.ORE
        };

        SuccessCode success = controller.tradeWithPlayer(player2, player1TradeResources, player2TradeResources);
        assertEquals(SuccessCode.UNDEFINED, success);
        assertEquals(numCardsBefore1, player1.hand.getResourceCardCount());
        assertEquals(numCardsBefore2, player2.hand.getResourceCardCount());
    }

    //Test: trade between two players where the second player does not have
    // enough resources to complete the trade
    @Test
    public void testTradeCardsBetweenTwoPlayers_Fail_SecondPlayerNotEnoughResources(){
        GameType gameType = GameType.Beginner;
        VertexGraph vertices = new VertexGraph();
        RoadGraph roads = new RoadGraph();

        vertices.initializeVertexToVertexAdjacency(VERTEX_TO_VERTEX_LAYOUT_FILE);
        vertices.initializeVertexToRoadAdjacency(roads, VERTEX_TO_ROAD_LAYOUT_FILE);
        vertices.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, gameType);
        roads.initializeRoadToRoadAdjacency(ROAD_TO_ROAD_LAYOUT_FILE);
        roads.initializeRoadToVertexAdjacency(vertices, ROAD_TO_VERTEX_LAYOUT_FILE);

        Player player1 = new Player(1);
        Player player2 = new Player(2);
        Player[] players = {player1, player2};

        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(gameType, GAMEBOARD_LAYOUT_FILE);
        Game game = new Game(gameBoard, vertices, roads, devCardDeck);
        Bank.getInstance().resetBank();

        // Assert that the beginner setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            controllerRef.set(new Controller(game, players, gameType));
        }, "Setup while loop timed out");
        Controller controller = controllerRef.get();

        Resource[] player1HandResources = {
                Resource.LUMBER,
                Resource.BRICK
        };
        Resource[] player2HandResources = {
                Resource.WOOL,
                Resource.ORE,
                Resource.ORE
        };

        // Note: at this point the players would have gotten some starter resources during the
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the player1's hand and assert that the player has zero resources because the
        // longest road card doesn't need resources because it is free
        for (Resource resource: Resource.values()) {
            if (resource != Resource.ANY) { // skip this one used for trading
                int count = player1.hand.getResourceCardAmount(resource);
                if (count > 0) {
                    player1.hand.removeResource(resource, count);
                }
            }
        }
        assertEquals(0, player1.hand.getResourceCardCount());

        for (Resource resource: Resource.values()) {
            if (resource != Resource.ANY) { // skip this one used for trading
                int count = player2.hand.getResourceCardAmount(resource);
                if (count > 0) {
                    player2.hand.removeResource(resource, count);
                }
            }
        }
        assertEquals(0, player2.hand.getResourceCardCount());

        player1.hand.addResources(player1HandResources);
        player2.hand.addResources(player2HandResources);

        int numCardsBefore1 = player1.hand.getResourceCardCount();
        int numCardsBefore2 = player2.hand.getResourceCardCount();

        Resource[] player1TradeResources = {
                Resource.LUMBER,
                Resource.BRICK
        };

        Resource[] player2TradeResources = {
                Resource.WOOL,
                Resource.ORE,
                Resource.ORE,
                Resource.LUMBER
        };

        SuccessCode success = controller.tradeWithPlayer(player2, player1TradeResources, player2TradeResources);
        assertEquals(SuccessCode.UNDEFINED, success);
        assertEquals(numCardsBefore1, player1.hand.getResourceCardCount());
        assertEquals(numCardsBefore2, player2.hand.getResourceCardCount());
    }
}
