package domain.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.game.Game;
import domain.game.GameType;
import domain.game.InvalidPlacementException;
import domain.game.NotEnoughResourcesException;
import domain.player.Player;

public class ClickedVertexTest {

    Game mockedGame;
    Player mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4;
    Player[] players;
    Controller controller;

    @BeforeEach
    public void setupMocks() {
        mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        mockedPlayer2 = EasyMock.createStrictMock(Player.class);
        mockedPlayer3 = EasyMock.createStrictMock(Player.class);
        mockedPlayer4 = EasyMock.createStrictMock(Player.class);

        players = new Player[]{mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4};

        controller = new Controller(mockedGame, players, gameType);
    }
    
    @Test
    public void testClickedVertex1() {
        controller.setPhase(GamePhase.SETUP);
        controller.setState(GameState.FIRST_ROAD);
        controller.setCurrentPlayer(mockedPlayer1);

        // states before
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();

        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.clickedVertex(22);
        EasyMock.verify(mockedGame);

        // states after
        GamePhase phaseAfter = controller.getPhase();
        GameState stateAfter = controller.getState();

        // assert states before the click are correct
        assertEquals(GamePhase.SETUP, phaseBefore);
        assertEquals(GameState.FIRST_ROAD, stateBefore);

        // assert on expected result
        assertEquals(SuccessCode.UNDEFINED, actual);

        // assert states after the click are correct
        assertEquals(GamePhase.SETUP, phaseAfter);
        assertEquals(GameState.FIRST_ROAD, stateAfter);
    }

    @Test
    public void testClickedVertex2() {
        controller.setPhase(GamePhase.SETUP);
        controller.setState(GameState.SECOND_ROAD);
        controller.setCurrentPlayer(mockedPlayer1);

        // states before
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();

        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.clickedVertex(22);
        EasyMock.verify(mockedGame);

        // states after
        GamePhase phaseAfter = controller.getPhase();
        GameState stateAfter = controller.getState();

        // assert states before the click are correct
        assertEquals(GamePhase.SETUP, phaseBefore);
        assertEquals(GameState.SECOND_ROAD, stateBefore);

        // assert on expected result
        assertEquals(SuccessCode.UNDEFINED, actual);

        // assert states after the click are correct
        assertEquals(GamePhase.SETUP, phaseAfter);
        assertEquals(GameState.SECOND_ROAD, stateAfter);
    }

    @Test
    public void testClickedVertex3() throws InvalidPlacementException, NotEnoughResourcesException {
        int testVertexId = 22;

        controller.setPhase(GamePhase.SETUP);
        controller.setState(GameState.FIRST_SETTLEMENT);
        controller.setCurrentPlayer(mockedPlayer1);

        mockedGame.placeSettlement(testVertexId, mockedPlayer1);
        // game should tell us it is invalid
        EasyMock.expectLastCall().andThrow(new InvalidPlacementException());

        // states before
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();

        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.clickedVertex(testVertexId);
        EasyMock.verify(mockedGame);

        // states after
        GamePhase phaseAfter = controller.getPhase();
        GameState stateAfter = controller.getState();

        // assert states before the click are correct
        assertEquals(GamePhase.SETUP, phaseBefore);
        assertEquals(GameState.FIRST_SETTLEMENT, stateBefore);

        // assert on expected result
        assertEquals(SuccessCode.INVALID_PLACEMENT, actual);

        // assert states after the click are correct
        assertEquals(GamePhase.SETUP, phaseAfter);
        assertEquals(GameState.FIRST_SETTLEMENT, stateAfter);
    }

    @Test
    public void testClickedVertex4() throws InvalidPlacementException, NotEnoughResourcesException {
        int testVertexId = 22;

        controller.setPhase(GamePhase.SETUP);
        controller.setState(GameState.FIRST_SETTLEMENT);
        controller.setCurrentPlayer(mockedPlayer1);

        mockedGame.placeSettlement(testVertexId, mockedPlayer1);
        // game runs method successfully

        // states before
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();

        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.clickedVertex(testVertexId);
        EasyMock.verify(mockedGame);

        // states after
        GamePhase phaseAfter = controller.getPhase();
        GameState stateAfter = controller.getState();

        // assert states before the click are correct
        assertEquals(GamePhase.SETUP, phaseBefore);
        assertEquals(GameState.FIRST_SETTLEMENT, stateBefore);

        // assert on expected result
        assertEquals(SuccessCode.SUCCESS, actual);

        // assert states after the click are correct
        assertEquals(GamePhase.SETUP, phaseAfter);
        assertEquals(GameState.FIRST_ROAD, stateAfter);
    }

    @Test
    public void testClickedVertex5() throws InvalidPlacementException, NotEnoughResourcesException {
        int testVertexId = 22;

        controller.setPhase(GamePhase.SETUP);
        controller.setState(GameState.FIRST_SETTLEMENT);
        controller.setCurrentPlayer(mockedPlayer1);

        mockedGame.placeSettlement(testVertexId, mockedPlayer1);
        // game should tell us the player does not have enough resources, in reality
        // this is bad
        EasyMock.expectLastCall().andThrow(new NotEnoughResourcesException());

        // states before
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();

        // method call
        EasyMock.replay(mockedGame);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> controller.clickedVertex(testVertexId));
        EasyMock.verify(mockedGame);

        String expectedMessage = "Controller and Game states not synchronized";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

        // assert states before the click are correct
        assertEquals(GamePhase.SETUP, phaseBefore);
        assertEquals(GameState.FIRST_SETTLEMENT, stateBefore);

        // note we don't test after states because the IllegalStateException should crash
        // the program
    }

    @Test
    public void testClickedVertex6() throws InvalidPlacementException, NotEnoughResourcesException {
        int testVertexId = 22;

        controller.setPhase(GamePhase.SETUP);
        controller.setState(GameState.SECOND_SETTLEMENT);
        controller.setCurrentPlayer(mockedPlayer1);

        mockedGame.placeSettlement(testVertexId, mockedPlayer1);
        // game should tell us it is invalid
        EasyMock.expectLastCall().andThrow(new InvalidPlacementException());

        // states before
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();

        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.clickedVertex(testVertexId);
        EasyMock.verify(mockedGame);

        // states after
        GamePhase phaseAfter = controller.getPhase();
        GameState stateAfter = controller.getState();

        // assert states before the click are correct
        assertEquals(GamePhase.SETUP, phaseBefore);
        assertEquals(GameState.SECOND_SETTLEMENT, stateBefore);

        // assert on expected result
        assertEquals(SuccessCode.INVALID_PLACEMENT, actual);

        // assert states after the click are correct
        assertEquals(GamePhase.SETUP, phaseAfter);
        assertEquals(GameState.SECOND_SETTLEMENT, stateAfter);
    }

    @Test
    public void testClickedVertex7() throws InvalidPlacementException, NotEnoughResourcesException {
        int testVertexId = 22;

        controller.setPhase(GamePhase.SETUP);
        controller.setState(GameState.SECOND_SETTLEMENT);
        controller.setCurrentPlayer(mockedPlayer1);

        mockedGame.placeSettlement(testVertexId, mockedPlayer1);
        // game should succeed no error
        mockedGame.distributeResources(mockedPlayer1, testVertexId);

        // states before
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();

        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.clickedVertex(testVertexId);
        EasyMock.verify(mockedGame);

        // states after
        GamePhase phaseAfter = controller.getPhase();
        GameState stateAfter = controller.getState();

        // assert states before the click are correct
        assertEquals(GamePhase.SETUP, phaseBefore);
        assertEquals(GameState.SECOND_SETTLEMENT, stateBefore);

        // assert on expected result
        assertEquals(SuccessCode.SUCCESS, actual);

        // assert states after the click are correct
        assertEquals(GamePhase.SETUP, phaseAfter);
        assertEquals(GameState.SECOND_ROAD, stateAfter);
    }

    @Test
    public void testClickedVertex8() throws InvalidPlacementException, NotEnoughResourcesException {
        int testVertexId = 22;

        controller.setPhase(GamePhase.SETUP);
        controller.setState(GameState.SECOND_SETTLEMENT);
        controller.setCurrentPlayer(mockedPlayer1);

        mockedGame.placeSettlement(testVertexId, mockedPlayer1);
        // game should tell us the player does not have enough resources, in reality
        // this is bad
        EasyMock.expectLastCall().andThrow(new NotEnoughResourcesException());

        // states before
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();

        // method call
        EasyMock.replay(mockedGame);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> controller.clickedVertex(testVertexId));
        EasyMock.verify(mockedGame);

        String expectedMessage = "Controller and Game states not synchronized";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

        // assert states before the click are correct
        assertEquals(GamePhase.SETUP, phaseBefore);
        assertEquals(GameState.SECOND_SETTLEMENT, stateBefore);

        // note we don't test after states because the IllegalStateException should crash
        // the program
    }
}
