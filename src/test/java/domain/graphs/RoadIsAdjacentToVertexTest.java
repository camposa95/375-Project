package domain.graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import data.GameLoader;
import domain.game.GameType;
import domain.graphs.Road;
import domain.graphs.RoadGraph;
import domain.graphs.Vertex;
import domain.graphs.VertexGraph;
import org.junit.jupiter.api.Test;

public class RoadIsAdjacentToVertexTest {

    @Test
    public void testIsAdjacentToIsNeighbor() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        // get any road, it does not matter which
        Road testRoad = roads.getRoad(0);

        // grab a vertex that is a neighbor to the Road according to the diagram
        Vertex testVertex = vertexes.getVertex(0);

        boolean expected = true;
        boolean actual = testRoad.isAdjacentTo(testVertex);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAdjacentToNotNeighbor() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        // get any road, it does not matter which
        Road testRoad = roads.getRoad(0);

        // grab a vertex that is not a neighbor to the Road according to the diagram
        Vertex testVertex = vertexes.getVertex(27); // not 27 was chosen arbitrarily

        boolean expected = false;
        boolean actual = testRoad.isAdjacentTo(testVertex);
        assertEquals(expected, actual);
    }
}
