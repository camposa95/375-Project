package domain.player;

import domain.bank.Bank;
import domain.bank.Resource;
import domain.devcarddeck.DevCard;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static domain.bank.Resource.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PurchaseDevCardTest {

    private static final Resource[] devCardResources = {WOOL, GRAIN, ORE};
    
    Bank bank;
    Player player;
    Hand mockedPlayer1Hand;
    
    @BeforeEach
    public void setup() {
        bank = new Bank();
        player = new Player(1, bank);

        mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor()
                .addMockedMethod("removeResources").addMockedMethod("addDevelopmentCard").createMock();
        player.hand = mockedPlayer1Hand;
    }

    @Test
    public void testPurchaseDevCard_nullCard_throwIllegalArgumentException() {
        String expectedMessage = "Cannot purchase null Development Card";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            player.purchaseDevCard(null);
        });

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testPurchaseDevCard_Knight_canAfford_returnTrue() {
        DevCard knight = DevCard.KNIGHT;
        mockedPlayer1Hand.addResources(devCardResources);
        bank.removeResources(devCardResources);

        EasyMock.expect(mockedPlayer1Hand.removeResources(devCardResources)).andReturn(true);
        EasyMock.expect(mockedPlayer1Hand.addDevelopmentCard(knight)).andReturn(true);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.purchaseDevCard(knight);

        assertTrue(success);
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testPurchaseDevCard_Knight_cannotAfford_returnFalse() {
        DevCard knight = DevCard.KNIGHT;

        EasyMock.expect(mockedPlayer1Hand.removeResources(devCardResources)).andReturn(false);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.purchaseDevCard(knight);

        assertFalse(success);
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testPurchaseDevCard_YearOfPlenty_canAfford_returnTrue() {
        DevCard yop = DevCard.PLENTY;
        mockedPlayer1Hand.addResources(devCardResources);
        bank.removeResources(devCardResources);

        EasyMock.expect(mockedPlayer1Hand.removeResources(devCardResources)).andReturn(true);
        EasyMock.expect(mockedPlayer1Hand.addDevelopmentCard(yop)).andReturn(true);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.purchaseDevCard(yop);

        assertTrue(success);
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testPurchaseDevCard_YearOfPlenty_cannotAfford_returnFalse() {
        DevCard yop = DevCard.PLENTY;

        EasyMock.expect(mockedPlayer1Hand.removeResources(devCardResources)).andReturn(false);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.purchaseDevCard(yop);

        assertFalse(success);
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testPurchaseDevCard_RoadBuilding_canAfford_returnTrue() {
        DevCard road = DevCard.ROAD;
        mockedPlayer1Hand.addResources(devCardResources);
        bank.removeResources(devCardResources);

        EasyMock.expect(mockedPlayer1Hand.removeResources(devCardResources)).andReturn(true);
        EasyMock.expect(mockedPlayer1Hand.addDevelopmentCard(road)).andReturn(true);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.purchaseDevCard(road);

        assertTrue(success);
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testPurchaseDevCard_RoadBuilding_cannotAfford_returnFalse() {
        DevCard road = DevCard.ROAD;

        EasyMock.expect(mockedPlayer1Hand.removeResources(devCardResources)).andReturn(false);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.purchaseDevCard(road);

        assertFalse(success);
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testPurchaseDevCard_Monopoly_canAfford_returnTrue() {
        DevCard monopoly = DevCard.MONOPOLY;
        mockedPlayer1Hand.addResources(devCardResources);
        bank.removeResources(devCardResources);

        EasyMock.expect(mockedPlayer1Hand.removeResources(devCardResources)).andReturn(true);
        EasyMock.expect(mockedPlayer1Hand.addDevelopmentCard(monopoly)).andReturn(true);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.purchaseDevCard(monopoly);

        assertTrue(success);
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testPurchaseDevCard_Monopoly_cannotAfford_returnFalse() {
        DevCard monopoly = DevCard.MONOPOLY;

        EasyMock.expect(mockedPlayer1Hand.removeResources(devCardResources)).andReturn(false);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.purchaseDevCard(monopoly);

        assertFalse(success);
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testPurchaseDevCard_VictoryPoint_canAfford_returnTrue() {
        DevCard victoryPoint = DevCard.VICTORY;
        mockedPlayer1Hand.addResources(devCardResources);
        bank.removeResources(devCardResources);

        EasyMock.expect(mockedPlayer1Hand.removeResources(devCardResources)).andReturn(true);
        EasyMock.expect(mockedPlayer1Hand.addDevelopmentCard(victoryPoint)).andReturn(true);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.purchaseDevCard(victoryPoint);

        assertTrue(success);
        assertEquals(1, player.victoryPoints);
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testPurchaseDevCard_VictoryPoint_cannotAfford_returnFalse() {
        DevCard victoryPoint = DevCard.VICTORY;

        EasyMock.expect(mockedPlayer1Hand.removeResources(devCardResources)).andReturn(false);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.purchaseDevCard(victoryPoint);

        assertFalse(success);
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testPurchaseDevCard_knight_full_returnFalse() {
        DevCard knight = DevCard.KNIGHT;

        mockedPlayer1Hand.addResource(WOOL, 15);
        mockedPlayer1Hand.addResource(GRAIN, 15);
        mockedPlayer1Hand.addResource(ORE, 15);
        bank.removeResource(WOOL, 15);
        bank.removeResource(GRAIN, 15);
        bank.removeResource(ORE, 15);

        EasyMock.expect(mockedPlayer1Hand.removeResources(devCardResources)).andReturn(true);
        EasyMock.expect(mockedPlayer1Hand.addDevelopmentCard(knight)).andReturn(false);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.purchaseDevCard(knight);

        assertFalse(success);
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testPurchaseDevCard_yearOfPlenty_full_returnFalse() {
        DevCard yop = DevCard.PLENTY;

        mockedPlayer1Hand.addResource(WOOL, 3);
        mockedPlayer1Hand.addResource(GRAIN, 3);
        mockedPlayer1Hand.addResource(ORE, 3);
        bank.removeResource(WOOL, 3);
        bank.removeResource(GRAIN, 3);
        bank.removeResource(ORE, 3);

        EasyMock.expect(mockedPlayer1Hand.removeResources(devCardResources)).andReturn(true);
        EasyMock.expect(mockedPlayer1Hand.addDevelopmentCard(yop)).andReturn(false);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.purchaseDevCard(yop);

        assertFalse(success);
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testPurchaseDevCard_roadBuilding_full_returnFalse() {
        DevCard roadBuilding = DevCard.ROAD;

        mockedPlayer1Hand.addResource(WOOL, 3);
        mockedPlayer1Hand.addResource(GRAIN, 3);
        mockedPlayer1Hand.addResource(ORE, 3);
        bank.removeResource(WOOL, 3);
        bank.removeResource(GRAIN, 3);
        bank.removeResource(ORE, 3);

        EasyMock.expect(mockedPlayer1Hand.removeResources(devCardResources)).andReturn(true);
        EasyMock.expect(mockedPlayer1Hand.addDevelopmentCard(roadBuilding)).andReturn(false);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.purchaseDevCard(roadBuilding);

        assertFalse(success);
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testPurchaseDevCard_monopoly_full_returnFalse() {
        DevCard monopoly = DevCard.MONOPOLY;

        mockedPlayer1Hand.addResource(WOOL, 3);
        mockedPlayer1Hand.addResource(GRAIN, 3);
        mockedPlayer1Hand.addResource(ORE, 3);
        bank.removeResource(WOOL, 3);
        bank.removeResource(GRAIN, 3);
        bank.removeResource(ORE, 3);

        EasyMock.expect(mockedPlayer1Hand.removeResources(devCardResources)).andReturn(true);
        EasyMock.expect(mockedPlayer1Hand.addDevelopmentCard(monopoly)).andReturn(false);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.purchaseDevCard(monopoly);

        assertFalse(success);
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testPurchaseDevCard_victoryPoint_full_returnFalse() {
        DevCard vp = DevCard.VICTORY;
        
        mockedPlayer1Hand.addResource(WOOL, 6);
        mockedPlayer1Hand.addResource(GRAIN, 6);
        mockedPlayer1Hand.addResource(ORE, 6);
        bank.removeResource(WOOL, 6);
        bank.removeResource(GRAIN, 6);
        bank.removeResource(ORE, 6);

        EasyMock.expect(mockedPlayer1Hand.removeResources(devCardResources)).andReturn(true);
        EasyMock.expect(mockedPlayer1Hand.addDevelopmentCard(vp)).andReturn(false);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.purchaseDevCard(vp);

        assertFalse(success);
        EasyMock.verify(mockedPlayer1Hand);
    }
}
