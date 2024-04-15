package domain.player;

import domain.bank.Bank;
import domain.bank.Resource;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TradeWithBankTest {

    Bank bank;
    Player player;
    Hand mockedPlayer1Hand;

    @BeforeEach
    public void setup() {
        bank = new Bank();
        player = new Player(1, bank);
        mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor().createMock();
        player.hand = mockedPlayer1Hand;
    }

    @Test
    public void testTrade_withBank_validTrade_returnTrue() {
        // move resources from bank to player
        mockedPlayer1Hand.addResource(Resource.LUMBER, 4);
        bank.removeResource(Resource.LUMBER, 4);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.tradeWithBank(Resource.LUMBER, Resource.BRICK);

        assertTrue(success);
        assertEquals(1, mockedPlayer1Hand.getResourceCount(Resource.BRICK));
        assertEquals(0, mockedPlayer1Hand.getResourceCount(Resource.LUMBER));
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testTrade_withBank_withBrickPort_validTrade_returnTrue() {
        // move resources from bank to player
        mockedPlayer1Hand.addResource(Resource.BRICK, 2);
        bank.removeResource(Resource.BRICK, 2);

        player.addTradeBoost(Resource.BRICK);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.tradeWithBank(Resource.BRICK, Resource.WOOL);

        assertTrue(success);
        assertEquals(0, mockedPlayer1Hand.getResourceCount(Resource.BRICK));
        assertEquals(1, mockedPlayer1Hand.getResourceCount(Resource.WOOL));
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testTrade_withBank_withBetterPort_validTrade_returnTrue() {
        // move resources from bank to player
        mockedPlayer1Hand.addResource(Resource.BRICK, 3);
        bank.removeResource(Resource.BRICK, 3);
        player.addTradeBoost(Resource.ANY);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.tradeWithBank(Resource.BRICK, Resource.WOOL);

        assertTrue(success);
        assertEquals(0, mockedPlayer1Hand.getResourceCount(Resource.BRICK));
        assertEquals(1, mockedPlayer1Hand.getResourceCount(Resource.WOOL));
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testTrade_withEmptyBank_returnFalse() {
        // move resources from bank to player
        mockedPlayer1Hand.addResource(Resource.BRICK, 4);
        bank.removeResource(Resource.BRICK, 4);

        // empty bank
        bank.removeResource(Resource.WOOL, 19);
        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.tradeWithBank(Resource.BRICK, Resource.WOOL);

        assertFalse(success);
        assertEquals(4, mockedPlayer1Hand.getResourceCount(Resource.BRICK));
        assertEquals(0, mockedPlayer1Hand.getResourceCount(Resource.WOOL));
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testTrade_withBank_notEnoughCards_returnFalse() {
        // move resources from bank to player
        mockedPlayer1Hand.addResource(Resource.WOOL, 1);
        bank.removeResource(Resource.WOOL, 1);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.tradeWithBank(Resource.WOOL, Resource.GRAIN);

        assertFalse(success);
        assertEquals(1, mockedPlayer1Hand.getResourceCount(Resource.WOOL));
        assertEquals(0, mockedPlayer1Hand.getResourceCount(Resource.GRAIN));
        EasyMock.verify(mockedPlayer1Hand);
    }
}
