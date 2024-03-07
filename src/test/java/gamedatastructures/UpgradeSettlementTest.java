package gamedatastructures;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import graphs.RoadGraph;
import graphs.Vertex;
import graphs.VertexGraph;

public class UpgradeSettlementTest {
    private static final String LAYOUT_FILE = "src/main/java/gamedatastructures/TileLayout.txt";

    @Test
    public void test_UpgradeSettlement_Invalid(){
        GameBoard gb = new GameBoard(GameType.Beginner,LAYOUT_FILE);
        RoadGraph rg = new RoadGraph();
        int vertexId = 0;

        VertexGraph mockVertexGraph =  EasyMock.createMock(VertexGraph.class);
        Vertex mockVertex = EasyMock.createNiceMock(Vertex.class);
        Player mockPlayer = EasyMock.createMock(Player.class);

        Game game = new Game(gb,mockVertexGraph,rg,null);
        EasyMock.expect(mockVertexGraph.getVertex(vertexId)).andReturn(mockVertex);
        EasyMock.expect(mockVertex.isUpgradableBy(mockPlayer)).andReturn(false);
        
        EasyMock.replay(mockPlayer,mockVertex,mockVertexGraph);
 
        
        assertThrows(InvalidPlacementException.class, ()->{game.upgradeSettlement(mockPlayer,vertexId);});
        
       
        EasyMock.verify(mockPlayer,mockVertex,mockVertexGraph);
    }
    @Test
    public void test_UpgradeSettlement_NotEnough(){
        GameBoard gb = new GameBoard(GameType.Beginner,LAYOUT_FILE);
        RoadGraph rg = new RoadGraph();
        int vertexId = 0;

        VertexGraph mockVertexGraph =  EasyMock.createMock(VertexGraph.class);
        Vertex mockVertex = EasyMock.createNiceMock(Vertex.class);
        Player mockPlayer = EasyMock.createMock(Player.class);

        Game game = new Game(gb,mockVertexGraph,rg,null);
        EasyMock.expect(mockVertexGraph.getVertex(vertexId)).andReturn(mockVertex);
        EasyMock.expect(mockVertex.isUpgradableBy(mockPlayer)).andReturn(true);
        EasyMock.expect(mockPlayer.upgradeSettlementToCity()).andReturn(false);
        
        EasyMock.replay(mockPlayer,mockVertex,mockVertexGraph);
 
        
        assertThrows(NotEnoughResourcesException.class, ()->{game.upgradeSettlement(mockPlayer,vertexId);});
        
       
        EasyMock.verify(mockPlayer,mockVertex,mockVertexGraph);
    }
    @Test
    public void test_UpgradeSettlement_ToCity(){
        GameBoard gb = new GameBoard(GameType.Beginner,LAYOUT_FILE);
        RoadGraph rg = new RoadGraph();
        int vertexId = 0;

        VertexGraph mockVertexGraph =  EasyMock.createMock(VertexGraph.class);
        Vertex mockVertex = EasyMock.createNiceMock(Vertex.class);
        Player mockPlayer = EasyMock.createMock(Player.class);

        Game game = new Game(gb,mockVertexGraph,rg,null);
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
