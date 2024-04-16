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
import domain.graphs.GameboardGraph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static domain.bank.Resource.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

//F9: Ability for a player to trade resources with another player during their turn
public class F9Test {

    GameboardGraph gameboardGraph;
    Player player1;
    Player player2;
    Player player3;
    Player player4;
    Player[] players;
    Controller controller;

    @BeforeEach
    public void createGameObjects() {
        GameType gameType = GameType.Beginner;
        gameboardGraph = new GameboardGraph(gameType);
        GameLoader.initializeGraphs(gameboardGraph);

        Bank bank = new Bank();
        player1 = new Player(1, new HarvestBooster(), bank);
        player2 = new Player(2, new HarvestBooster(), bank);
        player3 = new Player(3, new HarvestBooster(), bank);
        player4 = new Player(4, new HarvestBooster(), bank);
        players = new Player[]{player1, player2, player3, player4};

        // other things dependent on these things
        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        Game game = new Game(gameBoard, gameboardGraph, devCardDeck, bank);

        // Assert that the beginner setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> controllerRef.set(new Controller(game, players, gameType)), "Setup while loop timed out");
        controller = controllerRef.get();

        for (Player p: players) {
            p.hand.clearResources();
        }
    }

    @Test
    public void testTradeCardsBetweenTwoPlayers_Successful() {
        Resource[] player1Resources = {LUMBER, BRICK};
        Resource[] player2Resources = {WOOL, ORE, ORE};

        int numCardsBefore1 = player1.hand.getResourceCount();
        int numCardsBefore2 = player2.hand.getResourceCount();

        player1.hand.addResources(player1Resources);
        player2.hand.addResources(player2Resources);

        SuccessCode success = controller.tradeWithPlayer(player2, player1Resources, player2Resources);
        assertEquals(SuccessCode.SUCCESS, success);
        assertEquals(numCardsBefore1+3, player1.hand.getResourceCount());
        assertEquals(numCardsBefore2+2, player2.hand.getResourceCount());
    }

    @Test
    public void testTradeCardsBetweenTwoPlayers_Fail_FirstPlayerNotEnoughResources() {
        Resource[] player1HandResources = {LUMBER, BRICK};
        Resource[] player2HandResources = {WOOL, ORE, ORE};

        player1.hand.addResources(player1HandResources);
        player2.hand.addResources(player2HandResources);

        int numCardsBefore1 = player1.hand.getResourceCount();
        int numCardsBefore2 = player2.hand.getResourceCount();

        Resource[] player1TradeResources = {LUMBER, LUMBER, BRICK};

        Resource[] player2TradeResources = {WOOL, ORE, ORE};

        SuccessCode success = controller.tradeWithPlayer(player2, player1TradeResources, player2TradeResources);
        assertEquals(SuccessCode.UNDEFINED, success);
        assertEquals(numCardsBefore1, player1.hand.getResourceCount());
        assertEquals(numCardsBefore2, player2.hand.getResourceCount());
    }

    @Test
    public void testTradeCardsBetweenTwoPlayers_Fail_SecondPlayerNotEnoughResources() {

        Resource[] player1HandResources = {LUMBER, BRICK};
        Resource[] player2HandResources = {WOOL, ORE, ORE};

        player1.hand.addResources(player1HandResources);
        player2.hand.addResources(player2HandResources);

        int numCardsBefore1 = player1.hand.getResourceCount();
        int numCardsBefore2 = player2.hand.getResourceCount();

        Resource[] player1TradeResources = {LUMBER, BRICK};

        Resource[] player2TradeResources = {WOOL, ORE, ORE, LUMBER};

        SuccessCode success = controller.tradeWithPlayer(player2, player1TradeResources, player2TradeResources);
        assertEquals(SuccessCode.UNDEFINED, success);
        assertEquals(numCardsBefore1, player1.hand.getResourceCount());
        assertEquals(numCardsBefore2, player2.hand.getResourceCount());
    }
}
