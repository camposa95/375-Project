package domain.controller;

import domain.bank.Resource;
import domain.controller.Controller;
import domain.game.Game;
import domain.game.GameType;
import domain.player.BoostType;
import domain.player.HarvestBooster;
import domain.player.Player;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateWeatherEventTest {

    @Test
    public void test_createWeatherEventSpread(){
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        Player player = new Player(1);

        GameType gameType = GameType.Advanced;
        Controller controller = new Controller(mockedGame, new Player[] {player}, gameType);

        // Maps to track what weather event types were generated
        HashMap<Resource, Boolean> resourceResults = new HashMap<>();
        resourceResults.put(Resource.BRICK, false);
        resourceResults.put(Resource.LUMBER, false);
        resourceResults.put(Resource.ORE, false);
        resourceResults.put(Resource.GRAIN, false);
        resourceResults.put(Resource.WOOL, false);

        HashMap<BoostType, Boolean> boostTypeResults = new HashMap<>();
        boostTypeResults.put(BoostType.ZERO, false);
        boostTypeResults.put(BoostType.DOUBLE, false);

        HashMap<Boolean, Boolean> forEveryoneResults = new HashMap<>();
        forEveryoneResults.put(false, false);
        forEveryoneResults.put(true, false);


        for(int i = 0; i < 300; i++) { // try generated a lot fo times
            Controller.WeatherEvent weatherEvent = controller.createWeatherEvent();

            // make sure no bad weather events are generated
            assertNotSame(weatherEvent.resource(), Resource.ANY);
            assertNotSame(weatherEvent.boostType(), BoostType.NONE);

            // mark the types as generated
            resourceResults.put(weatherEvent.resource(), true);
            boostTypeResults.put(weatherEvent.boostType(), true);
            forEveryoneResults.put(weatherEvent.forEveryone(), true);
        }

        // now check the make to make sure every weather event type was generated
        checkResults(resourceResults);
        checkResults(boostTypeResults);
        checkResults(forEveryoneResults);
    }

    private void checkResults(Map<?, Boolean> results) {
        for (Map.Entry<?, Boolean> result: results.entrySet()) {
            assertTrue(result.getValue(), result.getKey() + " not generated");
        }
    }

    @Test
    public void test_createWeatherEventForEveryone() {
        // Create the mocks and basic objects
        HarvestBooster mockedPlayer1HarvestBooster = EasyMock.createStrictMock(HarvestBooster.class);
        HarvestBooster mockedPlayer2HarvestBooster = EasyMock.createStrictMock(HarvestBooster.class);
        HarvestBooster mockedPlayer3HarvestBooster = EasyMock.createStrictMock(HarvestBooster.class);
        HarvestBooster mockedPlayer4HarvestBooster = EasyMock.createStrictMock(HarvestBooster.class);
        Player player1 = new Player(1, mockedPlayer1HarvestBooster);
        Player player2 = new Player(2, mockedPlayer2HarvestBooster);
        Player player3 = new Player(3, mockedPlayer3HarvestBooster);
        Player player4 = new Player(4, mockedPlayer4HarvestBooster);
        Player[] players = new Player[] {player1, player2, player3, player4};

        GameType gameType = GameType.Advanced;
        Game mockedGame = EasyMock.createStrictMock(Game.class); // note we don't assert on this, it is simply required for the controller constructor
        Random mockedRandom = EasyMock.createStrictMock(Random.class);

        Controller controller = new Controller(mockedGame, players, gameType, mockedRandom);

        // Mocked calls expectations
        // Controlled random generation
        EasyMock.expect(mockedRandom.nextInt(5)).andReturn(0); // Call for generating the Brick resource
        EasyMock.expect(mockedRandom.nextInt(2)).andReturn(0); // Call for generating the DISABLE boost type
        EasyMock.expect(mockedRandom.nextBoolean()).andReturn(true); // Call to generate the "true" flag to apply weather event to everyone

        // Make sure we apply the boost to all the players
        for (Player p: players) {
            p.harvestBooster.setBoost(Resource.BRICK, BoostType.ZERO);
            EasyMock.expectLastCall().once();
        }

        // Method call
        EasyMock.replay(mockedRandom, mockedPlayer1HarvestBooster, mockedPlayer2HarvestBooster, mockedPlayer3HarvestBooster, mockedPlayer4HarvestBooster);
        controller.createWeatherEvent();
        EasyMock.verify(mockedRandom, mockedPlayer1HarvestBooster, mockedPlayer2HarvestBooster, mockedPlayer3HarvestBooster, mockedPlayer4HarvestBooster);
    }

    @Test
    public void test_createWeatherEventOnlyPlayer() {
        // Create the mocks and basic objects
        HarvestBooster mockedPlayer1HarvestBooster = EasyMock.createStrictMock(HarvestBooster.class);
        HarvestBooster mockedPlayer2HarvestBooster = EasyMock.createStrictMock(HarvestBooster.class);
        HarvestBooster mockedPlayer3HarvestBooster = EasyMock.createStrictMock(HarvestBooster.class);
        HarvestBooster mockedPlayer4HarvestBooster = EasyMock.createStrictMock(HarvestBooster.class);
        Player player1 = new Player(1, mockedPlayer1HarvestBooster);
        Player player2 = new Player(2, mockedPlayer2HarvestBooster);
        Player player3 = new Player(3, mockedPlayer3HarvestBooster);
        Player player4 = new Player(4, mockedPlayer4HarvestBooster);
        Player[] players = new Player[] {player1, player2, player3, player4};

        GameType gameType = GameType.Advanced;
        Game mockedGame = EasyMock.createStrictMock(Game.class); // note we don't assert on this, it is simply required for the controller constructor
        Random mockedRandom = EasyMock.createStrictMock(Random.class);

        Controller controller = new Controller(mockedGame, players, gameType, mockedRandom);
        controller.setCurrentPlayer(player2); // chosen arbitrarily

        // Mocked calls expectations
        // Controlled random generation
        EasyMock.expect(mockedRandom.nextInt(5)).andReturn(0); // Call for generating the Brick resource
        EasyMock.expect(mockedRandom.nextInt(2)).andReturn(0); // Call for generating the DISABLE boost type
        EasyMock.expect(mockedRandom.nextBoolean()).andReturn(false); // Call to generate the "false" flag to apply weather event to only the player who rolled

        // Make sure we apply the boost to only player2
        player2.harvestBooster.setBoost(Resource.BRICK, BoostType.ZERO);
        EasyMock.expectLastCall().once();

        // Method call
        EasyMock.replay(mockedRandom, mockedPlayer1HarvestBooster, mockedPlayer2HarvestBooster, mockedPlayer3HarvestBooster, mockedPlayer4HarvestBooster);
        controller.createWeatherEvent();
        EasyMock.verify(mockedRandom, mockedPlayer1HarvestBooster, mockedPlayer2HarvestBooster, mockedPlayer3HarvestBooster, mockedPlayer4HarvestBooster);
    }
}
