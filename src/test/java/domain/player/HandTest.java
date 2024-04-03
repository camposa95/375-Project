package domain.player;

import domain.bank.Resource;
import domain.devcarddeck.DevCard;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static domain.bank.Resource.*;
import static org.junit.jupiter.api.Assertions.*;

public class HandTest {
    
    Hand hand;
    
    @BeforeEach
    public void setup() {
        hand = new Hand();
    }

    @Test
    public void testAddResource_zeroLumber_throwInvalidArgumentException() {
        String expectedMessage = "Resource amount must be between 0 and 19";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            hand.addResource(LUMBER, 0);
        });

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testAddResource_oneLumber_emptyHand_returnTrue() {
        boolean success = hand.addResource(LUMBER, 1);
        assertTrue(success);
    }

    @Test
    public void testAddResource_twoLumber_emptyHand_returnTrue() {
        boolean success = hand.addResource(LUMBER, 2);
        assertTrue(success);
    }

    @Test
    public void testAddResource_allLumber_emptyHand_returnTrue() {
        boolean success = hand.addResource(LUMBER, 19);
        assertTrue(success);
    }

    @Test
    public void testAddResource_twentyLumber_emptyHand_throwIllegalArgumentException() {
        String expectedMessage = "Resource amount must be between 0 and 19";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            hand.addResource(LUMBER, 20);
        });

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testAddResource_allLumber_handFullOfLumber_returnFalse() {
        //create hand full of lumber
        hand.addResource(LUMBER, 19);

        boolean success = hand.addResource(LUMBER, 1);
        assertFalse(success);
    }

    @Test
    public void testAddResource_oneBrick_emptyHand_returnTrue() {
        boolean success = hand.addResource(BRICK, 1);
        assertTrue(success);
    }

    @Test
    public void testAddResource_oneWool_emptyHand_returnTrue() {
        boolean success = hand.addResource(WOOL, 1);
        assertTrue(success);
    }

    @Test
    public void testAddResource_oneWheat_emptyHand_returnTrue() {
        boolean success = hand.addResource(GRAIN, 1);
        assertTrue(success);
    }

    @Test
    public void testAddResource_oneOre_emptyHand_returnTrue() {
        boolean success = hand.addResource(ORE, 1);
        assertTrue(success);
    }

    @Test
    public void testAddResource_oneLumber_handWithOneLumber_returnTrue() {
        hand.addResource(LUMBER, 1);

        boolean success = hand.addResource(LUMBER, 1);
        assertTrue(success);
    }
    
    @Test
    public void testRemoveResource_EmptyHand_returnFalse() {
        boolean success = hand.removeResource(LUMBER,1);
        assertFalse(success);
    }

    @Test
    public void testRemoveResource_OneLumber_returnTrue() {
        hand.addResource(LUMBER, 1);

        boolean success = hand.removeResource(LUMBER, 1);
        assertTrue(success);
    }

    @Test
    public void testRemoveResource_OneBrick_returnTrue() {
        hand.addResource(BRICK, 1);

        boolean success = hand.removeResource(BRICK, 1);
        assertTrue(success);
    }

    @Test
    public void testRemoveResource_OneWool_returnTrue() {
        hand.addResource(WOOL, 1);

        boolean success = hand.removeResource(WOOL, 1);
        assertTrue(success);
    }

    @Test
    public void testRemoveResource_OneWheat_returnTrue() {
        hand.addResource(GRAIN, 1);

        boolean success = hand.removeResource(GRAIN, 1);
        assertTrue(success);
    }

    @Test
    public void testRemoveResource_OneOre_returnTrue() {
        hand.addResource(ORE, 1);

        boolean success = hand.removeResource(ORE, 1);
        assertTrue(success);
    }

    @Test
    public void testRemoveResource_TwoLumber_returnTrue() {
        hand.addResource(LUMBER, 2);

        boolean success = hand.removeResource(LUMBER, 2);
        assertTrue(success);
    }

    @Test
    public void testRemoveResource_zeroAmount_throwIllegalArgumentException() {
        String expectedMessage = "Resource amount must be between 0 and 19";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            hand.removeResource(LUMBER, 0);
        });

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testRemoveResource_threeLumber_fromHandWithOneLumber_returnFalse() {
        hand.addResource(LUMBER, 1);

        boolean success = hand.removeResource(LUMBER, 3);
        assertFalse(success);
    }

    @Test
    public void testRemoveResource_oneLumber_fromHandWithTwoLumber_returnTrue() {
        hand.addResource(LUMBER, 2);

        boolean success = hand.removeResource(LUMBER, 1);
        assertTrue(success);
    }

    @Test
    public void testRemoveResource_20Lumber_throwIllegalArgumentException() {
        String expectedMessage = "Resource amount must be between 0 and 19";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            hand.removeResource(LUMBER, 20);
        });

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }




    @Test
    public void testGetResourceCardCount_withEmptyHand_returnZero() {
        int expected = 0;
        assertEquals(expected, hand.getResourceCount());
    }

    @Test
    public void testGetResourceCardCount_withOneLumberHand_returnOne() {
        hand.addResource(LUMBER, 1);

        int expected = 1;
        assertEquals(expected, hand.getResourceCount());
    }

    @Test
    public void testGetResourceCardCount_withOneOfEachResourceCardInHand_returnFive() {
        hand.addResource(LUMBER, 1);
        hand.addResource(BRICK, 1);
        hand.addResource(WOOL, 1);
        hand.addResource(GRAIN, 1);
        hand.addResource(ORE, 1);

        int expected = 5;
        assertEquals(expected, hand.getResourceCount());
    }

    @Test
    public void testGetResourceCardCount_withAllCards_return95() {
        hand.addResource(LUMBER, 19);
        hand.addResource(BRICK, 19);
        hand.addResource(WOOL, 19);
        hand.addResource(GRAIN, 19);
        hand.addResource(ORE, 19);

        int expected = 95;
        assertEquals(expected, hand.getResourceCount());
    }




    @Test
    public void testGetResources_emptyResources_returnTrue() {
        Resource[] resources = {};

        boolean success = hand.addResources(resources);
        assertTrue(success);
    }

    @Test
    public void testAddResources_1OfEach_emptyHand_returnTrue() {
        Resource[] resources = {
                LUMBER,
                BRICK,
                WOOL,
                GRAIN,
                ORE
        };

        int sizeExpected = 5;

        boolean success = hand.addResources(resources);
        assertTrue(success);
        assertEquals(sizeExpected, hand.getResourceCount());
    }

    @Test
    public void testAddResources_1OfEach_fullHand_returnFalse() {
        //fill hand
        hand.addResource(LUMBER, 19);
        hand.addResource(BRICK, 19);
        hand.addResource(WOOL, 19);
        hand.addResource(GRAIN, 19);
        hand.addResource(ORE, 19);

        int expectedSize = hand.getResourceCount();

        Resource[] resources = {
                LUMBER,
                BRICK,
                WOOL,
                GRAIN,
                ORE
        };

        //attempt to add one of each
        boolean success = hand.addResources(resources);
        assertFalse(success);
        assertEquals(expectedSize, hand.getResourceCount());
    }

    @Test
    public void testAddResources_emptyHand_20Lumber_returnFalse() {
        Resource[] resources = { //20 Lumber
                LUMBER, LUMBER, LUMBER,
                LUMBER, LUMBER, LUMBER,
                LUMBER, LUMBER, LUMBER,
                LUMBER, LUMBER, LUMBER,
                LUMBER, LUMBER, LUMBER,
                LUMBER, LUMBER, LUMBER,
                LUMBER, LUMBER
        };

        boolean success = hand.addResources(resources);
        assertFalse(success);
        assertEquals(0,hand.getResourceCount());
    }

    @Test
    public void testAddResources_1Lumber1Brick_noLumber19BrickInHand_returnFalse() {
        hand.addResource(BRICK,19);

        int expectedSize = hand.getResourceCount();

        Resource[] resources = {
                LUMBER,
                BRICK
        };

        boolean success = hand.addResources(resources);
        assertFalse(success);
        assertEquals(expectedSize, hand.getResourceCount());
    }

    @Test
    public void testAddResources_18_add1_returnTrue() {
        Resource[] resources = {
                LUMBER,LUMBER,LUMBER,
                LUMBER,LUMBER,LUMBER,
                LUMBER,LUMBER,LUMBER,
                LUMBER,LUMBER,LUMBER,
                LUMBER,LUMBER,LUMBER,
                LUMBER,LUMBER,LUMBER,
        };

        hand.addResource(LUMBER, 1);

        boolean success = hand.addResources(resources);
        assertTrue(success);
        assertEquals(19, hand.getResourceCount(LUMBER));
        assertEquals(19, hand.getResourceCount());
    }



    @Test
    public void testRemoveResources_emptyList_returnTrue() {
        Resource[] resources = {};

        boolean success = hand.removeResources(resources);
        assertTrue(success);
    }

    @Test
    public void testRemoveResources_1OfEach_emptyHand_returnFalse() {
        Resource[] resources = {
                LUMBER,
                BRICK,
                WOOL,
                GRAIN,
                ORE
        };

        boolean success = hand.removeResources(resources);
        assertFalse(success);
    }

    @Test
    public void testRemoveResources_1OfEach_fullHand_returnTrue() {
        //fill hand
        hand.addResource(LUMBER, 19);
        hand.addResource(BRICK, 19);
        hand.addResource(WOOL, 19);
        hand.addResource(GRAIN, 19);
        hand.addResource(ORE, 19);

        int expectedSize = hand.getResourceCount() - 5;

        Resource[] resources = {
                LUMBER,
                BRICK,
                WOOL,
                GRAIN,
                ORE
        };
        boolean success = hand.removeResources(resources);

        assertTrue(success);
        assertEquals(expectedSize, hand.getResourceCount());
    }

    @Test
    public void testRemoveResources_19Lumber_20LumberInHand_returnFalse() {
        hand.addResource(LUMBER, 19);

        int expectedSize = hand.getResourceCount();

        Resource[] resources = { //20 Lumber
                LUMBER, LUMBER, LUMBER,
                LUMBER, LUMBER, LUMBER,
                LUMBER, LUMBER, LUMBER,
                LUMBER, LUMBER, LUMBER,
                LUMBER, LUMBER, LUMBER,
                LUMBER, LUMBER, LUMBER,
                LUMBER, LUMBER
        };

        boolean success = hand.removeResources(resources);
        assertFalse(success);
        assertEquals(expectedSize, hand.getResourceCount());
    }

    @Test
    public void testRemoveResources_1Lumber1Brick_1LumberNoBrickInHand_returnFalse() {
        hand.addResources(new Resource[]{LUMBER});

        Resource[] resources = {
                LUMBER,
                BRICK
        };

        int expectedSize = hand.getResourceCount();

        boolean success = hand.removeResources(resources);
        assertFalse(success);
        assertEquals(expectedSize, hand.getResourceCount());
    }

    @Test
    public void testAddDevelopmentCard_knight_returnTrue() {
        DevCard knight = DevCard.KNIGHT;

        boolean success = hand.addDevelopmentCard(knight);

        assertTrue(success);
        assertEquals(1, hand.devCards.get(knight));
    }

    @Test
    public void testAddDevelopmentCard_yearOfPlenty_returnTrue() {
        DevCard yearOfPlenty = DevCard.PLENTY;

        boolean success = hand.addDevelopmentCard(yearOfPlenty);

        assertTrue(success);
        assertEquals(1, hand.devCards.get(yearOfPlenty));
    }

    @Test
    public void testAddDevelopmentCard_monopoly_returnTrue() {
        DevCard monopoly = DevCard.PLENTY;

        boolean success = hand.addDevelopmentCard(monopoly);

        assertTrue(success);
        assertEquals(1, hand.devCards.get(monopoly));
    }

    @Test
    public void testAddDevelopmentCard_roadBuilding_returnTrue() {
        DevCard roadBuilding = DevCard.PLENTY;

        boolean success = hand.addDevelopmentCard(roadBuilding);

        assertTrue(success);
        assertEquals(1, hand.devCards.get(roadBuilding));
    }

    @Test
    public void testAddDevelopmentCard_victoryPoint_returnTrue() {
        DevCard victoryPoint = DevCard.PLENTY;

        boolean success = hand.addDevelopmentCard(victoryPoint);

        assertTrue(success);
        assertEquals(1, hand.devCards.get(victoryPoint));
    }

    @Test
    public void testAddDevelopmentCard_null_throwIllegalArgumentException() {
        DevCard nullCard = null;

        String expectedMessage = "Card added to hand cannot be null";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
           hand.addDevelopmentCard(nullCard);
        });

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testAddDevelopmentCard_knight_has1Knight_returnTrue() {
        DevCard knight = DevCard.KNIGHT;

        hand.addDevelopmentCard(knight);
        boolean success = hand.addDevelopmentCard(knight);

        assertTrue(success);
        assertEquals(2, hand.devCards.get(knight));
    }

    @Test
    public void testAddDevelopmentCard_knight_hasMaxKnights_returnFalse() {
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
    public void testAddDevelopmentCard_yearOfPlenty_hasMaxYearOfPlenty_returnFalse() {
        DevCard yop = DevCard.PLENTY;

        for(int i = 0; i < 2; i++){
            hand.addDevelopmentCard(yop);
        }

        boolean success = hand.addDevelopmentCard(yop);
        assertFalse(success);
        assertEquals(2, hand.devCards.get(yop));
    }

    @Test
    public void testAddDevelopmentCard_monopoly_hasMaxMonopoly_returnFalse() {
        DevCard monopoly = DevCard.MONOPOLY;

        for(int i = 0; i < 2; i++){
            hand.addDevelopmentCard(monopoly);
        }

        boolean success = hand.addDevelopmentCard(monopoly);
        assertFalse(success);
        assertEquals(2, hand.devCards.get(monopoly));
    }

    @Test
    public void testAddDevelopmentCard_roadBuilding_hasMaxRoadBuilding_returnFalse() {
        DevCard roadBuilding = DevCard.ROAD;

        for(int i = 0; i < 2; i++){
            hand.addDevelopmentCard(roadBuilding);
        }

        boolean success = hand.addDevelopmentCard(roadBuilding);
        assertFalse(success);
        assertEquals(2, hand.devCards.get(roadBuilding));
    }

    @Test
    public void testAddDevelopmentCard_victoryPoint_hasMaxVictoryPoint_returnFalse() {
        DevCard victoryPoint = DevCard.VICTORY;

        for(int i = 0; i < 5; i++){
            hand.addDevelopmentCard(victoryPoint);
        }

        boolean success = hand.addDevelopmentCard(victoryPoint);
        assertFalse(success);
        assertEquals(5, hand.devCards.get(victoryPoint));
    }

    @Test
    public void testRemoveDevelopmentCard_knight_returnTrue() {
        DevCard knight = DevCard.KNIGHT;

        hand.addDevelopmentCard(knight);

        boolean success = hand.removeDevelopmentCard(knight);

        assertTrue(success);
        assertEquals(0, hand.devCards.get(knight));
    }

    @Test
    public void testRemoveDevelopmentCard_yearOfPlenty_returnTrue() {
        DevCard yop = DevCard.PLENTY;

        hand.addDevelopmentCard(yop);

        boolean success = hand.removeDevelopmentCard(yop);

        assertTrue(success);
        assertEquals(0, hand.devCards.get(yop));
    }

    @Test
    public void testRemoveDevelopmentCard_monopoly_returnTrue() {
        DevCard monopoly = DevCard.MONOPOLY;

        hand.addDevelopmentCard(monopoly);

        boolean success = hand.removeDevelopmentCard(monopoly);

        assertTrue(success);
        assertEquals(0, hand.devCards.get(monopoly));
    }

    @Test
    public void testRemoveDevelopmentCard_roadBuilding_returnTrue() {
        DevCard road = DevCard.ROAD;

        hand.addDevelopmentCard(road);

        boolean success = hand.removeDevelopmentCard(road);

        assertTrue(success);
        assertEquals(0, hand.devCards.get(road));
    }

    @Test
    public void testRemoveDevelopmentCard_victoryPoint_returnFalse() {
        DevCard vp = DevCard.VICTORY;

        hand.addDevelopmentCard(vp);

        boolean success = hand.removeDevelopmentCard(vp);

        assertFalse(success);
        assertEquals(1, hand.devCards.get(vp));
    }

    @Test
    public void testRemoveDevelopmentCard_null_throwIllegalArgumentException() {
        DevCard nullCard = null;

        String expectedMessage = "Cannot remove null Dev Card";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            hand.removeDevelopmentCard(nullCard);
        });

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testRemoveDevelopmentCard_knight_has0_returnFalse() {
        DevCard knight = DevCard.KNIGHT;

        boolean success = hand.removeDevelopmentCard(knight);

        assertFalse(success);
        assertEquals(0, hand.devCards.get(knight));
    }

    @Test
    public void testRemoveDevelopmentCard_yearOfPlenty_has0_returnFalse() {
        DevCard yop = DevCard.PLENTY;

        boolean success = hand.removeDevelopmentCard(yop);

        assertFalse(success);
        assertEquals(0, hand.devCards.get(yop));
    }

    @Test
    public void testRemoveDevelopmentCard_monopoly_has0_returnFalse() {
        DevCard monopoly = DevCard.MONOPOLY;

        boolean success = hand.removeDevelopmentCard(monopoly);

        assertFalse(success);
        assertEquals(0, hand.devCards.get(monopoly));
    }

    @Test
    public void testRemoveDevelopmentCard_roadBuilding_has0_returnFalse() {
        DevCard road = DevCard.ROAD;

        boolean success = hand.removeDevelopmentCard(road);

        assertFalse(success);
        assertEquals(0, hand.devCards.get(road));
    }
}
