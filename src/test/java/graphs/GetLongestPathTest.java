package graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import gamedatastructures.Player;

public class GetLongestPathTest {

    private static final String ROAD_TO_ROAD_LAYOUT_FILE = "src/main/java/graphs/RoadToRoadLayout.txt";
    private static final String ROAD_TO_VERTEX_LAYOUT_FILE = "src/main/java/graphs/RoadToVertexLayout.txt";
    private static final String VERTEX_TO_ROAD_LAYOUT_FILE = "src/main/java/graphs/VertexToRoadLayout.txt";

    @Test public void testGetLongestPathInvalid() {
        RoadGraph roads =  new RoadGraph();

        // note we do not need to initialize adjacent because all
        // we are testing here is compatability between the road and
        // player passed in

        // here the test road has no owner so by default they are 
        // incompatible 
        Player mockedEnemy = EasyMock.createStrictMock(Player.class);
        Road testRoad = roads.getRoad(0);
        Vertex mockedVertex = EasyMock.createNiceMock(Vertex.class);

        EasyMock.replay(mockedEnemy, mockedVertex);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            roads.getLongestPath(testRoad, mockedEnemy, new HashSet<HashSet<Road>>(), new HashSet<Road>(), mockedVertex);
        });
        EasyMock.verify(mockedEnemy, mockedVertex);

        String expectedMessage = "Road does not own player, check calling method";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test public void testGetLongestPathContinous() {
        RoadGraph roads =  new RoadGraph();
        VertexGraph vertexes = new VertexGraph();
        roads.initializeRoadToRoadAdjacency(ROAD_TO_ROAD_LAYOUT_FILE);
        roads.initializeRoadToVertexAdjacency(vertexes, ROAD_TO_VERTEX_LAYOUT_FILE);
        vertexes.initializeVertexToRoadAdjacency(roads, VERTEX_TO_ROAD_LAYOUT_FILE);
        

        Player mockedPlayer = EasyMock.createStrictMock(Player.class);

        // create a continous path of length 5
        Road testStartingRoad = roads.getRoad(13); // start at begining of path
        roads.getRoad(13).setOwner(mockedPlayer);
        roads.getRoad(20).setOwner(mockedPlayer);
        roads.getRoad(28).setOwner(mockedPlayer);
        roads.getRoad(29).setOwner(mockedPlayer);
        roads.getRoad(30).setOwner(mockedPlayer);

        // grab the origin
        Vertex origin = vertexes.getVertex(10);


        // method call
        EasyMock.replay(mockedPlayer);
        int actualLength = roads.getLongestPath(testStartingRoad, mockedPlayer, new HashSet<HashSet<Road>>(), new HashSet<Road>(), origin);
        EasyMock.verify(mockedPlayer);

        int expectedLength = 5;
        assertEquals(expectedLength, actualLength);
    }

    @Test public void testGetLongestPathForked() {
        RoadGraph roads =  new RoadGraph();
        VertexGraph vertexes = new VertexGraph();
        roads.initializeRoadToRoadAdjacency(ROAD_TO_ROAD_LAYOUT_FILE);
        roads.initializeRoadToVertexAdjacency(vertexes, ROAD_TO_VERTEX_LAYOUT_FILE);
        vertexes.initializeVertexToRoadAdjacency(roads, VERTEX_TO_ROAD_LAYOUT_FILE);

        Player mockedPlayer = EasyMock.createStrictMock(Player.class);

        // create a forked path where one is 5 and the other is 4 long
        Road testStartingRoad = roads.getRoad(13); // start at begining of path
        roads.getRoad(13).setOwner(mockedPlayer);
        roads.getRoad(20).setOwner(mockedPlayer);
        roads.getRoad(28).setOwner(mockedPlayer);
        roads.getRoad(29).setOwner(mockedPlayer);
        roads.getRoad(21).setOwner(mockedPlayer);
        roads.getRoad(15).setOwner(mockedPlayer);
        roads.getRoad(36).setOwner(mockedPlayer);

        // grab the origin
        Vertex origin = vertexes.getVertex(10);


        // method call
        EasyMock.replay(mockedPlayer);
        int actualLength = roads.getLongestPath(testStartingRoad, mockedPlayer, new HashSet<HashSet<Road>>(), new HashSet<Road>(), origin);
        EasyMock.verify(mockedPlayer);

        int expectedLength = 6;
        assertEquals(expectedLength, actualLength);
    }

    @Test public void testGetLongestPathloop() {
        RoadGraph roads =  new RoadGraph();
        VertexGraph vertexes = new VertexGraph();
        roads.initializeRoadToRoadAdjacency(ROAD_TO_ROAD_LAYOUT_FILE);
        roads.initializeRoadToVertexAdjacency(vertexes, ROAD_TO_VERTEX_LAYOUT_FILE);
        vertexes.initializeVertexToRoadAdjacency(roads, VERTEX_TO_ROAD_LAYOUT_FILE);

        Player mockedPlayer = EasyMock.createStrictMock(Player.class);

        // create a forked path where one is 5 and the other is 4 long
        Road testStartingRoad = roads.getRoad(13); // start at begining of path
        roads.getRoad(13).setOwner(mockedPlayer);
        roads.getRoad(20).setOwner(mockedPlayer);
        roads.getRoad(28).setOwner(mockedPlayer);
        roads.getRoad(29).setOwner(mockedPlayer);
        roads.getRoad(21).setOwner(mockedPlayer);
        roads.getRoad(15).setOwner(mockedPlayer);
        roads.getRoad(14).setOwner(mockedPlayer);
        roads.getRoad(36).setOwner(mockedPlayer);

        // grab the origin
        Vertex origin = vertexes.getVertex(10);


        // method call
        EasyMock.replay(mockedPlayer);
        int actualLength = roads.getLongestPath(testStartingRoad, mockedPlayer, new HashSet<HashSet<Road>>(), new HashSet<Road>(), origin);
        EasyMock.verify(mockedPlayer);

        int expectedLength = 7;
        assertEquals(expectedLength, actualLength);
    }

    @Test public void testGetLongestForkThenMerge() {
        RoadGraph roads =  new RoadGraph();
        VertexGraph vertexes = new VertexGraph();
        roads.initializeRoadToRoadAdjacency(ROAD_TO_ROAD_LAYOUT_FILE);
        roads.initializeRoadToVertexAdjacency(vertexes, ROAD_TO_VERTEX_LAYOUT_FILE);
        vertexes.initializeVertexToRoadAdjacency(roads, VERTEX_TO_ROAD_LAYOUT_FILE);

        Player mockedPlayer = EasyMock.createStrictMock(Player.class);

        // create a forked path where one is 5 and the other is 4 long
        Road testStartingRoad = roads.getRoad(13); // start at begining of path
        roads.getRoad(13).setOwner(mockedPlayer);
        roads.getRoad(20).setOwner(mockedPlayer);
        roads.getRoad(28).setOwner(mockedPlayer);
        roads.getRoad(29).setOwner(mockedPlayer);
        roads.getRoad(21).setOwner(mockedPlayer);
        roads.getRoad(15).setOwner(mockedPlayer);
        roads.getRoad(14).setOwner(mockedPlayer);
        roads.getRoad(36).setOwner(mockedPlayer);
        roads.getRoad(30).setOwner(mockedPlayer);
        roads.getRoad(37).setOwner(mockedPlayer);
        roads.getRoad(47).setOwner(mockedPlayer);
        roads.getRoad(53).setOwner(mockedPlayer);

        // grab the origin
        Vertex origin = vertexes.getVertex(10);


        // method call
        EasyMock.replay(mockedPlayer);
        int actualLength = roads.getLongestPath(testStartingRoad, mockedPlayer, new HashSet<HashSet<Road>>(), new HashSet<Road>(), origin);
        EasyMock.verify(mockedPlayer);

        int expectedLength = 8;
        assertEquals(expectedLength, actualLength);
    }

    @Test public void testGetLongestBrokenPath() {
        RoadGraph roads =  new RoadGraph();
        VertexGraph vertexes = new VertexGraph();
        roads.initializeRoadToRoadAdjacency(ROAD_TO_ROAD_LAYOUT_FILE);
        roads.initializeRoadToVertexAdjacency(vertexes, ROAD_TO_VERTEX_LAYOUT_FILE);
        vertexes.initializeVertexToRoadAdjacency(roads, VERTEX_TO_ROAD_LAYOUT_FILE);

        Player mockedPlayer = EasyMock.createStrictMock(Player.class);
        Player mockedEnemy = EasyMock.createStrictMock(Player.class);

        // create a forked path where one is 5 and the other is 4 long
        Road testStartingRoad = roads.getRoad(13); // start at begining of path
        roads.getRoad(13).setOwner(mockedPlayer);
        roads.getRoad(20).setOwner(mockedPlayer);
        roads.getRoad(28).setOwner(mockedPlayer);
        roads.getRoad(29).setOwner(mockedPlayer);
        roads.getRoad(21).setOwner(mockedPlayer);
        roads.getRoad(15).setOwner(mockedPlayer);
        roads.getRoad(14).setOwner(mockedPlayer);
        roads.getRoad(30).setOwner(mockedPlayer);
        roads.getRoad(37).setOwner(mockedPlayer);
        roads.getRoad(47).setOwner(mockedPlayer);

        // block off path
        vertexes.getVertex(23).setOwner(mockedEnemy);

        // grab the origin
        Vertex origin = vertexes.getVertex(10);


        // method call
        EasyMock.replay(mockedPlayer, mockedEnemy);
        int actualLength = roads.getLongestPath(testStartingRoad, mockedPlayer, new HashSet<HashSet<Road>>(), new HashSet<Road>(), origin);
        EasyMock.verify(mockedPlayer, mockedEnemy);

        int expectedLength = 4;
        assertEquals(expectedLength, actualLength);
    }

    @Test public void testGetLongestBrokenPathFriendly() {
        RoadGraph roads =  new RoadGraph();
        VertexGraph vertexes = new VertexGraph();
        roads.initializeRoadToRoadAdjacency(ROAD_TO_ROAD_LAYOUT_FILE);
        roads.initializeRoadToVertexAdjacency(vertexes, ROAD_TO_VERTEX_LAYOUT_FILE);
        vertexes.initializeVertexToRoadAdjacency(roads, VERTEX_TO_ROAD_LAYOUT_FILE);

        Player mockedPlayer = EasyMock.createStrictMock(Player.class);

        // create a forked path where one is 5 and the other is 4 long
        Road testStartingRoad = roads.getRoad(13); // start at begining of path
        roads.getRoad(13).setOwner(mockedPlayer);
        roads.getRoad(20).setOwner(mockedPlayer);
        roads.getRoad(28).setOwner(mockedPlayer);
        roads.getRoad(29).setOwner(mockedPlayer);
        roads.getRoad(21).setOwner(mockedPlayer);
        roads.getRoad(15).setOwner(mockedPlayer);
        roads.getRoad(14).setOwner(mockedPlayer);
        roads.getRoad(30).setOwner(mockedPlayer);
        roads.getRoad(37).setOwner(mockedPlayer);
        roads.getRoad(47).setOwner(mockedPlayer);

        // block off path by self is ok
        vertexes.getVertex(23).setOwner(mockedPlayer);

        // grab the origin
        Vertex origin = vertexes.getVertex(10);


        // method call
        EasyMock.replay(mockedPlayer);
        int actualLength = roads.getLongestPath(testStartingRoad, mockedPlayer, new HashSet<HashSet<Road>>(), new HashSet<Road>(), origin);
        EasyMock.verify(mockedPlayer);

        int expectedLength = 7;
        assertEquals(expectedLength, actualLength);
    }

    @Test public void testGetLongestBadOrigin() {
        RoadGraph roads =  new RoadGraph();
        VertexGraph vertexes = new VertexGraph();
        roads.initializeRoadToRoadAdjacency(ROAD_TO_ROAD_LAYOUT_FILE);
        roads.initializeRoadToVertexAdjacency(vertexes, ROAD_TO_VERTEX_LAYOUT_FILE);
        vertexes.initializeVertexToRoadAdjacency(roads, VERTEX_TO_ROAD_LAYOUT_FILE);

        Player mockedPlayer = EasyMock.createStrictMock(Player.class);
        Player mockedEnemy = EasyMock.createStrictMock(Player.class);

        // create a forked path where one is 5 and the other is 4 long
        Road testStartingRoad = roads.getRoad(13); // start at begining of path
        roads.getRoad(13).setOwner(mockedPlayer);
        roads.getRoad(20).setOwner(mockedPlayer);
        roads.getRoad(28).setOwner(mockedPlayer);
        roads.getRoad(29).setOwner(mockedPlayer);
        roads.getRoad(21).setOwner(mockedPlayer);
        roads.getRoad(15).setOwner(mockedPlayer);
        roads.getRoad(14).setOwner(mockedPlayer);
        roads.getRoad(30).setOwner(mockedPlayer);
        roads.getRoad(37).setOwner(mockedPlayer);
        roads.getRoad(47).setOwner(mockedPlayer);

        // grab the origin
        Vertex origin = vertexes.getVertex(10);
        // block it
        origin.setOwner(mockedEnemy);


        // method call
        EasyMock.replay(mockedPlayer, mockedEnemy);
        int actualLength = roads.getLongestPath(testStartingRoad, mockedPlayer, new HashSet<HashSet<Road>>(), new HashSet<Road>(), origin);
        EasyMock.verify(mockedPlayer, mockedEnemy);

        int expectedLength = -1;
        assertEquals(expectedLength, actualLength);
    }

    @Test public void testGetTailException() {

        Road testRoad =  new Road(0);
        Vertex testVertex = new Vertex(0);

        Vertex[] badVertexes = {testVertex};
        testRoad.setAdjacentVertexes(badVertexes);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            testRoad.getTail(testVertex);
        });
        
        String expectedMessage = "This road only has one adjacent vertex?";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
