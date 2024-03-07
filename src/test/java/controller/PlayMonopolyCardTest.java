package controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import gamedatastructures.CardNotPlayableException;
import gamedatastructures.Game;
import gamedatastructures.GameType;
import gamedatastructures.Player;
import gamedatastructures.Resource;

public class PlayMonopolyCardTest {

    @Test
    public void testPlayMonopolyCardSuccess() throws CardNotPlayableException {
        Game mockedGame = EasyMock.createMock(Game.class);
        Player mockedPlayer1 = EasyMock.createMock(Player.class);
        Player mockedPlayer2 = EasyMock.createMock(Player.class);
        Player mockedPlayer3 = EasyMock.createMock(Player.class);
        Player mockedPlayer4 = EasyMock.createMock(Player.class);
        Player[] players = {mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4};
        Controller controller = new Controller(mockedGame, players, GameType.Advanced);


        // set up the variables for the test
        controller.setCurrentPlayer(mockedPlayer1);
        Player[] playersToRob = {mockedPlayer2, mockedPlayer3, mockedPlayer4};
        Resource resourceToRob = Resource.BRICK;


        // Expectations
        mockedGame.playMonopoly(mockedPlayer1, playersToRob, resourceToRob);
        // game runs method successfully


        // assert that dev cards are enabled before the card is played
        assertTrue(controller.getDevCardsEnabled());


        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.playMonopolyCard(resourceToRob);
        EasyMock.verify(mockedGame);

        // assert on expected results
        assertEquals(SuccessCode.SUCCESS, actual);
        assertFalse(controller.getDevCardsEnabled()); // dev cards are now disabled becasuse we can only play one per turn
    }

    @Test
    public void testPlayMonopolyCardNoCard() throws CardNotPlayableException {
        Game mockedGame = EasyMock.createMock(Game.class);
        Player mockedPlayer1 = EasyMock.createMock(Player.class);
        Player mockedPlayer2 = EasyMock.createMock(Player.class);
        Player mockedPlayer3 = EasyMock.createMock(Player.class);
        Player mockedPlayer4 = EasyMock.createMock(Player.class);
        Player[] players = {mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4};
        Controller controller = new Controller(mockedGame, players, GameType.Advanced);


        // set up the variables for the test
        controller.setCurrentPlayer(mockedPlayer1);
        Player[] playersToRob = {mockedPlayer2, mockedPlayer3, mockedPlayer4};
        Resource resourceToRob = Resource.BRICK;


        // Expectations
        mockedGame.playMonopoly(mockedPlayer1, playersToRob, resourceToRob);
        EasyMock.expectLastCall().andThrow(new CardNotPlayableException());
        // game tells us the player does not have the monopoly card playable


        // assert that dev cards are enabled before the card is played
        assertTrue(controller.getDevCardsEnabled());


        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.playMonopolyCard(resourceToRob);
        EasyMock.verify(mockedGame);

        // assert on expected results
        assertEquals(SuccessCode.CANNOT_PLAY_CARD, actual);
        assertTrue(controller.getDevCardsEnabled()); // dev cards are still enabled because the card failed to play
    }

    @Test
    public void testPlayMonopolyCardCardsDisabled() throws CardNotPlayableException {
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
        Resource resourceToRob = Resource.BRICK;


        // Expectations
        // game method not called


        // assert that dev cards are enabled before the card is played
        assertFalse(controller.getDevCardsEnabled());


        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.playMonopolyCard(resourceToRob);
        EasyMock.verify(mockedGame);

        // assert on expected results
        assertEquals(SuccessCode.CANNOT_PLAY_CARD, actual);
        assertFalse(controller.getDevCardsEnabled()); // dev cards still disabled
    }
}
