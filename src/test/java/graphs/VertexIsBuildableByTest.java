package graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import gamedatastructures.Player;

public class VertexIsBuildableByTest {
    private static final String VERTEX_TO_VERTEX_LAYOUT_FILE = "src/main/java/graphs/VertexToVertexLayout.txt";
    private static final String ROAD_TO_ROAD_LAYOUT_FILE = "src/main/java/graphs/RoadToRoadLayout.txt";
    private static final String VERTEX_TO_ROAD_LAYOUT_FILE = "src/main/java/graphs/VertexToRoadLayout.txt";

    @Test
    public void testIsBuildableBy1() {
        VertexGraph vertexes = new VertexGraph();
        RoadGraph roads = new RoadGraph();
        vertexes.initializeVertexToVertexAdjacency(VERTEX_TO_VERTEX_LAYOUT_FILE);
        roads.initializeRoadToRoadAdjacency(ROAD_TO_ROAD_LAYOUT_FILE);
        vertexes.initializeVertexToRoadAdjacency(roads, VERTEX_TO_ROAD_LAYOUT_FILE);

        // grab any vertex, it doesn't matter which one
        Vertex testVertex = vertexes.getVertex(0);

        // make it adjacent to a friendly Road
        Player mockPlayer = EasyMock.createMock(Player.class);
        roads.getRoad(0).setOwner(mockPlayer);

        EasyMock.replay(mockPlayer); // Note: Nothing should be called by player

        boolean expected = true;
        boolean actual = testVertex.isbuildableBy(mockPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testIsBuildableBy2() {
        VertexGraph vertexes = new VertexGraph();
        RoadGraph roads = new RoadGraph();
        vertexes.initializeVertexToVertexAdjacency(VERTEX_TO_VERTEX_LAYOUT_FILE);
        roads.initializeRoadToRoadAdjacency(ROAD_TO_ROAD_LAYOUT_FILE);
        vertexes.initializeVertexToRoadAdjacency(roads, VERTEX_TO_ROAD_LAYOUT_FILE);

        // grab any vertex, it doesn't matter which one
        Vertex testVertex = vertexes.getVertex(0);

        // make the vertex occupied
        Player mockPlayer = EasyMock.createMock(Player.class);
        testVertex.setOwner(mockPlayer);
        

        // make the vertex have neighboring settlments
        Vertex neighborVertex = vertexes.getVertex(1); // 1 is adjacent to 0 by the game diagram
        neighborVertex.setOwner(mockPlayer); // this simulates trying to build next to your own settlement
                                            // which has the same functionality as a opponents settlements
        
        // make it adjacent to a friendly Road
        roads.getRoad(0).setOwner(mockPlayer);


        EasyMock.replay(mockPlayer); // Note: Nothing should be called by player

        boolean expected = false;
        boolean actual = testVertex.isbuildableBy(mockPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testIsBuildableBy3() {
        VertexGraph vertexes = new VertexGraph();
        RoadGraph roads = new RoadGraph();
        vertexes.initializeVertexToVertexAdjacency(VERTEX_TO_VERTEX_LAYOUT_FILE);
        roads.initializeRoadToRoadAdjacency(ROAD_TO_ROAD_LAYOUT_FILE);
        vertexes.initializeVertexToRoadAdjacency(roads, VERTEX_TO_ROAD_LAYOUT_FILE);

        // grab any vertex, it doesn't matter which one
        Vertex testVertex = vertexes.getVertex(0);

        // make the vertex have neighboring settlments
        Player mockPlayer = EasyMock.createMock(Player.class);
        Vertex neighborVertex = vertexes.getVertex(1); // 1 is adjacent to 0 by the game diagram
        neighborVertex.setOwner(mockPlayer); // this simulates trying to build next to your own settlement
                                            // which has the same functionality as a opponents settlements
        
        // make it adjacent to a friendly Road
        roads.getRoad(0).setOwner(mockPlayer);


        EasyMock.replay(mockPlayer); // Note: Nothing should be called by player

        boolean expected = false;
        boolean actual = testVertex.isbuildableBy(mockPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testIsBuildableBy4() {
        VertexGraph vertexes = new VertexGraph();
        RoadGraph roads = new RoadGraph();
        vertexes.initializeVertexToVertexAdjacency(VERTEX_TO_VERTEX_LAYOUT_FILE);
        roads.initializeRoadToRoadAdjacency(ROAD_TO_ROAD_LAYOUT_FILE);
        vertexes.initializeVertexToRoadAdjacency(roads, VERTEX_TO_ROAD_LAYOUT_FILE);

        // grab any vertex, it doesn't matter which one
        Vertex testVertex = vertexes.getVertex(0);

        // make the vertex occupied
        Player mockPlayer = EasyMock.createMock(Player.class);
        testVertex.setOwner(mockPlayer);

        // make it adjacent to a friendly Road
        roads.getRoad(0).setOwner(mockPlayer);


        EasyMock.replay(mockPlayer); // Note: Nothing should be called by player

        boolean expected = false;
        boolean actual = testVertex.isbuildableBy(mockPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testIsBuildableBy5() {
        VertexGraph vertexes = new VertexGraph();
        RoadGraph roads = new RoadGraph();
        vertexes.initializeVertexToVertexAdjacency(VERTEX_TO_VERTEX_LAYOUT_FILE);
        roads.initializeRoadToRoadAdjacency(ROAD_TO_ROAD_LAYOUT_FILE);
        vertexes.initializeVertexToRoadAdjacency(roads, VERTEX_TO_ROAD_LAYOUT_FILE);

        // grab any vertex, it doesn't matter which one
        Vertex testVertex = vertexes.getVertex(0);

        // not adjacent to friendly road
        Player mockPlayer = EasyMock.createMock(Player.class);

        EasyMock.replay(mockPlayer); // Note: Nothing should be called by player

        boolean expected = false;
        boolean actual = testVertex.isbuildableBy(mockPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testIsBuildableBy6() {
        VertexGraph vertexes = new VertexGraph();
        RoadGraph roads = new RoadGraph();
        vertexes.initializeVertexToVertexAdjacency(VERTEX_TO_VERTEX_LAYOUT_FILE);
        roads.initializeRoadToRoadAdjacency(ROAD_TO_ROAD_LAYOUT_FILE);
        vertexes.initializeVertexToRoadAdjacency(roads, VERTEX_TO_ROAD_LAYOUT_FILE);

        // grab any vertex, it doesn't matter which one
        Vertex testVertex = vertexes.getVertex(0);

        // make the vertex occupied
        Player mockPlayer = EasyMock.createMock(Player.class);
        testVertex.setOwner(mockPlayer);
        

        // make the vertex have neighboring settlments
        Vertex neighborVertex = vertexes.getVertex(1); // 1 is adjacent to 0 by the game diagram
        neighborVertex.setOwner(mockPlayer); // this simulates trying to build next to your own settlement
                                            // which has the same functionality as a opponents settlements
        
        // not adjacent to friendly road


        EasyMock.replay(mockPlayer); // Note: Nothing should be called by player

        boolean expected = false;
        boolean actual = testVertex.isbuildableBy(mockPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testIsBuildableBy7() {
        VertexGraph vertexes = new VertexGraph();
        RoadGraph roads = new RoadGraph();
        vertexes.initializeVertexToVertexAdjacency(VERTEX_TO_VERTEX_LAYOUT_FILE);
        roads.initializeRoadToRoadAdjacency(ROAD_TO_ROAD_LAYOUT_FILE);
        vertexes.initializeVertexToRoadAdjacency(roads, VERTEX_TO_ROAD_LAYOUT_FILE);

        // grab any vertex, it doesn't matter which one
        Vertex testVertex = vertexes.getVertex(0);

        // make the vertex have neighboring settlments
        Player mockPlayer = EasyMock.createMock(Player.class);
        Vertex neighborVertex = vertexes.getVertex(1); // 1 is adjacent to 0 by the game diagram
        neighborVertex.setOwner(mockPlayer); // this simulates trying to build next to your own settlement
                                            // which has the same functionality as a opponents settlements
        
        // not adjacent to friendly road


        EasyMock.replay(mockPlayer); // Note: Nothing should be called by player

        boolean expected = false;
        boolean actual = testVertex.isbuildableBy(mockPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testIsBuildableBy8() {
        VertexGraph vertexes = new VertexGraph();
        RoadGraph roads = new RoadGraph();
        vertexes.initializeVertexToVertexAdjacency(VERTEX_TO_VERTEX_LAYOUT_FILE);
        roads.initializeRoadToRoadAdjacency(ROAD_TO_ROAD_LAYOUT_FILE);
        vertexes.initializeVertexToRoadAdjacency(roads, VERTEX_TO_ROAD_LAYOUT_FILE);

        // grab any vertex, it doesn't matter which one
        Vertex testVertex = vertexes.getVertex(0);

        // make the vertex occupied
        Player mockPlayer = EasyMock.createMock(Player.class);
        testVertex.setOwner(mockPlayer);

        // not adjacent to a friendly Road


        EasyMock.replay(mockPlayer); // Note: Nothing should be called by player

        boolean expected = false;
        boolean actual = testVertex.isbuildableBy(mockPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testIsBuildableBy9() {
        VertexGraph vertexes = new VertexGraph();
        RoadGraph roads = new RoadGraph();
        vertexes.initializeVertexToVertexAdjacency(VERTEX_TO_VERTEX_LAYOUT_FILE);
        roads.initializeRoadToRoadAdjacency(ROAD_TO_ROAD_LAYOUT_FILE);
        vertexes.initializeVertexToRoadAdjacency(roads, VERTEX_TO_ROAD_LAYOUT_FILE);

        // grab any vertex, it doesn't matter which one
        Vertex testVertex = vertexes.getVertex(0);

        // make it adjacent to a enemy Road
        Player mockPlayer = EasyMock.createMock(Player.class);
        Player mockEnemy = EasyMock.createMock(Player.class);
        roads.getRoad(0).setOwner(mockEnemy);

        EasyMock.replay(mockPlayer); // Note: Nothing should be called by player

        boolean expected = false;
        boolean actual = testVertex.isbuildableBy(mockPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testIsBuildableBy10() {
        VertexGraph vertexes = new VertexGraph();
        RoadGraph roads = new RoadGraph();
        vertexes.initializeVertexToVertexAdjacency(VERTEX_TO_VERTEX_LAYOUT_FILE);
        roads.initializeRoadToRoadAdjacency(ROAD_TO_ROAD_LAYOUT_FILE);
        vertexes.initializeVertexToRoadAdjacency(roads, VERTEX_TO_ROAD_LAYOUT_FILE);

        // grab any vertex, it doesn't matter which one
        Vertex testVertex = vertexes.getVertex(0);

        // make it adjacent to a enemy Road and friendly road
        Player mockPlayer = EasyMock.createMock(Player.class);
        Player mockEnemy = EasyMock.createMock(Player.class);
        roads.getRoad(0).setOwner(mockEnemy);
        roads.getRoad(6).setOwner(mockPlayer);

        EasyMock.replay(mockPlayer); // Note: Nothing should be called by player

        boolean expected = true;
        boolean actual = testVertex.isbuildableBy(mockPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }
}
