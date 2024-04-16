package domain.game;

import data.GameLoader;
import domain.bank.Bank;
import domain.bank.Resource;
import domain.gameboard.GameBoard;
import domain.graphs.GameboardGraph;
import domain.player.HarvestBooster;
import domain.player.Player;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ResourcesFromVertexTest {

    GameBoard gb;
    GameboardGraph vg;
    Player mockPlayer;
    Bank bank;
    Game game;

    @BeforeEach
    public void setup() {
        gb = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gb);

        vg = new GameboardGraph(GameType.Beginner);
        GameLoader.initializeGraphs(vg);

        mockPlayer = EasyMock.createMock(Player.class);

        bank = new Bank();
        game = new Game(gb, vg, null, bank);
    }

    @Test
    public void testResourcesFromVertex_NoSettlements_ExpectNoResources() {
        int vertexId = 0;

        Resource[] expected = new Resource[0];
        Resource[] actual = game.resourcesFromVertex(mockPlayer,vertexId);
        EasyMock.replay(mockPlayer);

        assertArrayEquals(expected, actual);
        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testResourcesFromVertex_HasSettlementOnCorner_Expect1Resource() {
        int vertexId = 0;

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
        int vertexId = 2;

        vg.getVertex(vertexId).setOwner(mockPlayer);

        Resource[] expected = {Resource.ORE, Resource.WOOL};
        Resource[] actual = game.resourcesFromVertex(mockPlayer,vertexId);
        EasyMock.replay(mockPlayer);


        assertArrayEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testResourcesFromVertex_HasSettlementOnBoard_Expect3Resources() {
        int vertexId = 10;

        vg.getVertex(vertexId).setOwner(mockPlayer);

        Resource[] expected = {Resource.ORE, Resource.WOOL,Resource.BRICK};
        Resource[] actual = game.resourcesFromVertex(mockPlayer,vertexId);
        EasyMock.replay(mockPlayer);


        assertArrayEquals(expected, actual);

        EasyMock.verify(mockPlayer);
    }

    @Test
    public void testDistributeResources_NotMocked() {
        int vertexId = 0;
        int die = 10;

        Player player = new Player(1, new HarvestBooster(), bank);

        vg.getVertex(vertexId).setOwner(player);

        Resource[] expected = {Resource.ORE};
        game.distributeResources(player,vertexId);
        assertTrue(player.hand.removeResources(expected));
        assertNotNull(game.resourcesFromVertex(player,vertexId));

        game.endSetup();
        game.distributeResources(player,die);
        assertTrue(player.hand.removeResources(expected));
        assertNotNull(game.resourcesFromVertex(player,vertexId));
    }
}
