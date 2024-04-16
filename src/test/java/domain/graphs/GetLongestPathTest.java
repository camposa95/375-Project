package domain.graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import data.GameLoader;
import domain.game.GameType;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.player.Player;

public class GetLongestPathTest {

    GameboardGraph gameboardGraph;
    Player mockedPlayer;

    @BeforeEach
    public void setup() {
        gameboardGraph = new GameboardGraph(GameType.Beginner);
        GameLoader.initializeGraphs(gameboardGraph);

        mockedPlayer = EasyMock.createStrictMock(Player.class);
    }

    @Test public void testGetLongestPathInvalid() {
        // here the test road has no owner so by default they are
        // incompatible
        Player mockedEnemy = EasyMock.createStrictMock(Player.class);
        Road testRoad = gameboardGraph.getRoad(0);
        Vertex mockedVertex = EasyMock.createNiceMock(Vertex.class);

        EasyMock.replay(mockedEnemy, mockedVertex);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> gameboardGraph.getLongestPath(testRoad, mockedEnemy, new HashSet<>(), new HashSet<>(), mockedVertex));
        EasyMock.verify(mockedEnemy, mockedVertex);

        String expectedMessage = "Road does not own player, check calling method";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test public void testGetLongestPathContinuous() {
        // create a continuous path of length 5
        Road testStartingRoad = gameboardGraph.getRoad(13); // start at beginning of path
        gameboardGraph.getRoad(13).setOwner(mockedPlayer);
        gameboardGraph.getRoad(20).setOwner(mockedPlayer);
        gameboardGraph.getRoad(28).setOwner(mockedPlayer);
        gameboardGraph.getRoad(29).setOwner(mockedPlayer);
        gameboardGraph.getRoad(30).setOwner(mockedPlayer);

        // grab the origin
        Vertex origin = gameboardGraph.getVertex(10);


        // method call
        EasyMock.replay(mockedPlayer);
        int actualLength = gameboardGraph.getLongestPath(testStartingRoad, mockedPlayer, new HashSet<>(), new HashSet<>(), origin);
        EasyMock.verify(mockedPlayer);

        int expectedLength = 5;
        assertEquals(expectedLength, actualLength);
    }

    @Test public void testGetLongestPathForked() {
        // create a forked path where one is 5 and the other is 4 long
        Road testStartingRoad = gameboardGraph.getRoad(13); // start at beginning of path
        gameboardGraph.getRoad(13).setOwner(mockedPlayer);
        gameboardGraph.getRoad(20).setOwner(mockedPlayer);
        gameboardGraph.getRoad(28).setOwner(mockedPlayer);
        gameboardGraph.getRoad(29).setOwner(mockedPlayer);
        gameboardGraph.getRoad(21).setOwner(mockedPlayer);
        gameboardGraph.getRoad(15).setOwner(mockedPlayer);
        gameboardGraph.getRoad(36).setOwner(mockedPlayer);

        // grab the origin
        Vertex origin = gameboardGraph.getVertex(10);


        // method call
        EasyMock.replay(mockedPlayer);
        int actualLength = gameboardGraph.getLongestPath(testStartingRoad, mockedPlayer, new HashSet<>(), new HashSet<>(), origin);
        EasyMock.verify(mockedPlayer);

        int expectedLength = 6;
        assertEquals(expectedLength, actualLength);
    }

    @Test public void testGetLongestPathLoop() {
        // create a forked path where one is 5 and the other is 4 long
        Road testStartingRoad = gameboardGraph.getRoad(13); // start at beginning of path
        gameboardGraph.getRoad(13).setOwner(mockedPlayer);
        gameboardGraph.getRoad(20).setOwner(mockedPlayer);
        gameboardGraph.getRoad(28).setOwner(mockedPlayer);
        gameboardGraph.getRoad(29).setOwner(mockedPlayer);
        gameboardGraph.getRoad(21).setOwner(mockedPlayer);
        gameboardGraph.getRoad(15).setOwner(mockedPlayer);
        gameboardGraph.getRoad(14).setOwner(mockedPlayer);
        gameboardGraph.getRoad(36).setOwner(mockedPlayer);

        // grab the origin
        Vertex origin = gameboardGraph.getVertex(10);


        // method call
        EasyMock.replay(mockedPlayer);
        int actualLength = gameboardGraph.getLongestPath(testStartingRoad, mockedPlayer, new HashSet<>(), new HashSet<>(), origin);
        EasyMock.verify(mockedPlayer);

        int expectedLength = 7;
        assertEquals(expectedLength, actualLength);
    }

    @Test public void testGetLongestForkThenMerge() {
        // create a forked path where one is 5 and the other is 4 long
        Road testStartingRoad = gameboardGraph.getRoad(13); // start at beginning of path
        gameboardGraph.getRoad(13).setOwner(mockedPlayer);
        gameboardGraph.getRoad(20).setOwner(mockedPlayer);
        gameboardGraph.getRoad(28).setOwner(mockedPlayer);
        gameboardGraph.getRoad(29).setOwner(mockedPlayer);
        gameboardGraph.getRoad(21).setOwner(mockedPlayer);
        gameboardGraph.getRoad(15).setOwner(mockedPlayer);
        gameboardGraph.getRoad(14).setOwner(mockedPlayer);
        gameboardGraph.getRoad(36).setOwner(mockedPlayer);
        gameboardGraph.getRoad(30).setOwner(mockedPlayer);
        gameboardGraph.getRoad(37).setOwner(mockedPlayer);
        gameboardGraph.getRoad(47).setOwner(mockedPlayer);
        gameboardGraph.getRoad(53).setOwner(mockedPlayer);

        // grab the origin
        Vertex origin = gameboardGraph.getVertex(10);


        // method call
        EasyMock.replay(mockedPlayer);
        int actualLength = gameboardGraph.getLongestPath(testStartingRoad, mockedPlayer, new HashSet<>(), new HashSet<>(), origin);
        EasyMock.verify(mockedPlayer);

        int expectedLength = 8;
        assertEquals(expectedLength, actualLength);
    }

    @Test public void testGetLongestBrokenPath() {
        Player mockedEnemy = EasyMock.createStrictMock(Player.class);

        // create a forked path where one is 5 and the other is 4 long
        Road testStartingRoad = gameboardGraph.getRoad(13); // start at beginning of path
        gameboardGraph.getRoad(13).setOwner(mockedPlayer);
        gameboardGraph.getRoad(20).setOwner(mockedPlayer);
        gameboardGraph.getRoad(28).setOwner(mockedPlayer);
        gameboardGraph.getRoad(29).setOwner(mockedPlayer);
        gameboardGraph.getRoad(21).setOwner(mockedPlayer);
        gameboardGraph.getRoad(15).setOwner(mockedPlayer);
        gameboardGraph.getRoad(14).setOwner(mockedPlayer);
        gameboardGraph.getRoad(30).setOwner(mockedPlayer);
        gameboardGraph.getRoad(37).setOwner(mockedPlayer);
        gameboardGraph.getRoad(47).setOwner(mockedPlayer);

        // block off path
        gameboardGraph.getVertex(23).setOwner(mockedEnemy);

        // grab the origin
        Vertex origin = gameboardGraph.getVertex(10);


        // method call
        EasyMock.replay(mockedPlayer, mockedEnemy);
        int actualLength = gameboardGraph.getLongestPath(testStartingRoad, mockedPlayer, new HashSet<>(), new HashSet<>(), origin);
        EasyMock.verify(mockedPlayer, mockedEnemy);

        int expectedLength = 4;
        assertEquals(expectedLength, actualLength);
    }

    @Test public void testGetLongestBrokenPathFriendly() {
        // create a forked path where one is 5 and the other is 4 long
        Road testStartingRoad = gameboardGraph.getRoad(13); // start at beginning of path
        gameboardGraph.getRoad(13).setOwner(mockedPlayer);
        gameboardGraph.getRoad(20).setOwner(mockedPlayer);
        gameboardGraph.getRoad(28).setOwner(mockedPlayer);
        gameboardGraph.getRoad(29).setOwner(mockedPlayer);
        gameboardGraph.getRoad(21).setOwner(mockedPlayer);
        gameboardGraph.getRoad(15).setOwner(mockedPlayer);
        gameboardGraph.getRoad(14).setOwner(mockedPlayer);
        gameboardGraph.getRoad(30).setOwner(mockedPlayer);
        gameboardGraph.getRoad(37).setOwner(mockedPlayer);
        gameboardGraph.getRoad(47).setOwner(mockedPlayer);

        // block off path by self is ok
        gameboardGraph.getVertex(23).setOwner(mockedPlayer);

        // grab the origin
        Vertex origin = gameboardGraph.getVertex(10);


        // method call
        EasyMock.replay(mockedPlayer);
        int actualLength = gameboardGraph.getLongestPath(testStartingRoad, mockedPlayer, new HashSet<>(), new HashSet<>(), origin);
        EasyMock.verify(mockedPlayer);

        int expectedLength = 7;
        assertEquals(expectedLength, actualLength);
    }

    @Test public void testGetLongestBadOrigin() {
        Player mockedEnemy = EasyMock.createStrictMock(Player.class);

        // create a forked path where one is 5 and the other is 4 long
        Road testStartingRoad = gameboardGraph.getRoad(13); // start at beginning of path
        gameboardGraph.getRoad(13).setOwner(mockedPlayer);
        gameboardGraph.getRoad(20).setOwner(mockedPlayer);
        gameboardGraph.getRoad(28).setOwner(mockedPlayer);
        gameboardGraph.getRoad(29).setOwner(mockedPlayer);
        gameboardGraph.getRoad(21).setOwner(mockedPlayer);
        gameboardGraph.getRoad(15).setOwner(mockedPlayer);
        gameboardGraph.getRoad(14).setOwner(mockedPlayer);
        gameboardGraph.getRoad(30).setOwner(mockedPlayer);
        gameboardGraph.getRoad(37).setOwner(mockedPlayer);
        gameboardGraph.getRoad(47).setOwner(mockedPlayer);

        // grab the origin
        Vertex origin = gameboardGraph.getVertex(10);
        // block it
        origin.setOwner(mockedEnemy);


        // method call
        EasyMock.replay(mockedPlayer, mockedEnemy);
        int actualLength = gameboardGraph.getLongestPath(testStartingRoad, mockedPlayer, new HashSet<>(), new HashSet<>(), origin);
        EasyMock.verify(mockedPlayer, mockedEnemy);

        int expectedLength = -1;
        assertEquals(expectedLength, actualLength);
    }

    @Test public void testGetTailException() {
        Road testRoad =  new Road(0);
        Vertex testVertex = new Vertex(0);

        List<Vertex> badVertexes = new ArrayList<>();
        badVertexes.add(testVertex);
        testRoad.setAdjacentVertexes(badVertexes);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> testRoad.getTail(testVertex));
        
        String expectedMessage = "This road only has one adjacent vertex?";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
