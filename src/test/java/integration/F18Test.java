package integration;

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
import domain.devcarddeck.DevCard;
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
 * The purpose of this test class is to test feature 18 (F18):
 *      Road Building Development Card: Allow the player to build two roads
 *      for free when they play this card during their turn.
 */
public class F18Test {

    VertexGraph vertexes;
    RoadGraph roads;
    Bank bank;
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

        bank = new Bank();
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
        bank.reset();
    }
    
    @Test
    public void testRoadBuildingAllGood() {
        // directly add the card to the hand.
        // Note this is not the same as buying the card which would not allow us to use the card we bought during the turn
        player1.hand.addDevelopmentCard(DevCard.ROAD);

        // play the card to start us off
        // this will remove the card from the player if the player can play it and
        // also set up the controller for the subsequent clicks
        SuccessCode canPlayCard = controller.useRoadBuildingCard();
        assertEquals(SuccessCode.SUCCESS, canPlayCard);
        // also note the player here starts with the max amount of road pieces, so we shouldn't fail


        // here is the first click which is valid
        int newRoadId1 = 24; // use a valid id
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(newRoadId1)); // click should succeed
        assertEquals(GameState.ROAD_BUILDING_2, controller.getState()); // gameState should move to next road building stage
        assertEquals(12, player1.getNumRoads()); // player should have used a road
        assertEquals(player1, roads.getRoad(newRoadId1).getOwner()); // road should now be owned by the player
        assertEquals(0, player1.hand.getResourceCount()); // player resources should not have changed


        // here is the second click which is valid and connects to the road we just built
        int newRoadId2 = 23; // use a valid id
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(newRoadId2)); // click should succeed
        assertEquals(GameState.DEFAULT, controller.getState()); // we are done with the building so reset to default
        assertEquals(11, player1.getNumRoads()); // player should have used another road
        assertEquals(player1, roads.getRoad(newRoadId2).getOwner()); // road should now be owned by the player
        assertEquals(0, player1.hand.getResourceCount()); // player resources should not have changed
    }

    @Test
    public void testRoadBuildingNoRoadsOnSecond() {
        // directly add the card to the hand.
        // Note this is not the same as buying the card which would not allow us to use the card we bought during the turn
        player1.hand.addDevelopmentCard(DevCard.ROAD);

        // play the card to start us off
        // this will remove the card from the player if the player can play it and
        // also set up the controller for the subsequent clicks
        SuccessCode canPlayCard = controller.useRoadBuildingCard();
        assertEquals(SuccessCode.SUCCESS, canPlayCard);
        // also note the player here starts with the max amount of road pieces, so we shouldn't fail


        // here is the first click which is valid
        int newRoadId1 = 24; // use a valid id
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(newRoadId1)); // click should succeed
        assertEquals(GameState.ROAD_BUILDING_2, controller.getState()); // gameState should move to next road building stage
        assertEquals(12, player1.getNumRoads()); // player should have used a road
        assertEquals(player1, roads.getRoad(newRoadId1).getOwner()); // road should now be owned by the player
        assertEquals(0, player1.hand.getResourceCount()); // player resources should not have changed


        // here we make sure that the player has no roads left
        player1.setNumRoads(0);
        assertEquals(0, player1.getNumRoads());
        

        // here is the second click which is valid and connects to the road we just built
        int newRoadId2 = 23; // use a valid id
        assertEquals(SuccessCode.INSUFFICIENT_RESOURCES, controller.clickedRoad(newRoadId2)); // click should fail because of no roads left
        assertEquals(GameState.DEFAULT, controller.getState()); // ran out of roads, so we exit the flow of logic here
        assertNull(roads.getRoad(newRoadId2).getOwner()); // road should still be unowned
        assertEquals(0, player1.hand.getResourceCount()); // player resources should not have changed
    }

    @Test
    public void testRoadBuildingNoRoadsOnFirst() {
        // directly add the card to the hand.
        // Note this is not the same as buying the card which would not allow us to use the card we bought during the turn
        player1.hand.addDevelopmentCard(DevCard.ROAD);

        // play the card to start us off
        // this will remove the card from the player if the player can play it and
        // also set up the controller for the subsequent clicks
        SuccessCode canPlayCard = controller.useRoadBuildingCard();
        assertEquals(SuccessCode.SUCCESS, canPlayCard);
        // also note the player here starts with the max amount of road pieces, so we shouldn't fail

        // manually set player to have zero roads left
        player1.setNumRoads(0);
        assertEquals(0, player1.getNumRoads());


        // here is the first click which is valid
        int newRoadId1 = 24; // use a valid id
        assertEquals(SuccessCode.INSUFFICIENT_RESOURCES, controller.clickedRoad(newRoadId1)); // click should fai because of lack of roads
        assertEquals(GameState.DEFAULT, controller.getState()); // gameState should reset because flow is killed off
        assertNull(roads.getRoad(newRoadId1).getOwner()); // road should still be unowned
        assertEquals(0, player1.hand.getResourceCount()); // resources should not change
    }

    @Test
    public void testRoadBuildingInvalidOnFirstThenAllGood() {
        // directly add the card to the hand.
        // Note this is not the same as buying the card which would not allow us to use the card we bought during the turn
        player1.hand.addDevelopmentCard(DevCard.ROAD);

        // play the card to start us off
        // this will remove the card from the player if the player can play it and
        // also set up the controller for the subsequent clicks
        SuccessCode canPlayCard = controller.useRoadBuildingCard();
        assertEquals(SuccessCode.SUCCESS, canPlayCard);
        // also note the player here starts with the max amount of road pieces, so we shouldn't fail


        // try clicking a road that is invalid
        int newRoadId1 = 12; // use a valid id
        assertEquals(SuccessCode.INVALID_PLACEMENT, controller.clickedRoad(newRoadId1)); // click should succeed
        assertEquals(GameState.ROAD_BUILDING_1, controller.getState()); // gameState should stay the same
        assertEquals(13, player1.getNumRoads()); // player should still have the same amount of roads
        assertNull(roads.getRoad(newRoadId1).getOwner()); // road should be unowned by the player
        assertEquals(0, player1.hand.getResourceCount()); // player resources should not have changed


        // here is the second first click which is valid
        int newRoadId2 = 24; // use a valid id
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(newRoadId2)); // click should succeed
        assertEquals(GameState.ROAD_BUILDING_2, controller.getState()); // gameState should move to next road building stage
        assertEquals(12, player1.getNumRoads()); // player should have used a road
        assertEquals(player1, roads.getRoad(newRoadId2).getOwner()); // road should now be owned by the player
        assertEquals(0, player1.hand.getResourceCount()); // player resources should not have changed


        // here is the second click which is valid and connects to the road we just built
        int newRoadId3 = 23; // use a valid id
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(newRoadId3)); // click should succeed
        assertEquals(GameState.DEFAULT, controller.getState()); // we are done with the building so reset to default
        assertEquals(11, player1.getNumRoads()); // player should have used another road
        assertEquals(player1, roads.getRoad(newRoadId3).getOwner()); // road should now be owned by the player
        assertEquals(0, player1.hand.getResourceCount()); // player resources should not have changed
    }

    @Test
    public void testRoadBuildingInvalidOnSecondThenAllGood() {
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
        // also note the player here starts with the max amount of road pieces, so we shouldn't fail

        // here is the first click which is valid
        int newRoadId1 = 24; // use a valid id
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(newRoadId1)); // click should succeed
        assertEquals(GameState.ROAD_BUILDING_2, controller.getState()); // gameState should move to next road building stage
        assertEquals(12, player1.getNumRoads()); // player should have used a road
        assertEquals(player1, roads.getRoad(newRoadId1).getOwner()); // road should now be owned by the player
        assertEquals(0, player1.hand.getResourceCount()); // player resources should not have changed


        // try clicking a road that is invalid
        int newRoadId2 = 12; // use a valid id
        assertEquals(SuccessCode.INVALID_PLACEMENT, controller.clickedRoad(newRoadId2)); // click should succeed
        assertEquals(GameState.ROAD_BUILDING_2, controller.getState()); // gameState should stay the same
        assertEquals(12, player1.getNumRoads()); // player should still have the same amount of roads
        assertNull(roads.getRoad(newRoadId2).getOwner()); // road should be unowned by the player
        assertEquals(0, player1.hand.getResourceCount()); // player resources should not have changed


        // here is the second click which is valid and connects to the road we just built
        int newRoadId3 = 23; // use a valid id
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(newRoadId3)); // click should succeed
        assertEquals(GameState.DEFAULT, controller.getState()); // we are done with the building so reset to default
        assertEquals(11, player1.getNumRoads()); // player should have used another road
        assertEquals(player1, roads.getRoad(newRoadId3).getOwner()); // road should now be owned by the player
        assertEquals(0, player1.hand.getResourceCount()); // player resources should not have changed
    }

    @Test
    public void testRoadBuildingNoCard() {
        // note here we don't give the player the card


        // play the card to start us off
        // this will remove the card from the player if the player can play it and
        // also set up the controller for the subsequent clicks
        assertTrue(controller.getDevCardsEnabled());
        SuccessCode canPlayCard = controller.useRoadBuildingCard();
        assertEquals(SuccessCode.CANNOT_PLAY_CARD, canPlayCard);
        assertTrue(controller.getDevCardsEnabled());
        // also note the player here starts with the max amount of road pieces, so we shouldn't
    }
}
