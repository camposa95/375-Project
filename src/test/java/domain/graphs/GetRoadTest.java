package domain.graphs;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import data.GameLoader;
import domain.game.GameType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GetRoadTest {

    private static final int NUM_ROADS = 72;
    
    RoadGraph roads;

    @BeforeEach
    public void setup() {
        roads = new RoadGraph();
    }
    @Test
    public void testGetRoadWithLocationId0() {
        Road road = roads.getRoad(0);
        int expectedId = 0;
        int actualId = road.getLocationId();

        assertEquals(expectedId, actualId);
    }

    @Test
    public void testGetRoadWithLocationId1() {
        Road road = roads.getRoad(1);
        int expectedId = 1;
        int actualId = road.getLocationId();

        assertEquals(expectedId, actualId);
    }

    @Test void testGetRoadWithN_2to71() {
        for (int i = 2; i < NUM_ROADS; i++) {
            Road road = roads.getRoad(i);
            int actualId = road.getLocationId();

            assertEquals(i, actualId);
        }
    }

    @Test
    public void testGetRoadWithInvalidLocationId72() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> roads.getRoad(NUM_ROADS));
        String expectedMessage = "LocationId out of bounds: Try [0, 72]";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetRoadWithInvalidLocationIdNegative1() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> roads.getRoad(-1));
        String expectedMessage = "LocationId out of bounds: Try [0, 72]";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
