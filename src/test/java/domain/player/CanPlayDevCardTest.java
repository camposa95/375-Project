package domain.player;

import domain.bank.Bank;
import domain.bank.Resource;
import domain.devcarddeck.DevCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static domain.bank.Resource.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class CanPlayDevCardTest {
    
    private static final Resource[] devCardResources = {WOOL, GRAIN, ORE};

    Bank bank;
    Player player;
    
    @BeforeEach
    public void setup() {
        bank = new Bank();
        player = new Player(1, bank);
    }
    
    @Test
    public void testCanPlayDevelopmentCard_hasPlayedDevCard_returnFalse() {
        player.hasPlayedDevCard = true;
        DevCard knight = DevCard.KNIGHT;
        assertFalse(player.canPlayDevelopmentCard(knight));
    }

    @Test
    public void testCanPlayDevelopmentCard_nullCard_throwIllegalArgumentException() {
        String expectedMessage = "Cannot attempt to play null Development Card";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            player.canPlayDevelopmentCard(null);
        });

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testCanPlayDevelopmentCard_knight_returnTrue() {
        DevCard knight = DevCard.KNIGHT;

        // make sure player has purchased a Knight
        player.hand.addResources(devCardResources);
        bank.removeResources(devCardResources);
        player.purchaseDevCard(knight);

        // attempt to play knight
        boolean success = player.canPlayDevelopmentCard(knight);

        assertTrue(success);
        assertEquals(1, player.numKnightsPlayed);
    }

    @Test
    public void testCanPlayDevelopmentCard_yearOfPlenty_returnTrue() {
        DevCard yop = DevCard.PLENTY;

        // make sure player has purchased a Year of plenty card
        player.hand.addResources(devCardResources);
        bank.removeResources(devCardResources);
        player.purchaseDevCard(yop);

        // attempt to play year of plenty
        boolean success = player.canPlayDevelopmentCard(yop);

        assertTrue(success);
    }

    @Test
    public void testCanPlayDevelopmentCard_roadBuilding_returnTrue() {
        DevCard roadBuilding = DevCard.ROAD;

        // make sure player has purchased a road Building card
        player.hand.addResources(devCardResources);
        bank.removeResources(devCardResources);
        player.purchaseDevCard(roadBuilding);

        // attempt to play road building
        boolean success = player.canPlayDevelopmentCard(roadBuilding);

        assertTrue(success);
    }

    @Test
    public void testCanPlayDevelopmentCard_monopoly_returnTrue() {
        DevCard monopoly = DevCard.MONOPOLY;

        // make sure player has purchased a Monopoly card
        player.hand.addResources(devCardResources);
        bank.removeResources(devCardResources);
        player.purchaseDevCard(monopoly);

        // attempt to play monopoly
        boolean success = player.canPlayDevelopmentCard(monopoly);

        assertTrue(success);
    }

    @Test
    public void testCanPlayDevelopmentCard_victoryPoint_returnFalse() {
        DevCard victoryPoint = DevCard.VICTORY;

        // make sure player has purchased a victory point card
        player.hand.addResources(devCardResources);
        bank.removeResources(devCardResources);
        player.purchaseDevCard(victoryPoint);

        // attempt to play victory point (should fail, card is not playable)
        boolean success = player.canPlayDevelopmentCard(victoryPoint);

        assertFalse(success);
    }

    @Test
    public void testCanPlayDevelopmentCard_knight_noKnightInHand_returnFalse() {
        DevCard knight = DevCard.KNIGHT;

        boolean success = player.canPlayDevelopmentCard(knight);
        assertFalse(success);
    }

    @Test
    public void testCanPlayDevelopmentCard_yearOfPlenty_noYOPInHand_returnFalse() {
        DevCard yop = DevCard.PLENTY;

        boolean success = player.canPlayDevelopmentCard(yop);
        assertFalse(success);
    }

    @Test
    public void testCanPlayDevelopmentCard_roadBuilding_noRBInHand_returnFalse() {
        DevCard road = DevCard.ROAD;

        boolean success = player.canPlayDevelopmentCard(road);
        assertFalse(success);
    }

    @Test
    public void testCanPlayDevelopmentCard_monopoly_noMonopolyInHand_returnFalse() {
        DevCard monopoly = DevCard.MONOPOLY;

        boolean success = player.canPlayDevelopmentCard(monopoly);
        assertFalse(success);
    }

    @Test
    public void testCanPlayDevelopmentCard_attemptToPlay2_returnFalse() {
        DevCard knight = DevCard.KNIGHT;

        player.hand.addResources(devCardResources);
        player.hand.addResources(devCardResources);

        bank.removeResources(devCardResources);
        bank.removeResources(devCardResources);

        player.purchaseDevCard(knight);
        player.purchaseDevCard(knight);

        boolean playFirst = player.canPlayDevelopmentCard(knight);
        boolean playSecond = player.canPlayDevelopmentCard(knight);
        assertTrue(playFirst);
        assertFalse(playSecond);
    }
}
