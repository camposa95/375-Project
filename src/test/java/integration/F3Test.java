package integration;

import domain.bank.Bank;
import domain.bank.Resource;
import domain.player.Hand;
import domain.player.HarvestBooster;
import domain.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static domain.bank.Resource.*;
import static org.junit.jupiter.api.Assertions.*;

public class F3Test {
    Bank bank;
    Player player1;
    Player player2;
    Player player3;
    Player player4;
    
    @BeforeEach
    public void setUpPlayerAndBank() {
        bank = new Bank();
        player1 = new Player(1, new HarvestBooster(), bank);
        player2 = new Player(2, new HarvestBooster(), bank);
        player3 = new Player(3, new HarvestBooster(), bank);
        player4 = new Player(4, new HarvestBooster(), bank);
    }

    @Test
    public void testPlayersStartWithEmptyHand() {
        // Assert that each hand is a distinct object
        Set<Hand> hands = new HashSet<>();
        assertTrue(hands.add(player1.hand));
        assertTrue(hands.add(player2.hand));
        assertTrue(hands.add(player3.hand));
        assertTrue(hands.add(player4.hand));

        
        // Assert they are empty by default
        assertEquals(0, player1.hand.getResourceCount());
        assertEquals(0, player2.hand.getResourceCount());
        assertEquals(0, player3.hand.getResourceCount());
        assertEquals(0, player4.hand.getResourceCount());
    }

    @Test
    public void testPlayerCanAddResourcesToTheirHand() {
        // try to add resources to each player's hand and should expect success
        Resource[] resourcesForPlayer1 = {LUMBER, BRICK};
        Resource[] resourcesForPlayer2 = {WOOL, WOOL, GRAIN};
        Resource[] resourcesForPlayer3 = {LUMBER, BRICK, WOOL, GRAIN};
        Resource[] resourcesForPlayer4 = {GRAIN, GRAIN, ORE, ORE, ORE};

        // add the cards to the hands

        //assertions for player 1
        assertTrue(player1.hand.addResources(resourcesForPlayer1));
        assertEquals(2, player1.hand.getResourceCount());
        assertEquals(1, player1.hand.getResourceCount(LUMBER));
        assertEquals(1, player1.hand.getResourceCount(BRICK));

        //assertions for player 2
        assertTrue(player2.hand.addResources(resourcesForPlayer2));
        assertEquals(3, player2.hand.getResourceCount());
        assertEquals(2, player2.hand.getResourceCount(WOOL));
        assertEquals(1, player2.hand.getResourceCount(GRAIN));

        //assertions for player 3
        assertTrue(player3.hand.addResources(resourcesForPlayer3));
        assertEquals(4, player3.hand.getResourceCount());
        assertEquals(1, player3.hand.getResourceCount(LUMBER));
        assertEquals(1, player3.hand.getResourceCount(BRICK));
        assertEquals(1, player3.hand.getResourceCount(WOOL));
        assertEquals(1, player3.hand.getResourceCount(GRAIN));

        //assertions for player 4
        assertTrue(player4.hand.addResources(resourcesForPlayer4));
        assertEquals(5, player4.hand.getResourceCount());
        assertEquals(2, player4.hand.getResourceCount(GRAIN));
        assertEquals(3, player4.hand.getResourceCount(ORE));

        // 19 lumber cards
        Resource[] addTooMany = {LUMBER, LUMBER, LUMBER, LUMBER, LUMBER, LUMBER, LUMBER, LUMBER, LUMBER, LUMBER, LUMBER, LUMBER, LUMBER, LUMBER, LUMBER, LUMBER, LUMBER, LUMBER, LUMBER,};
        // now attempt to add too many cards to a hand. This should fail
        assertFalse(player1.hand.addResources(addTooMany));
        assertEquals(1, player1.hand.getResourceCount(LUMBER));
        assertEquals(1, player1.hand.getResourceCount(BRICK));
    }

    @Test
    public void testPlayerCanRemoveResourcesToTheirHand() {
        Resource[] resourcesForPlayer1 = {LUMBER, BRICK};
        Resource[] resourcesForPlayer2 = {WOOL, WOOL, GRAIN};
        Resource[] resourcesForPlayer3 = {LUMBER, BRICK, WOOL, GRAIN};
        Resource[] resourcesForPlayer4 = {GRAIN, GRAIN, ORE, ORE, ORE};

        // add the cards to the hands
        player1.hand.addResources(resourcesForPlayer1);
        player2.hand.addResources(resourcesForPlayer2);
        player3.hand.addResources(resourcesForPlayer3);
        player4.hand.addResources(resourcesForPlayer4);

        // Now we test trying to remove resources from each player's hand

        // player 1's assertions
        assertTrue(player1.hand.removeResource(LUMBER, 1));
        assertEquals(1, player1.hand.getResourceCount());
        assertEquals(0, player1.hand.getResourceCount(LUMBER));
        assertEquals(1, player1.hand.getResourceCount(BRICK));

        // player 2's assertions
        assertTrue(player2.hand.removeResource(WOOL, 1));
        assertEquals(2, player2.hand.getResourceCount());
        assertEquals(1, player2.hand.getResourceCount(WOOL));
        assertEquals(1, player2.hand.getResourceCount(GRAIN));

        // player 3's assertions
        assertTrue(player3.hand.removeResource(BRICK, 1));
        assertEquals(3, player3.hand.getResourceCount());
        assertEquals(1, player3.hand.getResourceCount(LUMBER));
        assertEquals(1, player3.hand.getResourceCount(WOOL));
        assertEquals(1, player3.hand.getResourceCount(GRAIN));

        // player 4's assertions
        assertTrue(player4.hand.removeResource(ORE, 1));
        assertEquals(4, player4.hand.getResourceCount());
        assertEquals(2, player4.hand.getResourceCount(GRAIN));
        assertEquals(2, player4.hand.getResourceCount(ORE));

        // now try removing something from the hand that the player does not have
        Resource[] resourcesToRemove = {ORE};
        
        assertFalse(player1.hand.removeResources(resourcesToRemove));
        assertEquals(1, player1.hand.getResourceCount());
        assertEquals(0, player1.hand.getResourceCount(LUMBER));
        assertEquals(1, player1.hand.getResourceCount(BRICK));
    }
}
