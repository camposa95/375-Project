package domain.graphs;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import data.GameLoader;
import domain.game.Game;
import domain.game.GameType;
import domain.game.InvalidPlacementException;
import domain.game.NotEnoughResourcesException;
import domain.gameboard.GameBoard;
import domain.player.Player;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import domain.graphs.RoadGraph;
import domain.graphs.Vertex;
import domain.graphs.VertexGraph;

public class UpgradeSettlementTest {

    @Test
    public void test_UpgradeSettlement_Invalid() {
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        RoadGraph rg = new RoadGraph();
        int vertexId = 0;

        VertexGraph mockVertexGraph =  EasyMock.createMock(VertexGraph.class);
        Vertex mockVertex = EasyMock.createNiceMock(Vertex.class);
        Player mockPlayer = EasyMock.createMock(Player.class);

        Game game = new Game(gameBoard, mockVertexGraph, rg, null, null);
        EasyMock.expect(mockVertexGraph.getVertex(vertexId)).andReturn(mockVertex);
        EasyMock.expect(mockVertex.isUpgradableBy(mockPlayer)).andReturn(false);
        
        EasyMock.replay(mockPlayer,mockVertex,mockVertexGraph);
 
        
        assertThrows(InvalidPlacementException.class, ()->{game.upgradeSettlement(mockPlayer,vertexId);});
        
       
        EasyMock.verify(mockPlayer,mockVertex,mockVertexGraph);
    }
    @Test
    public void test_UpgradeSettlement_NotEnough(){
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        RoadGraph rg = new RoadGraph();
        int vertexId = 0;

        VertexGraph mockVertexGraph =  EasyMock.createMock(VertexGraph.class);
        Vertex mockVertex = EasyMock.createNiceMock(Vertex.class);
        Player mockPlayer = EasyMock.createMock(Player.class);

        Game game = new Game(gameBoard, mockVertexGraph, rg, null, null);
        EasyMock.expect(mockVertexGraph.getVertex(vertexId)).andReturn(mockVertex);
        EasyMock.expect(mockVertex.isUpgradableBy(mockPlayer)).andReturn(true);
        EasyMock.expect(mockPlayer.upgradeSettlementToCity()).andReturn(false);
        
        EasyMock.replay(mockPlayer,mockVertex,mockVertexGraph);
 
        
        assertThrows(NotEnoughResourcesException.class, ()->{game.upgradeSettlement(mockPlayer,vertexId);});
        
       
        EasyMock.verify(mockPlayer,mockVertex,mockVertexGraph);
    }
    @Test
    public void test_UpgradeSettlement_ToCity() {
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        RoadGraph rg = new RoadGraph();
        int vertexId = 0;

        VertexGraph mockVertexGraph =  EasyMock.createMock(VertexGraph.class);
        Vertex mockVertex = EasyMock.createNiceMock(Vertex.class);
        Player mockPlayer = EasyMock.createMock(Player.class);

        Game game = new Game(gameBoard, mockVertexGraph, rg, null, null);
        EasyMock.expect(mockVertexGraph.getVertex(vertexId)).andReturn(mockVertex);
        EasyMock.expect(mockVertex.isUpgradableBy(mockPlayer)).andReturn(true);
        EasyMock.expect(mockPlayer.upgradeSettlementToCity()).andReturn(true);

        mockVertex.setIsCity(true);
        EasyMock.expectLastCall();
        
        EasyMock.replay(mockPlayer,mockVertex,mockVertexGraph);
 
        
        try{
            game.upgradeSettlement(mockPlayer,vertexId);
        } catch (Exception e) {
            assertFalse(true);
        }
               
        EasyMock.verify(mockPlayer,mockVertex,mockVertexGraph);
    }
}
