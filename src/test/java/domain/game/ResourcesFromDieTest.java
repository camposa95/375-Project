package domain.game;

import data.GameLoader;
import domain.bank.Resource;
import domain.gameboard.GameBoard;
import domain.graphs.RoadGraph;
import domain.graphs.VertexGraph;
import domain.player.Player;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ResourcesFromDieTest {

    Player player;
    GameBoard gameBoard;
    VertexGraph vertexGraph;
    RoadGraph mockedRoadGraph;
    Game game;

    @BeforeEach
    public void setup() {
        player = new Player(1);
        gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        vertexGraph = new VertexGraph(GameType.Beginner);
        mockedRoadGraph = EasyMock.createStrictMock(RoadGraph.class);
        game = new Game(gameBoard, vertexGraph, mockedRoadGraph, null, null);
    }

    @Test
    public void testResourcesFromDie_NoSettlements() {
        //Check hand before
        int before = player.hand.getResourceCount();
        //Call method
        Resource[] resources = game.resourcesFromDie(player, 6);
        player.hand.addResources(resources);
        //Check After
        int after = player.hand.getResourceCount();

        assertEquals(before, after);
        assertEquals(0,resources.length);
    }

    @Test
    public void testResourcesFromDie_1_Die() {
        int die = 10;

        vertexGraph.getVertex(0).setOwner(player);
        Resource[] expected = {Resource.ORE};
        //Check hand before
        int before = player.hand.getResourceCount();
        //Call method
        Resource[] actual = game.resourcesFromDie(player, die);
        player.hand.addResources(actual);
        //Check After
        int after = player.hand.getResourceCount();


        assertNotEquals(before, after);
        assertEquals(1,actual.length);
        assertEquals(expected[0],actual[0]);
    }

    @Test
    public void testResourcesFromDie_1_NoDie() {
        int die = 5;

        vertexGraph.getVertex(0).setOwner(player);

        //Check hand before
        int before = player.hand.getResourceCount();
        //Call method
        Resource[] actual = game.resourcesFromDie(player, die);
        player.hand.addResources(actual);
        //Check After
        int after = player.hand.getResourceCount();


        assertEquals(before, after);
        assertEquals(0,actual.length);
        //assertEquals(expected[0],actual[0]);
    }

    @Test
    public void testResourcesFromDie_Many_One_Die() {
        int die = 10;

        vertexGraph.getVertex(0).setOwner(player);
        vertexGraph.getVertex(1).setOwner(player);
        vertexGraph.getVertex(2).setOwner(player);
        vertexGraph.getVertex(8).setOwner(player);
        vertexGraph.getVertex(9).setOwner(player);
        vertexGraph.getVertex(10).setOwner(player);

        Resource[] expected = {Resource.ORE,Resource.ORE,Resource.ORE,Resource.ORE,Resource.ORE,Resource.ORE};
        //Check hand before
        int before = player.hand.getResourceCount();
        //Call method
        Resource[] actual = game.resourcesFromDie(player, die);
        player.hand.addResources(actual);
        //Check After
        int after = player.hand.getResourceCount();


        assertNotEquals(before, after);
        assertEquals(6,actual.length);
        for(int i =0; i<6 ; i++) {
            assertEquals(expected[i],actual[i]);
        }
    }

    @Test
    public void testResourcesFromDie_Many_One_NoDie() {
        int die = 5;

        vertexGraph.getVertex(0).setOwner(player);
        vertexGraph.getVertex(1).setOwner(player);
        vertexGraph.getVertex(2).setOwner(player);
        vertexGraph.getVertex(8).setOwner(player);
        vertexGraph.getVertex(9).setOwner(player);
        vertexGraph.getVertex(10).setOwner(player);


        //Check hand before
        int before = player.hand.getResourceCount();
        //Call method
        Resource[] actual = game.resourcesFromDie(player, die);
        player.hand.addResources(actual);
        //Check After
        int after = player.hand.getResourceCount();


        assertEquals(before, after);
        assertEquals(0,actual.length);

    }

    @Test
    public void testResourcesFromDie_Many_Many_Die() {
        int die = 10;

        vertexGraph.getVertex(1).setOwner(player);
        vertexGraph.getVertex(9).setOwner(player);
        vertexGraph.getVertex(14).setOwner(player);
        vertexGraph.getVertex(24).setOwner(player);

        Resource[] expected = {Resource.ORE,Resource.ORE,Resource.BRICK,Resource.BRICK};
        //Check hand before
        int before = player.hand.getResourceCount();
        //Call method
        Resource[] actual = game.resourcesFromDie(player, die);
        player.hand.addResources(actual);
        //Check After
        int after = player.hand.getResourceCount();


        assertNotEquals(before, after);
        assertEquals(4,actual.length);
        for(int i =0; i<4 ; i++) {
            assertEquals(expected[i],actual[i]);
        }
    }

    @Test
    public void testResourcesFromDie_Many_Many_NoDie() {
        int die = 5;

        vertexGraph.getVertex(1).setOwner(player);
        vertexGraph.getVertex(9).setOwner(player);
        vertexGraph.getVertex(14).setOwner(player);
        vertexGraph.getVertex(24).setOwner(player);


        //Check hand before
        int before = player.hand.getResourceCount();
        //Call method
        Resource[] actual = game.resourcesFromDie(player, die);
        player.hand.addResources(actual);
        //Check After
        int after = player.hand.getResourceCount();


        assertEquals(before, after);
        assertEquals(0,actual.length);
    }

    @Test
    public void testResourcesFromDie_Die7() {
        Game game = new Game(null, null, null,null, null);
        int die = 7; // robber

        //Call method
        Resource[] actual = game.resourcesFromDie(null, die);

        assertEquals(0,actual.length);
    }
}
