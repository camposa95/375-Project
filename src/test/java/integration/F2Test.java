package integration;
import org.junit.jupiter.api.Test;

import domain.devcarddeck.DevCard;
import domain.devcarddeck.DevelopmentCardDeck;

import static org.junit.jupiter.api.Assertions.*;

public class F2Test {

    int knightCount = 0;
    int victoryCount = 0;
    int roadCount = 0;
    int plentyCount = 0;
    int monopolyCount = 0;

    @Test
    public void testInitializeGameAndHaveDevCards() {
        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
      
        int expK = 14;
        int expV = 5; 
        int expR = 2;  
        int expP = 2;
        int expM = 2;

        for (DevCard card : devCardDeck.getDeck()) {
            switch (card) {
                case KNIGHT -> knightCount++;
                case VICTORY -> victoryCount++;
                case ROAD -> roadCount++;
                case PLENTY -> plentyCount++;
                case MONOPOLY -> monopolyCount++;
                default -> fail(card.toString());
            }
        }

        assertEquals(expK, knightCount);
        assertEquals(expV, victoryCount);
        assertEquals(expR, roadCount);
        assertEquals(expP, plentyCount);
        assertEquals(expM, monopolyCount);
    }
}
