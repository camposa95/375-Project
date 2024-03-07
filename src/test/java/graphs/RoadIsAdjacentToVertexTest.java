package graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class RoadIsAdjacentToVertexTest {

    private static final String ROAD_TO_VERTEX_LAYOUT_FILE = "src/main/java/graphs/RoadToVertexLayout.txt";

    @Test
    public void testIsAdjacentToIsNeighbor() {
        RoadGraph roads = new RoadGraph();
        VertexGraph vertexes = new VertexGraph();
        roads.initializeRoadToVertexAdjacency(vertexes, ROAD_TO_VERTEX_LAYOUT_FILE);

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
        RoadGraph roads = new RoadGraph();
        VertexGraph vertexes = new VertexGraph();
        roads.initializeRoadToVertexAdjacency(vertexes, ROAD_TO_VERTEX_LAYOUT_FILE);

        // get any road, it does not matter which
        Road testRoad = roads.getRoad(0);

        // grab a vertex that is not a neighbor to the Road according to the diagram
        Vertex testVertex = vertexes.getVertex(27); // not 27 was chosen arbitrarily

        boolean expected = false;
        boolean actual = testRoad.isAdjacentTo(testVertex);
        assertEquals(expected, actual);
    }
}
