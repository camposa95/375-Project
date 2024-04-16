package domain.graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;

import data.GameLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.game.GameType;

public class HasAdjacentPortTest {
    private static final int NUM_VERTEXES = 54;

    GameboardGraph gameboardGraph;

    @BeforeEach
    public void setup() {
        gameboardGraph = new GameboardGraph(GameType.Beginner);
        GameLoader.initializeGraphs(gameboardGraph);
    }

    @Test
    public void testHasAdjacentPortWhereTrue() {
        final int[] ids = {0, 1, 3, 4, 14, 15, 26, 37, 46, 45, 51, 50, 48, 47, 38, 28, 17, 7};

        for (int id: ids) {
            Vertex vertex = gameboardGraph.getVertex(id);

            Boolean expected = true;
            boolean actual = vertex.hasPort();

            assertEquals(expected, actual);
        }
    }

    @Test
    public void testHasAdjacentPortWhereFalse() {
        final int[] ids = {0, 1, 3, 4, 14, 15, 26, 37, 46, 45, 51, 50, 48, 47, 38, 28, 17, 7};

        HashSet<Integer> vertexesWithPorts = new HashSet<>();
        for (int j : ids) {
            vertexesWithPorts.add(j);
        }

        for (int id = 0; id < NUM_VERTEXES; id++) {
            if (!vertexesWithPorts.contains(id)) {
                Vertex vertex = gameboardGraph.getVertex(id);

                Boolean expected = false;
                boolean actual = vertex.hasPort();

                assertEquals(expected, actual);
            }
        }
    }

    @Test
    public void testGetAdjacentPortNotInitialized() {
        GameboardGraph vertexes = new GameboardGraph(GameType.Beginner); // note not initialized
        Vertex testVertex = vertexes.getVertex(0); // get any vertex

        RuntimeException exception = assertThrows(RuntimeException.class, testVertex::hasPort);

        String expectedMessage = "Vertex-to-Port adjacency uninitialized";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
