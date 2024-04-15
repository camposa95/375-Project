package domain.graphs;

import data.GameLoader;
import domain.game.GameType;
import org.easymock.EasyMock;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.player.Player;

public class VertexIsBuildableTest {

    VertexGraph vertexes;
    RoadGraph roads;
    Player mockPlayer;
    Vertex testVertex;

    @BeforeEach
    public void setup() {
        vertexes = new VertexGraph(GameType.Beginner);
        roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        mockPlayer = EasyMock.createStrictMock(Player.class);
        testVertex = vertexes.getVertex(0);
    }

    @Test
    public void testIsBuildableNotOccupiedNoNeighbors() {
        boolean expected = true;
        boolean actual = testVertex.isBuildable();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsBuildableIsOccupiedHasNeighbors() {
        testVertex.setOwner(mockPlayer);

        // make the vertex have neighboring settlements
        Vertex neighborVertex = vertexes.getVertex(1); // 1 is adjacent to 0 by the game diagram
        neighborVertex.setOwner(mockPlayer); // this simulates trying to build next to your own settlement
                                            // which has the same functionality as an opponents settlements
        
        
        EasyMock.replay(mockPlayer); // Note: Nothing should be called by player

        boolean expected = false;
        boolean actual = testVertex.isBuildable();
        assertEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testIsBuildableNotOccupiedHasNeighbors() {
        Vertex neighborVertex = vertexes.getVertex(1); // 1 is adjacent to 0 by the game diagram
        neighborVertex.setOwner(mockPlayer); // this simulates trying to build next to your own settlement
                                            // which has the same functionality as an opponents settlements
        
        EasyMock.replay(mockPlayer); // Note: Nothing should be called by player

        boolean expected = false;
        boolean actual = testVertex.isBuildable();
        assertEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testIsBuildableIsOccupiedNoNeighbors() {
        testVertex.setOwner(mockPlayer);

        
        EasyMock.replay(mockPlayer); // Note: Nothing should be called by player

        boolean expected = false;
        boolean actual = testVertex.isBuildable();
        assertEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }
}