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
import domain.graphs.GameboardGraph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import static domain.bank.Resource.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * F12: Victory point development card: allow the player to secretly collect a victory point for each of these cards that they hold
 */
public class F17Test {
    //Note: on the GUI level, we decided against the "secretly" portion since four players are sharing the same screen,
    // and you'd be able to deduce what card they got either way

    GameboardGraph gameboardGraph;
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
        gameboardGraph = new GameboardGraph(gameType);
        GameLoader.initializeGraphs(gameboardGraph);

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
        Game game = new Game(gameBoard, gameboardGraph, devCardDeck, bank);

        // Assert that the beginner setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> controllerRef.set(new Controller(game, players, gameType)), "Setup while loop timed out");
        controller = controllerRef.get();

        for (Player p: players) {
            p.hand.clearResources();
        }
        bank.reset();
    }

    //Add Victory Point card to hand and show that VP goes up for the player
    @Test
    public void testAddVictoryPointCard_normalPlay() {
        Resource[] resourceForHand = {WOOL, GRAIN, ORE};
        player1.hand.addResources(resourceForHand);
        bank.removeResources(resourceForHand);

        assertEquals(3, player1.hand.getResourceCount());
        assertEquals(1, player1.hand.getResourceCount(WOOL));
        assertEquals(1, player1.hand.getResourceCount(GRAIN));
        assertEquals(1, player1.hand.getResourceCount(ORE));
        assertEquals(18, bank.getResourceAmount(WOOL));
        assertEquals(18, bank.getResourceAmount(GRAIN));
        assertEquals(18, bank.getResourceAmount(ORE));

        int victoryPointsBefore = player1.getVictoryPoints();

        boolean success = player1.purchaseDevCard(DevCard.VICTORY);

        assertTrue(success);
        int devCardAmount = 0;
        for(DevCard card: player1.hand.devCards.keySet()){
            devCardAmount += player1.hand.devCards.get(card);
        }
        assertEquals(1, devCardAmount);
        assertEquals(victoryPointsBefore+1, player1.getVictoryPoints());
        assertEquals(0, player1.hand.getResourceCount());
        assertEquals(19, bank.getResourceAmount(WOOL));
        assertEquals(19, bank.getResourceAmount(GRAIN));
        assertEquals(19, bank.getResourceAmount(ORE));
    }

    //Add Victory Point card, then cycle between two players and show that ending turn will win the game
    @Test
    public void testAddVictoryPointCard_winGameOnPurchase() {
        //artificially set the player's VP to 9 so buying a dev card will trigger a game win
        //we will show this on player 2's end turn through controller
        player1.setVictoryPoints(9);

        Resource[] resourceForHand = {WOOL, GRAIN, ORE};
        player1.hand.addResources(resourceForHand);
        bank.removeResources(resourceForHand);

        assertEquals(3, player1.hand.getResourceCount());
        assertEquals(1, player1.hand.getResourceCount(WOOL));
        assertEquals(1, player1.hand.getResourceCount(GRAIN));
        assertEquals(1, player1.hand.getResourceCount(ORE));
        assertEquals(18, bank.getResourceAmount(WOOL));
        assertEquals(18, bank.getResourceAmount(GRAIN));
        assertEquals(18, bank.getResourceAmount(ORE));

        int victoryPointsBefore = player1.getVictoryPoints();

        boolean success = player1.purchaseDevCard(DevCard.VICTORY);

        assertTrue(success);
        int devCardAmount = 0;
        for(DevCard card: player1.hand.devCards.keySet()){
            devCardAmount += player1.hand.devCards.get(card);
        }
        assertEquals(1, devCardAmount);
        assertEquals(victoryPointsBefore+1, player1.getVictoryPoints());
        assertEquals(0, player1.hand.getResourceCount());
        assertEquals(19, bank.getResourceAmount(WOOL));
        assertEquals(19, bank.getResourceAmount(GRAIN));
        assertEquals(19, bank.getResourceAmount(ORE));

        //Now cycle through all the way back to player 1
        controller.setState(GameState.DEFAULT);
        assertEquals(SuccessCode.SUCCESS, controller.endTurn());
        controller.setState(GameState.DEFAULT);
        assertEquals(SuccessCode.SUCCESS, controller.endTurn());
        controller.setState(GameState.DEFAULT);
        assertEquals(SuccessCode.SUCCESS, controller.endTurn());


        controller.setState(GameState.DEFAULT);
        assertEquals(SuccessCode.GAME_WIN, controller.endTurn());
    }

    //make sure that adding a sixth victory point card is not possible
    @Test
    public void testAddVictoryPoint_FailsTooManyVPCards() {
        player1.hand.addResource(WOOL, 6);
        player1.hand.addResource(GRAIN, 6);
        player1.hand.addResource(ORE, 6);
        bank.removeResource(WOOL, 6);
        bank.removeResource(GRAIN, 6);
        bank.removeResource(ORE, 6);

        assertEquals(18, player1.hand.getResourceCount());
        assertEquals(6, player1.hand.getResourceCount(WOOL));
        assertEquals(6, player1.hand.getResourceCount(GRAIN));
        assertEquals(6, player1.hand.getResourceCount(ORE));
        assertEquals(13, bank.getResourceAmount(WOOL));
        assertEquals(13, bank.getResourceAmount(GRAIN));
        assertEquals(13, bank.getResourceAmount(ORE));

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
            assertEquals(18-(3*(i+1)), player1.hand.getResourceCount());
            assertEquals(13+(i+1), bank.getResourceAmount(WOOL));
            assertEquals(13+(i+1), bank.getResourceAmount(GRAIN));
            assertEquals(13+(i+1), bank.getResourceAmount(ORE));
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
        assertEquals(3, player1.hand.getResourceCount());
        assertEquals(18, bank.getResourceAmount(WOOL));
        assertEquals(18, bank.getResourceAmount(GRAIN));
        assertEquals(18, bank.getResourceAmount(ORE));

    }
}
