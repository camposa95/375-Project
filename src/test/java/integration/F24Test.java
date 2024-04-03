package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

/**
 * The purpose of this test class is to test feature 24 (F24):
 *      Detect the end of the game Occurs when a player reaches 10+ Victory Points
 */
public class F24Test {

    private static final int POINTS_FROM_SETUP = 2;

    private static final Resource[] RESOURCES_FOR_ROAD = {Resource.BRICK, Resource.LUMBER};
    private static final Resource[] RESOURCES_FOR_DEV_CARD = {Resource.ORE, Resource.WOOL, Resource.GRAIN};
    private static final Resource[] RESOURCES_FOR_SETTLEMENT = {Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN};
    
    private void loopToBeginning(final Controller controller) {
        for (int i = 0; i < 4; i++) {
            controller.setState(GameState.DEFAULT);
            assertEquals(SuccessCode.SUCCESS, controller.endTurn());
        }
    }

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
    public void testWinClickedRoad() {
        // -------------------------- Player 1 build enough to get the longest road ---------------------------
        // Note here the controller default to player 1 first
        // give the player enough resources to build 4 roads
        for (int i = 0; i < 4; i++) {
            player1.hand.addResources(RESOURCES_FOR_ROAD);
        }
        // build some roads
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(26));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(27));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(28));
        
        
        // up to this point we haven't gained any points other that setup so assert on that
        assertEquals(POINTS_FROM_SETUP, player1.getVictoryPoints());

        // assume the player got 9 points from other various sources
        // the exact way how they got them does not matter because we already tested that separately
        player1.setVictoryPoints(9);


        // make sure we the current player is the right one
        assertEquals(player1, controller.getCurrentPlayer());
        // player builds the last road needed to get the longest road card
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.GAME_WIN, controller.clickedRoad(29));
        assertEquals(11, player1.getVictoryPoints());
        // assert that the player who won (the current player) is the one who clicked 
        assertEquals(player1, controller.getCurrentPlayer());

        // Note: it doesn't matter if we get longest road first or by overtaking, or even that we got
        // the longest road at all because the controller method will detect the win regardless of what caused it.
        // The exact source of the points don't really matter in the scope of this test. The only thing that mattered
        // is that we got enough points to win from the click
    }

    @Test
    public void testWinClickedVertex() {
        // now make sure the player has the enough resources
        player1.hand.addResources(RESOURCES_FOR_SETTLEMENT);


        // give the player another road, so we can follow the distance rule
        roads.getRoad(24).setOwner(player1);

        // up to this point we haven't gained any points other that setup so assert on that
        assertEquals(POINTS_FROM_SETUP, player1.getVictoryPoints());

        // assume the player got 9 points from other various sources
        // the exact way how they got them does not matter because we already tested that separately
        player1.setVictoryPoints(9);


        // make sure we the current player is the right one
        assertEquals(player1, controller.getCurrentPlayer());
        // here is the click to buy a new settlement
        int newVertexId = 17; // use a valid id
        controller.setState(GameState.BUILD_SETTLEMENT);
        assertEquals(SuccessCode.GAME_WIN, controller.clickedVertex(newVertexId));
        assertEquals(10, player1.getVictoryPoints());
        // assert that the player who won (the current player) is the one who clicked 
        assertEquals(player1, controller.getCurrentPlayer());

        // Note: again here, it doesn't really matter that we won because of the settlement build.
        // What matters is that the clicked vertex method is able to detect that the player gained
        // enough points to win by going that click. The specific action that caused the points to
        // increase does not matter as long as something did it. For example, this could be from
        // building a settlement, upgrading one to a city, or breaking another persons road and 
        // stealing their longest road card. All of these actions cause point gains and are tested
        // in their respective test classes
    }

    @Test
    public void testWinClickedBuyDevCard() {
        // let the player have 9 points, the exact source doesn't matter
        player1.setVictoryPoints(9);


        // make sure we are on the right player
        assertEquals(player1, controller.getCurrentPlayer());

        // Here we essentially buy dev cards until we gain a point, ie the victory point card was bought
        SuccessCode result;
        do {
            player1.hand.addResources(RESOURCES_FOR_DEV_CARD);
            result = controller.clickedBuyDevCard();

            // if we gained a point exit the loop
        } while (player1.getVictoryPoints() != 10);

        // assert that the win was detected
        assertEquals(SuccessCode.GAME_WIN, result);
        assertEquals(10, player1.getVictoryPoints());
        // assert that the player who won (the current player) is the one who clicked 
        assertEquals(player1, controller.getCurrentPlayer());
    }

    @Test
    public void testLargestArmyClaimedFirst() {
        // Make sure we are on player1
        assertEquals(player1, controller.getCurrentPlayer());

        // give the player some resources
        player1.hand.addResources(RESOURCES_FOR_DEV_CARD);
        player1.hand.addResources(RESOURCES_FOR_DEV_CARD);
        player1.hand.addResources(RESOURCES_FOR_DEV_CARD);

        // player buys three cards
        assertTrue(player1.purchaseDevCard(DevCard.KNIGHT));
        assertTrue(player1.purchaseDevCard(DevCard.KNIGHT));
        assertTrue(player1.purchaseDevCard(DevCard.KNIGHT));

        // can't use bought card on same turn, so end turn until we are back at player 1
        loopToBeginning(controller);
        assertEquals(player1, controller.getCurrentPlayer());
        assertEquals(SuccessCode.SUCCESS, controller.playKnightCard());
        
        // can only use one card per turn so loop back to beginning
        loopToBeginning(controller);
        assertEquals(player1, controller.getCurrentPlayer());
        assertEquals(SuccessCode.SUCCESS, controller.playKnightCard());

        // can only use one card per turn so loop back to beginning
        loopToBeginning(controller);
        assertEquals(player1, controller.getCurrentPlayer());

        // at this point assume the player has 9 points, again the exact source doesn't matter
        player1.setVictoryPoints(9);

        // now playing this knight card should cause us to gain the largest army and 2 points to win
        assertEquals(SuccessCode.GAME_WIN, controller.playKnightCard());
        assertEquals(11, player1.getVictoryPoints());
        // assert that the player who won (the current player) is the one who clicked 
        assertEquals(player1, controller.getCurrentPlayer());

        // Note: as usual, the fact that we gained the largest army for the first time doesn't matter as long
        // as the click caused us to gain enough points to win
    }

    @Test
    public void testWinClickedEndTurn() {
        // make sure we are on the right player
        assertEquals(player1, controller.getCurrentPlayer());

        // assume the next player to go has 10 points already, the exact way they got them does not matter
        player2.setVictoryPoints(10);

        // Here we end the turn and assert that the win was detected
        controller.setState(GameState.DEFAULT);
        assertEquals(SuccessCode.GAME_WIN, controller.endTurn());
        // assert that the player who won (the current player) is the player whose turn it just became 
        assertEquals(player2, controller.getCurrentPlayer());

        // again, the exact reason how the next player to go got their points doesn't matter as long 
        // as the controller is able to do win detection upon it becoming the next persons turn. In reality
        // though this is only possible if the player2 got the longest road from a third party road break on
        // the player who currently had it. However, if we were to adjust the game rules such that there are
        // more ways to gain points not on your turn this provides a nice abstraction
    }

    @Test
    public void testNoWinOnNotYourTurn() {
        // -------------------------- Start of Actual Test Stuff ---------------------------

        // ------------------- Player 1 build enough to get the longest road ---------------------------
        // Note here the controller default to player 1 first
        // give the player enough resources to build 7 roads
        for (int i = 0; i < 7; i++) {
            player1.hand.addResources(RESOURCES_FOR_ROAD);
        }
        // build some roads make sure they succeed
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(26));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(27));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(28));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(29));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(30));

        // player 1 should have the longest road card
        assertTrue(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        // ----------------------- Player 3 build a path of 5 long ---------------------------
        // move to player3
        controller.endTurn();
        controller.setState(GameState.DEFAULT);
        controller.endTurn();
        controller.setState(GameState.DEFAULT);
        assertEquals(player3, controller.getCurrentPlayer());

        // give the player enough resources to build 4 roads
        for (int i = 0; i < 4; i++) {
            controller.getCurrentPlayer().hand.addResources(RESOURCES_FOR_ROAD);
        }
        // build some roads
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(63));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(68));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(69));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(70));


        // player 1 should have the longest road card still
        assertTrue(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertFalse(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        // -------- Player 2 build and breaks player 1 but is short, but player 3 long enough to steal third party ---------------------------
        // move to next person
        controller.endTurn();
        controller.setState(GameState.DEFAULT);
        controller.endTurn();
        controller.setState(GameState.DEFAULT);
        controller.endTurn();
        controller.setState(GameState.DEFAULT);
        assertEquals(player2, controller.getCurrentPlayer());

        // give the player enough resources to build 4 roads
        for (int i = 0; i < 4; i++) {
            controller.getCurrentPlayer().hand.addResources(RESOURCES_FOR_ROAD);
        }
        // build some roads
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(51));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(44));
        controller.setState(GameState.BUILD_ROAD);
        assertEquals(SuccessCode.SUCCESS, controller.clickedRoad(36));

        // give the player enough resources to build a settlement
        controller.getCurrentPlayer().hand.addResources(RESOURCES_FOR_SETTLEMENT);

        // Directly before the significant click assume player 3 has 9 points
        player3.setVictoryPoints(9);

        // build the settlement to break player1's path
        // after this the player 1 has road max 4 long, player 2 hand path max 4 long, and player 3 has path 5 long
        controller.setState(GameState.BUILD_SETTLEMENT);
        SuccessCode result = controller.clickedVertex(22);

        // player 3 should have the card
        assertFalse(player1.hasLongestRoad());
        assertFalse(player2.hasLongestRoad());
        assertTrue(player3.hasLongestRoad());
        assertFalse(player4.hasLongestRoad());

        // player 3 should have 11 points
        assertEquals(11, player3.getVictoryPoints());

        // but notice how no game win was detected here because it is not player3's turn
        assertNotEquals(SuccessCode.GAME_WIN, result);
        // also assert that it is still player2's turn
        assertEquals(player2, controller.getCurrentPlayer());

        // Here we end our turn which should cause player 3 to win instantly
        assertEquals(SuccessCode.GAME_WIN, controller.endTurn());
        // assert that the current player (the player who won) is now player 3
        assertEquals(player3, controller.getCurrentPlayer()); 
    }
}
