package controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import gamedatastructures.Game;
import gamedatastructures.GameType;
import gamedatastructures.InvalidPlacementException;
import gamedatastructures.NotEnoughResourcesException;
import gamedatastructures.Player;

public class RoadBuildingCardTest {
    
    @Test
    public void testRoadBuilding20() throws InvalidPlacementException, NotEnoughResourcesException {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        Player mockedPlayer1 = new Player(1); // this needed to be an actual player to avoid null pointers on hand
        Player mockedPlayer2 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer3 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer4 = EasyMock.createStrictMock(Player.class);
        Player[] players = { mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4 };

        Controller controller = new Controller(mockedGame, players, gameType);
        int testRoadId = 0;

        controller.setPhase(GamePhase.REGULAR_PLAY);
        controller.setState(GameState.ROAD_BUILDING_1);
        controller.setCurrentPlayer(mockedPlayer1);

        mockedGame.placeRoad(testRoadId,0, mockedPlayer1);
        // game runs method successfully

        // Assert on states before the click
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();
        assertEquals(GamePhase.REGULAR_PLAY, phaseBefore);
        assertEquals(GameState.ROAD_BUILDING_1, stateBefore);

        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.clickedRoad(testRoadId);
        EasyMock.verify(mockedGame);

        // assert on expected result
        assertEquals(SuccessCode.SUCCESS, actual);

        // assert states after the click are correct
        GamePhase phaseAfter = controller.getPhase();
        GameState stateAfter = controller.getState();
        assertEquals(GamePhase.REGULAR_PLAY, phaseAfter);
        assertEquals(GameState.ROAD_BUILDING_2, stateAfter);
    }

    @Test
    public void testRoadBuilding21() throws InvalidPlacementException, NotEnoughResourcesException {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        Player mockedPlayer1 = new Player(1); // this needed to be an actual player to avoid null pointers on hand
        Player mockedPlayer2 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer3 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer4 = EasyMock.createStrictMock(Player.class);
        Player[] players = { mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4 };

        Controller controller = new Controller(mockedGame, players, gameType);
        int testRoadId = 0;

        controller.setPhase(GamePhase.REGULAR_PLAY);
        controller.setState(GameState.ROAD_BUILDING_1);
        controller.setCurrentPlayer(mockedPlayer1);

        mockedGame.placeRoad(testRoadId,0, mockedPlayer1);
        EasyMock.expectLastCall().andThrow(new InvalidPlacementException());
        // game tells us the placement is invalid

        // Assert on states before the click
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();
        assertEquals(GamePhase.REGULAR_PLAY, phaseBefore);
        assertEquals(GameState.ROAD_BUILDING_1, stateBefore);

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
        assertEquals(GameState.ROAD_BUILDING_1, stateAfter);
    }

    @Test
    public void testRoadBuilding22() throws InvalidPlacementException, NotEnoughResourcesException {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        Player mockedPlayer1 = new Player(1); // this needed to be an actual player to avoid null pointers on hand
        Player mockedPlayer2 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer3 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer4 = EasyMock.createStrictMock(Player.class);
        Player[] players = { mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4 };

        Controller controller = new Controller(mockedGame, players, gameType);
        int testRoadId = 0;

        controller.setPhase(GamePhase.REGULAR_PLAY);
        controller.setState(GameState.ROAD_BUILDING_1);
        controller.setCurrentPlayer(mockedPlayer1);

        mockedGame.placeRoad(testRoadId,0, mockedPlayer1);
        EasyMock.expectLastCall().andThrow(new NotEnoughResourcesException());
        // game tells us the placement is invalid

        // Assert on states before the click
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();
        assertEquals(GamePhase.REGULAR_PLAY, phaseBefore);
        assertEquals(GameState.ROAD_BUILDING_1, stateBefore);

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
        assertEquals(GameState.DEFAULT, stateAfter);
    }

    @Test
    public void testRoadBuilding23() throws InvalidPlacementException, NotEnoughResourcesException {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        Player mockedPlayer1 = new Player(1); // this needed to be an actual player to avoid null pointers on hand
        Player mockedPlayer2 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer3 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer4 = EasyMock.createStrictMock(Player.class);
        Player[] players = { mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4 };

        Controller controller = new Controller(mockedGame, players, gameType);
        int testRoadId = 0;

        controller.setPhase(GamePhase.REGULAR_PLAY);
        controller.setState(GameState.ROAD_BUILDING_2);
        controller.setCurrentPlayer(mockedPlayer1);

        mockedGame.placeRoad(testRoadId,0, mockedPlayer1);
        // game runs method successfully

        // Assert on states before the click
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();
        assertEquals(GamePhase.REGULAR_PLAY, phaseBefore);
        assertEquals(GameState.ROAD_BUILDING_2, stateBefore);

        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.clickedRoad(testRoadId);
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
    public void testRoadBuilding24() throws InvalidPlacementException, NotEnoughResourcesException {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        Player mockedPlayer1 = new Player(1); // this needed to be an actual player to avoid null pointers on hand
        Player mockedPlayer2 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer3 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer4 = EasyMock.createStrictMock(Player.class);
        Player[] players = { mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4 };

        Controller controller = new Controller(mockedGame, players, gameType);
        int testRoadId = 0;

        controller.setPhase(GamePhase.REGULAR_PLAY);
        controller.setState(GameState.ROAD_BUILDING_2);
        controller.setCurrentPlayer(mockedPlayer1);

        mockedGame.placeRoad(testRoadId,0, mockedPlayer1);
        EasyMock.expectLastCall().andThrow(new InvalidPlacementException());
        // game tells us the placement is invalid

        // Assert on states before the click
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();
        assertEquals(GamePhase.REGULAR_PLAY, phaseBefore);
        assertEquals(GameState.ROAD_BUILDING_2, stateBefore);

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
        assertEquals(GameState.ROAD_BUILDING_2, stateAfter);
    }

    @Test
    public void testRoadBuilding25() throws InvalidPlacementException, NotEnoughResourcesException {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        Player mockedPlayer1 = new Player(1); // this needed to be an actual player to avoid null pointers on hand
        Player mockedPlayer2 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer3 = EasyMock.createStrictMock(Player.class);
        Player mockedPlayer4 = EasyMock.createStrictMock(Player.class);
        Player[] players = { mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4 };

        Controller controller = new Controller(mockedGame, players, gameType);
        int testRoadId = 0;

        controller.setPhase(GamePhase.REGULAR_PLAY);
        controller.setState(GameState.ROAD_BUILDING_2);
        controller.setCurrentPlayer(mockedPlayer1);

        mockedGame.placeRoad(testRoadId,0, mockedPlayer1);
        EasyMock.expectLastCall().andThrow(new NotEnoughResourcesException());
        // game tells us the placement is invalid

        // Assert on states before the click
        GamePhase phaseBefore = controller.getPhase();
        GameState stateBefore = controller.getState();
        assertEquals(GamePhase.REGULAR_PLAY, phaseBefore);
        assertEquals(GameState.ROAD_BUILDING_2, stateBefore);

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
        assertEquals(GameState.DEFAULT, stateAfter);
    }

    @Test
    public void testPlayRoadBuildingCardsDisabled() throws InvalidPlacementException, NotEnoughResourcesException {
        Game mockedGame = EasyMock.createMock(Game.class);
        Player mockedPlayer1 = EasyMock.createMock(Player.class);
        Player mockedPlayer2 = EasyMock.createMock(Player.class);
        Player mockedPlayer3 = EasyMock.createMock(Player.class);
        Player mockedPlayer4 = EasyMock.createMock(Player.class);
        Player[] players = {mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4};
        Controller controller = new Controller(mockedGame, players, GameType.Advanced);


        // set up the variables for the test
        controller.setDevCardsEnabled(false);
        controller.setCurrentPlayer(mockedPlayer1);


        // Expectations
        // game method not called


        // assert that dev cards are enabled before the card is played
        assertFalse(controller.getDevCardsEnabled());


        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.useRoadBuildingCard();
        EasyMock.verify(mockedGame);

        // assert on expected results
        assertEquals(SuccessCode.CANNOT_PLAY_CARD, actual);
        assertFalse(controller.getDevCardsEnabled()); // dev cards still disabled
    }
}
