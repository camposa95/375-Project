package integration;

import static domain.bank.Resource.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import data.GameLoader;
import domain.bank.Bank;
import domain.player.HarvestBooster;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
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
import domain.graphs.GameboardGraph;

import java.util.Random;

/**
 * The purpose of this test class is to test feature 5 (F5):
 *      Ability for the Player to roll the dice, and all players to collect resources
 *      from spaces with the number on the dice
 */
public class F5Test {

    GameboardGraph vertexes;
    Player player1;
    Player player2;
    Player player3;
    Player player4;
    Player[] players;
    Random mockedRandom;
    Controller controller;
    @BeforeEach
    public void createGameObjects() {
        GameType gameType = GameType.Beginner;
        vertexes = new GameboardGraph(gameType);
        GameLoader.initializeGraphs(vertexes);

        Bank bank = new Bank();
        player1 = new Player(1, new HarvestBooster(), bank);
        player2 = new Player(2, new HarvestBooster(), bank);
        player3 = new Player(3, new HarvestBooster(), bank);
        player4 = new Player(4, new HarvestBooster(), bank);
        players = new Player[]{player1, player2, player3, player4};

        // other things dependent on these things
        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        Game game = new Game(gameBoard, vertexes, devCardDeck, bank);
        mockedRandom = EasyMock.createStrictMock(Random.class);
        controller = new Controller(game, players, gameType, mockedRandom);
    }

    @Test
    public void testGettingResourcesFromSetupBeginner() {
        // Based on beginner locations these are the resources each player should receive after setup
        Resource[] resources1 = {GRAIN, BRICK, LUMBER};
        Resource[] resources2 = {GRAIN, GRAIN, ORE};
        Resource[] resources3 = {BRICK, LUMBER,ORE};
        Resource[] resources4 = {LUMBER, LUMBER, GRAIN};

        assertTrue(player1.hand.removeResources(resources1));
        assertTrue(player2.hand.removeResources(resources2));
        assertTrue(player3.hand.removeResources(resources3));
        assertTrue(player4.hand.removeResources(resources4));
    }

    @Test
    public void testFirstTurnRollDieAndGetResources() {
        for (Player p: players) { // clear starter resources for test isolation
            p.hand.clearResources();
        }

        // Based on beginner locations these are the resources each player should receive on given die roll
        EasyMock.expect(mockedRandom.nextInt(2,13)).andReturn(10);
        EasyMock.replay(mockedRandom);
        controller.rollDice();
        EasyMock.verify(mockedRandom);

        Resource[] resources1 = {};
        Resource[] resources2 = {BRICK};
        Resource[] resources3 = {};
        Resource[] resources4 = {ORE};

        assertTrue(player1.hand.removeResources(resources1));
        assertTrue(player2.hand.removeResources(resources2));
        assertTrue(player3.hand.removeResources(resources3));
        assertTrue(player4.hand.removeResources(resources4));
    }

    @Test
    public void testGetMoreResourcesFromUpgradedSettlements() {
        for (Player p: players) { // clear starter resources for test isolation
            p.hand.clearResources();
        }

        // give resources needed for upgrade
        Resource[] resourcesForUpgrade = new Resource[]{ORE, ORE, ORE, GRAIN, GRAIN};
        player2.hand.addResources(resourcesForUpgrade);
        player4.hand.addResources(resourcesForUpgrade);

        //upgrade player 2
        controller.setCurrentPlayer(player2);
        controller.setState(GameState.UPGRADE_SETTLEMENT);
        assertEquals(SuccessCode.SUCCESS, controller.clickedVertex(13));

        //Upgrade player 4
        controller.setCurrentPlayer(player4);
        controller.setState(GameState.UPGRADE_SETTLEMENT);
        assertEquals(SuccessCode.SUCCESS, controller.clickedVertex(10));



        //Based on beginner locations these are the resources each player should receive on given die roll
        EasyMock.expect(mockedRandom.nextInt(2,13)).andReturn(10);
        EasyMock.replay(mockedRandom);
        controller.rollDice();
        EasyMock.verify(mockedRandom);

        Resource[] resources1 = {};
        Resource[] resources2 = {BRICK, BRICK};
        Resource[] resources3 = {};
        Resource[] resources4 = {ORE, ORE};

        assertTrue(player1.hand.removeResources(resources1));
        assertTrue(player2.hand.removeResources(resources2));
        assertTrue(player3.hand.removeResources(resources3));
        assertTrue(player4.hand.removeResources(resources4));
    }

    @Test
    public void testNoOneGetsResourcesOn7() {
        for (Player p: players) { // clear starter resources for test isolation
            p.hand.clearResources();
        }

        // Based on beginner locations these are the resources each player should receive on given die roll

        EasyMock.expect(mockedRandom.nextInt(2, 13)).andReturn(7);
        EasyMock.replay(mockedRandom);
        controller.rollDice();
        EasyMock.verify(mockedRandom);

        Resource[] resources2 = {BRICK};
        Resource[] resources4 = {ORE};

        // check players didn't receive
        assertFalse(player2.hand.removeResources(resources2));
        assertFalse(player4.hand.removeResources(resources4));
    }
}
