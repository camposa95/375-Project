package domain.graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import domain.game.GameType;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.player.Player;

public class RoadIsBuildableTest {

    GameboardGraph gameboardGraph;

    @BeforeEach
    public void setup() {
        gameboardGraph = new GameboardGraph(GameType.Beginner);
    }

    @Test
    public void testRoadIsBuildableIsOccupied() {
        // get any road it doesn't matter which one
        Road testRoad = gameboardGraph.getRoad(0);

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
        // get any road it doesn't matter which one
        Road testRoad = gameboardGraph.getRoad(0);

        // Note the default state of a Road is to be unoccupied

        boolean expected = true;
        boolean actual = testRoad.isBuildable();
        assertEquals(expected, actual);
    }
}
