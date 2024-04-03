package domain.player;

import domain.bank.Bank;
import domain.bank.Resource;
import domain.devcarddeck.DevCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static domain.bank.Resource.*;
import static org.junit.jupiter.api.Assertions.*;

public class UseDevCardTest {

    private static final Resource[] devCardResources = {WOOL, GRAIN, ORE};

    Bank bank;
    Player player;

    @BeforeEach
    public void setup() {
        bank = new Bank();
        player = new Player(1, bank);
    }

    @Test
    public void testUseDevCard_nullCard() {
        String expectedMessage = "Cannot attempt to play null Development Card";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            player.useDevCard(null);
        });

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testUseDevCard_exactEnoughNonBought() {
        DevCard card = DevCard.PLENTY;

        // make sure player has purchased a Year of plenty card
        player.hand.addResources(devCardResources);
        bank.removeResources(devCardResources);
        player.purchaseDevCard(card);

        // make it so it wasn't bought this turn
        player.addBoughtCardsToHand();

        // attempt to play card
        boolean success = player.useDevCard(card);

        assertTrue(success);
    }

    @Test
    public void testUseDevCard_exactEnoughOneBought() {
        DevCard card = DevCard.PLENTY;

        // make sure player has purchased a card
        player.hand.addResources(devCardResources);
        bank.removeResources(devCardResources);
        player.purchaseDevCard(card);
        // make it so it wasn't bought this turn
        player.addBoughtCardsToHand();


        // buy another of the same card
        player.hand.addResources(devCardResources);
        bank.removeResources(devCardResources);
        player.purchaseDevCard(card);
        // this one was bought during the turn

        // attempt to play card
        boolean success = player.useDevCard(card);

        assertTrue(success);
    }

    @Test
    public void testUseDevCard_notEnoughNoneBought() {
        DevCard card = DevCard.PLENTY;

        // attempt to play card that we don't have
        boolean success = player.useDevCard(card);

        assertFalse(success);
    }

    @Test
    public void testUseDevCard_notEnoughOneBought() {
        DevCard card = DevCard.PLENTY;

        // make sure player has purchased a card
        player.hand.addResources(devCardResources);
        bank.removeResources(devCardResources);
        player.purchaseDevCard(card);
        // player did buy it this turn

        // attempt to play card
        boolean success = player.useDevCard(card);

        assertFalse(success);
    }

    // false because the card was victory card
    @Test
    public void testUseDevCard_victoryPointCard() {
        DevCard card = DevCard.VICTORY;

        // make sure player has purchased a victory point card
        player.hand.addResources(devCardResources);
        bank.removeResources(devCardResources);
        player.purchaseDevCard(card);

        // make it so it wasn't bought this turn
        player.addBoughtCardsToHand();

        // attempt to play victory point card. note regardless if we have it or not it should fail
        boolean success = player.useDevCard(card);

        assertFalse(success);
    }
}
