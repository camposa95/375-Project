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

public class BuildSettlementTest {

    Game mockedGame;
    Player mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4;
    Player[] players;
    Controller controller;
    @BeforeEach
    public void createMocks() {
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
    public void testClickedVertex9() throws InvalidPlacementException, NotEnoughResourcesException {
        
        int testVertexId = 22;

        controller.setPhase(GamePhase.REGULAR_PLAY);
        controller.setState(GameState.BUILD_SETTLEMENT);
        controller.setCurrentPlayer(mockedPlayer1);

        mockedGame.placeSettlement(testVertexId, mockedPlayer1);
        // game runs method successfully

        // player did not gain enough points to win
        EasyMock.expect(mockedPlayer1.getVictoryPoints()).andReturn(9);

        // Assert on states before the click
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();
        assertEquals(GamePhase.REGULAR_PLAY, phaseBefore);
        assertEquals(GameState.BUILD_SETTLEMENT, stateBefore);

        // method call
        EasyMock.replay(mockedGame, mockedPlayer1);
        SuccessCode actual = controller.clickedVertex(testVertexId);
        EasyMock.verify(mockedGame, mockedPlayer1);

        // assert on expected result
        assertEquals(SuccessCode.SUCCESS, actual);

        // assert states after the click are correct
        GamePhase phaseAfter = controller.getPhase();
        GameState stateAfter = controller.getState();
        assertEquals(GamePhase.REGULAR_PLAY, phaseAfter);
        assertEquals(GameState.DEFAULT, stateAfter);
    }

    @Test
    public void testClickedVertex10() throws InvalidPlacementException, NotEnoughResourcesException {
        
        int testVertexId = 22;

        controller.setPhase(GamePhase.REGULAR_PLAY);
        controller.setState(GameState.BUILD_SETTLEMENT);
        controller.setCurrentPlayer(mockedPlayer1);

        // Game tells us that we don't have enough resources
        mockedGame.placeSettlement(testVertexId, mockedPlayer1);
        EasyMock.expectLastCall().andThrow(new NotEnoughResourcesException());


        // Assert on states before the click
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();
        assertEquals(GamePhase.REGULAR_PLAY, phaseBefore);
        assertEquals(GameState.BUILD_SETTLEMENT, stateBefore);

        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.clickedVertex(testVertexId);
        EasyMock.verify(mockedGame);

        // assert on expected result
        assertEquals(SuccessCode.INSUFFICIENT_RESOURCES, actual);

        // assert states after the click are correct
        GamePhase phaseAfter = controller.getPhase();
        GameState stateAfter = controller.getState();
        assertEquals(GamePhase.REGULAR_PLAY, phaseAfter);
        assertEquals(GameState.BUILD_SETTLEMENT, stateAfter);
    }

    @Test
    public void testClickedVertex11() throws InvalidPlacementException, NotEnoughResourcesException {
        
        int testVertexId = 22;

        controller.setPhase(GamePhase.REGULAR_PLAY);
        controller.setState(GameState.BUILD_SETTLEMENT);
        controller.setCurrentPlayer(mockedPlayer1);

        // Game tells us that the placement is invalid
        mockedGame.placeSettlement(testVertexId, mockedPlayer1);
        EasyMock.expectLastCall().andThrow(new InvalidPlacementException());


        // Assert on states before the click
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();
        assertEquals(GamePhase.REGULAR_PLAY, phaseBefore);
        assertEquals(GameState.BUILD_SETTLEMENT, stateBefore);

        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.clickedVertex(testVertexId);
        EasyMock.verify(mockedGame);

        // assert on expected result
        assertEquals(SuccessCode.INVALID_PLACEMENT, actual);

        // assert states after the click are correct
        GamePhase phaseAfter = controller.getPhase();
        GameState stateAfter = controller.getState();
        assertEquals(GamePhase.REGULAR_PLAY, phaseAfter);
        assertEquals(GameState.BUILD_SETTLEMENT, stateAfter);
    }

    @Test
    public void testClickedVertex12() {
        
        int testVertexId = 22;

        controller.setPhase(GamePhase.REGULAR_PLAY);
        controller.setState(GameState.DEFAULT);
        controller.setCurrentPlayer(mockedPlayer1);

        // Game method should not be called here

        // Assert on states before the click
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();
        assertEquals(GamePhase.REGULAR_PLAY, phaseBefore);
        assertEquals(GameState.DEFAULT, stateBefore);

        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.clickedVertex(testVertexId);
        EasyMock.verify(mockedGame);

        // assert on expected result
        assertEquals(SuccessCode.UNDEFINED, actual);

        // assert states after the click are correct
        GamePhase phaseAfter = controller.getPhase();
        GameState stateAfter = controller.getState();
        assertEquals(GamePhase.REGULAR_PLAY, phaseAfter);
        assertEquals(GameState.DEFAULT, stateAfter);
    }

    @Test
    public void testClickedVertex13() {
        int testVertexId = 22;

        controller.setPhase(GamePhase.REGULAR_PLAY);
        controller.setState(GameState.FIRST_SETTLEMENT);
        controller.setCurrentPlayer(mockedPlayer1);

        // no game method should be called

        // states before
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();

        // method call
        EasyMock.replay(mockedGame);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            controller.clickedVertex(testVertexId);
        });
        EasyMock.verify(mockedGame);

        String expectedMessage = "Setup state appeared during regular play";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

        // assert states before the click are correct
        assertEquals(GamePhase.REGULAR_PLAY, phaseBefore);
        assertEquals(GameState.FIRST_SETTLEMENT, stateBefore);

        // note we don't test after states because the IllegalStateException should crash
        // the program
    }

    @Test
    public void testClickedVertex14() {
        
        int testVertexId = 22;

        controller.setPhase(GamePhase.REGULAR_PLAY);
        controller.setState(GameState.SECOND_SETTLEMENT);
        controller.setCurrentPlayer(mockedPlayer1);

        // no game method should be called

        // states before
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();

        // method call
        EasyMock.replay(mockedGame);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            controller.clickedVertex(testVertexId);
        });
        EasyMock.verify(mockedGame);

        String expectedMessage = "Setup state appeared during regular play";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

        // assert states before the click are correct
        assertEquals(GamePhase.REGULAR_PLAY, phaseBefore);
        assertEquals(GameState.SECOND_SETTLEMENT, stateBefore);

        // note we don't test after states because the IllegalStateException should crash
        // the program
    }

    @Test
    public void testClickedVertex15() {
        
        int testVertexId = 22;

        controller.setPhase(GamePhase.REGULAR_PLAY);
        controller.setState(GameState.FIRST_ROAD);
        controller.setCurrentPlayer(mockedPlayer1);

        // no game method should be called

        // states before
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();

        // method call
        EasyMock.replay(mockedGame);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            controller.clickedVertex(testVertexId);
        });
        EasyMock.verify(mockedGame);

        String expectedMessage = "Setup state appeared during regular play";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

        // assert states before the click are correct
        assertEquals(GamePhase.REGULAR_PLAY, phaseBefore);
        assertEquals(GameState.FIRST_ROAD, stateBefore);

        // note we don't test after states because the IllegalStateException should crash
        // the program
    }

    @Test
    public void testClickedVertex16() {
        
        int testVertexId = 22;

        controller.setPhase(GamePhase.REGULAR_PLAY);
        controller.setState(GameState.SECOND_ROAD);
        controller.setCurrentPlayer(mockedPlayer1);

        // no game method should be called

        // states before
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();

        // method call
        EasyMock.replay(mockedGame);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            controller.clickedVertex(testVertexId);
        });
        EasyMock.verify(mockedGame);

        String expectedMessage = "Setup state appeared during regular play";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

        // assert states before the click are correct
        assertEquals(GamePhase.REGULAR_PLAY, phaseBefore);
        assertEquals(GameState.SECOND_ROAD, stateBefore);

        // note we don't test after states because the IllegalStateException should crash
        // the program
    }

    @Test
    public void testClickedVertex20_win() throws InvalidPlacementException, NotEnoughResourcesException {
        
        int testVertexId = 22;

        controller.setPhase(GamePhase.REGULAR_PLAY);
        controller.setState(GameState.BUILD_SETTLEMENT);
        controller.setCurrentPlayer(mockedPlayer1);

        mockedGame.placeSettlement(testVertexId, mockedPlayer1);
        // game runs method successfully

        // player gained enough points to win
        EasyMock.expect(mockedPlayer1.getVictoryPoints()).andReturn(10);

        // Assert on states before the click
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();
        assertEquals(GamePhase.REGULAR_PLAY, phaseBefore);
        assertEquals(GameState.BUILD_SETTLEMENT, stateBefore);

        // method call
        EasyMock.replay(mockedGame, mockedPlayer1);
        SuccessCode actual = controller.clickedVertex(testVertexId);
        EasyMock.verify(mockedGame, mockedPlayer1);

        // assert on expected result
        assertEquals(SuccessCode.GAME_WIN, actual);

        // assert states after the click are correct
        GamePhase phaseAfter = controller.getPhase();
        GameState stateAfter = controller.getState();
        assertEquals(GamePhase.REGULAR_PLAY, phaseAfter);
        assertEquals(GameState.DEFAULT, stateAfter);
    }
}
