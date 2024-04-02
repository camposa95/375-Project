package domain.game;

import data.GameLoader;
import domain.bank.Bank;
import domain.bank.Resource;
import domain.gameboard.GameBoard;
import domain.gameboard.Tile;
import domain.player.Hand;
import domain.player.HarvestBooster;
import domain.player.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.easymock.EasyMock;
import domain.graphs.Road;
import domain.graphs.RoadGraph;
import domain.graphs.Vertex;
import domain.graphs.VertexGraph;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    // place settlement
    @Test
    public void testPlaceSettlement_Valid_Enough() {
        GameBoard gb = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gb);

        RoadGraph mockedRoadGraph = EasyMock.createMock(RoadGraph.class);
        int vertexId = 0;

        VertexGraph mockVertexGraph =  EasyMock.createMock(VertexGraph.class);
        Vertex mockVertex = EasyMock.createNiceMock(Vertex.class);
        Player mockPlayer = EasyMock.createMock(Player.class);

        Game game = new Game(gb,mockVertexGraph,mockedRoadGraph, null, null);
        
        EasyMock.expect(mockVertexGraph.getVertex(vertexId)).andReturn(mockVertex);
        EasyMock.expect(mockVertex.isBuildable()).andReturn(true);
        EasyMock.expect(mockVertex.getOwner()).andReturn(mockPlayer);
        mockPlayer.placeSettlementSetup();
        mockedRoadGraph.giveLongestRoadCard();
        EasyMock.replay(mockPlayer,mockVertex,mockVertexGraph, mockedRoadGraph);
 
        try{
        game.placeSettlement(vertexId,mockPlayer);
        }
        catch(InvalidPlacementException | NotEnoughResourcesException e){
            fail();
        }
        assertEquals(mockVertex.getOwner(),mockPlayer);
        EasyMock.verify(mockPlayer,mockVertex,mockVertexGraph, mockedRoadGraph);
    }
    
    @Test
    public void testPlaceSettlement_Valid_NotEnough() {
        GameBoard gb = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gb);
        RoadGraph rg = new RoadGraph();
        int vertexId = 0;

        VertexGraph mockVertexGraph =  EasyMock.createMock(VertexGraph.class);
        Vertex mockVertex = EasyMock.createMock(Vertex.class);
        Player mockPlayer = EasyMock.createMock(Player.class);

        Game game = new Game(gb, mockVertexGraph, rg, null, null);
        game.endSetup();

        EasyMock.expect(mockVertexGraph.getVertex(vertexId)).andReturn(mockVertex);
        EasyMock.expect(mockVertex.isBuildable()).andReturn(true);
        EasyMock.expect(mockVertex.isAdjacentToFriendlyRoad(mockPlayer)).andReturn(true);
        EasyMock.expect(mockPlayer.purchaseSettlement()).andReturn(false);
        EasyMock.expect(mockVertex.getOwner()).andReturn(null);

        EasyMock.replay(mockPlayer,mockVertex,mockVertexGraph);

        assertThrows(NotEnoughResourcesException.class,()-> game.placeSettlement(vertexId,mockPlayer));
        assertNotEquals(mockVertex.getOwner(),mockPlayer);
        EasyMock.verify(mockPlayer,mockVertex,mockVertexGraph);
    }

    @Test
    public void testPlaceSettlement_Invalid_Enough() {
         GameBoard gb = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gb);
        RoadGraph rg = new RoadGraph();
        int vertexId = 0;

        VertexGraph mockVertexGraph =  EasyMock.createMock(VertexGraph.class);
        Vertex mockVertex = EasyMock.createMock(Vertex.class);
        Player mockPlayer = EasyMock.createMock(Player.class);

        Game game = new Game(gb, mockVertexGraph, rg, null, null);
        EasyMock.expect(mockVertexGraph.getVertex(vertexId)).andReturn(mockVertex);
        EasyMock.expect(mockVertex.isBuildable()).andReturn(false);
        
        EasyMock.expect(mockVertex.getOwner()).andReturn(null);

        EasyMock.replay(mockPlayer,mockVertex,mockVertexGraph);

        assertThrows(InvalidPlacementException.class,()-> game.placeSettlement(vertexId,mockPlayer));
        assertNotEquals(mockVertex.getOwner(),mockPlayer);
        EasyMock.verify(mockPlayer,mockVertex,mockVertexGraph);
    }

    @Test
    public void testPlaceSettlement_Invalid_NotEnough() {
         GameBoard gb = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gb);
        RoadGraph rg = new RoadGraph();
        int vertexId = 0;

        VertexGraph mockVertexGraph =  EasyMock.createMock(VertexGraph.class);
        Vertex mockVertex = EasyMock.createMock(Vertex.class);
        Player mockPlayer = EasyMock.createMock(Player.class);

        Game game = new Game(gb, mockVertexGraph, rg, null, null);
        game.endSetup();

        EasyMock.expect(mockVertexGraph.getVertex(vertexId)).andReturn(mockVertex);
        EasyMock.expect(mockVertex.isBuildable()).andReturn(false);
        
        EasyMock.expect(mockVertex.getOwner()).andReturn(null);


        EasyMock.replay(mockPlayer,mockVertex,mockVertexGraph);

        assertThrows(InvalidPlacementException.class,()-> game.placeSettlement(vertexId,mockPlayer));
        assertNotEquals(mockVertex.getOwner(),mockPlayer);
        EasyMock.verify(mockPlayer,mockVertex,mockVertexGraph);
    }

    // place road
    @Test
    public void testPlaceRoad_Valid_Adjacent_Enough() {
        GameBoard gb = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gb);
        VertexGraph vg = new VertexGraph(GameType.Beginner);
        int vertexId = 0;
        int roadId = 0;

        RoadGraph mockRoadGraph =  EasyMock.createMock(RoadGraph.class);
        Road mockRoad = EasyMock.createNiceMock(Road.class);
        Player mockPlayer = EasyMock.createMock(Player.class);

        Game game = new Game(gb, vg, mockRoadGraph, null, null);
        EasyMock.expect(mockRoadGraph.getRoad(roadId)).andReturn(mockRoad);
        EasyMock.expect(mockRoad.isAdjacentTo(vg.getVertex(vertexId))).andReturn(true);
        EasyMock.expect(mockRoad.isBuildable()).andReturn(true);
        EasyMock.expect(mockRoad.getOwner()).andReturn(mockPlayer);
        mockPlayer.placeRoadSetup();
        mockRoadGraph.giveLongestRoadCard();
        EasyMock.replay(mockPlayer,mockRoad,mockRoadGraph);

        try{
        game.placeRoad(roadId,vertexId,mockPlayer);
        }
        catch(InvalidPlacementException | NotEnoughResourcesException e){
            fail();
        }
        assertEquals(mockRoad.getOwner(),mockPlayer);
        EasyMock.verify(mockPlayer,mockRoad,mockRoadGraph);
    }

    @Test
    public void testPlaceRoad_Valid_Adjacent_NotEnough() {
         GameBoard gb = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gb);
        VertexGraph vg = new VertexGraph(GameType.Beginner);
        int vertexId = 0;
        int roadId = 0;

        RoadGraph mockRoadGraph =  EasyMock.createMock(RoadGraph.class);
        Road mockRoad = EasyMock.createMock(Road.class);
        Player mockPlayer = EasyMock.createMock(Player.class);

        Game game = new Game(gb, vg, mockRoadGraph, null, null);
        game.endSetup();

        EasyMock.expect(mockRoadGraph.getRoad(roadId)).andReturn(mockRoad);
        EasyMock.expect(mockRoad.isBuildableBy(mockPlayer)).andReturn(true);
        EasyMock.expect(mockPlayer.purchaseRoad()).andReturn(false);
        EasyMock.expect(mockRoad.getOwner()).andReturn(null);

        EasyMock.replay(mockPlayer,mockRoad,mockRoadGraph);

        assertThrows(NotEnoughResourcesException.class,()-> game.placeRoad(roadId,vertexId,mockPlayer));
        assertNotEquals(mockRoad.getOwner(),mockPlayer);
        EasyMock.verify(mockPlayer,mockRoad,mockRoadGraph);
    }

    @Test
    public void testPlaceRoad_Valid_NotAdjacent_Enough() {
         GameBoard gb = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gb);
        VertexGraph vg = new VertexGraph(GameType.Beginner);
        int vertexId = 10;
        int roadId = 0;

        RoadGraph mockRoadGraph =  EasyMock.createMock(RoadGraph.class);
        Road mockRoad = EasyMock.createNiceMock(Road.class);
        Player mockPlayer = EasyMock.createNiceMock(Player.class);

        Game game = new Game(gb, vg, mockRoadGraph, null, null);
        EasyMock.expect(mockRoadGraph.getRoad(roadId)).andReturn(mockRoad);
        EasyMock.expect(mockRoad.isAdjacentTo(vg.getVertex(vertexId))).andReturn(false);
        EasyMock.expect(mockRoad.isBuildable()).andReturn(true);
        EasyMock.expect(mockRoad.getOwner()).andReturn(null);

        EasyMock.replay(mockPlayer,mockRoad,mockRoadGraph);

        assertThrows(InvalidPlacementException.class,()-> game.placeRoad(roadId,vertexId,mockPlayer));
        assertNotEquals(mockRoad.getOwner(),mockPlayer);
        EasyMock.verify(mockPlayer,mockRoad,mockRoadGraph);
    }

    @Test
    public void testPlaceRoad_Invalid_NotAdjacent_Enough() {
         GameBoard gb = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gb);
        VertexGraph vg = new VertexGraph(GameType.Beginner);
        int vertexId = 10;
        int roadId = 0;

        RoadGraph mockRoadGraph =  EasyMock.createMock(RoadGraph.class);
        Road mockRoad = EasyMock.createMock(Road.class);
        Player mockPlayer = EasyMock.createMock(Player.class);

        Game game = new Game(gb, vg, mockRoadGraph, null, null);
        EasyMock.expect(mockRoadGraph.getRoad(roadId)).andReturn(mockRoad);
        EasyMock.expect(mockRoad.isBuildable()).andReturn(false);
        EasyMock.expect(mockRoad.getOwner()).andReturn(null);

        EasyMock.replay(mockPlayer,mockRoad,mockRoadGraph);

        assertThrows(InvalidPlacementException.class,()-> game.placeRoad(roadId,vertexId,mockPlayer));
        assertNotEquals(mockRoad.getOwner(),mockPlayer);
        EasyMock.verify(mockPlayer,mockRoad,mockRoadGraph);
    }

    @Test
    public void testPlaceRoad_Invalid_Adjacent_Enough() {
        GameBoard gb = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gb);
        VertexGraph vg = new VertexGraph(GameType.Beginner);
        int vertexId = 0;
        int roadId = 0;

        RoadGraph mockRoadGraph =  EasyMock.createMock(RoadGraph.class);
        Road mockRoad = EasyMock.createMock(Road.class);
        Player mockPlayer = EasyMock.createMock(Player.class);

        Game game = new Game(gb, vg, mockRoadGraph, null, null);
        EasyMock.expect(mockRoadGraph.getRoad(roadId)).andReturn(mockRoad);
        EasyMock.expect(mockRoad.isBuildable()).andReturn(false);
        EasyMock.expect(mockRoad.getOwner()).andReturn(null);

        EasyMock.replay(mockPlayer,mockRoad,mockRoadGraph);

        assertThrows(InvalidPlacementException.class,()-> game.placeRoad(roadId,vertexId,mockPlayer));
        assertNotEquals(mockRoad.getOwner(),mockPlayer);
        EasyMock.verify(mockPlayer,mockRoad,mockRoadGraph);
    }

    @Test
    public void testPlaceRoad_Invalid_Adjacent_NotEnough() {
        GameBoard gb = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gb);
        VertexGraph vg = new VertexGraph(GameType.Beginner);
        int vertexId = 0;
        int roadId = 0;

        RoadGraph mockRoadGraph =  EasyMock.createMock(RoadGraph.class);
        Road mockRoad = EasyMock.createMock(Road.class);
        Player mockPlayer = EasyMock.createMock(Player.class);

        Game game = new Game(gb, vg, mockRoadGraph, null, null);
        game.endSetup();

        EasyMock.expect(mockRoadGraph.getRoad(roadId)).andReturn(mockRoad);
        EasyMock.expect(mockRoad.isBuildableBy(mockPlayer)).andReturn(false);
        EasyMock.expect(mockRoad.getOwner()).andReturn(null);

        EasyMock.replay(mockPlayer,mockRoad,mockRoadGraph);


        assertThrows(InvalidPlacementException.class,()-> game.placeRoad(roadId,vertexId,mockPlayer));
        assertNotEquals(mockRoad.getOwner(),mockPlayer);
        EasyMock.verify(mockPlayer,mockRoad,mockRoadGraph);
    }

    @Test
    public void testPlaceRoad_Invalid_NotAdjacent_NotEnough() {
        GameBoard gb = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gb);
        VertexGraph vg = new VertexGraph(GameType.Beginner);
        int vertexId = 10;
        int roadId = 0;

        RoadGraph mockRoadGraph =  EasyMock.createMock(RoadGraph.class);
        Road mockRoad = EasyMock.createMock(Road.class);
        Player mockPlayer = EasyMock.createMock(Player.class);

        Game game = new Game(gb, vg, mockRoadGraph, null, null);
        game.endSetup();

        EasyMock.expect(mockRoadGraph.getRoad(roadId)).andReturn(mockRoad);
        EasyMock.expect(mockRoad.isBuildableBy(mockPlayer)).andReturn(false);
        EasyMock.expect(mockRoad.getOwner()).andReturn(null);

        EasyMock.replay(mockPlayer,mockRoad,mockRoadGraph);

        assertThrows(InvalidPlacementException.class,()-> game.placeRoad(roadId,vertexId,mockPlayer));
        assertNotEquals(mockRoad.getOwner(),mockPlayer);
        EasyMock.verify(mockPlayer,mockRoad,mockRoadGraph);
    }

    @Test
    public void testPlaceRoad_NonSetup_ValidPlacement_NotEnoughResources() {
        GameBoard gb = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gb);
        VertexGraph vg = new VertexGraph(GameType.Beginner);
        int vertexId = 0;
        int roadId = 0;

        RoadGraph mockRoadGraph =  EasyMock.createMock(RoadGraph.class);
        Road mockRoad = EasyMock.createMock(Road.class);
        Player player = new Player(1);

        Game game = new Game(gb, vg, mockRoadGraph, null, null);
        game.endSetup();

        EasyMock.expect(mockRoadGraph.getRoad(roadId)).andReturn(mockRoad);
        EasyMock.expect(mockRoad.isBuildableBy(player)).andReturn(true);
        EasyMock.expect(mockRoad.getOwner()).andReturn(null);

        EasyMock.replay(mockRoad,mockRoadGraph);

        assertThrows(NotEnoughResourcesException.class,()-> game.placeRoad(roadId,vertexId,player));
        assertNotEquals(mockRoad.getOwner(),player);
        EasyMock.verify(mockRoad,mockRoadGraph);
    }

    // resourcesFromVertex
    @Test
    public void testResourcesFromVertex_NoSettlements_ExpectNoResources() {
        GameBoard gb = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gb);
        VertexGraph vg = new VertexGraph(GameType.Beginner);
        RoadGraph rg = new RoadGraph();
        int vertexId = 0;

        Player mockPlayer = EasyMock.createMock(Player.class);

        Game game = new Game(gb, vg, rg, null, null);

        Resource[] expected = new Resource[0];
        Resource[] actual = game.resourcesFromVertex(mockPlayer,vertexId);
        EasyMock.replay(mockPlayer);

        assertArrayEquals(expected, actual);
        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testResourcesFromVertex_HasSettlementOnCorner_Expect1Resource() {
         GameBoard gb = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gb);
        VertexGraph vg = new VertexGraph(GameType.Beginner);
        RoadGraph rg = new RoadGraph();
        int vertexId = 0;

        Player mockPlayer = EasyMock.createMock(Player.class);


        Game game = new Game(gb, vg, rg, null, null);
        vg.getVertex(vertexId).setOwner(mockPlayer);

        Resource[] expected = {Resource.ORE};
        Resource[] actual = game.resourcesFromVertex(mockPlayer,vertexId);
        EasyMock.replay(mockPlayer);


        assertArrayEquals(expected, actual);
        assertNotNull(game.resourcesFromVertex(mockPlayer,vertexId));
        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testResourcesFromVertex_HasSettlementOnEdge_Expect2Resources() {
         GameBoard gb = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gb);
        VertexGraph vg = new VertexGraph(GameType.Beginner);
        RoadGraph rg = new RoadGraph();
        int vertexId = 2;

        Player mockPlayer = EasyMock.createMock(Player.class);


        Game game = new Game(gb, vg, rg, null, null);
        vg.getVertex(vertexId).setOwner(mockPlayer);

        Resource[] expected = {Resource.ORE, Resource.WOOL};
        Resource[] actual = game.resourcesFromVertex(mockPlayer,vertexId);
        EasyMock.replay(mockPlayer);


        assertArrayEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testResourcesFromVertex_HasSettlementOnBoard_Expect3Resources() {
        GameBoard gb = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gb);
        VertexGraph vg = new VertexGraph(GameType.Beginner);
        RoadGraph rg = new RoadGraph();
        int vertexId = 10;

        Player mockPlayer = EasyMock.createMock(Player.class);


        Game game = new Game(gb, vg, rg, null, null);
        vg.getVertex(vertexId).setOwner(mockPlayer);

        Resource[] expected = {Resource.ORE, Resource.WOOL,Resource.BRICK};
        Resource[] actual = game.resourcesFromVertex(mockPlayer,vertexId);
        EasyMock.replay(mockPlayer);


        assertArrayEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testPlaceSettlement_NotMocked() {
        GameBoard gb = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gb);
        int vertexId = 0;

        VertexGraph vertexGraph = new VertexGraph(GameType.Beginner);
        RoadGraph rg = new RoadGraph();
        GameLoader.initializeGraphs(rg, vertexGraph);

        Bank bank = new Bank();
        Player player = new Player(1, bank);

        Game game = new Game(gb, vertexGraph, rg, null, bank);

        try{
        game.placeSettlement(vertexId,player);
        }
        catch(InvalidPlacementException | NotEnoughResourcesException e){
            fail();
        }
        assertEquals(vertexGraph.getVertex(vertexId).getOwner(),player);

        //regular
        game.endSetup();
        player.hand.addResources(new Resource[] {Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN});
        vertexId = 3;
        // make sure it is adjacent to a friendly road
        rg.getRoad(3).setOwner(player);
        try{
            game.placeSettlement(vertexId,player);
            }
            catch(InvalidPlacementException | NotEnoughResourcesException e){
                fail();
            }
        assertEquals(vertexGraph.getVertex(vertexId).getOwner(),player);
    }

    @Test
    public void testPlaceRoad_NotMocked() {
        GameBoard gb = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gb);
        int vertexId = 0;
        int roadID = 0;

        VertexGraph vertexGraph = new VertexGraph(GameType.Beginner);
        RoadGraph roadgraph = new RoadGraph();
        GameLoader.initializeGraphs(roadgraph, vertexGraph);
        Bank bank = new Bank();
        Player player = new Player(1, bank);

        Game game = new Game(gb, vertexGraph, roadgraph, null, bank);

        //setup
        try{
        game.placeRoad(roadID,vertexId,player);
        }
        catch(InvalidPlacementException | NotEnoughResourcesException e){
            fail();
        }
        assertEquals(roadgraph.getRoad(roadID).getOwner(),player);

        //regular
        game.endSetup();
        player.hand.addResources(new Resource[] {Resource.BRICK, Resource.LUMBER});
        vertexGraph.getVertex(vertexId).setOwner(player);
        roadID = 6;
        try{
            game.placeRoad(roadID,vertexId,player);
            }
            catch(InvalidPlacementException | NotEnoughResourcesException e){
                fail();
            }
        assertEquals(roadgraph.getRoad(roadID).getOwner(),player);
    }

    @Test
    public void testDistributeResources_NotMocked() {
        GameBoard gb = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gb);
        RoadGraph roadgraph = new RoadGraph();
        int vertexId = 0;
        int die = 10;

        Bank bank = new Bank();
        Player player = new Player(1, new HarvestBooster(), bank);
        VertexGraph vertexGraph = new VertexGraph();
        vertexGraph.initializeVertexToVertexAdjacency(VERTEX_FILE);
        vertexGraph.initializeVertexToRoadAdjacency(roadgraph, VERTEXROAD_FILE);
        roadgraph.initializeRoadToRoadAdjacency(ROAD_FILE);
        roadgraph.initializeRoadToVertexAdjacency(vertexGraph, ROADVERTEX_FILE);
        vertexGraph.initializeVertexToPortAdjacency(VERTEXPORT_FILE, GameType.Beginner);

        Player player = new Player(1);

        Game game = new Game(gb,vertexGraph,roadgraph,null);
        vertexGraph.getVertex(vertexId).build(player);

        Resource[] expected = {Resource.ORE};
        game.distributeResources(player,vertexId);
        assertTrue(player.hand.removeResources(expected));
        assertNotNull(game.resourcesFromVertex(player,vertexId));
        
        game.endSetup();
        game.distributeResources(player,die);
        assertTrue(player.hand.removeResources(expected));
        assertNotNull(game.resourcesFromVertex(player,vertexId));
    }
 
    // resourcesFromDie
    @Test
    public void testResourcesFromDie_NoSettlements() {
        Player player = new Player(1);
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        VertexGraph vertexGraph = new VertexGraph(GameType.Beginner);
        RoadGraph mockedRoadGraph = EasyMock.createStrictMock(RoadGraph.class);
        Game game = new Game(gameBoard, vertexGraph, mockedRoadGraph, null, null);

        //Check hand before
        int before = player.hand.getResourceCardCount();
        //Call method
        Resource[] resources = game.resourcesFromDie(player, 6);
        player.hand.addResources(resources);
        //Check After
        int after = player.hand.getResourceCardCount();

        assertEquals(before, after);
        assertEquals(0,resources.length);
    }

    @Test
    public void testResourcesFromDie_1_Die() {
        Player player = new Player(1);
        GameBoard gameBoard = new GameBoard(GameType.Beginner, LAYOUT_FILE);
        VertexGraph vertexGraph = new VertexGraph();
        vertexGraph.initializeVertexToPortAdjacency(VERTEXPORT_FILE, GameType.Beginner);
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        RoadGraph mockedRoadGraph = EasyMock.createStrictMock(RoadGraph.class);
        Game game = new Game(gameBoard, vertexGraph, mockedRoadGraph, null, null);
        int die = 10;

        vertexGraph.getVertex(0).build(player);
        Resource[] expected = {Resource.ORE};
        //Check hand before
        int before = player.hand.getResourceCardCount();
        //Call method
        Resource[] actual = game.resourcesFromDie(player, die);
        player.hand.addResources(actual);
        //Check After
        int after = player.hand.getResourceCardCount();


        assertNotEquals(before, after);
        assertEquals(1,actual.length);
        assertEquals(expected[0],actual[0]);
    }

    @Test
    public void testResourcesFromDie_1_NoDie() {
        Player player = new Player(1);
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        VertexGraph vertexGraph = new VertexGraph(GameType.Beginner);
        RoadGraph mockedRoadGraph = EasyMock.createStrictMock(RoadGraph.class);
        Game game = new Game(gameBoard, vertexGraph, mockedRoadGraph, null, null);
        int die = 5;

        vertexGraph.getVertex(0).setOwner(player);

        //Check hand before
        int before = player.hand.getResourceCardCount();
        //Call method
        Resource[] actual = game.resourcesFromDie(player, die);
        player.hand.addResources(actual);
        //Check After
        int after = player.hand.getResourceCardCount();


        assertEquals(before, after);
        assertEquals(0,actual.length);
        //assertEquals(expected[0],actual[0]);
    }

    @Test
    public void testResourcesFromDie_Many_One_Die() {
        Player player = new Player(1);
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        VertexGraph vertexGraph = new VertexGraph();
        vertexGraph.initializeVertexToPortAdjacency(VERTEXPORT_FILE, GameType.Beginner);
        RoadGraph mockedRoadGraph = EasyMock.createStrictMock(RoadGraph.class);
        Game game = new Game(gameBoard, vertexGraph, mockedRoadGraph, null, null);
        int die = 10;

        vertexGraph.getVertex(0).build(player);
        vertexGraph.getVertex(1).build(player);
        vertexGraph.getVertex(2).build(player);
        vertexGraph.getVertex(8).build(player);
        vertexGraph.getVertex(9).build(player);
        vertexGraph.getVertex(10).build(player);
      
        Resource[] expected = {Resource.ORE,Resource.ORE,Resource.ORE,Resource.ORE,Resource.ORE,Resource.ORE};
        //Check hand before
        int before = player.hand.getResourceCardCount();
        //Call method
        Resource[] actual = game.resourcesFromDie(player, die);
        player.hand.addResources(actual);
        //Check After
        int after = player.hand.getResourceCardCount();


        assertNotEquals(before, after);
        assertEquals(6,actual.length);
        for(int i =0; i<6 ; i++) {
            assertEquals(expected[i],actual[i]);
        }
    }

    @Test
    public void testResourcesFromDie_Many_One_NoDie() {
        Player player = new Player(1);
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        VertexGraph vertexGraph = new VertexGraph(GameType.Beginner);
        RoadGraph mockedRoadGraph = EasyMock.createStrictMock(RoadGraph.class);
        Game game = new Game(gameBoard, vertexGraph, mockedRoadGraph, null, null);
        int die = 5;

        vertexGraph.getVertex(0).setOwner(player);
        vertexGraph.getVertex(1).setOwner(player);
        vertexGraph.getVertex(2).setOwner(player);
        vertexGraph.getVertex(8).setOwner(player);
        vertexGraph.getVertex(9).setOwner(player);
        vertexGraph.getVertex(10).setOwner(player);


        //Check hand before
        int before = player.hand.getResourceCardCount();
        //Call method
        Resource[] actual = game.resourcesFromDie(player, die);
        player.hand.addResources(actual);
        //Check After
        int after = player.hand.getResourceCardCount();


        assertEquals(before, after);
        assertEquals(0,actual.length);

    }

    @Test
    public void testResourcesFromDie_Many_Many_Die() {
        Player player = new Player(1);
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        VertexGraph vertexGraph = new VertexGraph(GameType.Beginner);
        vertexGraph.initializeVertexToPortAdjacency(VERTEXPORT_FILE, GameType.Beginner);
        RoadGraph mockedRoadGraph = EasyMock.createStrictMock(RoadGraph.class);
        Game game = new Game(gameBoard, vertexGraph, mockedRoadGraph, null, null);
        int die = 10;

        vertexGraph.getVertex(1).build(player);
        vertexGraph.getVertex(9).build(player);
        vertexGraph.getVertex(14).build(player);
        vertexGraph.getVertex(24).build(player);
    
        Resource[] expected = {Resource.ORE,Resource.ORE,Resource.BRICK,Resource.BRICK};
        //Check hand before
        int before = player.hand.getResourceCardCount();
        //Call method
        Resource[] actual = game.resourcesFromDie(player, die);
        player.hand.addResources(actual);
        //Check After
        int after = player.hand.getResourceCardCount();


        assertNotEquals(before, after);
        assertEquals(4,actual.length);
        for(int i =0; i<4 ; i++) {
            assertEquals(expected[i],actual[i]);
        }
    }

    @Test
    public void testResourcesFromDie_withSettlementWithMine_expectThreeOre() {
        Player player = new Player(1);
        GameBoard gameBoard = new GameBoard(GameType.Beginner, LAYOUT_FILE);
        VertexGraph vertexGraph = new VertexGraph();
        vertexGraph.initializeVertexToPortAdjacency(VERTEXPORT_FILE, GameType.Beginner);
        RoadGraph mockedRoadGraph = EasyMock.createStrictMock(RoadGraph.class);
        Game game = new Game(gameBoard, vertexGraph, mockedRoadGraph,null);
        int die = 10;

        vertexGraph.getVertex(1).build(player);
        vertexGraph.getVertex(1).buildDistrict(player, DistrictType.MINE);

        Resource[] expected = {Resource.ORE,Resource.ORE,Resource.ORE};
        //Check hand before
        int before = player.hand.getResourceCardCount();
        //Call method
        Resource[] actual = game.resourcesFromDie(player, die);
        player.hand.addResources(actual);
        //Check After
        int after = player.hand.getResourceCardCount();


        assertNotEquals(before, after);
        assertEquals(3,actual.length);
        for(int i =0; i<3; i++) {
            assertEquals(expected[i],actual[i]);
        }
    }

    @Test
    public void testResourcesFromDie_Many_Many_NoDie() {
        Player player = new Player(1);
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        VertexGraph vertexGraph = new VertexGraph(GameType.Beginner);
        RoadGraph mockedRoadGraph = EasyMock.createStrictMock(RoadGraph.class);
        Game game = new Game(gameBoard, vertexGraph, mockedRoadGraph, null, null);
        int die = 5;

        vertexGraph.getVertex(1).setOwner(player);
        vertexGraph.getVertex(9).setOwner(player);
        vertexGraph.getVertex(14).setOwner(player);
        vertexGraph.getVertex(24).setOwner(player);
    
       
        //Check hand before
        int before = player.hand.getResourceCardCount();
        //Call method
        Resource[] actual = game.resourcesFromDie(player, die);
        player.hand.addResources(actual);
        //Check After
        int after = player.hand.getResourceCardCount();


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

    @Test 
    public void testDropCards_empty() {

        GameBoard gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        VertexGraph vertexGraph = EasyMock.createStrictMock(VertexGraph.class);
        RoadGraph mockedRoadGraph = EasyMock.createStrictMock(RoadGraph.class);
        Game game = new Game(gameBoard, vertexGraph, mockedRoadGraph, null, null);

        //Create Hashmap
        HashMap<Player,Resource[]> testHash = new HashMap<>();
        
        //Replay
        EasyMock.replay(gameBoard, vertexGraph, mockedRoadGraph);
        
        //Call method
        try{ 
            game.dropCards(testHash);
        } catch( Exception e) {
            fail();
        }
        
        //Verify
        EasyMock.verify(gameBoard, vertexGraph, mockedRoadGraph);
    }

    @Test
    public void testDropCards_1player_nothing() {

        GameBoard gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        VertexGraph vertexGraph = EasyMock.createStrictMock(VertexGraph.class);
        RoadGraph mockedRoadGraph = EasyMock.createStrictMock(RoadGraph.class);
        Player mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        Game game = new Game(gameBoard, vertexGraph, mockedRoadGraph, null, null);
        
       
        //Replay
        EasyMock.replay(gameBoard, vertexGraph, mockedRoadGraph,mockedPlayer1);
        
        //Create Hashmap

        Resource[] resources = new Resource[0];
         
        HashMap<Player,Resource[]> testHash = new HashMap<>();
        testHash.put(mockedPlayer1, resources);

        //Call method
        try{ 
            game.dropCards(testHash);
        } catch( Exception e) {
            //Make sure nothing is thrown
            e.printStackTrace();
            fail();
        }
        
        //Verify
        EasyMock.verify(gameBoard, vertexGraph, mockedRoadGraph,mockedPlayer1);
    }

    @Test
    public void testDropCards_1player_1card() {

        GameBoard gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        VertexGraph vertexGraph = EasyMock.createStrictMock(VertexGraph.class);
        RoadGraph mockedRoadGraph = EasyMock.createStrictMock(RoadGraph.class);
        Player mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        Hand mockedHand1 = EasyMock.createStrictMock(Hand.class);
        mockedPlayer1.hand = mockedHand1;
        Game game = new Game(gameBoard, vertexGraph, mockedRoadGraph, null, null);
       
        //Expect 
        Resource[] resources = {Resource.WOOL};
        EasyMock.expect(mockedPlayer1.hand.removeResources(resources)).andReturn(true);
        //Replay
        EasyMock.replay(gameBoard, vertexGraph, mockedRoadGraph,mockedPlayer1,mockedHand1);
        
        //Create Hashmap
        HashMap<Player,Resource[]> testHash = new HashMap<>();
        testHash.put(mockedPlayer1, resources);

        //Call method
        try{ 
            game.dropCards(testHash);
        } catch( Exception e) {
            //Make sure nothing is thrown
            e.printStackTrace();
            fail();
        }
        
        //Verify
        EasyMock.verify(gameBoard, vertexGraph, mockedRoadGraph,mockedPlayer1,mockedHand1);
    }

    @Test
    public void testDropCards_ManyPlayer_MixCard() {

        GameBoard gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        VertexGraph vertexGraph = EasyMock.createStrictMock(VertexGraph.class);
        RoadGraph mockedRoadGraph = EasyMock.createStrictMock(RoadGraph.class);
        Player mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        Hand mockedHand1 = EasyMock.createStrictMock(Hand.class);
        Player mockedPlayer2 = EasyMock.createStrictMock(Player.class);
        Hand mockedHand2 = EasyMock.createStrictMock(Hand.class);
        Player mockedPlayer3 = EasyMock.createStrictMock(Player.class);
        Hand mockedHand3 = EasyMock.createStrictMock(Hand.class);
        //left empty
        mockedPlayer1.hand = mockedHand1;
        //5 cards
        mockedPlayer2.hand = mockedHand2;
        //max cards possible(22 of each)
        mockedPlayer3.hand = mockedHand3;
        Game game = new Game(gameBoard, vertexGraph, mockedRoadGraph, null, null);
       
        //Setup resources
        Resource[] resources1 = new Resource[0];
        Resource[] resources2 = {Resource.WOOL,Resource.BRICK, Resource.GRAIN, Resource.LUMBER, Resource.WOOL};
        Resource[] resources3 = new Resource[110];
        for(int i = 0; i<110; i+=5){
            resources3[i] = Resource.WOOL;
            resources3[i+1] = Resource.BRICK;
            resources3[i+2] =  Resource.GRAIN;
            resources3[i+3] = Resource.LUMBER;
            resources3[i+4] = Resource.WOOL;
        }
        //Expect 
        EasyMock.expect(mockedPlayer2.hand.removeResources(resources2)).andReturn(true);
        EasyMock.expect(mockedPlayer3.hand.removeResources(resources3)).andReturn(true);
        //Replay
        EasyMock.replay(gameBoard, vertexGraph, mockedRoadGraph,mockedPlayer1,mockedHand1,mockedPlayer2,mockedHand2,mockedPlayer3,mockedHand3);
        
        //Create Hashmap
        HashMap<Player,Resource[]> testHash = new HashMap<>();
        testHash.put(mockedPlayer1, resources1);
        testHash.put(mockedPlayer2, resources2);
        testHash.put(mockedPlayer3, resources3);

        //Call method
        try{ 
            game.dropCards(testHash);
        } catch( Exception e) {
            //Make sure nothing is thrown
            e.printStackTrace();
            fail();
        }
        
        //Verify
        EasyMock.verify(gameBoard, vertexGraph, mockedRoadGraph,mockedPlayer1,mockedHand1,mockedPlayer2,mockedHand2,mockedPlayer3,mockedHand3);
    }

    @Test
    public void testDropCards_ManyPlayer_SameResources() {

        GameBoard gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        VertexGraph vertexGraph = EasyMock.createStrictMock(VertexGraph.class);
        RoadGraph mockedRoadGraph = EasyMock.createStrictMock(RoadGraph.class);
        Player mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        Hand mockedHand1 = EasyMock.createStrictMock(Hand.class);
        Player mockedPlayer2 = EasyMock.createStrictMock(Player.class);
        Hand mockedHand2 = EasyMock.createStrictMock(Hand.class);
        Player mockedPlayer3 = EasyMock.createStrictMock(Player.class);
        Hand mockedHand3 = EasyMock.createStrictMock(Hand.class);
        Player mockedPlayer4 = EasyMock.createStrictMock(Player.class);
        Hand mockedHand4 = EasyMock.createStrictMock(Hand.class);
        Player mockedPlayer5 = EasyMock.createStrictMock(Player.class);
        Hand mockedHand5 = EasyMock.createStrictMock(Hand.class);
        Player mockedPlayer6 = EasyMock.createStrictMock(Player.class);
        Hand mockedHand6 = EasyMock.createStrictMock(Hand.class);

        mockedPlayer1.hand = mockedHand1;
        mockedPlayer2.hand = mockedHand2;
        mockedPlayer3.hand = mockedHand3;
        mockedPlayer4.hand = mockedHand4;
        mockedPlayer5.hand = mockedHand5;
        mockedPlayer6.hand = mockedHand6;

        Game game = new Game(gameBoard, vertexGraph, mockedRoadGraph, null, null);
       
        //Setup resources
        Resource[] resources = {Resource.WOOL};
       
        //Expect 
        EasyMock.expect(mockedPlayer1.hand.removeResources(resources)).andReturn(true);
        EasyMock.expect(mockedPlayer2.hand.removeResources(resources)).andReturn(true);
        EasyMock.expect(mockedPlayer3.hand.removeResources(resources)).andReturn(true);
        EasyMock.expect(mockedPlayer4.hand.removeResources(resources)).andReturn(true);
        EasyMock.expect(mockedPlayer5.hand.removeResources(resources)).andReturn(true);
        EasyMock.expect(mockedPlayer6.hand.removeResources(resources)).andReturn(true);
        //Replay
        EasyMock.replay(gameBoard, vertexGraph, mockedRoadGraph,mockedPlayer1,mockedHand1,mockedPlayer2,mockedHand2,mockedPlayer3,mockedHand3
        ,mockedPlayer4,mockedHand4,mockedPlayer5,mockedHand5,mockedPlayer6,mockedHand6);
        
        //Create Hashmap
        HashMap<Player,Resource[]> testHash = new HashMap<>();
        testHash.put(mockedPlayer1, resources);
        testHash.put(mockedPlayer2, resources);
        testHash.put(mockedPlayer3, resources);
        testHash.put(mockedPlayer4, resources);
        testHash.put(mockedPlayer5, resources);
        testHash.put(mockedPlayer6, resources);

        //Call method
        try{ 
            game.dropCards(testHash);
        } catch( Exception e) {
            //Make sure nothing is thrown
            e.printStackTrace();
            fail();
        }
        
        //Verify
        EasyMock.verify(gameBoard, vertexGraph, mockedRoadGraph,mockedPlayer1,mockedHand1,mockedPlayer2,mockedHand2,mockedPlayer3,mockedHand3
        ,mockedPlayer4,mockedHand4,mockedPlayer5,mockedHand5,mockedPlayer6,mockedHand6);
    }

    @Test
    void testDropCards_1player_Invalid() {

        GameBoard gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        VertexGraph vertexGraph = EasyMock.createStrictMock(VertexGraph.class);
        RoadGraph mockedRoadGraph = EasyMock.createStrictMock(RoadGraph.class);
        Player mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        Hand mockedHand1 = EasyMock.createStrictMock(Hand.class);
        mockedPlayer1.hand = mockedHand1;
        Game game = new Game(gameBoard, vertexGraph, mockedRoadGraph, null, null);
       
        //Expect 
        Resource[] resources = {Resource.WOOL};
        EasyMock.expect(mockedPlayer1.hand.removeResources(resources)).andReturn(false);
        //Replay
        EasyMock.replay(gameBoard, vertexGraph, mockedRoadGraph,mockedPlayer1,mockedHand1);
        
        //Create Hashmap
        HashMap<Player,Resource[]> testHash = new HashMap<>();
        testHash.put(mockedPlayer1, resources);

        //Call method
       assertThrows(IllegalArgumentException.class, ()-> game.dropCards(testHash),"DropCards was called on player with not enough cards - synchronization issue");
           
        //Verify
        EasyMock.verify(gameBoard, vertexGraph, mockedRoadGraph,mockedPlayer1,mockedHand1);
    }

    // move robber
    @Test 
    void testMoveRobber_without() {

        GameBoard gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        Tile tile1 = EasyMock.createStrictMock(Tile.class);
        Tile tile2 = EasyMock.createStrictMock(Tile.class);
        Game game = new Game(gameBoard, null, null , null, null);
        int tileNum = 0;

        //Expect 
        
        EasyMock.expect(gameBoard.getTiles()).andReturn(new Tile[]{tile1});
        EasyMock.expect(gameBoard.getRobberTile()).andReturn(tile2);
        tile1.setRobber(true);
        tile2.setRobber(false);
        gameBoard.setRobberTile(tile1);
        
        //Replay
        EasyMock.replay(gameBoard, tile1, tile2);
        
        //Call method
        try{ game.moveRobber(tileNum); }
        catch(Exception e) {
            e.printStackTrace();
            fail();
        }

        //Verify
        EasyMock.verify(gameBoard, tile1, tile2);
    }

    @Test
    void testMoveRobber_with() {

        GameBoard gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        Tile tile1 = EasyMock.createStrictMock(Tile.class);
        Game game = new Game(gameBoard, null, null , null, null);
        int tileNum = 0;

        //Expect 
        EasyMock.expect(gameBoard.getTiles()).andReturn(new Tile[]{tile1});
        EasyMock.expect(gameBoard.getRobberTile()).andReturn(tile1);

        //Replay
        EasyMock.replay(gameBoard, tile1);
        
        //Call method
        assertThrows(InvalidPlacementException.class,()-> game.moveRobber(tileNum));

        //Verify
        EasyMock.verify(gameBoard, tile1);
    }

    @Test
    void testMoveRobber_invalid() {

        GameBoard gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        Game game = new Game(gameBoard, null, null , null, null);

        //Replay
        EasyMock.replay(gameBoard);
        
        //Call method
        assertThrows(RuntimeException.class,()-> game.moveRobber(-1));
        assertThrows(RuntimeException.class,()-> game.moveRobber(19));

        //Verify
        EasyMock.verify(gameBoard);
    }

    // Steal from player
    @Test 
    void test_StealFromPlayer_None() {

        GameBoard gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        Player mockedRobber = EasyMock.createStrictMock(Player.class);
        Player mockedVictim = EasyMock.createStrictMock(Player.class);
        mockedVictim.hand = EasyMock.createStrictMock(Hand.class);
        Game game = new Game(gameBoard, null, null , null, null);

        //Expect 
        EasyMock.expect(mockedVictim.hand.getResourceCardCount()).andReturn(0);
        //Replay
        EasyMock.replay(gameBoard, mockedRobber, mockedVictim);
        
        //Call method
        assertThrows(NotEnoughResourcesException.class, ()-> game.stealFromPlayer(mockedRobber,mockedVictim));
        
        //Verify
        EasyMock.verify(gameBoard, mockedRobber, mockedVictim);
    }

    @Test
    void test_StealFromPlayer_One() {

        GameBoard gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        Player robber = new Player(1);
        Player robbed = new Player(2);
        Game game = new Game(gameBoard, null, null , null, null);

        EasyMock.replay(gameBoard);
        robbed.hand.addResource(Resource.WOOL, 1);
        
        int robberBefore = robber.hand.getResourceCardCount();
        int robbedBefore = robbed.hand.getResourceCardCount();
        
        //Call method
        try{ game.stealFromPlayer(robber,robbed); }
        catch(Exception e) {
            e.printStackTrace();
            fail();
        }
        
        int robberAfter = robber.hand.getResourceCardCount();
        int robbedAfter = robbed.hand.getResourceCardCount();

        assertEquals(robberBefore, 0);
        assertEquals(robbedBefore,1);
        assertEquals(robberAfter, 1);
        assertEquals(robbedAfter,0);
        EasyMock.verify(gameBoard);
    }

    @Test
    void test_StealFromPlayer_Many() {

        GameBoard gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        Player robber = new Player(1);
        Player robbed = new Player(2);
        Game game = new Game(gameBoard, null, null , null, null);

        EasyMock.replay(gameBoard);
        robbed.hand.addResources(new Resource[]{Resource.WOOL,Resource.BRICK,Resource.LUMBER});
        
        int robberBefore = robber.hand.getResourceCardCount();
        int robbedBefore = robbed.hand.getResourceCardCount();
        
        //Call method
        try{ game.stealFromPlayer(robber,robbed); }
        catch(Exception e) {
            e.printStackTrace();
            fail();
        }
        
        int robberAfter = robber.hand.getResourceCardCount();
        int robbedAfter = robbed.hand.getResourceCardCount();

        assertEquals(robberBefore, 0);
        assertEquals(robbedBefore,3);
        assertEquals(robberAfter, 1);
        assertEquals(robbedAfter,2);
        EasyMock.verify(gameBoard);
    }

    // Get owners from tile
    @Test 
    void test_GetPlayersFromTile_None() {

        GameBoard gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        VertexGraph mockVertexGraph = EasyMock.createStrictMock(VertexGraph.class);
        Vertex mockVertex = EasyMock.createStrictMock(Vertex.class);
        Game game = new Game(gameBoard, mockVertexGraph, null, null, null);
        int tileId = 0;

        //Expect
        List<Integer> expect = new ArrayList<>();
        expect.add(0);
        EasyMock.expect(gameBoard.getTileVertexIDs(tileId)).andReturn(expect);
        EasyMock.expect(mockVertexGraph.getVertex(0)).andReturn(mockVertex);
        EasyMock.expect(mockVertex.isOccupied()).andReturn(false);
        //Replay
        EasyMock.replay(gameBoard, mockVertexGraph, mockVertex);

        Player[] actual = null;                  
        //Call method
        try {
            actual = game.getPlayersFromTile(tileId);
        }
        catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        assertEquals(actual.length, 0);
        EasyMock.verify(gameBoard, mockVertexGraph, mockVertex);
    }

    @Test
    void test_GetPlayersFromTile_One() {

        GameBoard gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        VertexGraph mockVertexGraph = EasyMock.createStrictMock(VertexGraph.class);
        Vertex mockVertex = EasyMock.createStrictMock(Vertex.class);
        Player mockPlayer = EasyMock.createStrictMock(Player.class);
        Game game = new Game(gameBoard, mockVertexGraph, null, null, null);
        int tileId = 0;

        //Expect
        List<Integer> expect = new ArrayList<>();
        expect.add(0);
        EasyMock.expect(gameBoard.getTileVertexIDs(tileId)).andReturn(expect);
        EasyMock.expect(mockVertexGraph.getVertex(0)).andReturn(mockVertex);
        EasyMock.expect(mockVertex.isOccupied()).andReturn(true);
        EasyMock.expect(mockVertex.getOwner()).andReturn(mockPlayer);

        //Replay
        EasyMock.replay(gameBoard, mockVertexGraph, mockVertex, mockPlayer);


        Player[] actual = null;                  
        //Call method
        try{actual = game.getPlayersFromTile(tileId); }
        catch(Exception e) {
            e.printStackTrace();
            fail();
        }

        assertEquals(actual.length, 1);
        assertEquals(actual[0], mockPlayer);
        EasyMock.verify(gameBoard, mockVertexGraph, mockVertex, mockPlayer);
    }

    @Test
    void test_GetPlayersFromTile_ManyFromOne() {

        GameBoard gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        VertexGraph mockVertexGraph = EasyMock.createStrictMock(VertexGraph.class);
        Vertex mockVertex1 = EasyMock.createStrictMock(Vertex.class);
        Vertex mockVertex2 = EasyMock.createStrictMock(Vertex.class);
        Vertex mockVertex3 = EasyMock.createStrictMock(Vertex.class);
        Player mockPlayer = EasyMock.createStrictMock(Player.class);
        Game game = new Game(gameBoard, mockVertexGraph, null, null, null);
        int tileId = 0;

        //Expect
        List<Integer> expected = new ArrayList<>();
        expected.add(0);
        expected.add(1);
        expected.add(2);

        EasyMock.expect(gameBoard.getTileVertexIDs(tileId)).andReturn(expected);
        EasyMock.expect(mockVertexGraph.getVertex(0)).andReturn(mockVertex1);
        EasyMock.expect(mockVertex1.isOccupied()).andReturn(true);
        EasyMock.expect(mockVertex1.getOwner()).andReturn(mockPlayer);

        EasyMock.expect(mockVertexGraph.getVertex(1)).andReturn(mockVertex2);
        EasyMock.expect(mockVertex2.isOccupied()).andReturn(true);
        EasyMock.expect(mockVertex2.getOwner()).andReturn(mockPlayer);

        EasyMock.expect(mockVertexGraph.getVertex(2)).andReturn(mockVertex3);
        EasyMock.expect(mockVertex3.isOccupied()).andReturn(true);
        EasyMock.expect(mockVertex3.getOwner()).andReturn(mockPlayer);

        //Replay
        EasyMock.replay(gameBoard, mockVertexGraph, mockVertex1,mockVertex2,mockVertex3, mockPlayer);


        Player[] actual = null;                  
        //Call method
        try{actual = game.getPlayersFromTile(tileId); }
        catch(Exception e) {
            e.printStackTrace();
            fail();
        }

        assertEquals(actual.length, 1);
        assertEquals(actual[0], mockPlayer);
        EasyMock.verify(gameBoard, mockVertexGraph, mockVertex1,mockVertex2,mockVertex3, mockPlayer);
    }

    @Test
    void test_GetPlayersFromTile_2From2() {

        GameBoard gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        VertexGraph mockVertexGraph = EasyMock.createStrictMock(VertexGraph.class);
        Vertex mockVertex1 = EasyMock.createStrictMock(Vertex.class);
        Vertex mockVertex2 = EasyMock.createStrictMock(Vertex.class);
        Player mockPlayer1 = EasyMock.createStrictMock(Player.class);
        Player mockPlayer2 = EasyMock.createStrictMock(Player.class);
        Game game = new Game(gameBoard, mockVertexGraph, null, null, null);
        int tileId = 0;

        //Expect
        List<Integer> expected = new ArrayList<>();
        expected.add(0);
        expected.add(1);
        EasyMock.expect(gameBoard.getTileVertexIDs(tileId)).andReturn(expected);
        EasyMock.expect(mockVertexGraph.getVertex(0)).andReturn(mockVertex1);
        EasyMock.expect(mockVertex1.isOccupied()).andReturn(true);
        EasyMock.expect(mockVertex1.getOwner()).andReturn(mockPlayer1);

        EasyMock.expect(mockVertexGraph.getVertex(1)).andReturn(mockVertex2);
        EasyMock.expect(mockVertex2.isOccupied()).andReturn(true);
        EasyMock.expect(mockVertex2.getOwner()).andReturn(mockPlayer2);

        //Replay
        EasyMock.replay(gameBoard, mockVertexGraph, mockVertex1,mockVertex2,mockPlayer1, mockPlayer2);


        Player[] actual = null;                  
        //Call method
        try{actual = game.getPlayersFromTile(tileId); }
        catch(Exception e) {
            e.printStackTrace();
            fail();
        }

        assertEquals(actual.length, 2);
        assertEquals(actual[0], mockPlayer1);
        assertEquals(actual[1], mockPlayer2);
        EasyMock.verify(gameBoard, mockVertexGraph, mockVertex1,mockVertex2,mockPlayer1, mockPlayer2);
    }

    @Test
    void test_GetPlayersFromTile_6Players() {
        GameBoard gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        VertexGraph mockVertexGraph = EasyMock.createStrictMock(VertexGraph.class);
        Vertex mockVertex1 = EasyMock.createStrictMock(Vertex.class);
        Vertex mockVertex2 = EasyMock.createStrictMock(Vertex.class);
        Vertex mockVertex3 = EasyMock.createStrictMock(Vertex.class);
        Vertex mockVertex4 = EasyMock.createStrictMock(Vertex.class);
        Vertex mockVertex5 = EasyMock.createStrictMock(Vertex.class);
        Vertex mockVertex6 = EasyMock.createStrictMock(Vertex.class);
        Player mockPlayer1 = EasyMock.createStrictMock(Player.class);
        Player mockPlayer2 = EasyMock.createStrictMock(Player.class);
        Player mockPlayer3 = EasyMock.createStrictMock(Player.class);
        Player mockPlayer4 = EasyMock.createStrictMock(Player.class);
        Player mockPlayer5 = EasyMock.createStrictMock(Player.class);
        Player mockPlayer6 = EasyMock.createStrictMock(Player.class);
        Game game = new Game(gameBoard, mockVertexGraph, null, null, null);
        int tileId = 0;

        //Expect
        List<Integer> expected = new ArrayList<>();
        expected.add(0);
        expected.add(1);
        expected.add(2);
        expected.add(3);
        expected.add(4);
        expected.add(5);
        EasyMock.expect(gameBoard.getTileVertexIDs(tileId)).andReturn(expected);
        //First 
        EasyMock.expect(mockVertexGraph.getVertex(0)).andReturn(mockVertex1);
        EasyMock.expect(mockVertex1.isOccupied()).andReturn(true);
        EasyMock.expect(mockVertex1.getOwner()).andReturn(mockPlayer1);
        //Second
        EasyMock.expect(mockVertexGraph.getVertex(1)).andReturn(mockVertex2);
        EasyMock.expect(mockVertex2.isOccupied()).andReturn(true);
        EasyMock.expect(mockVertex2.getOwner()).andReturn(mockPlayer2);
        //Third
        EasyMock.expect(mockVertexGraph.getVertex(2)).andReturn(mockVertex3);
        EasyMock.expect(mockVertex3.isOccupied()).andReturn(true);
        EasyMock.expect(mockVertex3.getOwner()).andReturn(mockPlayer3);
         //Forth 
         EasyMock.expect(mockVertexGraph.getVertex(3)).andReturn(mockVertex4);
         EasyMock.expect(mockVertex4.isOccupied()).andReturn(true);
         EasyMock.expect(mockVertex4.getOwner()).andReturn(mockPlayer4);
         // Fifth
         EasyMock.expect(mockVertexGraph.getVertex(4)).andReturn(mockVertex5);
         EasyMock.expect(mockVertex5.isOccupied()).andReturn(true);
         EasyMock.expect(mockVertex5.getOwner()).andReturn(mockPlayer5);
         // Sixth
         EasyMock.expect(mockVertexGraph.getVertex(5)).andReturn(mockVertex6);
         EasyMock.expect(mockVertex6.isOccupied()).andReturn(true);
         EasyMock.expect(mockVertex6.getOwner()).andReturn(mockPlayer6);

        //Replay
        EasyMock.replay(gameBoard, mockVertexGraph, mockVertex1,mockVertex2, mockVertex3, mockVertex4, mockVertex5, mockVertex6,
        mockPlayer1, mockPlayer2, mockPlayer3, mockPlayer4, mockPlayer5, mockPlayer6);


        Player[] actual = null;                  
        //Call method
        try{actual = game.getPlayersFromTile(tileId); }
        catch(Exception e) {
            e.printStackTrace();
            fail();
        }

        assertEquals(actual.length, 6);
        assertEquals(actual[0], mockPlayer1);
        assertEquals(actual[1], mockPlayer2);
        assertEquals(actual[2], mockPlayer3);
        assertEquals(actual[3], mockPlayer4);
        assertEquals(actual[4], mockPlayer5);
        assertEquals(actual[5], mockPlayer6);
        EasyMock.verify(gameBoard, mockVertexGraph, mockVertex1,mockVertex2, mockVertex3, mockVertex4, mockVertex5, mockVertex6,
        mockPlayer1, mockPlayer2, mockPlayer3, mockPlayer4, mockPlayer5, mockPlayer6);
    }

    @Test
    void test_GetPlayersFromTile_Error() {

        GameBoard gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        VertexGraph mockVertexGraph = EasyMock.createStrictMock(VertexGraph.class);
        Vertex mockVertex = EasyMock.createStrictMock(Vertex.class);
        Game game = new Game(gameBoard, mockVertexGraph, null, null, null);

        //Replay
        EasyMock.replay(gameBoard, mockVertexGraph, mockVertex);
                
        //Call method
        assertThrows(IllegalArgumentException.class,()-> game.getPlayersFromTile(-1));
        assertThrows(IllegalArgumentException.class,()-> game.getPlayersFromTile(19));
        
        EasyMock.verify(gameBoard,mockVertexGraph, mockVertex);
    }

    @Test
    public void testBuildDistrict_withVertexOwnedByPlayerBuildSawmill_expectSuccess() {
        Player player = new Player(1);
        GameBoard gameBoard = new GameBoard(GameType.Beginner, LAYOUT_FILE);
        VertexGraph vertexGraph = new VertexGraph();
        vertexGraph.initializeVertexToPortAdjacency(VERTEXPORT_FILE, GameType.Beginner);
        RoadGraph mockedRoadGraph = EasyMock.createStrictMock(RoadGraph.class);
        Game game = new Game(gameBoard, vertexGraph, mockedRoadGraph,null);

        vertexGraph.getVertex(1).build(player);

        //Replay
        EasyMock.replay(mockedRoadGraph);

        game.buildDistrictOnVertex(player, 1, DistrictType.SAWMILL);

        Assertions.assertEquals(DistrictType.SAWMILL, vertexGraph.getVertex(1).getBuilding().getDistrict());

        EasyMock.verify(mockedRoadGraph);
    }

    @Test
    public void testBuildDistrict_withVertexOwnedByOtherPlayerBuildSawmill_expectIllegalArgumentException() {
        Player player = new Player(1);
        Player enemy = new Player(2);
        GameBoard gameBoard = new GameBoard(GameType.Beginner, LAYOUT_FILE);
        VertexGraph vertexGraph = new VertexGraph();
        vertexGraph.initializeVertexToPortAdjacency(VERTEXPORT_FILE, GameType.Beginner);
        RoadGraph mockedRoadGraph = EasyMock.createStrictMock(RoadGraph.class);
        Game game = new Game(gameBoard, vertexGraph, mockedRoadGraph,null);

        vertexGraph.getVertex(1).build(enemy);

        //Replay
        EasyMock.replay(mockedRoadGraph);

        Assertions.assertThrows(IllegalArgumentException.class, () -> game.buildDistrictOnVertex(player, 1, DistrictType.SAWMILL));

        EasyMock.verify(mockedRoadGraph);
    }
}
