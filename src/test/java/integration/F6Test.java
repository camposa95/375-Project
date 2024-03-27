package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import data.GameLoader;
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

/**
 * The purpose of this test class is to test feature 6 (F6):
 *      Ability for a Player to build settlements, and collect a Victory Point for each,
 *      during their turn. Requires the player to spend a Wood, Brick, Sheep, and Wheat 
 *      card to build each. Settlements must be built on the intersection of multiple 
 *      spaces, and ...
 */
public class F6Test {
    
    @Test
    public void testBuildSettlmentSuccess() {
        // ---------------------- Here are some basic wiring needed that would be done by main ------------------------------
        
        // Here we use begineer game to skip through to the regular gameplay
        GameType gameType = GameType.Beginner;
        VertexGraph vertexes = new VertexGraph(gameType);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        // Players. Note: 3 players is enough for our purposes here
        Player player1 = new Player(1);
        Player player2 = new Player(2);
        Player player3 = new Player(3);
        Player player4 = new Player(4);

        Player[] players = {player1, player2, player3, player4};

        // other things dependent on these things
        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        Game game = new Game(gameBoard, vertexes, roads, devCardDeck);
        
        // Assert that the begineer setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            controllerRef.set(new Controller(game, players, gameType));
        }, "Setup while loop timed out");
        Controller controller = controllerRef.get();

        // -------------------------- Start of Actual Test Stuff ---------------------------
        // Note: we assume everything about setup was correct because it was tested earlier

        // Note: at this point the players would have gotten some starter resources during the 
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the player1's hand and assert that the player has zero resources so we can
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
        Resource[] resourcesForSettlment = {Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN};
        player1.hand.addResources(resourcesForSettlment);

        // set up the controller for the click
        controller.setState(GameState.BUILD_SETTLEMENT);
        // Note controller should already default to currentPlayer == to player1
        // and we should already be in regular play

        // give the player another road so we can follow the distance rule
        roads.getRoad(24).setOwner(player1);


        // here is the actual click
        int newVertexId = 17; // use a valid id
        assertEquals(SuccessCode.SUCCESS, controller.clickedVertex(newVertexId)); // click should succeed
        assertEquals(GameState.DEFAULT, controller.getState()); // gameState should now be revert on succees
        assertEquals(2, player1.getNumSettlements()); // player should have used a settlment
        assertEquals(3, player1.getVictoryPoints()); // player should have gained a victory point
        assertEquals(player1, vertexes.getVertex(newVertexId).getOwner()); // vertex should now be owned by the player
        assertEquals(0, player1.hand.getResourceCardCount()); // player should have used the resources
    }

    @Test
    public void testBuildSettlmentInvalidPlacementDisconnected() {
        // ---------------------- Here are some basic wiring needed that would be done by main ------------------------------
        
        // Here we use begineer game to skip through to the regular gameplay
        GameType gameType = GameType.Beginner;
        VertexGraph vertexes = new VertexGraph(gameType);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        // Players. Note: 3 players is enough for our purposes here
        Player player1 = new Player(1);
        Player player2 = new Player(2);
        Player player3 = new Player(3);
        Player player4 = new Player(4);

        Player[] players = {player1, player2, player3, player4};

        // other things dependent on these things
        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        Game game = new Game(gameBoard, vertexes, roads, devCardDeck);
        
        // Assert that the begineer setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            controllerRef.set(new Controller(game, players, gameType));
        }, "Setup while loop timed out");
        Controller controller = controllerRef.get();

        // -------------------------- Start of Actual Test Stuff ---------------------------
        // Note: we assume everything about setup was correct because it was tested earlier

        // Note: at this point the players would have gotten some starter resources during the 
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the player1's hand and assert that the player has zero resources so we can
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
        Resource[] resourcesForSettlment = {Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN};
        player1.hand.addResources(resourcesForSettlment);

        // set up the controller for the click
        controller.setState(GameState.BUILD_SETTLEMENT);
        // Note controller should already default to currentPlayer == to player1
        // and we should already be in regular play

        // don't give the player this Road so we try to place a disconnected settlment
        // roads.getRoad(24).setOwner(player1);


        // here is the actual click
        int newVertexId = 17; // use the disconnected id
        assertEquals(SuccessCode.INVALID_PLACEMENT, controller.clickedVertex(newVertexId)); // click should succeed
        assertEquals(GameState.BUILD_SETTLEMENT, controller.getState()); // gameState should not change to allow for re-click
        assertEquals(3, player1.getNumSettlements()); // player settlments should be the same
        assertEquals(2, player1.getVictoryPoints()); // player victory points should be the same
        assertEquals(null, vertexes.getVertex(newVertexId).getOwner()); // vertex should still be unowned
        assertEquals(4, player1.hand.getResourceCardCount()); // player should not have used the resources
    }

    @Test
    public void testBuildSettlmentInvalidPlacementTooClose() {
        // ---------------------- Here are some basic wiring needed that would be done by main ------------------------------
        
        // Here we use begineer game to skip through to the regular gameplay
        GameType gameType = GameType.Beginner;
        VertexGraph vertexes = new VertexGraph(gameType);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        // Players. Note: 3 players is enough for our purposes here
        Player player1 = new Player(1);
        Player player2 = new Player(2);
        Player player3 = new Player(3);
        Player player4 = new Player(4);

        Player[] players = {player1, player2, player3, player4};

        // other things dependent on these things
        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        Game game = new Game(gameBoard, vertexes, roads, devCardDeck);
        
        // Assert that the begineer setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            controllerRef.set(new Controller(game, players, gameType));
        }, "Setup while loop timed out");
        Controller controller = controllerRef.get();

        // -------------------------- Start of Actual Test Stuff ---------------------------
        // Note: we assume everything about setup was correct because it was tested earlier

        // Note: at this point the players would have gotten some starter resources during the 
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the player1's hand and assert that the player has zero resources so we can
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
        Resource[] resourcesForSettlment = {Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN};
        player1.hand.addResources(resourcesForSettlment);

        // set up the controller for the click
        controller.setState(GameState.BUILD_SETTLEMENT);
        // Note controller should already default to currentPlayer == to player1
        // and we should already be in regular play

        // here is the actual click
        int newVertexId = 18; // use the and id that is too close to existing road
        assertEquals(SuccessCode.INVALID_PLACEMENT, controller.clickedVertex(newVertexId)); // click should succeed
        assertEquals(GameState.BUILD_SETTLEMENT, controller.getState()); // gameState should not change to allow for re-click
        assertEquals(3, player1.getNumSettlements()); // player settlments should be the same
        assertEquals(2, player1.getVictoryPoints()); // player victory points should be the same
        assertEquals(null, vertexes.getVertex(newVertexId).getOwner()); // vertex should still be unowned
        assertEquals(4, player1.hand.getResourceCardCount()); // player should not have used the resources
    }

    @Test
    public void testBuildSettlmentInvalidPlacementAlreadyOwned() {
        // ---------------------- Here are some basic wiring needed that would be done by main ------------------------------
        
        // Here we use begineer game to skip through to the regular gameplay
        GameType gameType = GameType.Beginner;
        VertexGraph vertexes = new VertexGraph(gameType);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        // Players. Note: 3 players is enough for our purposes here
        Player player1 = new Player(1);
        Player player2 = new Player(2);
        Player player3 = new Player(3);
        Player player4 = new Player(4);

        Player[] players = {player1, player2, player3, player4};

        // other things dependent on these things
        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        Game game = new Game(gameBoard, vertexes, roads, devCardDeck);
        
        // Assert that the begineer setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            controllerRef.set(new Controller(game, players, gameType));
        }, "Setup while loop timed out");
        Controller controller = controllerRef.get();

        // -------------------------- Start of Actual Test Stuff ---------------------------
        // Note: we assume everything about setup was correct because it was tested earlier

        // Note: at this point the players would have gotten some starter resources during the 
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the player1's hand and assert that the player has zero resources so we can
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
        Resource[] resourcesForSettlment = {Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN};
        player1.hand.addResources(resourcesForSettlment);

        // set up the controller for the click
        controller.setState(GameState.BUILD_SETTLEMENT);
        // Note controller should already default to currentPlayer == to player1
        // and we should already be in regular play

        // here is the actual click
        int newVertexId = 13; // use the and id that is already owned by someone 
        assertEquals(SuccessCode.INVALID_PLACEMENT, controller.clickedVertex(newVertexId)); // click should succeed
        assertEquals(GameState.BUILD_SETTLEMENT, controller.getState()); // gameState should not change to allow for re-click
        assertEquals(3, player1.getNumSettlements()); // player settlments should be the same
        assertEquals(2, player1.getVictoryPoints()); // player victory points should be the same
        assertEquals(player2, vertexes.getVertex(newVertexId).getOwner()); // vertex should still be owned by the enemy
        assertEquals(4, player1.hand.getResourceCardCount()); // player should not have used the resources
    }

    @Test
    public void testBuildSettlmentNotEnoughResources() {
        // ---------------------- Here are some basic wiring needed that would be done by main ------------------------------
        
        // Here we use begineer game to skip through to the regular gameplay
        GameType gameType = GameType.Beginner;
        VertexGraph vertexes = new VertexGraph(gameType);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        // Players. Note: 3 players is enough for our purposes here
        Player player1 = new Player(1);
        Player player2 = new Player(2);
        Player player3 = new Player(3);
        Player player4 = new Player(4);

        Player[] players = {player1, player2, player3, player4};

        // other things dependent on these things
        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        Game game = new Game(gameBoard, vertexes, roads, devCardDeck);
        
        // Assert that the begineer setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            controllerRef.set(new Controller(game, players, gameType));
        }, "Setup while loop timed out");
        Controller controller = controllerRef.get();

        // -------------------------- Start of Actual Test Stuff ---------------------------
        // Note: we assume everything about setup was correct because it was tested earlier

        // Note: at this point the players would have gotten some starter resources during the 
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the player1's hand and assert that the player has zero resources so we can
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
    
        // don't add the necessary resources
        // Resource[] resourcesForSettlment = {Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN};
        // player1.hand.addResources(resourcesForSettlment);

        // set up the controller for the click
        controller.setState(GameState.BUILD_SETTLEMENT);
        // Note controller should already default to currentPlayer == to player1
        // and we should already be in regular play

        // give the player another road so we can follow the distance rule
        roads.getRoad(24).setOwner(player1);

        // here is the actual click
        int newVertexId = 17; // use a valid id
        assertEquals(SuccessCode.INSUFFICIENT_RESOURCES, controller.clickedVertex(newVertexId)); // click should succeed
        assertEquals(GameState.BUILD_SETTLEMENT, controller.getState()); // gameState stays the same
        assertEquals(3, player1.getNumSettlements()); // player has same num settlments
        assertEquals(2, player1.getVictoryPoints()); // player has same num victory points
        assertEquals(null, vertexes.getVertex(newVertexId).getOwner()); // vertex should still be unowned
    }
}
