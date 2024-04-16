package integration;

import data.GameLoader;
import domain.controller.Controller;
import domain.controller.SuccessCode;
import domain.bank.Bank;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * F10: Ability for a player to purchase a development card during their turn. Requires a sheep, and wheat, and an ore to purchase
 */
public class F10Test {
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
    public void testBuyDevelopmentCard_SuccessfulPurchase() {
        player1.hand.addResource(WOOL, 5);
        player1.hand.addResource(GRAIN, 5);
        player1.hand.addResource(ORE, 5);

        assertEquals(15, player1.hand.getResourceCount());
        assertEquals(5, player1.hand.getResourceCount(WOOL));
        assertEquals(5, player1.hand.getResourceCount(GRAIN));
        assertEquals(5, player1.hand.getResourceCount(ORE));

        //attempt to purchase a few development cards
        SuccessCode success = controller.clickedBuyDevCard();

        //make sure one card is added and the correct amount of resources is added
        assertEquals(SuccessCode.SUCCESS, success);
        int devCardAmount =  0;
        for(DevCard r: player1.hand.devCards.keySet()){
            devCardAmount += player1.hand.devCards.get(r);
        }
        assertEquals(1, devCardAmount);
        assertEquals(12, player1.hand.getResourceCount());
        assertEquals(4, player1.hand.getResourceCount(WOOL));
        assertEquals(4, player1.hand.getResourceCount(GRAIN));
        assertEquals(4, player1.hand.getResourceCount(ORE));

        //buy another dev card
        success = controller.clickedBuyDevCard();

        assertEquals(SuccessCode.SUCCESS, success);
        devCardAmount =  0;
        for(DevCard r: player1.hand.devCards.keySet()){
            devCardAmount += player1.hand.devCards.get(r);
        }
        assertEquals(2, devCardAmount);
        assertEquals(9, player1.hand.getResourceCount());
        assertEquals(3, player1.hand.getResourceCount(WOOL));
        assertEquals(3, player1.hand.getResourceCount(GRAIN));
        assertEquals(3, player1.hand.getResourceCount(ORE));

        //buy a third dev card
        success = controller.clickedBuyDevCard();

        assertEquals(SuccessCode.SUCCESS, success);
        devCardAmount =  0;
        for(DevCard r: player1.hand.devCards.keySet()){
            devCardAmount += player1.hand.devCards.get(r);
        }
        assertEquals(3, devCardAmount);
        assertEquals(6, player1.hand.getResourceCount());
        assertEquals(2, player1.hand.getResourceCount(WOOL));
        assertEquals(2, player1.hand.getResourceCount(GRAIN));
        assertEquals(2, player1.hand.getResourceCount(ORE));
    }

    @Test
    public void testBuyDevelopmentCard_Fail_notEnoughResources() {
        player1.hand.addResource(WOOL, 2);
        player1.hand.addResource(GRAIN, 1);
        player1.hand.addResource(ORE, 1);

        assertEquals(4, player1.hand.getResourceCount());
        assertEquals(2, player1.hand.getResourceCount(WOOL));
        assertEquals(1, player1.hand.getResourceCount(GRAIN));
        assertEquals(1, player1.hand.getResourceCount(ORE));

        //buy one card successfully first
        SuccessCode success = controller.clickedBuyDevCard();

        //make sure one card is added and the correct amount of resources is added
        assertEquals(SuccessCode.SUCCESS, success);
        int devCardAmount =  0;
        for(DevCard r: player1.hand.devCards.keySet()){
            devCardAmount += player1.hand.devCards.get(r);
        }
        assertEquals(1, devCardAmount);
        assertEquals(1, player1.hand.getResourceCount());
        assertEquals(1, player1.hand.getResourceCount(WOOL));
        assertEquals(0, player1.hand.getResourceCount(GRAIN));
        assertEquals(0, player1.hand.getResourceCount(ORE));

        //now try and buy one and fail because the player does not have enough resources
        success = controller.clickedBuyDevCard();

        assertEquals(SuccessCode.INSUFFICIENT_RESOURCES, success);
        devCardAmount =  0;
        for(DevCard r: player1.hand.devCards.keySet()){
            devCardAmount += player1.hand.devCards.get(r);
        }
        assertEquals(1, devCardAmount);
        assertEquals(1, player1.hand.getResourceCount());
        assertEquals(1, player1.hand.getResourceCount(WOOL));
        assertEquals(0, player1.hand.getResourceCount(GRAIN));
        assertEquals(0, player1.hand.getResourceCount(ORE));
    }

    @Test
    public void testBuyDevelopmentCard_Fail_DevCardDeckIsEmpty() {
        //We need to empty the development card deck. We will do this in two batches:
        //Buy the first 19 cards
        //Then buy the last 6 cards

        //First 19:
        player1.hand.addResource(WOOL, 19);
        player1.hand.addResource(GRAIN, 19);
        player1.hand.addResource(ORE, 19);

        for(int i = 0; i < 19; i++){
            SuccessCode success = controller.clickedBuyDevCard();
            assertEquals(SuccessCode.SUCCESS, success);
        }

        assertEquals(0, player1.hand.getResourceCount());
        int devCardAmount =  0;
        for(DevCard r: player1.hand.devCards.keySet()){
            devCardAmount += player1.hand.devCards.get(r);
        }
        assertEquals(19, devCardAmount);

        //Last 6:
        player1.hand.addResource(WOOL, 6);
        player1.hand.addResource(GRAIN, 6);
        player1.hand.addResource(ORE, 6);

        for(int i = 0; i < 6; i++){
            SuccessCode success = controller.clickedBuyDevCard();
            assertEquals(SuccessCode.SUCCESS, success);
        }

        assertEquals(0, player1.hand.getResourceCount());
        devCardAmount =  0;
        for(DevCard r: player1.hand.devCards.keySet()){
            devCardAmount += player1.hand.devCards.get(r);
        }
        assertEquals(25, devCardAmount);

        //Now that the player has all 25 dev cards, attempt to buy another dev card
        player1.hand.addResource(WOOL, 1);
        player1.hand.addResource(GRAIN, 1);
        player1.hand.addResource(ORE, 1);

        SuccessCode success = controller.clickedBuyDevCard();
        assertEquals(SuccessCode.EMPTY_DEV_CARD_DECK, success);
        devCardAmount =  0;
        for(DevCard r: player1.hand.devCards.keySet()){
            devCardAmount += player1.hand.devCards.get(r);
        }
        assertEquals(25, devCardAmount);
        assertEquals(3, player1.hand.getResourceCount());
        assertEquals(1, player1.hand.getResourceCount(WOOL));
        assertEquals(1, player1.hand.getResourceCount(GRAIN));
        assertEquals(1, player1.hand.getResourceCount(ORE));
    }
}
