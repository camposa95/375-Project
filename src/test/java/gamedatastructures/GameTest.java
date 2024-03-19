package gamedatastructures;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import org.easymock.EasyMock;
import graphs.Road;
import graphs.RoadGraph;
import graphs.Vertex;
import graphs.VertexGraph;

public class GameTest {
    private static final String LAYOUT_FILE = "src/main/java/gamedatastructures/TileLayout.txt";
    private static final String VERTEX_FILE = "src/main/java/graphs/VertexToVertexLayout.txt";
    private static final String VERTEXROAD_FILE = "src/main/java/graphs/VertexToRoadLayout.txt";
    private static final String VERTEXPORT_FILE = "src/main/java/graphs/VertexToPortLayout.txt";
    private static final String ROAD_FILE = "src/main/java/graphs/RoadToRoadLayout.txt";
    private static final String ROADVERTEX_FILE = "src/main/java/graphs/RoadToVertexLayout.txt";



   // placeSettlement
    @Test
    public void testPlaceSettlement_Valid_Enough(){
        GameBoard gb = new GameBoard(GameType.Beginner,LAYOUT_FILE);
        RoadGraph mockedRoadGraph = EasyMock.createMock(RoadGraph.class);
        int vertexId = 0;

        VertexGraph mockVertexGraph =  EasyMock.createMock(VertexGraph.class);
        Vertex mockVertex = EasyMock.createNiceMock(Vertex.class);
        Player mockPlayer = EasyMock.createMock(Player.class);

        Game game = new Game(gb,mockVertexGraph,mockedRoadGraph, null);
        
        EasyMock.expect(mockVertexGraph.getVertex(vertexId)).andReturn(mockVertex);
        EasyMock.expect(mockVertex.isBuildable()).andReturn(true);
        EasyMock.expect(mockVertex.getOwner()).andReturn(mockPlayer);
        mockPlayer.placeSettlementSetup();
        mockedRoadGraph.giveLongestRoadCard();
        EasyMock.replay(mockPlayer,mockVertex,mockVertexGraph, mockedRoadGraph);
 
        try{
        game.placeSettlement(vertexId,mockPlayer);
        }
        catch(InvalidPlacementException e){
            assertFalse(true);
        }
        catch(NotEnoughResourcesException e){
            assertFalse(true);
        }
        assertEquals(mockVertex.getOwner(),mockPlayer);
        EasyMock.verify(mockPlayer,mockVertex,mockVertexGraph, mockedRoadGraph);
    }
    
    @Test
    public void testPlaceSettlement_Valid_NotEnough(){
        GameBoard gb = new GameBoard(GameType.Beginner,LAYOUT_FILE);
        RoadGraph rg = new RoadGraph();
        int vertexId = 0;

        VertexGraph mockVertexGraph =  EasyMock.createMock(VertexGraph.class);
        Vertex mockVertex = EasyMock.createMock(Vertex.class);
        Player mockPlayer = EasyMock.createMock(Player.class);

        Game game = new Game(gb,mockVertexGraph,rg,null);
        game.setup = false;

        EasyMock.expect(mockVertexGraph.getVertex(vertexId)).andReturn(mockVertex);
        EasyMock.expect(mockVertex.isBuildable()).andReturn(true);
        EasyMock.expect(mockVertex.isAdjacentToFriendlyRoad(mockPlayer)).andReturn(true);
        EasyMock.expect(mockPlayer.purchaseSettlement()).andReturn(false);
        EasyMock.expect(mockVertex.getOwner()).andReturn(null);
        
        EasyMock.replay(mockPlayer,mockVertex,mockVertexGraph);
 
        assertThrows(NotEnoughResourcesException.class,()->{game.placeSettlement(vertexId,mockPlayer);});
        assertNotEquals(mockVertex.getOwner(),mockPlayer);
        EasyMock.verify(mockPlayer,mockVertex,mockVertexGraph);
    }
    @Test
    public void testPlaceSettlement_Invalid_Enough(){
         GameBoard gb = new GameBoard(GameType.Beginner,LAYOUT_FILE);
        RoadGraph rg = new RoadGraph();
        int vertexId = 0;

        VertexGraph mockVertexGraph =  EasyMock.createMock(VertexGraph.class);
        Vertex mockVertex = EasyMock.createMock(Vertex.class);
        Player mockPlayer = EasyMock.createMock(Player.class);

        Game game = new Game(gb,mockVertexGraph,rg,null);
        EasyMock.expect(mockVertexGraph.getVertex(vertexId)).andReturn(mockVertex);
        EasyMock.expect(mockVertex.isBuildable()).andReturn(false);
        
        EasyMock.expect(mockVertex.getOwner()).andReturn(null);
        
        EasyMock.replay(mockPlayer,mockVertex,mockVertexGraph);
 
        assertThrows(InvalidPlacementException.class,()->{game.placeSettlement(vertexId,mockPlayer);});
        assertNotEquals(mockVertex.getOwner(),mockPlayer);
        EasyMock.verify(mockPlayer,mockVertex,mockVertexGraph);
    }
    @Test
    public void testPlaceSettlement_Invalid_NotEnough(){
         GameBoard gb = new GameBoard(GameType.Beginner,LAYOUT_FILE);
        RoadGraph rg = new RoadGraph();
        int vertexId = 0;

        VertexGraph mockVertexGraph =  EasyMock.createMock(VertexGraph.class);
        Vertex mockVertex = EasyMock.createMock(Vertex.class);
        Player mockPlayer = EasyMock.createMock(Player.class);

        Game game = new Game(gb,mockVertexGraph,rg,null);
        game.setup = false;

        EasyMock.expect(mockVertexGraph.getVertex(vertexId)).andReturn(mockVertex);
        EasyMock.expect(mockVertex.isBuildable()).andReturn(false);
        
        EasyMock.expect(mockVertex.getOwner()).andReturn(null);

        
        EasyMock.replay(mockPlayer,mockVertex,mockVertexGraph);
 
        assertThrows(InvalidPlacementException.class,()->{game.placeSettlement(vertexId,mockPlayer);});
        assertNotEquals(mockVertex.getOwner(),mockPlayer);
        EasyMock.verify(mockPlayer,mockVertex,mockVertexGraph);
    }
// placeRoad
    @Test
    public void testPlaceRoad_Valid_Adjacent_Enough(){
         GameBoard gb = new GameBoard(GameType.Beginner,LAYOUT_FILE);
        VertexGraph vg = new VertexGraph();
        int vertexId = 0;
        int roadId = 0;

        RoadGraph mockRoadGraph =  EasyMock.createMock(RoadGraph.class);
        Road mockRoad = EasyMock.createNiceMock(Road.class);
        Player mockPlayer = EasyMock.createMock(Player.class);

        Game game = new Game(gb,vg,mockRoadGraph,null);
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
        catch(InvalidPlacementException e){
            assertFalse(true);
        }
        catch(NotEnoughResourcesException e){
            assertFalse(true);
        }
        assertEquals(mockRoad.getOwner(),mockPlayer);
        EasyMock.verify(mockPlayer,mockRoad,mockRoadGraph);
    }
    @Test
    public void testPlaceRoad_Valid_Adjacent_NotEnough(){
         GameBoard gb = new GameBoard(GameType.Beginner,LAYOUT_FILE);
        VertexGraph vg = new VertexGraph();
        int vertexId = 0;
        int roadId = 0;

        RoadGraph mockRoadGraph =  EasyMock.createMock(RoadGraph.class);
        Road mockRoad = EasyMock.createMock(Road.class);
        Player mockPlayer = EasyMock.createMock(Player.class);

        Game game = new Game(gb,vg,mockRoadGraph,null);
        game.setup = false;

        EasyMock.expect(mockRoadGraph.getRoad(roadId)).andReturn(mockRoad);
        EasyMock.expect(mockRoad.isBuildableBy(mockPlayer)).andReturn(true);
        EasyMock.expect(mockPlayer.purchaseRoad()).andReturn(false);
        EasyMock.expect(mockRoad.getOwner()).andReturn(null);
        
        EasyMock.replay(mockPlayer,mockRoad,mockRoadGraph);
        
        assertThrows(NotEnoughResourcesException.class,()->{game.placeRoad(roadId,vertexId,mockPlayer);});
        assertNotEquals(mockRoad.getOwner(),mockPlayer);
        EasyMock.verify(mockPlayer,mockRoad,mockRoadGraph);
    }
    @Test
    public void testPlaceRoad_Valid_NotAdjacent_Enough(){
         GameBoard gb = new GameBoard(GameType.Beginner,LAYOUT_FILE);
        VertexGraph vg = new VertexGraph();
        int vertexId = 10;
        int roadId = 0;

        RoadGraph mockRoadGraph =  EasyMock.createMock(RoadGraph.class);
        Road mockRoad = EasyMock.createNiceMock(Road.class);
        Player mockPlayer = EasyMock.createNiceMock(Player.class);

        Game game = new Game(gb,vg,mockRoadGraph,null);
        EasyMock.expect(mockRoadGraph.getRoad(roadId)).andReturn(mockRoad);
        EasyMock.expect(mockRoad.isAdjacentTo(vg.getVertex(vertexId))).andReturn(false);
        EasyMock.expect(mockRoad.isBuildable()).andReturn(true);
        EasyMock.expect(mockRoad.getOwner()).andReturn(null);
        
        EasyMock.replay(mockPlayer,mockRoad,mockRoadGraph);
        
        assertThrows(InvalidPlacementException.class,()->{game.placeRoad(roadId,vertexId,mockPlayer);});
        assertNotEquals(mockRoad.getOwner(),mockPlayer);
        EasyMock.verify(mockPlayer,mockRoad,mockRoadGraph);
    }
    
    @Test
    public void testPlaceRoad_Invalid_NotAdjacent_Enough(){
         GameBoard gb = new GameBoard(GameType.Beginner,LAYOUT_FILE);
        VertexGraph vg = new VertexGraph();
        int vertexId = 10;
        int roadId = 0;

        RoadGraph mockRoadGraph =  EasyMock.createMock(RoadGraph.class);
        Road mockRoad = EasyMock.createMock(Road.class);
        Player mockPlayer = EasyMock.createMock(Player.class);

        Game game = new Game(gb,vg,mockRoadGraph,null);
        EasyMock.expect(mockRoadGraph.getRoad(roadId)).andReturn(mockRoad);
        EasyMock.expect(mockRoad.isBuildable()).andReturn(false);
        EasyMock.expect(mockRoad.getOwner()).andReturn(null);
        
        EasyMock.replay(mockPlayer,mockRoad,mockRoadGraph);
        
        assertThrows(InvalidPlacementException.class,()->{game.placeRoad(roadId,vertexId,mockPlayer);});
        assertNotEquals(mockRoad.getOwner(),mockPlayer);
        EasyMock.verify(mockPlayer,mockRoad,mockRoadGraph);
    }
    @Test
    public void testPlaceRoad_Invalid_Adjacent_Enough(){
        GameBoard gb = new GameBoard(GameType.Beginner,LAYOUT_FILE);
        VertexGraph vg = new VertexGraph();
        int vertexId = 0;
        int roadId = 0;

        RoadGraph mockRoadGraph =  EasyMock.createMock(RoadGraph.class);
        Road mockRoad = EasyMock.createMock(Road.class);
        Player mockPlayer = EasyMock.createMock(Player.class);

        Game game = new Game(gb,vg,mockRoadGraph,null);
        EasyMock.expect(mockRoadGraph.getRoad(roadId)).andReturn(mockRoad);
        EasyMock.expect(mockRoad.isBuildable()).andReturn(false);
        EasyMock.expect(mockRoad.getOwner()).andReturn(null);
        
        EasyMock.replay(mockPlayer,mockRoad,mockRoadGraph);
        
        assertThrows(InvalidPlacementException.class,()->{game.placeRoad(roadId,vertexId,mockPlayer);});
        assertNotEquals(mockRoad.getOwner(),mockPlayer);
        EasyMock.verify(mockPlayer,mockRoad,mockRoadGraph);
    }
    @Test
    public void testPlaceRoad_Invalid_Adjacent_NotEnough(){
        GameBoard gb = new GameBoard(GameType.Beginner,LAYOUT_FILE);
        VertexGraph vg = new VertexGraph();
        int vertexId = 0;
        int roadId = 0;

        RoadGraph mockRoadGraph =  EasyMock.createMock(RoadGraph.class);
        Road mockRoad = EasyMock.createMock(Road.class);
        Player mockPlayer = EasyMock.createMock(Player.class);

        Game game = new Game(gb,vg,mockRoadGraph,null);
        game.setup = false;

        EasyMock.expect(mockRoadGraph.getRoad(roadId)).andReturn(mockRoad);
        EasyMock.expect(mockRoad.isBuildableBy(mockPlayer)).andReturn(false);
        EasyMock.expect(mockRoad.getOwner()).andReturn(null);
        
        EasyMock.replay(mockPlayer,mockRoad,mockRoadGraph);
        
        
        assertThrows(InvalidPlacementException.class,()->{game.placeRoad(roadId,vertexId,mockPlayer);});
        assertNotEquals(mockRoad.getOwner(),mockPlayer);
        EasyMock.verify(mockPlayer,mockRoad,mockRoadGraph);
    }
    @Test
    public void testPlaceRoad_Invalid_NotAdjacent_NotEnough(){
        GameBoard gb = new GameBoard(GameType.Beginner,LAYOUT_FILE);
        VertexGraph vg = new VertexGraph();
        int vertexId = 10;
        int roadId = 0;

        RoadGraph mockRoadGraph =  EasyMock.createMock(RoadGraph.class);
        Road mockRoad = EasyMock.createMock(Road.class);
        Player mockPlayer = EasyMock.createMock(Player.class);

        Game game = new Game(gb,vg,mockRoadGraph,null);
        game.setup = false;

        EasyMock.expect(mockRoadGraph.getRoad(roadId)).andReturn(mockRoad);
        EasyMock.expect(mockRoad.isBuildableBy(mockPlayer)).andReturn(false);
        EasyMock.expect(mockRoad.getOwner()).andReturn(null);

        EasyMock.replay(mockPlayer,mockRoad,mockRoadGraph);
        
        assertThrows(InvalidPlacementException.class,()->{game.placeRoad(roadId,vertexId,mockPlayer);});
        assertNotEquals(mockRoad.getOwner(),mockPlayer);
        EasyMock.verify(mockPlayer,mockRoad,mockRoadGraph);
    }
    @Test
    public void testPlaceRoad_NonSetup_ValidPlacement_NotEnoughResources() {
        GameBoard gb = new GameBoard(GameType.Beginner,LAYOUT_FILE);
        VertexGraph vg = new VertexGraph();
        int vertexId = 0;
        int roadId = 0;

        RoadGraph mockRoadGraph =  EasyMock.createMock(RoadGraph.class);
        Road mockRoad = EasyMock.createMock(Road.class);
        Player player = new Player(1);

        Game game = new Game(gb,vg,mockRoadGraph,null);
        game.setup = false;

        EasyMock.expect(mockRoadGraph.getRoad(roadId)).andReturn(mockRoad);
        EasyMock.expect(mockRoad.isBuildableBy(player)).andReturn(true);
        EasyMock.expect(mockRoad.getOwner()).andReturn(null);
        
        EasyMock.replay(mockRoad,mockRoadGraph);
        
        assertThrows(NotEnoughResourcesException.class,()->{game.placeRoad(roadId,vertexId,player);});
        assertNotEquals(mockRoad.getOwner(),player);
        EasyMock.verify(mockRoad,mockRoadGraph);
    }   
   
    //resourcesFromVertex
    @Test
    public void testresourcesFromVertex_NoSettlements_ExpectNoResources(){
        GameBoard gb = new GameBoard(GameType.Beginner,LAYOUT_FILE);
        VertexGraph vg = new VertexGraph();
        RoadGraph rg = new RoadGraph();
        int vertexId = 0;

        Player mockPlayer = EasyMock.createMock(Player.class);

        Game game = new Game(gb,vg,rg,null);
        
        Resource[] expected = new Resource[0];
        Resource[] actual = game.resourcesFromVertex(mockPlayer,vertexId);
        EasyMock.replay(mockPlayer);
        
        assertTrue(Arrays.equals(expected,actual));
        EasyMock.verify(mockPlayer);
    }
    @Test
    public void testresourcesFromVertex_HasSettlementOnCorner_Expect1Resource(){
         GameBoard gb = new GameBoard(GameType.Beginner,LAYOUT_FILE);
        VertexGraph vg = new VertexGraph();
        RoadGraph rg = new RoadGraph();
        int vertexId = 0;

        Player mockPlayer = EasyMock.createMock(Player.class);
       

        Game game = new Game(gb,vg,rg,null);
        vg.getVertex(vertexId).setOwner(mockPlayer);

        Resource[] expected = {Resource.ORE};
        Resource[] actual = game.resourcesFromVertex(mockPlayer,vertexId);
        EasyMock.replay(mockPlayer);

       
        assertTrue(Arrays.equals(expected,actual));
        assertNotNull(game.resourcesFromVertex(mockPlayer,vertexId));
        EasyMock.verify(mockPlayer);
    }
    @Test
    public void testresourcesFromVertex_HasSettlementOnEdge_Expect2Resources(){
         GameBoard gb = new GameBoard(GameType.Beginner,LAYOUT_FILE);
        VertexGraph vg = new VertexGraph();
        RoadGraph rg = new RoadGraph();
        int vertexId = 2;

        Player mockPlayer = EasyMock.createMock(Player.class);
       

        Game game = new Game(gb,vg,rg,null);
        vg.getVertex(vertexId).setOwner(mockPlayer);

        Resource[] expected = {Resource.ORE, Resource.WOOL};
        Resource[] actual = game.resourcesFromVertex(mockPlayer,vertexId);
        EasyMock.replay(mockPlayer);

       
        assertTrue(Arrays.equals(expected,actual));
        
        EasyMock.verify(mockPlayer);
    }
    @Test
    public void testresourcesFromVertex_HasSettlementOnBoard_Expect3Resources(){
         GameBoard gb = new GameBoard(GameType.Beginner,LAYOUT_FILE);
        VertexGraph vg = new VertexGraph();
        RoadGraph rg = new RoadGraph();
        int vertexId = 10;

        Player mockPlayer = EasyMock.createMock(Player.class);
       

        Game game = new Game(gb,vg,rg,null);
        vg.getVertex(vertexId).setOwner(mockPlayer);

        Resource[] expected = {Resource.ORE, Resource.WOOL,Resource.BRICK};
        Resource[] actual = game.resourcesFromVertex(mockPlayer,vertexId);
        EasyMock.replay(mockPlayer);

        
        assertTrue(Arrays.equals(expected,actual));
        
        EasyMock.verify(mockPlayer);
    }
    @Test
    public void testPlaceSettlement_Unmocked(){
        GameBoard gb = new GameBoard(GameType.Beginner,LAYOUT_FILE);
        RoadGraph rg = new RoadGraph();
        int vertexId = 0;

        VertexGraph vertexGraph = new VertexGraph();
        vertexGraph.initializeVertexToVertexAdjacency(VERTEX_FILE);
        vertexGraph.initializeVertexToRoadAdjacency(rg, VERTEXROAD_FILE);
        vertexGraph.initializeVertexToPortAdjacency(VERTEXPORT_FILE, GameType.Beginner);
        rg.initializeRoadToVertexAdjacency(vertexGraph, ROADVERTEX_FILE);
      
        Player player = new Player(1);

        Game game = new Game(gb,vertexGraph,rg,null);

        try{
        game.placeSettlement(vertexId,player);
        }
        catch(InvalidPlacementException e){
            assertFalse(true);
        }
        catch(NotEnoughResourcesException e){
            assertFalse(true);
        }
        assertEquals(vertexGraph.getVertex(vertexId).getOwner(),player);
       
        //regular
        game.setup = false;
        player.hand.addResources(new Resource[] {Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN});
        vertexId = 3;
        // make sure it is adjacent to a friendly road
        rg.getRoad(3).setOwner(player);
        try{
            game.placeSettlement(vertexId,player);
            }
            catch(InvalidPlacementException e){
                assertFalse(true);
            }
            catch(NotEnoughResourcesException e){
                assertFalse(true);
            }
            assertEquals(vertexGraph.getVertex(vertexId).getOwner(),player);
    }
    @Test
    public void testPlaceRoad_Unmocked(){
        GameBoard gb = new GameBoard(GameType.Beginner,LAYOUT_FILE);
        RoadGraph roadgraph = new RoadGraph();
        int vertexId = 0;
        int roadID = 0;

        VertexGraph vertexGraph = new VertexGraph();
        vertexGraph.initializeVertexToVertexAdjacency(VERTEX_FILE);
        vertexGraph.initializeVertexToRoadAdjacency(roadgraph, VERTEXROAD_FILE);
        vertexGraph.initializeVertexToPortAdjacency(VERTEXPORT_FILE, null);
        roadgraph.initializeRoadToRoadAdjacency(ROAD_FILE);
        roadgraph.initializeRoadToVertexAdjacency(vertexGraph, ROADVERTEX_FILE);
      
        Player player = new Player(1);

        Game game = new Game(gb,vertexGraph,roadgraph,null);

        //setup
        try{
        game.placeRoad(roadID,vertexId,player);
        }
        catch(InvalidPlacementException e){
            assertFalse(true);
        }
        catch(NotEnoughResourcesException e){
            assertFalse(true);
        }
        assertEquals(roadgraph.getRoad(roadID).getOwner(),player);

        //regular
        game.setup = false;
        player.hand.addResources(new Resource[] {Resource.BRICK, Resource.LUMBER});
        vertexGraph.getVertex(vertexId).setOwner(player);
        roadID = 6;
        try{
            game.placeRoad(roadID,vertexId,player);
            }
            catch(InvalidPlacementException e){
                assertFalse(true);
            }
            catch(NotEnoughResourcesException e){
                assertFalse(true);
            }
            assertEquals(roadgraph.getRoad(roadID).getOwner(),player);
    }
    @Test
    public void testDistributeResources_Unmocked(){
        GameBoard gb = new GameBoard(GameType.Beginner,LAYOUT_FILE);
        RoadGraph roadgraph = new RoadGraph();
        int vertexId = 0;
        int die = 10;
        VertexGraph vertexGraph = new VertexGraph();
        vertexGraph.initializeVertexToVertexAdjacency(VERTEX_FILE);
        vertexGraph.initializeVertexToRoadAdjacency(roadgraph, VERTEXROAD_FILE);
        roadgraph.initializeRoadToRoadAdjacency(ROAD_FILE);
        roadgraph.initializeRoadToVertexAdjacency(vertexGraph, ROADVERTEX_FILE);
      
        Player player = new Player(1);

        Game game = new Game(gb,vertexGraph,roadgraph,null);
        vertexGraph.getVertex(vertexId).setOwner(player);

        Resource[] expected = {Resource.ORE};
        game.distributeResources(player,vertexId);
        assertTrue(player.hand.removeResources(expected));
        assertNotNull(game.resourcesFromVertex(player,vertexId));
        
        game.setup = false;
        game.distributeResources(player,die);
        assertTrue(player.hand.removeResources(expected));
        assertNotNull(game.resourcesFromVertex(player,vertexId));
    }
 
    //resourcesFromDie
    @Test
    public void testResourcesFromDie_NoSettlements() {
        Player player = new Player(1);
        GameBoard gameBoard = new GameBoard(GameType.Beginner, LAYOUT_FILE);
        VertexGraph vertexGraph = new VertexGraph();
        RoadGraph mockedRoadGraph = EasyMock.createStrictMock(RoadGraph.class);
        Game game = new Game(gameBoard, vertexGraph, mockedRoadGraph,null);

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
        RoadGraph mockedRoadGraph = EasyMock.createStrictMock(RoadGraph.class);
        Game game = new Game(gameBoard, vertexGraph, mockedRoadGraph,null);
        int die = 10;

        vertexGraph.getVertex(0).setOwner(player);
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
        GameBoard gameBoard = new GameBoard(GameType.Beginner, LAYOUT_FILE);
        VertexGraph vertexGraph = new VertexGraph();
        RoadGraph mockedRoadGraph = EasyMock.createStrictMock(RoadGraph.class);
        Game game = new Game(gameBoard, vertexGraph, mockedRoadGraph,null);
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
        GameBoard gameBoard = new GameBoard(GameType.Beginner, LAYOUT_FILE);
        VertexGraph vertexGraph = new VertexGraph();
        RoadGraph mockedRoadGraph = EasyMock.createStrictMock(RoadGraph.class);
        Game game = new Game(gameBoard, vertexGraph, mockedRoadGraph,null);
        int die = 10;

        vertexGraph.getVertex(0).setOwner(player);
        vertexGraph.getVertex(1).setOwner(player);
        vertexGraph.getVertex(2).setOwner(player);
        vertexGraph.getVertex(8).setOwner(player);
        vertexGraph.getVertex(9).setOwner(player);
        vertexGraph.getVertex(10).setOwner(player);
      
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
        GameBoard gameBoard = new GameBoard(GameType.Beginner, LAYOUT_FILE);
        VertexGraph vertexGraph = new VertexGraph();
        RoadGraph mockedRoadGraph = EasyMock.createStrictMock(RoadGraph.class);
        Game game = new Game(gameBoard, vertexGraph, mockedRoadGraph,null);
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
        GameBoard gameBoard = new GameBoard(GameType.Beginner, LAYOUT_FILE);
        VertexGraph vertexGraph = new VertexGraph();
        RoadGraph mockedRoadGraph = EasyMock.createStrictMock(RoadGraph.class);
        Game game = new Game(gameBoard, vertexGraph, mockedRoadGraph,null);
        int die = 10;

        vertexGraph.getVertex(1).setOwner(player);
        vertexGraph.getVertex(9).setOwner(player);
        vertexGraph.getVertex(14).setOwner(player);
        vertexGraph.getVertex(24).setOwner(player);
    
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
    public void testResourcesFromDie_Many_Many_NoDie() {
        Player player = new Player(1);
        GameBoard gameBoard = new GameBoard(GameType.Beginner, LAYOUT_FILE);
        VertexGraph vertexGraph = new VertexGraph();
        RoadGraph mockedRoadGraph = EasyMock.createStrictMock(RoadGraph.class);
        Game game = new Game(gameBoard, vertexGraph, mockedRoadGraph,null);
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
        Player player = new Player(1);
        GameBoard gameBoard = new GameBoard(GameType.Beginner, LAYOUT_FILE);
        VertexGraph vertexGraph = new VertexGraph();
        RoadGraph mockedRoadGraph = EasyMock.createStrictMock(RoadGraph.class);
        Game game = new Game(gameBoard, vertexGraph, mockedRoadGraph,null);
        int die = 7;
        
        EasyMock.replay(mockedRoadGraph);

        gameBoard.getTiles()[0].setDie(die);

        vertexGraph.getVertex(1).setOwner(player);
        vertexGraph.getVertex(9).setOwner(player);
        
    
        
        //Check hand before
        int before = player.hand.getResourceCardCount();
        //Call method
        Resource[] actual = game.resourcesFromDie(player, die);
        player.hand.addResources(actual);
        //Check After
        int after = player.hand.getResourceCardCount();


        assertEquals(before, after);
        assertEquals(0,actual.length);  
        EasyMock.verify(mockedRoadGraph);
    }
    @Test
    public void testEndSetup() {
        GameBoard gameBoard = new GameBoard(GameType.Beginner, LAYOUT_FILE);
        VertexGraph vertexGraph = new VertexGraph();
        RoadGraph mockedRoadGraph = EasyMock.createStrictMock(RoadGraph.class);
        Game game = new Game(gameBoard, vertexGraph, mockedRoadGraph,null);

        EasyMock.replay(mockedRoadGraph);

        assertFalse(game.endSetup());

        EasyMock.verify(mockedRoadGraph);
    }
    @Test 
    public void testDropCards_empty() {
        //Initilize Objects
        GameBoard gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        VertexGraph vertexGraph = EasyMock.createStrictMock(VertexGraph.class);
        RoadGraph mockedRoadGraph = EasyMock.createStrictMock(RoadGraph.class);
        Game game = new Game(gameBoard, vertexGraph, mockedRoadGraph,null);

        //Create Hashmap
        HashMap<Player,Resource[]> testHash = new HashMap<Player,Resource[]>();
        
        //Replay
        EasyMock.replay(gameBoard, vertexGraph, mockedRoadGraph);
        
        //Call method
        try{ 
            game.dropCards(testHash);
        } catch( Exception e) {
            //Make sure nothing is thrown
            assertFalse(true);
        }
        
        //Verify
        EasyMock.verify(gameBoard, vertexGraph, mockedRoadGraph);
    }
    @Test
    public void testDropCards_1player_nothing() {
        //Initilize Objects
        GameBoard gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        VertexGraph vertexGraph = EasyMock.createStrictMock(VertexGraph.class);
        RoadGraph mockedRoadGraph = EasyMock.createStrictMock(RoadGraph.class);
        Player mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        Game game = new Game(gameBoard, vertexGraph, mockedRoadGraph,null);
        
       
        //Replay
        EasyMock.replay(gameBoard, vertexGraph, mockedRoadGraph,mockedPlayer1);
        
        //Create Hashmap

        Resource[] resources = new Resource[0];
         
        HashMap<Player,Resource[]> testHash = new HashMap<Player,Resource[]>();
        testHash.put(mockedPlayer1, resources);

        //Call method
        try{ 
            game.dropCards(testHash);
        } catch( Exception e) {
            //Make sure nothing is thrown
            e.printStackTrace();
            assertTrue(false);
        }
        
        //Verify
        EasyMock.verify(gameBoard, vertexGraph, mockedRoadGraph,mockedPlayer1);
    }
    @Test 
    public void testDropCards_1player_1card() {
        //Initilize Objects
        GameBoard gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        VertexGraph vertexGraph = EasyMock.createStrictMock(VertexGraph.class);
        RoadGraph mockedRoadGraph = EasyMock.createStrictMock(RoadGraph.class);
        Player mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        Hand mockedHand1 = EasyMock.createStrictMock(Hand.class);
        mockedPlayer1.hand = mockedHand1;
        Game game = new Game(gameBoard, vertexGraph, mockedRoadGraph,null);
       
        //Expect 
        Resource[] resources = {Resource.WOOL};
        EasyMock.expect(mockedPlayer1.hand.removeResources(resources)).andReturn(true);
        //Replay
        EasyMock.replay(gameBoard, vertexGraph, mockedRoadGraph,mockedPlayer1,mockedHand1);
        
        //Create Hashmap
        HashMap<Player,Resource[]> testHash = new HashMap<Player,Resource[]>();
        testHash.put(mockedPlayer1, resources);

        //Call method
        try{ 
            game.dropCards(testHash);
        } catch( Exception e) {
            //Make sure nothing is thrown
            e.printStackTrace();
            assertTrue(false);
        }
        
        //Verify
        EasyMock.verify(gameBoard, vertexGraph, mockedRoadGraph,mockedPlayer1,mockedHand1);
    }
    @Test 
    public void testDropCards_ManyPlayer_MixCard() {
        //Initilize Objects
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
        Game game = new Game(gameBoard, vertexGraph, mockedRoadGraph,null);
       
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
        HashMap<Player,Resource[]> testHash = new HashMap<Player,Resource[]>();
        testHash.put(mockedPlayer1, resources1);
        testHash.put(mockedPlayer2, resources2);
        testHash.put(mockedPlayer3, resources3);

        //Call method
        try{ 
            game.dropCards(testHash);
        } catch( Exception e) {
            //Make sure nothing is thrown
            e.printStackTrace();
            assertTrue(false);
        }
        
        //Verify
        EasyMock.verify(gameBoard, vertexGraph, mockedRoadGraph,mockedPlayer1,mockedHand1,mockedPlayer2,mockedHand2,mockedPlayer3,mockedHand3);
    }
    @Test 
    public void testDropCards_ManyPlayer_SameResources() {
        //Initilize Objects
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

        Game game = new Game(gameBoard, vertexGraph, mockedRoadGraph,null);
       
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
        HashMap<Player,Resource[]> testHash = new HashMap<Player,Resource[]>();
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
            assertTrue(false);
        }
        
        //Verify
        EasyMock.verify(gameBoard, vertexGraph, mockedRoadGraph,mockedPlayer1,mockedHand1,mockedPlayer2,mockedHand2,mockedPlayer3,mockedHand3
        ,mockedPlayer4,mockedHand4,mockedPlayer5,mockedHand5,mockedPlayer6,mockedHand6);
    }
    @Test 
    void testDropCards_1player_Invalid() {
        //Initilize Objects
        GameBoard gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        VertexGraph vertexGraph = EasyMock.createStrictMock(VertexGraph.class);
        RoadGraph mockedRoadGraph = EasyMock.createStrictMock(RoadGraph.class);
        Player mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        Hand mockedHand1 = EasyMock.createStrictMock(Hand.class);
        mockedPlayer1.hand = mockedHand1;
        Game game = new Game(gameBoard, vertexGraph, mockedRoadGraph,null);
       
        //Expect 
        Resource[] resources = {Resource.WOOL};
        EasyMock.expect(mockedPlayer1.hand.removeResources(resources)).andReturn(false);
        //Replay
        EasyMock.replay(gameBoard, vertexGraph, mockedRoadGraph,mockedPlayer1,mockedHand1);
        
        //Create Hashmap
        HashMap<Player,Resource[]> testHash = new HashMap<Player,Resource[]>();
        testHash.put(mockedPlayer1, resources);

        //Call method
       assertThrows(IllegalArgumentException.class, ()->{game.dropCards(testHash);},"DropCards was called on player with not enough cards - desync issue"); 
           
        //Verify
        EasyMock.verify(gameBoard, vertexGraph, mockedRoadGraph,mockedPlayer1,mockedHand1);
    }
    //move robber
    @Test 
    void testMoveRobber_without() {
        //Initilize Objects
        GameBoard gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        Tile tile1 = EasyMock.createStrictMock(Tile.class);
        Tile tile2 = EasyMock.createStrictMock(Tile.class);
        Game game = new Game(gameBoard, null, null ,null);
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
            assertTrue(false);
        }

        //Verify
        EasyMock.verify(gameBoard, tile1, tile2);
    }
    @Test 
    void testMoveRobber_with() {
        //Initilize Objects
        GameBoard gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        Tile tile1 = EasyMock.createStrictMock(Tile.class);
        Game game = new Game(gameBoard, null, null ,null);
        int tileNum = 0;

        //Expect 
        EasyMock.expect(gameBoard.getTiles()).andReturn(new Tile[]{tile1});
        EasyMock.expect(gameBoard.getRobberTile()).andReturn(tile1);

        //Replay
        EasyMock.replay(gameBoard, tile1);
        
        //Call method
        assertThrows(InvalidPlacementException.class,()->{game.moveRobber(tileNum); });

        //Verify
        EasyMock.verify(gameBoard, tile1);
    }
    @Test 
    void testMoveRobber_invalid() {
        //Initilize Objects
        GameBoard gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        Game game = new Game(gameBoard, null, null ,null);

        //Replay
        EasyMock.replay(gameBoard);
        
        //Call method
        assertThrows(RuntimeException.class,()->{game.moveRobber(-1); });
        assertThrows(RuntimeException.class,()->{game.moveRobber(19); });

        //Verify
        EasyMock.verify(gameBoard);
    }
    //Steal from player
    @Test 
    void test_StealFromPlayer_None() {
        //Initilize Objects
        GameBoard gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        Player mockedRobber = EasyMock.createStrictMock(Player.class);
        Player mockedVictim = EasyMock.createStrictMock(Player.class);
        Hand mockedHand = EasyMock.createStrictMock(Hand.class);
        mockedVictim.hand = mockedHand;
        Game game = new Game(gameBoard, null, null ,null);

        //Expect 
        EasyMock.expect(mockedVictim.hand.getResourceCardCount()).andReturn(0);
        //Replay
        EasyMock.replay(gameBoard, mockedRobber, mockedVictim);
        
        //Call method
        assertThrows(NotEnoughResourcesException.class, ()->{game.stealFromPlayer(mockedRobber,mockedVictim);});
        
        //Verify
        EasyMock.verify(gameBoard, mockedRobber, mockedVictim);
    }
    @Test 
    void test_StealFromPlayer_One() {
        //Initilize Objects
        GameBoard gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        Player robber = new Player(1);
        Player robbed = new Player(2);
        Game game = new Game(gameBoard, null, null ,null);

        EasyMock.replay(gameBoard);
        robbed.hand.addResource(Resource.WOOL, 1);
        
        int robberBefore = robber.hand.getResourceCardCount();
        int robbedBefore = robbed.hand.getResourceCardCount();
        
        //Call method
        try{ game.stealFromPlayer(robber,robbed); }
        catch(Exception e) {
            e.printStackTrace();
            assertTrue(false);
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
        //Initilize Objects
        GameBoard gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        Player robber = new Player(1);
        Player robbed = new Player(2);
        Game game = new Game(gameBoard, null, null ,null);

        EasyMock.replay(gameBoard);
        robbed.hand.addResources(new Resource[]{Resource.WOOL,Resource.BRICK,Resource.LUMBER});
        
        int robberBefore = robber.hand.getResourceCardCount();
        int robbedBefore = robbed.hand.getResourceCardCount();
        
        //Call method
        try{ game.stealFromPlayer(robber,robbed); }
        catch(Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
        
        int robberAfter = robber.hand.getResourceCardCount();
        int robbedAfter = robbed.hand.getResourceCardCount();

        assertEquals(robberBefore, 0);
        assertEquals(robbedBefore,3);
        assertEquals(robberAfter, 1);
        assertEquals(robbedAfter,2);
        EasyMock.verify(gameBoard);
    }
    //Get owners from tile
    @Test 
    void test_GetPlayersFromTile_None() {
        //Initilize Objects
        GameBoard gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        VertexGraph mockVertexGraph = EasyMock.createStrictMock(VertexGraph.class);
        Vertex mockVertex = EasyMock.createStrictMock(Vertex.class);
        Game game = new Game(gameBoard, mockVertexGraph, null ,null);
        int tileId = 0;

        //Expect
        EasyMock.expect(gameBoard.getTileVertexIDs(tileId)).andReturn(new int[]{0});
        EasyMock.expect(mockVertexGraph.getVertex(0)).andReturn(mockVertex);
        EasyMock.expect(mockVertex.isOccupied()).andReturn(false);
        //Replay
        EasyMock.replay(gameBoard, mockVertexGraph, mockVertex);

        Player[] actual = null;                  
        //Call method
        try{actual = game.getPlayersFromTile(tileId); }
        catch(Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertEquals(actual.length, 0);
        EasyMock.verify(gameBoard,mockVertexGraph, mockVertex);
    }
    @Test 
    void test_GetPlayersFromTile_One() {
        //Initilize Objects
        GameBoard gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        VertexGraph mockVertexGraph = EasyMock.createStrictMock(VertexGraph.class);
        Vertex mockVertex = EasyMock.createStrictMock(Vertex.class);
        Player mockPlayer = EasyMock.createStrictMock(Player.class);
        Game game = new Game(gameBoard, mockVertexGraph, null ,null);
        int tileId = 0;

        //Expect
        EasyMock.expect(gameBoard.getTileVertexIDs(tileId)).andReturn(new int[]{0});
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
            assertTrue(false);
        }

        assertEquals(actual.length, 1);
        assertEquals(actual[0], mockPlayer);
        EasyMock.verify(gameBoard, mockVertexGraph, mockVertex, mockPlayer);
    }
    @Test 
    void test_GetPlayersFromTile_ManyFromOne() {
        //Initilize Objects
        GameBoard gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        VertexGraph mockVertexGraph = EasyMock.createStrictMock(VertexGraph.class);
        Vertex mockVertex1 = EasyMock.createStrictMock(Vertex.class);
        Vertex mockVertex2 = EasyMock.createStrictMock(Vertex.class);
        Vertex mockVertex3 = EasyMock.createStrictMock(Vertex.class);
        Player mockPlayer = EasyMock.createStrictMock(Player.class);
        Game game = new Game(gameBoard, mockVertexGraph, null ,null);
        int tileId = 0;

        //Expect
        EasyMock.expect(gameBoard.getTileVertexIDs(tileId)).andReturn(new int[]{0,1,2});
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
            assertTrue(false);
        }

        assertEquals(actual.length, 1);
        assertEquals(actual[0], mockPlayer);
        EasyMock.verify(gameBoard, mockVertexGraph, mockVertex1,mockVertex2,mockVertex3, mockPlayer);
    }
    @Test 
    void test_GetPlayersFromTile_2From2() {
        //Initilize Objects
        GameBoard gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        VertexGraph mockVertexGraph = EasyMock.createStrictMock(VertexGraph.class);
        Vertex mockVertex1 = EasyMock.createStrictMock(Vertex.class);
        Vertex mockVertex2 = EasyMock.createStrictMock(Vertex.class);
        Player mockPlayer1 = EasyMock.createStrictMock(Player.class);
        Player mockPlayer2 = EasyMock.createStrictMock(Player.class);
        Game game = new Game(gameBoard, mockVertexGraph, null ,null);
        int tileId = 0;

        //Expect
        EasyMock.expect(gameBoard.getTileVertexIDs(tileId)).andReturn(new int[]{0,1});
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
            assertTrue(false);
        }

        assertEquals(actual.length, 2);
        assertEquals(actual[0], mockPlayer1);
        assertEquals(actual[1], mockPlayer2);
        EasyMock.verify(gameBoard, mockVertexGraph, mockVertex1,mockVertex2,mockPlayer1, mockPlayer2);
    }
    @Test 
    void test_GetPlayersFromTile_6Players() {
        //Initilize Objects
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
        Game game = new Game(gameBoard, mockVertexGraph, null ,null);
        int tileId = 0;

        //Expect
        EasyMock.expect(gameBoard.getTileVertexIDs(tileId)).andReturn(new int[]{0,1,2,3,4,5});
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
         //Fith
         EasyMock.expect(mockVertexGraph.getVertex(4)).andReturn(mockVertex5);
         EasyMock.expect(mockVertex5.isOccupied()).andReturn(true);
         EasyMock.expect(mockVertex5.getOwner()).andReturn(mockPlayer5);
         //Sixth
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
            assertTrue(false);
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
        //Initilize Objects
        GameBoard gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        VertexGraph mockVertexGraph = EasyMock.createStrictMock(VertexGraph.class);
        Vertex mockVertex = EasyMock.createStrictMock(Vertex.class);
        Game game = new Game(gameBoard, mockVertexGraph, null ,null);

        //Replay
        EasyMock.replay(gameBoard, mockVertexGraph, mockVertex);
                
        //Call method
        assertThrows(IllegalArgumentException.class,()->{game.getPlayersFromTile(-1);});
        assertThrows(IllegalArgumentException.class,()->{game.getPlayersFromTile(19);});
        
        EasyMock.verify(gameBoard,mockVertexGraph, mockVertex);
    }
}
