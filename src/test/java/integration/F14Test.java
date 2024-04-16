package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

import data.GameLoader;
import domain.bank.Bank;
import domain.player.HarvestBooster;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.controller.Controller;
import domain.devcarddeck.DevelopmentCardDeck;
import domain.game.Game;
import domain.gameboard.GameBoard;
import domain.game.GameType;
import domain.player.Player;
import domain.bank.Resource;
import domain.graphs.GameboardGraph;

/**
 * The purpose of this test class is to test feature 14 (F14):
 *      Require the players to drop half their deck on a 7
 */
public class F14Test {

    GameboardGraph gameboardGraph;
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
        gameboardGraph = new GameboardGraph(gameType);
        GameLoader.initializeGraphs(gameboardGraph);

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
        Game game = new Game(gameBoard, gameboardGraph, devCardDeck, bank);

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
    public void testRoll7DropResources() {
        player2.hand.addResource(Resource.BRICK, 7);
        player3.hand.addResource(Resource.BRICK, 8);
        player4.hand.addResource(Resource.BRICK, 18);
        //create the expected hash map with resources as they'd come from gui(Only players with 8+ lose cards)
        HashMap<Integer,Resource[]> resourcesMap = new HashMap<>();
        resourcesMap.put(3,new Resource[]{Resource.BRICK,Resource.BRICK,Resource.BRICK,Resource.BRICK});
        resourcesMap.put(4,new Resource[]{Resource.BRICK,Resource.BRICK,Resource.BRICK,Resource.BRICK,
                                            Resource.BRICK,Resource.BRICK,Resource.BRICK,Resource.BRICK,Resource.BRICK});

        int p1Before = player1.hand.getResourceCount();
        int p2Before = player2.hand.getResourceCount();
        int p3before = player3.hand.getResourceCount();
        int p4Before = player4.hand.getResourceCount();

        //Method called by gui when it detects a 7
        controller.dropResources(resourcesMap);

        int p1After = player1.hand.getResourceCount();
        int p2After = player2.hand.getResourceCount();
        int p3After = player3.hand.getResourceCount();
        int p4After = player4.hand.getResourceCount();
        
        assertEquals(p1Before,0);
        assertEquals(p1After,0);
        assertEquals(p2Before,7);
        assertEquals(p2After,7);
        assertEquals(p3before,8);
        assertEquals(p3After,4);
        assertEquals(p4Before,18);
        assertEquals(p4After,9);
    }
  }

