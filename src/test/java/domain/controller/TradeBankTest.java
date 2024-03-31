package domain.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import domain.game.Game;
import domain.game.GameType;
import domain.player.Player;
import domain.bank.Resource;

public class TradeBankTest {

    @Test
    public void testTradeBank_Valid() {
        
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        Player mockedPlayer = EasyMock.createStrictMock(Player.class);
        GameType gameType = GameType.Advanced;
        Resource giving = Resource.BRICK;
        Resource receiving = Resource.GRAIN;

        Player[] players = {mockedPlayer};
        Controller controller = new Controller(mockedGame, players, gameType);


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
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        Player mockedPlayer = EasyMock.createStrictMock(Player.class);
        GameType gameType = GameType.Advanced;
        Resource giving = Resource.BRICK;
        Resource receiving = Resource.GRAIN;

        Player[] players = {mockedPlayer};
        Controller controller = new Controller(mockedGame, players, gameType);


        // Expect
        EasyMock.expect(mockedPlayer.tradeWithBank(giving, receiving)).andReturn(false);

        // method call
        EasyMock.replay(mockedPlayer);
        SuccessCode actual = controller.tradeWithBank(giving,receiving);
        EasyMock.verify(mockedPlayer);
                
        assertEquals(SuccessCode.INSUFFICIENT_RESOURCES, actual);
    }
}
