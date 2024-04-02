package domain.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import domain.game.Game;
import domain.game.GameType;
import domain.player.Player;

public class IncrementPlayerTest {

    @Test
    public void testIncrementPlayerFromStart() {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        Player mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer2 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer3 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer4 = EasyMock.createStrictMock(Player.class);

        Player[] players = { mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4 };

        Controller controller = new Controller(mockedGame, players, gameType);

        EasyMock.replay(mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4);

        assertEquals(mockedPlayer1, controller.getCurrentPlayer());
        controller.incrementPlayer();
        assertEquals(mockedPlayer2, controller.getCurrentPlayer());
    }

    @Test
    public void testIncrementPlayer2() {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        Player mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer2 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer3 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer4 = EasyMock.createStrictMock(Player.class);

        Player[] players = { mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4 };

        Controller controller = new Controller(mockedGame, players, gameType);

        EasyMock.replay(mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4);

        assertEquals(mockedPlayer1, controller.getCurrentPlayer());
        controller.incrementPlayer();
        controller.incrementPlayer();
        assertEquals(mockedPlayer3, controller.getCurrentPlayer());
    }

    @Test
    public void testIncrementPlayer3() {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        Player mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer2 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer3 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer4 = EasyMock.createStrictMock(Player.class);

        Player[] players = { mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4 };

        Controller controller = new Controller(mockedGame, players, gameType);

        EasyMock.replay(mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4);

        assertEquals(mockedPlayer1, controller.getCurrentPlayer());
        controller.incrementPlayer();
        controller.incrementPlayer();
        controller.incrementPlayer();
        assertEquals(mockedPlayer4, controller.getCurrentPlayer());
    }

    @Test
    public void testIncrementPlayerWrapAround() {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        Player mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer2 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer3 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer4 = EasyMock.createStrictMock(Player.class);

        Player[] players = { mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4 };

        Controller controller = new Controller(mockedGame, players, gameType);

        EasyMock.replay(mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4);

        assertEquals(mockedPlayer1, controller.getCurrentPlayer());
        controller.incrementPlayer();
        controller.incrementPlayer();
        controller.incrementPlayer();
        controller.incrementPlayer();
        assertEquals(mockedPlayer1, controller.getCurrentPlayer());
    }

    @Test
    public void testIncrementPlayerWrapAroundWith3players() {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        Player mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer2 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer3 = EasyMock.createStrictMock(Player.class);

        Player[] players = {mockedPlayer1, mockedPlayer2, mockedPlayer3};

        Controller controller = new Controller(mockedGame, players, gameType);

        EasyMock.replay(mockedPlayer1, mockedPlayer2, mockedPlayer3);

        assertEquals(mockedPlayer1, controller.getCurrentPlayer());
        controller.incrementPlayer();
        controller.incrementPlayer();
        controller.incrementPlayer();
        assertEquals(mockedPlayer1, controller.getCurrentPlayer());
    }

    @Test
    public void testIncrementPlayerWrapAroundWith1players() {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        Player mockedPlayer1 = EasyMock.createStrictMock(Player.class);

        Player[] players = {mockedPlayer1};

        Controller controller = new Controller(mockedGame, players, gameType);

        EasyMock.replay(mockedPlayer1);

        assertEquals(mockedPlayer1, controller.getCurrentPlayer());
        controller.incrementPlayer();
        assertEquals(mockedPlayer1, controller.getCurrentPlayer());
    }
}
