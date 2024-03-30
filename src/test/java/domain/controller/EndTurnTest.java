package domain.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import domain.controller.Controller;
import domain.controller.GameState;
import domain.controller.SuccessCode;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import domain.game.Game;
import domain.game.GameType;
import domain.player.Player;

public class EndTurnTest {
    
    @Test
    public void testEndTurnSuccess() {

        Game mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        Player mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer2 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer3 = EasyMock.createStrictMock(Player.class);
        Player[] players = {mockedPlayer1, mockedPlayer2, mockedPlayer3};

        // setting up the controller
        Controller controller = new Controller(mockedGame, players, gameType);
        controller.setState(GameState.DEFAULT);
        controller.setCurrentPlayer(mockedPlayer1);
        controller.setDevCardsEnabled(false);

        // expect this to be called
        mockedPlayer1.addboughtCardsToHand();

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
        assertEquals(true, controller.getDevCardsEnabled());
    }

    @Test
    public void testEndTurnFailure() {

        Game mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        Player mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer2 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer3 = EasyMock.createStrictMock(Player.class);
        Player[] players = {mockedPlayer1, mockedPlayer2, mockedPlayer3};

        // setting up the controller
        Controller controller = new Controller(mockedGame, players, gameType);
        controller.setState(GameState.BUILD_ROAD);
        controller.setCurrentPlayer(mockedPlayer1);
        controller.setDevCardsEnabled(false);

        // method call
        SuccessCode actualCode = controller.endTurn();

        // assert on results
        assertEquals(SuccessCode.UNDEFINED, actualCode);
        assertEquals(mockedPlayer1, controller.getCurrentPlayer());
        assertEquals(GameState.BUILD_ROAD, controller.getState());
        assertEquals(false, controller.getDevCardsEnabled());
    }

    @Test
    public void testEndTurnNextPlayerWin() {

        Game mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        Player mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer2 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer3 = EasyMock.createStrictMock(Player.class);
        Player[] players = {mockedPlayer1, mockedPlayer2, mockedPlayer3};

        // setting up the controller
        Controller controller = new Controller(mockedGame, players, gameType);
        controller.setState(GameState.DEFAULT);
        controller.setCurrentPlayer(mockedPlayer1);
        controller.setDevCardsEnabled(false);

        // expect this to be called
        mockedPlayer1.addboughtCardsToHand();
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
        assertEquals(true, controller.getDevCardsEnabled());
    }
}
