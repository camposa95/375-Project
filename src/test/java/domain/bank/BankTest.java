package domain.bank;

import domain.game.NotEnoughResourcesException;
import domain.player.Player;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BankTest {

    private Player mockPlayer;
    private Bank bank;

    @BeforeEach
    public void setup() {
        mockPlayer = EasyMock.niceMock(Player.class);
        bank = new Bank();
    }

    @Test
    public void testRemoveResource_0Lumber_throwIllegalArgumentException() {
        String expectedMessage = "Must be a value between 1 and 19";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> bank.removeResource(Resource.LUMBER, 0));
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testRemoveResource_20Lumber_fullBank_throwIllegalArgumentException() {
        String expectedMessage = "Must be a value between 1 and 19";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> bank.removeResource(Resource.LUMBER, 20));
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testRemoveResource_1Lumber_fullBank_returnTrue() {
        boolean success = bank.removeResource(Resource.LUMBER, 1);
        assertTrue(success);
    }

    @Test
    public void testRemoveResource_19Lumber_fullBank_returnTrue() {
        boolean success = bank.removeResource(Resource.LUMBER, 19);
        assertTrue(success);
    }

    @Test
    public void testRemoveResource_2Lumber_1InBank_returnFalse() {
        //Leave 1 in bank
        bank.removeResource(Resource.LUMBER, 18);

        //attempt to remove 2
        boolean success = bank.removeResource(Resource.LUMBER, 2);
        assertFalse(success);
    }

    @Test
    public void testRemoveResource_1Lumber_1InBank_returnTrue(){
        //Leave 1 in bank
        bank.removeResource(Resource.LUMBER, 18);

        //attempt to remove 1
        boolean success = bank.removeResource(Resource.LUMBER, 1);
        assertTrue(success);
    }

    @Test
    public void testRemoveResource_emptyBank_returnFalse(){
        bank.removeResource(Resource.LUMBER, 19);
        boolean success = bank.removeResource(Resource.LUMBER, 1);
        assertFalse(success);
    }

    @Test
    public void testAddResource_0Lumber_throwIllegalArgumentException() {
        String expectedMessage = "Must be a value between 1 and 19";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> bank.addResource(Resource.LUMBER, 0));
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testAddResource_20Lumber_emptyBank_throwIllegalArgumentException() {
        String expectedMessage = "Must be a value between 1 and 19";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> bank.addResource(Resource.LUMBER, 20));
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testAddResource_1Lumber_fullBank_returnFalse() {
        boolean success = bank.addResource(Resource.LUMBER, 1);
        assertFalse(success);
    }

    @Test
    public void testAddResource_19Lumber_emptyBank_returnTrue(){
        bank.removeResource(Resource.LUMBER, 19);

        boolean success = bank.addResource(Resource.LUMBER, 19);
        assertTrue(success);
    }

    @Test
    public void testAddResource_1Lumber_emptyBank_returnTrue() {
        bank.removeResource(Resource.LUMBER, 19);

        boolean success = bank.addResource(Resource.LUMBER, 1);
        assertTrue(success);
    }

    @Test
    public void testGetResourceAmount_Lumber_FullBank_Return19(){
        int expected = 19;
        int actual = bank.getResourceAmount(Resource.LUMBER);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetResourceAmount_Lumber_EmptyBank_Return0(){
        int expected = 0;
        bank.removeResource(Resource.LUMBER, 19);
        int actual = bank.getResourceAmount(Resource.LUMBER);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetResourceAmount_Lumber_1InBank_Return1(){
        int expected = 1;
        bank.removeResource(Resource.LUMBER, 18);
        int actual = bank.getResourceAmount(Resource.LUMBER);
        assertEquals(expected, actual);
    }



    @Test
    public void testAddResources_emptyResources__anyBank_returnTrue(){
        Resource[] resources = {};
        boolean success = bank.addResources(resources);
        assertTrue(success);
    }

    @Test
    public void testAddResources_1OfEach_emptyBank_returnTrue(){
        //empty the bank
        bank.removeResource(Resource.LUMBER, 19);
        bank.removeResource(Resource.BRICK, 19);
        bank.removeResource(Resource.WOOL, 19);
        bank.removeResource(Resource.GRAIN, 19);
        bank.removeResource(Resource.ORE, 19);

        int expectedSize = 5;

        Resource[] resources = {
                Resource.LUMBER,
                Resource.BRICK,
                Resource.WOOL,
                Resource.GRAIN,
                Resource.ORE
        };

        boolean success = bank.addResources(resources);

        int actualSize = bank.getResourceAmount(Resource.LUMBER)
                + bank.getResourceAmount(Resource.BRICK)
                + bank.getResourceAmount(Resource.WOOL)
                + bank.getResourceAmount(Resource.GRAIN)
                + bank.getResourceAmount(Resource.ORE);

        assertTrue(success);
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testAddResources_1OfEach_fullBank_returnFalse(){
        int expectedSize = 95;

        Resource[] resources = {
                Resource.LUMBER,
                Resource.BRICK,
                Resource.WOOL,
                Resource.GRAIN,
                Resource.ORE
        };

        boolean success = bank.addResources(resources);

        int actualSize = bank.getResourceAmount(Resource.LUMBER)
                + bank.getResourceAmount(Resource.BRICK)
                + bank.getResourceAmount(Resource.WOOL)
                + bank.getResourceAmount(Resource.GRAIN)
                + bank.getResourceAmount(Resource.ORE);

        assertFalse(success);
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testAddResources_emptyBank_20Lumber_returnFalse(){
        //empty the bank
        bank.removeResource(Resource.LUMBER, 19);
        bank.removeResource(Resource.BRICK, 19);
        bank.removeResource(Resource.WOOL, 19);
        bank.removeResource(Resource.GRAIN, 19);
        bank.removeResource(Resource.ORE, 19);

        int expectedSize = 0;

        Resource[] resources = {
                Resource.LUMBER, Resource.LUMBER, Resource.LUMBER,
                Resource.LUMBER, Resource.LUMBER, Resource.LUMBER,
                Resource.LUMBER, Resource.LUMBER, Resource.LUMBER,
                Resource.LUMBER, Resource.LUMBER, Resource.LUMBER,
                Resource.LUMBER, Resource.LUMBER, Resource.LUMBER,
                Resource.LUMBER, Resource.LUMBER, Resource.LUMBER,
                Resource.LUMBER, Resource.LUMBER
        };

        boolean success = bank.addResources(resources);

        int actualSize = bank.getResourceAmount(Resource.LUMBER)
                + bank.getResourceAmount(Resource.BRICK)
                + bank.getResourceAmount(Resource.WOOL)
                + bank.getResourceAmount(Resource.GRAIN)
                + bank.getResourceAmount(Resource.ORE);

        assertFalse(success);
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testAddResources_1Lumber1Brick_noLumber19BrickInBank_returnFalse(){
        //empty the lumber
        bank.removeResource(Resource.LUMBER, 19);

        Resource[] resources = {
                Resource.LUMBER,
                Resource.BRICK
        };

        int expectedSize = 76;

        boolean success = bank.addResources(resources);

        int actualSize = bank.getResourceAmount(Resource.LUMBER)
                + bank.getResourceAmount(Resource.BRICK)
                + bank.getResourceAmount(Resource.WOOL)
                + bank.getResourceAmount(Resource.GRAIN)
                + bank.getResourceAmount(Resource.ORE);

        assertFalse(success);
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testRemoveResources_emptyList_returnTrue(){
        Resource[] resources = {};
        boolean success = bank.removeResources(resources);
        assertTrue(success);
    }

    @Test
    public void testRemoveResources_1OfEach_emptyBank_returnFalse() {
        //empty the bank
        bank.removeResource(Resource.LUMBER, 19);
        bank.removeResource(Resource.BRICK, 19);
        bank.removeResource(Resource.WOOL, 19);
        bank.removeResource(Resource.GRAIN, 19);
        bank.removeResource(Resource.ORE, 19);

        Resource[] resources = {
                Resource.LUMBER,
                Resource.BRICK,
                Resource.WOOL,
                Resource.GRAIN,
                Resource.ORE
        };

        int expectedSize = 0;
        boolean success = bank.removeResources(resources);
        int actualSize = bank.getResourceAmount(Resource.LUMBER)
                + bank.getResourceAmount(Resource.BRICK)
                + bank.getResourceAmount(Resource.WOOL)
                + bank.getResourceAmount(Resource.GRAIN)
                + bank.getResourceAmount(Resource.ORE);
        assertFalse(success);
        assertEquals(expectedSize,actualSize);
    }

    @Test
    public void testRemoveResources_1OfEach_fullBank_returnTrue(){
        Resource[] resources = {
                Resource.LUMBER,
                Resource.BRICK,
                Resource.WOOL,
                Resource.GRAIN,
                Resource.ORE
        };

        int expectedSize = 90;
        boolean success = bank.removeResources(resources);
        int actualSize = bank.getResourceAmount(Resource.LUMBER)
                + bank.getResourceAmount(Resource.BRICK)
                + bank.getResourceAmount(Resource.WOOL)
                + bank.getResourceAmount(Resource.GRAIN)
                + bank.getResourceAmount(Resource.ORE);
        assertTrue(success);
        assertEquals(expectedSize,actualSize);
    }

    @Test
    public void testRemoveResources_20Lumber_19LumberInBank_returnFalse(){
        Resource[] resources = {
                Resource.LUMBER, Resource.LUMBER, Resource.LUMBER,
                Resource.LUMBER, Resource.LUMBER, Resource.LUMBER,
                Resource.LUMBER, Resource.LUMBER, Resource.LUMBER,
                Resource.LUMBER, Resource.LUMBER, Resource.LUMBER,
                Resource.LUMBER, Resource.LUMBER, Resource.LUMBER,
                Resource.LUMBER, Resource.LUMBER, Resource.LUMBER,
                Resource.LUMBER, Resource.LUMBER
        };

        int expectedSize = 95;
        boolean success = bank.removeResources(resources);
        int actualSize = bank.getResourceAmount(Resource.LUMBER)
                + bank.getResourceAmount(Resource.BRICK)
                + bank.getResourceAmount(Resource.WOOL)
                + bank.getResourceAmount(Resource.GRAIN)
                + bank.getResourceAmount(Resource.ORE);
        assertFalse(success);
        assertEquals(expectedSize,actualSize);
    }

    @Test
    public void testRemoveResources_1Lumber1Brick_1LumberNoBrickInBank_returnFalse(){
        //empty the lumber
        bank.removeResource(Resource.LUMBER, 18);
        bank.removeResource(Resource.BRICK, 19);

        Resource[] resources = {
                Resource.LUMBER,
                Resource.BRICK
        };

        int expectedSize = 58;

        boolean success = bank.removeResources(resources);

        int actualSize = bank.getResourceAmount(Resource.LUMBER)
                + bank.getResourceAmount(Resource.BRICK)
                + bank.getResourceAmount(Resource.WOOL)
                + bank.getResourceAmount(Resource.GRAIN)
                + bank.getResourceAmount(Resource.ORE);

        assertFalse(success);
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testTakeOutLoan_withValidLoan_expectSuccess() throws NotEnoughResourcesException {
        Resource[] borrow = {Resource.ORE};

        Bank bank = new Bank();

        EasyMock.expect(mockPlayer.getPlayerNum()).andReturn(1).times(2);
        EasyMock.replay(mockPlayer);

        bank.takeOutLoan(mockPlayer, borrow);

        Assert.assertTrue(bank.playerHasLoan(mockPlayer));
        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testTakeOutLoan_withInvalidLoan_expectFailure() {
        Resource[] borrow = {Resource.ORE, Resource.LUMBER, Resource.WOOL, Resource.LUMBER};

        Bank bank = new Bank();

        EasyMock.expect(mockPlayer.getPlayerNum()).andReturn(1).times(2);
        EasyMock.replay(mockPlayer);

        Assert.assertThrows(IllegalArgumentException.class, () -> bank.takeOutLoan(mockPlayer, borrow));

        Assert.assertFalse(bank.playerHasLoan(mockPlayer));
        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testTakeOutLoan_withNotEnoughResources_expectFailure() {
        Resource[] borrow = {Resource.ORE, Resource.ORE};

        Bank bank = new Bank();
        bank.removeResource(Resource.ORE, 18);

        EasyMock.expect(mockPlayer.getPlayerNum()).andReturn(1).times(2);
        EasyMock.replay(mockPlayer);

        Assert.assertThrows(NotEnoughResourcesException.class, () -> bank.takeOutLoan(mockPlayer, borrow));

        Assert.assertFalse(bank.playerHasLoan(mockPlayer));
        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testTakeOutLoan_withExistingLoan_expectFailure() throws NotEnoughResourcesException {
        Resource[] borrow = {Resource.ORE};

        Bank bank = new Bank();

        EasyMock.expect(mockPlayer.getPlayerNum()).andReturn(1).times(3);
        EasyMock.expect(mockPlayer.addResources(borrow)).andReturn(true);
        EasyMock.replay(mockPlayer);

        bank.takeOutLoan(mockPlayer, borrow);
        Assert.assertTrue(bank.playerHasLoan(mockPlayer));

        Assert.assertThrows(IllegalStateException.class, () -> bank.takeOutLoan(mockPlayer, borrow));
        EasyMock.verify(mockPlayer);
    }
}
