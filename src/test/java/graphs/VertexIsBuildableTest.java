package graphs;

import org.easymock.EasyMock;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import gamedatastructures.Player;

public class VertexIsBuildableTest {

    private static final String VERTEX_TO_VERTEX_LAYOUT_FILE = "src/main/java/graphs/VertexToVertexLayout.txt";

    @Test
    public void testIsBuildableNotOccupiedNoNeighbors() {
        VertexGraph vertexes = new VertexGraph();
        vertexes.initializeVertexToVertexAdjacency(VERTEX_TO_VERTEX_LAYOUT_FILE);

        // grab any vertex, it doesn't matter which one
        Vertex testVertex = vertexes.getVertex(0);

        boolean expected = true;
        boolean actual = testVertex.isbuildable();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsBuildableIsOccupiedHasNeighbors() {
        VertexGraph vertexes = new VertexGraph();
        vertexes.initializeVertexToVertexAdjacency(VERTEX_TO_VERTEX_LAYOUT_FILE);

        // grab any vertex, it doesn't matter which one
        Vertex testVertex = vertexes.getVertex(0);

        // make the vertex occupied
        Player mockPlayer = EasyMock.createMock(Player.class);
        testVertex.setOwner(mockPlayer);

        // make the vertex have neighboring settlments
        Vertex neighborVertex = vertexes.getVertex(1); // 1 is adjacent to 0 by the game diagram
        neighborVertex.setOwner(mockPlayer); // this simulates trying to build next to your own settlement
                                            // which has the same functionality as a opponents settlements
        
        
        EasyMock.replay(mockPlayer); // Note: Nothing should be called by player

        boolean expected = false;
        boolean actual = testVertex.isbuildable();
        assertEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testIsBuildableNotOccupiedHasNeighbors() {
        VertexGraph vertexes = new VertexGraph();
        vertexes.initializeVertexToVertexAdjacency(VERTEX_TO_VERTEX_LAYOUT_FILE);

        // grab any vertex, it doesn't matter which one
        Vertex testVertex = vertexes.getVertex(0);

        // make the vertex have neighboring settlments
        Player mockPlayer = EasyMock.createMock(Player.class);
        Vertex neighborVertex = vertexes.getVertex(1); // 1 is adjacent to 0 by the game diagram
        neighborVertex.setOwner(mockPlayer); // this simulates trying to build next to your own settlement
                                            // which has the same functionality as a opponents settlements
        
        EasyMock.replay(mockPlayer); // Note: Nothing should be called by player

        boolean expected = false;
        boolean actual = testVertex.isbuildable();
        assertEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testIsBuildableIsOccupiedNoNeighbors() {
        VertexGraph vertexes = new VertexGraph();
        vertexes.initializeVertexToVertexAdjacency(VERTEX_TO_VERTEX_LAYOUT_FILE);

        // grab any vertex, it doesn't matter which one
        Vertex testVertex = vertexes.getVertex(0);

        // make the vertex occupied
        Player mockPlayer = EasyMock.createMock(Player.class);
        testVertex.setOwner(mockPlayer);

        
        EasyMock.replay(mockPlayer); // Note: Nothing should be called by player

        boolean expected = false;
        boolean actual = testVertex.isbuildable();
        assertEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }
}