package domain.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.game.Game;
import domain.game.GameType;
import domain.player.Player;
import domain.bank.Resource;

public class TradeBankTest {

    Game mockedGame;
    Player mockedPlayer;
    Player[] players;
    Controller controller;

    @BeforeEach
    public void setupMocks() {
        mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        mockedPlayer = EasyMock.createStrictMock(Player.class);

        players = new Player[]{mockedPlayer};

        controller = new Controller(mockedGame, players, gameType);
    }

    @Test
    public void testTradeBank_Valid() {
        Resource giving = Resource.BRICK;
        Resource receiving = Resource.GRAIN;

        // Expect
        EasyMock.expect(mockedPlayer.tradeWithBank(giving, receiving)).andReturn(true);

        // method call
        EasyMock.replay(mockedPlayer);
        SuccessCode actual = controller.tradeWithBank(giving,receiving);
        EasyMock.verify(mockedPlayer);
                
        assertEquals(SuccessCode.SUCCESS, actual);
    }

    @Test
    public void testTradeBank_Invalid() {
        Resource giving = Resource.BRICK;
        Resource receiving = Resource.GRAIN;

        // Expect
        EasyMock.expect(mockedPlayer.tradeWithBank(giving, receiving)).andReturn(false);

        // method call
        EasyMock.replay(mockedPlayer);
        SuccessCode actual = controller.tradeWithBank(giving,receiving);
        EasyMock.verify(mockedPlayer);
                
        assertEquals(SuccessCode.INSUFFICIENT_RESOURCES, actual);
    }
}
