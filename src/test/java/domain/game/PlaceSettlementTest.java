package domain.game;

import data.GameLoader;
import domain.bank.Bank;
import domain.bank.Resource;
import domain.gameboard.GameBoard;
import domain.graphs.Vertex;
import domain.graphs.GameboardGraph;
import domain.player.Player;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PlaceSettlementTest {

    GameBoard gb;
    GameboardGraph mockGameboardGraph;
    Vertex mockVertex;
    Player mockPlayer;
    Game game;

    @BeforeEach
    public void setup() {
        gb = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gb);
        mockGameboardGraph =  EasyMock.createMock(GameboardGraph.class);
        mockVertex = EasyMock.createNiceMock(Vertex.class);
        mockPlayer = EasyMock.createMock(Player.class);
        game = new Game(gb, mockGameboardGraph, null, null);
    }

    private void replayMocks() {
        EasyMock.replay(mockPlayer,mockVertex, mockGameboardGraph);
    }

    private void verifyMocks() {
        EasyMock.verify(mockPlayer,mockVertex, mockGameboardGraph);
    }

    @Test
    public void testPlaceSettlement_Valid_Enough() {
        int vertexId = 0;

        EasyMock.expect(mockGameboardGraph.getVertex(vertexId)).andReturn(mockVertex);
        EasyMock.expect(mockVertex.isBuildable()).andReturn(true);
        EasyMock.expect(mockVertex.getOwner()).andReturn(mockPlayer);
        mockPlayer.placeSettlementSetup();
        mockGameboardGraph.giveLongestRoadCard();
        replayMocks();

        try{
            game.placeSettlement(vertexId,mockPlayer);
        }
        catch(InvalidPlacementException | NotEnoughResourcesException e){
            fail();
        }
        assertEquals(mockVertex.getOwner(),mockPlayer);
        verifyMocks();
    }

    @Test
    public void testPlaceSettlement_Valid_NotEnough() {
        int vertexId = 0;
        game.endSetup();

        EasyMock.expect(mockGameboardGraph.getVertex(vertexId)).andReturn(mockVertex);
        EasyMock.expect(mockVertex.isBuildable()).andReturn(true);
        EasyMock.expect(mockVertex.isAdjacentToFriendlyRoad(mockPlayer)).andReturn(true);
        EasyMock.expect(mockPlayer.purchaseSettlement()).andReturn(false);
        EasyMock.expect(mockVertex.getOwner()).andReturn(null);

        EasyMock.replay(mockPlayer,mockVertex, mockGameboardGraph);

        assertThrows(NotEnoughResourcesException.class,()-> game.placeSettlement(vertexId,mockPlayer));
        assertNotEquals(mockVertex.getOwner(),mockPlayer);
        EasyMock.verify(mockPlayer,mockVertex, mockGameboardGraph);
    }

    @Test
    public void testPlaceSettlement_Invalid_Enough() {
        int vertexId = 0;

        EasyMock.expect(mockGameboardGraph.getVertex(vertexId)).andReturn(mockVertex);
        EasyMock.expect(mockVertex.isBuildable()).andReturn(false);

        EasyMock.expect(mockVertex.getOwner()).andReturn(null);

        EasyMock.replay(mockPlayer,mockVertex, mockGameboardGraph);

        assertThrows(InvalidPlacementException.class,()-> game.placeSettlement(vertexId,mockPlayer));
        assertNotEquals(mockVertex.getOwner(),mockPlayer);
        EasyMock.verify(mockPlayer,mockVertex, mockGameboardGraph);
    }

    @Test
    public void testPlaceSettlement_Invalid_NotEnough() {
        int vertexId = 0;

        game.endSetup();

        EasyMock.expect(mockGameboardGraph.getVertex(vertexId)).andReturn(mockVertex);
        EasyMock.expect(mockVertex.isBuildable()).andReturn(false);

        EasyMock.expect(mockVertex.getOwner()).andReturn(null);


        EasyMock.replay(mockPlayer,mockVertex, mockGameboardGraph);

        assertThrows(InvalidPlacementException.class,()-> game.placeSettlement(vertexId,mockPlayer));
        assertNotEquals(mockVertex.getOwner(),mockPlayer);
        EasyMock.verify(mockPlayer,mockVertex, mockGameboardGraph);
    }

    @Test
    public void testPlaceSettlement_NotMocked() {
        int vertexId = 0;

        GameboardGraph gameboardGraph = new GameboardGraph(GameType.Beginner);
        GameLoader.initializeGraphs(gameboardGraph);

        Bank bank = new Bank();
        Player player = new Player(1, bank);

        Game game = new Game(gb, gameboardGraph, null, bank);

        try{
            game.placeSettlement(vertexId,player);
        }
        catch(InvalidPlacementException | NotEnoughResourcesException e){
            fail();
        }
        assertEquals(gameboardGraph.getVertex(vertexId).getOwner(),player);

        //regular
        game.endSetup();
        player.hand.addResources(new Resource[] {Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN});
        vertexId = 3;
        // make sure it is adjacent to a friendly road
        gameboardGraph.getRoad(3).setOwner(player);
        try{
            game.placeSettlement(vertexId,player);
        }
        catch(InvalidPlacementException | NotEnoughResourcesException e){
            fail();
        }
        assertEquals(gameboardGraph.getVertex(vertexId).getOwner(),player);
    }
}
