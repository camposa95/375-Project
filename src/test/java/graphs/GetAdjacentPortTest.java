package graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import gamedatastructures.GameType;

public class GetAdjacentPortTest {
    private static final int NUM_VERTEXES = 54;
    private static final String VERTEX_TO_PORT_LAYOUT_FILE = "src/main/java/graphs/VertexToPortLayout.txt";

    @Test
    public void testGetAdjacentPortToNIn0and1() {
        VertexGraph vertexes = new VertexGraph();
        vertexes.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, GameType.Beginner);
        final int[] ids = {0, 1};

        for (int id: ids) {
            Vertex vertex = vertexes.getVertex(id);

            Port expectedPort = vertexes.getPort(0);
            Port actualPort = vertex.getAdjacentPort();
            assertEquals(expectedPort, actualPort);
        }
    }

    @Test
    public void testGetAdjacentPortToNIn3and4() {
        VertexGraph vertexes = new VertexGraph();
        vertexes.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, GameType.Beginner);
        final int[] ids = {3, 4};

        for (int id: ids) {
            Vertex vertex = vertexes.getVertex(id);

            Port expectedPort = vertexes.getPort(1);
            Port actualPort = vertex.getAdjacentPort();
            assertEquals(expectedPort, actualPort);
        }
    }

    @Test
    public void testGetAdjacentPortToNIn14and15() {
        VertexGraph vertexes = new VertexGraph();
        vertexes.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, GameType.Beginner);
        final int[] ids = {14, 15};

        for (int id: ids) {
            Vertex vertex = vertexes.getVertex(id);

            Port expectedPort = vertexes.getPort(2);
            Port actualPort = vertex.getAdjacentPort();
            assertEquals(expectedPort, actualPort);
        }
    }

    @Test
    public void testGetAdjacentPortToNIn26and37() {
        VertexGraph vertexes = new VertexGraph();
        vertexes.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, GameType.Beginner);
        final int[] ids = {26, 37};

        for (int id: ids) {
            Vertex vertex = vertexes.getVertex(id);

            Port expectedPort = vertexes.getPort(3);
            Port actualPort = vertex.getAdjacentPort();
            assertEquals(expectedPort, actualPort);
        }
    }

    @Test
    public void testGetAdjacentPortToNIn46and45() {
        VertexGraph vertexes = new VertexGraph();
        vertexes.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, GameType.Beginner);
        final int[] ids = {46, 45};

        for (int id: ids) {
            Vertex vertex = vertexes.getVertex(id);

            Port expectedPort = vertexes.getPort(4);
            Port actualPort = vertex.getAdjacentPort();
            assertEquals(expectedPort, actualPort);
        }
    }

    @Test
    public void testGetAdjacentPortToNIn51and50() {
        VertexGraph vertexes = new VertexGraph();
        vertexes.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, GameType.Beginner);
        final int[] ids = {51, 50};

        for (int id: ids) {
            Vertex vertex = vertexes.getVertex(id);

            Port expectedPort = vertexes.getPort(5);
            Port actualPort = vertex.getAdjacentPort();
            assertEquals(expectedPort, actualPort);
        }
    }

    @Test
    public void testGetAdjacentPortToNIn48and47() {
        VertexGraph vertexes = new VertexGraph();
        vertexes.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, GameType.Beginner);
        final int[] ids = {48, 47};

        for (int id: ids) {
            Vertex vertex = vertexes.getVertex(id);

            Port expectedPort = vertexes.getPort(6);
            Port actualPort = vertex.getAdjacentPort();
            assertEquals(expectedPort, actualPort);
        }
    }

    @Test
    public void testGetAdjacentPortToNIn38and28() {
        VertexGraph vertexes = new VertexGraph();
        vertexes.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, GameType.Beginner);
        final int[] ids = {38, 28};

        for (int id: ids) {
            Vertex vertex = vertexes.getVertex(id);

            Port expectedPort = vertexes.getPort(7);
            Port actualPort = vertex.getAdjacentPort();
            assertEquals(expectedPort, actualPort);
        }
    }

    @Test
    public void testGetAdjacentPortToNIn17and7() {
        VertexGraph vertexes = new VertexGraph();
        vertexes.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, GameType.Beginner);
        final int[] ids = {17, 7};

        for (int id: ids) {
            Vertex vertex = vertexes.getVertex(id);

            Port expectedPort = vertexes.getPort(8);
            Port actualPort = vertex.getAdjacentPort();
            assertEquals(expectedPort, actualPort);
        }
    }

    @Test
    public void testGetAdjacentPortToInvalidVertex() {
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

                NullPointerException exception = assertThrows(NullPointerException.class, () -> {
                    vertex.getAdjacentPort();
                });
                String expectedMessage = "This vertex has no port";
                String actualMessage = exception.getMessage();
                assertEquals(expectedMessage, actualMessage);
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
            testVertex.getAdjacentPort();
        });

        String expectedMessage = "Vertex-to-Port adjacency uninitialized";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
