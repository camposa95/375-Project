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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import static domain.bank.Resource.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class F12Test {

    VertexGraph vertexes;
    RoadGraph roads;
    Bank bank;
    Player player1;
    Player player2;
    Player player3;
    Player player4;
    Player[] players;
    Controller controller;

    @BeforeEach
    public void createGameObjects() {
        GameType gameType = GameType.Beginner;
        vertexes = new VertexGraph(gameType);
        roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        bank = new Bank();
        player1 = new Player(1, new HarvestBooster(), bank);
        player2 = new Player(2, new HarvestBooster(), bank);
        player3 = new Player(3, new HarvestBooster(), bank);
        player4 = new Player(4, new HarvestBooster(), bank);
        players = new Player[]{player1, player2, player3, player4};

        // other things dependent on these things
        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        Game game = new Game(gameBoard, vertexes, roads, devCardDeck, bank);

        // Assert that the beginner setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> controllerRef.set(new Controller(game, players, gameType)), "Setup while loop timed out");
        controller = controllerRef.get();

        for (Player p: players) {
            p.hand.clearResources();
        }
        bank.reset();
    }

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
        Resource[] playerHandResources = {LUMBER, LUMBER, LUMBER, LUMBER};

        player1.hand.addResources(playerHandResources);
        bank.removeResources(playerHandResources);

        assertEquals(4, player1.hand.getResourceCount());
        assertEquals(4, player1.hand.getResourceCount(LUMBER));
        assertEquals(0, player1.hand.getResourceCount(BRICK));
        assertEquals(15, bank.getResourceAmount(LUMBER));
        assertEquals(19, bank.getResourceAmount(BRICK));

        //Will be trading 4 Lumber for 1 Brick now:
        SuccessCode success = controller.tradeWithBank(LUMBER, BRICK);

        assertEquals(SuccessCode.SUCCESS, success);
        assertEquals(1, player1.hand.getResourceCount());
        assertEquals(1, player1.hand.getResourceCount(BRICK));
        assertEquals(0, player1.hand.getResourceCount(LUMBER));
        assertEquals(19, bank.getResourceAmount(LUMBER));
        assertEquals(18, bank.getResourceAmount(BRICK));
    }

    //Test the 4:1 trade failing: Player does not have enough resources
    @Test
    public void testBankTradeFailed_PlayerNotEnoughResources_baseTrade() {
        //Give the player only three resources
        Resource[] playerHandResources = {WOOL, WOOL, WOOL};

        player1.hand.addResources(playerHandResources);
        bank.removeResources(playerHandResources);

        assertEquals(3, player1.hand.getResourceCount());
        assertEquals(3, player1.hand.getResourceCount(WOOL));
        assertEquals(0, player1.hand.getResourceCount(GRAIN));
        assertEquals(16, bank.getResourceAmount(WOOL));
        assertEquals(19, bank.getResourceAmount(GRAIN));

        //Will attempt to trade 4 Wool for 1 Grain now:
        SuccessCode success = controller.tradeWithBank(WOOL, GRAIN);

        assertEquals(SuccessCode.INSUFFICIENT_RESOURCES, success);
        assertEquals(3, player1.hand.getResourceCount());
        assertEquals(3, player1.hand.getResourceCount(WOOL));
        assertEquals(0, player1.hand.getResourceCount(GRAIN));
        assertEquals(19, bank.getResourceAmount(GRAIN));
        assertEquals(16, bank.getResourceAmount(WOOL));
    }

    //Test trading with the bank - failed because the bank is out of resources
    @Test
    public void testBankTradeFailed_BankNotEnoughResources_baseTrade() {
        //Give the player 4x ore cards
        Resource[] playerHandResources = {ORE, ORE, ORE, ORE};
        player1.hand.addResources(playerHandResources);
        bank.removeResources(playerHandResources);

        //Remove all the lumber from the bank so there are none to trade for
        bank.removeResource(LUMBER, 19);

        assertEquals(4, player1.hand.getResourceCount());
        assertEquals(4, player1.hand.getResourceCount(ORE));
        assertEquals(0, player1.hand.getResourceCount(LUMBER));
        assertEquals(15, bank.getResourceAmount(ORE));
        assertEquals(0, bank.getResourceAmount(LUMBER));

        //Attempt to trade 4 Lumber for 1x Ore
        SuccessCode success = controller.tradeWithBank(ORE, LUMBER);

        assertEquals(SuccessCode.INSUFFICIENT_RESOURCES, success);
        assertEquals(4, player1.hand.getResourceCount());
        assertEquals(4, player1.hand.getResourceCount(ORE));
        assertEquals(0, player1.hand.getResourceCount(LUMBER));
        assertEquals(0, bank.getResourceAmount(LUMBER));
        assertEquals(15, bank.getResourceAmount(ORE));
    }

    //Test trading with the bank 3:1 trade boost applied
    @Test
    public void testBankTradeSuccess_3for1Trade() {
        Resource[] playerHandResources = {LUMBER, LUMBER, LUMBER};

        player1.hand.addResources(playerHandResources);
        bank.removeResources(playerHandResources);

        assertEquals(3, player1.hand.getResourceCount());
        assertEquals(3, player1.hand.getResourceCount(LUMBER));
        assertEquals(0, player1.hand.getResourceCount(BRICK));
        assertEquals(16, bank.getResourceAmount(LUMBER));
        assertEquals(19, bank.getResourceAmount(BRICK));

        //add trade boost to the player
        player1.addTradeBoost(Resource.ANY);

        assertTrue(contains(player1.getTradeBoosts(), Resource.ANY));

        //Will be trading 3 Lumber for 1 Brick now:
        SuccessCode success = controller.tradeWithBank(LUMBER, BRICK);

        assertEquals(SuccessCode.SUCCESS, success);
        assertEquals(1, player1.hand.getResourceCount());
        assertEquals(1, player1.hand.getResourceCount(BRICK));
        assertEquals(0, player1.hand.getResourceCount(LUMBER));
        assertEquals(19, bank.getResourceAmount(LUMBER));
        assertEquals(18, bank.getResourceAmount(BRICK));
    }

    //Test trading with the bank 3:1 trade boost - fails, player does not have enough resources
    @Test
    public void testBankTradeSuccess_3for1Trade_Failed_PlayerNotEnoughResources() {
        //Give the player only three resources
        Resource[] playerHandResources = {WOOL, WOOL};

        player1.hand.addResources(playerHandResources);
        bank.removeResources(playerHandResources);

        assertEquals(2, player1.hand.getResourceCount());
        assertEquals(2, player1.hand.getResourceCount(WOOL));
        assertEquals(0, player1.hand.getResourceCount(GRAIN));
        assertEquals(17, bank.getResourceAmount(WOOL));
        assertEquals(19, bank.getResourceAmount(GRAIN));

        //add trade boost to the player
        player1.addTradeBoost(Resource.ANY);

        assertTrue(contains(player1.getTradeBoosts(), Resource.ANY));

        //Will attempt to trade 3 Wool for 1 Grain now:
        SuccessCode success = controller.tradeWithBank(WOOL, GRAIN);

        assertEquals(SuccessCode.INSUFFICIENT_RESOURCES, success);
        assertEquals(2, player1.hand.getResourceCount());
        assertEquals(2, player1.hand.getResourceCount(WOOL));
        assertEquals(0, player1.hand.getResourceCount(GRAIN));
        assertEquals(19, bank.getResourceAmount(GRAIN));
        assertEquals(17, bank.getResourceAmount(WOOL));
    }

    //Test trading with the bank 3:1 trade boost - fails, bank does not have enough resources
    @Test
    public void testBankTradeSuccess_3for1Trade_Failed_BankNotEnoughResources() {
        //Give the player 4x ore cards
        Resource[] playerHandResources = {ORE, ORE, ORE};
        player1.hand.addResources(playerHandResources);
        bank.removeResources(playerHandResources);

        //Remove all the lumber from the bank so there are none to trade for
        bank.removeResource(LUMBER, 19);

        assertEquals(3, player1.hand.getResourceCount());
        assertEquals(3, player1.hand.getResourceCount(ORE));
        assertEquals(0, player1.hand.getResourceCount(LUMBER));
        assertEquals(16, bank.getResourceAmount(ORE));
        assertEquals(0, bank.getResourceAmount(LUMBER));

        //add trade boost to the player
        player1.addTradeBoost(Resource.ANY);

        assertTrue(contains(player1.getTradeBoosts(), Resource.ANY));

        //Attempt to trade 3 Or for 1x Lumber
        SuccessCode success = controller.tradeWithBank(ORE, LUMBER);

        assertEquals(SuccessCode.INSUFFICIENT_RESOURCES, success);
        assertEquals(3, player1.hand.getResourceCount());
        assertEquals(3, player1.hand.getResourceCount(ORE));
        assertEquals(0, player1.hand.getResourceCount(LUMBER));
        assertEquals(0, bank.getResourceAmount(LUMBER));
        assertEquals(16, bank.getResourceAmount(ORE));
    }

    //Test trading with the bank 2:1 lumber trade boost applied
    @Test
    public void testTradeBank_2for1Trade_Success() {
        Resource[] playerHandResources = {LUMBER, LUMBER};

        player1.hand.addResources(playerHandResources);
        bank.removeResources(playerHandResources);

        assertEquals(2, player1.hand.getResourceCount());
        assertEquals(2, player1.hand.getResourceCount(LUMBER));
        assertEquals(0, player1.hand.getResourceCount(BRICK));
        assertEquals(17, bank.getResourceAmount(LUMBER));
        assertEquals(19, bank.getResourceAmount(BRICK));

        //add trade boost to the player
        player1.addTradeBoost(LUMBER);

        assertTrue(contains(player1.getTradeBoosts(), LUMBER));

        //Will be trading 2 Lumber for 1 Brick now:
        SuccessCode success = controller.tradeWithBank(LUMBER, BRICK);

        assertEquals(SuccessCode.SUCCESS, success);
        assertEquals(1, player1.hand.getResourceCount());
        assertEquals(1, player1.hand.getResourceCount(BRICK));
        assertEquals(0, player1.hand.getResourceCount(LUMBER));
        assertEquals(19, bank.getResourceAmount(LUMBER));
        assertEquals(18, bank.getResourceAmount(BRICK));
    }

    //Test trading with the bank 2:1 lumber trade boost applied - fails, player does not have enough resources
    @Test
    public void testTradeBank_2for1Trade_Failed_PlayerNotEnoughResources() {
        //Give the player only three resources
        Resource[] playerHandResources = {WOOL};

        player1.hand.addResources(playerHandResources);
        bank.removeResources(playerHandResources);

        assertEquals(1, player1.hand.getResourceCount());
        assertEquals(1, player1.hand.getResourceCount(WOOL));
        assertEquals(0, player1.hand.getResourceCount(GRAIN));
        assertEquals(18, bank.getResourceAmount(WOOL));
        assertEquals(19, bank.getResourceAmount(GRAIN));

        //add trade boost to the player
        player1.addTradeBoost(WOOL);

        assertTrue(contains(player1.getTradeBoosts(), WOOL));

        //Will attempt to trade 2 Wool for 1 Grain now:
        SuccessCode success = controller.tradeWithBank(WOOL, GRAIN);

        assertEquals(SuccessCode.INSUFFICIENT_RESOURCES, success);
        assertEquals(1, player1.hand.getResourceCount());
        assertEquals(1, player1.hand.getResourceCount(WOOL));
        assertEquals(0, player1.hand.getResourceCount(GRAIN));
        assertEquals(19, bank.getResourceAmount(GRAIN));
        assertEquals(18, bank.getResourceAmount(WOOL));
    }

    //Test trading with the bank 2:1 lumber trade boost applied - fails, bank does not have enough resources
    @Test
    public void testTradeBank_2for1Trade_Failed_BankNotEnoughResources() {
        //Give the player 4x ore cards
        Resource[] playerHandResources = {ORE, ORE};
        player1.hand.addResources(playerHandResources);
        bank.removeResources(playerHandResources);

        //Remove all the lumber from the bank so there are none to trade for
        bank.removeResource(LUMBER, 19);

        assertEquals(2, player1.hand.getResourceCount());
        assertEquals(2, player1.hand.getResourceCount(ORE));
        assertEquals(0, player1.hand.getResourceCount(LUMBER));
        assertEquals(17, bank.getResourceAmount(ORE));
        assertEquals(0, bank.getResourceAmount(LUMBER));

        //add trade boost to the player
        player1.addTradeBoost(ORE);

        assertTrue(contains(player1.getTradeBoosts(), ORE));

        //Attempt to trade 2x Ore for 1x Lumber
        SuccessCode success = controller.tradeWithBank(ORE, LUMBER);

        assertEquals(SuccessCode.INSUFFICIENT_RESOURCES, success);
        assertEquals(2, player1.hand.getResourceCount());
        assertEquals(2, player1.hand.getResourceCount(ORE));
        assertEquals(0, player1.hand.getResourceCount(LUMBER));
        assertEquals(0, bank.getResourceAmount(LUMBER));
        assertEquals(17, bank.getResourceAmount(ORE));
    }
}
