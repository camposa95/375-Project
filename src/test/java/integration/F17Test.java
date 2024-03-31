package integration;
import data.GameLoader;
import domain.controller.Controller;
import domain.controller.GameState;
import domain.controller.SuccessCode;
import domain.bank.Bank;
import domain.bank.Resource;
import domain.devcarddeck.DevCard;
import domain.devcarddeck.DevelopmentCardDeck;
import domain.game.Game;
import domain.game.GameType;
import domain.gameboard.GameBoard;
import domain.player.HarvestBooster;
import domain.player.Player;
import domain.graphs.RoadGraph;
import domain.graphs.VertexGraph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
public class F17Test {
    //F12: Victory point development card: allow the player to secretly collect a victory point for each of these cards that they hold
    //Note: on the GUI level, we decided against the "secretly" portion since four players are sharing the same screen
    // and you'd be able to deduce what card they got either way

    //Add Victory Point card to hand and show that VP goes up for the player
    @Test
    public void testAddVictoryPointCard_normalPlay(){
        GameType gameType = GameType.Beginner;
        VertexGraph vertexes = new VertexGraph(gameType);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        Bank bank = new Bank();
        Player player1 = new Player(1, new HarvestBooster(), bank);
        Player player2 = new Player(2, new HarvestBooster(), bank);
        Player player3 = new Player(3, new HarvestBooster(), bank);
        Player player4 = new Player(4, new HarvestBooster(), bank);
        Player[] players = {player1, player2, player3, player4};

        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        Game game = new Game(gameBoard, vertexes, roads, devCardDeck, bank);
        bank.reset();

        // Assert that the beginner setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            controllerRef.set(new Controller(game, players, gameType));
        }, "Setup while loop timed out");
        Controller controller = controllerRef.get();
        assertEquals(GameState.TURN_START, controller.getState());

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

        bank.reset();
        

        //Begin test
        Resource[] resourceForHand = {
                Resource.WOOL,
                Resource.GRAIN,
                Resource.ORE
        };
        player1.hand.addResources(resourceForHand);
        bank.removeResources(resourceForHand);

        assertEquals(3, player1.hand.getResourceCardCount());
        assertEquals(1, player1.hand.getResourceCardAmount(Resource.WOOL));
        assertEquals(1, player1.hand.getResourceCardAmount(Resource.GRAIN));
        assertEquals(1, player1.hand.getResourceCardAmount(Resource.ORE));
        assertEquals(18, bank.getResourceAmount(Resource.WOOL));
        assertEquals(18, bank.getResourceAmount(Resource.GRAIN));
        assertEquals(18, bank.getResourceAmount(Resource.ORE));

        int victoryPointsBefore = player1.getVictoryPoints();

        boolean success = player1.purchaseDevCard(DevCard.VICTORY);

        assertTrue(success);
        int devCardAmount = 0;
        for(DevCard card: player1.hand.devCards.keySet()){
            devCardAmount += player1.hand.devCards.get(card);
        }
        assertEquals(1, devCardAmount);
        assertEquals(victoryPointsBefore+1, player1.getVictoryPoints());
        assertEquals(0, player1.hand.getResourceCardCount());
        assertEquals(19, bank.getResourceAmount(Resource.WOOL));
        assertEquals(19, bank.getResourceAmount(Resource.GRAIN));
        assertEquals(19, bank.getResourceAmount(Resource.ORE));
    }

    //Add Victory Point card, then cycle between two players and show that ending turn will win the game
    @Test
    public void testAddVictoryPointCard_winGameOnPurchase(){
        GameType gameType = GameType.Beginner;
        VertexGraph vertexes = new VertexGraph(gameType);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        Bank bank = new Bank();
        // Only two player here, important
        Player player1 = new Player(1, new HarvestBooster(), bank);
        Player player2 = new Player(2, new HarvestBooster(), bank);
        Player[] players = {player1, player2};

        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        Game game = new Game(gameBoard, vertexes, roads, devCardDeck, bank);
        bank.reset();

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

        bank.reset();
        

        //Begin test

        //artificially set the player's VP to 9 so buying a dev card will trigger a game win
        //we will show this on player 2's end turn through controller
        player1.setVictoryPoints(9);

        Resource[] resourceForHand = {
                Resource.WOOL,
                Resource.GRAIN,
                Resource.ORE
        };
        player1.hand.addResources(resourceForHand);
        bank.removeResources(resourceForHand);

        assertEquals(3, player1.hand.getResourceCardCount());
        assertEquals(1, player1.hand.getResourceCardAmount(Resource.WOOL));
        assertEquals(1, player1.hand.getResourceCardAmount(Resource.GRAIN));
        assertEquals(1, player1.hand.getResourceCardAmount(Resource.ORE));
        assertEquals(18, bank.getResourceAmount(Resource.WOOL));
        assertEquals(18, bank.getResourceAmount(Resource.GRAIN));
        assertEquals(18, bank.getResourceAmount(Resource.ORE));

        int victoryPointsBefore = player1.getVictoryPoints();

        boolean success = player1.purchaseDevCard(DevCard.VICTORY);

        assertTrue(success);
        int devCardAmount = 0;
        for(DevCard card: player1.hand.devCards.keySet()){
            devCardAmount += player1.hand.devCards.get(card);
        }
        assertEquals(1, devCardAmount);
        assertEquals(victoryPointsBefore+1, player1.getVictoryPoints());
        assertEquals(0, player1.hand.getResourceCardCount());
        assertEquals(19, bank.getResourceAmount(Resource.WOOL));
        assertEquals(19, bank.getResourceAmount(Resource.GRAIN));
        assertEquals(19, bank.getResourceAmount(Resource.ORE));

        //Now cycle through Player1, then Player2's turn
        controller.setState(GameState.DEFAULT);
        SuccessCode endTurnSuccess = controller.endTurn();
        assertEquals(SuccessCode.SUCCESS, endTurnSuccess);

        controller.setState(GameState.DEFAULT);
        endTurnSuccess = controller.endTurn();
        assertEquals(SuccessCode.GAME_WIN, endTurnSuccess);
    }

    //make sure that adding a sixth victory point card is not possible
    @Test
    public void testAddVictoryPoint_FailsTooManyVPCards(){
        GameType gameType = GameType.Beginner;
        VertexGraph vertexes = new VertexGraph(gameType);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        Bank bank = new Bank();
        Player player1 = new Player(1, new HarvestBooster(), bank);
        Player player2 = new Player(2, new HarvestBooster(), bank);
        Player player3 = new Player(3, new HarvestBooster(), bank);
        Player player4 = new Player(4, new HarvestBooster(), bank);
        Player[] players = {player1, player2, player3, player4};

        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        Game game = new Game(gameBoard, vertexes, roads, devCardDeck, bank);
        bank.reset();

        // Assert that the beginner setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            controllerRef.set(new Controller(game, players, gameType));
        }, "Setup while loop timed out");
        Controller controller = controllerRef.get();
        assertEquals(GameState.TURN_START, controller.getState());

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

        bank.reset();
        

        //Begin test
        player1.hand.addResource(Resource.WOOL, 6);
        player1.hand.addResource(Resource.GRAIN, 6);
        player1.hand.addResource(Resource.ORE, 6);
        bank.removeResource(Resource.WOOL, 6);
        bank.removeResource(Resource.GRAIN, 6);
        bank.removeResource(Resource.ORE, 6);

        assertEquals(18, player1.hand.getResourceCardCount());
        assertEquals(6, player1.hand.getResourceCardAmount(Resource.WOOL));
        assertEquals(6, player1.hand.getResourceCardAmount(Resource.GRAIN));
        assertEquals(6, player1.hand.getResourceCardAmount(Resource.ORE));
        assertEquals(13, bank.getResourceAmount(Resource.WOOL));
        assertEquals(13, bank.getResourceAmount(Resource.GRAIN));
        assertEquals(13, bank.getResourceAmount(Resource.ORE));

        //now purchase 5 victory point cards successfully
        for(int i = 0; i < 5; i++){
            int victoryPointsBefore = player1.getVictoryPoints();

            boolean success = player1.purchaseDevCard(DevCard.VICTORY);

            assertTrue(success);
            int devCardAmount = 0;
            for(DevCard card: player1.hand.devCards.keySet()){
                devCardAmount += player1.hand.devCards.get(card);
            }
            assertEquals(i+1, devCardAmount);
            assertEquals(victoryPointsBefore+1, player1.getVictoryPoints());
            assertEquals(18-(3*(i+1)), player1.hand.getResourceCardCount());
            assertEquals(13+(i+1), bank.getResourceAmount(Resource.WOOL));
            assertEquals(13+(i+1), bank.getResourceAmount(Resource.GRAIN));
            assertEquals(13+(i+1), bank.getResourceAmount(Resource.ORE));
        }

        //Now, attempt to purchase a sixth victory point card. this should fail
        int victoryPointsBefore = player1.getVictoryPoints();

        boolean success = player1.purchaseDevCard(DevCard.VICTORY);

        assertFalse(success);
        int devCardAmount = 0;
        for(DevCard card: player1.hand.devCards.keySet()){
            devCardAmount += player1.hand.devCards.get(card);
        }
        assertEquals(5, devCardAmount);
        assertEquals(victoryPointsBefore, player1.getVictoryPoints());
        assertEquals(3, player1.hand.getResourceCardCount());
        assertEquals(18, bank.getResourceAmount(Resource.WOOL));
        assertEquals(18, bank.getResourceAmount(Resource.GRAIN));
        assertEquals(18, bank.getResourceAmount(Resource.ORE));

    }
}
