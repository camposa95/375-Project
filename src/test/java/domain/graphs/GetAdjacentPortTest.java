package domain.graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;

import data.GameLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.game.GameType;

public class GetAdjacentPortTest {
    private static final int NUM_VERTEXES = 54;

    VertexGraph vertexes;
    RoadGraph roads;
    
    @BeforeEach
    public void setup() {
        vertexes = new VertexGraph(GameType.Beginner);
        roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
    }

    @Test
    public void testGetAdjacentPortToNIn0and1() {
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
        final int[] ids = {0, 1, 3, 4, 14, 15, 26, 37, 46, 45, 51, 50, 48, 47, 38, 28, 17, 7};

        HashSet<Integer> vertexesWithPorts = new HashSet<>();
        for (int j : ids) {
            vertexesWithPorts.add(j);
        }

        for (int id = 0; id < NUM_VERTEXES; id++) {
            if (!vertexesWithPorts.contains(id)) {
                Vertex vertex = vertexes.getVertex(id);

                NullPointerException exception = assertThrows(NullPointerException.class, vertex::getAdjacentPort);
                String expectedMessage = "This vertex has no port";
                String actualMessage = exception.getMessage();
                assertEquals(expectedMessage, actualMessage);
            }
        }
    }

    @Test
    public void testGetAdjacentPortNotInitialized() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        Vertex testVertex = vertexes.getVertex(0); // get any vertex

        RuntimeException exception = assertThrows(RuntimeException.class, testVertex::getAdjacentPort);

        String expectedMessage = "Vertex-to-Port adjacency uninitialized";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
