package integration;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;

import gamedatastructures.DevCard;
import gamedatastructures.DevelopmentCardDeck;
public class F2Test {
    
       
    private static final String GAMEBOARD_LAYOUT_FILE = "src/main/java/gamedatastructures/TileLayout.txt";
    private static final String ROAD_TO_ROAD_LAYOUT_FILE = "src/main/java/graphs/RoadToRoadLayout.txt";
    private static final String ROAD_TO_VERTEX_LAYOUT_FILE = "src/main/java/graphs/RoadToVertexLayout.txt";
    private static final String VERTEX_TO_VERTEX_LAYOUT_FILE = "src/main/java/graphs/VertexToVertexLayout.txt";
    private static final String VERTEX_TO_ROAD_LAYOUT_FILE = "src/main/java/graphs/VertexToRoadLayout.txt";
    private static final String VERTEX_TO_PORT_LAYOUT_FILE = "src/main/java/graphs/VertexToPortLayout.txt";
    int knightCount=0;
    int victoryCount=0;
    int roadCount=0;
    int plentyCount=0;
    int monopolyCount=0;

    @Test
    public void testInitilizeGameAndHaveDevCards() {
     // ---------------------- Here are some basic wiring needed that would be done by main ------------------------------
        
        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();

    // -------------------------- Start of Test  ---------------------------
        
      
        int expK = 14;
        int expV = 5; 
        int expR = 2;  
        int expP = 2;
        int expM = 2;
        for(DevCard card : devCardDeck.getDeck()) {
            switch(card) {
                case KNIGHT :
                    knightCount++;
                    return;
                case VICTORY:
                    victoryCount++;
                    return;
                case ROAD:
                    roadCount++;
                    return;
                case PLENTY:
                    plentyCount++;
                    return;
                case MONOPOLY:
                    monopolyCount++;
                    return;
                default:
                    System.out.println(card);
                    assertFalse(true);
            }
        }
        assertEquals(expK,knightCount);
        assertEquals(expV,victoryCount);
        assertEquals(expR,roadCount);
        assertEquals(expP,plentyCount);
        assertEquals(expM,monopolyCount);
    }
}
