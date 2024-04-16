package domain.game;

import data.GameLoader;
import domain.bank.Bank;
import domain.bank.Resource;
import domain.gameboard.GameBoard;
import domain.graphs.Road;
import domain.graphs.GameboardGraph;
import domain.graphs.Vertex;
import domain.player.Player;
import org.easymock.EasyMock;
import org.easymock.MockType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PlaceRoadTest {

    GameBoard gb;
    GameboardGraph gameboardGraph;
    Road mockRoad;
    Vertex mockVertex;
    Player mockPlayer;
    Game game;

    @BeforeEach
    public void setup() {
        gb = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gb);

        gameboardGraph = EasyMock.partialMockBuilder(GameboardGraph.class)
                .addMockedMethod("getRoad", Integer.class)
                .addMockedMethod("getVertex", Integer.class)
                .addMockedMethod("giveLongestRoadCard")
                .createMock(MockType.NICE);
        mockRoad = EasyMock.createNiceMock(Road.class);
        mockPlayer = EasyMock.createMock(Player.class);
        game = new Game(gb, gameboardGraph, null, null);
    }

    private void replayMocks() {
        EasyMock.replay(mockPlayer,mockRoad, gameboardGraph);
    }

    private void verifyMocks() {
        EasyMock.verify(mockPlayer,mockRoad);
    }

    @Test
    public void testPlaceRoad_Valid_Adjacent_Enough() {
        int vertexId = 0;
        int roadId = 0;

        EasyMock.expect(gameboardGraph.getRoad(roadId)).andReturn(mockRoad);
        EasyMock.expect(gameboardGraph.getVertex(vertexId)).andReturn(mockVertex);
        EasyMock.expect(mockRoad.isAdjacentTo(mockVertex)).andReturn(true);
        EasyMock.expect(mockRoad.isBuildable()).andReturn(true);
        EasyMock.expect(mockRoad.getOwner()).andReturn(mockPlayer);
        mockPlayer.placeRoadSetup();
        gameboardGraph.giveLongestRoadCard();
        replayMocks();
        try{
            game.placeRoad(roadId,vertexId,mockPlayer);
        }
        catch(InvalidPlacementException | NotEnoughResourcesException e){
            fail();
        }
        assertEquals(mockRoad.getOwner(),mockPlayer);
        verifyMocks();
    }

    @Test
    public void testPlaceRoad_Valid_Adjacent_NotEnough() {
        int vertexId = 0;
        int roadId = 0;

        game.endSetup();

        EasyMock.expect(gameboardGraph.getRoad(roadId)).andReturn(mockRoad);
        EasyMock.expect(mockRoad.isBuildableBy(mockPlayer)).andReturn(true);
        EasyMock.expect(mockPlayer.purchaseRoad()).andReturn(false);
        EasyMock.expect(mockRoad.getOwner()).andReturn(null);

        replayMocks();

        assertThrows(NotEnoughResourcesException.class,()-> game.placeRoad(roadId,vertexId,mockPlayer));
        assertNotEquals(mockRoad.getOwner(),mockPlayer);
        verifyMocks();
    }

    @Test
    public void testPlaceRoad_Valid_NotAdjacent_Enough() {
        int vertexId = 10;
        int roadId = 0;

        EasyMock.expect(gameboardGraph.getRoad(roadId)).andReturn(mockRoad);
        EasyMock.expect(gameboardGraph.getVertex(vertexId)).andReturn(mockVertex);
        EasyMock.expect(mockRoad.isAdjacentTo(mockVertex)).andReturn(false);
        EasyMock.expect(mockRoad.isBuildable()).andReturn(true);
        EasyMock.expect(mockRoad.getOwner()).andReturn(null);

        replayMocks();

        assertThrows(InvalidPlacementException.class,()-> game.placeRoad(roadId,vertexId,mockPlayer));
        assertNotEquals(mockRoad.getOwner(),mockPlayer);
        verifyMocks();
    }

    @Test
    public void testPlaceRoad_Invalid_NotAdjacent_Enough() {
        int vertexId = 10;
        int roadId = 0;

        EasyMock.expect(gameboardGraph.getRoad(roadId)).andReturn(mockRoad);
        EasyMock.expect(mockRoad.isBuildable()).andReturn(false);
        EasyMock.expect(mockRoad.getOwner()).andReturn(null);

        replayMocks();

        assertThrows(InvalidPlacementException.class,()-> game.placeRoad(roadId,vertexId,mockPlayer));
        assertNotEquals(mockRoad.getOwner(),mockPlayer);
        verifyMocks();
    }

    @Test
    public void testPlaceRoad_Invalid_Adjacent_Enough() {
        int vertexId = 0;
        int roadId = 0;

        EasyMock.expect(gameboardGraph.getRoad(roadId)).andReturn(mockRoad);
        EasyMock.expect(mockRoad.isBuildable()).andReturn(false);
        EasyMock.expect(mockRoad.getOwner()).andReturn(null);

        replayMocks();

        assertThrows(InvalidPlacementException.class,()-> game.placeRoad(roadId,vertexId,mockPlayer));
        assertNotEquals(mockRoad.getOwner(),mockPlayer);
        verifyMocks();
    }

    @Test
    public void testPlaceRoad_Invalid_Adjacent_NotEnough() {
        int vertexId = 0;
        int roadId = 0;

        game.endSetup();

        EasyMock.expect(gameboardGraph.getRoad(roadId)).andReturn(mockRoad);
        EasyMock.expect(mockRoad.isBuildableBy(mockPlayer)).andReturn(false);
        EasyMock.expect(mockRoad.getOwner()).andReturn(null);

        replayMocks();


        assertThrows(InvalidPlacementException.class,()-> game.placeRoad(roadId,vertexId,mockPlayer));
        assertNotEquals(mockRoad.getOwner(),mockPlayer);
        verifyMocks();
    }

    @Test
    public void testPlaceRoad_Invalid_NotAdjacent_NotEnough() {
        int vertexId = 10;
        int roadId = 0;

        game.endSetup();

        EasyMock.expect(gameboardGraph.getRoad(roadId)).andReturn(mockRoad);
        EasyMock.expect(mockRoad.isBuildableBy(mockPlayer)).andReturn(false);
        EasyMock.expect(mockRoad.getOwner()).andReturn(null);

        replayMocks();

        assertThrows(InvalidPlacementException.class,()-> game.placeRoad(roadId,vertexId,mockPlayer));
        assertNotEquals(mockRoad.getOwner(),mockPlayer);
        verifyMocks();
    }

    @Test
    public void testPlaceRoad_NonSetup_ValidPlacement_NotEnoughResources() {
        int vertexId = 0;
        int roadId = 0;

        Player player = new Player(1); // shadow the mocked one here

        game.endSetup();

        EasyMock.expect(gameboardGraph.getRoad(roadId)).andReturn(mockRoad);
        EasyMock.expect(mockRoad.isBuildableBy(player)).andReturn(true);
        EasyMock.expect(mockRoad.getOwner()).andReturn(null);

        replayMocks();

        assertThrows(NotEnoughResourcesException.class,()-> game.placeRoad(roadId,vertexId,player));
        assertNotEquals(mockRoad.getOwner(),player);
        verifyMocks();
    }

    @Test
    public void testPlaceRoad_NotMocked() {
        GameBoard gb = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gb);
        int vertexId = 0;
        int roadID = 0;

        GameboardGraph gameboardGraph = new GameboardGraph(GameType.Beginner);
        GameLoader.initializeGraphs(gameboardGraph);
        Bank bank = new Bank();
        Player player = new Player(1, bank);

        Game game = new Game(gb, gameboardGraph, null, bank);

        //setup
        try{
            game.placeRoad(roadID,vertexId,player);
        }
        catch(InvalidPlacementException | NotEnoughResourcesException e){
            fail();
        }
        assertEquals(gameboardGraph.getRoad(roadID).getOwner(),player);

        //regular
        game.endSetup();
        player.hand.addResources(new Resource[] {Resource.BRICK, Resource.LUMBER});
        gameboardGraph.getVertex(vertexId).setOwner(player);
        roadID = 6;
        try{
            game.placeRoad(roadID,vertexId,player);
        }
        catch(InvalidPlacementException | NotEnoughResourcesException e){
            fail();
        }
        assertEquals(gameboardGraph.getRoad(roadID).getOwner(),player);
    }
}
