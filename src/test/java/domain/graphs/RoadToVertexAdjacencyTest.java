package domain.graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import domain.game.GameType;
import data.GameLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class RoadToVertexAdjacencyTest {

    VertexGraph vertexes;
    RoadGraph roads;

    @BeforeEach
    public void setup() {
        vertexes = new VertexGraph(GameType.Beginner);
        roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
    }

    @Test
    public void testGetAdjacentVertexesToNIn0Though5() {
        for (int id = 0; id <= 5; id++) {
            Road road = roads.getRoad(id);

            Vertex[] expectedVertexes = {vertexes.getVertex(id),
                                    vertexes.getVertex(id + 1)};

            List<Vertex> actualVertexes = road.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.size());
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes.get(j));
            }
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn6Though9() {
        int i = 0;
        for (int id = 6; id <= 9; id++) {
            Road road = roads.getRoad(id);

            Vertex[] expectedVertexes = {vertexes.getVertex(id - (6 - i)),
                                    vertexes.getVertex(id + (2 + i))};
            List<Vertex> actualVertexes = road.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.size());
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes.get(j));
            }
            i++;
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn10Though17() {
        for (int id = 10; id <= 17; id++) {
            Road road = roads.getRoad(id);

            Vertex[] expectedVertexes = {vertexes.getVertex(id - 3),
                                    vertexes.getVertex(id - 2)};
            List<Vertex> actualVertexes = road.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.size());
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes.get(j));
            }
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn18Though22() {
        int i = 0;
        for (int id = 18; id <= 22; id++) {
            Road road = roads.getRoad(id);

            Vertex[] expectedVertexes = {vertexes.getVertex(id - (11 - i)),
                                    vertexes.getVertex(id + (-1 + i))};
            List<Vertex> actualVertexes = road.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.size());
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes.get(j));
            }
            i++;
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn23Though32() {
        for (int id = 23; id <= 32; id++) {
            Road road = roads.getRoad(id);

            Vertex[] expectedVertexes = {vertexes.getVertex(id - 7),
                                    vertexes.getVertex(id - 6)};
            List<Vertex> actualVertexes = road.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.size());
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes.get(j));
            }
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn33Though38() {
        int i = 0;
        for (int id = 33; id <= 38; id++) {
            Road road = roads.getRoad(id);

            Vertex[] expectedVertexes = {vertexes.getVertex(id - (17 - i)),
                                    vertexes.getVertex(id - (6 - i))};
            List<Vertex> actualVertexes = road.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.size());
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes.get(j));
            }
            i++;
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn39Though48() {
        for (int id = 39; id <= 48; id++) {
            Road road = roads.getRoad(id);

            Vertex[] expectedVertexes = {vertexes.getVertex(id - 12),
                                    vertexes.getVertex(id - 11)};
            List<Vertex> actualVertexes = road.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.size());
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes.get(j));
            }
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn49Though53() {
        int i = 0;
        for (int id = 49; id <= 53; id++) {
            Road road = roads.getRoad(id);

            Vertex[] expectedVertexes = {vertexes.getVertex(id - (21 - i)),
                                    vertexes.getVertex(id - (11 - i))};
            List<Vertex> actualVertexes = road.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.size());
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes.get(j));
            }
            i++;
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn54Though61() {
        for (int id = 54; id <= 61; id++) {
            Road road = roads.getRoad(id);

            Vertex[] expectedVertexes = {vertexes.getVertex(id - 16),
                                    vertexes.getVertex(id - 15)};
            List<Vertex> actualVertexes = road.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.size());
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes.get(j));
            }
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn62Though65() {
        int i = 0;
        for (int id = 62; id <= 65; id++) {
            Road road = roads.getRoad(id);

            Vertex[] expectedVertexes = {vertexes.getVertex(id - (23 - i)),
                                    vertexes.getVertex(id - (15 - i))};
            List<Vertex> actualVertexes = road.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.size());
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes.get(j));
            }
            i++;
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn66Though71() {
        for (int id = 66; id <= 71; id++) {
            Road road = roads.getRoad(id);

            Vertex[] expectedVertexes = {vertexes.getVertex(id - 19),
                                    vertexes.getVertex(id - 18)};
            List<Vertex> actualVertexes = road.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.size());
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes.get(j));
            }
        }
    }

    @Test
    public void testGetAdjacentRoadsNotInitialized() {
        RoadGraph roads = new RoadGraph();
        Road testRoad = roads.getRoad(0); // get any road

        RuntimeException exception = assertThrows(RuntimeException.class, testRoad::getAdjacentVertexes);

        String expectedMessage = "Road-to-Vertex adjacency uninitialized";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
