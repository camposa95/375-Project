package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;

import data.GameLoader;
import domain.bank.Bank;
import domain.player.HarvestBooster;
import org.junit.jupiter.api.Test;

import domain.controller.Controller;
import domain.devcarddeck.DevelopmentCardDeck;
import domain.game.Game;
import domain.gameboard.GameBoard;
import domain.game.GameType;
import domain.player.Player;
import domain.bank.Resource;
import domain.graphs.RoadGraph;
import domain.graphs.VertexGraph;

/**
 * The purpose of this test class is to test feature 14 (F14):
 *      Require the players to drop half their deck on a 7
 */
public class F14Test {
    
    @Test
    public void testRoll7DropResources() {

        // Here are some basic wiring needed that would be done by main
        // declare some constants up here
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
        Controller controller = new Controller(game, players, gameType);

        //Based on beginner locations these are the resources each player should recieve after setup
        Resource[] resources1 = {Resource.GRAIN,Resource.BRICK,Resource.LUMBER};
        Resource[] resources2 = {Resource.GRAIN,Resource.GRAIN,Resource.ORE};
        Resource[] resources3 = {Resource.BRICK,Resource.LUMBER,Resource.ORE};
        Resource[] resources4 = {Resource.LUMBER,Resource.LUMBER,Resource.GRAIN};
        //now remove them
        player1.hand.removeResources(resources1);
        player2.hand.removeResources(resources2);
        player3.hand.removeResources(resources3);
        player4.hand.removeResources(resources4);
        //add back the amount we want for our test
        player2.hand.addResource(Resource.BRICK, 7);
        player3.hand.addResource(Resource.BRICK, 8);
        player4.hand.addResource(Resource.BRICK, 18);
        //create the expected hash map with resources as theyd come from gui(Only players with 8+ lose cards)
        HashMap<Integer,Resource[]> resourcesMap = new HashMap<>();
        resourcesMap.put(3,new Resource[]{Resource.BRICK,Resource.BRICK,Resource.BRICK,Resource.BRICK});
        resourcesMap.put(4,new Resource[]{Resource.BRICK,Resource.BRICK,Resource.BRICK,Resource.BRICK,
                                            Resource.BRICK,Resource.BRICK,Resource.BRICK,Resource.BRICK,Resource.BRICK});

        int p1Before = player1.hand.getResourceCardCount();
        int p2Before = player2.hand.getResourceCardCount();
        int p3before = player3.hand.getResourceCardCount();
        int p4Before = player4.hand.getResourceCardCount();

        //Method called by gui when it detects a 7
        controller.dropResources(resourcesMap);

        int p1After = player1.hand.getResourceCardCount();
        int p2After = player2.hand.getResourceCardCount();
        int p3After = player3.hand.getResourceCardCount();
        int p4After = player4.hand.getResourceCardCount();
        
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

