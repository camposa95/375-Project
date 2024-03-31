package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import domain.devcarddeck.DevCard;
import domain.devcarddeck.DevelopmentCardDeck;
import domain.game.Game;
import domain.gameboard.GameBoard;
import domain.game.GameType;
import domain.player.Player;
import domain.bank.Resource;
import domain.graphs.RoadGraph;
import domain.graphs.VertexGraph;

/**
 * The purpose of this test class is to test feature 18 (F18):
 *      Road Building Development Card: Allow the player to build two roads
 *      for free when they play this card during their turn.
 */
public class F18Test {
    
    @Test
    public void testRoadBuildingAllGood() {
        // ---------------------- Here are some basic wiring needed that would be done by main ------------------------------
        
        // Here we use begineer game to skip through to the regular gameplay
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
        // clear out the player1's hand and assert that the player has zero resources because the
        // longest road card doesn't need resources because it is free
        for (Resource resource: Resource.values()) {
            if (resource != Resource.ANY) { // skip this one used for trading
                int count = player1.hand.getResourceCardAmount(resource);
                if (count > 0) {
                    player1.hand.removeResource(resource, count);
                }
            }
        }
        assertEquals(0, player1.hand.getResourceCardCount());

        // directly add the card to the hand.
        // Note this is not the same as buying the card which would not allow us to use the card we bought during the turn
        player1.hand.addDevelopmentCard(DevCard.ROAD);

        // play the card to start us off
        // this will remove the card from the player if the player can play it and
        // also set up the controller for the subsequent clicks
        SuccessCode canPlayCard = controller.useRoadBuildingCard();
        assertEquals(SuccessCode.SUCCESS, canPlayCard);
        // also note the player here starts with the max amount of road pieces so we shouldn't fail


        // here is the first click which is valid
        int newRoadId1 = 24; // use a valid id
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(newRoadId1)); // click should succeed
        assertEquals(GameState.ROAD_BUILDING_2, controller.getState()); // gameState should move to next road building stage
        assertEquals(12, player1.getNumRoads()); // player should have used a road
        assertEquals(player1, roads.getRoad(newRoadId1).getOwner()); // road should now be owned by the player
        assertEquals(0, player1.hand.getResourceCardCount()); // player resources should not have changed


        // here is the second click which is valid and connects to the road we just built
        int newRoadId2 = 23; // use a valid id
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(newRoadId2)); // click should succeed
        assertEquals(GameState.DEFAULT, controller.getState()); // we are done with the building so reset to default
        assertEquals(11, player1.getNumRoads()); // player should have used another road
        assertEquals(player1, roads.getRoad(newRoadId2).getOwner()); // road should now be owned by the player
        assertEquals(0, player1.hand.getResourceCardCount()); // player resources should not have changed
    }

    @Test
    public void testRoadBuildingNoRoadsOnSecond() {
        // ---------------------- Here are some basic wiring needed that would be done by main ------------------------------
        
        // Here we use begineer game to skip through to the regular gameplay
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
        // clear out the player1's hand and assert that the player has zero resources because the
        // longest road card doesn't need resources because it is free
        for (Resource resource: Resource.values()) {
            if (resource != Resource.ANY) { // skip this one used for trading
                int count = player1.hand.getResourceCardAmount(resource);
                if (count > 0) {
                    player1.hand.removeResource(resource, count);
                }
            }
        }
        assertEquals(0, player1.hand.getResourceCardCount());

        // directly add the card to the hand.
        // Note this is not the same as buying the card which would not allow us to use the card we bought during the turn
        player1.hand.addDevelopmentCard(DevCard.ROAD);

        // play the card to start us off
        // this will remove the card from the player if the player can play it and
        // also set up the controller for the subsequent clicks
        SuccessCode canPlayCard = controller.useRoadBuildingCard();
        assertEquals(SuccessCode.SUCCESS, canPlayCard);
        // also note the player here starts with the max amount of road pieces so we shouldn't fail


        // here is the first click which is valid
        int newRoadId1 = 24; // use a valid id
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(newRoadId1)); // click should succeed
        assertEquals(GameState.ROAD_BUILDING_2, controller.getState()); // gameState should move to next road building stage
        assertEquals(12, player1.getNumRoads()); // player should have used a road
        assertEquals(player1, roads.getRoad(newRoadId1).getOwner()); // road should now be owned by the player
        assertEquals(0, player1.hand.getResourceCardCount()); // player resources should not have changed


        // here we make sure that the player has not roads left
        player1.setNumRoads(0);
        assertEquals(0, player1.getNumRoads());
        

        // here is the second click which is valid and connects to the road we just built
        int newRoadId2 = 23; // use a valid id
        assertEquals(SuccessCode.INSUFFICIENT_RESOURCES, controller.clickedRoad(newRoadId2)); // click should fail because of no roads left
        assertEquals(GameState.DEFAULT, controller.getState()); // ran out of roads so we exit the flow of logic here
        assertEquals(null, roads.getRoad(newRoadId2).getOwner()); // road should still be unowned
        assertEquals(0, player1.hand.getResourceCardCount()); // player resources should not have changed
    }

    @Test
    public void testRoadBuildingNoRoadsOnFirst() {
        // ---------------------- Here are some basic wiring needed that would be done by main ------------------------------
        
        // Here we use begineer game to skip through to the regular gameplay
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
        // clear out the player1's hand and assert that the player has zero resources because the
        // longest road card doesn't need resources because it is free
        for (Resource resource: Resource.values()) {
            if (resource != Resource.ANY) { // skip this one used for trading
                int count = player1.hand.getResourceCardAmount(resource);
                if (count > 0) {
                    player1.hand.removeResource(resource, count);
                }
            }
        }
        assertEquals(0, player1.hand.getResourceCardCount());

        // directly add the card to the hand.
        // Note this is not the same as buying the card which would not allow us to use the card we bought during the turn
        player1.hand.addDevelopmentCard(DevCard.ROAD);

        // play the card to start us off
        // this will remove the card from the player if the player can play it and
        // also set up the controller for the subsequent clicks
        SuccessCode canPlayCard = controller.useRoadBuildingCard();
        assertEquals(SuccessCode.SUCCESS, canPlayCard);
        // also note the player here starts with the max amount of road pieces so we shouldn't fail

        // manually set player to have zero roads left
        player1.setNumRoads(0);
        assertEquals(0, player1.getNumRoads());


        // here is the first click which is valid
        int newRoadId1 = 24; // use a valid id
        assertEquals(SuccessCode.INSUFFICIENT_RESOURCES, controller.clickedRoad(newRoadId1)); // click should fai because of lack of raods
        assertEquals(GameState.DEFAULT, controller.getState()); // gameState should reset because flow is killed off
        assertEquals(null, roads.getRoad(newRoadId1).getOwner()); // road should still be unowned
        assertEquals(0, player1.hand.getResourceCardCount()); // resources should not change
    }

    @Test
    public void testRoadBuildingInvalidOnFirstThenAllGood() {
        // ---------------------- Here are some basic wiring needed that would be done by main ------------------------------
        
        // Here we use begineer game to skip through to the regular gameplay
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
        // clear out the player1's hand and assert that the player has zero resources because the
        // longest road card doesn't need resources because it is free
        for (Resource resource: Resource.values()) {
            if (resource != Resource.ANY) { // skip this one used for trading
                int count = player1.hand.getResourceCardAmount(resource);
                if (count > 0) {
                    player1.hand.removeResource(resource, count);
                }
            }
        }
        assertEquals(0, player1.hand.getResourceCardCount());

        // directly add the card to the hand.
        // Note this is not the same as buying the card which would not allow us to use the card we bought during the turn
        player1.hand.addDevelopmentCard(DevCard.ROAD);

        // play the card to start us off
        // this will remove the card from the player if the player can play it and
        // also set up the controller for the subsequent clicks
        SuccessCode canPlayCard = controller.useRoadBuildingCard();
        assertEquals(SuccessCode.SUCCESS, canPlayCard);
        // also note the player here starts with the max amount of road pieces so we shouldn't fail


        // try clicking a road that is invalid
        int newRoadId1 = 12; // use a valid id
        assertEquals(SuccessCode.INVALID_PLACEMENT, controller.clickedRoad(newRoadId1)); // click should succeed
        assertEquals(GameState.ROAD_BUILDING_1, controller.getState()); // gameState should stay the same
        assertEquals(13, player1.getNumRoads()); // player should still have the same amount of raods
        assertEquals(null, roads.getRoad(newRoadId1).getOwner()); // road should be unowned by the player
        assertEquals(0, player1.hand.getResourceCardCount()); // player resources should not have changed


        // here is the second first click which is valid
        int newRoadId2 = 24; // use a valid id
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(newRoadId2)); // click should succeed
        assertEquals(GameState.ROAD_BUILDING_2, controller.getState()); // gameState should move to next road building stage
        assertEquals(12, player1.getNumRoads()); // player should have used a road
        assertEquals(player1, roads.getRoad(newRoadId2).getOwner()); // road should now be owned by the player
        assertEquals(0, player1.hand.getResourceCardCount()); // player resources should not have changed


        // here is the second click which is valid and connects to the road we just built
        int newRoadId3 = 23; // use a valid id
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(newRoadId3)); // click should succeed
        assertEquals(GameState.DEFAULT, controller.getState()); // we are done with the building so reset to default
        assertEquals(11, player1.getNumRoads()); // player should have used another road
        assertEquals(player1, roads.getRoad(newRoadId3).getOwner()); // road should now be owned by the player
        assertEquals(0, player1.hand.getResourceCardCount()); // player resources should not have changed
    }

    @Test
    public void testRoadBuildingInvalidOnSecondThenAllGood() {
        // ---------------------- Here are some basic wiring needed that would be done by main ------------------------------
        
        // Here we use begineer game to skip through to the regular gameplay
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
        // clear out the player1's hand and assert that the player has zero resources because the
        // longest road card doesn't need resources because it is free
        for (Resource resource: Resource.values()) {
            if (resource != Resource.ANY) { // skip this one used for trading
                int count = player1.hand.getResourceCardAmount(resource);
                if (count > 0) {
                    player1.hand.removeResource(resource, count);
                }
            }
        }
        assertEquals(0, player1.hand.getResourceCardCount());

        // directly add the card to the hand.
        // Note this is not the same as buying the card which would not allow us to use the card we bought during the turn
        player1.hand.addDevelopmentCard(DevCard.ROAD);

        // play the card to start us off
        // this will remove the card from the player if the player can play it and
        // also set up the controller for the subsequent clicks
        assertTrue(controller.getDevCardsEnabled());
        SuccessCode canPlayCard = controller.useRoadBuildingCard();
        assertEquals(SuccessCode.SUCCESS, canPlayCard);
        assertFalse(controller.getDevCardsEnabled());
        // also note the player here starts with the max amount of road pieces so we shouldn't fail

        // here is the first click which is valid
        int newRoadId1 = 24; // use a valid id
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(newRoadId1)); // click should succeed
        assertEquals(GameState.ROAD_BUILDING_2, controller.getState()); // gameState should move to next road building stage
        assertEquals(12, player1.getNumRoads()); // player should have used a road
        assertEquals(player1, roads.getRoad(newRoadId1).getOwner()); // road should now be owned by the player
        assertEquals(0, player1.hand.getResourceCardCount()); // player resources should not have changed


        // try clicking a road that is invalid
        int newRoadId2 = 12; // use a valid id
        assertEquals(SuccessCode.INVALID_PLACEMENT, controller.clickedRoad(newRoadId2)); // click should succeed
        assertEquals(GameState.ROAD_BUILDING_2, controller.getState()); // gameState should stay the same
        assertEquals(12, player1.getNumRoads()); // player should still have the same amount of raods
        assertEquals(null, roads.getRoad(newRoadId2).getOwner()); // road should be unowned by the player
        assertEquals(0, player1.hand.getResourceCardCount()); // player resources should not have changed


        // here is the second click which is valid and connects to the road we just built
        int newRoadId3 = 23; // use a valid id
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(newRoadId3)); // click should succeed
        assertEquals(GameState.DEFAULT, controller.getState()); // we are done with the building so reset to default
        assertEquals(11, player1.getNumRoads()); // player should have used another road
        assertEquals(player1, roads.getRoad(newRoadId3).getOwner()); // road should now be owned by the player
        assertEquals(0, player1.hand.getResourceCardCount()); // player resources should not have changed
    }

    @Test
    public void testRoadBuildingNoCard() {
        // ---------------------- Here are some basic wiring needed that would be done by main ------------------------------
        
        // Here we use begineer game to skip through to the regular gameplay
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
        // clear out the player1's hand and assert that the player has zero resources because the
        // longest road card doesn't need resources because it is free
        for (Resource resource: Resource.values()) {
            if (resource != Resource.ANY) { // skip this one used for trading
                int count = player1.hand.getResourceCardAmount(resource);
                if (count > 0) {
                    player1.hand.removeResource(resource, count);
                }
            }
        }
        assertEquals(0, player1.hand.getResourceCardCount());

        // note here we don't give the player the card


        // play the card to start us off
        // this will remove the card from the player if the player can play it and
        // also set up the controller for the subsequent clicks
        assertTrue(controller.getDevCardsEnabled());
        SuccessCode canPlayCard = controller.useRoadBuildingCard();
        assertEquals(SuccessCode.CANNOT_PLAY_CARD, canPlayCard);
        assertTrue(controller.getDevCardsEnabled());
        // also note the player here starts with the max amount of road pieces so we shouldn't 
    }
}
