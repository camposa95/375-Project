package domain.player;

import domain.bank.Bank;
import domain.bank.Resource;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static domain.bank.Resource.GRAIN;
import static domain.bank.Resource.ORE;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpgradeSettlementTest {
    
    private static final Resource[] upgradeResources = {ORE, ORE, GRAIN};
    private static final Resource[] twoUpgradeResources = {ORE, ORE, ORE, GRAIN, GRAIN};

    Bank bank;
    Player player;
    Hand mockedPlayer1Hand;

    @BeforeEach
    public void setup() {
        bank = new Bank();
        player = new Player(1, bank);
        mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor()
                .addMockedMethod("removeResources").createMock();
        player.hand = mockedPlayer1Hand;
    }

    @Test
    public void testUpgradeSettlementToCity_HasCity_CanAfford_returnTrue() {

        mockedPlayer1Hand.addResources(twoUpgradeResources);
        bank.removeResources(twoUpgradeResources);

        EasyMock.expect(mockedPlayer1Hand.removeResources(twoUpgradeResources)).andReturn(true);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.canUpgradeSettlementToCity();

        assertTrue(success);
        assertEquals(3, player.numCities);
    }

    @Test
    public void testUpgradeSettlementToCity_HasCity_CannotAfford_returnFalse() {
        mockedPlayer1Hand.addResources(upgradeResources);

        bank.removeResources(upgradeResources);

        EasyMock.expect(mockedPlayer1Hand.removeResources(twoUpgradeResources)).andReturn(false);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.canUpgradeSettlementToCity();

        assertFalse(success);
        assertEquals(4, player.numCities);
    }

    @Test
    public void testUpgradeSettlementToCity_HasNoCities_CanAfford_returnFalse() {
        mockedPlayer1Hand.addResources(twoUpgradeResources);

        bank.removeResources(twoUpgradeResources);

        // simulate having played all cities
        player.numCities = 0;

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.canUpgradeSettlementToCity();

        assertFalse(success);
        assertEquals(0, player.numCities);
    }

    @Test
    public void testUpgradeSettlementToCity_HasNoCities_CannotAfford_returnFalse() {
        mockedPlayer1Hand.addResources(upgradeResources);

        bank.removeResources(upgradeResources);

        // simulate having played all cities
        player.numCities = 0;

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.canUpgradeSettlementToCity();

        assertFalse(success);
        assertEquals(0, player.numCities);
        EasyMock.verify(mockedPlayer1Hand);
    }
}
