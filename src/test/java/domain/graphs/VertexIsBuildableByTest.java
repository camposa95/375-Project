package domain.graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import domain.player.Player;
import data.GameLoader;
import domain.game.GameType;

public class VertexIsBuildableByTest {

    @Test
    public void testIsBuildableBy1() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        // grab any vertex, it doesn't matter which one
        Vertex testVertex = vertexes.getVertex(0);

        // make it adjacent to a friendly Road
        Player mockPlayer = EasyMock.createMock(Player.class);
        roads.getRoad(0).setOwner(mockPlayer);

        EasyMock.replay(mockPlayer); // Note: Nothing should be called by player

        boolean expected = true;
        boolean actual = testVertex.isBuildableBy(mockPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testIsBuildableBy2() {
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
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        // grab any vertex, it doesn't matter which one
        Vertex testVertex = vertexes.getVertex(0);

        // make the vertex occupied
        Player mockPlayer = EasyMock.createMock(Player.class);
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
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        // grab any vertex, it doesn't matter which one
        Vertex testVertex = vertexes.getVertex(0);

        // not adjacent to friendly road
        Player mockPlayer = EasyMock.createMock(Player.class);

        EasyMock.replay(mockPlayer); // Note: Nothing should be called by player

        boolean expected = false;
        boolean actual = testVertex.isBuildableBy(mockPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testIsBuildableBy6() {
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
        
        // not adjacent to friendly road


        EasyMock.replay(mockPlayer); // Note: Nothing should be called by player

        boolean expected = false;
        boolean actual = testVertex.isBuildableBy(mockPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testIsBuildableBy7() {
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
        
        // not adjacent to friendly road


        EasyMock.replay(mockPlayer); // Note: Nothing should be called by player

        boolean expected = false;
        boolean actual = testVertex.isBuildableBy(mockPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testIsBuildableBy8() {
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        // grab any vertex, it doesn't matter which one
        Vertex testVertex = vertexes.getVertex(0);

        // make the vertex occupied
        Player mockPlayer = EasyMock.createMock(Player.class);
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
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        // grab any vertex, it doesn't matter which one
        Vertex testVertex = vertexes.getVertex(0);

        // make it adjacent to an enemy Road
        Player mockPlayer = EasyMock.createMock(Player.class);
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
        VertexGraph vertexes = new VertexGraph(GameType.Beginner);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        // grab any vertex, it doesn't matter which one
        Vertex testVertex = vertexes.getVertex(0);

        // make it adjacent to an enemy Road and friendly road
        Player mockPlayer = EasyMock.createMock(Player.class);
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
