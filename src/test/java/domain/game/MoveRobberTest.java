package domain.game;

import domain.gameboard.GameBoard;
import domain.gameboard.Tile;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class MoveRobberTest {

    GameBoard gameBoard;
    Tile tile1;
    Tile tile2;
    Game game;

    @BeforeEach
    public void createMocks() {
        gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        tile1 = EasyMock.createStrictMock(Tile.class);
        tile2 = EasyMock.createStrictMock(Tile.class);
        game = new Game(gameBoard, null, null , null, null);
    }

    @Test
    void testMoveRobber_without() {
        int tileNum = 0;

        //Expect

        EasyMock.expect(gameBoard.getTiles()).andReturn(new Tile[]{tile1});
        EasyMock.expect(gameBoard.getRobberTile()).andReturn(tile2);
        tile1.setRobber(true);
        tile2.setRobber(false);
        gameBoard.setRobberTile(tile1);

        //Replay
        EasyMock.replay(gameBoard, tile1, tile2);

        //Call method
        try{ game.moveRobber(tileNum); }
        catch(Exception e) {
            e.printStackTrace();
            fail();
        }

        //Verify
        EasyMock.verify(gameBoard, tile1, tile2);
    }

    @Test
    void testMoveRobber_with() {
        int tileNum = 0;

        //Expect
        EasyMock.expect(gameBoard.getTiles()).andReturn(new Tile[]{tile1});
        EasyMock.expect(gameBoard.getRobberTile()).andReturn(tile1);

        //Replay
        EasyMock.replay(gameBoard, tile1);

        //Call method
        assertThrows(InvalidPlacementException.class,()-> game.moveRobber(tileNum));

        //Verify
        EasyMock.verify(gameBoard, tile1);
    }

    @Test
    void testMoveRobber_invalid() {
        //Replay
        EasyMock.replay(gameBoard);

        //Call method
        assertThrows(RuntimeException.class,()-> game.moveRobber(-1));
        assertThrows(RuntimeException.class,()-> game.moveRobber(19));

        //Verify
        EasyMock.verify(gameBoard);
    }
}
