package domain.game;

import data.GameLoader;
import domain.bank.Bank;
import domain.bank.Resource;
import domain.gameboard.GameBoard;
import domain.graphs.RoadGraph;
import domain.graphs.Vertex;
import domain.graphs.VertexGraph;
import domain.player.Player;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PlaceSettlementTest {

    GameBoard gb;
    RoadGraph mockedRoadGraph;
    VertexGraph mockVertexGraph;
    Vertex mockVertex;
    Player mockPlayer;
    Game game;

    @BeforeEach
    public void setup() {
        gb = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gb);
        mockedRoadGraph = EasyMock.createMock(RoadGraph.class);
        mockVertexGraph =  EasyMock.createMock(VertexGraph.class);
        mockVertex = EasyMock.createNiceMock(Vertex.class);
        mockPlayer = EasyMock.createMock(Player.class);
        game = new Game(gb,mockVertexGraph,mockedRoadGraph, null, null);
    }

    @Test
    public void testPlaceSettlement_Valid_Enough() {
        int vertexId = 0;

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
        int vertexId = 0;
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
        int vertexId = 0;

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
        int vertexId = 0;

        game.endSetup();

        EasyMock.expect(mockVertexGraph.getVertex(vertexId)).andReturn(mockVertex);
        EasyMock.expect(mockVertex.isBuildable()).andReturn(false);

        EasyMock.expect(mockVertex.getOwner()).andReturn(null);


        EasyMock.replay(mockPlayer,mockVertex,mockVertexGraph);

        assertThrows(InvalidPlacementException.class,()-> game.placeSettlement(vertexId,mockPlayer));
        assertNotEquals(mockVertex.getOwner(),mockPlayer);
        EasyMock.verify(mockPlayer,mockVertex,mockVertexGraph);
    }

    @Test
    public void testPlaceSettlement_NotMocked() {
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
}
