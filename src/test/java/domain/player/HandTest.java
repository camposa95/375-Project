package domain.player;

import domain.bank.Resource;
import domain.devcarddeck.DevCard;
import domain.player.Hand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HandTest {

    @Test
    public void testAddResource_zeroLumber_throwInvalidArgumentException(){
        Hand hand = new Hand();

        String expectedMessage = "Resource amount must be between 0 and 19";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            hand.addResource(Resource.LUMBER, 0);
        });

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testAddResource_oneLumber_emptyHand_returnTrue() {
        Hand hand = new Hand();
        boolean success = hand.addResource(Resource.LUMBER, 1);
        assertTrue(success);
    }

    @Test
    public void testAddResource_twoLumber_emptyHand_returnTrue() {
        Hand hand = new Hand();
        boolean success = hand.addResource(Resource.LUMBER, 2);
        assertTrue(success);
    }

    @Test
    public void testAddResource_allLumber_emptyHand_returnTrue() {
        Hand hand = new Hand();
        boolean success = hand.addResource(Resource.LUMBER, 19);
        assertTrue(success);
    }

    @Test
    public void testAddResource_twentyLumber_emptyHand_throwIllegalArgumentException() {
        Hand hand = new Hand();

        String expectedMessage = "Resource amount must be between 0 and 19";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            hand.addResource(Resource.LUMBER, 20);
        });

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testAddResource_allLumber_handFullOfLumber_returnFalse(){
        Hand hand = new Hand();

        //create hand full of lumber
        hand.addResource(Resource.LUMBER, 19);

        boolean success = hand.addResource(Resource.LUMBER, 1);
        assertFalse(success);
    }

    @Test
    public void testAddResource_oneBrick_emptyHand_returnTrue() {
        Hand hand = new Hand();
        boolean success = hand.addResource(Resource.BRICK, 1);
        assertTrue(success);
    }

    @Test
    public void testAddResource_oneWool_emptyHand_returnTrue(){
        Hand hand = new Hand();
        boolean success = hand.addResource(Resource.WOOL, 1);
        assertTrue(success);
    }

    @Test
    public void testAddResource_oneWheat_emptyHand_returnTrue(){
        Hand hand = new Hand();
        boolean success = hand.addResource(Resource.GRAIN, 1);
        assertTrue(success);
    }

    @Test
    public void testAddResource_oneOre_emptyHand_returnTrue(){
        Hand hand = new Hand();
        boolean success = hand.addResource(Resource.ORE, 1);
        assertTrue(success);
    }

    @Test
    public void testAddResource_oneLumber_handWithOneLumber_returnTrue(){
        Hand hand = new Hand();
        hand.addResource(Resource.LUMBER, 1);

        boolean success = hand.addResource(Resource.LUMBER, 1);
        assertTrue(success);
    }



    @Test
    public void testRemoveResource_EmptyHand_returnFalse(){
        Hand hand = new Hand();

        boolean success = hand.removeResource(Resource.LUMBER,1);
        assertFalse(success);
    }

    @Test
    public void testRemoveResource_OneLumber_returnTrue(){
        Hand hand = new Hand();
        hand.addResource(Resource.LUMBER, 1);

        boolean success = hand.removeResource(Resource.LUMBER, 1);
        assertTrue(success);
    }

    @Test
    public void testRemoveResource_OneBrick_returnTrue(){
        Hand hand = new Hand();
        hand.addResource(Resource.BRICK, 1);

        boolean success = hand.removeResource(Resource.BRICK, 1);
        assertTrue(success);
    }

    @Test
    public void testRemoveResource_OneWool_returnTrue(){
        Hand hand = new Hand();
        hand.addResource(Resource.WOOL, 1);

        boolean success = hand.removeResource(Resource.WOOL, 1);
        assertTrue(success);
    }

    @Test
    public void testRemoveResource_OneWheat_returnTrue(){
        Hand hand = new Hand();
        hand.addResource(Resource.GRAIN, 1);

        boolean success = hand.removeResource(Resource.GRAIN, 1);
        assertTrue(success);
    }

    @Test
    public void testRemoveResource_OneOre_returnTrue(){
        Hand hand = new Hand();
        hand.addResource(Resource.ORE, 1);

        boolean success = hand.removeResource(Resource.ORE, 1);
        assertTrue(success);
    }

    @Test
    public void testRemoveResource_TwoLumber_returnTrue(){
        Hand hand = new Hand();
        hand.addResource(Resource.LUMBER, 2);

        boolean success = hand.removeResource(Resource.LUMBER, 2);
        assertTrue(success);
    }

    @Test
    public void testRemoveResource_zeroAmount_throwIllegalArgumentException(){
        Hand hand = new Hand();

        String expectedMessage = "Resource amount must be between 0 and 19";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            hand.removeResource(Resource.LUMBER, 0);
        });

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testRemoveResource_threeLumber_fromHandWithOneLumber_returnFalse(){
        Hand hand = new Hand();
        hand.addResource(Resource.LUMBER, 1);

        boolean success = hand.removeResource(Resource.LUMBER, 3);
        assertFalse(success);
    }

    @Test
    public void testRemoveResource_oneLumber_fromHandWithTwoLumber_returnTrue(){
        Hand hand = new Hand();
        hand.addResource(Resource.LUMBER, 2);

        boolean success = hand.removeResource(Resource.LUMBER, 1);
        assertTrue(success);
    }

    @Test
    public void testRemoveResource_20Lumber_throwIllegalArgumentException(){
        Hand hand = new Hand();

        String expectedMessage = "Resource amount must be between 0 and 19";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            hand.removeResource(Resource.LUMBER, 20);
        });

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }




    @Test
    public void testGetResourceCardCount_withEmptyHand_returnZero(){
        Hand hand = new Hand();

        int expected = 0;
        assertEquals(expected, hand.getResourceCardCount());
    }

    @Test
    public void testGetResourceCardCount_withOneLumberHand_returnOne(){
        Hand hand = new Hand();
        hand.addResource(Resource.LUMBER, 1);

        int expected = 1;
        assertEquals(expected, hand.getResourceCardCount());
    }

    @Test
    public void testGetResourceCardCount_withOneOfEachResourceCardInHand_returnFive(){
        Hand hand = new Hand();
        hand.addResource(Resource.LUMBER, 1);
        hand.addResource(Resource.BRICK, 1);
        hand.addResource(Resource.WOOL, 1);
        hand.addResource(Resource.GRAIN, 1);
        hand.addResource(Resource.ORE, 1);

        int expected = 5;
        assertEquals(expected, hand.getResourceCardCount());
    }

    @Test
    public void testGetResourceCardCount_withAllCards_return95(){
        Hand hand = new Hand();
        hand.addResource(Resource.LUMBER, 19);
        hand.addResource(Resource.BRICK, 19);
        hand.addResource(Resource.WOOL, 19);
        hand.addResource(Resource.GRAIN, 19);
        hand.addResource(Resource.ORE, 19);

        int expected = 95;
        assertEquals(expected, hand.getResourceCardCount());
    }




    @Test
    public void testGetResources_emptyResources_returnTrue(){
        Hand hand = new Hand();
        Resource[] resources = {};

        boolean success = hand.addResources(resources);
        assertTrue(success);
    }

    @Test
    public void testAddResources_1OfEach_emptyHand_returnTrue(){
        Hand hand = new Hand();
        Resource[] resources = {
                Resource.LUMBER,
                Resource.BRICK,
                Resource.WOOL,
                Resource.GRAIN,
                Resource.ORE
        };

        int sizeExpected = 5;

        boolean success = hand.addResources(resources);
        assertTrue(success);
        assertEquals(sizeExpected, hand.getResourceCardCount());
    }

    @Test
    public void testAddResources_1OfEach_fullHand_returnFalse(){
        Hand hand = new Hand();

        //fill hand
        hand.addResource(Resource.LUMBER, 19);
        hand.addResource(Resource.BRICK, 19);
        hand.addResource(Resource.WOOL, 19);
        hand.addResource(Resource.GRAIN, 19);
        hand.addResource(Resource.ORE, 19);

        int expectedSize = hand.getResourceCardCount();

        Resource[] resources = {
                Resource.LUMBER,
                Resource.BRICK,
                Resource.WOOL,
                Resource.GRAIN,
                Resource.ORE
        };

        //attempt to add one of each
        boolean success = hand.addResources(resources);
        assertFalse(success);
        assertEquals(expectedSize, hand.getResourceCardCount());
    }

    @Test
    public void testAddResources_emptyHand_20Lumber_returnFalse(){
        Hand hand = new Hand();
        Resource[] resources = { //20 Lumber
                Resource.LUMBER, Resource.LUMBER, Resource.LUMBER,
                Resource.LUMBER, Resource.LUMBER, Resource.LUMBER,
                Resource.LUMBER, Resource.LUMBER, Resource.LUMBER,
                Resource.LUMBER, Resource.LUMBER, Resource.LUMBER,
                Resource.LUMBER, Resource.LUMBER, Resource.LUMBER,
                Resource.LUMBER, Resource.LUMBER, Resource.LUMBER,
                Resource.LUMBER, Resource.LUMBER
        };

        boolean success = hand.addResources(resources);
        assertFalse(success);
        assertEquals(0,hand.getResourceCardCount());
    }

    @Test
    public void testAddResources_1Lumber1Brick_noLumber19BrickInHand_returnFalse(){
        Hand hand = new Hand();
        hand.addResource(Resource.BRICK,19);

        int expectedSize = hand.getResourceCardCount();

        Resource[] resources = {
                Resource.LUMBER,
                Resource.BRICK
        };

        boolean success = hand.addResources(resources);
        assertFalse(success);
        assertEquals(expectedSize, hand.getResourceCardCount());
    }

    @Test
    public void testAddResources_18_add1_returnTrue(){
        Hand hand = new Hand();
        Resource[] resources = {
                Resource.LUMBER,Resource.LUMBER,Resource.LUMBER,
                Resource.LUMBER,Resource.LUMBER,Resource.LUMBER,
                Resource.LUMBER,Resource.LUMBER,Resource.LUMBER,
                Resource.LUMBER,Resource.LUMBER,Resource.LUMBER,
                Resource.LUMBER,Resource.LUMBER,Resource.LUMBER,
                Resource.LUMBER,Resource.LUMBER,Resource.LUMBER,
        };

        hand.addResource(Resource.LUMBER, 1);

        boolean success = hand.addResources(resources);
        assertTrue(success);
        assertEquals(19, hand.getResourceCardAmount(Resource.LUMBER));
        assertEquals(19, hand.getResourceCardCount());
    }



    @Test
    public void testRemoveResources_emptyList_returnTrue(){
        Hand hand = new Hand();
        Resource[] resources = {};

        boolean success = hand.removeResources(resources);
        assertTrue(success);
    }

    @Test
    public void testRemoveResources_1OfEach_emptyHand_returnFalse(){
        Hand hand = new Hand();
        Resource[] resources = {
                Resource.LUMBER,
                Resource.BRICK,
                Resource.WOOL,
                Resource.GRAIN,
                Resource.ORE
        };

        boolean success = hand.removeResources(resources);
        assertFalse(success);
    }

    @Test
    public void testRemoveResources_1OfEach_fullHand_returnTrue(){
        Hand hand = new Hand();
        //fill hand
        hand.addResource(Resource.LUMBER, 19);
        hand.addResource(Resource.BRICK, 19);
        hand.addResource(Resource.WOOL, 19);
        hand.addResource(Resource.GRAIN, 19);
        hand.addResource(Resource.ORE, 19);

        int expectedSize = hand.getResourceCardCount() - 5;

        Resource[] resources = {
                Resource.LUMBER,
                Resource.BRICK,
                Resource.WOOL,
                Resource.GRAIN,
                Resource.ORE
        };
        boolean success = hand.removeResources(resources);

        assertTrue(success);
        assertEquals(expectedSize, hand.getResourceCardCount());
    }

    @Test
    public void testRemoveResources_19Lumber_20LumberInHand_returnFalse(){
        Hand hand = new Hand();
        hand.addResource(Resource.LUMBER, 19);

        int expectedSize = hand.getResourceCardCount();

        Resource[] resources = { //20 Lumber
                Resource.LUMBER, Resource.LUMBER, Resource.LUMBER,
                Resource.LUMBER, Resource.LUMBER, Resource.LUMBER,
                Resource.LUMBER, Resource.LUMBER, Resource.LUMBER,
                Resource.LUMBER, Resource.LUMBER, Resource.LUMBER,
                Resource.LUMBER, Resource.LUMBER, Resource.LUMBER,
                Resource.LUMBER, Resource.LUMBER, Resource.LUMBER,
                Resource.LUMBER, Resource.LUMBER
        };

        boolean success = hand.removeResources(resources);
        assertFalse(success);
        assertEquals(expectedSize, hand.getResourceCardCount());
    }

    @Test
    public void testRemoveResources_1Lumber1Brick_1LumberNoBrickInHand_returnFalse(){
        Hand hand = new Hand();
        hand.addResources(new Resource[]{Resource.LUMBER});

        Resource[] resources = {
                Resource.LUMBER,
                Resource.BRICK
        };

        int expectedSize = hand.getResourceCardCount();

        boolean success = hand.removeResources(resources);
        assertFalse(success);
        assertEquals(expectedSize, hand.getResourceCardCount());
    }

    @Test
    public void testAddDevelopmentCard_knight_returnTrue(){
        Hand hand = new Hand();
        DevCard knight = DevCard.KNIGHT;

        boolean success = hand.addDevelopmentCard(knight);

        assertTrue(success);
        assertEquals(1, hand.devCards.get(knight));
    }

    @Test
    public void testAddDevelopmentCard_yearOfPlenty_returnTrue(){
        Hand hand = new Hand();
        DevCard yearOfPlenty = DevCard.PLENTY;

        boolean success = hand.addDevelopmentCard(yearOfPlenty);

        assertTrue(success);
        assertEquals(1, hand.devCards.get(yearOfPlenty));
    }

    @Test
    public void testAddDevelopmentCard_monopoly_returnTrue(){
        Hand hand = new Hand();
        DevCard monopoly = DevCard.PLENTY;

        boolean success = hand.addDevelopmentCard(monopoly);

        assertTrue(success);
        assertEquals(1, hand.devCards.get(monopoly));
    }

    @Test
    public void testAddDevelopmentCard_roadBuilding_returnTrue(){
        Hand hand = new Hand();
        DevCard roadBuilding = DevCard.PLENTY;

        boolean success = hand.addDevelopmentCard(roadBuilding);

        assertTrue(success);
        assertEquals(1, hand.devCards.get(roadBuilding));
    }

    @Test
    public void testAddDevelopmentCard_victoryPoint_returnTrue(){
        Hand hand = new Hand();
        DevCard victoryPoint = DevCard.PLENTY;

        boolean success = hand.addDevelopmentCard(victoryPoint);

        assertTrue(success);
        assertEquals(1, hand.devCards.get(victoryPoint));
    }

    @Test
    public void testAddDevelopmentCard_null_throwIllegalArgumentException(){
        Hand hand = new Hand();
        DevCard nullCard = null;

        String expectedMessage = "Card added to hand cannot be null";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
           hand.addDevelopmentCard(nullCard);
        });

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testAddDevelopmentCard_knight_has1Knight_returnTrue(){
        Hand hand = new Hand();
        DevCard knight = DevCard.KNIGHT;

        hand.addDevelopmentCard(knight);
        boolean success = hand.addDevelopmentCard(knight);

        assertTrue(success);
        assertEquals(2, hand.devCards.get(knight));
    }

    @Test
    public void testAddDevelopmentCard_knight_hasMaxKnights_returnFalse(){
        Hand hand = new Hand();
        DevCard knight = DevCard.KNIGHT;

        //add max amount of knights
        for(int i = 0; i < 14; i++){
            hand.addDevelopmentCard(knight);
        }

        boolean success = hand.addDevelopmentCard(knight);
        assertFalse(success);
        assertEquals(14, hand.devCards.get(knight));
    }

    @Test
    public void testAddDevelopmentCard_yearOfPlenty_hasMaxYearOfPlenty_returnFalse(){
        Hand hand = new Hand();
        DevCard yop = DevCard.PLENTY;

        for(int i = 0; i < 2; i++){
            hand.addDevelopmentCard(yop);
        }

        boolean success = hand.addDevelopmentCard(yop);
        assertFalse(success);
        assertEquals(2, hand.devCards.get(yop));
    }

    @Test
    public void testAddDevelopmentCard_monopoly_hasMaxMonopoly_returnFalse(){
        Hand hand = new Hand();
        DevCard monopoly = DevCard.MONOPOLY;

        for(int i = 0; i < 2; i++){
            hand.addDevelopmentCard(monopoly);
        }

        boolean success = hand.addDevelopmentCard(monopoly);
        assertFalse(success);
        assertEquals(2, hand.devCards.get(monopoly));
    }

    @Test
    public void testAddDevelopmentCard_roadBuilding_hasMaxRoadBuilding_returnFalse(){
        Hand hand = new Hand();
        DevCard roadBuilding = DevCard.ROAD;

        for(int i = 0; i < 2; i++){
            hand.addDevelopmentCard(roadBuilding);
        }

        boolean success = hand.addDevelopmentCard(roadBuilding);
        assertFalse(success);
        assertEquals(2, hand.devCards.get(roadBuilding));
    }

    @Test
    public void testAddDevelopmentCard_victoryPoint_hasMaxVictoryPoint_returnFalse(){
        Hand hand = new Hand();
        DevCard victoryPoint = DevCard.VICTORY;

        for(int i = 0; i < 5; i++){
            hand.addDevelopmentCard(victoryPoint);
        }

        boolean success = hand.addDevelopmentCard(victoryPoint);
        assertFalse(success);
        assertEquals(5, hand.devCards.get(victoryPoint));
    }

    @Test
    public void testRemoveDevelopmentCard_knight_returnTrue(){
        Hand hand = new Hand();
        DevCard knight = DevCard.KNIGHT;

        hand.addDevelopmentCard(knight);

        boolean success = hand.removeDevelopmentCard(knight);

        assertTrue(success);
        assertEquals(0, hand.devCards.get(knight));
    }

    @Test
    public void testRemoveDevelopmentCard_yearOfPlenty_returnTrue(){
        Hand hand = new Hand();
        DevCard yop = DevCard.PLENTY;

        hand.addDevelopmentCard(yop);

        boolean success = hand.removeDevelopmentCard(yop);

        assertTrue(success);
        assertEquals(0, hand.devCards.get(yop));
    }

    @Test
    public void testRemoveDevelopmentCard_monopoly_returnTrue(){
        Hand hand = new Hand();
        DevCard monopoly = DevCard.MONOPOLY;

        hand.addDevelopmentCard(monopoly);

        boolean success = hand.removeDevelopmentCard(monopoly);

        assertTrue(success);
        assertEquals(0, hand.devCards.get(monopoly));
    }

    @Test
    public void testRemoveDevelopmentCard_roadBuilding_returnTrue(){
        Hand hand = new Hand();
        DevCard road = DevCard.ROAD;

        hand.addDevelopmentCard(road);

        boolean success = hand.removeDevelopmentCard(road);

        assertTrue(success);
        assertEquals(0, hand.devCards.get(road));
    }

    @Test
    public void testRemoveDevelopmentCard_victoryPoint_returnFalse(){
        Hand hand = new Hand();
        DevCard vp = DevCard.VICTORY;

        hand.addDevelopmentCard(vp);

        boolean success = hand.removeDevelopmentCard(vp);

        assertFalse(success);
        assertEquals(1, hand.devCards.get(vp));
    }

    @Test
    public void testRemoveDevelopmentCard_null_throwIllegalArgumentException(){
        Hand hand = new Hand();
        DevCard nullCard = null;

        String expectedMessage = "Cannot remove null Dev Card";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            hand.removeDevelopmentCard(nullCard);
        });

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testRemoveDevelopmentCard_knight_has0_returnFalse(){
        Hand hand = new Hand();
        DevCard knight = DevCard.KNIGHT;

        boolean success = hand.removeDevelopmentCard(knight);

        assertFalse(success);
        assertEquals(0, hand.devCards.get(knight));
    }

    @Test
    public void testRemoveDevelopmentCard_yearOfPlenty_has0_returnFalse(){
        Hand hand = new Hand();
        DevCard yop = DevCard.PLENTY;

        boolean success = hand.removeDevelopmentCard(yop);

        assertFalse(success);
        assertEquals(0, hand.devCards.get(yop));
    }

    @Test
    public void testRemoveDevelopmentCard_monopoly_has0_returnFalse(){
        Hand hand = new Hand();
        DevCard monopoly = DevCard.MONOPOLY;

        boolean success = hand.removeDevelopmentCard(monopoly);

        assertFalse(success);
        assertEquals(0, hand.devCards.get(monopoly));
    }

    @Test
    public void testRemoveDevelopmentCard_roadBuilding_has0_returnFalse(){
        Hand hand = new Hand();
        DevCard road = DevCard.ROAD;

        boolean success = hand.removeDevelopmentCard(road);

        assertFalse(success);
        assertEquals(0, hand.devCards.get(road));
    }
}
