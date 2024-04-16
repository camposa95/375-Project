package domain.graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import data.GameLoader;
import domain.game.GameType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RoadIsAdjacentToVertexTest {

    GameboardGraph gameboardGraph;

    @BeforeEach
    public void setup() {
        gameboardGraph = new GameboardGraph(GameType.Beginner);
        GameLoader.initializeGraphs(gameboardGraph);
    }

    @Test
    public void testIsAdjacentToIsNeighbor() {
        // get any road, it does not matter which
        Road testRoad = gameboardGraph.getRoad(0);

        // grab a vertex that is a neighbor to the Road according to the diagram
        Vertex testVertex = gameboardGraph.getVertex(0);

        boolean expected = true;
        boolean actual = testRoad.isAdjacentTo(testVertex);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAdjacentToNotNeighbor() {
        // get any road, it does not matter which
        Road testRoad = gameboardGraph.getRoad(0);

        // grab a vertex that is not a neighbor to the Road according to the diagram
        Vertex testVertex = gameboardGraph.getVertex(27); // not 27 was chosen arbitrarily

        boolean expected = false;
        boolean actual = testRoad.isAdjacentTo(testVertex);
        assertEquals(expected, actual);
    }
}
