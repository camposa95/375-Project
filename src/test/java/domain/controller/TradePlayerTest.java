package domain.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import domain.game.Game;
import domain.game.GameType;
import domain.player.Player;
import domain.bank.Resource;

public class TradePlayerTest {

    @Test
    public void testTradePlayer_1Each() {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        Player mockedPlayer = EasyMock.createStrictMock(Player.class);
        Player mockedTradePartner = EasyMock.createStrictMock(Player.class);
        GameType gameType = GameType.Advanced;
        Resource[] giving = {Resource.BRICK};
        Resource[] receiving = {Resource.GRAIN};

        Player[] players = {mockedPlayer, mockedTradePartner};
        Controller controller = new Controller(mockedGame, players, gameType);


        // Expect
        EasyMock.expect(mockedPlayer.tradeResources(mockedTradePartner, giving, receiving)).andReturn(true);

        // method call
        EasyMock.replay(mockedPlayer);
        SuccessCode actual = controller.tradeWithPlayer(mockedTradePartner,giving,receiving);
        EasyMock.verify(mockedPlayer);
                
        assertEquals(SuccessCode.SUCCESS, actual);
    }

    @Test
    public void testTradePlayer_SamePlayer() {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        Player mockedPlayer = EasyMock.createStrictMock(Player.class);
        GameType gameType = GameType.Advanced;
        Resource[] giving = {Resource.BRICK};
        Resource[] receiving = {Resource.GRAIN};

        Player[] players = {mockedPlayer};
        Controller controller = new Controller(mockedGame, players, gameType);


        // Expect

        // method call
        EasyMock.replay(mockedPlayer);
        assertThrows(IllegalArgumentException.class,()-> controller.tradeWithPlayer(mockedPlayer,giving,receiving));
        EasyMock.verify(mockedPlayer);
                
    }

    @Test
    public void testTradePlayer_BothEmpty() {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        Player mockedPlayer = EasyMock.createStrictMock(Player.class);
        Player mockedTradePartner = EasyMock.createStrictMock(Player.class);
        GameType gameType = GameType.Advanced;
        Resource[] giving = new Resource[0];
        Resource[] receiving = new Resource[0];

        Player[] players = {mockedPlayer, mockedTradePartner};
        Controller controller = new Controller(mockedGame, players, gameType);


        // Expect

        // method call
        EasyMock.replay(mockedPlayer);
        SuccessCode actual = controller.tradeWithPlayer(mockedTradePartner,giving,receiving);
        EasyMock.verify(mockedPlayer);
                
        assertEquals(SuccessCode.INSUFFICIENT_RESOURCES, actual);
    }

    @Test
    public void testTradePlayer_NoGiving() {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        Player mockedPlayer = EasyMock.createStrictMock(Player.class);
        Player mockedTradePartner = EasyMock.createStrictMock(Player.class);
        GameType gameType = GameType.Advanced;
        Resource[] giving = new Resource[0];
        Resource[] receiving = {Resource.GRAIN};

        Player[] players = {mockedPlayer, mockedTradePartner};
        Controller controller = new Controller(mockedGame, players, gameType);


        // Expect

        // method call
        EasyMock.replay(mockedPlayer);
        SuccessCode actual = controller.tradeWithPlayer(mockedTradePartner,giving,receiving);
        EasyMock.verify(mockedPlayer);
                
        assertEquals(SuccessCode.INSUFFICIENT_RESOURCES, actual);
    }

    @Test
    public void testTradePlayer_NoReceiving() {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        Player mockedPlayer = EasyMock.createStrictMock(Player.class);
        Player mockedTradePartner = EasyMock.createStrictMock(Player.class);
        GameType gameType = GameType.Advanced;
        Resource[] receiving = new Resource[0];
        Resource[] giving = {Resource.GRAIN};

        Player[] players = {mockedPlayer, mockedTradePartner};
        Controller controller = new Controller(mockedGame, players, gameType);


        // Expect

        // method call
        EasyMock.replay(mockedPlayer);
        SuccessCode actual = controller.tradeWithPlayer(mockedTradePartner,giving,receiving);
        EasyMock.verify(mockedPlayer);
                
        assertEquals(SuccessCode.INSUFFICIENT_RESOURCES, actual);
    }

    @Test
    public void testTradePlayer_ManyGiving_NotEnough() {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        Player mockedPlayer = EasyMock.createStrictMock(Player.class);
        Player mockedTradePartner = EasyMock.createStrictMock(Player.class);
        GameType gameType = GameType.Advanced;
        Resource[] giving = {Resource.BRICK, Resource.GRAIN, Resource.ORE};
        Resource[] receiving = {Resource.GRAIN};

        Player[] players = {mockedPlayer, mockedTradePartner};
        Controller controller = new Controller(mockedGame, players, gameType);

        // Expect
        EasyMock.expect(mockedPlayer.tradeResources(mockedTradePartner, giving, receiving)).andReturn(false);

        // method call
        EasyMock.replay(mockedPlayer);
        SuccessCode actual = controller.tradeWithPlayer(mockedTradePartner,giving,receiving);
        EasyMock.verify(mockedPlayer);
                
        assertEquals(SuccessCode.UNDEFINED, actual);
    }

    @Test
    public void testTradePlayer_ManyReceiving_NotEnough() {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        Player mockedPlayer = EasyMock.createStrictMock(Player.class);
        Player mockedTradePartner = EasyMock.createStrictMock(Player.class);
        GameType gameType = GameType.Advanced;
        Resource[] receiving = {Resource.BRICK, Resource.GRAIN, Resource.ORE};
        Resource[] giving = {Resource.GRAIN};

        Player[] players = {mockedPlayer, mockedTradePartner};
        Controller controller = new Controller(mockedGame, players, gameType);

        // Expect
        EasyMock.expect(mockedPlayer.tradeResources(mockedTradePartner, giving, receiving)).andReturn(false);

        // method call
        EasyMock.replay(mockedPlayer);
        SuccessCode actual = controller.tradeWithPlayer(mockedTradePartner,giving,receiving);
        EasyMock.verify(mockedPlayer);
                
        assertEquals(SuccessCode.UNDEFINED, actual);
    }
}
