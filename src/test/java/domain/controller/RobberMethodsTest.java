package domain.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import domain.controller.Controller;
import domain.controller.SuccessCode;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import domain.game.Game;
import domain.game.GameType;
import domain.game.InvalidPlacementException;
import domain.game.NotEnoughResourcesException;
import domain.player.Player;

public class RobberMethodsTest {
    
    @Test
    public void testGetPlayersOnTile() {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        Player mockedPlayer = EasyMock.createStrictMock(Player.class);
        GameType gameType = GameType.Advanced;

        Player[] expectedPlayers = {mockedPlayer}; // not players don't matter
        Controller controller = new Controller(mockedGame, expectedPlayers, gameType);

        int tileId = 4;

        // Expectations
        EasyMock.expect(mockedGame.getPlayersFromTile(tileId)).andReturn(expectedPlayers);

        // method call
        EasyMock.replay(mockedGame);
        Player[] actualPlayers = controller.getPlayersOnTile(tileId);
        EasyMock.verify(mockedGame);
        
        // assert that the controller method returns what the game method does
        assertTrue(expectedPlayers == actualPlayers);
    }

    @Test
    public void testMoveRobberSuccess() throws InvalidPlacementException {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        Player mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        Player[] players = { mockedPlayer1};
        GameType gameType = GameType.Advanced;

        Controller controller = new Controller(mockedGame, players, gameType);
        int tileID = 3;

        mockedGame.moveRobber(tileID);

        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.moveRobber(tileID);
        EasyMock.verify(mockedGame);

        // assert on expected result
        assertEquals(SuccessCode.SUCCESS, actual);
    }

    @Test
    public void testMoveRobberInvalidPlacement() throws InvalidPlacementException {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        Player mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        Player[] players = { mockedPlayer1};
        GameType gameType = GameType.Advanced;

        Controller controller = new Controller(mockedGame, players, gameType);
        int tileID = 3;

        mockedGame.moveRobber(tileID);
        // game should tell us it is invalid
        EasyMock.expectLastCall().andThrow(new InvalidPlacementException());

        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.moveRobber(tileID);
        EasyMock.verify(mockedGame);

        // assert on expected result
        assertEquals(SuccessCode.INVALID_PLACEMENT, actual);
    }

    @Test
    public void testRobPlayerSuccess() throws NotEnoughResourcesException {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        Player player1 = new Player(1);
        Player player2 = new Player(2);
        Player player3 = new Player(3);
        Player[] players = {player1, player2, player3};
        GameType gameType = GameType.Advanced;

        Controller controller = new Controller(mockedGame, players, gameType);
        int playerId = 2;
        controller.setCurrentPlayer(player1); // any player should do as long it is not the same as the robbee


        // set expectations
        mockedGame.stealFromPlayer(player1, player2);
        // this should succeed

        // method call
        EasyMock.replay(mockedGame); // don't really care about players here
        SuccessCode actual = controller.robPlayer(playerId);
        EasyMock.verify(mockedGame);

        // assert on expected result
        assertEquals(SuccessCode.SUCCESS, actual);
    }

    @Test
    public void testRobPlayerNotEnoughResources() throws NotEnoughResourcesException {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        Player player1 = new Player(1);
        Player player2 = new Player(2);
        Player player3 = new Player(3);
        Player[] players = {player1, player2, player3};
        GameType gameType = GameType.Advanced;

        Controller controller = new Controller(mockedGame, players, gameType);
        int playerId = 2;
        controller.setCurrentPlayer(player1); // any player should do as long it is not the same as the robbee


        // set expectations
        mockedGame.stealFromPlayer(player1, player2);
        // game should tell us the player doesn't have enough resource
        EasyMock.expectLastCall().andThrow(new NotEnoughResourcesException());

        // method call
        EasyMock.replay(mockedGame); // don't really care about players here
        SuccessCode actual = controller.robPlayer(playerId);
        EasyMock.verify(mockedGame);

        // assert on expected result
        assertEquals(SuccessCode.INSUFFICIENT_RESOURCES, actual);
    }

    @Test
    public void testRobPlayerDNE() throws NotEnoughResourcesException {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        Player player1 = new Player(1);
        Player player2 = new Player(2);
        Player player3 = new Player(3);
        Player[] players = {player1, player2, player3};
        GameType gameType = GameType.Advanced;

        Controller controller = new Controller(mockedGame, players, gameType);
        int playerId = 4;
        controller.setCurrentPlayer(player1); // any player should do as long it is not the same as the robbee


        // set expectations
        // game method should not be called

        // method call
        EasyMock.replay(mockedGame); // don't really care about players here
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.robPlayer(playerId);
        });
        EasyMock.verify(mockedGame);

        String expectedMessage = "Player to rob does not exist";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testRobPlayerSameAsRobber() throws NotEnoughResourcesException {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        Player player1 = new Player(1);
        Player player2 = new Player(2);
        Player player3 = new Player(3);
        Player[] players = {player1, player2, player3};
        GameType gameType = GameType.Advanced;

        Controller controller = new Controller(mockedGame, players, gameType);
        int playerId = 1;
        controller.setCurrentPlayer(player1); // any player should do as long it is not the same as the robbee


        // set expectations
        // game method should not be called

        // method call
        EasyMock.replay(mockedGame); // don't really care about players here
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.robPlayer(playerId);
        });
        EasyMock.verify(mockedGame);

        String expectedMessage = "Cannot Rob Yourself";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
