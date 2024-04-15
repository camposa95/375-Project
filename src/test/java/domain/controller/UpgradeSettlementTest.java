package domain.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.game.Game;
import domain.game.GameType;
import domain.game.InvalidPlacementException;
import domain.game.NotEnoughResourcesException;
import domain.player.Player;

public class UpgradeSettlementTest {

    Game mockedGame;
    Player mockedPlayer;
    Player[] players;
    Controller controller;

    @BeforeEach
    public void setupMocks() {
        mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        mockedPlayer = EasyMock.createStrictMock(Player.class);
        players = new Player[]{mockedPlayer};

        controller = new Controller(mockedGame, players, gameType);
    }

    @Test
    public void testClickedVertex17() throws NotEnoughResourcesException, InvalidPlacementException {
        int testVertexId = 22;

        controller.setPhase(GamePhase.REGULAR_PLAY);
        controller.setState(GameState.UPGRADE_SETTLEMENT);
        controller.setCurrentPlayer(mockedPlayer);

        // Game succeeds
        mockedGame.upgradeSettlement(mockedPlayer, testVertexId);

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
        int testVertexId = 22;

        controller.setPhase(GamePhase.REGULAR_PLAY);
        controller.setState(GameState.UPGRADE_SETTLEMENT);
        controller.setCurrentPlayer(mockedPlayer);

        // Game tells us the player does not have enough resources
        mockedGame.upgradeSettlement(mockedPlayer, testVertexId);
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
        int testVertexId = 22;

        controller.setPhase(GamePhase.REGULAR_PLAY);
        controller.setState(GameState.UPGRADE_SETTLEMENT);
        controller.setCurrentPlayer(mockedPlayer);

        // Game tells us the player does not have enough resources
        mockedGame.upgradeSettlement(mockedPlayer, testVertexId);
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
