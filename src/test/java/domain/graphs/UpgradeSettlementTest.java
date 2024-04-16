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

public class UpgradeSettlementTest {

    GameBoard gameBoard;
    GameboardGraph mockGameboardGraph;
    Vertex mockVertex;
    Player mockPlayer;
    Game game;

    @BeforeEach
    public void setup() {
        gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);

        mockGameboardGraph =  EasyMock.createMock(GameboardGraph.class);
        mockVertex = EasyMock.createNiceMock(Vertex.class);
        mockPlayer = EasyMock.createMock(Player.class);

        game = new Game(gameBoard, mockGameboardGraph, null, null);
    }

    @Test
    public void test_UpgradeSettlement_Invalid() {
        int vertexId = 0;
        EasyMock.expect(mockGameboardGraph.getVertex(vertexId)).andReturn(mockVertex);
        EasyMock.expect(mockVertex.isUpgradableBy(mockPlayer)).andReturn(false);

        EasyMock.replay(mockPlayer,mockVertex, mockGameboardGraph);


        assertThrows(InvalidPlacementException.class, ()-> game.upgradeSettlement(mockPlayer,vertexId));


        EasyMock.verify(mockPlayer,mockVertex, mockGameboardGraph);
    }

    @Test
    public void test_UpgradeSettlement_NotEnough() {
        int vertexId = 0;

        EasyMock.expect(mockGameboardGraph.getVertex(vertexId)).andReturn(mockVertex);
        EasyMock.expect(mockVertex.isUpgradableBy(mockPlayer)).andReturn(true);
        EasyMock.expect(mockPlayer.canUpgradeSettlementToCity()).andReturn(false);

        EasyMock.replay(mockPlayer,mockVertex, mockGameboardGraph);


        assertThrows(NotEnoughResourcesException.class, ()-> game.upgradeSettlement(mockPlayer,vertexId));


        EasyMock.verify(mockPlayer,mockVertex, mockGameboardGraph);
    }

    @Test
    public void test_UpgradeSettlement_ToCity() {
        int vertexId = 0;

        EasyMock.expect(mockGameboardGraph.getVertex(vertexId)).andReturn(mockVertex);
        EasyMock.expect(mockVertex.isUpgradableBy(mockPlayer)).andReturn(true);
        EasyMock.expect(mockPlayer.canUpgradeSettlementToCity()).andReturn(true);

        mockVertex.upgradeToCity(mockPlayer);
        EasyMock.expectLastCall();
        
        EasyMock.replay(mockPlayer,mockVertex, mockGameboardGraph);
 

        try {
            game.upgradeSettlement(mockPlayer,vertexId);
        } catch (Exception e) {
            fail();
        }
               
        EasyMock.verify(mockPlayer,mockVertex, mockGameboardGraph);
    }
}
