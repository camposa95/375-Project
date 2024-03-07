package graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class RoadToVertexAdjacencyTest {
    
    private static final String ROAD_TO_VERTEX_LAYOUT_FILE = "src/main/java/graphs/RoadToVertexLayout.txt";

    @Test
    public void testGetAdjacentVertexesToNIn0Though5() {
        VertexGraph vertexes = new VertexGraph();
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToVertexAdjacency(vertexes, ROAD_TO_VERTEX_LAYOUT_FILE);

        for (int id = 0; id <= 5; id++) {
            Road road = roads.getRoad(id);

            Vertex[] expectedVertexes = {vertexes.getVertex(id),
                                    vertexes.getVertex(id + 1)};
            Vertex[] actualVertexes = road.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.length);
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes[j]);
            }
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn6Though9() {
        VertexGraph vertexes = new VertexGraph();
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToVertexAdjacency(vertexes, ROAD_TO_VERTEX_LAYOUT_FILE);

        int i = 0;
        for (int id = 6; id <= 9; id++) {
            Road road = roads.getRoad(id);

            Vertex[] expectedVertexes = {vertexes.getVertex(id - (6 - i)),
                                    vertexes.getVertex(id + (2 + i))};
            Vertex[] actualVertexes = road.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.length);
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes[j]);
            }
            i++;
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn10Though17() {
        VertexGraph vertexes = new VertexGraph();
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToVertexAdjacency(vertexes, ROAD_TO_VERTEX_LAYOUT_FILE);

        for (int id = 10; id <= 17; id++) {
            Road road = roads.getRoad(id);

            Vertex[] expectedVertexes = {vertexes.getVertex(id - 3),
                                    vertexes.getVertex(id - 2)};
            Vertex[] actualVertexes = road.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.length);
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes[j]);
            }
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn18Though22() {
        VertexGraph vertexes = new VertexGraph();
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToVertexAdjacency(vertexes, ROAD_TO_VERTEX_LAYOUT_FILE);

        int i = 0;
        for (int id = 18; id <= 22; id++) {
            Road road = roads.getRoad(id);

            Vertex[] expectedVertexes = {vertexes.getVertex(id - (11 - i)),
                                    vertexes.getVertex(id + (-1 + i))};
            Vertex[] actualVertexes = road.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.length);
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes[j]);
            }
            i++;
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn23Though32() {
        VertexGraph vertexes = new VertexGraph();
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToVertexAdjacency(vertexes, ROAD_TO_VERTEX_LAYOUT_FILE);

        for (int id = 23; id <= 32; id++) {
            Road road = roads.getRoad(id);

            Vertex[] expectedVertexes = {vertexes.getVertex(id - 7),
                                    vertexes.getVertex(id - 6)};
            Vertex[] actualVertexes = road.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.length);
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes[j]);
            }
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn33Though38() {
        VertexGraph vertexes = new VertexGraph();
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToVertexAdjacency(vertexes, ROAD_TO_VERTEX_LAYOUT_FILE);

        int i = 0;
        for (int id = 33; id <= 38; id++) {
            Road road = roads.getRoad(id);

            Vertex[] expectedVertexes = {vertexes.getVertex(id - (17 - i)),
                                    vertexes.getVertex(id - (6 - i))};
            Vertex[] actualVertexes = road.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.length);
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes[j]);
            }
            i++;
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn39Though48() {
        VertexGraph vertexes = new VertexGraph();
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToVertexAdjacency(vertexes, ROAD_TO_VERTEX_LAYOUT_FILE);

        for (int id = 39; id <= 48; id++) {
            Road road = roads.getRoad(id);

            Vertex[] expectedVertexes = {vertexes.getVertex(id - 12),
                                    vertexes.getVertex(id - 11)};
            Vertex[] actualVertexes = road.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.length);
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes[j]);
            }
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn49Though53() {
        VertexGraph vertexes = new VertexGraph();
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToVertexAdjacency(vertexes, ROAD_TO_VERTEX_LAYOUT_FILE);

        int i = 0;
        for (int id = 49; id <= 53; id++) {
            Road road = roads.getRoad(id);

            Vertex[] expectedVertexes = {vertexes.getVertex(id - (21 - i)),
                                    vertexes.getVertex(id - (11 - i))};
            Vertex[] actualVertexes = road.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.length);
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes[j]);
            }
            i++;
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn54Though61() {
        VertexGraph vertexes = new VertexGraph();
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToVertexAdjacency(vertexes, ROAD_TO_VERTEX_LAYOUT_FILE);

        for (int id = 54; id <= 61; id++) {
            Road road = roads.getRoad(id);

            Vertex[] expectedVertexes = {vertexes.getVertex(id - 16),
                                    vertexes.getVertex(id - 15)};
            Vertex[] actualVertexes = road.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.length);
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes[j]);
            }
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn62Though65() {
        VertexGraph vertexes = new VertexGraph();
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToVertexAdjacency(vertexes, ROAD_TO_VERTEX_LAYOUT_FILE);

        int i = 0;
        for (int id = 62; id <= 65; id++) {
            Road road = roads.getRoad(id);

            Vertex[] expectedVertexes = {vertexes.getVertex(id - (23 - i)),
                                    vertexes.getVertex(id - (15 - i))};
            Vertex[] actualVertexes = road.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.length);
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes[j]);
            }
            i++;
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn66Though71() {
        VertexGraph vertexes = new VertexGraph();
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToVertexAdjacency(vertexes, ROAD_TO_VERTEX_LAYOUT_FILE);

        for (int id = 66; id <= 71; id++) {
            Road road = roads.getRoad(id);

            Vertex[] expectedVertexes = {vertexes.getVertex(id - 19),
                                    vertexes.getVertex(id - 18)};
            Vertex[] actualVertexes = road.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.length);
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes[j]);
            }
        }
    }

    @Test
    public void testGetAdjacentRoadsInitializedIncorrectly() {
        RoadGraph roads = new RoadGraph();
        VertexGraph vertexes = new VertexGraph();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            roads.initializeRoadToVertexAdjacency(vertexes, "blah blah blah");
        });

        String expectedMessage = "Incorrect filename/path when initialized";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetAdjacentRoadsNotInitialized() {
        RoadGraph roads = new RoadGraph();
        Road testRoad = roads.getRoad(0); // get any road

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            testRoad.getAdjacentVertexes();
        });

        String expectedMessage = "Road-to-Vertex adjacency uninitialized";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
