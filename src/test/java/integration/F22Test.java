package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import data.GameLoader;
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
 * The purpose of this test class is to test feature 22 (F22):
 *      Ability for a Player to collect 2 Victory Points for having the Largest Army,
 *      larger than 3 Knight cards. Another player can steal these points if they
 *      accrue a larger army.
 */
public class F22Test {
    private static final int POINTS_FROM_SETUP = 2;
    private static final int POINTS_FOR_LARGEST_ARMY = 2;
    private static final Resource[] resourcesForDevCard = {Resource.ORE, Resource.WOOL, Resource.GRAIN};

    private void loopToBeginging(final Controller controller) {
        for (int i = 0; i < 4; i++) {
            controller.setState(GameState.DEFAULT);
            assertEquals(SuccessCode.SUCCESS, controller.endTurn());
        }
    }

    @Test
    public void testLargestArmyNoOneHasIt() {
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

        // Note: we assume everything about setup was correct because it was tested earlier

        // Note: at this point the players would have gotten some starter resources during the 
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the player1's hand and assert that the player has zero resources so we can
        // better test on the specific cases.
        for (Player player: players) {
            for (Resource resource: Resource.values()) {
                if (resource != Resource.ANY) { // skip this one used for trading
                    int count = player.hand.getResourceCardAmount(resource);
                    if (count > 0) {
                        player.hand.removeResource(resource, count);
                    }
                }
            }
            assertEquals(0, player.hand.getResourceCardCount());
        }

        // -------------------------- Start of Actual Test Stuff ---------------------------

        assertFalse(player1.hasLargestArmy());
        assertFalse(player2.hasLargestArmy());
        assertFalse(player3.hasLargestArmy());
        assertFalse(player4.hasLargestArmy());

        assertEquals(POINTS_FROM_SETUP, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());
    }

    @Test
    public void testLargestArmyClaimedFirst() {
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

        // Note: we assume everything about setup was correct because it was tested earlier

        // Note: at this point the players would have gotten some starter resources during the 
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the player1's hand and assert that the player has zero resources so we can
        // better test on the specific cases.
        for (Player player: players) {
            for (Resource resource: Resource.values()) {
                if (resource != Resource.ANY) { // skip this one used for trading
                    int count = player.hand.getResourceCardAmount(resource);
                    if (count > 0) {
                        player.hand.removeResource(resource, count);
                    }
                }
            }
            assertEquals(0, player.hand.getResourceCardCount());
        }

        // -------------------------- Start of Actual Test Stuff ---------------------------

        // at the begining of the game no one has it
        assertFalse(player1.hasLargestArmy());
        assertFalse(player2.hasLargestArmy());
        assertFalse(player3.hasLargestArmy());
        assertFalse(player4.hasLargestArmy());

        assertEquals(POINTS_FROM_SETUP, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());

        // Make sure we are on player1
        assertEquals(player1, controller.getCurrentPlayer());

        // give the player some resources
        player1.hand.addResources(resourcesForDevCard);
        player1.hand.addResources(resourcesForDevCard);
        player1.hand.addResources(resourcesForDevCard);

        // player buys three cards
        assertTrue(player1.purchaseDevCard(DevCard.KNIGHT));
        assertTrue(player1.purchaseDevCard(DevCard.KNIGHT));
        assertTrue(player1.purchaseDevCard(DevCard.KNIGHT));

        // can't use bought card on same turn, so end turn until we are back at player 1
        loopToBeginging(controller);
        assertEquals(player1, controller.getCurrentPlayer());
        assertEquals(SuccessCode.SUCCESS, controller.playKnightCard());
        
        // can only use one card per turn so loop back to begining
        loopToBeginging(controller);
        assertEquals(player1, controller.getCurrentPlayer());
        assertEquals(SuccessCode.SUCCESS, controller.playKnightCard());

        // can only use one card per turn so loop back to begining
        loopToBeginging(controller);
        assertEquals(player1, controller.getCurrentPlayer());
        assertEquals(SuccessCode.SUCCESS, controller.playKnightCard());


        // player should now have the largest army card
        assertEquals(3, player1.getNumKnightsPlayed());
        assertEquals(0, player2.getNumKnightsPlayed());
        assertEquals(0, player3.getNumKnightsPlayed());
        assertEquals(0, player4.getNumKnightsPlayed());

        assertTrue(player1.hasLargestArmy());
        assertFalse(player2.hasLargestArmy());
        assertFalse(player3.hasLargestArmy());
        assertFalse(player4.hasLargestArmy());

        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_LARGEST_ARMY, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());
    }

    @Test
    public void testLargestArmyTied() {
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

        // Note: we assume everything about setup was correct because it was tested earlier

        // Note: at this point the players would have gotten some starter resources during the 
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the player1's hand and assert that the player has zero resources so we can
        // better test on the specific cases.
        for (Player player: players) {
            for (Resource resource: Resource.values()) {
                if (resource != Resource.ANY) { // skip this one used for trading
                    int count = player.hand.getResourceCardAmount(resource);
                    if (count > 0) {
                        player.hand.removeResource(resource, count);
                    }
                }
            }
            assertEquals(0, player.hand.getResourceCardCount());
        }

        // -------------------------- Start of Actual Test Stuff ---------------------------

        // at the begining of the game no one has it
        assertFalse(player1.hasLargestArmy());
        assertFalse(player2.hasLargestArmy());
        assertFalse(player3.hasLargestArmy());
        assertFalse(player4.hasLargestArmy());

        assertEquals(POINTS_FROM_SETUP, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());

        // Make sure we are on player1
        assertEquals(player1, controller.getCurrentPlayer());

        // give the player some resources
        player1.hand.addResources(resourcesForDevCard);
        player1.hand.addResources(resourcesForDevCard);
        player1.hand.addResources(resourcesForDevCard);

        // player buys three cards
        assertTrue(player1.purchaseDevCard(DevCard.KNIGHT));
        assertTrue(player1.purchaseDevCard(DevCard.KNIGHT));
        assertTrue(player1.purchaseDevCard(DevCard.KNIGHT));

        // can't use bought card on same turn, so end turn until we are back at player 1
        loopToBeginging(controller);
        assertEquals(player1, controller.getCurrentPlayer());
        assertEquals(SuccessCode.SUCCESS, controller.playKnightCard());
        
        // can only use one card per turn so loop back to begining
        loopToBeginging(controller);
        assertEquals(player1, controller.getCurrentPlayer());
        assertEquals(SuccessCode.SUCCESS, controller.playKnightCard());

        // can only use one card per turn so loop back to begining
        loopToBeginging(controller);
        assertEquals(player1, controller.getCurrentPlayer());
        assertEquals(SuccessCode.SUCCESS, controller.playKnightCard());


        // player should now have the largest army card
        assertEquals(3, player1.getNumKnightsPlayed());
        assertEquals(0, player2.getNumKnightsPlayed());
        assertEquals(0, player3.getNumKnightsPlayed());
        assertEquals(0, player4.getNumKnightsPlayed());

        assertTrue(player1.hasLargestArmy());
        assertFalse(player2.hasLargestArmy());
        assertFalse(player3.hasLargestArmy());
        assertFalse(player4.hasLargestArmy());

        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_LARGEST_ARMY, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());


        // ------------- player 2 ties player 1 for knight ----------------------
        // Make sure we are on player1
        controller.setState(GameState.DEFAULT);
        controller.endTurn();
        assertEquals(player2, controller.getCurrentPlayer());

        // give the player some resources
        player2.hand.addResources(resourcesForDevCard);
        player2.hand.addResources(resourcesForDevCard);
        player2.hand.addResources(resourcesForDevCard);

        // player buys three cards
        assertTrue(player2.purchaseDevCard(DevCard.KNIGHT));
        assertTrue(player2.purchaseDevCard(DevCard.KNIGHT));
        assertTrue(player2.purchaseDevCard(DevCard.KNIGHT));

        // can't use bought card on same turn, so end turn until we are back at player 1
        loopToBeginging(controller);
        assertEquals(player2, controller.getCurrentPlayer());
        assertEquals(SuccessCode.SUCCESS, controller.playKnightCard());
        
        // can only use one card per turn so loop back to begining
        loopToBeginging(controller);
        assertEquals(player2, controller.getCurrentPlayer());
        assertEquals(SuccessCode.SUCCESS, controller.playKnightCard());

        // can only use one card per turn so loop back to begining
        loopToBeginging(controller);
        assertEquals(player2, controller.getCurrentPlayer());
        assertEquals(SuccessCode.SUCCESS, controller.playKnightCard());


        // player should keep the largest army card
        assertEquals(3, player1.getNumKnightsPlayed());
        assertEquals(3, player2.getNumKnightsPlayed());
        assertEquals(0, player3.getNumKnightsPlayed());
        assertEquals(0, player4.getNumKnightsPlayed());

        assertTrue(player1.hasLargestArmy());
        assertFalse(player2.hasLargestArmy());
        assertFalse(player3.hasLargestArmy());
        assertFalse(player4.hasLargestArmy());

        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_LARGEST_ARMY, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());
    }

    @Test
    public void testLargestArmyOvertaken() {
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

        // Note: we assume everything about setup was correct because it was tested earlier

        // Note: at this point the players would have gotten some starter resources during the 
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the player1's hand and assert that the player has zero resources so we can
        // better test on the specific cases.
        for (Player player: players) {
            for (Resource resource: Resource.values()) {
                if (resource != Resource.ANY) { // skip this one used for trading
                    int count = player.hand.getResourceCardAmount(resource);
                    if (count > 0) {
                        player.hand.removeResource(resource, count);
                    }
                }
            }
            assertEquals(0, player.hand.getResourceCardCount());
        }

        // -------------------------- Start of Actual Test Stuff ---------------------------

        // at the begining of the game no one has it
        assertFalse(player1.hasLargestArmy());
        assertFalse(player2.hasLargestArmy());
        assertFalse(player3.hasLargestArmy());
        assertFalse(player4.hasLargestArmy());

        assertEquals(POINTS_FROM_SETUP, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());

        // Make sure we are on player1
        assertEquals(player1, controller.getCurrentPlayer());

        // give the player some resources
        player1.hand.addResources(resourcesForDevCard);
        player1.hand.addResources(resourcesForDevCard);
        player1.hand.addResources(resourcesForDevCard);

        // player buys three cards
        assertTrue(player1.purchaseDevCard(DevCard.KNIGHT));
        assertTrue(player1.purchaseDevCard(DevCard.KNIGHT));
        assertTrue(player1.purchaseDevCard(DevCard.KNIGHT));

        // can't use bought card on same turn, so end turn until we are back at player 1
        loopToBeginging(controller);
        assertEquals(player1, controller.getCurrentPlayer());
        assertEquals(SuccessCode.SUCCESS, controller.playKnightCard());
        
        // can only use one card per turn so loop back to begining
        loopToBeginging(controller);
        assertEquals(player1, controller.getCurrentPlayer());
        assertEquals(SuccessCode.SUCCESS, controller.playKnightCard());

        // can only use one card per turn so loop back to begining
        loopToBeginging(controller);
        assertEquals(player1, controller.getCurrentPlayer());
        assertEquals(SuccessCode.SUCCESS, controller.playKnightCard());


        // player should now have the largest army card
        assertEquals(3, player1.getNumKnightsPlayed());
        assertEquals(0, player2.getNumKnightsPlayed());
        assertEquals(0, player3.getNumKnightsPlayed());
        assertEquals(0, player4.getNumKnightsPlayed());

        assertTrue(player1.hasLargestArmy());
        assertFalse(player2.hasLargestArmy());
        assertFalse(player3.hasLargestArmy());
        assertFalse(player4.hasLargestArmy());

        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_LARGEST_ARMY, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());


        // ------------- player 2 overtakes player 1 ----------------------
        // Make sure we are on player1
        controller.setState(GameState.DEFAULT);
        controller.endTurn();
        assertEquals(player2, controller.getCurrentPlayer());

        // give the player some resources
        player2.hand.addResources(resourcesForDevCard);
        player2.hand.addResources(resourcesForDevCard);
        player2.hand.addResources(resourcesForDevCard);
        player2.hand.addResources(resourcesForDevCard);

        // player buys three cards
        assertTrue(player2.purchaseDevCard(DevCard.KNIGHT));
        assertTrue(player2.purchaseDevCard(DevCard.KNIGHT));
        assertTrue(player2.purchaseDevCard(DevCard.KNIGHT));
        assertTrue(player2.purchaseDevCard(DevCard.KNIGHT));

        // can't use bought card on same turn, so end turn until we are back at player 1
        loopToBeginging(controller);
        assertEquals(player2, controller.getCurrentPlayer());
        assertEquals(SuccessCode.SUCCESS, controller.playKnightCard());
        
        // can only use one card per turn so loop back to begining
        loopToBeginging(controller);
        assertEquals(player2, controller.getCurrentPlayer());
        assertEquals(SuccessCode.SUCCESS, controller.playKnightCard());

        // can only use one card per turn so loop back to begining
        loopToBeginging(controller);
        assertEquals(player2, controller.getCurrentPlayer());
        assertEquals(SuccessCode.SUCCESS, controller.playKnightCard());

        // can only use one card per turn so loop back to begining
        loopToBeginging(controller);
        assertEquals(player2, controller.getCurrentPlayer());
        assertEquals(SuccessCode.SUCCESS, controller.playKnightCard());

        // player 2 shoudl have the largest army card now
        assertEquals(3, player1.getNumKnightsPlayed());
        assertEquals(4, player2.getNumKnightsPlayed());
        assertEquals(0, player3.getNumKnightsPlayed());
        assertEquals(0, player4.getNumKnightsPlayed());

        assertFalse(player1.hasLargestArmy());
        assertTrue(player2.hasLargestArmy());
        assertFalse(player3.hasLargestArmy());
        assertFalse(player4.hasLargestArmy());

        assertEquals(POINTS_FROM_SETUP, player1.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP + POINTS_FOR_LARGEST_ARMY, player2.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player3.getVictoryPoints());
        assertEquals(POINTS_FROM_SETUP, player4.getVictoryPoints());
    }
}
