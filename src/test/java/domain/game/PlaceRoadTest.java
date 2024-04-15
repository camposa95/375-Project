package domain.game;

import data.GameLoader;
import domain.bank.Bank;
import domain.bank.Resource;
import domain.gameboard.GameBoard;
import domain.graphs.Road;
import domain.graphs.RoadGraph;
import domain.graphs.VertexGraph;
import domain.player.Player;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PlaceRoadTest {

    GameBoard gb;
    VertexGraph vg;
    RoadGraph mockRoadGraph;
    Road mockRoad;
    Player mockPlayer;
    Game game;

    @BeforeEach
    public void setup() {
        gb = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gb);
        vg = new VertexGraph(GameType.Beginner);
        mockRoadGraph =  EasyMock.createMock(RoadGraph.class);
        mockRoad = EasyMock.createNiceMock(Road.class);
        mockPlayer = EasyMock.createMock(Player.class);
        game = new Game(gb, vg, mockRoadGraph, null, null);
    }

    @Test
    public void testPlaceRoad_Valid_Adjacent_Enough() {
        int vertexId = 0;
        int roadId = 0;

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
        int vertexId = 0;
        int roadId = 0;

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
        int vertexId = 10;
        int roadId = 0;

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
        int vertexId = 10;
        int roadId = 0;

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
        int vertexId = 0;
        int roadId = 0;

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
        int vertexId = 0;
        int roadId = 0;

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
        int vertexId = 10;
        int roadId = 0;

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
        int vertexId = 0;
        int roadId = 0;

        Player player = new Player(1); // shadow the mocked one here

        game.endSetup();

        EasyMock.expect(mockRoadGraph.getRoad(roadId)).andReturn(mockRoad);
        EasyMock.expect(mockRoad.isBuildableBy(player)).andReturn(true);
        EasyMock.expect(mockRoad.getOwner()).andReturn(null);

        EasyMock.replay(mockRoad,mockRoadGraph);

        assertThrows(NotEnoughResourcesException.class,()-> game.placeRoad(roadId,vertexId,player));
        assertNotEquals(mockRoad.getOwner(),player);
        EasyMock.verify(mockRoad,mockRoadGraph);
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
}
