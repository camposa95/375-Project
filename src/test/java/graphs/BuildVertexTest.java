package graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import gamedatastructures.GameType;
import gamedatastructures.Player;
import gamedatastructures.Resource;

public class BuildVertexTest {

    private static final String VERTEX_TO_PORT_LAYOUT_FILE = "src/main/java/graphs/VertexToPortLayout.txt";

    @Test
    public void testBuildVertex1() {
        VertexGraph vertexes = new VertexGraph();
        vertexes.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, GameType.Beginner);

        // create an actual player
        Player player =  new Player(1);
        // get its trade boosts before the build
        Resource[] boostsBefore = player.getTradeBoosts();
        Resource[] expectedBoostsBefore = {};
        assertTrue(Arrays.equals(expectedBoostsBefore, boostsBefore));

        // grab a vertex not adjacent to a port
        Vertex testVertex = vertexes.getVertex(20);

        // player builds on the vertex
        testVertex.build(player);

        // get the players trade boosts after the build
        Resource[] boostsAfter = player.getTradeBoosts();

        // assert that the vertex is owned by the player
        Player expectedOwner = player;
        Player actualOwner = testVertex.getOwner();
        assertEquals(expectedOwner, actualOwner);


        // assert that the player has no change to its trade boosts available
        assertTrue(Arrays.equals(boostsBefore, boostsAfter));
    }

    @Test
    public void testBuildVertex2() {
        VertexGraph vertexes = new VertexGraph();
        vertexes.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, GameType.Beginner);

        // create an actual player
        Player player =  new Player(1);
        // get its trade boosts before the build
        Resource[] boostsBefore = player.getTradeBoosts();
        // assert that the player had not boosts before
        Resource[] expectedBoostsBefore = {};
        assertTrue(Arrays.equals(expectedBoostsBefore, boostsBefore));

        // grab a vertex adjacent to port with brick
        Vertex testVertex = vertexes.getVertex(28);

        // player builds on the vertex
        testVertex.build(player);

        // assert that the vertex is owned by the player
        Player expectedOwner = player;
        Player actualOwner = testVertex.getOwner();
        assertEquals(expectedOwner, actualOwner);


        // assert that the playerhas the expected boosts after
        Resource[] expectedBoostsAfter = {Resource.BRICK};
        Resource[] boostsAfter = player.getTradeBoosts();
        assertTrue(Arrays.equals(expectedBoostsAfter, boostsAfter));
    }

    @Test
    public void testBuildVertex3() {
        VertexGraph vertexes = new VertexGraph();
        vertexes.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, GameType.Beginner);

        // create an actual player
        Player player =  new Player(1);
        // get its trade boosts before the build
        Resource[] boostsBefore = player.getTradeBoosts();
        // assert that the player had not boosts before
        Resource[] expectedBoostsBefore = {};
        assertTrue(Arrays.equals(expectedBoostsBefore, boostsBefore));

        // grab a vertex adjacent to port with brick
        Vertex testVertex = vertexes.getVertex(3);

        // player builds on the vertex
        testVertex.build(player);

        // assert that the vertex is owned by the player
        Player expectedOwner = player;
        Player actualOwner = testVertex.getOwner();
        assertEquals(expectedOwner, actualOwner);


        // assert that the playerhas the expected boosts after
        Resource[] expectedBoostsAfter = {Resource.GRAIN};
        Resource[] boostsAfter = player.getTradeBoosts();
        assertTrue(Arrays.equals(expectedBoostsAfter, boostsAfter));
    }

    @Test
    public void testBuildVertex4() {
        VertexGraph vertexes = new VertexGraph();
        vertexes.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, GameType.Beginner);

        // create an actual player
        Player player =  new Player(1);
        // get its trade boosts before the build
        Resource[] boostsBefore = player.getTradeBoosts();
        // assert that the player had not boosts before
        Resource[] expectedBoostsBefore = {};
        assertTrue(Arrays.equals(expectedBoostsBefore, boostsBefore));

        // grab a vertex adjacent to port with brick
        Vertex testVertex = vertexes.getVertex(14);

        // player builds on the vertex
        testVertex.build(player);

        // assert that the vertex is owned by the player
        Player expectedOwner = player;
        Player actualOwner = testVertex.getOwner();
        assertEquals(expectedOwner, actualOwner);


        // assert that the playerhas the expected boosts after
        Resource[] expectedBoostsAfter = {Resource.WOOL};
        Resource[] boostsAfter = player.getTradeBoosts();
        assertTrue(Arrays.equals(expectedBoostsAfter, boostsAfter));
    }

    @Test
    public void testBuildVertex5() {
        VertexGraph vertexes = new VertexGraph();
        vertexes.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, GameType.Beginner);

        // create an actual player
        Player player =  new Player(1);
        // get its trade boosts before the build
        Resource[] boostsBefore = player.getTradeBoosts();
        // assert that the player had not boosts before
        Resource[] expectedBoostsBefore = {};
        assertTrue(Arrays.equals(expectedBoostsBefore, boostsBefore));

        // grab a vertex adjacent to port with brick
        Vertex testVertex = vertexes.getVertex(45);

        // player builds on the vertex
        testVertex.build(player);

        // assert that the vertex is owned by the player
        Player expectedOwner = player;
        Player actualOwner = testVertex.getOwner();
        assertEquals(expectedOwner, actualOwner);


        // assert that the playerhas the expected boosts after
        Resource[] expectedBoostsAfter = {Resource.ORE};
        Resource[] boostsAfter = player.getTradeBoosts();
        assertTrue(Arrays.equals(expectedBoostsAfter, boostsAfter));
    }

    @Test
    public void testBuildVertex6() {
        VertexGraph vertexes = new VertexGraph();
        vertexes.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, GameType.Beginner);

        // create an actual player
        Player player =  new Player(1);
        // get its trade boosts before the build
        Resource[] boostsBefore = player.getTradeBoosts();
        // assert that the player had not boosts before
        Resource[] expectedBoostsBefore = {};
        assertTrue(Arrays.equals(expectedBoostsBefore, boostsBefore));

        // grab a vertex adjacent to port with brick
        Vertex testVertex = vertexes.getVertex(17);

        // player builds on the vertex
        testVertex.build(player);

        // assert that the vertex is owned by the player
        Player expectedOwner = player;
        Player actualOwner = testVertex.getOwner();
        assertEquals(expectedOwner, actualOwner);


        // assert that the playerhas the expected boosts after
        Resource[] expectedBoostsAfter = {Resource.LUMBER};
        Resource[] boostsAfter = player.getTradeBoosts();
        assertTrue(Arrays.equals(expectedBoostsAfter, boostsAfter));
    }

    @Test
    public void testBuildVertex7() {
        VertexGraph vertexes = new VertexGraph();
        vertexes.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, GameType.Beginner);

        // create an actual player
        Player player =  new Player(1);
        // get its trade boosts before the build
        Resource[] boostsBefore = player.getTradeBoosts();
        // assert that the player had not boosts before
        Resource[] expectedBoostsBefore = {};
        assertTrue(Arrays.equals(expectedBoostsBefore, boostsBefore));

        // grab a vertex adjacent to port with brick
        Vertex testVertex = vertexes.getVertex(50);

        // player builds on the vertex
        testVertex.build(player);

        // assert that the vertex is owned by the player
        Player expectedOwner = player;
        Player actualOwner = testVertex.getOwner();
        assertEquals(expectedOwner, actualOwner);


        // assert that the playerhas the expected boosts after
        Resource[] expectedBoostsAfter = {Resource.ANY};
        Resource[] boostsAfter = player.getTradeBoosts();
        assertTrue(Arrays.equals(expectedBoostsAfter, boostsAfter));
    }

    @Test
    public void testBuildVertex8() {
        VertexGraph vertexes = new VertexGraph();
        vertexes.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, GameType.Beginner);

        // create an actual player already with grain boost
        Player player =  new Player(1);
        vertexes.getVertex(3).build(player);
        // get its trade boosts before the build
        Resource[] boostsBefore = player.getTradeBoosts();
        // assert that the player had not boosts before
        Resource[] expectedBoostsBefore = {Resource.GRAIN};
        assertTrue(Arrays.equals(expectedBoostsBefore, boostsBefore));

        // grab a vertex adjacent to port with lumber
        Vertex testVertex = vertexes.getVertex(17);

        // player builds on the vertex
        testVertex.build(player);

        // assert that the vertex is owned by the player
        Player expectedOwner = player;
        Player actualOwner = testVertex.getOwner();
        assertEquals(expectedOwner, actualOwner);


        // assert that the playerhas the expected boosts after
        Resource[] expectedBoostsAfter = {Resource.GRAIN, Resource.LUMBER};
        Resource[] boostsAfter = player.getTradeBoosts();
        assertTrue(Arrays.equals(expectedBoostsAfter, boostsAfter));
    }

    @Test
    public void testBuildVertex9() {
        VertexGraph vertexes = new VertexGraph();
        vertexes.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, GameType.Beginner);

        // create an actual player already with all boosts except lumber
        Player player =  new Player(1);
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
        assertTrue(Arrays.equals(expectedBoostsBefore, boostsBefore));

        // grab a vertex adjacent to port with lumber
        Vertex testVertex = vertexes.getVertex(17);

        // player builds on the vertex
        testVertex.build(player);

        // assert that the vertex is owned by the player
        Player expectedOwner = player;
        Player actualOwner = testVertex.getOwner();
        assertEquals(expectedOwner, actualOwner);


        // assert that the playerhas the expected boosts after
        Resource[] expectedBoostsAfter = {Resource.GRAIN, Resource.WOOL, Resource.ANY, Resource.ORE, Resource.ANY, Resource.ANY, Resource.BRICK, Resource.ANY, Resource.LUMBER};
        Resource[] boostsAfter = player.getTradeBoosts();
        assertTrue(Arrays.equals(expectedBoostsAfter, boostsAfter));
    }
}
