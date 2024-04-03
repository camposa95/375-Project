package domain.graphs;

import data.GameLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.game.GameType;
import domain.player.Player;
import domain.bank.Resource;

import static org.junit.jupiter.api.Assertions.*;

public class BuildVertexTest {

    VertexGraph vertexes;
    RoadGraph roads;
    Player player;

    @BeforeEach
    public void setup() {
        vertexes = new VertexGraph(GameType.Beginner);
        roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
        player =  new Player(1);
    }

    @Test
    public void testBuildVertex1() {
        Resource[] boostsBefore = player.getTradeBoosts();
        Resource[] expectedBoostsBefore = {};
        assertArrayEquals(expectedBoostsBefore, boostsBefore);

        // grab a vertex not adjacent to a port
        Vertex testVertex = vertexes.getVertex(20);

        // player builds on the vertex
        testVertex.build(player);

        // get the players trade boosts after the build
        Resource[] boostsAfter = player.getTradeBoosts();

        // assert that the vertex is owned by the player
        Player actualOwner = testVertex.getOwner();
        assertEquals(player, actualOwner);


        // assert that the player has no change to its trade boosts available
        assertArrayEquals(boostsBefore, boostsAfter);
    }

    @Test
    public void testBuildVertex2() {
        Resource[] boostsBefore = player.getTradeBoosts();
        // assert that the player had not boosts before
        Resource[] expectedBoostsBefore = {};
        assertArrayEquals(expectedBoostsBefore, boostsBefore);

        // grab a vertex adjacent to port with brick
        Vertex testVertex = vertexes.getVertex(28);

        // player builds on the vertex
        testVertex.build(player);

        // assert that the vertex is owned by the player
        Player actualOwner = testVertex.getOwner();
        assertEquals(player, actualOwner);


        // assert that the player has the expected boosts after
        Resource[] expectedBoostsAfter = {Resource.BRICK};
        Resource[] boostsAfter = player.getTradeBoosts();
        assertArrayEquals(expectedBoostsAfter, boostsAfter);
    }

    @Test
    public void testBuildVertex3() {
        // get its trade boosts before the build
        Resource[] boostsBefore = player.getTradeBoosts();
        // assert that the player had not boosts before
        Resource[] expectedBoostsBefore = {};
        assertArrayEquals(expectedBoostsBefore, boostsBefore);

        // grab a vertex adjacent to port with brick
        Vertex testVertex = vertexes.getVertex(3);

        // player builds on the vertex
        testVertex.build(player);

        // assert that the vertex is owned by the player
        Player actualOwner = testVertex.getOwner();
        assertEquals(player, actualOwner);


        // assert that the player has the expected boosts after
        Resource[] expectedBoostsAfter = {Resource.GRAIN};
        Resource[] boostsAfter = player.getTradeBoosts();
        assertArrayEquals(expectedBoostsAfter, boostsAfter);
    }

    @Test
    public void testBuildVertex4() {
        // get its trade boosts before the build
        Resource[] boostsBefore = player.getTradeBoosts();
        // assert that the player had not boosts before
        Resource[] expectedBoostsBefore = {};
        assertArrayEquals(expectedBoostsBefore, boostsBefore);

        // grab a vertex adjacent to port with brick
        Vertex testVertex = vertexes.getVertex(14);

        // player builds on the vertex
        testVertex.build(player);

        // assert that the vertex is owned by the player
        Player actualOwner = testVertex.getOwner();
        assertEquals(player, actualOwner);


        // assert that the player has the expected boosts after
        Resource[] expectedBoostsAfter = {Resource.WOOL};
        Resource[] boostsAfter = player.getTradeBoosts();
        assertArrayEquals(expectedBoostsAfter, boostsAfter);
    }

    @Test
    public void testBuildVertex5() {
        // get its trade boosts before the build
        Resource[] boostsBefore = player.getTradeBoosts();
        // assert that the player had not boosts before
        Resource[] expectedBoostsBefore = {};
        assertArrayEquals(expectedBoostsBefore, boostsBefore);

        // grab a vertex adjacent to port with brick
        Vertex testVertex = vertexes.getVertex(45);

        // player builds on the vertex
        testVertex.build(player);

        // assert that the vertex is owned by the player
        Player actualOwner = testVertex.getOwner();
        assertEquals(player, actualOwner);


        // assert that the player has the expected boosts after
        Resource[] expectedBoostsAfter = {Resource.ORE};
        Resource[] boostsAfter = player.getTradeBoosts();
        assertArrayEquals(expectedBoostsAfter, boostsAfter);
    }

    @Test
    public void testBuildVertex6() {
        // get its trade boosts before the build
        Resource[] boostsBefore = player.getTradeBoosts();
        // assert that the player had not boosts before
        Resource[] expectedBoostsBefore = {};
        assertArrayEquals(expectedBoostsBefore, boostsBefore);

        // grab a vertex adjacent to port with brick
        Vertex testVertex = vertexes.getVertex(17);

        // player builds on the vertex
        testVertex.build(player);

        // assert that the vertex is owned by the player
        Player actualOwner = testVertex.getOwner();
        assertEquals(player, actualOwner);


        // assert that the player has the expected boosts after
        Resource[] expectedBoostsAfter = {Resource.LUMBER};
        Resource[] boostsAfter = player.getTradeBoosts();
        assertArrayEquals(expectedBoostsAfter, boostsAfter);
    }

    @Test
    public void testBuildVertex7() {
        // get its trade boosts before the build
        Resource[] boostsBefore = player.getTradeBoosts();
        // assert that the player had not boosts before
        Resource[] expectedBoostsBefore = {};
        assertArrayEquals(expectedBoostsBefore, boostsBefore);

        // grab a vertex adjacent to port with brick
        Vertex testVertex = vertexes.getVertex(50);

        // player builds on the vertex
        testVertex.build(player);

        // assert that the vertex is owned by the player
        Player actualOwner = testVertex.getOwner();
        assertEquals(player, actualOwner);


        // assert that the player has the expected boosts after
        Resource[] expectedBoostsAfter = {Resource.ANY};
        Resource[] boostsAfter = player.getTradeBoosts();
        assertArrayEquals(expectedBoostsAfter, boostsAfter);
    }

    @Test
    public void testBuildVertex8() {
        vertexes.getVertex(3).build(player);
        // get its trade boosts before the build
        Resource[] boostsBefore = player.getTradeBoosts();
        // assert that the player had not boosts before
        Resource[] expectedBoostsBefore = {Resource.GRAIN};
        assertArrayEquals(expectedBoostsBefore, boostsBefore);

        // grab a vertex adjacent to port with lumber
        Vertex testVertex = vertexes.getVertex(17);

        // player builds on the vertex
        testVertex.build(player);

        // assert that the vertex is owned by the player
        Player actualOwner = testVertex.getOwner();
        assertEquals(player, actualOwner);


        // assert that the player has the expected boosts after
        Resource[] expectedBoostsAfter = {Resource.GRAIN, Resource.LUMBER};
        Resource[] boostsAfter = player.getTradeBoosts();
        assertArrayEquals(expectedBoostsAfter, boostsAfter);
    }

    @Test
    public void testBuildVertex9() {
        vertexes.getVertex(3).build(player);// grain
        vertexes.getVertex(14).build(player); // wool
        vertexes.getVertex(26).build(player); // any
        vertexes.getVertex(46).build(player); //ore
        vertexes.getVertex(50).build(player); // any
        vertexes.getVertex(48).build(player); // any
        vertexes.getVertex(38).build(player); // brick
        vertexes.getVertex(0).build(player); // any
        // get its trade boosts before the build
        Resource[] boostsBefore = player.getTradeBoosts();
        // assert that the player had not boosts before
        Resource[] expectedBoostsBefore = {Resource.GRAIN, Resource.WOOL, Resource.ANY, Resource.ORE, Resource.ANY, Resource.ANY, Resource.BRICK, Resource.ANY};
        assertArrayEquals(expectedBoostsBefore, boostsBefore);

        // grab a vertex adjacent to port with lumber
        Vertex testVertex = vertexes.getVertex(17);

        // player builds on the vertex
        testVertex.build(player);

        // assert that the vertex is owned by the player
        Player actualOwner = testVertex.getOwner();
        assertEquals(player, actualOwner);


        // assert that the player has the expected boosts after
        Resource[] expectedBoostsAfter = {Resource.GRAIN, Resource.WOOL, Resource.ANY, Resource.ORE, Resource.ANY, Resource.ANY, Resource.BRICK, Resource.ANY, Resource.LUMBER};
        Resource[] boostsAfter = player.getTradeBoosts();
        assertArrayEquals(expectedBoostsAfter, boostsAfter);
    }
}
