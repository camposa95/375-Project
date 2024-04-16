package integration;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import data.GameLoader;
import domain.player.HarvestBooster;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.controller.Controller;
import domain.controller.GameState;
import domain.controller.SuccessCode;
import domain.bank.Bank;
import domain.devcarddeck.DevCard;
import domain.devcarddeck.DevelopmentCardDeck;
import domain.game.Game;
import domain.gameboard.GameBoard;
import domain.game.GameType;
import domain.player.Player;
import domain.bank.Resource;
import domain.graphs.GameboardGraph;

import static domain.bank.Resource.*;
import static org.junit.jupiter.api.Assertions.*;


/**
 * The purpose of this test class is to test feature 11 (F11):
 *      Ability for a Player to play one Development Card from their hand during their turn.
 */
public class F11Test {

    private static final Resource[] RESOURCES_FOR_CARD = {WOOL, GRAIN, ORE};

    private static final int VICTORY_POINTS_FROM_SETUP = 2;

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
    
    // helper method that will loop the game back to the current player by ending turn a bunch
    private void loopToBeginning(final Controller controller) {
        for (int i = 0; i < 4; i++) {
            controller.setState(GameState.DEFAULT);
            assertEquals(SuccessCode.SUCCESS, controller.endTurn());
        }
    }

    @Test
    public void testPlayCardCorrectly() {
        // ------------------------------------ for knight ------------------------------------------

        // give the player some resources to buy
        player1.hand.addResources(RESOURCES_FOR_CARD);

        // player purchases the card
        assertTrue(player1.purchaseDevCard(DevCard.KNIGHT));

        // wait till next turn to play card
        loopToBeginning(controller);
        assertEquals(player1, controller.getCurrentPlayer());
        assertTrue(controller.getDevCardsEnabled());

        // play knight card
        assertEquals(SuccessCode.SUCCESS, controller.playKnightCard());
        assertEquals(0, player1.hand.devCards.get(DevCard.KNIGHT));
        assertFalse(controller.getDevCardsEnabled());

        // ------------------------------------ for plenty ------------------------------------------

        // give the player some resources to buy
        player1.hand.addResources(RESOURCES_FOR_CARD);

        // player purchases the card
        assertTrue(player1.purchaseDevCard(DevCard.PLENTY));

        // wait till next turn to play card
        loopToBeginning(controller);
        assertEquals(player1, controller.getCurrentPlayer());
        assertTrue(controller.getDevCardsEnabled());

        // play card
        assertEquals(SuccessCode.SUCCESS, controller.playYearOfPlenty(WOOL, GRAIN));
        assertEquals(0, player1.hand.devCards.get(DevCard.PLENTY));
        assertFalse(controller.getDevCardsEnabled());

        // ------------------------------------ for Monopoly ------------------------------------------

        // give the player some resources to buy
        player1.hand.addResources(RESOURCES_FOR_CARD);

        // player purchases the card
        assertTrue(player1.purchaseDevCard(DevCard.MONOPOLY));

        // wait till next turn to play card
        loopToBeginning(controller);
        assertEquals(player1, controller.getCurrentPlayer());
        assertTrue(controller.getDevCardsEnabled());

        // play card
        assertEquals(SuccessCode.SUCCESS, controller.playMonopolyCard(WOOL));
        assertEquals(0, player1.hand.devCards.get(DevCard.MONOPOLY));
        assertFalse(controller.getDevCardsEnabled());

        // ------------------------------------ for Road ------------------------------------------

        // give the player some resources to buy
        player1.hand.addResources(RESOURCES_FOR_CARD);

        // player purchases the card
        assertTrue(player1.purchaseDevCard(DevCard.ROAD));

        // wait till next turn to play card
        loopToBeginning(controller);
        assertEquals(player1, controller.getCurrentPlayer());
        assertTrue(controller.getDevCardsEnabled());

        // play knight card
        assertEquals(SuccessCode.SUCCESS, controller.useRoadBuildingCard());
        assertEquals(0, player1.hand.devCards.get(DevCard.ROAD));
        assertFalse(controller.getDevCardsEnabled());

        // ------------------------------------ for Victory Point cards enabled ------------------------------------------

        // let dev cards be enabled, but note they don't need to be
        controller.setDevCardsEnabled(true);

        // give the player some resources to buy
        player1.hand.addResources(RESOURCES_FOR_CARD);

        // player purchases the card
        assertTrue(player1.purchaseDevCard(DevCard.VICTORY));

        // don't need to wait for next turn

        // card should be automatically played and victory points increased
        assertEquals(VICTORY_POINTS_FROM_SETUP + 1, player1.getVictoryPoints());

        // we don't need to disable cards after using this one
        assertTrue(controller.getDevCardsEnabled());

        // ------------------------------------ for Victory Point cards disabled ------------------------------------------

        // disable dev cards, should matter
        controller.setDevCardsEnabled(false);

        // give the player some resources to buy
        player1.hand.addResources(RESOURCES_FOR_CARD);

        // player purchases the card
        assertTrue(player1.purchaseDevCard(DevCard.VICTORY));

        // don't need to wait for next turn

        // card should be automatically played and victory points increased
        assertEquals(VICTORY_POINTS_FROM_SETUP + 1 + 1, player1.getVictoryPoints());

        // dev cards should still be disabled, nothing changed
        assertFalse(controller.getDevCardsEnabled());
    }

    @Test
    public void testPlayCardTriedMoreThan1() {
        // give the player some resources to buy
        player1.hand.addResources(RESOURCES_FOR_CARD);
        player1.hand.addResources(RESOURCES_FOR_CARD);
        player1.hand.addResources(RESOURCES_FOR_CARD);
        player1.hand.addResources(RESOURCES_FOR_CARD);

        // player purchases the card
        assertTrue(player1.purchaseDevCard(DevCard.ROAD));
        assertTrue(player1.purchaseDevCard(DevCard.KNIGHT));
        assertTrue(player1.purchaseDevCard(DevCard.PLENTY));
        assertTrue(player1.purchaseDevCard(DevCard.MONOPOLY));

        // wait till next turn to play cards
        loopToBeginning(controller);
        assertEquals(player1, controller.getCurrentPlayer());

        // assume devCardsEnabled is false because we already played one this turn. this was tested above
        controller.setDevCardsEnabled(false);

        // play knight card
        assertEquals(SuccessCode.CANNOT_PLAY_CARD, controller.playKnightCard());

        // play year of plenty card
        assertEquals(SuccessCode.CANNOT_PLAY_CARD, controller.playYearOfPlenty(WOOL, GRAIN));

        // play monopoly card
        assertEquals(SuccessCode.CANNOT_PLAY_CARD, controller.playMonopolyCard(WOOL));

        // play road card
        assertEquals(SuccessCode.CANNOT_PLAY_CARD, controller.useRoadBuildingCard());
    }

    @Test
    public void testPlayCardTriedToPlayBoughtSameTurn() {
        // give the player some resources to buy
        player1.hand.addResources(RESOURCES_FOR_CARD);
        player1.hand.addResources(RESOURCES_FOR_CARD);
        player1.hand.addResources(RESOURCES_FOR_CARD);
        player1.hand.addResources(RESOURCES_FOR_CARD);

        // player purchases the card
        assertTrue(player1.purchaseDevCard(DevCard.ROAD));
        assertTrue(player1.purchaseDevCard(DevCard.KNIGHT));
        assertTrue(player1.purchaseDevCard(DevCard.PLENTY));
        assertTrue(player1.purchaseDevCard(DevCard.MONOPOLY));

        // don't wait till next turn to try to play card
        // dev cards enabled should be true
        assertTrue(controller.getDevCardsEnabled());

        // play knight card
        assertEquals(SuccessCode.CANNOT_PLAY_CARD, controller.playKnightCard());
        // since the cards wasn't played we don't disable other cards from trying to be played
        assertTrue(controller.getDevCardsEnabled());

        // play year of plenty card
        assertEquals(SuccessCode.CANNOT_PLAY_CARD, controller.playYearOfPlenty(WOOL, GRAIN));
        // since the cards wasn't played we don't disable other cards from trying to be played
        assertTrue(controller.getDevCardsEnabled());

        // play monopoly card
        assertEquals(SuccessCode.CANNOT_PLAY_CARD, controller.playMonopolyCard(WOOL));
        // since the cards wasn't played we don't disable other cards from trying to be played
        assertTrue(controller.getDevCardsEnabled());

        // play road card
        assertEquals(SuccessCode.CANNOT_PLAY_CARD, controller.useRoadBuildingCard());
        // since the cards wasn't played we don't disable other cards from trying to be played
        assertTrue(controller.getDevCardsEnabled());
    }
}
