package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import data.GameLoader;
import domain.player.HarvestBooster;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.controller.Controller;
import domain.controller.SuccessCode;
import domain.bank.Bank;
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
 * The purpose of this test class is to test feature 19 (F19):
 *      Year of Plenty Development Card: Allow the player to draw any two resources
 *      from the bank for free when they play this card during their turn.
 */
public class F19Test {

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
    public void testYearOfPlentyAllGood() {
        // directly add the card to the hand.
        // Note this is not the same as buying the card which would not allow us to use the card we bought during the turn
        player1.hand.addDevelopmentCard(DevCard.PLENTY);

        Resource resource1 = Resource.WOOL;
        Resource resource2 = Resource.GRAIN;

        // play the card to start us off
        assertEquals(SuccessCode.SUCCESS, controller.playYearOfPlenty(resource1, resource2));

        // assert we got the resources
        assertEquals(1, player1.hand.getResourceCount(resource1));
        assertEquals(1, player1.hand.getResourceCount(resource2));

        // assert we used the card
        assertEquals(0, player1.hand.devCards.get(DevCard.PLENTY));
    }

    @Test
    public void testYearOfPlentyNoCard() {
        // don't give the player the card
        // player1.hand.addDevelopmentCard(DevCard.PLENTY);

        Resource resource1 = Resource.WOOL;
        Resource resource2 = Resource.GRAIN;

        // play the card to start us off
        assertEquals(SuccessCode.CANNOT_PLAY_CARD, controller.playYearOfPlenty(resource1, resource2));

        // assert we got the
        assertEquals(0, player1.hand.getResourceCount(resource1));
        assertEquals(0, player1.hand.getResourceCount(resource2));
    }

    @Test
    public void testYearOfPlentyBankEmpty() {
        // directly add the card to the hand.
        // Note this is not the same as buying the card which would not allow us to use the card we bought during the turn
        player1.hand.addDevelopmentCard(DevCard.PLENTY);

        Resource resource1 = Resource.WOOL;
        Resource resource2 = Resource.GRAIN;

        // make the bank empty
        bank.removeResource(resource1, bank.getResourceAmount(resource1));
        bank.removeResource(resource2, bank.getResourceAmount(resource2));

        // play the card to start us off
        assertEquals(SuccessCode.INSUFFICIENT_RESOURCES, controller.playYearOfPlenty(resource1, resource2));

        // assert we did not get the resources
        assertEquals(0, player1.hand.getResourceCount(resource1));
        assertEquals(0, player1.hand.getResourceCount(resource2));

        // assert did not use the card because it failed
        assertEquals(1, player1.hand.devCards.get(DevCard.PLENTY));
    }
}
