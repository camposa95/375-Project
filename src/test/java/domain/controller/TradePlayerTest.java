package domain.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.game.Game;
import domain.game.GameType;
import domain.player.Player;
import domain.bank.Resource;

public class TradePlayerTest {

    Game mockedGame;
    Player mockedPlayer, mockedTradePartner;
    Player[] players;
    Controller controller;

    @BeforeEach
    public void setupMocks() {
        mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        mockedPlayer = EasyMock.createStrictMock(Player.class);
        mockedTradePartner = EasyMock.createStrictMock(Player.class);

        players = new Player[]{mockedPlayer, mockedTradePartner};

        controller = new Controller(mockedGame, players, gameType);
    }

    @Test
    public void testTradePlayer_1Each() {
        Resource[] giving = {Resource.BRICK};
        Resource[] receiving = {Resource.GRAIN};

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
        Resource[] giving = {Resource.BRICK};
        Resource[] receiving = {Resource.GRAIN};

        // Expect

        // method call
        EasyMock.replay(mockedPlayer);
        assertThrows(IllegalArgumentException.class,()-> controller.tradeWithPlayer(mockedPlayer,giving,receiving));
        EasyMock.verify(mockedPlayer);
                
    }

    @Test
    public void testTradePlayer_BothEmpty() {
        Resource[] giving = new Resource[0];
        Resource[] receiving = new Resource[0];

        // Expect

        // method call
        EasyMock.replay(mockedPlayer);
        SuccessCode actual = controller.tradeWithPlayer(mockedTradePartner,giving,receiving);
        EasyMock.verify(mockedPlayer);
                
        assertEquals(SuccessCode.INSUFFICIENT_RESOURCES, actual);
    }

    @Test
    public void testTradePlayer_NoGiving() {
        Resource[] giving = new Resource[0];
        Resource[] receiving = {Resource.GRAIN};

        // Expect

        // method call
        EasyMock.replay(mockedPlayer);
        SuccessCode actual = controller.tradeWithPlayer(mockedTradePartner,giving,receiving);
        EasyMock.verify(mockedPlayer);
                
        assertEquals(SuccessCode.INSUFFICIENT_RESOURCES, actual);
    }

    @Test
    public void testTradePlayer_NoReceiving() {
        Resource[] receiving = new Resource[0];
        Resource[] giving = {Resource.GRAIN};

        // Expect

        // method call
        EasyMock.replay(mockedPlayer);
        SuccessCode actual = controller.tradeWithPlayer(mockedTradePartner,giving,receiving);
        EasyMock.verify(mockedPlayer);
                
        assertEquals(SuccessCode.INSUFFICIENT_RESOURCES, actual);
    }

    @Test
    public void testTradePlayer_ManyGiving_NotEnough() {
        Resource[] giving = {Resource.BRICK, Resource.GRAIN, Resource.ORE};
        Resource[] receiving = {Resource.GRAIN};

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
        Resource[] receiving = {Resource.BRICK, Resource.GRAIN, Resource.ORE};
        Resource[] giving = {Resource.GRAIN};

        // Expect
        EasyMock.expect(mockedPlayer.tradeResources(mockedTradePartner, giving, receiving)).andReturn(false);

        // method call
        EasyMock.replay(mockedPlayer);
        SuccessCode actual = controller.tradeWithPlayer(mockedTradePartner,giving,receiving);
        EasyMock.verify(mockedPlayer);
                
        assertEquals(SuccessCode.UNDEFINED, actual);
    }
}
