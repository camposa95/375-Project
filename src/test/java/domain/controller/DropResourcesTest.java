package domain.controller;

import java.util.HashMap;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.game.Game;
import domain.game.GameType;
import domain.player.Player;
import domain.bank.Resource;

import static org.junit.jupiter.api.Assertions.*;

public class DropResourcesTest {

    // create are basic objects
    Game mockedGame;
    Player player1, player2, player3;
    Player[] players;
    Controller controller;

    @BeforeEach
    public void createMocks() {
        mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;

        player1 = new Player(1);
        player2 = new Player(2);
        player3 = new Player(3);

        players = new Player[]{player1, player2, player3};

        controller = new Controller(mockedGame, players, gameType);
    }
    
    @Test
    public void testDropResourcesValidIds() {
        // not the resource array doesn't really matter for this test
        Resource[] toDrop = {Resource.WOOL, Resource.WOOL, Resource.LUMBER, Resource.LUMBER};

        // create map to pass into controller
        HashMap<Integer, Resource[]> playerIdToResourceMap = new HashMap<>();
        playerIdToResourceMap.put(1, toDrop);
        playerIdToResourceMap.put(2, toDrop);
        playerIdToResourceMap.put(3, toDrop);


        // map that should be passed into game
        HashMap<Player, Resource[]> playerToResourceMap = new HashMap<>();
        playerToResourceMap.put(player1, toDrop);
        playerToResourceMap.put(player2, toDrop);
        playerToResourceMap.put(player3, toDrop);

        // create expectations
        mockedGame.dropCards(playerToResourceMap);

        // method call
        EasyMock.replay(mockedGame);
        controller.dropResources(playerIdToResourceMap);
        EasyMock.verify(mockedGame);
    }

    @Test
    public void testDropResourcesInvalidIds() {
        // create arguments

        // not the resource array doesn't really matter for this test
        Resource[] toDrop = {Resource.WOOL, Resource.WOOL, Resource.LUMBER, Resource.LUMBER};

        // create map to pass into controller
        HashMap<Integer, Resource[]> playerIdToResourceMap = new HashMap<>();
        playerIdToResourceMap.put(1, toDrop);
        playerIdToResourceMap.put(4, toDrop); // invalid Id
        playerIdToResourceMap.put(3, toDrop);

        // create expectations: game should not be called

        // method call: should throw the illegal argument exception
        EasyMock.replay(mockedGame);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> controller.dropResources(playerIdToResourceMap));
        EasyMock.verify(mockedGame);

        String expectedMessage = "Player ID does not exists";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetPlayersHelper() {
        Player[] actualPlayers = controller.getPlayerArr();

        assertArrayEquals(players, actualPlayers);
    }
}