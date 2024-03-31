package integration;

import data.GameLoader;
import domain.controller.Controller;
import domain.controller.SuccessCode;
import domain.bank.Bank;
import domain.bank.Resource;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class F12Test {

    private boolean contains(Resource[] tradeBoosts, Resource tradeBoost) {
        for (Resource boost : tradeBoosts) {
            if (boost == tradeBoost) {
                return true;
            }
        }
        return false;
    }

    //Test the basic 4:1 trade succeeding
    @Test
    public void testBankTradeSuccessful_baseTrade() {
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
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> controllerRef.set(new Controller(game, players, gameType)), "Setup while loop timed out");
        Controller controller = controllerRef.get();

        //--------------------------------------------------------------------
        //Beginning of test

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
        
        bank.reset();

        Resource[] playerHandResources = {
                Resource.LUMBER,
                Resource.LUMBER,
                Resource.LUMBER,
                Resource.LUMBER
        };

        player1.hand.addResources(playerHandResources);
        bank.removeResources(playerHandResources);

        assertEquals(4, player1.hand.getResourceCardCount());
        assertEquals(4, player1.hand.getResourceCardAmount(Resource.LUMBER));
        assertEquals(0, player1.hand.getResourceCardAmount(Resource.BRICK));
        assertEquals(15, bank.getResourceAmount(Resource.LUMBER));
        assertEquals(19, bank.getResourceAmount(Resource.BRICK));

        //Will be trading 4 Lumber for 1 Brick now:
        SuccessCode success = controller.tradeWithBank(Resource.LUMBER, Resource.BRICK);

        assertEquals(SuccessCode.SUCCESS, success);
        assertEquals(1, player1.hand.getResourceCardCount());
        assertEquals(1, player1.hand.getResourceCardAmount(Resource.BRICK));
        assertEquals(0, player1.hand.getResourceCardAmount(Resource.LUMBER));
        assertEquals(19, bank.getResourceAmount(Resource.LUMBER));
        assertEquals(18, bank.getResourceAmount(Resource.BRICK));
    }

    //Test the 4:1 trade failing: Player does not have enough resources
    @Test
    public void testBankTradeFailed_PlayerNotEnoughResources_baseTrade() {
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
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> controllerRef.set(new Controller(game, players, gameType)), "Setup while loop timed out");
        Controller controller = controllerRef.get();

        //--------------------------------------------------------------------
        //Beginning of test

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
        
        bank.reset();

        //Give the player only three resources
        Resource[] playerHandResources = {
                Resource.WOOL,
                Resource.WOOL,
                Resource.WOOL
        };

        player1.hand.addResources(playerHandResources);
        bank.removeResources(playerHandResources);

        assertEquals(3, player1.hand.getResourceCardCount());
        assertEquals(3, player1.hand.getResourceCardAmount(Resource.WOOL));
        assertEquals(0, player1.hand.getResourceCardAmount(Resource.GRAIN));
        assertEquals(16, bank.getResourceAmount(Resource.WOOL));
        assertEquals(19, bank.getResourceAmount(Resource.GRAIN));

        //Will attempt to trade 4 Wool for 1 Grain now:
        SuccessCode success = controller.tradeWithBank(Resource.WOOL, Resource.GRAIN);

        assertEquals(SuccessCode.INSUFFICIENT_RESOURCES, success);
        assertEquals(3, player1.hand.getResourceCardCount());
        assertEquals(3, player1.hand.getResourceCardAmount(Resource.WOOL));
        assertEquals(0, player1.hand.getResourceCardAmount(Resource.GRAIN));
        assertEquals(19, bank.getResourceAmount(Resource.GRAIN));
        assertEquals(16, bank.getResourceAmount(Resource.WOOL));
    }

    //Test trading with the bank - failed because the bank is out of resources
    @Test
    public void testBankTradeFailed_BankNotEnoughResources_baseTrade() {
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
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> controllerRef.set(new Controller(game, players, gameType)), "Setup while loop timed out");
        Controller controller = controllerRef.get();

        //--------------------------------------------------------------------
        //Beginning of test

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

        
        bank.reset();

        //Give the player 4x ore cards
        Resource[] playerHandResources = {
                Resource.ORE,
                Resource.ORE,
                Resource.ORE,
                Resource.ORE
        };
        player1.hand.addResources(playerHandResources);
        bank.removeResources(playerHandResources);

        //Remove all the lumber from the bank so there are none to trade for
        bank.removeResource(Resource.LUMBER, 19);

        assertEquals(4, player1.hand.getResourceCardCount());
        assertEquals(4, player1.hand.getResourceCardAmount(Resource.ORE));
        assertEquals(0, player1.hand.getResourceCardAmount(Resource.LUMBER));
        assertEquals(15, bank.getResourceAmount(Resource.ORE));
        assertEquals(0, bank.getResourceAmount(Resource.LUMBER));

        //Attempt to trade 4 Lumber for 1x Ore
        SuccessCode success = controller.tradeWithBank(Resource.ORE, Resource.LUMBER);

        assertEquals(SuccessCode.INSUFFICIENT_RESOURCES, success);
        assertEquals(4, player1.hand.getResourceCardCount());
        assertEquals(4, player1.hand.getResourceCardAmount(Resource.ORE));
        assertEquals(0, player1.hand.getResourceCardAmount(Resource.LUMBER));
        assertEquals(0, bank.getResourceAmount(Resource.LUMBER));
        assertEquals(15, bank.getResourceAmount(Resource.ORE));
    }

    //Test trading with the bank 3:1 trade boost applied
    @Test
    public void testBankTradeSuccess_3for1Trade() {
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
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> controllerRef.set(new Controller(game, players, gameType)), "Setup while loop timed out");
        Controller controller = controllerRef.get();

        //--------------------------------------------------------------------
        //Beginning of test

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

        
        bank.reset();

        Resource[] playerHandResources = {
                Resource.LUMBER,
                Resource.LUMBER,
                Resource.LUMBER,
        };

        player1.hand.addResources(playerHandResources);
        bank.removeResources(playerHandResources);

        assertEquals(3, player1.hand.getResourceCardCount());
        assertEquals(3, player1.hand.getResourceCardAmount(Resource.LUMBER));
        assertEquals(0, player1.hand.getResourceCardAmount(Resource.BRICK));
        assertEquals(16, bank.getResourceAmount(Resource.LUMBER));
        assertEquals(19, bank.getResourceAmount(Resource.BRICK));

        //add trade boost to the player
        player1.addTradeBoost(Resource.ANY);

        assertTrue(contains(player1.getTradeBoosts(), Resource.ANY));

        //Will be trading 3 Lumber for 1 Brick now:
        SuccessCode success = controller.tradeWithBank(Resource.LUMBER, Resource.BRICK);

        assertEquals(SuccessCode.SUCCESS, success);
        assertEquals(1, player1.hand.getResourceCardCount());
        assertEquals(1, player1.hand.getResourceCardAmount(Resource.BRICK));
        assertEquals(0, player1.hand.getResourceCardAmount(Resource.LUMBER));
        assertEquals(19, bank.getResourceAmount(Resource.LUMBER));
        assertEquals(18, bank.getResourceAmount(Resource.BRICK));
    }

    //Test trading with the bank 3:1 trade boost - fails, player does not have enough resources
    @Test
    public void testBankTradeSuccess_3for1Trade_Failed_PlayerNotEnoughResources() {
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
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> controllerRef.set(new Controller(game, players, gameType)), "Setup while loop timed out");
        Controller controller = controllerRef.get();

        //--------------------------------------------------------------------
        //Beginning of test

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
        
        bank.reset();

        //Give the player only three resources
        Resource[] playerHandResources = {
                Resource.WOOL,
                Resource.WOOL,
        };

        player1.hand.addResources(playerHandResources);
        bank.removeResources(playerHandResources);

        assertEquals(2, player1.hand.getResourceCardCount());
        assertEquals(2, player1.hand.getResourceCardAmount(Resource.WOOL));
        assertEquals(0, player1.hand.getResourceCardAmount(Resource.GRAIN));
        assertEquals(17, bank.getResourceAmount(Resource.WOOL));
        assertEquals(19, bank.getResourceAmount(Resource.GRAIN));

        //add trade boost to the player
        player1.addTradeBoost(Resource.ANY);

        assertTrue(contains(player1.getTradeBoosts(), Resource.ANY));

        //Will attempt to trade 3 Wool for 1 Grain now:
        SuccessCode success = controller.tradeWithBank(Resource.WOOL, Resource.GRAIN);

        assertEquals(SuccessCode.INSUFFICIENT_RESOURCES, success);
        assertEquals(2, player1.hand.getResourceCardCount());
        assertEquals(2, player1.hand.getResourceCardAmount(Resource.WOOL));
        assertEquals(0, player1.hand.getResourceCardAmount(Resource.GRAIN));
        assertEquals(19, bank.getResourceAmount(Resource.GRAIN));
        assertEquals(17, bank.getResourceAmount(Resource.WOOL));
    }

    //Test trading with the bank 3:1 trade boost - fails, bank does not have enough resources
    @Test
    public void testBankTradeSuccess_3for1Trade_Failed_BankNotEnoughResources() {
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
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> controllerRef.set(new Controller(game, players, gameType)), "Setup while loop timed out");
        Controller controller = controllerRef.get();

        //--------------------------------------------------------------------
        //Beginning of test

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

        
        bank.reset();

        //Give the player 4x ore cards
        Resource[] playerHandResources = {
                Resource.ORE,
                Resource.ORE,
                Resource.ORE,
        };
        player1.hand.addResources(playerHandResources);
        bank.removeResources(playerHandResources);

        //Remove all the lumber from the bank so there are none to trade for
        bank.removeResource(Resource.LUMBER, 19);

        assertEquals(3, player1.hand.getResourceCardCount());
        assertEquals(3, player1.hand.getResourceCardAmount(Resource.ORE));
        assertEquals(0, player1.hand.getResourceCardAmount(Resource.LUMBER));
        assertEquals(16, bank.getResourceAmount(Resource.ORE));
        assertEquals(0, bank.getResourceAmount(Resource.LUMBER));

        //add trade boost to the player
        player1.addTradeBoost(Resource.ANY);

        assertTrue(contains(player1.getTradeBoosts(), Resource.ANY));

        //Attempt to trade 3 Or for 1x Lumber
        SuccessCode success = controller.tradeWithBank(Resource.ORE, Resource.LUMBER);

        assertEquals(SuccessCode.INSUFFICIENT_RESOURCES, success);
        assertEquals(3, player1.hand.getResourceCardCount());
        assertEquals(3, player1.hand.getResourceCardAmount(Resource.ORE));
        assertEquals(0, player1.hand.getResourceCardAmount(Resource.LUMBER));
        assertEquals(0, bank.getResourceAmount(Resource.LUMBER));
        assertEquals(16, bank.getResourceAmount(Resource.ORE));
    }

    //Test trading with the bank 2:1 lumber trade boost applied
    @Test
    public void testTradeBank_2for1Trade_Success() {
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
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> controllerRef.set(new Controller(game, players, gameType)), "Setup while loop timed out");
        Controller controller = controllerRef.get();

        //--------------------------------------------------------------------
        //Beginning of test

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

        
        bank.reset();

        Resource[] playerHandResources = {
                Resource.LUMBER,
                Resource.LUMBER,
        };

        player1.hand.addResources(playerHandResources);
        bank.removeResources(playerHandResources);

        assertEquals(2, player1.hand.getResourceCardCount());
        assertEquals(2, player1.hand.getResourceCardAmount(Resource.LUMBER));
        assertEquals(0, player1.hand.getResourceCardAmount(Resource.BRICK));
        assertEquals(17, bank.getResourceAmount(Resource.LUMBER));
        assertEquals(19, bank.getResourceAmount(Resource.BRICK));

        //add trade boost to the player
        player1.addTradeBoost(Resource.LUMBER);

        assertTrue(contains(player1.getTradeBoosts(), Resource.LUMBER));

        //Will be trading 2 Lumber for 1 Brick now:
        SuccessCode success = controller.tradeWithBank(Resource.LUMBER, Resource.BRICK);

        assertEquals(SuccessCode.SUCCESS, success);
        assertEquals(1, player1.hand.getResourceCardCount());
        assertEquals(1, player1.hand.getResourceCardAmount(Resource.BRICK));
        assertEquals(0, player1.hand.getResourceCardAmount(Resource.LUMBER));
        assertEquals(19, bank.getResourceAmount(Resource.LUMBER));
        assertEquals(18, bank.getResourceAmount(Resource.BRICK));
    }

    //Test trading with the bank 2:1 lumber trade boost applied - fails, player does not have enough resources
    @Test
    public void testTradeBank_2for1Trade_Failed_PlayerNotEnoughResources() {
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
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> controllerRef.set(new Controller(game, players, gameType)), "Setup while loop timed out");
        Controller controller = controllerRef.get();

        //--------------------------------------------------------------------
        //Beginning of test

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

        
        bank.reset();

        //Give the player only three resources
        Resource[] playerHandResources = {
                Resource.WOOL
        };

        player1.hand.addResources(playerHandResources);
        bank.removeResources(playerHandResources);

        assertEquals(1, player1.hand.getResourceCardCount());
        assertEquals(1, player1.hand.getResourceCardAmount(Resource.WOOL));
        assertEquals(0, player1.hand.getResourceCardAmount(Resource.GRAIN));
        assertEquals(18, bank.getResourceAmount(Resource.WOOL));
        assertEquals(19, bank.getResourceAmount(Resource.GRAIN));

        //add trade boost to the player
        player1.addTradeBoost(Resource.WOOL);

        assertTrue(contains(player1.getTradeBoosts(), Resource.WOOL));

        //Will attempt to trade 2 Wool for 1 Grain now:
        SuccessCode success = controller.tradeWithBank(Resource.WOOL, Resource.GRAIN);

        assertEquals(SuccessCode.INSUFFICIENT_RESOURCES, success);
        assertEquals(1, player1.hand.getResourceCardCount());
        assertEquals(1, player1.hand.getResourceCardAmount(Resource.WOOL));
        assertEquals(0, player1.hand.getResourceCardAmount(Resource.GRAIN));
        assertEquals(19, bank.getResourceAmount(Resource.GRAIN));
        assertEquals(18, bank.getResourceAmount(Resource.WOOL));
    }

    //Test trading with the bank 2:1 lumber trade boost applied - fails, bank does not have enough resources
    @Test
    public void testTradeBank_2for1Trade_Failed_BankNotEnoughResources() {
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
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> controllerRef.set(new Controller(game, players, gameType)), "Setup while loop timed out");
        Controller controller = controllerRef.get();

        //--------------------------------------------------------------------
        //Beginning of test

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

        
        bank.reset();

        //Give the player 4x ore cards
        Resource[] playerHandResources = {
                Resource.ORE,
                Resource.ORE,
        };
        player1.hand.addResources(playerHandResources);
        bank.removeResources(playerHandResources);

        //Remove all the lumber from the bank so there are none to trade for
        bank.removeResource(Resource.LUMBER, 19);

        assertEquals(2, player1.hand.getResourceCardCount());
        assertEquals(2, player1.hand.getResourceCardAmount(Resource.ORE));
        assertEquals(0, player1.hand.getResourceCardAmount(Resource.LUMBER));
        assertEquals(17, bank.getResourceAmount(Resource.ORE));
        assertEquals(0, bank.getResourceAmount(Resource.LUMBER));

        //add trade boost to the player
        player1.addTradeBoost(Resource.ORE);

        assertTrue(contains(player1.getTradeBoosts(), Resource.ORE));

        //Attempt to trade 2x Ore for 1x Lumber
        SuccessCode success = controller.tradeWithBank(Resource.ORE, Resource.LUMBER);

        assertEquals(SuccessCode.INSUFFICIENT_RESOURCES, success);
        assertEquals(2, player1.hand.getResourceCardCount());
        assertEquals(2, player1.hand.getResourceCardAmount(Resource.ORE));
        assertEquals(0, player1.hand.getResourceCardAmount(Resource.LUMBER));
        assertEquals(0, bank.getResourceAmount(Resource.LUMBER));
        assertEquals(17, bank.getResourceAmount(Resource.ORE));
    }
}
