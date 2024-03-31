package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import data.GameLoader;
import domain.bank.Bank;
import domain.player.HarvestBooster;
import org.easymock.EasyMock;
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

import java.util.Random;

/**
 * The purpose of this test class is to test feature 5 (F5):
 *      Ability for the Player to roll the dice, and all players to collect resources
 *      from spaces with the number on the dice
 */
public class F5Test {

    @Test
    public void testGettingResourcesFromSetupBeginner() {
     // ---------------------- Here are some basic wiring needed that would be done by main ------------------------------
        
        // Testing for beginner
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

    // -------------------------- Start of Test  ---------------------------
        
        //Based on beginner locations these are the resources each player should recieve after setup
        Resource[] resources1 = {Resource.GRAIN,Resource.BRICK,Resource.LUMBER};
        Resource[] resources2 = {Resource.GRAIN,Resource.GRAIN,Resource.ORE};
        Resource[] resources3 = {Resource.BRICK,Resource.LUMBER,Resource.ORE};
        Resource[] resources4 = {Resource.LUMBER,Resource.LUMBER,Resource.GRAIN};

        assertTrue(player1.hand.removeResources(resources1));
        assertTrue(player2.hand.removeResources(resources2));
        assertTrue(player3.hand.removeResources(resources3));
        assertTrue(player4.hand.removeResources(resources4));
        
        //avoid an spotbug error with controller not called
        controller.endTurn();

    }
    @Test
    public void testGettingResourcesFromSetupAdvanced() {
     // ---------------------- Here are some basic wiring needed that would be done by main ------------------------------
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

        // other things dependent on these things
        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        // must have gameBoard in beginner still so we know where tiles are
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        Game game = new Game(gameBoard, vertexes, roads, devCardDeck, bank);
        Controller controller = new Controller(game, players, gameType);

    // -------------------------- Start of Test  ---------------------------
        
        //Based on beginner locations these are the resources each player should recieve after setup
        //Note resources4 only has 2 since it was moved to edge of desert tile to test
        Resource[] resources1 = {Resource.GRAIN,Resource.BRICK,Resource.LUMBER};
        Resource[] resources2 = {Resource.GRAIN,Resource.GRAIN,Resource.ORE};
        Resource[] resources3 = {Resource.BRICK,Resource.LUMBER,Resource.ORE};
        Resource[] resources4 = {Resource.LUMBER,Resource.WOOL};

        //Manualy imput player turns, these are the same as beginner except for a first location
        
        //First player first settlement and road
        assertEquals(SuccessCode.SUCCESS,controller.clickedVertex(35));
        assertEquals(SuccessCode.SUCCESS,controller.clickedRoad(37));

        //Second player first settlement and road
        assertEquals(SuccessCode.SUCCESS,controller.clickedVertex(13));
        assertEquals(SuccessCode.SUCCESS,controller.clickedRoad(15));

        //Third player first settlement and road
        assertEquals(SuccessCode.SUCCESS,controller.clickedVertex(44));
        assertEquals(SuccessCode.SUCCESS,controller.clickedRoad(52));

        //Fourth player first settlement and road
        assertEquals(SuccessCode.SUCCESS,controller.clickedVertex(10));
        assertEquals(SuccessCode.SUCCESS,controller.clickedRoad(13));

        //Second placements, each player should get resources after placing settlement

        //Fourth player second settlement and road
        assertEquals(SuccessCode.SUCCESS,controller.clickedVertex(22)); 
        assertEquals(SuccessCode.SUCCESS,controller.clickedRoad(36));
        assertTrue(player4.hand.removeResources(resources4));
        //Third player second settlement and road
        assertEquals(SuccessCode.SUCCESS,controller.clickedVertex(40));
        assertEquals(SuccessCode.SUCCESS,controller.clickedRoad(56));
        assertTrue(player3.hand.removeResources(resources3));
        //Second player second settlement and road
        assertEquals(SuccessCode.SUCCESS,controller.clickedVertex(42));
        assertTrue(player2.hand.removeResources(resources2));
        assertEquals(SuccessCode.SUCCESS,controller.clickedRoad(58));     

        //First player second settlement and road
        assertEquals(SuccessCode.SUCCESS,controller.clickedVertex(19));
        assertEquals(SuccessCode.SUCCESS,controller.clickedRoad(25));
        assertTrue(player1.hand.removeResources(resources1));

    }
    @Test
    public void testFirstTurnRollDieAndGetResources() {
         // ---------------------- Here are some basic wiring needed that would be done by main ------------------------------
        
        // Testing for beginner
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

        Random mockedRandom = EasyMock.createStrictMock(Random.class);

        Controller controller = new Controller(game, players, gameType, mockedRandom);

    // -------------------------- Start of Test  ---------------------------
        
        //Remove starter resources
        Resource[] r1 = {Resource.GRAIN,Resource.BRICK,Resource.LUMBER};
        Resource[] r2 = {Resource.GRAIN,Resource.GRAIN,Resource.ORE};
        Resource[] r3 = {Resource.BRICK,Resource.LUMBER,Resource.ORE};
        Resource[] r4 = {Resource.LUMBER,Resource.LUMBER,Resource.GRAIN};
        player1.hand.removeResources(r1);
        player2.hand.removeResources(r2);
        player3.hand.removeResources(r3);
        player4.hand.removeResources(r4);

        //Based on beginner locations these are the resources each player should recieve on given die roll
        EasyMock.expect(mockedRandom.nextInt(2,13)).andReturn(10);
        EasyMock.replay(mockedRandom);
        controller.rollDice();
        EasyMock.verify(mockedRandom);

        Resource[] resources1 = {};
        Resource[] resources2 = {Resource.BRICK};
        Resource[] resources3 = {};
        Resource[] resources4 = {Resource.ORE};

        assertTrue(player1.hand.removeResources(resources1));
        assertTrue(player2.hand.removeResources(resources2));
        assertTrue(player3.hand.removeResources(resources3));
        assertTrue(player4.hand.removeResources(resources4));
        
        // avoid an spotbug error with controller not called
        controller.endTurn();
    }
    @Test
    public void testGetMoreResourcesFromUpgradedSettlements() {
        // ---------------------- Here are some basic wiring needed that would be done by main ------------------------------
        
        // Testing for beginner
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
        Random mockedRandom = EasyMock.createStrictMock(Random.class);
        Controller controller = new Controller(game, players, gameType, mockedRandom);

    // -------------------------- Start of Test  ---------------------------
        
        //Remove starter resources
        Resource[] r1 = {Resource.GRAIN,Resource.BRICK,Resource.LUMBER};
        Resource[] r2 = {Resource.GRAIN,Resource.GRAIN,Resource.ORE};
        Resource[] r3 = {Resource.BRICK,Resource.LUMBER,Resource.ORE};
        Resource[] r4 = {Resource.LUMBER,Resource.LUMBER,Resource.GRAIN};
        player1.hand.removeResources(r1);
        player2.hand.removeResources(r2);
        player3.hand.removeResources(r3);
        player4.hand.removeResources(r4);
        
        //give resources needed for upgrade
        player2.hand.addResources(new Resource[]{
            Resource.ORE,
            Resource.ORE,
            Resource.ORE,
            Resource.GRAIN,
            Resource.GRAIN
        });
        player4.hand.addResources(new Resource[]{
            Resource.ORE,
            Resource.ORE,
            Resource.ORE,
            Resource.GRAIN,
            Resource.GRAIN
        });
        
        //upgrade player 2
        controller.setCurrentPlayer(player2);
        controller.setState(GameState.UPGRADE_SETTLEMENT);
        assertEquals(SuccessCode.SUCCESS, controller.clickedVertex(13));

        //Upgrade player 4
        controller.setCurrentPlayer(player4);
        controller.setState(GameState.UPGRADE_SETTLEMENT);
        assertEquals(SuccessCode.SUCCESS, controller.clickedVertex(10));

        
        
        //Based on beginner locations these are the resources each player should recieve on given die roll
        EasyMock.expect(mockedRandom.nextInt(2,13)).andReturn(10);
        EasyMock.replay(mockedRandom);
        controller.rollDice();
        EasyMock.verify(mockedRandom);

        Resource[] resources1 = {};
        Resource[] resources2 = {Resource.BRICK,Resource.BRICK};
        Resource[] resources3 = {};
        Resource[] resources4 = {Resource.ORE,Resource.ORE};

        assertTrue(player1.hand.removeResources(resources1));
        assertTrue(player2.hand.removeResources(resources2));
        assertTrue(player3.hand.removeResources(resources3));
        assertTrue(player4.hand.removeResources(resources4));
        
        //avoid an spotbug error with controller not called
        controller.endTurn();
    }
    @Test
    public void testNoOneGetsResourcesOn7() {
         // ---------------------- Here are some basic wiring needed that would be done by main ------------------------------
        
        // Testing for beginner
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
        Random mockedRandom = EasyMock.createStrictMock(Random.class);
        Controller controller = new Controller(game, players, gameType, mockedRandom);

    // -------------------------- Start of Test  ---------------------------
        
        //Remove starter resources
        Resource[] r1 = {Resource.GRAIN,Resource.BRICK,Resource.LUMBER};
        Resource[] r2 = {Resource.GRAIN,Resource.GRAIN,Resource.ORE};
        Resource[] r3 = {Resource.BRICK,Resource.LUMBER,Resource.ORE};
        Resource[] r4 = {Resource.LUMBER,Resource.LUMBER,Resource.GRAIN};
        player1.hand.removeResources(r1);
        player2.hand.removeResources(r2);
        player3.hand.removeResources(r3);
        player4.hand.removeResources(r4);
        //Based on beginner locations these are the resources each player should recieve on given die roll

        EasyMock.expect(mockedRandom.nextInt(2, 13)).andReturn(7);
        EasyMock.replay(mockedRandom);
        controller.rollDice();
        EasyMock.verify(mockedRandom);

        Resource[] resources2 = {Resource.BRICK};
        Resource[] resources4 = {Resource.ORE};

        //check players didnt recieve
        assertFalse(player2.hand.removeResources(resources2));
        assertFalse(player4.hand.removeResources(resources4));
        
        //avoid an spotbug error with controller not called
        controller.endTurn();

    }

}
