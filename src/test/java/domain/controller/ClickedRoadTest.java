package domain.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import domain.controller.Controller;
import domain.controller.GamePhase;
import domain.controller.GameState;
import domain.controller.SuccessCode;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import domain.game.Game;
import domain.game.GameType;
import domain.game.InvalidPlacementException;
import domain.game.NotEnoughResourcesException;
import domain.player.Player;

public class ClickedRoadTest {
    @Test
    void testClickedRoad_RegPlay_NoChange(){
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        Player mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer2 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer3 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer4 = EasyMock.createStrictMock(Player.class);

        Player[] players = { mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4 };

        Controller controller = new Controller(mockedGame, players, gameType);

        controller.setPhase(GamePhase.REGULAR_PLAY);
        controller.setCurrentPlayer(mockedPlayer1);

        // states before
        GamePhase phaseBefore = controller.getPhase();

        // method call
        EasyMock.replay(mockedGame);
        assertThrows(IllegalStateException.class,()->{controller.clickedRoad(35);});
        EasyMock.verify(mockedGame);

        // states after
        GamePhase phaseAfter = controller.getPhase();

        // assert states before the click are correct
        assertEquals(GamePhase.REGULAR_PLAY, phaseBefore);

        // assert states after the click are correct
        assertEquals(GamePhase.REGULAR_PLAY, phaseAfter);
    }
    @Test
    void testClickedRoad_Setup_FirstSettlement_NoChange(){
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        Player mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer2 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer3 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer4 = EasyMock.createStrictMock(Player.class);

        Player[] players = { mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4 };

        Controller controller = new Controller(mockedGame, players, gameType);

        controller.setPhase(GamePhase.SETUP);
        controller.setState(GameState.FIRST_SETTLEMENT);
        controller.setCurrentPlayer(mockedPlayer1);

        // states before
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();

        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.clickedRoad(35);
        EasyMock.verify(mockedGame);

        // states after
        GamePhase phaseAfter = controller.getPhase();
        GameState stateAfter = controller.getState();

        // assert states before the click are correct
        assertEquals(GamePhase.SETUP, phaseBefore);
        assertEquals(GameState.FIRST_SETTLEMENT, stateBefore);

        // assert on expected result
        assertEquals(SuccessCode.UNDEFINED, actual);

        // assert states after the click are correct
        assertEquals(GamePhase.SETUP, phaseAfter);
        assertEquals(GameState.FIRST_SETTLEMENT, stateAfter);
    }
    @Test
    void testClickedRoad_Setup_SecondtSettlement_NoChange(){
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        Player mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer2 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer3 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer4 = EasyMock.createStrictMock(Player.class);

        Player[] players = { mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4 };

        Controller controller = new Controller(mockedGame, players, gameType);

        controller.setPhase(GamePhase.SETUP);
        controller.setState(GameState.SECOND_SETTLEMENT);
        controller.setCurrentPlayer(mockedPlayer1);

        // states before
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();

        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.clickedRoad(35);
        EasyMock.verify(mockedGame);

        // states after
        GamePhase phaseAfter = controller.getPhase();
        GameState stateAfter = controller.getState();

        // assert states before the click are correct
        assertEquals(GamePhase.SETUP, phaseBefore);
        assertEquals(GameState.SECOND_SETTLEMENT, stateBefore);

        // assert on expected result
        assertEquals(SuccessCode.UNDEFINED, actual);

        // assert states after the click are correct
        assertEquals(GamePhase.SETUP, phaseAfter);
        assertEquals(GameState.SECOND_SETTLEMENT, stateAfter);
    }
    @Test
    public void testClickedRoad_Setup_FirstRoad_Invalid_NoChange() throws InvalidPlacementException, NotEnoughResourcesException {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        Player mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer2 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer3 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer4 = EasyMock.createStrictMock(Player.class);

        Player[] players = { mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4 };

        Controller controller = new Controller(mockedGame, players, gameType);
        int testVertexId = 0;
        int testRoadId = 55;
        
        controller.setPhase(GamePhase.SETUP);
        controller.setState(GameState.FIRST_ROAD);
        controller.setCurrentPlayer(mockedPlayer1);
        controller.lastPlacedVertex = testVertexId;

        mockedGame.placeRoad(testRoadId,testVertexId, mockedPlayer1);
        // game should tell us it is invalid
        EasyMock.expectLastCall().andThrow(new InvalidPlacementException());

        // states before
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();

        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.clickedRoad(testRoadId);
        EasyMock.verify(mockedGame);

        // states after
        GamePhase phaseAfter = controller.getPhase();
        GameState stateAfter = controller.getState();

        // assert states before the click are correct
        assertEquals(GamePhase.SETUP, phaseBefore);
        assertEquals(GameState.FIRST_ROAD, stateBefore);

        // assert on expected result
        assertEquals(SuccessCode.INVALID_PLACEMENT, actual);

        // assert states after the click are correct
        assertEquals(GamePhase.SETUP, phaseAfter);
        assertEquals(GameState.FIRST_ROAD, stateAfter);
    }
    @Test
    public void testClickedRoad_Setup_First_Succesesful_IncPlay() throws InvalidPlacementException, NotEnoughResourcesException{
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        Player mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer2 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer3 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer4 = EasyMock.createStrictMock(Player.class);

        Player[] players = { mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4 };

        Controller controller = new Controller(mockedGame, players, gameType);
        int testVertexId = 0;
        int testRoadId = 0;
        
        controller.setPhase(GamePhase.SETUP);
        controller.setState(GameState.FIRST_ROAD);
        controller.setCurrentPlayer(mockedPlayer1);
        controller.lastPlacedVertex = testVertexId;

        mockedGame.placeRoad(testRoadId,testVertexId,mockedPlayer1);

        // states before
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();
        Player playerBefore = controller.getCurrentPlayer();

        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.clickedRoad(testRoadId);
        EasyMock.verify(mockedGame);

        // states after
        GamePhase phaseAfter = controller.getPhase();
        GameState stateAfter = controller.getState();
        Player playerAfter = controller.getCurrentPlayer();

        // assert states before the click are correct
        assertEquals(GamePhase.SETUP, phaseBefore);
        assertEquals(GameState.FIRST_ROAD, stateBefore);
        assertEquals(mockedPlayer1,playerBefore);

        // assert on expected result
        assertEquals(SuccessCode.SUCCESS, actual);

        // assert states after the click are correct
        assertEquals(GamePhase.SETUP, phaseAfter);
        assertEquals(GameState.FIRST_SETTLEMENT, stateAfter);
        assertEquals(mockedPlayer2,playerAfter);
    }
    @Test
    public void testClickedRoad_Setup_First_Succesesful_NoInc() throws InvalidPlacementException, NotEnoughResourcesException{
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        Player mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer2 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer3 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer4 = EasyMock.createStrictMock(Player.class);

        Player[] players = { mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4 };

        Controller controller = new Controller(mockedGame, players, gameType);
        int testVertexId = 0;
        int testRoadId = 0;
        
        controller.setPhase(GamePhase.SETUP);
        controller.setState(GameState.FIRST_ROAD);
        controller.setCurrentPlayer(mockedPlayer4);
        controller.lastPlacedVertex = testVertexId;

        mockedGame.placeRoad(testRoadId,testVertexId,mockedPlayer4);
        

        // states before
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();
        Player playerBefore = controller.getCurrentPlayer();

        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.clickedRoad(testRoadId);
        EasyMock.verify(mockedGame);

        // states after
        GamePhase phaseAfter = controller.getPhase();
        GameState stateAfter = controller.getState();
        Player playerAfter = controller.getCurrentPlayer();

        // assert states before the click are correct
        assertEquals(GamePhase.SETUP, phaseBefore);
        assertEquals(GameState.FIRST_ROAD, stateBefore);
        assertEquals(mockedPlayer4, playerBefore);
        // assert on expected result
        assertEquals(SuccessCode.SUCCESS, actual);

        // assert states after the click are correct
        assertEquals(GamePhase.SETUP, phaseAfter);
        assertEquals(GameState.SECOND_SETTLEMENT, stateAfter);
        assertEquals(mockedPlayer4, playerAfter);
    }
    @Test
    public void testClickedRoad_Setup_FirstRoad_Desync() throws InvalidPlacementException, NotEnoughResourcesException{
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        Player mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer2 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer3 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer4 = EasyMock.createStrictMock(Player.class);

        Player[] players = { mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4 };

        Controller controller = new Controller(mockedGame, players, gameType);
        int testVertexId = 0;
        int testRoadId = 0;
        
        controller.setPhase(GamePhase.SETUP);
        controller.setState(GameState.FIRST_ROAD);
        controller.setCurrentPlayer(mockedPlayer1);
        controller.lastPlacedVertex = testVertexId;

        
        mockedGame.placeRoad(testRoadId,testVertexId,mockedPlayer1);
        EasyMock.expectLastCall().andThrow(new NotEnoughResourcesException());

        // states before
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();

        // method call
        EasyMock.replay(mockedGame);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            controller.clickedRoad(testRoadId);
        });
        EasyMock.verify(mockedGame);

        String expectedMessage = "Controller and Game states unsyncronized";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

        // assert states before the click are correct
        assertEquals(GamePhase.SETUP, phaseBefore);
        assertEquals(GameState.FIRST_ROAD, stateBefore);
    }
    @Test
    public void testClickedRoad_Setup_SecondRoad_Invalid_NoChange() throws InvalidPlacementException, NotEnoughResourcesException {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        Player mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer2 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer3 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer4 = EasyMock.createStrictMock(Player.class);

        Player[] players = { mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4 };

        Controller controller = new Controller(mockedGame, players, gameType);
        int testVertexId = 0;
        int testRoadId = 55;
        
        controller.setPhase(GamePhase.SETUP);
        controller.setState(GameState.SECOND_ROAD);
        controller.setCurrentPlayer(mockedPlayer1);
        controller.lastPlacedVertex = testVertexId;

        mockedGame.placeRoad(testRoadId,testVertexId, mockedPlayer1);
        // game should tell us it is invalid
        EasyMock.expectLastCall().andThrow(new InvalidPlacementException());

        // states before
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();

        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.clickedRoad(testRoadId);
        EasyMock.verify(mockedGame);

        // states after
        GamePhase phaseAfter = controller.getPhase();
        GameState stateAfter = controller.getState();

        // assert states before the click are correct
        assertEquals(GamePhase.SETUP, phaseBefore);
        assertEquals(GameState.SECOND_ROAD, stateBefore);

        // assert on expected result
        assertEquals(SuccessCode.INVALID_PLACEMENT, actual);

        // assert states after the click are correct
        assertEquals(GamePhase.SETUP, phaseAfter);
        assertEquals(GameState.SECOND_ROAD, stateAfter);
    }
    @Test
    public void testClickedRoad_Setup_SecondRoad_NoChange() throws InvalidPlacementException, NotEnoughResourcesException{
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        Player mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer2 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer3 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer4 = EasyMock.createStrictMock(Player.class);

        Player[] players = { mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4 };

        Controller controller = new Controller(mockedGame, players, gameType);
        int testVertexId = 0;
        int testRoadId = 0;
        
        controller.setPhase(GamePhase.SETUP);
        controller.setState(GameState.SECOND_ROAD);
        controller.setCurrentPlayer(mockedPlayer1);
        controller.lastPlacedVertex = testVertexId;

        mockedGame.placeRoad(testRoadId,testVertexId,mockedPlayer1);
        mockedGame.endSetup();
        EasyMock.expectLastCall().once();
        // states before
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();

        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.clickedRoad(testRoadId);
        EasyMock.verify(mockedGame);

        // states after
        GamePhase phaseAfter = controller.getPhase();
        GameState stateAfter = controller.getState();

        // assert states before the click are correct
        assertEquals(GamePhase.SETUP, phaseBefore);
        assertEquals(GameState.SECOND_ROAD, stateBefore);

        // assert on expected result
        assertEquals(SuccessCode.SUCCESS, actual);

        // assert states after the click are correct
        assertEquals(GamePhase.REGULAR_PLAY, phaseAfter);
        assertEquals(GameState.TURN_START, stateAfter);
    }
    @Test
    public void testClickedRoad_Setup_SecondRoad_IncPlayer() throws InvalidPlacementException, NotEnoughResourcesException{
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        Player mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer2 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer3 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer4 = EasyMock.createStrictMock(Player.class);

        Player[] players = { mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4 };

        Controller controller = new Controller(mockedGame, players, gameType);
        int testVertexId = 0;
        int testRoadId = 0;
        
        controller.setPhase(GamePhase.SETUP);
        controller.setState(GameState.SECOND_ROAD);
        controller.incrementPlayer();
        controller.lastPlacedVertex = testVertexId;

        mockedGame.placeRoad(testRoadId,testVertexId,mockedPlayer2);

        // states before
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();
        Player playerBefore = controller.getCurrentPlayer();

        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.clickedRoad(testRoadId);
        EasyMock.verify(mockedGame);

        // states after
        GamePhase phaseAfter = controller.getPhase();
        GameState stateAfter = controller.getState();
        Player playerAfter = controller.getCurrentPlayer();

        // assert states before the click are correct
        assertEquals(GamePhase.SETUP, phaseBefore);
        assertEquals(GameState.SECOND_ROAD, stateBefore);
        assertEquals(mockedPlayer2, playerBefore);

        // assert on expected result
        assertEquals(SuccessCode.SUCCESS, actual);

        // assert states after the click are correct
        assertEquals(GamePhase.SETUP, phaseAfter);
        assertEquals(GameState.SECOND_SETTLEMENT, stateAfter);
        assertEquals(mockedPlayer1, playerAfter);
    }
    @Test
    public void testClickedRoad_Setup_SecondRoad_NoInc() throws InvalidPlacementException, NotEnoughResourcesException{
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        Player mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer2 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer3 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer4 = EasyMock.createStrictMock(Player.class);

        Player[] players = { mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4 };

        Controller controller = new Controller(mockedGame, players, gameType);
        int testVertexId = 0;
        int testRoadId = 0;
        
        controller.setPhase(GamePhase.SETUP);
        controller.setState(GameState.SECOND_ROAD);
        controller.setCurrentPlayer(mockedPlayer1);
        controller.lastPlacedVertex = testVertexId;

        mockedGame.placeRoad(testRoadId,testVertexId,mockedPlayer1);
        mockedGame.endSetup();
        EasyMock.expectLastCall().once();
        // states before
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();
        Player playerBefore = controller.getCurrentPlayer();

        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.clickedRoad(testRoadId);
        EasyMock.verify(mockedGame);

        // states after
        GamePhase phaseAfter = controller.getPhase();
        GameState stateAfter = controller.getState();
        Player playerAfter = controller.getCurrentPlayer();

        // assert states before the click are correct
        assertEquals(GamePhase.SETUP, phaseBefore);
        assertEquals(GameState.SECOND_ROAD, stateBefore);
        assertEquals(mockedPlayer1, playerBefore);

        // assert on expected result
        assertEquals(SuccessCode.SUCCESS, actual);

        // assert states after the click are correct
        assertEquals(GamePhase.REGULAR_PLAY, phaseAfter);
        assertEquals(GameState.TURN_START, stateAfter);
        assertEquals(mockedPlayer1, playerAfter);
    }
    @Test
    public void testClickedRoad_Setup_SecondRoad_Desync() throws InvalidPlacementException, NotEnoughResourcesException{
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        Player mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer2 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer3 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer4 = EasyMock.createStrictMock(Player.class);

        Player[] players = { mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4 };

        Controller controller = new Controller(mockedGame, players, gameType);
        int testVertexId = 0;
        int testRoadId = 0;
        
        controller.setPhase(GamePhase.SETUP);
        controller.setState(GameState.SECOND_ROAD);
        controller.setCurrentPlayer(mockedPlayer1);
        controller.lastPlacedVertex = testVertexId;

        
        mockedGame.placeRoad(testRoadId,testVertexId,mockedPlayer1);
        EasyMock.expectLastCall().andThrow(new NotEnoughResourcesException());

        // states before
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();

        // method call
        EasyMock.replay(mockedGame);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            controller.clickedRoad(testRoadId);
        });
        EasyMock.verify(mockedGame);

        String expectedMessage = "Controller and Game states unsyncronized";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

        // assert states before the click are correct
        assertEquals(GamePhase.SETUP, phaseBefore);
        assertEquals(GameState.SECOND_ROAD, stateBefore);
    }
    
}
