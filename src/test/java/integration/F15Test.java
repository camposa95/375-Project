package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import data.GameLoader;
import domain.player.HarvestBooster;
import org.junit.jupiter.api.Test;

import domain.controller.Controller;
import domain.controller.SuccessCode;
import domain.bank.Bank;
import domain.devcarddeck.DevelopmentCardDeck;
import domain.game.Game;
import domain.gameboard.GameBoard;
import domain.game.GameType;
import domain.player.Player;
import domain.graphs.RoadGraph;
import domain.graphs.VertexGraph;


/**
 * The purpose of this test class is to test feature 15 (F15):
 *   Ability for the Player to move the robber to a new space
 *   and collect a random resource from any player on the new space,
 *   when the player rolls a 7.
 */
public class F15Test {
    
    @Test
    public void moveRobberAndRobAPlayer() {
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
        bank.reset();
        Controller controller = new Controller(game, players, gameType);

        // -------------------------- Start of Actual Test Stuff ---------------------------
               
        //Reminder of resources that each player has after basic setup
        //Resource[] resources1 = {Resource.GRAIN,Resource.BRICK,Resource.LUMBER};
        //Resource[] resources2 = {Resource.GRAIN,Resource.GRAIN,Resource.ORE};
        //Resource[] resources3 = {Resource.BRICK,Resource.LUMBER,Resource.ORE};
        //Resource[] resources4 = {Resource.LUMBER,Resource.LUMBER,Resource.GRAIN};

        //check state before
        assertEquals(9,gameBoard.getRobberTile().getTileNumber());
        assertEquals(3, player1.hand.getResourceCardCount());
        assertEquals(3, player2.hand.getResourceCardCount());

        int newRobber = 13;
        //The player on the new robber tile
        int robbedPlayer = 2;
        //the gui will call these two methods
        assertEquals(SuccessCode.SUCCESS, controller.moveRobber(newRobber));
        assertEquals(SuccessCode.SUCCESS, controller.robPlayer(robbedPlayer));
        
        //check that the robber moved
        assertEquals(newRobber,gameBoard.getRobberTile().getTileNumber());
        //check that the hands are correct
        assertEquals(4, player1.hand.getResourceCardCount());
        assertEquals(2, player2.hand.getResourceCardCount());
    }
    @Test
    public void tryMoveRobberAndRobAPlayerAndFail() {
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
        bank.reset();
        Controller controller = new Controller(game, players, gameType);

        // -------------------------- Start of Actual Test Stuff ---------------------------
               
        //Reminder of resources that each player has after basic setup
        //Resource[] resources1 = {Resource.GRAIN,Resource.BRICK,Resource.LUMBER};
        //Resource[] resources2 = {Resource.GRAIN,Resource.GRAIN,Resource.ORE};
        //Resource[] resources3 = {Resource.BRICK,Resource.LUMBER,Resource.ORE};
        //Resource[] resources4 = {Resource.LUMBER,Resource.LUMBER,Resource.GRAIN};

        //check state before
        assertEquals(9,gameBoard.getRobberTile().getTileNumber());
        assertEquals(3, player1.hand.getResourceCardCount());

        int newRobber = 9;
        //This time we call with an invalid player(cur player)
        int robbedPlayer = 1;
        //the gui will call these two methods
        //This time the robber tile is the same
        assertEquals(SuccessCode.INVALID_PLACEMENT, controller.moveRobber(newRobber));
        assertThrows(IllegalArgumentException.class,()->{controller.robPlayer(robbedPlayer);});
        
        //check that the robber didnt move
        assertEquals(newRobber,gameBoard.getRobberTile().getTileNumber());
        //check that the hands are correct
        assertEquals(3, player1.hand.getResourceCardCount());
       
    }
}