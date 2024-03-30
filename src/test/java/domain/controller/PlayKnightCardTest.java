package domain.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import domain.controller.Controller;
import domain.controller.SuccessCode;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import domain.game.CardNotPlayableException;
import domain.devcarddeck.DevCard;
import domain.game.Game;
import domain.game.GameType;
import domain.game.NotEnoughResourcesException;
import domain.player.Player;

public class PlayKnightCardTest {
    
    @Test
    public void testPlayKnightCardSuccess() throws NotEnoughResourcesException, CardNotPlayableException {
        Game mockedGame = EasyMock.createNiceMock(Game.class);
        Player mockedPlayer1 = EasyMock.createMock(Player.class);
        Player mockedPlayer2 = EasyMock.createMock(Player.class);
        Player mockedPlayer3 = EasyMock.createMock(Player.class);
        Player mockedPlayer4 = EasyMock.createMock(Player.class);
        Player[] players = {mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4};
        Controller controller = new Controller(mockedGame, players, GameType.Advanced);


        // set up the variables for the test
        controller.setCurrentPlayer(mockedPlayer1);


        // Expectations
        EasyMock.expect(mockedPlayer1.useDevCard(DevCard.KNIGHT)).andReturn(true);

        // stuff for seeing where to distribute the largest army card
        EasyMock.expect(mockedPlayer1.getNumKnightsPlayed()).andReturn(1);
        EasyMock.expect(mockedPlayer2.getNumKnightsPlayed()).andReturn(0);
        EasyMock.expect(mockedPlayer3.getNumKnightsPlayed()).andReturn(0);
        EasyMock.expect(mockedPlayer4.getNumKnightsPlayed()).andReturn(0);

        EasyMock.expect(mockedPlayer1.hasLargestArmy()).andReturn(false);
        EasyMock.expect(mockedPlayer2.hasLargestArmy()).andReturn(false);
        EasyMock.expect(mockedPlayer3.hasLargestArmy()).andReturn(false);
        EasyMock.expect(mockedPlayer4.hasLargestArmy()).andReturn(false);

        // player can play the card
        mockedPlayer1.incrementNumKnights();

        // player didn't gain enough points to win
        EasyMock.expect(mockedPlayer1.getVictoryPoints()).andReturn(9);


        // assert that dev cards are enabled before the card is played
        assertTrue(controller.getDevCardsEnabled());


        // method call
        EasyMock.replay(mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4);
        SuccessCode actual = controller.playKnightCard();
        EasyMock.verify(mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4);

        // assert on expected results
        assertEquals(SuccessCode.SUCCESS, actual);
        assertFalse(controller.getDevCardsEnabled()); // dev cards are now disabled becasuse we can only play one per turn
    }

    @Test
    public void testPlayKnightCardNoCard() throws NotEnoughResourcesException, CardNotPlayableException {
        Game mockedGame = EasyMock.createMock(Game.class);
        Player mockedPlayer1 = EasyMock.createMock(Player.class);
        Player mockedPlayer2 = EasyMock.createMock(Player.class);
        Player mockedPlayer3 = EasyMock.createMock(Player.class);
        Player mockedPlayer4 = EasyMock.createMock(Player.class);
        Player[] players = {mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4};
        Controller controller = new Controller(mockedGame, players, GameType.Advanced);


        // set up the variables for the test
        controller.setCurrentPlayer(mockedPlayer1);


        // Expectations
        EasyMock.expect(mockedPlayer1.useDevCard(DevCard.KNIGHT)).andReturn(false);
        // player does not play the card


        // assert that dev cards are enabled before the card is played
        assertTrue(controller.getDevCardsEnabled());


        // method call
        EasyMock.replay(mockedPlayer1);
        SuccessCode actual = controller.playKnightCard();
        EasyMock.verify(mockedPlayer1);

        // assert on expected results
        assertEquals(SuccessCode.CANNOT_PLAY_CARD, actual);
        assertTrue(controller.getDevCardsEnabled()); // dev cards are now disabled becasuse we can only play one per turn
    }

    @Test
    public void testPlayKnightCardsDisabled() throws CardNotPlayableException {
        Game mockedGame = EasyMock.createMock(Game.class);
        Player mockedPlayer1 = EasyMock.createMock(Player.class);
        Player mockedPlayer2 = EasyMock.createMock(Player.class);
        Player mockedPlayer3 = EasyMock.createMock(Player.class);
        Player mockedPlayer4 = EasyMock.createMock(Player.class);
        Player[] players = {mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4};
        Controller controller = new Controller(mockedGame, players, GameType.Advanced);


        // set up the variables for the test
        controller.setDevCardsEnabled(false);
        controller.setCurrentPlayer(mockedPlayer1);


        // Expectations
        // game method not called


        // assert that dev cards are enabled before the card is played
        assertFalse(controller.getDevCardsEnabled());


        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.playKnightCard();
        EasyMock.verify(mockedGame);

        // assert on expected results
        assertEquals(SuccessCode.CANNOT_PLAY_CARD, actual);
        assertFalse(controller.getDevCardsEnabled()); // dev cards still disabled
    }

    @Test
    public void testPlayKnightCardWin() throws NotEnoughResourcesException, CardNotPlayableException {
        Game mockedGame = EasyMock.createNiceMock(Game.class);
        Player mockedPlayer1 = EasyMock.createMock(Player.class);
        Player mockedPlayer2 = EasyMock.createMock(Player.class);
        Player mockedPlayer3 = EasyMock.createMock(Player.class);
        Player mockedPlayer4 = EasyMock.createMock(Player.class);
        Player[] players = {mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4};
        Controller controller = new Controller(mockedGame, players, GameType.Advanced);


        // set up the variables for the test
        controller.setCurrentPlayer(mockedPlayer1);


        // Expectations
        EasyMock.expect(mockedPlayer1.useDevCard(DevCard.KNIGHT)).andReturn(true);

        // stuff for seeing where to distribute the largest army card
        EasyMock.expect(mockedPlayer1.getNumKnightsPlayed()).andReturn(1);
        EasyMock.expect(mockedPlayer2.getNumKnightsPlayed()).andReturn(0);
        EasyMock.expect(mockedPlayer3.getNumKnightsPlayed()).andReturn(0);
        EasyMock.expect(mockedPlayer4.getNumKnightsPlayed()).andReturn(0);

        EasyMock.expect(mockedPlayer1.hasLargestArmy()).andReturn(false);
        EasyMock.expect(mockedPlayer2.hasLargestArmy()).andReturn(false);
        EasyMock.expect(mockedPlayer3.hasLargestArmy()).andReturn(false);
        EasyMock.expect(mockedPlayer4.hasLargestArmy()).andReturn(false);

        // player can play the card
        mockedPlayer1.incrementNumKnights();

        // player gained enough points to win
        EasyMock.expect(mockedPlayer1.getVictoryPoints()).andReturn(10);


        // assert that dev cards are enabled before the card is played
        assertTrue(controller.getDevCardsEnabled());


        // method call
        EasyMock.replay(mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4);
        SuccessCode actual = controller.playKnightCard();
        EasyMock.verify(mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4);

        // assert on expected results
        assertEquals(SuccessCode.GAME_WIN, actual);
        assertFalse(controller.getDevCardsEnabled()); // dev cards are now disabled becasuse we can only play one per turn
    }
}
