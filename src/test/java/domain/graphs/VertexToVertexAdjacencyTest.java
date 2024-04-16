package domain.graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import data.GameLoader;
import domain.game.GameType;

import java.util.List;

public class VertexToVertexAdjacencyTest {

    GameboardGraph gameboardGraph;

    @BeforeEach
    public void setup() {
        gameboardGraph = new GameboardGraph(GameType.Beginner);
        GameLoader.initializeGraphs(gameboardGraph);
    }

    @Test
    public void testGetAdjacentVertexesTo0() {
        Vertex vertex = gameboardGraph.getVertex(0);

        Vertex[] expectedVertexes = {gameboardGraph.getVertex(1),
                                gameboardGraph.getVertex(8)};
        List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

        assertEquals(expectedVertexes.length, actualVertexes.size());
        for (int i = 0; i < expectedVertexes.length; i++) {
            assertEquals(expectedVertexes[i], actualVertexes.get(i));
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn1and3and5() {
        final int[] ids = {1, 3, 5};

        for (int id: ids) {
            Vertex vertex = gameboardGraph.getVertex(id);

            Vertex[] expectedVertexes = {gameboardGraph.getVertex(id - 1),
                                    gameboardGraph.getVertex(id + 1)};
            List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.size());
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes.get(j));
            }
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn2and4() {
        final int[] ids = {2, 4};

        for (int id: ids) {
            Vertex vertex = gameboardGraph.getVertex(id);

            Vertex[] expectedVertexes = {gameboardGraph.getVertex(id - 1),
                                    gameboardGraph.getVertex(id + 1),
                                    gameboardGraph.getVertex(id + 8)};
            List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.size());
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes.get(j));
            }
        }
    }

    @Test
    public void testGetAdjacentVertexesTo6() {
        Vertex vertex = gameboardGraph.getVertex(6);

        Vertex[] expectedVertexes = {gameboardGraph.getVertex(5),
                                gameboardGraph.getVertex(14)};
        List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

        assertEquals(expectedVertexes.length, actualVertexes.size());
        for (int i = 0; i < expectedVertexes.length; i++) {
            assertEquals(expectedVertexes[i], actualVertexes.get(i));
        }
    }

    @Test
    public void testGetAdjacentVertexesTo7() {
        Vertex vertex = gameboardGraph.getVertex(7);

        Vertex[] expectedVertexes = {gameboardGraph.getVertex(8),
                                gameboardGraph.getVertex(17)};
        List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

        assertEquals(expectedVertexes.length, actualVertexes.size());
        for (int i = 0; i < expectedVertexes.length; i++) {
            assertEquals(expectedVertexes[i], actualVertexes.get(i));
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn8and10and12and14() {
        final int[] ids = {8, 10, 12, 14};

        for (int id: ids) {
            Vertex vertex = gameboardGraph.getVertex(id);

            Vertex[] expectedVertexes = {gameboardGraph.getVertex(id - 8),
                                    gameboardGraph.getVertex(id - 1),
                                    gameboardGraph.getVertex(id + 1)};
            List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.size());
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes.get(j));
            }
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn9and11and13() {
        final int[] ids = {9, 11, 13};

        for (int id: ids) {
            Vertex vertex = gameboardGraph.getVertex(id);

            Vertex[] expectedVertexes = {gameboardGraph.getVertex(id - 1),
                                    gameboardGraph.getVertex(id + 1),
                                    gameboardGraph.getVertex(id + 10)};
            List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.size());
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes.get(j));
            }
        }
    }

    @Test
    public void testGetAdjacentVertexesTo15() {
        Vertex vertex = gameboardGraph.getVertex(15);

        Vertex[] expectedVertexes = {gameboardGraph.getVertex(14),
                                gameboardGraph.getVertex(25)};
        List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

        assertEquals(expectedVertexes.length, actualVertexes.size());
        for (int i = 0; i < expectedVertexes.length; i++) {
            assertEquals(expectedVertexes[i], actualVertexes.get(i));
        }
    }

    @Test
    public void testGetAdjacentVertexesTo16() {
        Vertex vertex = gameboardGraph.getVertex(16);

        Vertex[] expectedVertexes = {gameboardGraph.getVertex(17),
                                gameboardGraph.getVertex(27)};
        List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

        assertEquals(expectedVertexes.length, actualVertexes.size());
        for (int i = 0; i < expectedVertexes.length; i++) {
            assertEquals(expectedVertexes[i], actualVertexes.get(i));
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn17and19and21and23and25() {
        final int[] ids = {17, 19, 21, 23, 25};

        for (int id: ids) {
            Vertex vertex = gameboardGraph.getVertex(id);

            Vertex[] expectedVertexes = {gameboardGraph.getVertex(id - 10),
                                    gameboardGraph.getVertex(id - 1),
                                    gameboardGraph.getVertex(id + 1)};
            List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.size());
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes.get(j));
            }
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn18and20and22and24() {
        final int[] ids = {18, 20, 22, 24};

        for (int id: ids) {
            Vertex vertex = gameboardGraph.getVertex(id);

            Vertex[] expectedVertexes = {gameboardGraph.getVertex(id - 1),
                                    gameboardGraph.getVertex(id + 1),
                                    gameboardGraph.getVertex(id + 11)};
            List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.size());
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes.get(j));
            }
        }
    }

    @Test
    public void testGetAdjacentVertexesTo26() {
        Vertex vertex = gameboardGraph.getVertex(26);

        Vertex[] expectedVertexes = {gameboardGraph.getVertex(25),
                                gameboardGraph.getVertex(37)};
        List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

        assertEquals(expectedVertexes.length, actualVertexes.size());
        for (int i = 0; i < expectedVertexes.length; i++) {
            assertEquals(expectedVertexes[i], actualVertexes.get(i));
        }
    }

    @Test
    public void testGetAdjacentVertexesTo27() {
        Vertex vertex = gameboardGraph.getVertex(27);

        Vertex[] expectedVertexes = {gameboardGraph.getVertex(16),
                                gameboardGraph.getVertex(28)};
        List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

        assertEquals(expectedVertexes.length, actualVertexes.size());
        for (int i = 0; i < expectedVertexes.length; i++) {
            assertEquals(expectedVertexes[i], actualVertexes.get(i));
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn28and30and32and34and36() {
        final int[] ids = {28, 30, 32, 34, 36};

        for (int id: ids) {
            Vertex vertex = gameboardGraph.getVertex(id);

            Vertex[] expectedVertexes = {gameboardGraph.getVertex(id - 1),
                                    gameboardGraph.getVertex(id + 1),
                                    gameboardGraph.getVertex(id + 10)};
            List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.size());
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes.get(j));
            }
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn29and31and33and35() {
        final int[] ids = {29, 31, 33, 35};

        for (int id: ids) {
            Vertex vertex = gameboardGraph.getVertex(id);

            Vertex[] expectedVertexes = {gameboardGraph.getVertex(id - 11),
                                    gameboardGraph.getVertex(id - 1),
                                    gameboardGraph.getVertex(id + 1)};
            List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.size());
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes.get(j));
            }
        }
    }

    @Test
    public void testGetAdjacentVertexesTo37() {
        Vertex vertex = gameboardGraph.getVertex(37);

        Vertex[] expectedVertexes = {gameboardGraph.getVertex(26),
                                gameboardGraph.getVertex(36)};
        List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

        assertEquals(expectedVertexes.length, actualVertexes.size());
        for (int i = 0; i < expectedVertexes.length; i++) {
            assertEquals(expectedVertexes[i], actualVertexes.get(i));
        }
    }

    @Test
    public void testGetAdjacentVertexesTo38() {
        Vertex vertex = gameboardGraph.getVertex(38);

        Vertex[] expectedVertexes = {gameboardGraph.getVertex(28),
                                gameboardGraph.getVertex(39)};
        List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

        assertEquals(expectedVertexes.length, actualVertexes.size());
        for (int i = 0; i < expectedVertexes.length; i++) {
            assertEquals(expectedVertexes[i], actualVertexes.get(i));
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn39and41and43and45() {
        final int[] ids = {39, 41, 43, 45};

        for (int id: ids) {
            Vertex vertex = gameboardGraph.getVertex(id);

            Vertex[] expectedVertexes = {gameboardGraph.getVertex(id - 1),
                                    gameboardGraph.getVertex(id + 1),
                                    gameboardGraph.getVertex(id + 8)};
            List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.size());
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes.get(j));
            }
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn40and42and44() {
        final int[] ids = {40, 42, 44};

        for (int id: ids) {
            Vertex vertex = gameboardGraph.getVertex(id);

            Vertex[] expectedVertexes = {gameboardGraph.getVertex(id - 10),
                                    gameboardGraph.getVertex(id - 1),
                                    gameboardGraph.getVertex(id + 1)};
            List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.size());
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes.get(j));
            }
        }
    }

    @Test
    public void testGetAdjacentVertexesTo46() {
        Vertex vertex = gameboardGraph.getVertex(46);

        Vertex[] expectedVertexes = {gameboardGraph.getVertex(36),
                                gameboardGraph.getVertex(45)};
        List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

        assertEquals(expectedVertexes.length, actualVertexes.size());
        for (int i = 0; i < expectedVertexes.length; i++) {
            assertEquals(expectedVertexes[i], actualVertexes.get(i));
        }
    }

    @Test
    public void testGetAdjacentVertexesTo47() {
        Vertex vertex = gameboardGraph.getVertex(47);

        Vertex[] expectedVertexes = {gameboardGraph.getVertex(39),
                                gameboardGraph.getVertex(48)};
        List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

        assertEquals(expectedVertexes.length, actualVertexes.size());
        for (int i = 0; i < expectedVertexes.length; i++) {
            assertEquals(expectedVertexes[i], actualVertexes.get(i));
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn48and50and52() {
        final int[] ids = {48, 50, 52};

        for (int id: ids) {
            Vertex vertex = gameboardGraph.getVertex(id);

            Vertex[] expectedVertexes = {gameboardGraph.getVertex(id - 1),
                                    gameboardGraph.getVertex(id + 1)};
            List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.size());
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes.get(j));
            }
        }
    }

    @Test
    public void testGetAdjacentVertexesToNIn49and51() {
        final int[] ids = {49, 51};

        for (int id: ids) {
            Vertex vertex = gameboardGraph.getVertex(id);

            Vertex[] expectedVertexes = {gameboardGraph.getVertex(id - 8),
                                    gameboardGraph.getVertex(id - 1),
                                    gameboardGraph.getVertex(id + 1)};
            List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.size());
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes.get(j));
            }
        }
    }

    @Test
    public void testGetAdjacentVertexesTo53() {
        Vertex vertex = gameboardGraph.getVertex(53);

        Vertex[] expectedVertexes = {gameboardGraph.getVertex(45),
                                gameboardGraph.getVertex(52)};
        List<Vertex> actualVertexes = vertex.getAdjacentVertexes();

        assertEquals(expectedVertexes.length, actualVertexes.size());
        for (int i = 0; i < expectedVertexes.length; i++) {
            assertEquals(expectedVertexes[i], actualVertexes.get(i));
        }
    }

    @Test
    public void testGetAdjacentVertexesNotInitialized() {
        GameboardGraph vertexes = new GameboardGraph(GameType.Beginner);
        Vertex testVertex = vertexes.getVertex(0); // get any road

        RuntimeException exception = assertThrows(RuntimeException.class, testVertex::getAdjacentVertexes);

        String expectedMessage = "Vertex-to-Vertex adjacency uninitialized";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
