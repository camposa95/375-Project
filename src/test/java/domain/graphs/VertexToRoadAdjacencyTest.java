package domain.graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import data.GameLoader;
import domain.game.GameType;
import domain.graphs.Road;
import domain.graphs.RoadGraph;
import domain.graphs.Vertex;
import domain.graphs.VertexGraph;
import org.junit.jupiter.api.Test;

import java.util.List;

public class VertexToRoadAdjacencyTest {

    @Test
    public void testGetAdjacentRoadsTo0() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        Vertex vertex = vertexes.getVertex(0);

        Road[] expectedRoads = {roads.getRoad(0),
                                roads.getRoad(6)};
        
        List<Road> actualRoads = vertex.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.size());
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn1and3and5() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        final int[] ids = {1, 3, 5};

        for (int id: ids) {
            Vertex vertex = vertexes.getVertex(id);

            Road[] expectedRoads = {roads.getRoad(id - 1),
                                    roads.getRoad(id)};

            List<Road> actualRoads = vertex.getAdjacentRoads();

            assertEquals(expectedRoads.length, actualRoads.size());
            for (int i = 0; i < expectedRoads.length; i++) {
                assertEquals(expectedRoads[i], actualRoads.get(i));
            }
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn2and4() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        final int[] ids = {2, 4};

        int i = 0;
        for (int id: ids) {
            Vertex vertex = vertexes.getVertex(id);

            Road[] expectedRoads = {roads.getRoad(id - 1),
                                    roads.getRoad(id),
                                    roads.getRoad(id + (5 - i))};

            List<Road> actualRoads = vertex.getAdjacentRoads();

            assertEquals(expectedRoads.length, actualRoads.size());
            for (int j = 0; j < expectedRoads.length; j++) {
                assertEquals(expectedRoads[j], actualRoads.get(j));
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsTo6() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        Vertex vertex = vertexes.getVertex(6);

        Road[] expectedRoads = {roads.getRoad(5),
                                roads.getRoad(9)};

        List<Road> actualRoads = vertex.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.size());
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsTo7() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        Vertex vertex = vertexes.getVertex(7);

        Road[] expectedRoads = {roads.getRoad(10),
                                roads.getRoad(18)};

        List<Road> actualRoads = vertex.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.size());
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn8and10and12and14() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        final int[] ids = {8, 10, 12, 14};

        int i = 0;
        for (int id: ids) {
            Vertex vertex = vertexes.getVertex(id);

            Road[] expectedRoads = {roads.getRoad(id - (2 + i)),
                                    roads.getRoad(id + 2),
                                    roads.getRoad(id + 3)};

            List<Road> actualRoads = vertex.getAdjacentRoads();

            assertEquals(expectedRoads.length, actualRoads.size());
            for (int j = 0; j < expectedRoads.length; j++) {
                assertEquals(expectedRoads[j], actualRoads.get(j));
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn9and11and13() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        final int[] ids = {9, 11, 13};

        int i = 0;
        for (int id: ids) {
            Vertex vertex = vertexes.getVertex(id);

            Road[] expectedRoads = {roads.getRoad(id + 2),
                                    roads.getRoad(id + 3),
                                    roads.getRoad(id + (10 - i))};

            List<Road> actualRoads = vertex.getAdjacentRoads();

            assertEquals(expectedRoads.length, actualRoads.size());
            for (int j = 0; j < expectedRoads.length; j++) {
                assertEquals(expectedRoads[j], actualRoads.get(j));
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsTo15() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        Vertex vertex = vertexes.getVertex(15);

        Road[] expectedRoads = {roads.getRoad(17),
                                roads.getRoad(22)};

        List<Road> actualRoads = vertex.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.size());
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsTo16() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        Vertex vertex = vertexes.getVertex(16);

        Road[] expectedRoads = {roads.getRoad(23),
                                roads.getRoad(33)};

        List<Road> actualRoads = vertex.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.size());
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn17and19and21and23and25() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        final int[] ids = {17, 19, 21, 23, 25};

        int i = 0;
        for (int id: ids) {
            Vertex vertex = vertexes.getVertex(id);

            Road[] expectedRoads = {roads.getRoad(id + (1 - i)),
                                    roads.getRoad(id + 6),
                                    roads.getRoad(id + 7)};

            List<Road> actualRoads = vertex.getAdjacentRoads();

            assertEquals(expectedRoads.length, actualRoads.size());
            for (int j = 0; j < expectedRoads.length; j++) {
                assertEquals(expectedRoads[j], actualRoads.get(j));
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn18and20and22and24() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        final int[] ids = {18, 20, 22, 24};

        int i = 0;
        for (int id: ids) {
            Vertex vertex = vertexes.getVertex(id);

            Road[] expectedRoads = {roads.getRoad(id + 6),
                                    roads.getRoad(id + 7),
                                    roads.getRoad(id + (16 - i))};

            List<Road> actualRoads = vertex.getAdjacentRoads();

            assertEquals(expectedRoads.length, actualRoads.size());
            for (int j = 0; j < expectedRoads.length; j++) {
                assertEquals(expectedRoads[j], actualRoads.get(j));
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsTo26() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        Vertex vertex = vertexes.getVertex(26);

        Road[] expectedRoads = {roads.getRoad(32),
                                roads.getRoad(38)};

        List<Road> actualRoads = vertex.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.size());
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsTo27() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        Vertex vertex = vertexes.getVertex(27);

        Road[] expectedRoads = {roads.getRoad(33),
                                roads.getRoad(39)};

        List<Road> actualRoads = vertex.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.size());
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn28and30and32and34and36() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        final int[] ids = {28, 30, 32, 34, 36};

        int i = 0;
        for (int id: ids) {
            Vertex vertex = vertexes.getVertex(id);

            Road[] expectedRoads = {roads.getRoad(id + 11),
                                    roads.getRoad(id + 12),
                                    roads.getRoad(id + (21 - i))};

            List<Road> actualRoads = vertex.getAdjacentRoads();

            assertEquals(expectedRoads.length, actualRoads.size());
            for (int j = 0; j < expectedRoads.length; j++) {
                assertEquals(expectedRoads[j], actualRoads.get(j));
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn29and31and33and35() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        final int[] ids = {29, 31, 33, 35};

        int i = 0;
        for (int id: ids) {
            Vertex vertex = vertexes.getVertex(id);

            Road[] expectedRoads = {roads.getRoad(id + (5 - i)),
                                    roads.getRoad(id + 11),
                                    roads.getRoad(id + 12)};

            List<Road> actualRoads = vertex.getAdjacentRoads();

            assertEquals(expectedRoads.length, actualRoads.size());
            for (int j = 0; j < expectedRoads.length; j++) {
                assertEquals(expectedRoads[j], actualRoads.get(j));
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsTo37() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        Vertex vertex = vertexes.getVertex(37);

        Road[] expectedRoads = {roads.getRoad(38),
                                roads.getRoad(48)};

        List<Road> actualRoads = vertex.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.size());
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsTo38() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        Vertex vertex = vertexes.getVertex(38);

        Road[] expectedRoads = {roads.getRoad(49),
                                roads.getRoad(54)};

        List<Road> actualRoads = vertex.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.size());
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn39and41and43and45() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        final int[] ids = {39, 41, 43, 45};

        int i = 0;
        for (int id: ids) {
            Vertex vertex = vertexes.getVertex(id);

            Road[] expectedRoads = {roads.getRoad(id + 15),
                                    roads.getRoad(id + 16),
                                    roads.getRoad(id + (23 - i))};

            List<Road> actualRoads = vertex.getAdjacentRoads();

            assertEquals(expectedRoads.length, actualRoads.size());
            for (int j = 0; j < expectedRoads.length; j++) {
                assertEquals(expectedRoads[j], actualRoads.get(j));
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn40and42and44() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        final int[] ids = {40, 42, 44};

        int i = 0;
        for (int id: ids) {
            Vertex vertex = vertexes.getVertex(id);

            Road[] expectedRoads = {roads.getRoad(id + (10 - i)),
                                    roads.getRoad(id + 15),
                                    roads.getRoad(id + 16)};

            List<Road> actualRoads = vertex.getAdjacentRoads();

            assertEquals(expectedRoads.length, actualRoads.size());
            for (int j = 0; j < expectedRoads.length; j++) {
                assertEquals(expectedRoads[j], actualRoads.get(j));
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsTo46() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        Vertex vertex = vertexes.getVertex(46);

        Road[] expectedRoads = {roads.getRoad(53),
                                roads.getRoad(61)};

        List<Road> actualRoads = vertex.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.size());
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsTo47() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        Vertex vertex = vertexes.getVertex(47);

        Road[] expectedRoads = {roads.getRoad(62),
                                roads.getRoad(66)};

        List<Road> actualRoads = vertex.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.size());
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn48and50and52() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        final int[] ids = {48, 50, 52};

        for (int id: ids) {
            Vertex vertex = vertexes.getVertex(id);

            Road[] expectedRoads = {roads.getRoad(id + 18),
                                    roads.getRoad(id + 19)};

            List<Road> actualRoads = vertex.getAdjacentRoads();

            assertEquals(expectedRoads.length, actualRoads.size());
            for (int j = 0; j < expectedRoads.length; j++) {
                assertEquals(expectedRoads[j], actualRoads.get(j));
            }
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn49and51() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        final int[] ids = {49, 51};

        int i = 0;
        for (int id: ids) {
            Vertex vertex = vertexes.getVertex(id);

            Road[] expectedRoads = {roads.getRoad(id + (14 - i)),
                                    roads.getRoad(id + 18),
                                    roads.getRoad(id + 19)};

            List<Road> actualRoads = vertex.getAdjacentRoads();

            assertEquals(expectedRoads.length, actualRoads.size());
            for (int j = 0; j < expectedRoads.length; j++) {
                assertEquals(expectedRoads[j], actualRoads.get(j));
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsTo53() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        Vertex vertex = vertexes.getVertex(53);

        Road[] expectedRoads = {roads.getRoad(65),
                                roads.getRoad(71)};

        List<Road> actualRoads = vertex.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.size());
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsNotInitialized() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        Vertex testVertex = vertexes.getVertex(0); // get any road

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            testVertex.getAdjacentRoads();
        });

        String expectedMessage = "Vertex-to-Road adjacency uninitialized";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
