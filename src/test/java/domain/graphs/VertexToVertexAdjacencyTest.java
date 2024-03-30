package domain.graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import data.GameLoader;
import domain.game.GameType;

import java.util.List;

public class VertexToVertexAdjacencyTest {

    @Test
    public void testGetAdjacentVertexesTo0() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        Vertex vertex = vertexes.getVertex(0);

        Vertex[] expectedVertexes = {vertexes.getVertex(1),
                                vertexes.getVertex(8)};
        List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

        assertEquals(expectedVertexes.length, actualVertexes.size());
        for (int i = 0; i < expectedVertexes.length; i++) {
            assertEquals(expectedVertexes[i], actualVertexes.get(i));
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn1and3and5() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        final int[] ids = {1, 3, 5};

        for (int id: ids) {
            Vertex vertex = vertexes.getVertex(id);

            Vertex[] expectedVertexes = {vertexes.getVertex(id - 1),
                                    vertexes.getVertex(id + 1)};
            List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.size());
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes.get(j));
            }
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn2and4() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        final int[] ids = {2, 4};

        for (int id: ids) {
            Vertex vertex = vertexes.getVertex(id);

            Vertex[] expectedVertexes = {vertexes.getVertex(id - 1),
                                    vertexes.getVertex(id + 1),
                                    vertexes.getVertex(id + 8)};
            List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.size());
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes.get(j));
            }
        }
    }

    @Test
    public void testGetAdjacentVertexesTo6() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        Vertex vertex = vertexes.getVertex(6);

        Vertex[] expectedVertexes = {vertexes.getVertex(5),
                                vertexes.getVertex(14)};
        List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

        assertEquals(expectedVertexes.length, actualVertexes.size());
        for (int i = 0; i < expectedVertexes.length; i++) {
            assertEquals(expectedVertexes[i], actualVertexes.get(i));
        }
    }

    @Test
    public void testGetAdjacentVertexesTo7() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        Vertex vertex = vertexes.getVertex(7);

        Vertex[] expectedVertexes = {vertexes.getVertex(8),
                                vertexes.getVertex(17)};
        List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

        assertEquals(expectedVertexes.length, actualVertexes.size());
        for (int i = 0; i < expectedVertexes.length; i++) {
            assertEquals(expectedVertexes[i], actualVertexes.get(i));
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn8and10and12and14() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        final int[] ids = {8, 10, 12, 14};

        for (int id: ids) {
            Vertex vertex = vertexes.getVertex(id);

            Vertex[] expectedVertexes = {vertexes.getVertex(id - 8),
                                    vertexes.getVertex(id - 1),
                                    vertexes.getVertex(id + 1)};
            List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.size());
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes.get(j));
            }
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn9and11and13() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        final int[] ids = {9, 11, 13};

        for (int id: ids) {
            Vertex vertex = vertexes.getVertex(id);

            Vertex[] expectedVertexes = {vertexes.getVertex(id - 1),
                                    vertexes.getVertex(id + 1),
                                    vertexes.getVertex(id + 10)};
            List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.size());
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes.get(j));
            }
        }
    }

    @Test
    public void testGetAdjacentVertexesTo15() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        Vertex vertex = vertexes.getVertex(15);

        Vertex[] expectedVertexes = {vertexes.getVertex(14),
                                vertexes.getVertex(25)};
        List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

        assertEquals(expectedVertexes.length, actualVertexes.size());
        for (int i = 0; i < expectedVertexes.length; i++) {
            assertEquals(expectedVertexes[i], actualVertexes.get(i));
        }
    }

    @Test
    public void testGetAdjacentVertexesTo16() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        Vertex vertex = vertexes.getVertex(16);

        Vertex[] expectedVertexes = {vertexes.getVertex(17),
                                vertexes.getVertex(27)};
        List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

        assertEquals(expectedVertexes.length, actualVertexes.size());
        for (int i = 0; i < expectedVertexes.length; i++) {
            assertEquals(expectedVertexes[i], actualVertexes.get(i));
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn17and19and21and23and25() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        final int[] ids = {17, 19, 21, 23, 25};

        for (int id: ids) {
            Vertex vertex = vertexes.getVertex(id);

            Vertex[] expectedVertexes = {vertexes.getVertex(id - 10),
                                    vertexes.getVertex(id - 1),
                                    vertexes.getVertex(id + 1)};
            List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.size());
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes.get(j));
            }
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn18and20and22and24() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        final int[] ids = {18, 20, 22, 24};

        for (int id: ids) {
            Vertex vertex = vertexes.getVertex(id);

            Vertex[] expectedVertexes = {vertexes.getVertex(id - 1),
                                    vertexes.getVertex(id + 1),
                                    vertexes.getVertex(id + 11)};
            List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.size());
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes.get(j));
            }
        }
    }

    @Test
    public void testGetAdjacentVertexesTo26() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        Vertex vertex = vertexes.getVertex(26);

        Vertex[] expectedVertexes = {vertexes.getVertex(25),
                                vertexes.getVertex(37)};
        List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

        assertEquals(expectedVertexes.length, actualVertexes.size());
        for (int i = 0; i < expectedVertexes.length; i++) {
            assertEquals(expectedVertexes[i], actualVertexes.get(i));
        }
    }

    @Test
    public void testGetAdjacentVertexesTo27() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        Vertex vertex = vertexes.getVertex(27);

        Vertex[] expectedVertexes = {vertexes.getVertex(16),
                                vertexes.getVertex(28)};
        List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

        assertEquals(expectedVertexes.length, actualVertexes.size());
        for (int i = 0; i < expectedVertexes.length; i++) {
            assertEquals(expectedVertexes[i], actualVertexes.get(i));
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn28and30and32and34and36() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        final int[] ids = {28, 30, 32, 34, 36};

        for (int id: ids) {
            Vertex vertex = vertexes.getVertex(id);

            Vertex[] expectedVertexes = {vertexes.getVertex(id - 1),
                                    vertexes.getVertex(id + 1),
                                    vertexes.getVertex(id + 10)};
            List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.size());
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes.get(j));
            }
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn29and31and33and35() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        final int[] ids = {29, 31, 33, 35};

        for (int id: ids) {
            Vertex vertex = vertexes.getVertex(id);

            Vertex[] expectedVertexes = {vertexes.getVertex(id - 11),
                                    vertexes.getVertex(id - 1),
                                    vertexes.getVertex(id + 1)};
            List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.size());
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes.get(j));
            }
        }
    }

    @Test
    public void testGetAdjacentVertexesTo37() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        Vertex vertex = vertexes.getVertex(37);

        Vertex[] expectedVertexes = {vertexes.getVertex(26),
                                vertexes.getVertex(36)};
        List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

        assertEquals(expectedVertexes.length, actualVertexes.size());
        for (int i = 0; i < expectedVertexes.length; i++) {
            assertEquals(expectedVertexes[i], actualVertexes.get(i));
        }
    }

    @Test
    public void testGetAdjacentVertexesTo38() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        Vertex vertex = vertexes.getVertex(38);

        Vertex[] expectedVertexes = {vertexes.getVertex(28),
                                vertexes.getVertex(39)};
        List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

        assertEquals(expectedVertexes.length, actualVertexes.size());
        for (int i = 0; i < expectedVertexes.length; i++) {
            assertEquals(expectedVertexes[i], actualVertexes.get(i));
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn39and41and43and45() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        final int[] ids = {39, 41, 43, 45};

        for (int id: ids) {
            Vertex vertex = vertexes.getVertex(id);

            Vertex[] expectedVertexes = {vertexes.getVertex(id - 1),
                                    vertexes.getVertex(id + 1),
                                    vertexes.getVertex(id + 8)};
            List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.size());
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes.get(j));
            }
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn40and42and44() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        final int[] ids = {40, 42, 44};

        for (int id: ids) {
            Vertex vertex = vertexes.getVertex(id);

            Vertex[] expectedVertexes = {vertexes.getVertex(id - 10),
                                    vertexes.getVertex(id - 1),
                                    vertexes.getVertex(id + 1)};
            List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.size());
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes.get(j));
            }
        }
    }

    @Test
    public void testGetAdjacentVertexesTo46() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        Vertex vertex = vertexes.getVertex(46);

        Vertex[] expectedVertexes = {vertexes.getVertex(36),
                                vertexes.getVertex(45)};
        List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

        assertEquals(expectedVertexes.length, actualVertexes.size());
        for (int i = 0; i < expectedVertexes.length; i++) {
            assertEquals(expectedVertexes[i], actualVertexes.get(i));
        }
    }

    @Test
    public void testGetAdjacentVertexesTo47() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        Vertex vertex = vertexes.getVertex(47);

        Vertex[] expectedVertexes = {vertexes.getVertex(39),
                                vertexes.getVertex(48)};
        List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

        assertEquals(expectedVertexes.length, actualVertexes.size());
        for (int i = 0; i < expectedVertexes.length; i++) {
            assertEquals(expectedVertexes[i], actualVertexes.get(i));
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn48and50and52() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        final int[] ids = {48, 50, 52};

        for (int id: ids) {
            Vertex vertex = vertexes.getVertex(id);

            Vertex[] expectedVertexes = {vertexes.getVertex(id - 1),
                                    vertexes.getVertex(id + 1)};
            List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.size());
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes.get(j));
            }
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn49and51() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        final int[] ids = {49, 51};

        for (int id: ids) {
            Vertex vertex = vertexes.getVertex(id);

            Vertex[] expectedVertexes = {vertexes.getVertex(id - 8),
                                    vertexes.getVertex(id - 1),
                                    vertexes.getVertex(id + 1)};
            List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.size());
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes.get(j));
            }
        }
    }

    @Test
    public void testGetAdjacentVertexesTo53() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        Vertex vertex = vertexes.getVertex(53);

        Vertex[] expectedVertexes = {vertexes.getVertex(45),
                                vertexes.getVertex(52)};
        List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

        assertEquals(expectedVertexes.length, actualVertexes.size());
        for (int i = 0; i < expectedVertexes.length; i++) {
            assertEquals(expectedVertexes[i], actualVertexes.get(i));
        }
    }

    @Test
    public void testGetAdjacentVertexesNotInitialized() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        Vertex testVertex = vertexes.getVertex(0); // get any road

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            testVertex.getAdjacentVertexes();
        });

        String expectedMessage = "Vertex-to-Vertex adjacency uninitialized";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
