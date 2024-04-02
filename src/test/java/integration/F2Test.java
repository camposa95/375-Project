package integration;
import org.junit.jupiter.api.Test;

import domain.devcarddeck.DevCard;
import domain.devcarddeck.DevelopmentCardDeck;

import static org.junit.jupiter.api.Assertions.*;

public class F2Test {

    int knightCount=0;
    int victoryCount=0;
    int roadCount=0;
    int plentyCount=0;
    int monopolyCount=0;

    @Test
    public void testInitializeGameAndHaveDevCards() {
     // ---------------------- Here are some basic wiring needed that would be done by main ------------------------------
        
        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();

    // -------------------------- Start of Test  ---------------------------
        
      
        int expK = 14;
        int expV = 5; 
        int expR = 2;  
        int expP = 2;
        int expM = 2;
        for(DevCard card : devCardDeck.getDeck()) {
            switch (card) {
                case KNIGHT -> {
                    knightCount++;
                    return;
                }
                case VICTORY -> {
                    victoryCount++;
                    return;
                }
                case ROAD -> {
                    roadCount++;
                    return;
                }
                case PLENTY -> {
                    plentyCount++;
                    return;
                }
                case MONOPOLY -> {
                    monopolyCount++;
                    return;
                }
                default -> {
                    System.out.println(card);
                    fail();
                }
            }
        }
        assertEquals(expK,knightCount);
        assertEquals(expV,victoryCount);
        assertEquals(expR,roadCount);
        assertEquals(expP,plentyCount);
        assertEquals(expM,monopolyCount);
    }
}
