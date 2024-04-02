package integration;

import data.GameLoader;
import domain.controller.Controller;
import domain.controller.GameState;
import domain.bank.Bank;
import domain.bank.Resource;
import domain.devcarddeck.DevelopmentCardDeck;
import domain.game.Game;
import domain.game.GameType;
import domain.gameboard.GameBoard;
import domain.player.Hand;
import domain.player.HarvestBooster;
import domain.player.Player;
import domain.graphs.RoadGraph;
import domain.graphs.VertexGraph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class F3Test {

    //Test to make sure each player starts with an empty hand and
    @Test
    public void testPlayersStartWithEmptyHand() {
        GameType gameType = GameType.Advanced;
        VertexGraph vertexes = new VertexGraph(gameType);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        Bank bank = new Bank();
        Player player1 = new Player(1, new HarvestBooster(), bank);
        Player player2 = new Player(2, new HarvestBooster(), bank);
        Player player3 = new Player(3, new HarvestBooster(), bank);
        Player player4 = new Player(4, new HarvestBooster(), bank);
        Player[] players = {player1, player2, player3, player4};

        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        Game game = new Game(gameBoard, vertexes, roads, devCardDeck, bank);

        // Assert that the beginner setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> controllerRef.set(new Controller(game, players, gameType)), "Setup while loop timed out");
        Controller controller = controllerRef.get();
        assertEquals(GameState.FIRST_SETTLEMENT, controller.getState());

        Hand player1Hand = player1.hand;
        Hand player2Hand = player2.hand;
        Hand player3Hand = player3.hand;
        Hand player4Hand = player4.hand;

        assertNotEquals(player1Hand, player2Hand);
        assertNotEquals(player1Hand, player3Hand);
        assertNotEquals(player1Hand, player4Hand);

        assertNotEquals(player2Hand, player3Hand);
        assertNotEquals(player2Hand, player4Hand);

        assertNotEquals(player3Hand, player4Hand);

        assertEquals(0, player1Hand.getResourceCardCount());
        assertEquals(0, player2Hand.getResourceCardCount());
        assertEquals(0, player3Hand.getResourceCardCount());
        assertEquals(0, player4Hand.getResourceCardCount());
    }

    @Test
    public void testPlayerCanAddResourcesToTheirHand() {
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

        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        Game game = new Game(gameBoard, vertexes, roads, devCardDeck, bank);

        // Assert that the beginner setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> controllerRef.set(new Controller(game, players, gameType)), "Setup while loop timed out");
        Controller controller = controllerRef.get();
        assertEquals(GameState.TURN_START, controller.getState());

        // Note: at this point the players would have gotten some starter resources during the
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the player's hand
        for (Resource resource: Resource.values()) {
            if (resource != Resource.ANY) { // skip this one used for trading
                int count = player1.hand.getResourceCardAmount(resource);
                if (count > 0) {
                    player1.hand.removeResource(resource, count);
                }
            }
        }
        assertEquals(0, player1.hand.getResourceCardCount());

        for (Resource resource: Resource.values()) {
            if (resource != Resource.ANY) { // skip this one used for trading
                int count = player2.hand.getResourceCardAmount(resource);
                if (count > 0) {
                    player2.hand.removeResource(resource, count);
                }
            }
        }
        assertEquals(0, player2.hand.getResourceCardCount());

        for (Resource resource: Resource.values()) {
            if (resource != Resource.ANY) { // skip this one used for trading
                int count = player3.hand.getResourceCardAmount(resource);
                if (count > 0) {
                    player3.hand.removeResource(resource, count);
                }
            }
        }
        assertEquals(0, player3.hand.getResourceCardCount());

        for (Resource resource: Resource.values()) {
            if (resource != Resource.ANY) { // skip this one used for trading
                int count = player4.hand.getResourceCardAmount(resource);
                if (count > 0) {
                    player4.hand.removeResource(resource, count);
                }
            }
        }
        assertEquals(0, player4.hand.getResourceCardCount());

        bank.reset();

        //try to add resources to each player's hand and should expect success
        Resource[] resourcesForPlayer1 = {
                Resource.LUMBER,
                Resource.BRICK
        };

        Resource[] resourcesForPlayer2 = {
                Resource.WOOL,
                Resource.WOOL,
                Resource.GRAIN
        };

        Resource[] resourcesForPlayer3 = {
                Resource.LUMBER,
                Resource.BRICK,
                Resource.WOOL,
                Resource.GRAIN
        };

        Resource[] resourcesForPlayer4 = {
                Resource.GRAIN,
                Resource.GRAIN,
                Resource.ORE,
                Resource.ORE,
                Resource.ORE
        };

        //add the cards to the hands
        boolean successP1 = player1.hand.addResources(resourcesForPlayer1);
        boolean successP2 = player2.hand.addResources(resourcesForPlayer2);
        boolean successP3 = player3.hand.addResources(resourcesForPlayer3);
        boolean successP4 = player4.hand.addResources(resourcesForPlayer4);

        //assertions for player 1
        assertTrue(successP1);
        assertEquals(2, player1.hand.getResourceCardCount());
        assertEquals(1, player1.hand.getResourceCardAmount(Resource.LUMBER));
        assertEquals(1, player1.hand.getResourceCardAmount(Resource.BRICK));

        //assertions for player 2
        assertTrue(successP2);
        assertEquals(3, player2.hand.getResourceCardCount());
        assertEquals(2, player2.hand.getResourceCardAmount(Resource.WOOL));
        assertEquals(1, player2.hand.getResourceCardAmount(Resource.GRAIN));

        //assertions for player 3
        assertTrue(successP3);
        assertEquals(4, player3.hand.getResourceCardCount());
        assertEquals(1, player3.hand.getResourceCardAmount(Resource.LUMBER));
        assertEquals(1, player3.hand.getResourceCardAmount(Resource.BRICK));
        assertEquals(1, player3.hand.getResourceCardAmount(Resource.WOOL));
        assertEquals(1, player3.hand.getResourceCardAmount(Resource.GRAIN));

        //assertions for player 4
        assertTrue(successP4);
        assertEquals(5, player4.hand.getResourceCardCount());
        assertEquals(2, player4.hand.getResourceCardAmount(Resource.GRAIN));
        assertEquals(3, player4.hand.getResourceCardAmount(Resource.ORE));

        //19 lumber cards
        Resource[] addTooMany = {
                Resource.LUMBER,Resource.LUMBER,Resource.LUMBER,
                Resource.LUMBER,Resource.LUMBER,Resource.LUMBER,
                Resource.LUMBER,Resource.LUMBER,Resource.LUMBER,
                Resource.LUMBER,Resource.LUMBER,Resource.LUMBER,
                Resource.LUMBER,Resource.LUMBER,Resource.LUMBER,
                Resource.LUMBER,Resource.LUMBER,Resource.LUMBER,
                Resource.LUMBER,
        };
        //now attempt to add too many cards to a hand. This should fail

        boolean successP1TooMany = player1.hand.addResources(addTooMany);
        assertFalse(successP1TooMany);
        assertEquals(1, player1.hand.getResourceCardAmount(Resource.LUMBER));
        assertEquals(1, player1.hand.getResourceCardAmount(Resource.BRICK));
    }

    @Test
    public void testPlayerCanRemoveResourcesToTheirHand(){
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

        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        Game game = new Game(gameBoard, vertexes, roads, devCardDeck, bank);

        // Assert that the beginner setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> controllerRef.set(new Controller(game, players, gameType)), "Setup while loop timed out");
        Controller controller = controllerRef.get();
        assertEquals(GameState.TURN_START, controller.getState());

        // Note: at this point the players would have gotten some starter resources during the
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the player's hand
        for (Resource resource: Resource.values()) {
            if (resource != Resource.ANY) { // skip this one used for trading
                int count = player1.hand.getResourceCardAmount(resource);
                if (count > 0) {
                    player1.hand.removeResource(resource, count);
                }
            }
        }
        assertEquals(0, player1.hand.getResourceCardCount());

        for (Resource resource: Resource.values()) {
            if (resource != Resource.ANY) { // skip this one used for trading
                int count = player2.hand.getResourceCardAmount(resource);
                if (count > 0) {
                    player2.hand.removeResource(resource, count);
                }
            }
        }
        assertEquals(0, player2.hand.getResourceCardCount());

        for (Resource resource: Resource.values()) {
            if (resource != Resource.ANY) { // skip this one used for trading
                int count = player3.hand.getResourceCardAmount(resource);
                if (count > 0) {
                    player3.hand.removeResource(resource, count);
                }
            }
        }
        assertEquals(0, player3.hand.getResourceCardCount());

        for (Resource resource: Resource.values()) {
            if (resource != Resource.ANY) { // skip this one used for trading
                int count = player4.hand.getResourceCardAmount(resource);
                if (count > 0) {
                    player4.hand.removeResource(resource, count);
                }
            }
        }
        assertEquals(0, player4.hand.getResourceCardCount());

        bank.reset();

        Resource[] resourcesForPlayer1 = {
                Resource.LUMBER,
                Resource.BRICK
        };

        Resource[] resourcesForPlayer2 = {
                Resource.WOOL,
                Resource.WOOL,
                Resource.GRAIN
        };

        Resource[] resourcesForPlayer3 = {
                Resource.LUMBER,
                Resource.BRICK,
                Resource.WOOL,
                Resource.GRAIN
        };

        Resource[] resourcesForPlayer4 = {
                Resource.GRAIN,
                Resource.GRAIN,
                Resource.ORE,
                Resource.ORE,
                Resource.ORE
        };

        //add the cards to the hands
        player1.hand.addResources(resourcesForPlayer1);
        player2.hand.addResources(resourcesForPlayer2);
        player3.hand.addResources(resourcesForPlayer3);
        player4.hand.addResources(resourcesForPlayer4);

        //Now we test trying to remove resources from each player's hand
        boolean successP1 = player1.hand.removeResource(Resource.LUMBER, 1);
        boolean successP2 = player2.hand.removeResource(Resource.WOOL, 1);
        boolean successP3 = player3.hand.removeResource(Resource.BRICK, 1);
        boolean successP4 = player4.hand.removeResource(Resource.ORE, 1);

        //player 1's assertions
        assertTrue(successP1);
        assertEquals(1, player1.hand.getResourceCardCount());
        assertEquals(0, player1.hand.getResourceCardAmount(Resource.LUMBER));
        assertEquals(1, player1.hand.getResourceCardAmount(Resource.BRICK));

        //player 2's assertions
        assertTrue(successP2);
        assertEquals(2, player2.hand.getResourceCardCount());
        assertEquals(1, player2.hand.getResourceCardAmount(Resource.WOOL));
        assertEquals(1, player2.hand.getResourceCardAmount(Resource.GRAIN));

        //player 3's assertions
        assertTrue(successP3);
        assertEquals(3, player3.hand.getResourceCardCount());
        assertEquals(1, player3.hand.getResourceCardAmount(Resource.LUMBER));
        assertEquals(1, player3.hand.getResourceCardAmount(Resource.WOOL));
        assertEquals(1, player3.hand.getResourceCardAmount(Resource.GRAIN));

        //player 4's assertions
        assertTrue(successP4);
        assertEquals(4, player4.hand.getResourceCardCount());
        assertEquals(2, player4.hand.getResourceCardAmount(Resource.GRAIN));
        assertEquals(2, player4.hand.getResourceCardAmount(Resource.ORE));

        //now try removing something from the hand that the player does not have
        Resource[] resourcesToRemove = {
                Resource.ORE
        };

        boolean successRemoveShouldFail = player1.hand.removeResources(resourcesToRemove);
        assertFalse(successRemoveShouldFail);
        assertEquals(1, player1.hand.getResourceCardCount());
        assertEquals(0, player1.hand.getResourceCardAmount(Resource.LUMBER));
        assertEquals(1, player1.hand.getResourceCardAmount(Resource.BRICK));
    }
}
