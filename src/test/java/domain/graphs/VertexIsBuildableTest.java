package domain.graphs;

import data.GameLoader;
import domain.game.GameType;
import org.easymock.EasyMock;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.player.Player;

public class VertexIsBuildableTest {

    GameboardGraph gameboardGraph;
    Player mockPlayer;
    Vertex testVertex;

    @BeforeEach
    public void setup() {
        gameboardGraph = new GameboardGraph(GameType.Beginner);
        GameLoader.initializeGraphs(gameboardGraph);

        mockPlayer = EasyMock.createStrictMock(Player.class);
        testVertex = gameboardGraph.getVertex(0);
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
        Vertex neighborVertex = gameboardGraph.getVertex(1); // 1 is adjacent to 0 by the game diagram
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
        Vertex neighborVertex = gameboardGraph.getVertex(1); // 1 is adjacent to 0 by the game diagram
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