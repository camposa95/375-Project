package domain.controller;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.game.Game;
import domain.game.GameType;
import domain.player.Player;

import static org.junit.jupiter.api.Assertions.*;

public class EndTurnTest {

    Game mockedGame;
    Player mockedPlayer1, mockedPlayer2, mockedPlayer3;
    Player[] players;
    Controller controller;

    @BeforeEach
    public void setup() {
        mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        mockedPlayer2 = EasyMock.createStrictMock(Player.class);
        mockedPlayer3 = EasyMock.createStrictMock(Player.class);
        players = new Player[]{mockedPlayer1, mockedPlayer2, mockedPlayer3};

        // setting up the controller
        controller = new Controller(mockedGame, players, gameType);
        controller.setState(GameState.DEFAULT);
        controller.setCurrentPlayer(mockedPlayer1);
        controller.setDevCardsEnabled(false);
    }
    
    @Test
    public void testEndTurnSuccess() {
        // expect this to be called
        mockedPlayer1.addBoughtCardsToHand();

        // next player to go does not have enough points to win
        EasyMock.expect(mockedPlayer2.getVictoryPoints()).andReturn(9);

        EasyMock.replay(mockedPlayer1, mockedPlayer2);
        // method call
        SuccessCode actualCode = controller.endTurn();
        EasyMock.verify(mockedPlayer1, mockedPlayer2);

        // assert on results
        assertEquals(SuccessCode.SUCCESS, actualCode);
        assertEquals(mockedPlayer2, controller.getCurrentPlayer());
        assertEquals(GameState.TURN_START, controller.getState());
        assertTrue(controller.getDevCardsEnabled());
    }

    @Test
    public void testEndTurnFailure() {
        controller.setState(GameState.BUILD_ROAD); // non default
        // method call
        SuccessCode actualCode = controller.endTurn();

        // assert on results
        assertEquals(SuccessCode.UNDEFINED, actualCode);
        assertEquals(mockedPlayer1, controller.getCurrentPlayer());
        assertEquals(GameState.BUILD_ROAD, controller.getState());
        assertFalse(controller.getDevCardsEnabled());
    }

    @Test
    public void testEndTurnNextPlayerWin() {
        // expect this to be called
        mockedPlayer1.addBoughtCardsToHand();
        // next player already has enough points to win
        EasyMock.expect(mockedPlayer2.getVictoryPoints()).andReturn(10);


        EasyMock.replay(mockedPlayer1, mockedPlayer2);

        // method call
        SuccessCode actualCode = controller.endTurn();
        EasyMock.verify(mockedPlayer1, mockedPlayer2);

        // assert on results
        assertEquals(SuccessCode.GAME_WIN, actualCode);
        assertEquals(mockedPlayer2, controller.getCurrentPlayer());
        assertEquals(GameState.TURN_START, controller.getState());
        assertTrue(controller.getDevCardsEnabled());
    }
}
