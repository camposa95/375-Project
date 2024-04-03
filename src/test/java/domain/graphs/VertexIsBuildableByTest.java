package domain.graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.player.Player;
import data.GameLoader;
import domain.game.GameType;

public class VertexIsBuildableByTest {

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
    public void testIsBuildableBy1() {
       // make it adjacent to a friendly Road
        roads.getRoad(0).setOwner(mockPlayer);

        EasyMock.replay(mockPlayer); // Note: Nothing should be called by player

        boolean expected = true;
        boolean actual = testVertex.isBuildableBy(mockPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testIsBuildableBy2() {
        // make the vertex occupied
        testVertex.setOwner(mockPlayer);
        

        // make the vertex have neighboring settlements
        Vertex neighborVertex = vertexes.getVertex(1); // 1 is adjacent to 0 by the game diagram
        neighborVertex.setOwner(mockPlayer); // this simulates trying to build next to your own settlement
                                            // which has the same functionality as an opponents settlements
        
        // make it adjacent to a friendly Road
        roads.getRoad(0).setOwner(mockPlayer);


        EasyMock.replay(mockPlayer); // Note: Nothing should be called by player

        boolean expected = false;
        boolean actual = testVertex.isBuildableBy(mockPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testIsBuildableBy3() {
        Vertex neighborVertex = vertexes.getVertex(1); // 1 is adjacent to 0 by the game diagram
        neighborVertex.setOwner(mockPlayer); // this simulates trying to build next to your own settlement
                                            // which has the same functionality as an opponents settlements
        
        // make it adjacent to a friendly Road
        roads.getRoad(0).setOwner(mockPlayer);


        EasyMock.replay(mockPlayer); // Note: Nothing should be called by player

        boolean expected = false;
        boolean actual = testVertex.isBuildableBy(mockPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testIsBuildableBy4() {
        testVertex.setOwner(mockPlayer);

        // make it adjacent to a friendly Road
        roads.getRoad(0).setOwner(mockPlayer);


        EasyMock.replay(mockPlayer); // Note: Nothing should be called by player

        boolean expected = false;
        boolean actual = testVertex.isBuildableBy(mockPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testIsBuildableBy5() {
        EasyMock.replay(mockPlayer); // Note: Nothing should be called by player

        boolean expected = false;
        boolean actual = testVertex.isBuildableBy(mockPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testIsBuildableBy6() {
        testVertex.setOwner(mockPlayer);
        

        // make the vertex have neighboring settlements
        Vertex neighborVertex = vertexes.getVertex(1); // 1 is adjacent to 0 by the game diagram
        neighborVertex.setOwner(mockPlayer); // this simulates trying to build next to your own settlement
                                            // which has the same functionality as an opponents settlements
        
        // not adjacent to friendly road


        EasyMock.replay(mockPlayer); // Note: Nothing should be called by player

        boolean expected = false;
        boolean actual = testVertex.isBuildableBy(mockPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testIsBuildableBy7() {
        Vertex neighborVertex = vertexes.getVertex(1); // 1 is adjacent to 0 by the game diagram
        neighborVertex.setOwner(mockPlayer); // this simulates trying to build next to your own settlement
                                            // which has the same functionality as an opponents settlements
        
        // not adjacent to friendly road


        EasyMock.replay(mockPlayer); // Note: Nothing should be called by player

        boolean expected = false;
        boolean actual = testVertex.isBuildableBy(mockPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testIsBuildableBy8() {
        testVertex.setOwner(mockPlayer);

        // not adjacent to a friendly Road


        EasyMock.replay(mockPlayer); // Note: Nothing should be called by player

        boolean expected = false;
        boolean actual = testVertex.isBuildableBy(mockPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testIsBuildableBy9() {
        Player mockEnemy = EasyMock.createMock(Player.class);
        roads.getRoad(0).setOwner(mockEnemy);

        EasyMock.replay(mockPlayer); // Note: Nothing should be called by player

        boolean expected = false;
        boolean actual = testVertex.isBuildableBy(mockPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testIsBuildableBy10() {
        Player mockEnemy = EasyMock.createMock(Player.class);
        roads.getRoad(0).setOwner(mockEnemy);
        roads.getRoad(6).setOwner(mockPlayer);

        EasyMock.replay(mockPlayer); // Note: Nothing should be called by player

        boolean expected = true;
        boolean actual = testVertex.isBuildableBy(mockPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }
}
