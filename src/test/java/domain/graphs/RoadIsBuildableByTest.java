package domain.graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import data.GameLoader;
import domain.game.GameType;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.player.Player;

public class RoadIsBuildableByTest {

    VertexGraph vertexes;
    RoadGraph roads;
    Player mockedPlayer, mockedEnemy;

    @BeforeEach
    public void setup() {
        vertexes = new VertexGraph(GameType.Beginner);
        roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        mockedPlayer = EasyMock.createStrictMock(Player.class);
        mockedEnemy = EasyMock.createStrictMock(Player.class);
    }

    @Test
    public void test0IsBuildableBy() {
        // get any road
        Road testRoad = roads.getRoad(0);

        // it is occupied by player trying to place
        testRoad.setOwner(mockedPlayer);


        EasyMock.replay(mockedPlayer);

        boolean expected = false;
        boolean actual = testRoad.isBuildableBy(mockedPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockedPlayer);
    }

    @Test
    public void test1IsBuildableBy() {
        // get any road
        Road testRoad = roads.getRoad(0); 

        // it is occupied by enemy
        testRoad.setOwner(mockedEnemy);


        EasyMock.replay(mockedPlayer, mockedEnemy);

        boolean expected = false;
        boolean actual = testRoad.isBuildableBy(mockedPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockedPlayer, mockedEnemy);
    }

    @Test
    public void test2IsBuildableBy() {
        // get any road
        Road testRoad = roads.getRoad(28); 
        // not occupied

        // road is adjacent to a friendly city
        vertexes.getVertex(22).setOwner(mockedPlayer);
        roads.getRoad(36).setOwner(mockedPlayer);
        roads.getRoad(45).setOwner(mockedPlayer);
        vertexes.getVertex(34).setOwner(mockedPlayer);
        // not adjacent to enemy city

        EasyMock.replay(mockedPlayer);

        boolean expected = true;
        boolean actual = testRoad.isBuildableBy(mockedPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockedPlayer);
    }

    @Test
    public void test3IsBuildableBy() {
        // get any road
        Road testRoad = roads.getRoad(28); 
        // not occupied

        // road adjacent to enemy city
        vertexes.getVertex(21).setOwner(mockedEnemy);
        roads.getRoad(27).setOwner(mockedEnemy);

        EasyMock.replay(mockedPlayer, mockedEnemy);

        boolean expected = false;
        boolean actual = testRoad.isBuildableBy(mockedPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockedPlayer, mockedEnemy);
    }

    @Test
    public void test4IsBuildableBy() {
        // get any road
        Road testRoad = roads.getRoad(28); 
        // is occupied by any player
        testRoad.setOwner(mockedPlayer);

        // road is adjacent to a friendly city
        vertexes.getVertex(22).setOwner(mockedPlayer);
        roads.getRoad(36).setOwner(mockedPlayer);
        roads.getRoad(45).setOwner(mockedPlayer);
        vertexes.getVertex(34).setOwner(mockedPlayer);


        EasyMock.replay(mockedPlayer);

        boolean expected = false;
        boolean actual = testRoad.isBuildableBy(mockedPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockedPlayer);
    }

    @Test
    public void test5IsBuildableBy() {
        // get any road
        Road testRoad = roads.getRoad(28); 
        // not occupied

        // road is adjacent to a friendly road, friendly road is not
        // adjacent to an enemy vertex or road, new road is not adjacent
        // to an enemy vertex or road
        roads.getRoad(36).setOwner(mockedPlayer);
        roads.getRoad(45).setOwner(mockedPlayer);
        vertexes.getVertex(34).setOwner(mockedPlayer);


        EasyMock.replay(mockedPlayer);

        boolean expected = true;
        boolean actual = testRoad.isBuildableBy(mockedPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockedPlayer);
    }

    @Test
    public void test6IsBuildableBy() {
        // get any road
        Road testRoad = roads.getRoad(28); 
        // not occupied

        // road is adjacent to a friendly road, friendly road is not
        // adjacent to an enemy vertex or road
        roads.getRoad(36).setOwner(mockedPlayer);
        roads.getRoad(45).setOwner(mockedPlayer);
        vertexes.getVertex(34).setOwner(mockedPlayer);
        // new road is adjacent to an enemy road
        roads.getRoad(27).setOwner(mockedEnemy);

        EasyMock.replay(mockedPlayer, mockedEnemy);

        boolean expected = true;
        boolean actual = testRoad.isBuildableBy(mockedPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockedPlayer, mockedEnemy);
    }

    @Test
    public void test7IsBuildableBy() {
        // get any road
        Road testRoad = roads.getRoad(28); 
        // not occupied

        // road is adjacent to a friendly road, friendly road is not
        // adjacent to an enemy vertex or road
        roads.getRoad(36).setOwner(mockedPlayer);
        roads.getRoad(45).setOwner(mockedPlayer);
        vertexes.getVertex(34).setOwner(mockedPlayer);
        // new road is adjacent to an enemy vertex
        vertexes.getVertex(21).setOwner(mockedEnemy);

        EasyMock.replay(mockedPlayer, mockedEnemy);

        boolean expected = true;
        boolean actual = testRoad.isBuildableBy(mockedPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockedPlayer, mockedEnemy);
    }

    @Test
    public void test8IsBuildableBy() {
        // get any road
        Road testRoad = roads.getRoad(28); 
        // not occupied

        // road is adjacent to a friendly road
        roads.getRoad(36).setOwner(mockedPlayer);
        roads.getRoad(45).setOwner(mockedPlayer);
        vertexes.getVertex(34).setOwner(mockedPlayer);
        // friendly road is adjacent to enemy road
        roads.getRoad(29).setOwner(mockedEnemy);
        // new road is not adjacent to an enemy vertex

        EasyMock.replay(mockedPlayer, mockedEnemy);

        boolean expected = true;
        boolean actual = testRoad.isBuildableBy(mockedPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockedPlayer, mockedEnemy);
    }

    @Test
    public void test9IsBuildableBy() {
        // get any road
        Road testRoad = roads.getRoad(28); 
        // not occupied

        // road is adjacent to a friendly road
        roads.getRoad(36).setOwner(mockedPlayer);
        roads.getRoad(45).setOwner(mockedPlayer);
        vertexes.getVertex(34).setOwner(mockedPlayer);
        // friendly road is adjacent to enemy road
        roads.getRoad(29).setOwner(mockedEnemy);
        // new road is adjacent to an enemy vertex
        roads.getRoad(21).setOwner(mockedEnemy);
        roads.getRoad(15).setOwner(mockedEnemy);
        roads.getRoad(14).setOwner(mockedEnemy);
        roads.getRoad(20).setOwner(mockedEnemy);
        vertexes.getVertex(21).setOwner(mockedEnemy);

        EasyMock.replay(mockedPlayer, mockedEnemy);

        boolean expected = true;
        boolean actual = testRoad.isBuildableBy(mockedPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockedPlayer, mockedEnemy);
    }

    @Test
    public void test10IsBuildableBy() {
        // get any road
        Road testRoad = roads.getRoad(28); 
        // not occupied

        // road is adjacent to a friendly road
        roads.getRoad(36).setOwner(mockedPlayer);
        roads.getRoad(45).setOwner(mockedPlayer);
        vertexes.getVertex(34).setOwner(mockedPlayer);
        // friendly road is adjacent to enemy vertex
        vertexes.getVertex(22).setOwner(mockedEnemy);

        EasyMock.replay(mockedPlayer, mockedEnemy);

        boolean expected = false;
        boolean actual = testRoad.isBuildableBy(mockedPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockedPlayer, mockedEnemy);
    }

    @Test
    public void test11IsBuildableBy() {
        // get any road
        Road testRoad = roads.getRoad(28); 
        // not occupied

        // road is adjacent to a friendly road
        roads.getRoad(36).setOwner(mockedPlayer);
        roads.getRoad(45).setOwner(mockedPlayer);
        vertexes.getVertex(34).setOwner(mockedPlayer);
        // friendly road is adjacent to enemy vertex
        vertexes.getVertex(22).setOwner(mockedEnemy);

        // road is adjacent to another friendly road not adjacent to any vertex
        roads.getRoad(44).setOwner(mockedPlayer);
        roads.getRoad(43).setOwner(mockedPlayer);
        roads.getRoad(35).setOwner(mockedPlayer);
        roads.getRoad(27).setOwner(mockedPlayer);


        EasyMock.replay(mockedPlayer, mockedEnemy);

        boolean expected = true;
        boolean actual = testRoad.isBuildableBy(mockedPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockedPlayer, mockedEnemy);
    }

    @Test
    public void test12IsBuildableBy() {
        // get any road
        Road testRoad = roads.getRoad(28); 
        // not occupied

        // road is adjacent to a friendly road
        roads.getRoad(36).setOwner(mockedPlayer);
        roads.getRoad(45).setOwner(mockedPlayer);
        vertexes.getVertex(34).setOwner(mockedPlayer);
        // friendly road is adjacent to enemy vertex
        vertexes.getVertex(22).setOwner(mockedEnemy);

        // road is adjacent to another friendly road not adjacent to enemy vertex, but friendly vertex 
        roads.getRoad(44).setOwner(mockedPlayer);
        roads.getRoad(43).setOwner(mockedPlayer);
        roads.getRoad(35).setOwner(mockedPlayer);
        roads.getRoad(27).setOwner(mockedPlayer);
        vertexes.getVertex(20).setOwner(mockedPlayer);


        EasyMock.replay(mockedPlayer, mockedEnemy);

        boolean expected = true;
        boolean actual = testRoad.isBuildableBy(mockedPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockedPlayer, mockedEnemy);
    }

    @Test
    public void test13IsBuildableBy() {
        // get any road
        Road testRoad = roads.getRoad(0);
        // not occupied, doesn't connect to friendly road or vertex 


        EasyMock.replay(mockedPlayer);

        boolean expected = false;
        boolean actual = testRoad.isBuildableBy(mockedPlayer);
        assertEquals(expected, actual);

        EasyMock.verify(mockedPlayer);
    }
}
