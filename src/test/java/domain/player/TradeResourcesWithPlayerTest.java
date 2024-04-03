package domain.player;

import domain.bank.Resource;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TradeResourcesWithPlayerTest {

    Player player1, player2;
    Hand mockedPlayer1Hand, mockedPlayer2Hand;
    
    @BeforeEach
    public void setup() {
        player1 = new Player(1);
        player2 = new Player(2);

        mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor().createMock();
        mockedPlayer2Hand = EasyMock.createMockBuilder(Hand.class).withConstructor().createMock();

        player1.hand = mockedPlayer1Hand;
        player2.hand = mockedPlayer2Hand;
    }
    
    @Test
    public void testTradeResources_withNullPlayer_throwsNullPointerException() {
        Player player2 = null;

        String expectedMessage = "Other player cannot be null";

        Exception thrown = assertThrows(IllegalArgumentException.class, () -> {
            player1.tradeResources(player2, new Resource[] {}, new Resource[] {});
        });
        assertEquals(0, player1.hand.getResourceCount());
        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    public void testTradeResources_withPlayer_andEmptyCollections_throwsIllegalArgumentException() {
        Resource[] player1Hand = new Resource[] {
                Resource.LUMBER
        };

        player1.hand.addResources(player1Hand);

        String expectedMessage = "Traded resources must not be empty";

        Exception thrown = assertThrows(IllegalArgumentException.class, () -> {
            player1.tradeResources(player2, player1Hand, new Resource[] {});
        });
        assertEquals(1, player1.hand.getResourceCount());
        assertEquals(0, player2.hand.getResourceCount());
        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    public void testTradeResources_withPlayer_andPlayer2EmptyCollections_throwsIllegalArgumentException() {
        Resource[] player2Hand = new Resource[] {
                Resource.LUMBER
        };

        player2.hand.addResources(player2Hand);

        String expectedMessage = "Traded resources must not be empty";

        Exception thrown = assertThrows(IllegalArgumentException.class, () -> {
            player1.tradeResources(player2, new Resource[] {}, player2Hand);
        });
        assertEquals(0, player1.hand.getResourceCount());
        assertEquals(1, player2.hand.getResourceCount());
        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    public void testTradeResources_withPlayer_withOneResource_returnTrue() {
        mockedPlayer1Hand.addResource(Resource.LUMBER, 1);
        mockedPlayer2Hand.addResource(Resource.BRICK, 1);

        EasyMock.replay(mockedPlayer1Hand, mockedPlayer2Hand);

        boolean success = player1.tradeResources(player2,
                new Resource[] { Resource.LUMBER },
                new Resource[] { Resource.BRICK });

        assertTrue(success);
        assertEquals(1, mockedPlayer1Hand.getResourceCount());
        assertEquals(1, mockedPlayer2Hand.getResourceCount());
        assertEquals(1, mockedPlayer1Hand.getResourceCount(Resource.BRICK));
        assertEquals(1, mockedPlayer2Hand.getResourceCount(Resource.LUMBER));
        EasyMock.verify(mockedPlayer1Hand, mockedPlayer2Hand);
    }

    @Test
    public void testTradeResources_withPlayer_withTwoResources_returnTrue() {
        mockedPlayer1Hand.addResource(Resource.LUMBER, 1);
        mockedPlayer1Hand.addResource(Resource.BRICK, 1);
        mockedPlayer2Hand.addResource(Resource.GRAIN, 1);

        EasyMock.replay(mockedPlayer1Hand, mockedPlayer2Hand);

        boolean success = player1.tradeResources(player2,
                new Resource[] { Resource.LUMBER, Resource.BRICK },
                new Resource[] { Resource.GRAIN });

        assertTrue(success);
        assertEquals(1, mockedPlayer1Hand.getResourceCount());
        assertEquals(2, mockedPlayer2Hand.getResourceCount());
        assertEquals(1, mockedPlayer1Hand.getResourceCount(Resource.GRAIN));
        assertEquals(1, mockedPlayer2Hand.getResourceCount(Resource.BRICK));
        assertEquals(1, mockedPlayer2Hand.getResourceCount(Resource.LUMBER));
        EasyMock.verify(mockedPlayer1Hand, mockedPlayer2Hand);
    }

    @Test
    public void testTradeResources_withPlayer_withDuplicateResources_returnTrue() {
        mockedPlayer1Hand.addResource(Resource.LUMBER, 2);
        mockedPlayer2Hand.addResource(Resource.BRICK, 1);

        EasyMock.replay(mockedPlayer1Hand, mockedPlayer2Hand);

        boolean success = player1.tradeResources(player2,
                new Resource[] { Resource.LUMBER, Resource.LUMBER },
                new Resource[] { Resource.BRICK });

        assertTrue(success);
        assertEquals(1, mockedPlayer1Hand.getResourceCount());
        assertEquals(2, mockedPlayer2Hand.getResourceCount());
        assertEquals(1, mockedPlayer1Hand.getResourceCount(Resource.BRICK));
        assertEquals(2, mockedPlayer2Hand.getResourceCount(Resource.LUMBER));
        EasyMock.verify(mockedPlayer1Hand, mockedPlayer2Hand);
    }

    @Test
    public void testTradeResources_withPlayer_withTooManyResources_returnFalse() {
        mockedPlayer1Hand.addResource(Resource.LUMBER, 1);
        mockedPlayer2Hand.addResource(Resource.BRICK, 1);

        EasyMock.replay(mockedPlayer1Hand, mockedPlayer2Hand);

        boolean success = player1.tradeResources(player2,
                new Resource[] { Resource.LUMBER, Resource.LUMBER },
                new Resource[] { Resource.BRICK });

        assertFalse(success);
        assertEquals(1, mockedPlayer1Hand.getResourceCount());
        assertEquals(1, mockedPlayer2Hand.getResourceCount());
        assertEquals(1, mockedPlayer1Hand.getResourceCount(Resource.LUMBER));
        assertEquals(1, mockedPlayer2Hand.getResourceCount(Resource.BRICK));
        EasyMock.verify(mockedPlayer1Hand, mockedPlayer2Hand);
    }

    @Test
    public void testTradeResources_withPlayer_withTooManyResourcesPlayer2_returnFalse() {
        mockedPlayer1Hand.addResource(Resource.LUMBER, 1);
        mockedPlayer2Hand.addResource(Resource.BRICK, 1);

        EasyMock.replay(mockedPlayer1Hand, mockedPlayer2Hand);

        boolean success = player1.tradeResources(player2,
                new Resource[] { Resource.LUMBER },
                new Resource[] { Resource.BRICK, Resource.BRICK });

        assertFalse(success);
        assertEquals(1, mockedPlayer1Hand.getResourceCount());
        assertEquals(1, mockedPlayer2Hand.getResourceCount());
        assertEquals(1, mockedPlayer1Hand.getResourceCount(Resource.LUMBER));
        assertEquals(1, mockedPlayer2Hand.getResourceCount(Resource.BRICK));
        EasyMock.verify(mockedPlayer1Hand, mockedPlayer2Hand);
    }
}
