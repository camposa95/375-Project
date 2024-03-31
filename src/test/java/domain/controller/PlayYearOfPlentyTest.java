package domain.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import domain.game.CardNotPlayableException;
import domain.game.Game;
import domain.game.GameType;
import domain.game.NotEnoughResourcesException;
import domain.player.Player;
import domain.bank.Resource;

public class PlayYearOfPlentyTest {
    
    @Test
    public void testPlayYearOfPlentyCardSuccess() throws NotEnoughResourcesException, CardNotPlayableException {
        Game mockedGame = EasyMock.createMock(Game.class);
        Player mockedPlayer1 = EasyMock.createMock(Player.class);
        Player mockedPlayer2 = EasyMock.createMock(Player.class);
        Player mockedPlayer3 = EasyMock.createMock(Player.class);
        Player mockedPlayer4 = EasyMock.createMock(Player.class);
        Player[] players = {mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4};
        Controller controller = new Controller(mockedGame, players, GameType.Advanced);


        // set up the variables for the test
        controller.setCurrentPlayer(mockedPlayer1);
        Resource resource1 = Resource.WOOL;
        Resource resource2 = Resource.LUMBER;


        // Expectations
        mockedGame.playYearOfPlenty(mockedPlayer1, resource1, resource2);
        // game runs method successfully


        // assert that dev cards are enabled before the card is played
        assertTrue(controller.getDevCardsEnabled());


        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.playYearOfPlenty(resource1, resource2);
        EasyMock.verify(mockedGame);

        // assert on expected results
        assertEquals(SuccessCode.SUCCESS, actual);
        assertFalse(controller.getDevCardsEnabled()); // dev cards are now disabled because we can only play one per turn
    }

    @Test
    public void testPlayYearOfPlentyCardNotEnoughResources() throws NotEnoughResourcesException, CardNotPlayableException {
        Game mockedGame = EasyMock.createMock(Game.class);
        Player mockedPlayer1 = EasyMock.createMock(Player.class);
        Player mockedPlayer2 = EasyMock.createMock(Player.class);
        Player mockedPlayer3 = EasyMock.createMock(Player.class);
        Player mockedPlayer4 = EasyMock.createMock(Player.class);
        Player[] players = {mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4};
        Controller controller = new Controller(mockedGame, players, GameType.Advanced);


        // set up the variables for the test
        controller.setCurrentPlayer(mockedPlayer1);
        Resource resource1 = Resource.WOOL;
        Resource resource2 = Resource.LUMBER;


        // Expectations
        mockedGame.playYearOfPlenty(mockedPlayer1, resource1, resource2);
        EasyMock.expectLastCall().andThrow(new NotEnoughResourcesException());
        // game tells us the player failed to play because the bank does not have enough resources


        // assert that dev cards are enabled before the card is played
        assertTrue(controller.getDevCardsEnabled());


        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.playYearOfPlenty(resource1, resource2);
        EasyMock.verify(mockedGame);

        // assert on expected results
        assertEquals(SuccessCode.INSUFFICIENT_RESOURCES, actual);
        assertTrue(controller.getDevCardsEnabled()); // dev cards are still enabled because the card failed to play
    }

    @Test
    public void testPlayYearOfPlentyCardNoCard() throws NotEnoughResourcesException, CardNotPlayableException {
        Game mockedGame = EasyMock.createMock(Game.class);
        Player mockedPlayer1 = EasyMock.createMock(Player.class);
        Player mockedPlayer2 = EasyMock.createMock(Player.class);
        Player mockedPlayer3 = EasyMock.createMock(Player.class);
        Player mockedPlayer4 = EasyMock.createMock(Player.class);
        Player[] players = {mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4};
        Controller controller = new Controller(mockedGame, players, GameType.Advanced);


        // set up the variables for the test
        controller.setCurrentPlayer(mockedPlayer1);
        Resource resource1 = Resource.WOOL;
        Resource resource2 = Resource.LUMBER;


        // Expectations
        mockedGame.playYearOfPlenty(mockedPlayer1, resource1, resource2);
        EasyMock.expectLastCall().andThrow(new CardNotPlayableException());
        // game tells us the player failed to play because the bank does not have enough resources


        // assert that dev cards are enabled before the card is played
        assertTrue(controller.getDevCardsEnabled());


        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.playYearOfPlenty(resource1, resource2);
        EasyMock.verify(mockedGame);

        // assert on expected results
        assertEquals(SuccessCode.CANNOT_PLAY_CARD, actual);
        assertTrue(controller.getDevCardsEnabled()); // dev cards are still enabled because the card failed to play
    }

    @Test
    public void testPlayYearOfPlentyCardsDisabled() {
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
        Resource resource1 = Resource.BRICK;
        Resource resource2 = Resource.BRICK;


        // Expectations
        // game method not called


        // assert that dev cards are enabled before the card is played
        assertFalse(controller.getDevCardsEnabled());


        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.playYearOfPlenty(resource1, resource2);
        EasyMock.verify(mockedGame);

        // assert on expected results
        assertEquals(SuccessCode.CANNOT_PLAY_CARD, actual);
        assertFalse(controller.getDevCardsEnabled()); // dev cards still disabled
    }
}
