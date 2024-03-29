package integration;

import controller.Controller;
import controller.SuccessCode;
import gamedatastructures.*;
import graphs.RoadGraph;
import graphs.VertexGraph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class F10Test {
    //F10: Ability for a player to purchase a development card during their turn. Requires a sheep, and wheat, and an ore to purchase

    private static final String GAMEBOARD_LAYOUT_FILE = "src/main/java/gamedatastructures/TileLayout.txt";
    private static final String ROAD_TO_ROAD_LAYOUT_FILE = "src/main/java/graphs/RoadToRoadLayout.txt";
    private static final String ROAD_TO_VERTEX_LAYOUT_FILE = "src/main/java/graphs/RoadToVertexLayout.txt";
    private static final String VERTEX_TO_VERTEX_LAYOUT_FILE = "src/main/java/graphs/VertexToVertexLayout.txt";
    private static final String VERTEX_TO_ROAD_LAYOUT_FILE = "src/main/java/graphs/VertexToRoadLayout.txt";
    private static final String VERTEX_TO_PORT_LAYOUT_FILE = "src/main/java/graphs/VertexToPortLayout.txt";

    @Test
    public void testBuyDevelopmentCard_SuccessfulPurchase(){
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

        // Note: at this point the players would have gotten some starter resources during the
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the player1's hand and assert that the player has zero resources
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

        Bank.getInstance().resetBank();

        //Begin Test

        player1.hand.addResource(Resource.WOOL, 5);
        player1.hand.addResource(Resource.GRAIN, 5);
        player1.hand.addResource(Resource.ORE, 5);

        assertEquals(15, player1.hand.getResourceCardCount());
        assertEquals(5, player1.hand.getResourceCardAmount(Resource.WOOL));
        assertEquals(5, player1.hand.getResourceCardAmount(Resource.GRAIN));
        assertEquals(5, player1.hand.getResourceCardAmount(Resource.ORE));

        //attempt to purchase a few development cards
        SuccessCode success = controller.clickedBuyDevCard();

        //make sure one card is added and the correct amount of resources are added
        assertEquals(SuccessCode.SUCCESS, success);
        int devCardAmount =  0;
        for(DevCard r: player1.hand.devCards.keySet()){
            devCardAmount += player1.hand.devCards.get(r);
        }
        assertEquals(1, devCardAmount);
        assertEquals(12, player1.hand.getResourceCardCount());
        assertEquals(4, player1.hand.getResourceCardAmount(Resource.WOOL));
        assertEquals(4, player1.hand.getResourceCardAmount(Resource.GRAIN));
        assertEquals(4, player1.hand.getResourceCardAmount(Resource.ORE));

        //buy another dev card
        success = controller.clickedBuyDevCard();

        assertEquals(SuccessCode.SUCCESS, success);
        devCardAmount =  0;
        for(DevCard r: player1.hand.devCards.keySet()){
            devCardAmount += player1.hand.devCards.get(r);
        }
        assertEquals(2, devCardAmount);
        assertEquals(9, player1.hand.getResourceCardCount());
        assertEquals(3, player1.hand.getResourceCardAmount(Resource.WOOL));
        assertEquals(3, player1.hand.getResourceCardAmount(Resource.GRAIN));
        assertEquals(3, player1.hand.getResourceCardAmount(Resource.ORE));

        //buy a third dev card
        success = controller.clickedBuyDevCard();

        assertEquals(SuccessCode.SUCCESS, success);
        devCardAmount =  0;
        for(DevCard r: player1.hand.devCards.keySet()){
            devCardAmount += player1.hand.devCards.get(r);
        }
        assertEquals(3, devCardAmount);
        assertEquals(6, player1.hand.getResourceCardCount());
        assertEquals(2, player1.hand.getResourceCardAmount(Resource.WOOL));
        assertEquals(2, player1.hand.getResourceCardAmount(Resource.GRAIN));
        assertEquals(2, player1.hand.getResourceCardAmount(Resource.ORE));
    }

    @Test
    public void testBuyDevelopmentCard_Fail_notEnoughResources(){
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

        // Note: at this point the players would have gotten some starter resources during the
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the player1's hand and assert that the player has zero resources
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

        Bank.getInstance().resetBank();

        //Begin Test

        player1.hand.addResource(Resource.WOOL, 2);
        player1.hand.addResource(Resource.GRAIN, 1);
        player1.hand.addResource(Resource.ORE, 1);

        assertEquals(4, player1.hand.getResourceCardCount());
        assertEquals(2, player1.hand.getResourceCardAmount(Resource.WOOL));
        assertEquals(1, player1.hand.getResourceCardAmount(Resource.GRAIN));
        assertEquals(1, player1.hand.getResourceCardAmount(Resource.ORE));

        //buy one card successfully first
        SuccessCode success = controller.clickedBuyDevCard();

        //make sure one card is added and the correct amount of resources are added
        assertEquals(SuccessCode.SUCCESS, success);
        int devCardAmount =  0;
        for(DevCard r: player1.hand.devCards.keySet()){
            devCardAmount += player1.hand.devCards.get(r);
        }
        assertEquals(1, devCardAmount);
        assertEquals(1, player1.hand.getResourceCardCount());
        assertEquals(1, player1.hand.getResourceCardAmount(Resource.WOOL));
        assertEquals(0, player1.hand.getResourceCardAmount(Resource.GRAIN));
        assertEquals(0, player1.hand.getResourceCardAmount(Resource.ORE));

        //now try and buy one and fail because the player does not have enough resources
        success = controller.clickedBuyDevCard();

        assertEquals(SuccessCode.INSUFFICIENT_RESOURCES, success);
        devCardAmount =  0;
        for(DevCard r: player1.hand.devCards.keySet()){
            devCardAmount += player1.hand.devCards.get(r);
        }
        assertEquals(1, devCardAmount);
        assertEquals(1, player1.hand.getResourceCardCount());
        assertEquals(1, player1.hand.getResourceCardAmount(Resource.WOOL));
        assertEquals(0, player1.hand.getResourceCardAmount(Resource.GRAIN));
        assertEquals(0, player1.hand.getResourceCardAmount(Resource.ORE));
    }

    @Test
    public void testBuyDevelopmentCard_Fail_DevCardDeckIsEmpty(){
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

        // Note: at this point the players would have gotten some starter resources during the
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the player1's hand and assert that the player has zero resources
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

        Bank.getInstance().resetBank();

        //Begin Test

        //We need to empty the development card deck. We will do this in two batches:
        //Buy the first 19 cards
        //Then buy the last 6 cards

        //First 19:
        player1.hand.addResource(Resource.WOOL, 19);
        player1.hand.addResource(Resource.GRAIN, 19);
        player1.hand.addResource(Resource.ORE, 19);

        for(int i = 0; i < 19; i++){
            SuccessCode success = controller.clickedBuyDevCard();
            assertEquals(SuccessCode.SUCCESS, success);
        }

        assertEquals(0, player1.hand.getResourceCardCount());
        int devCardAmount =  0;
        for(DevCard r: player1.hand.devCards.keySet()){
            devCardAmount += player1.hand.devCards.get(r);
        }
        assertEquals(19, devCardAmount);

        //Last 6:
        player1.hand.addResource(Resource.WOOL, 6);
        player1.hand.addResource(Resource.GRAIN, 6);
        player1.hand.addResource(Resource.ORE, 6);

        for(int i = 0; i < 6; i++){
            SuccessCode success = controller.clickedBuyDevCard();
            assertEquals(SuccessCode.SUCCESS, success);
        }

        assertEquals(0, player1.hand.getResourceCardCount());
        devCardAmount =  0;
        for(DevCard r: player1.hand.devCards.keySet()){
            devCardAmount += player1.hand.devCards.get(r);
        }
        assertEquals(25, devCardAmount);

        //Now that the player has all 25 dev cards, attempt to buy another dev card
        player1.hand.addResource(Resource.WOOL, 1);
        player1.hand.addResource(Resource.GRAIN, 1);
        player1.hand.addResource(Resource.ORE, 1);

        SuccessCode success = controller.clickedBuyDevCard();
        assertEquals(SuccessCode.EMPTY_DEV_CARD_DECK, success);
        devCardAmount =  0;
        for(DevCard r: player1.hand.devCards.keySet()){
            devCardAmount += player1.hand.devCards.get(r);
        }
        assertEquals(25, devCardAmount);
        assertEquals(3, player1.hand.getResourceCardCount());
        assertEquals(1, player1.hand.getResourceCardAmount(Resource.WOOL));
        assertEquals(1, player1.hand.getResourceCardAmount(Resource.GRAIN));
        assertEquals(1, player1.hand.getResourceCardAmount(Resource.ORE));
    }
}
