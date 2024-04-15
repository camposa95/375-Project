package domain.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.game.Game;
import domain.game.GameType;
import domain.player.Player;

public class IncrementPlayerTest {

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
    public void testIncrementPlayerFromStart() {
        EasyMock.replay(mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4);

        assertEquals(mockedPlayer1, controller.getCurrentPlayer());
        controller.incrementPlayer();
        assertEquals(mockedPlayer2, controller.getCurrentPlayer());
    }

    @Test
    public void testIncrementPlayer2() {
        EasyMock.replay(mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4);

        assertEquals(mockedPlayer1, controller.getCurrentPlayer());
        controller.incrementPlayer();
        controller.incrementPlayer();
        assertEquals(mockedPlayer3, controller.getCurrentPlayer());
    }

    @Test
    public void testIncrementPlayer3() {
        EasyMock.replay(mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4);

        assertEquals(mockedPlayer1, controller.getCurrentPlayer());
        controller.incrementPlayer();
        controller.incrementPlayer();
        controller.incrementPlayer();
        assertEquals(mockedPlayer4, controller.getCurrentPlayer());
    }

    @Test
    public void testIncrementPlayerWrapAround() {
        EasyMock.replay(mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4);

        assertEquals(mockedPlayer1, controller.getCurrentPlayer());
        controller.incrementPlayer();
        controller.incrementPlayer();
        controller.incrementPlayer();
        controller.incrementPlayer();
        assertEquals(mockedPlayer1, controller.getCurrentPlayer());
    }
}
