package domain.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.devcarddeck.DevCard;
import domain.game.Game;
import domain.game.GameType;
import domain.player.Player;

public class PlayKnightCardTest {

    Game mockedGame;
    Player mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4;
    Player[] players;
    Controller controller;

    @BeforeEach
    public void setupMocks() {
        mockedGame = EasyMock.createMock(Game.class);
        GameType gameType = GameType.Advanced;

        mockedPlayer1 = EasyMock.createMock(Player.class);
        mockedPlayer2 = EasyMock.createMock(Player.class);
        mockedPlayer3 = EasyMock.createMock(Player.class);
        mockedPlayer4 = EasyMock.createMock(Player.class);

        players = new Player[]{mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4};

        controller = new Controller(mockedGame, players, gameType);
    }

    @Test
    public void testPlayKnightCardSuccess() {
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
        assertFalse(controller.getDevCardsEnabled()); // dev cards are now disabled because we can only play one per turn
    }

    @Test
    public void testPlayKnightCardNoCard() {
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
        assertTrue(controller.getDevCardsEnabled()); // dev cards are now disabled because we can only play one per turn
    }

    @Test
    public void testPlayKnightCardsDisabled() {
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
    public void testPlayKnightCardWin() {
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
        assertFalse(controller.getDevCardsEnabled()); // dev cards are now disabled because we can only play one per turn
    }
}
