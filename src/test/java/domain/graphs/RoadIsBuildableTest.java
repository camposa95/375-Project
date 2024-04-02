package domain.graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import domain.player.Player;

public class RoadIsBuildableTest {

    @Test
    public void testRoadIsBuildableIsOccupied() {
        RoadGraph roads = new RoadGraph();

        // get any road it doesn't matter which one
        Road testRoad = roads.getRoad(0);

        // make it occupied
        Player mockPlayer = EasyMock.createMock(Player.class);
        testRoad.setOwner(mockPlayer);

        EasyMock.replay(mockPlayer);

        boolean expected = false;
        boolean actual = testRoad.isBuildable();
        assertEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testRoadIsBuildableNotOccupied() {
        RoadGraph roads = new RoadGraph();

        // get any road it doesn't matter which one
        Road testRoad = roads.getRoad(0); 

        // Note the default state of a Road is to be unoccupied

        boolean expected = true;
        boolean actual = testRoad.isBuildable();
        assertEquals(expected, actual);
    }
}
