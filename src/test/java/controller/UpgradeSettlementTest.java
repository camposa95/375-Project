package controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import gamedatastructures.Game;
import gamedatastructures.GameType;
import gamedatastructures.InvalidPlacementException;
import gamedatastructures.NotEnoughResourcesException;
import gamedatastructures.Player;

public class UpgradeSettlementTest {
    @Test
    public void testClickedVertex17() throws NotEnoughResourcesException, InvalidPlacementException {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        Player mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        Player[] players = {mockedPlayer1};

        Controller controller = new Controller(mockedGame, players, gameType);
        int testVertexId = 22;

        controller.setPhase(GamePhase.REGULAR_PLAY);
        controller.setState(GameState.UPGRADE_SETTLEMENT);
        controller.setCurrentPlayer(mockedPlayer1);

        // Game succeeds
        mockedGame.upgradeSettlement(mockedPlayer1, testVertexId);

        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.clickedVertex(testVertexId);
        EasyMock.verify(mockedGame);

        // assert on expected result
        assertEquals(SuccessCode.SUCCESS, actual);

        // assert states after the click are correct
        GamePhase phaseAfter = controller.getPhase();
        GameState stateAfter = controller.getState();
        assertEquals(GamePhase.REGULAR_PLAY, phaseAfter);
        assertEquals(GameState.DEFAULT, stateAfter);
    }

    @Test
    public void testClickedVertex18() throws NotEnoughResourcesException, InvalidPlacementException {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        Player mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        Player[] players = {mockedPlayer1};

        Controller controller = new Controller(mockedGame, players, gameType);
        int testVertexId = 22;

        controller.setPhase(GamePhase.REGULAR_PLAY);
        controller.setState(GameState.UPGRADE_SETTLEMENT);
        controller.setCurrentPlayer(mockedPlayer1);

        // Game tells us the player does not have enough resources
        mockedGame.upgradeSettlement(mockedPlayer1, testVertexId);
        EasyMock.expectLastCall().andThrow(new NotEnoughResourcesException());

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
        assertEquals(GameState.UPGRADE_SETTLEMENT, stateAfter);
    }

    @Test
    public void testClickedVertex19() throws NotEnoughResourcesException, InvalidPlacementException {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        Player mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        Player[] players = {mockedPlayer1};

        Controller controller = new Controller(mockedGame, players, gameType);
        int testVertexId = 22;

        controller.setPhase(GamePhase.REGULAR_PLAY);
        controller.setState(GameState.UPGRADE_SETTLEMENT);
        controller.setCurrentPlayer(mockedPlayer1);

        // Game tells us the player does not have enough resources
        mockedGame.upgradeSettlement(mockedPlayer1, testVertexId);
        EasyMock.expectLastCall().andThrow(new InvalidPlacementException());

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
        assertEquals(GameState.UPGRADE_SETTLEMENT, stateAfter);
    }
}
