package domain.graphs;

import data.GameLoader;
import domain.game.GameType;
import org.easymock.EasyMock;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import domain.player.Player;

public class VertexIsBuildableTest {

    @Test
    public void testIsBuildableNotOccupiedNoNeighbors() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        // grab any vertex, it doesn't matter which one
        Vertex testVertex = vertexes.getVertex(0);

        boolean expected = true;
        boolean actual = testVertex.isbuildable();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsBuildableIsOccupiedHasNeighbors() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        // grab any vertex, it doesn't matter which one
        Vertex testVertex = vertexes.getVertex(0);

        // make the vertex occupied
        Player mockPlayer = EasyMock.createMock(Player.class);
        testVertex.setOwner(mockPlayer);

        // make the vertex have neighboring settlements
        Vertex neighborVertex = vertexes.getVertex(1); // 1 is adjacent to 0 by the game diagram
        neighborVertex.setOwner(mockPlayer); // this simulates trying to build next to your own settlement
                                            // which has the same functionality as an opponents settlements
        
        
        EasyMock.replay(mockPlayer); // Note: Nothing should be called by player

        boolean expected = false;
        boolean actual = testVertex.isbuildable();
        assertEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testIsBuildableNotOccupiedHasNeighbors() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        // grab any vertex, it doesn't matter which one
        Vertex testVertex = vertexes.getVertex(0);

        // make the vertex have neighboring settlements
        Player mockPlayer = EasyMock.createMock(Player.class);
        Vertex neighborVertex = vertexes.getVertex(1); // 1 is adjacent to 0 by the game diagram
        neighborVertex.setOwner(mockPlayer); // this simulates trying to build next to your own settlement
                                            // which has the same functionality as an opponents settlements
        
        EasyMock.replay(mockPlayer); // Note: Nothing should be called by player

        boolean expected = false;
        boolean actual = testVertex.isbuildable();
        assertEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testIsBuildableIsOccupiedNoNeighbors() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

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