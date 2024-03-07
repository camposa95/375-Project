package graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import gamedatastructures.GameType;

public class HasAdjacentPortTest {
    private static final int NUM_VERTEXES = 54;
    private static final String VERTEX_TO_PORT_LAYOUT_FILE = "src/main/java/graphs/VertexToPortLayout.txt";

    @Test
    public void testHasAdjacentPortWhereTrue() {
        VertexGraph vertexes = new VertexGraph();
        vertexes.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, GameType.Beginner);
        final int[] ids = {0, 1, 3, 4, 14, 15, 26, 37, 46, 45, 51, 50, 48, 47, 38, 28, 17, 7};

        for (int id: ids) {
            Vertex vertex = vertexes.getVertex(id);

            Boolean expected = true;
            boolean actual = vertex.hasPort();

            assertEquals(expected, actual);
        }
    }

    @Test
    public void testHasAdjacentPortWhereFalse() {
        VertexGraph vertexes = new VertexGraph();
        vertexes.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, GameType.Beginner);
        final int[] ids = {0, 1, 3, 4, 14, 15, 26, 37, 46, 45, 51, 50, 48, 47, 38, 28, 17, 7};

        HashSet<Integer> vertexesWithPorts = new HashSet<>();
        for (int i = 0; i < ids.length; i++) {
            vertexesWithPorts.add(ids[i]);
        }

        for (int id = 0; id < NUM_VERTEXES; id++) {
            if (!vertexesWithPorts.contains(id)) {
                Vertex vertex = vertexes.getVertex(id);

                Boolean expected = false;
                boolean actual = vertex.hasPort();

                assertEquals(expected, actual);
            }
        }
    }

    @Test
    public void testGetAdjacentPortInitializedIncorrectly() {
        VertexGraph vertexes = new VertexGraph();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            vertexes.initializeVertexToPortAdjacency("blah blah blah", GameType.Beginner);
        });

        String expectedMessage = "Incorrect filename/path when initialized";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetAdjacentPortNotInitialized() {
        VertexGraph vertexes = new VertexGraph();
        Vertex testVertex = vertexes.getVertex(0); // get any vertex

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            testVertex.hasPort();
        });

        String expectedMessage = "Vertex-to-Port adjacency uninitialized";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
