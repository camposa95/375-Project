package gamedatastructures;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BankTest {

    @Test
    public void testRemoveResource_0Lumber_throwIllegalArgumentException() {
        Bank bank = Bank.getInstance();
        bank.resetBank();
        String expectedMessage = "Must be a value between 1 and 19";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bank.removeResource(Resource.LUMBER, 0);
        });
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testRemoveResource_20Lumber_fullBank_throwIllegalArgumentException() {
        Bank bank = Bank.getInstance();
        bank.resetBank();
        String expectedMessage = "Must be a value between 1 and 19";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bank.removeResource(Resource.LUMBER, 20);
        });
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testRemoveResource_1Lumber_fullBank_returnTrue() {
        Bank bank = Bank.getInstance();
        bank.resetBank();
        boolean success = bank.removeResource(Resource.LUMBER, 1);
        assertTrue(success);
    }

    @Test
    public void testRemoveResource_19Lumber_fullBank_returnTrue() {
        Bank bank = Bank.getInstance();
        bank.resetBank();
        boolean success = bank.removeResource(Resource.LUMBER, 19);
        assertTrue(success);
    }

    @Test
    public void testRemoveResource_2Lumber_1InBank_returnFalse() {
        Bank bank = Bank.getInstance();
        bank.resetBank();
        //Leave 1 in bank
        bank.removeResource(Resource.LUMBER, 18);

        //attempt to remove 2
        boolean success = bank.removeResource(Resource.LUMBER, 2);
        assertFalse(success);
    }

    @Test
    public void testRemoveResource_1Lumber_1InBank_returnTrue(){
        Bank bank = Bank.getInstance();
        bank.resetBank();
        //Leave 1 in bank
        bank.removeResource(Resource.LUMBER, 18);

        //attempt to remove 1
        boolean success = bank.removeResource(Resource.LUMBER, 1);
        assertTrue(success);
    }

    @Test
    public void testRemoveResource_emptyBank_returnFalse(){
        Bank bank = Bank.getInstance();
        bank.resetBank();
        bank.removeResource(Resource.LUMBER, 19);
        boolean success = bank.removeResource(Resource.LUMBER, 1);
        assertFalse(success);
    }

//    @Test
//    public void testRemoveResource_1Desert_throwIllegalArgumentException(){
//        Bank bank = Bank.getInstance();
//        bank.resetBank();
//        String expectedMessage = "Cannot use Desert";
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            bank.removeResource(Resource.LUMBER, 1);
//        });
//        String actualMessage = exception.getMessage();
//        assertEquals(expectedMessage, actualMessage);
//
//    }




    @Test
    public void testAddResource_0Lumber_throwIllegalArgumentException() {
        Bank bank = Bank.getInstance();
        bank.resetBank();
        String expectedMessage = "Must be a value between 1 and 19";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bank.addResource(Resource.LUMBER, 0);
        });
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testAddResource_20Lumber_emptyBank_throwIllegalArgumentException() {
        Bank bank = Bank.getInstance();
        bank.resetBank();
        String expectedMessage = "Must be a value between 1 and 19";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bank.addResource(Resource.LUMBER, 20);
        });
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testAddResource_1Lumber_fullBank_returnFalse() {
        Bank bank = Bank.getInstance();
        bank.resetBank();
        boolean success = bank.addResource(Resource.LUMBER, 1);
        assertFalse(success);
    }

    @Test
    public void testAddResource_19Lumber_emptyBank_returnTrue(){
        Bank bank = Bank.getInstance();
        bank.resetBank();
        bank.removeResource(Resource.LUMBER, 19);

        boolean success = bank.addResource(Resource.LUMBER, 19);
        assertTrue(success);
    }

    @Test
    public void testAddResource_1Lumber_emptyBank_returnTrue() {
        Bank bank = Bank.getInstance();
        bank.resetBank();
        bank.removeResource(Resource.LUMBER, 19);

        boolean success = bank.addResource(Resource.LUMBER, 1);
        assertTrue(success);
    }

//    @Test
//    public void testAddResource_1Desert_throwIllegalArgumentException() {
//        Bank bank = Bank.getInstance();
//        bank.resetBank();
//        String expectedMessage = "Cannot use Desert";
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            bank.addResource(Resource.LUMBER, 1);
//        });
//        String actualMessage = exception.getMessage();
//        assertEquals(expectedMessage, actualMessage);
//    }



//    @Test
//    public void testGetResourceAmount_Desert_ReturnIllegalArgumentException() {
//        Bank bank = Bank.getInstance();
//        bank.resetBank();
//        String expectedMessage = "Cannot use Desert";
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            bank.getResourceAmount(Terrain.DESERT);
//        });
//        String actualMessage = exception.getMessage();
//        assertEquals(expectedMessage, actualMessage);
//    }

    @Test
    public void testGetResourceAmount_Lumber_FullBank_Return19(){
        Bank bank = Bank.getInstance();
        bank.resetBank();
        int expected = 19;
        int actual = bank.getResourceAmount(Resource.LUMBER);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetResourceAmount_Lumber_Emptybank_Return0(){
        Bank bank = Bank.getInstance();
        bank.resetBank();
        int expected = 0;
        bank.removeResource(Resource.LUMBER, 19);
        int actual = bank.getResourceAmount(Resource.LUMBER);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetResourceAmount_Lumber_1InBank_Return1(){
        Bank bank = Bank.getInstance();
        bank.resetBank();
        int expected = 1;
        bank.removeResource(Resource.LUMBER, 18);
        int actual = bank.getResourceAmount(Resource.LUMBER);
        assertEquals(expected, actual);
    }



    @Test
    public void testAddResources_emptyResources__anyBank_returnTrue(){
        Bank bank = Bank.getInstance();
        bank.resetBank();

        Resource[] resources = {};
        boolean success = bank.addResources(resources);
        assertTrue(success);
    }

    @Test
    public void testAddResources_1OfEach_emptyBank_returnTrue(){
        Bank bank = Bank.getInstance();
        bank.resetBank();

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
        Bank bank = Bank.getInstance();
        bank.resetBank();

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
        Bank bank = Bank.getInstance();
        bank.resetBank();

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
        Bank bank = Bank.getInstance();
        bank.resetBank();

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
        Bank bank = Bank.getInstance();
        bank.resetBank();

        Resource[] resources = {};
        boolean success = bank.removeResources(resources);
        assertTrue(success);
    }

    @Test
    public void testRemoveResources_1OfEach_emptyBank_returnFalse(){
        Bank bank = Bank.getInstance();
        bank.resetBank();

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
        Bank bank = Bank.getInstance();
        bank.resetBank();

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
        Bank bank = Bank.getInstance();
        bank.resetBank();

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
        Bank bank = Bank.getInstance();
        bank.resetBank();

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
}
