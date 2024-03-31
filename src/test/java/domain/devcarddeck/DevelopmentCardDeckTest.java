package domain.devcarddeck;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DevelopmentCardDeckTest {

    @Test
    public void test_InitializeDeck() {
        DevelopmentCardDeck Deck1 = new DevelopmentCardDeck();
        DevCard[] DefaultDeck = {DevCard.KNIGHT, DevCard.KNIGHT, DevCard.KNIGHT, DevCard.KNIGHT, DevCard.KNIGHT, DevCard.KNIGHT, DevCard.KNIGHT, DevCard.KNIGHT, DevCard.KNIGHT, DevCard.KNIGHT, DevCard.KNIGHT, DevCard.KNIGHT, DevCard.KNIGHT, DevCard.KNIGHT, DevCard.VICTORY, DevCard.VICTORY, DevCard.VICTORY, DevCard.VICTORY, DevCard.VICTORY, DevCard.ROAD, DevCard.ROAD, DevCard.PLENTY, DevCard.PLENTY, DevCard.MONOPOLY, DevCard.MONOPOLY};
        DevCard[] testDeck =  Deck1.getDeck().toArray(new DevCard[25]);
        assertEquals(25, Deck1.getDeck().size());
        int mismatchCount = 0;
        for(int i =0; i<25;i++) {
            if(DefaultDeck[i] != testDeck[i]) {
                mismatchCount++;
            }
        }
        System.out.println(mismatchCount);
        assertNotEquals(0, mismatchCount);
        
    }

    @Test
    public void test_DrawFullDeck_ExpectCard() throws EmptyDevCardDeckException {
        DevelopmentCardDeck Deck1 = new DevelopmentCardDeck();
        assertEquals(25, Deck1.getDeck().size());
        assertEquals(DevCard.class, Deck1.draw().getClass());
        assertEquals(24, Deck1.getDeck().size());
    }
    
    @Test 
    public void test_DrawEmptyDeck_ExpectError() throws EmptyDevCardDeckException {
        DevelopmentCardDeck Deck1 = new DevelopmentCardDeck();
        
        for(int i =0; i<25; i++){
            Deck1.draw();
        }   

        assertThrows(EmptyDevCardDeckException.class,
                Deck1::draw);
    }
}
