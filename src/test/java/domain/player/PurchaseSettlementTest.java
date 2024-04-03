package domain.player;

import domain.bank.Bank;
import domain.bank.Resource;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static domain.bank.Resource.*;
import static org.junit.jupiter.api.Assertions.*;

public class PurchaseSettlementTest {

    Bank bank;
    Player player;
    Hand mockedHand;

    @BeforeEach
    public void setup() {
        bank = new Bank();
        player = new Player(1, bank);

        mockedHand = EasyMock.createMockBuilder(Hand.class).withConstructor().createMock();
        player.hand = mockedHand;
    }

    @Test
    public void testPurchaseSettlement_emptyHand_returnFalse() {
        boolean success = player.purchaseSettlement();

        int expectedVictoryPoints = 0;

        assertFalse(success);
        assertEquals(expectedVictoryPoints, player.victoryPoints);
    }

    @Test
    public void testPurchaseSettlement_justEnoughResources_returnTrue() {// Move resources into mocked hand
        bank.removeResources(new Resource[] { LUMBER, BRICK, WOOL, Resource.GRAIN });
        mockedHand.addResources(new Resource[] { LUMBER, BRICK, WOOL, Resource.GRAIN});

        EasyMock.replay(mockedHand);

        boolean success = player.purchaseSettlement();
        int expectedHandSize = 0;
        int expectedVictoryPoints = 1;
        int actualHandSize = mockedHand.getResourceCount();

        assertTrue(success);
        assertEquals(expectedHandSize, actualHandSize);
        assertEquals(expectedVictoryPoints, player.victoryPoints);
        EasyMock.verify(mockedHand);
    }

    @Test
    public void testPurchaseSettlement_missingOneResource_returnFalse() {// Move resources into mocked hand
        bank.removeResources(new Resource[] { LUMBER, BRICK, WOOL });
        mockedHand.addResources(new Resource[] { LUMBER, BRICK, WOOL });
        EasyMock.replay(mockedHand);

        boolean success = player.purchaseSettlement();
        int expectedHandSize = 3;
        int expectedVictoryPoints = 0;
        int actualHandSize = mockedHand.getResourceCount();

        assertFalse(success);
        assertEquals(expectedHandSize, actualHandSize);
        assertEquals(expectedVictoryPoints, player.victoryPoints);
        EasyMock.verify(mockedHand);
    }

    @Test
    public void testPurchaseSettlement_fullHand_returnTrue() {// remove all from bank
        bank.removeResource(LUMBER, 19);
        bank.removeResource(BRICK, 19);
        bank.removeResource(WOOL, 19);
        bank.removeResource(Resource.GRAIN, 19);
        bank.removeResource(Resource.ORE, 19);
        // add all to hand
        mockedHand.addResource(LUMBER, 19);
        mockedHand.addResource(BRICK, 19);
        mockedHand.addResource(WOOL, 19);
        mockedHand.addResource(Resource.GRAIN, 19);
        mockedHand.addResource(Resource.ORE, 19);
        EasyMock.replay(mockedHand);

        boolean success = player.purchaseSettlement();
        int expectedHandSize = 91;
        int expectedVictoryPoints = 1;
        int actualHandSize = mockedHand.getResourceCount();

        assertTrue(success);
        assertEquals(expectedHandSize, actualHandSize);
        assertEquals(expectedVictoryPoints, player.victoryPoints);
        EasyMock.verify(mockedHand);
    }

    @Test
    public void testPurchaseSettlement_notEnoughSettlements_returnFalse() {
        // Move resources into mocked hand
        bank.removeResources(new Resource[] { LUMBER, BRICK, WOOL, Resource.GRAIN });
        mockedHand.addResources(new Resource[] { LUMBER, BRICK, WOOL, Resource.GRAIN });

        player.numSettlements = 0;

        EasyMock.replay(mockedHand);

        boolean success = player.purchaseSettlement();
        int expectedHandSize = 4;
        int expectedVictoryPoints = 0;
        int actualHandSize = mockedHand.getResourceCount();

        assertFalse(success);
        assertEquals(expectedHandSize, actualHandSize);
        assertEquals(expectedVictoryPoints, player.victoryPoints);
        EasyMock.verify(mockedHand);
    }

    @Test
    public void testPurchaseRoad_emptyHand_returnFalse() {
        boolean success = player.purchaseRoad();

        assertFalse(success);
    }

    @Test
    public void testPurchaseRoad_JustEnoughResources_returnTrue() {
        // Move resources into mocked hand
        bank.removeResources(new Resource[] { LUMBER, BRICK });
        mockedHand.addResources(new Resource[] { LUMBER, BRICK });

        EasyMock.replay(mockedHand);

        boolean success = player.purchaseRoad();
        int expectedHandSize = 0;
        int actualHandSize = mockedHand.getResourceCount();

        assertTrue(success);
        assertEquals(expectedHandSize, actualHandSize);
        EasyMock.verify(mockedHand);
    }

    @Test
    public void testPurchaseRoad_OnlyOneLumber_returnFalse() {
        // Move resources into mocked hand
        bank.removeResources(new Resource[] { LUMBER });
        mockedHand.addResources(new Resource[] { LUMBER });
        EasyMock.replay(mockedHand);

        boolean success = player.purchaseRoad();
        int expectedHandSize = 1;
        int actualHandSize = mockedHand.getResourceCount();

        assertFalse(success);
        assertEquals(expectedHandSize, actualHandSize);
        EasyMock.verify(mockedHand);
    }

    @Test
    public void testPurchaseRoad_notEnoughRoads_returnFalse() {
        // Move resources into mocked hand
        bank.removeResources(new Resource[] { LUMBER, BRICK });
        mockedHand.addResources(new Resource[] { LUMBER, BRICK });

        player.numRoads = 0;

        EasyMock.replay(mockedHand);

        boolean success = player.purchaseRoad();
        int expectedHandSize = 2;
        int actualHandSize = mockedHand.getResourceCount();

        assertFalse(success);
        assertEquals(expectedHandSize, actualHandSize);
        EasyMock.verify(mockedHand);
    }
}
