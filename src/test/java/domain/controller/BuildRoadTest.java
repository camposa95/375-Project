package domain.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.game.Game;
import domain.game.GameType;
import domain.game.InvalidPlacementException;
import domain.game.NotEnoughResourcesException;
import domain.player.Player;

public class BuildRoadTest {

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
    public void test_BuildRoad_Build_Valid_Enough() throws InvalidPlacementException, NotEnoughResourcesException {
        int testRoadId = 0;

        controller.setPhase(GamePhase.REGULAR_PLAY);
        controller.setState(GameState.BUILD_ROAD);
        controller.setCurrentPlayer(mockedPlayer1);

        mockedGame.placeRoad(testRoadId,0, mockedPlayer1);
        // game runs method successfully

        // player did not gain enough points to win
        EasyMock.expect(mockedPlayer1.getVictoryPoints()).andReturn(9);

        // Assert on states before the click
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();
        assertEquals(GamePhase.REGULAR_PLAY, phaseBefore);
        assertEquals(GameState.BUILD_ROAD, stateBefore);

        // method call
        EasyMock.replay(mockedGame, mockedPlayer1);
        SuccessCode actual = controller.clickedRoad(testRoadId);
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
    public void test_BuildRoad_Build_Valid_NotEnough() throws InvalidPlacementException, NotEnoughResourcesException {
        int testRoadId = 0;

        controller.setPhase(GamePhase.REGULAR_PLAY);
        controller.setState(GameState.BUILD_ROAD);
        controller.setCurrentPlayer(mockedPlayer1);

        mockedGame.placeRoad(testRoadId,0, mockedPlayer1);
        EasyMock.expectLastCall().andThrow(new NotEnoughResourcesException());
        // game runs method successfully

        // Assert on states before the click
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();
        assertEquals(GamePhase.REGULAR_PLAY, phaseBefore);
        assertEquals(GameState.BUILD_ROAD, stateBefore);

        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.clickedRoad(testRoadId);
        EasyMock.verify(mockedGame);

        // assert on expected result
        assertEquals(SuccessCode.INSUFFICIENT_RESOURCES, actual);

        // assert states after the click are correct
        GamePhase phaseAfter = controller.getPhase();
        GameState stateAfter = controller.getState();
        assertEquals(GamePhase.REGULAR_PLAY, phaseAfter);
        assertEquals(GameState.BUILD_ROAD, stateAfter);
    }

    @Test
    public void test_BuildRoad_Build_InValid_Enough() throws InvalidPlacementException, NotEnoughResourcesException {
        int testRoadId = 0;

        controller.setPhase(GamePhase.REGULAR_PLAY);
        controller.setState(GameState.BUILD_ROAD);
        controller.setCurrentPlayer(mockedPlayer1);

        mockedGame.placeRoad(testRoadId,0, mockedPlayer1);
        EasyMock.expectLastCall().andThrow(new InvalidPlacementException());
        // game runs method successfully

        // Assert on states before the click
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();
        assertEquals(GamePhase.REGULAR_PLAY, phaseBefore);
        assertEquals(GameState.BUILD_ROAD, stateBefore);

        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.clickedRoad(testRoadId);
        EasyMock.verify(mockedGame);

        // assert on expected result
        assertEquals(SuccessCode.INVALID_PLACEMENT, actual);

        // assert states after the click are correct
        GamePhase phaseAfter = controller.getPhase();
        GameState stateAfter = controller.getState();
        assertEquals(GamePhase.REGULAR_PLAY, phaseAfter);
        assertEquals(GameState.BUILD_ROAD, stateAfter);
    }

    @Test
    public void test_BuildRoad_Default() {
        int testRoadId = 0;

        controller.setPhase(GamePhase.REGULAR_PLAY);
        controller.setState(GameState.DEFAULT);
        controller.setCurrentPlayer(mockedPlayer1);

       
        // Assert on states before the click
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();
        assertEquals(GamePhase.REGULAR_PLAY, phaseBefore);
        assertEquals(GameState.DEFAULT, stateBefore);

        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.clickedRoad(testRoadId);
       
        EasyMock.verify(mockedGame);

        // assert on expected result
        assertNotNull(actual);
        assertEquals(SuccessCode.UNDEFINED, actual);

        // assert states after the click are correct
        GamePhase phaseAfter = controller.getPhase();
        GameState stateAfter = controller.getState();
        assertEquals(GamePhase.REGULAR_PLAY, phaseAfter);
        assertEquals(GameState.DEFAULT, stateAfter);
    }

    @Test
    public void test_BuildRoad_SetupStates1() {
        int testRoadId = 0;

        controller.setPhase(GamePhase.REGULAR_PLAY);
        controller.setState(GameState.FIRST_SETTLEMENT);
        controller.setCurrentPlayer(mockedPlayer1);

        // states before
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();

        // method call
        EasyMock.replay(mockedGame);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> { controller.clickedRoad(testRoadId);});
        EasyMock.verify(mockedGame);

        String expectedMessage = "Setup state appeared during regular play";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

        // assert states before the click are correct
        assertEquals(GamePhase.REGULAR_PLAY, phaseBefore);
        assertEquals(GameState.FIRST_SETTLEMENT, stateBefore);

        
    }

    @Test
    public void test_BuildRoad_SetupStates2() {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        Player mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer2 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer3 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer4 = EasyMock.createStrictMock(Player.class);

        Player[] players = { mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4 };

        Controller controller = new Controller(mockedGame, players, gameType);
        int testRoadId = 0;

        controller.setPhase(GamePhase.REGULAR_PLAY);
        controller.setState(GameState.SECOND_SETTLEMENT);
        controller.setCurrentPlayer(mockedPlayer1);

        // states before
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();

        // method call
        EasyMock.replay(mockedGame);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> { controller.clickedRoad(testRoadId);});
        EasyMock.verify(mockedGame);

        String expectedMessage = "Setup state appeared during regular play";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

        // assert states before the click are correct
        assertEquals(GamePhase.REGULAR_PLAY, phaseBefore);
        assertEquals(GameState.SECOND_SETTLEMENT, stateBefore);

        
    }

    @Test
    public void test_BuildRoad_SetupStates3() {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        Player mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer2 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer3 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer4 = EasyMock.createStrictMock(Player.class);

        Player[] players = { mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4 };

        Controller controller = new Controller(mockedGame, players, gameType);
        int testRoadId = 0;

        controller.setPhase(GamePhase.REGULAR_PLAY);
        controller.setState(GameState.FIRST_ROAD);
        controller.setCurrentPlayer(mockedPlayer1);

        // states before
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();

        // method call
        EasyMock.replay(mockedGame);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> { controller.clickedRoad(testRoadId);});
        EasyMock.verify(mockedGame);

        String expectedMessage = "Setup state appeared during regular play";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

        // assert states before the click are correct
        assertEquals(GamePhase.REGULAR_PLAY, phaseBefore);
        assertEquals(GameState.FIRST_ROAD, stateBefore);

        
    }

    @Test
    public void test_BuildRoad_SetupStates4() {
        int testRoadId = 0;

        controller.setPhase(GamePhase.REGULAR_PLAY);
        controller.setState(GameState.SECOND_ROAD);
        controller.setCurrentPlayer(mockedPlayer1);

        // states before
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();

        // method call
        EasyMock.replay(mockedGame);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> { controller.clickedRoad(testRoadId);});
        EasyMock.verify(mockedGame);

        String expectedMessage = "Setup state appeared during regular play";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

        // assert states before the click are correct
        assertEquals(GamePhase.REGULAR_PLAY, phaseBefore);
        assertEquals(GameState.SECOND_ROAD, stateBefore);

        
    }

    @Test
    public void test_BuildRoad_Win() throws InvalidPlacementException, NotEnoughResourcesException {
        int testRoadId = 0;

        controller.setPhase(GamePhase.REGULAR_PLAY);
        controller.setState(GameState.BUILD_ROAD);
        controller.setCurrentPlayer(mockedPlayer1);

        mockedGame.placeRoad(testRoadId,0, mockedPlayer1);
        // game runs method successfully

        // player gained enough points to win
        EasyMock.expect(mockedPlayer1.getVictoryPoints()).andReturn(10);

        // Assert on states before the click
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();
        assertEquals(GamePhase.REGULAR_PLAY, phaseBefore);
        assertEquals(GameState.BUILD_ROAD, stateBefore);

        // method call
        EasyMock.replay(mockedGame, mockedPlayer1);
        SuccessCode actual = controller.clickedRoad(testRoadId);
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