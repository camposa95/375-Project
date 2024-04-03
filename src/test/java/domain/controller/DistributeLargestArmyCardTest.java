package domain.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.game.Game;
import domain.game.GameType;
import domain.player.Player;

public class DistributeLargestArmyCardTest {

    Game mockedGame;
    Player mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4;
    Player[] players;
    Controller controller;
    @BeforeEach
    public void createMocks() {
        mockedGame = EasyMock.createMock(Game.class);
        GameType gameType = GameType.Advanced;

        mockedPlayer1 = EasyMock.createMock(Player.class);
        mockedPlayer2 = EasyMock.createMock(Player.class);
        mockedPlayer3 = EasyMock.createMock(Player.class);
        mockedPlayer4 = EasyMock.createMock(Player.class);

        players = new Player[]{mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4};

        controller = new Controller(mockedGame, players, gameType);
    }
    
    @Test
    public void testLargestArmyNoOneGetsIt() {
        // no one already has the largest army card
        EasyMock.expect(mockedPlayer1.hasLargestArmy()).andReturn(false);
        EasyMock.expect(mockedPlayer2.hasLargestArmy()).andReturn(false);
        EasyMock.expect(mockedPlayer3.hasLargestArmy()).andReturn(false);
        EasyMock.expect(mockedPlayer4.hasLargestArmy()).andReturn(false);

        // everybody has an army that is less than 3 big
        EasyMock.expect(mockedPlayer1.getNumKnightsPlayed()).andReturn(2);
        EasyMock.expect(mockedPlayer2.getNumKnightsPlayed()).andReturn(1);
        EasyMock.expect(mockedPlayer3.getNumKnightsPlayed()).andReturn(1);
        EasyMock.expect(mockedPlayer4.getNumKnightsPlayed()).andReturn(0);

        // don't expect anybody to get the card


        // method call
        EasyMock.replay(mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4);
        controller.distributeLargestArmyCard();
        EasyMock.verify(mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4);
    }

    @Test
    public void testLargestArmyFirstPlayerGetsIt() {
        // no one already has the largest army card
        EasyMock.expect(mockedPlayer1.hasLargestArmy()).andReturn(false);
        EasyMock.expect(mockedPlayer2.hasLargestArmy()).andReturn(false);
        EasyMock.expect(mockedPlayer3.hasLargestArmy()).andReturn(false);
        EasyMock.expect(mockedPlayer4.hasLargestArmy()).andReturn(false);

        // everybody has an army that is less than 3 big
        EasyMock.expect(mockedPlayer1.getNumKnightsPlayed()).andReturn(2);
        EasyMock.expect(mockedPlayer2.getNumKnightsPlayed()).andReturn(1);
        EasyMock.expect(mockedPlayer3.getNumKnightsPlayed()).andReturn(3);
        EasyMock.expect(mockedPlayer4.getNumKnightsPlayed()).andReturn(0);

        // player 3 gets the card for the first
        mockedPlayer3.giveLargestArmyCard();


        // method call
        EasyMock.replay(mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4);
        controller.distributeLargestArmyCard();
        EasyMock.verify(mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4);
    }

    @Test
    public void testLargestArmyFirstPlayerGetsTied() {
        // no one already has the largest army card
        EasyMock.expect(mockedPlayer1.hasLargestArmy()).andReturn(false);
        EasyMock.expect(mockedPlayer2.hasLargestArmy()).andReturn(false);
        EasyMock.expect(mockedPlayer3.hasLargestArmy()).andReturn(true);
        EasyMock.expect(mockedPlayer4.hasLargestArmy()).andReturn(false);

        // everybody has an army that is less than 3 big
        EasyMock.expect(mockedPlayer1.getNumKnightsPlayed()).andReturn(2);
        EasyMock.expect(mockedPlayer2.getNumKnightsPlayed()).andReturn(3);
        EasyMock.expect(mockedPlayer3.getNumKnightsPlayed()).andReturn(3);
        EasyMock.expect(mockedPlayer4.getNumKnightsPlayed()).andReturn(0);

        // nothing changes


        // method call
        EasyMock.replay(mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4);
        controller.distributeLargestArmyCard();
        EasyMock.verify(mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4);
    }

    @Test
    public void testLargestArmyFirstPlayerGetsOvertaken() {
        // no one already has the largest army card
        EasyMock.expect(mockedPlayer1.hasLargestArmy()).andReturn(false);
        EasyMock.expect(mockedPlayer2.hasLargestArmy()).andReturn(false);
        EasyMock.expect(mockedPlayer3.hasLargestArmy()).andReturn(true);
        EasyMock.expect(mockedPlayer4.hasLargestArmy()).andReturn(false);

        // everybody has an army that is less than 3 big
        EasyMock.expect(mockedPlayer1.getNumKnightsPlayed()).andReturn(2);
        EasyMock.expect(mockedPlayer2.getNumKnightsPlayed()).andReturn(4);
        EasyMock.expect(mockedPlayer3.getNumKnightsPlayed()).andReturn(3);
        EasyMock.expect(mockedPlayer4.getNumKnightsPlayed()).andReturn(0);

        // player 3 gets the largest army taken by player 2
        mockedPlayer2.giveLargestArmyCard();
        mockedPlayer3.removeLargestArmyCard();


        // method call
        EasyMock.replay(mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4);
        controller.distributeLargestArmyCard();
        EasyMock.verify(mockedPlayer1, mockedPlayer2, mockedPlayer3, mockedPlayer4);
    }

    // ----------------------------------------------------------
    //
    // Test for the largest army setters and getters in player
    //
    // ----------------------------------------------------------

    @Test
    public void testGiveLargestArmyCard() {
        Player player = new Player(0);

        // before giving the card
        assertEquals(0, player.getVictoryPoints());
        assertFalse(player.hasLargestArmy());


        // give the player the card
        player.giveLargestArmyCard();


        // after the give
        assertEquals(2, player.getVictoryPoints());
        assertTrue(player.hasLargestArmy());
    }

    @Test
    public void testRemoveLargestArmyCard() {
        Player player = new Player(0);

        // before giving the card
        assertEquals(0, player.getVictoryPoints());
        assertFalse(player.hasLargestArmy());


        // give the player the card
        player.giveLargestArmyCard();


        // after the give
        assertEquals(2, player.getVictoryPoints());
        assertTrue(player.hasLargestArmy());


        // remove the card from the player
        player.removeLargestArmyCard();


        // after the remove
        assertEquals(0, player.getVictoryPoints());
        assertFalse(player.hasLargestArmy());
    }

    @Test
    public void testIncrementNumKnights() {
        Player player = new Player(0);

        // before incrementing
        assertEquals(0, player.getNumKnightsPlayed());

        // increment the knights
        player.incrementNumKnights();

        // after incrementing
        assertEquals(1, player.getNumKnightsPlayed());
    }
}
