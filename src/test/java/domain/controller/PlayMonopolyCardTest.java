package domain.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.game.CardNotPlayableException;
import domain.game.Game;
import domain.game.GameType;
import domain.player.Player;
import domain.bank.Resource;

public class PlayMonopolyCardTest {

    Game mockedGame;
    Player mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4;
    Player[] players;
    Controller controller;

    @BeforeEach
    public void setupMocks() {
        mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        mockedPlayer2 = EasyMock.createStrictMock(Player.class);
        mockedPlayer3 = EasyMock.createStrictMock(Player.class);
        mockedPlayer4 = EasyMock.createStrictMock(Player.class);

        players = new Player[]{mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4};

        controller = new Controller(mockedGame, players, gameType);
    }

    @Test
    public void testPlayMonopolyCardSuccess() throws CardNotPlayableException {
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
    public void testPlayMonopolyCardCardsDisabled() {
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
