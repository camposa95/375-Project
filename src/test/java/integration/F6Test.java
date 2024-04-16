package integration;

import static domain.bank.Resource.*;
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
import domain.graphs.GameboardGraph;

/**
 * The purpose of this test class is to test feature 6 (F6):
 *      Ability for a Player to build settlements, and collect a Victory Point for each,
 *      during their turn. Requires the player to spend a Wood, Brick, Sheep, and Wheat 
 *      card to build each. Settlements must be built on the intersection of multiple 
 *      spaces, and ...
 */
public class F6Test {

    GameboardGraph gameboardGraph;
    Player player1;
    Player player2;
    Player player3;
    Player player4;
    Player[] players;
    Controller controller;

    @BeforeEach
    public void createGameObjects() {
        GameType gameType = GameType.Beginner;
        gameboardGraph = new GameboardGraph(gameType);
        GameLoader.initializeGraphs(gameboardGraph);

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
        Game game = new Game(gameBoard, gameboardGraph, devCardDeck, bank);

        // Assert that the beginner setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> controllerRef.set(new Controller(game, players, gameType)), "Setup while loop timed out");
        controller = controllerRef.get();

        for (Player p: players) {
            p.hand.clearResources();
        }
    }
    
    @Test
    public void testBuildSettlementSuccess() {
        // now make sure the player has the enough resources
        Resource[] resourcesForSettlement = {BRICK, LUMBER, WOOL, GRAIN};
        player1.hand.addResources(resourcesForSettlement);

        // set up the controller for the click
        controller.setState(GameState.BUILD_SETTLEMENT);


        // give the player another road, so we can follow the distance rule
        gameboardGraph.getRoad(24).setOwner(player1);


        // here is the actual click
        int newVertexId = 17; // use a valid id
        assertEquals(SuccessCode.SUCCESS, controller.clickedVertex(newVertexId)); // click should succeed
        assertEquals(GameState.DEFAULT, controller.getState()); // gameState should now be reverted on success
        assertEquals(2, player1.getNumSettlements()); // player should have used a settlement
        assertEquals(3, player1.getVictoryPoints()); // player should have gained a victory point
        assertEquals(player1, gameboardGraph.getVertex(newVertexId).getOwner()); // vertex should now be owned by the player
        assertEquals(0, player1.hand.getResourceCount()); // player should have used the resources
    }

    @Test
    public void testBuildSettlementInvalidPlacementDisconnected() {
        // now make sure the player has the enough resources
        Resource[] resourcesForSettlement = {BRICK, LUMBER, WOOL, GRAIN};
        player1.hand.addResources(resourcesForSettlement);

        // set up the controller for the click
        controller.setState(GameState.BUILD_SETTLEMENT);


        // don't give the player this Road, so we try to place a disconnected settlement
        // roads.getRoad(24).setOwner(player1);


        // here is the actual click
        int newVertexId = 17; // use the disconnected id
        assertEquals(SuccessCode.INVALID_PLACEMENT, controller.clickedVertex(newVertexId));
        assertEquals(GameState.BUILD_SETTLEMENT, controller.getState()); // gameState should not change to allow for re-click
        assertEquals(3, player1.getNumSettlements()); // player settlements should be the same
        assertEquals(2, player1.getVictoryPoints()); // player victory points should be the same
        assertNull(gameboardGraph.getVertex(newVertexId).getOwner()); // vertex should still be unowned
        assertEquals(4, player1.hand.getResourceCount()); // player should not have used the resources
    }

    @Test
    public void testBuildSettlementInvalidPlacementTooClose() {
        // now make sure the player has the enough resources
        Resource[] resourcesForSettlement = {BRICK, LUMBER, WOOL, GRAIN};
        player1.hand.addResources(resourcesForSettlement);


        controller.setState(GameState.BUILD_SETTLEMENT);


        // here is the actual click
        int newVertexId = 18; // use the and id that is too close to existing road
        assertEquals(SuccessCode.INVALID_PLACEMENT, controller.clickedVertex(newVertexId)); // click should succeed
        assertEquals(GameState.BUILD_SETTLEMENT, controller.getState()); // gameState should not change to allow for re-click
        assertEquals(3, player1.getNumSettlements()); // player settlements should be the same
        assertEquals(2, player1.getVictoryPoints()); // player victory points should be the same
        assertNull(gameboardGraph.getVertex(newVertexId).getOwner()); // vertex should still be unowned
        assertEquals(4, player1.hand.getResourceCount()); // player should not have used the resources
    }

    @Test
    public void testBuildSettlementInvalidPlacementAlreadyOwned() {
        // now make sure the player has the enough resources
        Resource[] resourcesForSettlement = {BRICK, LUMBER, WOOL, GRAIN};
        player1.hand.addResources(resourcesForSettlement);


        controller.setState(GameState.BUILD_SETTLEMENT);

        // here is the actual click
        int newVertexId = 13; // use the and id that is already owned by someone 
        assertEquals(SuccessCode.INVALID_PLACEMENT, controller.clickedVertex(newVertexId)); // click should succeed
        assertEquals(GameState.BUILD_SETTLEMENT, controller.getState()); // gameState should not change to allow for re-click
        assertEquals(3, player1.getNumSettlements()); // player settlements should be the same
        assertEquals(2, player1.getVictoryPoints()); // player victory points should be the same
        assertEquals(player2, gameboardGraph.getVertex(newVertexId).getOwner()); // vertex should still be owned by the enemy
        assertEquals(4, player1.hand.getResourceCount()); // player should not have used the resources
    }

    @Test
    public void testBuildSettlementNotEnoughResources() {
        controller.setState(GameState.BUILD_SETTLEMENT);

        // give the player another road, so we can follow the distance rule
        gameboardGraph.getRoad(24).setOwner(player1);

        // here is the actual click
        int newVertexId = 17; // use a valid id
        assertEquals(SuccessCode.INSUFFICIENT_RESOURCES, controller.clickedVertex(newVertexId)); // click should succeed
        assertEquals(GameState.BUILD_SETTLEMENT, controller.getState()); // gameState stays the same
        assertEquals(3, player1.getNumSettlements()); // player has same num settlements
        assertEquals(2, player1.getVictoryPoints()); // player has same num victory points
        assertNull(gameboardGraph.getVertex(newVertexId).getOwner()); // vertex should still be unowned
    }
}
