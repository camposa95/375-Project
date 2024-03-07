package controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashMap;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import gamedatastructures.Game;
import gamedatastructures.GameType;
import gamedatastructures.Player;
import gamedatastructures.Resource;

public class DropResourcesTest {
    
    @Test
    public void testDropResourcesValidIds() {
        // create are basic objects
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        Player player1 = new Player(1);
        Player player2 = new Player(2);
        Player player3 = new Player(3);

        Player[] players = {player1, player2, player3};

        Controller controller = new Controller(mockedGame, players, gameType);


        // create arguments

        // not the resource array doesn't really matter for this test
        Resource[] toDrop = {Resource.WOOL, Resource.WOOL, Resource.LUMBER, Resource.LUMBER};

        // create map to pass into controller
        HashMap<Integer, Resource[]> playerIdToResouceMap = new HashMap<Integer, Resource[]>();
        playerIdToResouceMap.put(1, toDrop);
        playerIdToResouceMap.put(2, toDrop);
        playerIdToResouceMap.put(3, toDrop);


        // map that should be passed into game
        HashMap<Player, Resource[]> playerToResouceMap = new HashMap<Player, Resource[]>();
        playerToResouceMap.put(player1, toDrop);
        playerToResouceMap.put(player2, toDrop);
        playerToResouceMap.put(player3, toDrop);

        // create expectations
        mockedGame.dropCards(playerToResouceMap);

        // method call
        EasyMock.replay(mockedGame);
        controller.dropResources(playerIdToResouceMap);
        EasyMock.verify(mockedGame);
    }

    @Test
    public void testDropResourcesInvalidIds() {
        // create are basic objects
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        Player player1 = new Player(1);
        Player player2 = new Player(2);
        Player player3 = new Player(3);

        Player[] players = {player1, player2, player3};

        Controller controller = new Controller(mockedGame, players, gameType);


        // create arguments

        // not the resource array doesn't really matter for this test
        Resource[] toDrop = {Resource.WOOL, Resource.WOOL, Resource.LUMBER, Resource.LUMBER};

        // create map to pass into controller
        HashMap<Integer, Resource[]> playerIdToResouceMap = new HashMap<Integer, Resource[]>();
        playerIdToResouceMap.put(1, toDrop);
        playerIdToResouceMap.put(4, toDrop); // invalid Id
        playerIdToResouceMap.put(3, toDrop);

        // create expectations: game should not be called

        // method call: should throw the illegal argument exception
        EasyMock.replay(mockedGame);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.dropResources(playerIdToResouceMap);
        });
        EasyMock.verify(mockedGame);

        String expectedMessage = "Player ID does not exists";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetPlayersHelper() {
        // create are basic objects
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        Player player1 = new Player(1);
        Player player2 = new Player(2);
        Player player3 = new Player(3);

        Player[] expectedPlayers = {player1, player2, player3};

        Controller controller = new Controller(mockedGame, expectedPlayers, gameType);

        Player[] actualPlayers = controller.getPlayerArr();

        assertTrue(Arrays.equals(expectedPlayers, actualPlayers));
    }
}