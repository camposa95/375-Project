package domain.graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import domain.game.GameType;
import data.GameLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class RoadToVertexAdjacencyTest {

    GameboardGraph gameboardGraph;

    @BeforeEach
    public void setup() {
        gameboardGraph = new GameboardGraph(GameType.Beginner);
        GameLoader.initializeGraphs(gameboardGraph);
    }

    @Test
    public void testGetAdjacentVertexesToNIn0Though5() {
        for (int id = 0; id <= 5; id++) {
            Road road = gameboardGraph.getRoad(id);

            Vertex[] expectedVertexes = {gameboardGraph.getVertex(id),
                                    gameboardGraph.getVertex(id + 1)};

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
            Road road = gameboardGraph.getRoad(id);

            Vertex[] expectedVertexes = {gameboardGraph.getVertex(id - (6 - i)),
                                    gameboardGraph.getVertex(id + (2 + i))};
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
            Road road = gameboardGraph.getRoad(id);

            Vertex[] expectedVertexes = {gameboardGraph.getVertex(id - 3),
                                    gameboardGraph.getVertex(id - 2)};
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
            Road road = gameboardGraph.getRoad(id);

            Vertex[] expectedVertexes = {gameboardGraph.getVertex(id - (11 - i)),
                                    gameboardGraph.getVertex(id + (-1 + i))};
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
            Road road = gameboardGraph.getRoad(id);

            Vertex[] expectedVertexes = {gameboardGraph.getVertex(id - 7),
                                    gameboardGraph.getVertex(id - 6)};
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
            Road road = gameboardGraph.getRoad(id);

            Vertex[] expectedVertexes = {gameboardGraph.getVertex(id - (17 - i)),
                                    gameboardGraph.getVertex(id - (6 - i))};
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
            Road road = gameboardGraph.getRoad(id);

            Vertex[] expectedVertexes = {gameboardGraph.getVertex(id - 12),
                                    gameboardGraph.getVertex(id - 11)};
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
            Road road = gameboardGraph.getRoad(id);

            Vertex[] expectedVertexes = {gameboardGraph.getVertex(id - (21 - i)),
                                    gameboardGraph.getVertex(id - (11 - i))};
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
            Road road = gameboardGraph.getRoad(id);

            Vertex[] expectedVertexes = {gameboardGraph.getVertex(id - 16),
                                    gameboardGraph.getVertex(id - 15)};
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
            Road road = gameboardGraph.getRoad(id);

            Vertex[] expectedVertexes = {gameboardGraph.getVertex(id - (23 - i)),
                                    gameboardGraph.getVertex(id - (15 - i))};
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
            Road road = gameboardGraph.getRoad(id);

            Vertex[] expectedVertexes = {gameboardGraph.getVertex(id - 19),
                                    gameboardGraph.getVertex(id - 18)};
            List<Vertex> actualVertexes = road.getAdjacentVertexes();

            assertEquals(expectedVertexes.length, actualVertexes.size());
            for (int j = 0; j < expectedVertexes.length; j++) {
                assertEquals(expectedVertexes[j], actualVertexes.get(j));
            }
        }
    }

    @Test
    public void testGetAdjacentRoadsNotInitialized() {
        gameboardGraph = new GameboardGraph(GameType.Beginner);
        Road testRoad = gameboardGraph.getRoad(0); // get any road

        RuntimeException exception = assertThrows(RuntimeException.class, testRoad::getAdjacentVertexes);

        String expectedMessage = "Road-to-Vertex adjacency uninitialized";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
