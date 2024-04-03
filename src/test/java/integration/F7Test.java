package integration;

import static domain.bank.Resource.BRICK;
import static domain.bank.Resource.LUMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import data.GameLoader;
import domain.bank.Bank;
import domain.player.HarvestBooster;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.controller.Controller;
import domain.controller.GameState;
import domain.controller.SuccessCode;
import domain.devcarddeck.DevelopmentCardDeck;
import domain.game.Game;
import domain.gameboard.GameBoard;
import domain.game.GameType;
import domain.player.Player;
import domain.bank.Resource;
import domain.graphs.RoadGraph;
import domain.graphs.VertexGraph;


/**
 * The purpose of this test class is to test feature 7 (F7):
 *      Ability for a Player to build roads, stemming from their existing roads,
 *      during their turn. Requires the player to spend a Wood and Brick card to
 *      build each. Roads must be built on connecting edges between spaces
 *      (or an edge with the sea).
 */
public class F7Test {

    VertexGraph vertexes;
    RoadGraph roads;
    Player player1;
    Player player2;
    Player player3;
    Player player4;
    Player[] players;
    Controller controller;

    @BeforeEach
    public void createGameObjects() {
        GameType gameType = GameType.Beginner;
        vertexes = new VertexGraph(gameType);
        roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        Bank bank = new Bank();
        player1 = new Player(1, new HarvestBooster(), bank);
        player2 = new Player(2, new HarvestBooster(), bank);
        player3 = new Player(3, new HarvestBooster(), bank);
        player4 = new Player(4, new HarvestBooster(), bank);
        players = new Player[]{player1, player2, player3, player4};

        // other things dependent on these things
        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        Game game = new Game(gameBoard, vertexes, roads, devCardDeck, bank);

        // Assert that the beginner setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> controllerRef.set(new Controller(game, players, gameType)), "Setup while loop timed out");
        controller = controllerRef.get();

        for (Player p: players) {
            p.hand.clearResources();
        }
    }
    
    @Test
    public void testBuildRoadSuccess() {
        // now make sure the player has the enough resources
        Resource[] resourcesForRoad = {BRICK, LUMBER};
        player1.hand.addResources(resourcesForRoad);

        // set up the controller for the click
        controller.setState(GameState.BUILD_ROAD);
        // Note controller should already default to currentPlayer == to player1,
        // and we should already be in regular play

        // here is the actual click
        int newRoadId = 26; // use a valid id
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(newRoadId)); // click should succeed
        assertEquals(GameState.DEFAULT, controller.getState()); // gameState should now be reverted on success
        assertEquals(12, player1.getNumRoads()); // player should have used a road
        assertEquals(player1, roads.getRoad(newRoadId).getOwner()); // road should now be owned by the player
        assertEquals(0, player1.hand.getResourceCount()); // player should have used the resources
    }

    @Test
    public void testBuildRoadInvalidPlacement() {
        // now make sure the player has the enough resources
        Resource[] resourcesForRoad = {BRICK, LUMBER};
        player1.hand.addResources(resourcesForRoad);

        // set up the controller for the click
        controller.setState(GameState.BUILD_ROAD);
        // Note controller should already default to currentPlayer == to player1,
        // and we should already be in regular play

        // here is the actual click
        int newRoadId = 27; // use an invalid id
        assertEquals(SuccessCode.INVALID_PLACEMENT, controller.clickedRoad(newRoadId)); // click should succeed
        assertEquals(GameState.BUILD_ROAD, controller.getState()); // gameState should now be reverted on success
        assertEquals(13, player1.getNumRoads()); // player should have used a road
        assertNull(roads.getRoad(newRoadId).getOwner()); // road should be unowned
        assertEquals(2, player1.hand.getResourceCount()); // player should not have used the resources
    }

    @Test
    public void testBuildRoadNotEnoughResources() {
        // don't give resources to the player
        // Resource[] resourcesForRoad = {Resource.BRICK, Resource.LUMBER};
        // player1.hand.addResources(resourcesForRoad);

        // set up the controller for the click
        controller.setState(GameState.BUILD_ROAD);
        // Note controller should already default to currentPlayer == to player1,
        // and we should already be in regular play

        // here is the actual click
        int newRoadId = 26; // use a valid id
        assertEquals(SuccessCode.INSUFFICIENT_RESOURCES, controller.clickedRoad(newRoadId)); // click should succeed
        assertEquals(GameState.BUILD_ROAD, controller.getState()); // gameState should now be reverted on success
        assertEquals(13, player1.getNumRoads()); // player should have used a road
        assertNull(roads.getRoad(newRoadId).getOwner()); // road should be unowned
    }
}
