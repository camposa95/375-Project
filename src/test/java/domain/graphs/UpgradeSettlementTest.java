package domain.graphs;

import data.GameLoader;
import domain.game.Game;
import domain.game.GameType;
import domain.game.InvalidPlacementException;
import domain.game.NotEnoughResourcesException;
import domain.gameboard.GameBoard;
import domain.player.Player;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

public class UpgradeSettlementTest {

    GameBoard gameBoard;
    RoadGraph rg;
    VertexGraph mockVertexGraph;
    Vertex mockVertex;
    Player mockPlayer;
    Game game;

    @BeforeEach
    public void setup() {
        gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        rg = new RoadGraph();

        mockVertexGraph =  EasyMock.createMock(VertexGraph.class);
        mockVertex = EasyMock.createNiceMock(Vertex.class);
        mockPlayer = EasyMock.createMock(Player.class);

        game = new Game(gameBoard, mockVertexGraph, rg, null, null);
    }

    @Test
    public void test_UpgradeSettlement_Invalid() {
        int vertexId = 0;
        EasyMock.expect(mockVertexGraph.getVertex(vertexId)).andReturn(mockVertex);
        EasyMock.expect(mockVertex.isUpgradableBy(mockPlayer)).andReturn(false);

        EasyMock.replay(mockPlayer,mockVertex,mockVertexGraph);


        assertThrows(InvalidPlacementException.class, ()-> game.upgradeSettlement(mockPlayer,vertexId));


        EasyMock.verify(mockPlayer,mockVertex,mockVertexGraph);
    }

    @Test
    public void test_UpgradeSettlement_NotEnough() {
        int vertexId = 0;

        EasyMock.expect(mockVertexGraph.getVertex(vertexId)).andReturn(mockVertex);
        EasyMock.expect(mockVertex.isUpgradableBy(mockPlayer)).andReturn(true);
        EasyMock.expect(mockPlayer.canUpgradeSettlementToCity()).andReturn(false);

        EasyMock.replay(mockPlayer,mockVertex,mockVertexGraph);


        assertThrows(NotEnoughResourcesException.class, ()-> game.upgradeSettlement(mockPlayer,vertexId));


        EasyMock.verify(mockPlayer,mockVertex,mockVertexGraph);
    }

    @Test
    public void test_UpgradeSettlement_ToCity() {
        int vertexId = 0;

        EasyMock.expect(mockVertexGraph.getVertex(vertexId)).andReturn(mockVertex);
        EasyMock.expect(mockVertex.isUpgradableBy(mockPlayer)).andReturn(true);
        EasyMock.expect(mockPlayer.canUpgradeSettlementToCity()).andReturn(true);

        mockVertex.upgradeToCity(mockPlayer);
        EasyMock.expectLastCall();
        
        EasyMock.replay(mockPlayer,mockVertex,mockVertexGraph);
 

        try {
            game.upgradeSettlement(mockPlayer,vertexId);
        } catch (Exception e) {
            fail();
        }
               
        EasyMock.verify(mockPlayer,mockVertex,mockVertexGraph);
    }
}
