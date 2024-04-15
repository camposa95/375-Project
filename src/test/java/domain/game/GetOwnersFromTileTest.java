package domain.game;

import domain.gameboard.GameBoard;
import domain.graphs.Vertex;
import domain.graphs.VertexGraph;
import domain.player.Player;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GetOwnersFromTileTest {

    GameBoard gameBoard;
    VertexGraph mockVertexGraph;
    Vertex mockVertex;
    Game game;
    Player mockPlayer1, mockPlayer2;
    Vertex mockVertex1, mockVertex2, mockVertex3;

    @BeforeEach
    public void setupMocks() {
        gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        mockVertexGraph = EasyMock.createStrictMock(VertexGraph.class);
        mockVertex = EasyMock.createStrictMock(Vertex.class);
        game = new Game(gameBoard, mockVertexGraph, null, null, null);

        mockPlayer1 = EasyMock.createStrictMock(Player.class);
        mockPlayer2 = EasyMock.createStrictMock(Player.class);

        mockVertex1 = EasyMock.createStrictMock(Vertex.class);
        mockVertex2 = EasyMock.createStrictMock(Vertex.class);
        mockVertex3 = EasyMock.createStrictMock(Vertex.class);
    }

    @Test
    void test_GetPlayersFromTile_None() {
        int tileId = 0;

        //Expect
        List<Integer> expect = new ArrayList<>();
        expect.add(0);
        EasyMock.expect(gameBoard.getTileVertexIDs(tileId)).andReturn(expect);
        EasyMock.expect(mockVertexGraph.getVertex(0)).andReturn(mockVertex);
        EasyMock.expect(mockVertex.isOccupied()).andReturn(false);
        //Replay
        EasyMock.replay(gameBoard, mockVertexGraph, mockVertex);

        Player[] actual = null;
        //Call method
        try {
            actual = game.getPlayersFromTile(tileId);
        }
        catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        assertEquals(actual.length, 0);
        EasyMock.verify(gameBoard, mockVertexGraph, mockVertex);
    }

    @Test
    void test_GetPlayersFromTile_One() {
        int tileId = 0;

        //Expect
        List<Integer> expect = new ArrayList<>();
        expect.add(0);
        EasyMock.expect(gameBoard.getTileVertexIDs(tileId)).andReturn(expect);
        EasyMock.expect(mockVertexGraph.getVertex(0)).andReturn(mockVertex);
        EasyMock.expect(mockVertex.isOccupied()).andReturn(true);
        EasyMock.expect(mockVertex.getOwner()).andReturn(mockPlayer1);

        //Replay
        EasyMock.replay(gameBoard, mockVertexGraph, mockVertex, mockPlayer1);


        Player[] actual = null;
        //Call method
        try{actual = game.getPlayersFromTile(tileId); }
        catch(Exception e) {
            e.printStackTrace();
            fail();
        }

        assertEquals(actual.length, 1);
        assertEquals(actual[0], mockPlayer1);
        EasyMock.verify(gameBoard, mockVertexGraph, mockVertex, mockPlayer1);
    }

    @Test
    void test_GetPlayersFromTile_ManyFromOne() {
        int tileId = 0;

        //Expect
        List<Integer> expected = new ArrayList<>();
        expected.add(0);
        expected.add(1);
        expected.add(2);

        EasyMock.expect(gameBoard.getTileVertexIDs(tileId)).andReturn(expected);
        EasyMock.expect(mockVertexGraph.getVertex(0)).andReturn(mockVertex1);
        EasyMock.expect(mockVertex1.isOccupied()).andReturn(true);
        EasyMock.expect(mockVertex1.getOwner()).andReturn(mockPlayer1);

        EasyMock.expect(mockVertexGraph.getVertex(1)).andReturn(mockVertex2);
        EasyMock.expect(mockVertex2.isOccupied()).andReturn(true);
        EasyMock.expect(mockVertex2.getOwner()).andReturn(mockPlayer1);

        EasyMock.expect(mockVertexGraph.getVertex(2)).andReturn(mockVertex3);
        EasyMock.expect(mockVertex3.isOccupied()).andReturn(true);
        EasyMock.expect(mockVertex3.getOwner()).andReturn(mockPlayer1);

        //Replay
        EasyMock.replay(gameBoard, mockVertexGraph, mockVertex1,mockVertex2,mockVertex3, mockPlayer1);


        Player[] actual = null;
        //Call method
        try{actual = game.getPlayersFromTile(tileId); }
        catch(Exception e) {
            e.printStackTrace();
            fail();
        }

        assertEquals(actual.length, 1);
        assertEquals(actual[0], mockPlayer1);
        EasyMock.verify(gameBoard, mockVertexGraph, mockVertex1,mockVertex2,mockVertex3, mockPlayer1);
    }

    @Test
    void test_GetPlayersFromTile_2From2() {
        int tileId = 0;

        //Expect
        List<Integer> expected = new ArrayList<>();
        expected.add(0);
        expected.add(1);
        EasyMock.expect(gameBoard.getTileVertexIDs(tileId)).andReturn(expected);
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
            fail();
        }

        assertEquals(actual.length, 2);
        assertEquals(actual[0], mockPlayer1);
        assertEquals(actual[1], mockPlayer2);
        EasyMock.verify(gameBoard, mockVertexGraph, mockVertex1,mockVertex2,mockPlayer1, mockPlayer2);
    }

    @Test
    void test_GetPlayersFromTile_Error() {

        //Replay
        EasyMock.replay(gameBoard, mockVertexGraph, mockVertex);

        //Call method
        assertThrows(IllegalArgumentException.class,()-> game.getPlayersFromTile(-1));
        assertThrows(IllegalArgumentException.class,()-> game.getPlayersFromTile(19));

        EasyMock.verify(gameBoard,mockVertexGraph, mockVertex);
    }
}
