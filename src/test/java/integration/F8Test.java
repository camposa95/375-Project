package integration;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import data.GameLoader;
import domain.bank.Bank;
import domain.player.HarvestBooster;
import org.junit.jupiter.api.Assertions;
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

import static org.junit.jupiter.api.Assertions.*;


/**
 * The purpose of this test class is to test feature 8 (F8):
 *      Ability for a Player to upgrade a settlement to a city during their turn.
 *      Requires the player to spend three Or and two Wheat to upgrade each.
 */
public class F8Test {
    
    @Test
    public void testBuildSettlementSuccess() {
        // ---------------------- Here are some basic wiring needed that would be done by main ------------------------------
        
        // Here we use beginner game to skip through to the regular gameplay
        GameType gameType = GameType.Beginner;
        VertexGraph vertexes = new VertexGraph(gameType);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        Bank bank = new Bank();
        Player player1 = new Player(1, new HarvestBooster(), bank);
        Player player2 = new Player(2, new HarvestBooster(), bank);
        Player player3 = new Player(3, new HarvestBooster(), bank);
        Player player4 = new Player(4, new HarvestBooster(), bank);
        Player[] players = {player1, player2, player3, player4};

        // other things dependent on these things
        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        Game game = new Game(gameBoard, vertexes, roads, devCardDeck, bank);
        
        // Assert that the beginner setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> controllerRef.set(new Controller(game, players, gameType)), "Setup while loop timed out");
        Controller controller = controllerRef.get();

        // -------------------------- Start of Actual Test Stuff ---------------------------
        // Note: we assume everything about setup was correct because it was tested earlier

        // Note: at this point the players would have gotten some starter resources during the 
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the player1's hand and assert that the player has zero resources, so we can
        // better test on the specific cases.
        for (Resource resource: Resource.values()) {
            if (resource != Resource.ANY) { // skip this one used for trading
                int count = player1.hand.getResourceCardAmount(resource);
                if (count > 0) {
                    player1.hand.removeResource(resource, count);
                }
            }
        }
        assertEquals(0, player1.hand.getResourceCardCount());
    
        // now make sure the player has the enough resources
        Resource[] resourcesForCity = {Resource.ORE, Resource.ORE, Resource.ORE, Resource.GRAIN, Resource.GRAIN};
        player1.hand.addResources(resourcesForCity);

        // set up the controller for the click
        controller.setState(GameState.UPGRADE_SETTLEMENT);
        // Note controller should already default to currentPlayer == to player1,
        // and we should already be in regular play

        // here is the actual click
        int newVertexId = 19; // use a valid id
        assertEquals(SuccessCode.SUCCESS, controller.clickedVertex(newVertexId)); // click should succeed
        assertEquals(GameState.DEFAULT, controller.getState()); // gameState should now be reverted on success
        assertEquals(4, player1.getNumSettlements()); // player should have gained back a settlement
        assertEquals(3, player1.getNumCities()); // player should have used a city
        assertEquals(3, player1.getVictoryPoints()); // player should have gained a victory point
        assertTrue(vertexes.getVertex(newVertexId).getIsCity()); // Vertex is now a city
        assertEquals(0, player1.hand.getResourceCardCount()); // player should have used the resources
    }

    @Test
    public void testBuildSettlementInvalidPlacementNotOwned() {
        // ---------------------- Here are some basic wiring needed that would be done by main ------------------------------
        
        // Here we use beginner game to skip through to the regular gameplay
        GameType gameType = GameType.Beginner;
        VertexGraph vertexes = new VertexGraph(gameType);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        Bank bank = new Bank();
        Player player1 = new Player(1, new HarvestBooster(), bank);
        Player player2 = new Player(2, new HarvestBooster(), bank);
        Player player3 = new Player(3, new HarvestBooster(), bank);
        Player player4 = new Player(4, new HarvestBooster(), bank);
        Player[] players = {player1, player2, player3, player4};

        // other things dependent on these things
        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        Game game = new Game(gameBoard, vertexes, roads, devCardDeck, bank);
        
        // Assert that the beginner setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> controllerRef.set(new Controller(game, players, gameType)), "Setup while loop timed out");
        Controller controller = controllerRef.get();

        // -------------------------- Start of Actual Test Stuff ---------------------------
        // Note: we assume everything about setup was correct because it was tested earlier

        // Note: at this point the players would have gotten some starter resources during the 
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the player1's hand and assert that the player has zero resources, so we can
        // better test on the specific cases.
        for (Resource resource: Resource.values()) {
            if (resource != Resource.ANY) { // skip this one used for trading
                int count = player1.hand.getResourceCardAmount(resource);
                if (count > 0) {
                    player1.hand.removeResource(resource, count);
                }
            }
        }
        assertEquals(0, player1.hand.getResourceCardCount());
    
        // now make sure the player has the enough resources
        Resource[] resourcesForCity = {Resource.ORE, Resource.ORE, Resource.ORE, Resource.GRAIN, Resource.GRAIN};
        player1.hand.addResources(resourcesForCity);

        // set up the controller for the click
        controller.setState(GameState.UPGRADE_SETTLEMENT);
        // Note controller should already default to currentPlayer == to player1,
        // and we should already be in regular play

        // here is the actual click
        int newVertexId = 21; // use an unowned versed it
        assertEquals(SuccessCode.INVALID_PLACEMENT, controller.clickedVertex(newVertexId)); // click should fail
        assertEquals(GameState.UPGRADE_SETTLEMENT, controller.getState()); // gameState should stay the same
        assertEquals(3, player1.getNumSettlements()); // player settlements stay the same
        assertEquals(4, player1.getNumCities()); // player doesn't use a city
        assertEquals(2, player1.getVictoryPoints()); // player should not have gained a victory point
        assertNull(vertexes.getVertex(newVertexId).getOwner()); // Vertex is still unowned
        assertEquals(5, player1.hand.getResourceCardCount()); // player should not have used the resources
    }

    @Test
    public void testBuildSettlementInvalidPlacementEnemyOwned() {
        // ---------------------- Here are some basic wiring needed that would be done by main ------------------------------
        
        // Here we use beginner game to skip through to the regular gameplay
        GameType gameType = GameType.Beginner;
        VertexGraph vertexes = new VertexGraph(gameType);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        Bank bank = new Bank();
        Player player1 = new Player(1, new HarvestBooster(), bank);
        Player player2 = new Player(2, new HarvestBooster(), bank);
        Player player3 = new Player(3, new HarvestBooster(), bank);
        Player player4 = new Player(4, new HarvestBooster(), bank);
        Player[] players = {player1, player2, player3, player4};

        // other things dependent on these things
        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        Game game = new Game(gameBoard, vertexes, roads, devCardDeck, bank);
        
        // Assert that the beginner setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> controllerRef.set(new Controller(game, players, gameType)), "Setup while loop timed out");
        Controller controller = controllerRef.get();

        // -------------------------- Start of Actual Test Stuff ---------------------------
        // Note: we assume everything about setup was correct because it was tested earlier

        // Note: at this point the players would have gotten some starter resources during the 
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the player1's hand and assert that the player has zero resources, so we can
        // better test on the specific cases.
        for (Resource resource: Resource.values()) {
            if (resource != Resource.ANY) { // skip this one used for trading
                int count = player1.hand.getResourceCardAmount(resource);
                if (count > 0) {
                    player1.hand.removeResource(resource, count);
                }
            }
        }
        assertEquals(0, player1.hand.getResourceCardCount());
    
        // now make sure the player has the enough resources
        Resource[] resourcesForCity = {Resource.ORE, Resource.ORE, Resource.ORE, Resource.GRAIN, Resource.GRAIN};
        player1.hand.addResources(resourcesForCity);

        // set up the controller for the click
        controller.setState(GameState.UPGRADE_SETTLEMENT);
        // Note controller should already default to currentPlayer == to player1,
        // and we should already be in regular play

        // here is the actual click
        int newVertexId = 13; // use a vertex owned by player 2
        assertEquals(SuccessCode.INVALID_PLACEMENT, controller.clickedVertex(newVertexId)); // click should fail
        assertEquals(GameState.UPGRADE_SETTLEMENT, controller.getState()); // gameState should stay the same
        assertEquals(3, player1.getNumSettlements()); // player settlements stay the same
        assertEquals(4, player1.getNumCities()); // player doesn't use a city
        assertEquals(2, player1.getVictoryPoints()); // player should not have gained a victory point
        assertEquals(player2, vertexes.getVertex(newVertexId).getOwner()); // Vertex is still owned by player 2
        assertFalse(vertexes.getVertex(newVertexId).getIsCity()); // vertex is still only a settlement
        assertEquals(5, player1.hand.getResourceCardCount()); // player should not have used the resources
    }

    @Test
    public void testBuildSettlementNotEnoughResources() {
        // ---------------------- Here are some basic wiring needed that would be done by main ------------------------------
        
        // Here we use beginner game to skip through to the regular gameplay
        GameType gameType = GameType.Beginner;
        VertexGraph vertexes = new VertexGraph(gameType);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        Bank bank = new Bank();
        Player player1 = new Player(1, new HarvestBooster(), bank);
        Player player2 = new Player(2, new HarvestBooster(), bank);
        Player player3 = new Player(3, new HarvestBooster(), bank);
        Player player4 = new Player(4, new HarvestBooster(), bank);
        Player[] players = {player1, player2, player3, player4};

        // other things dependent on these things
        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        Game game = new Game(gameBoard, vertexes, roads, devCardDeck, bank);
        
        // Assert that the beginner setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> controllerRef.set(new Controller(game, players, gameType)), "Setup while loop timed out");
        Controller controller = controllerRef.get();

        // -------------------------- Start of Actual Test Stuff ---------------------------
        // Note: we assume everything about setup was correct because it was tested earlier

        // Note: at this point the players would have gotten some starter resources during the 
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the player1's hand and assert that the player has zero resources, so we can
        // better test on the specific cases.
        for (Resource resource: Resource.values()) {
            if (resource != Resource.ANY) { // skip this one used for trading
                int count = player1.hand.getResourceCardAmount(resource);
                if (count > 0) {
                    player1.hand.removeResource(resource, count);
                }
            }
        }
        assertEquals(0, player1.hand.getResourceCardCount());
    
        // now make sure the player does not have enough resources
        // Resource[] resourcesForCity = {Resource.ORE, Resource.ORE, Resource.ORE, Resource.GRAIN, Resource.GRAIN};
        // player1.hand.addResources(resourcesForCity);

        // set up the controller for the click
        controller.setState(GameState.UPGRADE_SETTLEMENT);
        // Note controller should already default to currentPlayer == to player1,
        // and we should already be in regular play

        // here is the actual click
        int newVertexId = 19; // use a valid vertex
        assertEquals(SuccessCode.INSUFFICIENT_RESOURCES, controller.clickedVertex(newVertexId)); // click should fail
        assertEquals(GameState.UPGRADE_SETTLEMENT, controller.getState()); // gameState should stay the same
        assertEquals(3, player1.getNumSettlements()); // player settlements stay the same
        assertEquals(4, player1.getNumCities()); // player doesn't use a city
        assertEquals(2, player1.getVictoryPoints()); // player should not have gained a victory point
        assertFalse(vertexes.getVertex(newVertexId).getIsCity()); // vertex is still only a settlement
    }
}
