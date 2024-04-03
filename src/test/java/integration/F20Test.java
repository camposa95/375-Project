package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;


/**
 * The purpose of this test class is to test feature 20 (F20):
 *    Monopoly Development Card: Allow the player to claim all resources
 *    of a single type from the hands of all other players, when they play this card during their turn.
 */
public class F20Test {

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
    }
    
    @Test
    public void testMonopolyWorks() {
        controller.setCurrentPlayer(player3);
        //Reminder of resources that each player has after basic setup
        //Resource[] resources1 = {Resource.GRAIN,Resource.BRICK,Resource.LUMBER};
        //Resource[] resources2 = {Resource.GRAIN,Resource.GRAIN,Resource.ORE};
        //Resource[] resources3 = {Resource.BRICK,Resource.LUMBER,Resource.ORE};
        //Resource[] resources4 = {Resource.LUMBER,Resource.LUMBER,Resource.GRAIN};


        // directly add the card to the hand.
        // Note this is not the same as buying the card which would not allow us to use the card we bought during the turn
        player3.hand.addDevelopmentCard(DevCard.MONOPOLY);

        Resource resource = Resource.GRAIN;

        // play the card to start us off
        assertEquals(SuccessCode.SUCCESS, controller.playMonopolyCard(resource));

        // assert we got the resources
        assertEquals(4, player3.hand.getResourceCount(resource));
       

        // assert we used the card
        assertEquals(0, player3.hand.devCards.get(DevCard.MONOPOLY));
    }

    @Test
    public void testMonopolyNoCard() {
        controller.setCurrentPlayer(player3);
        //Reminder of resources that each player has after basic setup
        //Resource[] resources1 = {Resource.GRAIN,Resource.BRICK,Resource.LUMBER};
        //Resource[] resources2 = {Resource.GRAIN,Resource.GRAIN,Resource.ORE};
        //Resource[] resources3 = {Resource.BRICK,Resource.LUMBER,Resource.ORE};
        //Resource[] resources4 = {Resource.LUMBER,Resource.LUMBER,Resource.GRAIN};

       
        // don't give the player the card
       
        Resource resource = Resource.GRAIN;

        // play the card to start us off
        assertEquals(SuccessCode.CANNOT_PLAY_CARD, controller.playMonopolyCard(resource));

        // assert we didn't get the resources
        assertEquals(0, player3.hand.getResourceCount(resource));
       
    }
}