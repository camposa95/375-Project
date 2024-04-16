package domain.game;

import domain.bank.Resource;
import domain.gameboard.GameBoard;
import domain.player.Hand;
import domain.player.Player;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StealFromPlayerTest {

    GameBoard gameBoard;
    Player robber;
    Player robbed;
    Game game;

    @BeforeEach
    public void setup() {
        gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        robber = new Player(1);
        robbed = new Player(2);
        game = new Game(gameBoard, null, null, null);
    }

    @Test
    void test_StealFromPlayer_None() {
        // Shadow the default concrete setup
        Player mockedRobber = EasyMock.createStrictMock(Player.class);
        Player mockedVictim = EasyMock.createStrictMock(Player.class);
        mockedVictim.hand = EasyMock.createStrictMock(Hand.class);
        Game game = new Game(gameBoard, null, null , null);

        //Expect
        EasyMock.expect(mockedVictim.hand.getResourceCount()).andReturn(0);
        //Replay
        EasyMock.replay(gameBoard, mockedRobber, mockedVictim);

        //Call method
        assertThrows(NotEnoughResourcesException.class, ()-> game.stealFromPlayer(mockedRobber,mockedVictim));

        //Verify
        EasyMock.verify(gameBoard, mockedRobber, mockedVictim);
    }

    @Test
    void test_StealFromPlayer_One() {
        EasyMock.replay(gameBoard);
        robbed.hand.addResource(Resource.WOOL, 1);

        int robberBefore = robber.hand.getResourceCount();
        int robbedBefore = robbed.hand.getResourceCount();

        //Call method
        try{ game.stealFromPlayer(robber,robbed); }
        catch(Exception e) {
            e.printStackTrace();
            fail();
        }

        int robberAfter = robber.hand.getResourceCount();
        int robbedAfter = robbed.hand.getResourceCount();

        assertEquals(robberBefore, 0);
        assertEquals(robbedBefore,1);
        assertEquals(robberAfter, 1);
        assertEquals(robbedAfter,0);
        EasyMock.verify(gameBoard);
    }

    @Test
    void test_StealFromPlayer_Many() {
        EasyMock.replay(gameBoard);
        robbed.hand.addResources(new Resource[]{Resource.WOOL,Resource.BRICK,Resource.LUMBER});

        int robberBefore = robber.hand.getResourceCount();
        int robbedBefore = robbed.hand.getResourceCount();

        //Call method
        try{ game.stealFromPlayer(robber,robbed); }
        catch(Exception e) {
            e.printStackTrace();
            fail();
        }

        int robberAfter = robber.hand.getResourceCount();
        int robbedAfter = robbed.hand.getResourceCount();

        assertEquals(robberBefore, 0);
        assertEquals(robbedBefore,3);
        assertEquals(robberAfter, 1);
        assertEquals(robbedAfter,2);
        EasyMock.verify(gameBoard);
    }
}
