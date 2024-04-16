package domain.graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import data.GameLoader;
import domain.game.GameType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class VertexToRoadAdjacencyTest {

    GameboardGraph gameboardGraph;

    @BeforeEach
    public void setup() {
        gameboardGraph = new GameboardGraph(GameType.Beginner);
        GameLoader.initializeGraphs(gameboardGraph);
    }

    @Test
    public void testGetAdjacentRoadsTo0() {
        Vertex vertex = gameboardGraph.getVertex(0);

        Road[] expectedRoads = {gameboardGraph.getRoad(0),
                                gameboardGraph.getRoad(6)};
        
        List<Road> actualRoads = vertex.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.size());
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn1and3and5() {
        final int[] ids = {1, 3, 5};

        for (int id: ids) {
            Vertex vertex = gameboardGraph.getVertex(id);

            Road[] expectedRoads = {gameboardGraph.getRoad(id - 1),
                                    gameboardGraph.getRoad(id)};

            List<Road> actualRoads = vertex.getAdjacentRoads();

            assertEquals(expectedRoads.length, actualRoads.size());
            for (int i = 0; i < expectedRoads.length; i++) {
                assertEquals(expectedRoads[i], actualRoads.get(i));
            }
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn2and4() {
        final int[] ids = {2, 4};

        int i = 0;
        for (int id: ids) {
            Vertex vertex = gameboardGraph.getVertex(id);

            Road[] expectedRoads = {gameboardGraph.getRoad(id - 1),
                                    gameboardGraph.getRoad(id),
                                    gameboardGraph.getRoad(id + (5 - i))};

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
        Vertex vertex = gameboardGraph.getVertex(6);

        Road[] expectedRoads = {gameboardGraph.getRoad(5),
                                gameboardGraph.getRoad(9)};

        List<Road> actualRoads = vertex.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.size());
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsTo7() {
        Vertex vertex = gameboardGraph.getVertex(7);

        Road[] expectedRoads = {gameboardGraph.getRoad(10),
                                gameboardGraph.getRoad(18)};

        List<Road> actualRoads = vertex.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.size());
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn8and10and12and14() {
        final int[] ids = {8, 10, 12, 14};

        int i = 0;
        for (int id: ids) {
            Vertex vertex = gameboardGraph.getVertex(id);

            Road[] expectedRoads = {gameboardGraph.getRoad(id - (2 + i)),
                                    gameboardGraph.getRoad(id + 2),
                                    gameboardGraph.getRoad(id + 3)};

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
        final int[] ids = {9, 11, 13};

        int i = 0;
        for (int id: ids) {
            Vertex vertex = gameboardGraph.getVertex(id);

            Road[] expectedRoads = {gameboardGraph.getRoad(id + 2),
                                    gameboardGraph.getRoad(id + 3),
                                    gameboardGraph.getRoad(id + (10 - i))};

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
        Vertex vertex = gameboardGraph.getVertex(15);

        Road[] expectedRoads = {gameboardGraph.getRoad(17),
                                gameboardGraph.getRoad(22)};

        List<Road> actualRoads = vertex.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.size());
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsTo16() {
        Vertex vertex = gameboardGraph.getVertex(16);

        Road[] expectedRoads = {gameboardGraph.getRoad(23),
                                gameboardGraph.getRoad(33)};

        List<Road> actualRoads = vertex.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.size());
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn17and19and21and23and25() {
        final int[] ids = {17, 19, 21, 23, 25};

        int i = 0;
        for (int id: ids) {
            Vertex vertex = gameboardGraph.getVertex(id);

            Road[] expectedRoads = {gameboardGraph.getRoad(id + (1 - i)),
                                    gameboardGraph.getRoad(id + 6),
                                    gameboardGraph.getRoad(id + 7)};

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
        final int[] ids = {18, 20, 22, 24};

        int i = 0;
        for (int id: ids) {
            Vertex vertex = gameboardGraph.getVertex(id);

            Road[] expectedRoads = {gameboardGraph.getRoad(id + 6),
                                    gameboardGraph.getRoad(id + 7),
                                    gameboardGraph.getRoad(id + (16 - i))};

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
        Vertex vertex = gameboardGraph.getVertex(26);

        Road[] expectedRoads = {gameboardGraph.getRoad(32),
                                gameboardGraph.getRoad(38)};

        List<Road> actualRoads = vertex.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.size());
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsTo27() {
        Vertex vertex = gameboardGraph.getVertex(27);

        Road[] expectedRoads = {gameboardGraph.getRoad(33),
                                gameboardGraph.getRoad(39)};

        List<Road> actualRoads = vertex.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.size());
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn28and30and32and34and36() {
        final int[] ids = {28, 30, 32, 34, 36};

        int i = 0;
        for (int id: ids) {
            Vertex vertex = gameboardGraph.getVertex(id);

            Road[] expectedRoads = {gameboardGraph.getRoad(id + 11),
                                    gameboardGraph.getRoad(id + 12),
                                    gameboardGraph.getRoad(id + (21 - i))};

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
        final int[] ids = {29, 31, 33, 35};

        int i = 0;
        for (int id: ids) {
            Vertex vertex = gameboardGraph.getVertex(id);

            Road[] expectedRoads = {gameboardGraph.getRoad(id + (5 - i)),
                                    gameboardGraph.getRoad(id + 11),
                                    gameboardGraph.getRoad(id + 12)};

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
        Vertex vertex = gameboardGraph.getVertex(37);

        Road[] expectedRoads = {gameboardGraph.getRoad(38),
                                gameboardGraph.getRoad(48)};

        List<Road> actualRoads = vertex.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.size());
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsTo38() {
        Vertex vertex = gameboardGraph.getVertex(38);

        Road[] expectedRoads = {gameboardGraph.getRoad(49),
                                gameboardGraph.getRoad(54)};

        List<Road> actualRoads = vertex.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.size());
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn39and41and43and45() {
        final int[] ids = {39, 41, 43, 45};

        int i = 0;
        for (int id: ids) {
            Vertex vertex = gameboardGraph.getVertex(id);

            Road[] expectedRoads = {gameboardGraph.getRoad(id + 15),
                                    gameboardGraph.getRoad(id + 16),
                                    gameboardGraph.getRoad(id + (23 - i))};

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
        final int[] ids = {40, 42, 44};

        int i = 0;
        for (int id: ids) {
            Vertex vertex = gameboardGraph.getVertex(id);

            Road[] expectedRoads = {gameboardGraph.getRoad(id + (10 - i)),
                                    gameboardGraph.getRoad(id + 15),
                                    gameboardGraph.getRoad(id + 16)};

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
        Vertex vertex = gameboardGraph.getVertex(46);

        Road[] expectedRoads = {gameboardGraph.getRoad(53),
                                gameboardGraph.getRoad(61)};

        List<Road> actualRoads = vertex.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.size());
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsTo47() {
        Vertex vertex = gameboardGraph.getVertex(47);

        Road[] expectedRoads = {gameboardGraph.getRoad(62),
                                gameboardGraph.getRoad(66)};

        List<Road> actualRoads = vertex.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.size());
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn48and50and52() {
        final int[] ids = {48, 50, 52};

        for (int id: ids) {
            Vertex vertex = gameboardGraph.getVertex(id);

            Road[] expectedRoads = {gameboardGraph.getRoad(id + 18),
                                    gameboardGraph.getRoad(id + 19)};

            List<Road> actualRoads = vertex.getAdjacentRoads();

            assertEquals(expectedRoads.length, actualRoads.size());
            for (int j = 0; j < expectedRoads.length; j++) {
                assertEquals(expectedRoads[j], actualRoads.get(j));
            }
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn49and51() {
        final int[] ids = {49, 51};

        int i = 0;
        for (int id: ids) {
            Vertex vertex = gameboardGraph.getVertex(id);

            Road[] expectedRoads = {gameboardGraph.getRoad(id + (14 - i)),
                                    gameboardGraph.getRoad(id + 18),
                                    gameboardGraph.getRoad(id + 19)};

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
        Vertex vertex = gameboardGraph.getVertex(53);

        Road[] expectedRoads = {gameboardGraph.getRoad(65),
                                gameboardGraph.getRoad(71)};

        List<Road> actualRoads = vertex.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.size());
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsNotInitialized() {
        GameboardGraph vertexes = new GameboardGraph(GameType.Beginner); // note not initialized
        Vertex testVertex = vertexes.getVertex(0); // get any road

        RuntimeException exception = assertThrows(RuntimeException.class, testVertex::getAdjacentRoads);

        String expectedMessage = "Vertex-to-Road adjacency uninitialized";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
