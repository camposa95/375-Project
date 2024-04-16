package domain.graphs;

import data.GameLoader;
import domain.game.GameType;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.player.Player;

import static org.junit.jupiter.api.Assertions.*;

public class GiveLongestRoadCardTest {

    GameboardGraph gameboardGraph;
    Player mockedPlayer1, mockedPlayer2, mockedPlayer3;

    @BeforeEach
    public void setup() {
        gameboardGraph = new GameboardGraph(GameType.Beginner);
        GameLoader.initializeGraphs(gameboardGraph);

        mockedPlayer1 = EasyMock.createMock(Player.class);
        mockedPlayer2 = EasyMock.createMock(Player.class);
        mockedPlayer3 = EasyMock.createMock(Player.class);
    }

    @Test
    public void testGiveLongestRoadCard1() {
        int player1_origin1 = 40;
        int player1_origin2 = 18;

        int player2_origin1 = 21;
        int player2_origin2 = 6;
        
        int player3_origin1 = 34;
        int player3_origin2 = 24;

        // create some basic paths for each of these players where nobody has a 5 long road
        // player 1:
        gameboardGraph.getVertex(player1_origin1).setOwner(mockedPlayer1); // origin 1
        gameboardGraph.getRoad(50).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(56).setOwner(mockedPlayer1);

        gameboardGraph.getVertex(player1_origin2).setOwner(mockedPlayer1); // origin 2
        gameboardGraph.getRoad(25).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(19).setOwner(mockedPlayer1);

        // player 2:
        gameboardGraph.getVertex(player2_origin1).setOwner(mockedPlayer2); // origin 1
        gameboardGraph.getRoad(20).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(13).setOwner(mockedPlayer2);

        gameboardGraph.getVertex(player2_origin2).setOwner(mockedPlayer2); // origin 2
        gameboardGraph.getRoad(5).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(9).setOwner(mockedPlayer2);

        // player 3:
        gameboardGraph.getVertex(player3_origin1).setOwner(mockedPlayer3); // origin 1
        gameboardGraph.getRoad(45).setOwner(mockedPlayer3);
        gameboardGraph.getRoad(36).setOwner(mockedPlayer3);

        gameboardGraph.getVertex(player3_origin2).setOwner(mockedPlayer3); // origin 2
        gameboardGraph.getRoad(31).setOwner(mockedPlayer3);
        gameboardGraph.getRoad(32).setOwner(mockedPlayer3);


        // Expectations
        // no one has the card
        EasyMock.expect(mockedPlayer1.hasLongestRoad()).andReturn(false);
        EasyMock.expect(mockedPlayer2.hasLongestRoad()).andReturn(false);
        EasyMock.expect(mockedPlayer3.hasLongestRoad()).andReturn(false);

        // no one gets the card because they are all short

        // Method call
        EasyMock.replay(mockedPlayer1, mockedPlayer2, mockedPlayer3);
        gameboardGraph.giveLongestRoadCard();
        EasyMock.verify(mockedPlayer1, mockedPlayer2, mockedPlayer3);
    }

    @Test
    public void testGiveLongestRoadCard2() {
        int player1_origin1 = 40;
        int player1_origin2 = 18;

        int player2_origin1 = 21;
        int player2_origin2 = 6;
        
        int player3_origin1 = 34;
        int player3_origin2 = 24;

        // create some basic paths for each of these players
        // player 1: gets a 5 length
        gameboardGraph.getVertex(player1_origin1).setOwner(mockedPlayer1); // origin 1
        gameboardGraph.getRoad(50).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(56).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(42).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(43).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(63).setOwner(mockedPlayer1);


        gameboardGraph.getVertex(player1_origin2).setOwner(mockedPlayer1); // origin 2
        gameboardGraph.getRoad(25).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(19).setOwner(mockedPlayer1);

        // player 2: short road
        gameboardGraph.getVertex(player2_origin1).setOwner(mockedPlayer2); // origin 1
        gameboardGraph.getRoad(20).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(13).setOwner(mockedPlayer2);

        gameboardGraph.getVertex(player2_origin2).setOwner(mockedPlayer2); // origin 2
        gameboardGraph.getRoad(5).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(9).setOwner(mockedPlayer2);

        // player 3: short road
        gameboardGraph.getVertex(player3_origin1).setOwner(mockedPlayer3); // origin 1
        gameboardGraph.getRoad(45).setOwner(mockedPlayer3);
        gameboardGraph.getRoad(36).setOwner(mockedPlayer3);

        gameboardGraph.getVertex(player3_origin2).setOwner(mockedPlayer3); // origin 2
        gameboardGraph.getRoad(31).setOwner(mockedPlayer3);
        gameboardGraph.getRoad(32).setOwner(mockedPlayer3);


        // Expectations
        // no one has the card
        EasyMock.expect(mockedPlayer1.hasLongestRoad()).andReturn(false);
        EasyMock.expect(mockedPlayer2.hasLongestRoad()).andReturn(false);
        EasyMock.expect(mockedPlayer3.hasLongestRoad()).andReturn(false);

        // expect that that player 1 gets the longest road card
        mockedPlayer1.giveLongestRoadCard();


        // Method call
        EasyMock.replay(mockedPlayer1, mockedPlayer2, mockedPlayer3);
        gameboardGraph.giveLongestRoadCard();
        EasyMock.verify(mockedPlayer1, mockedPlayer2, mockedPlayer3);
    }

    @Test
    public void testGiveLongestRoadCard3() {
        int player1_origin1 = 40;
        int player1_origin2 = 18;

        int player2_origin1 = 21;
        int player2_origin2 = 6;
        
        int player3_origin1 = 34;
        int player3_origin2 = 24;

        // create some basic paths for each of these players
        // player 1: gets a 5 length
        gameboardGraph.getVertex(player1_origin1).setOwner(mockedPlayer1); // origin 1
        gameboardGraph.getRoad(50).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(56).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(42).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(43).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(63).setOwner(mockedPlayer1);

        gameboardGraph.getVertex(player1_origin2).setOwner(mockedPlayer1); // origin 2
        gameboardGraph.getRoad(25).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(19).setOwner(mockedPlayer1);

        // player 2: ties with player 1
        gameboardGraph.getVertex(player2_origin1).setOwner(mockedPlayer2); // origin 1
        gameboardGraph.getRoad(20).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(13).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(7).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(28).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(29).setOwner(mockedPlayer2);

        gameboardGraph.getVertex(player2_origin2).setOwner(mockedPlayer2); // origin 2
        gameboardGraph.getRoad(5).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(9).setOwner(mockedPlayer2);

        // player 3: short road
        gameboardGraph.getVertex(player3_origin1).setOwner(mockedPlayer3); // origin 1
        gameboardGraph.getRoad(45).setOwner(mockedPlayer3);
        gameboardGraph.getRoad(36).setOwner(mockedPlayer3);

        gameboardGraph.getVertex(player3_origin2).setOwner(mockedPlayer3); // origin 2
        gameboardGraph.getRoad(31).setOwner(mockedPlayer3);
        gameboardGraph.getRoad(32).setOwner(mockedPlayer3);


        // Expectations
        // expect that player 1 already has the card
        EasyMock.expect(mockedPlayer1.hasLongestRoad()).andReturn(true);
        EasyMock.expect(mockedPlayer2.hasLongestRoad()).andReturn(false);
        EasyMock.expect(mockedPlayer3.hasLongestRoad()).andReturn(false);

        // since player 2 tied they won't change anything


        // Method call
        EasyMock.replay(mockedPlayer1, mockedPlayer2, mockedPlayer3);
        gameboardGraph.giveLongestRoadCard();
        EasyMock.verify(mockedPlayer1, mockedPlayer2, mockedPlayer3);
    }

    @Test
    public void testGiveLongestRoadCard4() {
        int player1_origin1 = 40;
        int player1_origin2 = 18;

        int player2_origin1 = 21;
        int player2_origin2 = 6;
        
        int player3_origin1 = 34;
        int player3_origin2 = 24;

        // create some basic paths for each of these players
        // player 1: gets a 5 length
        gameboardGraph.getVertex(player1_origin1).setOwner(mockedPlayer1); // origin 1
        gameboardGraph.getRoad(50).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(56).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(42).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(43).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(63).setOwner(mockedPlayer1);

        gameboardGraph.getVertex(player1_origin2).setOwner(mockedPlayer1); // origin 2
        gameboardGraph.getRoad(25).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(19).setOwner(mockedPlayer1);

        // player 2: overtakes player 1
        gameboardGraph.getVertex(player2_origin1).setOwner(mockedPlayer2); // origin 1
        gameboardGraph.getRoad(20).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(13).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(7).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(1).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(28).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(29).setOwner(mockedPlayer2);

        gameboardGraph.getVertex(player2_origin2).setOwner(mockedPlayer2); // origin 2
        gameboardGraph.getRoad(5).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(9).setOwner(mockedPlayer2);

        // player 3: short road
        gameboardGraph.getVertex(player3_origin1).setOwner(mockedPlayer3); // origin 1
        gameboardGraph.getRoad(45).setOwner(mockedPlayer3);
        gameboardGraph.getRoad(36).setOwner(mockedPlayer3);

        gameboardGraph.getVertex(player3_origin2).setOwner(mockedPlayer3); // origin 2
        gameboardGraph.getRoad(31).setOwner(mockedPlayer3);
        gameboardGraph.getRoad(32).setOwner(mockedPlayer3);


        // Expectations
        // expect that player 1 already has the card
        EasyMock.expect(mockedPlayer1.hasLongestRoad()).andReturn(true);
        EasyMock.expect(mockedPlayer2.hasLongestRoad()).andReturn(false);
        EasyMock.expect(mockedPlayer3.hasLongestRoad()).andReturn(false);

        // player 2 overtook 1 so expect swap
        mockedPlayer1.removeLongestRoadCard();
        mockedPlayer2.giveLongestRoadCard();


        // Method call
        EasyMock.replay(mockedPlayer1, mockedPlayer2, mockedPlayer3);
        gameboardGraph.giveLongestRoadCard();
        EasyMock.verify(mockedPlayer1, mockedPlayer2, mockedPlayer3);
    }

    @Test
    public void testGiveLongestRoadCard5() {
        int player1_origin1 = 40;
        int player1_origin2 = 18;

        int player2_origin1 = 20;
        int player2_origin2 = 6;
        
        int player3_origin1 = 34;
        int player3_origin2 = 24;

        // create some basic paths for each of these players
        // player 1: gets a 9 length
        gameboardGraph.getRoad(68).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(63).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(56).setOwner(mockedPlayer1);
        gameboardGraph.getVertex(player1_origin1).setOwner(mockedPlayer1); // origin 1
        gameboardGraph.getRoad(50).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(42).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(43).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(35).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(26).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(25).setOwner(mockedPlayer1);
        gameboardGraph.getVertex(player1_origin2).setOwner(mockedPlayer1); // origin 2
        gameboardGraph.getRoad(19).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(11).setOwner(mockedPlayer1);

        // player 2: breaks player 1 road, into 6 and 3
        gameboardGraph.getVertex(player2_origin1).setOwner(mockedPlayer2); // origin 1, blocking player 1
        gameboardGraph.getRoad(27).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(20).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(13).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(7).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(28).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(29).setOwner(mockedPlayer2);

        gameboardGraph.getVertex(player2_origin2).setOwner(mockedPlayer2); // origin 2
        gameboardGraph.getRoad(5).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(9).setOwner(mockedPlayer2);

        // player 3: short road
        gameboardGraph.getVertex(player3_origin1).setOwner(mockedPlayer3); // origin 1
        gameboardGraph.getRoad(45).setOwner(mockedPlayer3);
        gameboardGraph.getRoad(36).setOwner(mockedPlayer3);

        gameboardGraph.getVertex(player3_origin2).setOwner(mockedPlayer3); // origin 2
        gameboardGraph.getRoad(31).setOwner(mockedPlayer3);
        gameboardGraph.getRoad(32).setOwner(mockedPlayer3);


        // Expectations
        // expect that player 1 already has the card
        EasyMock.expect(mockedPlayer1.hasLongestRoad()).andReturn(true);
        EasyMock.expect(mockedPlayer2.hasLongestRoad()).andReturn(false);
        EasyMock.expect(mockedPlayer3.hasLongestRoad()).andReturn(false);

        // no swap after break because player 1 is still longest


        // Method call
        EasyMock.replay(mockedPlayer1, mockedPlayer2, mockedPlayer3);
        gameboardGraph.giveLongestRoadCard();
        EasyMock.verify(mockedPlayer1, mockedPlayer2, mockedPlayer3);
    }

    @Test
    public void testGiveLongestRoadCard6() {
        int player1_origin1 = 40;
        int player1_origin2 = 18;

        int player2_origin1 = 20;
        int player2_origin2 = 6;
        
        int player3_origin1 = 34;
        int player3_origin2 = 24;

        // create some basic paths for each of these players
        // player 1: gets a 6 length
        gameboardGraph.getVertex(player1_origin1).setOwner(mockedPlayer1); // origin 1
        gameboardGraph.getRoad(50).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(42).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(43).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(35).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(26).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(25).setOwner(mockedPlayer1);
        gameboardGraph.getVertex(player1_origin2).setOwner(mockedPlayer1); // origin 2
        gameboardGraph.getRoad(19).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(11).setOwner(mockedPlayer1);

        // player 2: overtakes player 1 with break into 3 and 3
        gameboardGraph.getVertex(player2_origin1).setOwner(mockedPlayer2); // origin 1, blocking player 1
        gameboardGraph.getRoad(27).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(20).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(13).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(7).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(28).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(29).setOwner(mockedPlayer2);

        gameboardGraph.getVertex(player2_origin2).setOwner(mockedPlayer2); // origin 2
        gameboardGraph.getRoad(5).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(9).setOwner(mockedPlayer2);

        // player 3: short road
        gameboardGraph.getVertex(player3_origin1).setOwner(mockedPlayer3); // origin 1
        gameboardGraph.getRoad(45).setOwner(mockedPlayer3);
        gameboardGraph.getRoad(36).setOwner(mockedPlayer3);

        gameboardGraph.getVertex(player3_origin2).setOwner(mockedPlayer3); // origin 2
        gameboardGraph.getRoad(31).setOwner(mockedPlayer3);
        gameboardGraph.getRoad(32).setOwner(mockedPlayer3);


        // Expectations
        // expect that player 1 already has the card
        EasyMock.expect(mockedPlayer1.hasLongestRoad()).andReturn(true);
        EasyMock.expect(mockedPlayer2.hasLongestRoad()).andReturn(false);
        EasyMock.expect(mockedPlayer3.hasLongestRoad()).andReturn(false);

        // player 2 overtook 1 so expect swap
        mockedPlayer1.removeLongestRoadCard();
        mockedPlayer2.giveLongestRoadCard();


        // Method call
        EasyMock.replay(mockedPlayer1, mockedPlayer2, mockedPlayer3);
        gameboardGraph.giveLongestRoadCard();
        EasyMock.verify(mockedPlayer1, mockedPlayer2, mockedPlayer3);
    }

    @Test
    public void testGiveLongestRoadCard7() {
        int player1_origin1 = 40;
        int player1_origin2 = 18;

        int player2_origin1 = 20;
        int player2_origin2 = 6;
        
        int player3_origin1 = 34;
        int player3_origin2 = 24;

        // create some basic paths for each of these players
        // player 1: gets a 9 length
        gameboardGraph.getRoad(63).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(56).setOwner(mockedPlayer1);
        gameboardGraph.getVertex(player1_origin1).setOwner(mockedPlayer1); // origin 1
        gameboardGraph.getRoad(50).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(42).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(43).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(35).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(26).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(25).setOwner(mockedPlayer1);
        gameboardGraph.getVertex(player1_origin2).setOwner(mockedPlayer1); // origin 2
        gameboardGraph.getRoad(19).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(11).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(6).setOwner(mockedPlayer1);

        // player 2: breaks player 1 road with break into 4 and 5
        gameboardGraph.getVertex(player2_origin1).setOwner(mockedPlayer2); // origin 1, blocking player 1
        gameboardGraph.getRoad(27).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(20).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(13).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(7).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(28).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(29).setOwner(mockedPlayer2);

        gameboardGraph.getVertex(player2_origin2).setOwner(mockedPlayer2); // origin 2
        gameboardGraph.getRoad(5).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(9).setOwner(mockedPlayer2);

        // player 3: short road
        gameboardGraph.getVertex(player3_origin1).setOwner(mockedPlayer3); // origin 1
        gameboardGraph.getRoad(45).setOwner(mockedPlayer3);
        gameboardGraph.getRoad(36).setOwner(mockedPlayer3);

        gameboardGraph.getVertex(player3_origin2).setOwner(mockedPlayer3); // origin 2
        gameboardGraph.getRoad(31).setOwner(mockedPlayer3);
        gameboardGraph.getRoad(32).setOwner(mockedPlayer3);


        // Expectations
        // expect that player 1 already has the card
        EasyMock.expect(mockedPlayer1.hasLongestRoad()).andReturn(true);
        EasyMock.expect(mockedPlayer2.hasLongestRoad()).andReturn(false);
        EasyMock.expect(mockedPlayer3.hasLongestRoad()).andReturn(false);

        // no swap after break because player 1 is still tied


        // Method call
        EasyMock.replay(mockedPlayer1, mockedPlayer2, mockedPlayer3);
        gameboardGraph.giveLongestRoadCard();
        EasyMock.verify(mockedPlayer1, mockedPlayer2, mockedPlayer3);
    }

    @Test
    public void testGiveLongestRoadCard8() {
        int player1_origin1 = 40;
        int player1_origin2 = 18;

        int player2_origin1 = 20;
        int player2_origin2 = 6;
        
        int player3_origin1 = 34;
        int player3_origin2 = 24;

        // create some basic paths for each of these players
        // player 1: gets a 6 length
        gameboardGraph.getVertex(player1_origin1).setOwner(mockedPlayer1); // origin 1
        gameboardGraph.getRoad(50).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(42).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(43).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(35).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(26).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(25).setOwner(mockedPlayer1);
        gameboardGraph.getVertex(player1_origin2).setOwner(mockedPlayer1); // origin 2
        gameboardGraph.getRoad(19).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(11).setOwner(mockedPlayer1);

        // player 2: overtakes player 1 with break into 3 and 3
        gameboardGraph.getVertex(player2_origin1).setOwner(mockedPlayer2); // origin 1, blocking player 1
        gameboardGraph.getRoad(27).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(20).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(13).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(7).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(28).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(29).setOwner(mockedPlayer2);

        gameboardGraph.getVertex(player2_origin2).setOwner(mockedPlayer2); // origin 2
        gameboardGraph.getRoad(5).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(9).setOwner(mockedPlayer2);

        // player 3: also tied with player 2
        gameboardGraph.getVertex(player3_origin1).setOwner(mockedPlayer3); // origin 1
        gameboardGraph.getRoad(45).setOwner(mockedPlayer3);
        gameboardGraph.getRoad(36).setOwner(mockedPlayer3);

        gameboardGraph.getRoad(32).setOwner(mockedPlayer3);
        gameboardGraph.getRoad(31).setOwner(mockedPlayer3);
        gameboardGraph.getVertex(player3_origin2).setOwner(mockedPlayer3); // origin 2
        gameboardGraph.getRoad(37).setOwner(mockedPlayer3);
        gameboardGraph.getRoad(47).setOwner(mockedPlayer3);
        gameboardGraph.getRoad(53).setOwner(mockedPlayer3);


        // Expectations
        // expect that player 1 already has the card
        EasyMock.expect(mockedPlayer1.hasLongestRoad()).andReturn(true);
        EasyMock.expect(mockedPlayer2.hasLongestRoad()).andReturn(false);
        EasyMock.expect(mockedPlayer3.hasLongestRoad()).andReturn(false);

        // player 2 overtook 1 with break but now other 2 people are tied so no one has it
        mockedPlayer1.removeLongestRoadCard();


        // Method call
        EasyMock.replay(mockedPlayer1, mockedPlayer2, mockedPlayer3);
        gameboardGraph.giveLongestRoadCard();
        EasyMock.verify(mockedPlayer1, mockedPlayer2, mockedPlayer3);
    }

    @Test
    public void testGiveLongestRoadCard9() {
        int player1_origin1 = 40;
        int player1_origin2 = 18;

        int player2_origin1 = 20;
        int player2_origin2 = 6;
        
        int player3_origin1 = 34;
        int player3_origin2 = 24;

        // create some basic paths for each of these players
        // player 1: gets a 6 length
        gameboardGraph.getVertex(player1_origin1).setOwner(mockedPlayer1); // origin 1
        gameboardGraph.getRoad(50).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(42).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(43).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(35).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(26).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(25).setOwner(mockedPlayer1);
        gameboardGraph.getVertex(player1_origin2).setOwner(mockedPlayer1); // origin 2
        gameboardGraph.getRoad(19).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(11).setOwner(mockedPlayer1);

        // player 2: overtakes player 1 with break into 3 and 3
        gameboardGraph.getVertex(player2_origin1).setOwner(mockedPlayer2); // origin 1, blocking player 1
        gameboardGraph.getRoad(27).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(20).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(28).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(29).setOwner(mockedPlayer2);

        gameboardGraph.getVertex(player2_origin2).setOwner(mockedPlayer2); // origin 2
        gameboardGraph.getRoad(5).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(9).setOwner(mockedPlayer2);

        // player 3: Short road
        gameboardGraph.getVertex(player3_origin1).setOwner(mockedPlayer3); // origin 1
        gameboardGraph.getRoad(45).setOwner(mockedPlayer3);
        gameboardGraph.getRoad(36).setOwner(mockedPlayer3);

        gameboardGraph.getVertex(player3_origin2).setOwner(mockedPlayer3); // origin 2
        gameboardGraph.getRoad(31).setOwner(mockedPlayer3);
        gameboardGraph.getRoad(32).setOwner(mockedPlayer3);


        // Expectations
        // expect that player 1 already has the card
        EasyMock.expect(mockedPlayer1.hasLongestRoad()).andReturn(true);
        EasyMock.expect(mockedPlayer2.hasLongestRoad()).andReturn(false);
        EasyMock.expect(mockedPlayer3.hasLongestRoad()).andReturn(false);

        // player 2 broke player 1 road but no one is 5 or above so no one gets it
        mockedPlayer1.removeLongestRoadCard();


        // Method call
        EasyMock.replay(mockedPlayer1, mockedPlayer2, mockedPlayer3);
        gameboardGraph.giveLongestRoadCard();
        EasyMock.verify(mockedPlayer1, mockedPlayer2, mockedPlayer3);
    }

    @Test
    public void testGiveLongestRoadCard10() {
        int player1_origin1 = 40;
        int player1_origin2 = 18;

        int player2_origin1 = 20;
        int player2_origin2 = 6;
        
        int player3_origin1 = 34;
        int player3_origin2 = 24;

        // create some basic paths for each of these players
        // player 1: gets a 6 length
        gameboardGraph.getVertex(player1_origin1).setOwner(mockedPlayer1); // origin 1
        gameboardGraph.getRoad(50).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(42).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(43).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(35).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(26).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(25).setOwner(mockedPlayer1);
        gameboardGraph.getVertex(player1_origin2).setOwner(mockedPlayer1); // origin 2
        gameboardGraph.getRoad(19).setOwner(mockedPlayer1);
        gameboardGraph.getRoad(11).setOwner(mockedPlayer1);

        // player 2: overtakes player 1 with break into 3 and 3
        gameboardGraph.getVertex(player2_origin1).setOwner(mockedPlayer2); // origin 1, blocking player 1
        gameboardGraph.getRoad(27).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(20).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(13).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(7).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(28).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(29).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(21).setOwner(mockedPlayer2);

        gameboardGraph.getVertex(player2_origin2).setOwner(mockedPlayer2); // origin 2
        gameboardGraph.getRoad(5).setOwner(mockedPlayer2);
        gameboardGraph.getRoad(9).setOwner(mockedPlayer2);

        // player 3: Short road
        gameboardGraph.getRoad(36).setOwner(mockedPlayer3);
        gameboardGraph.getRoad(45).setOwner(mockedPlayer3);
        gameboardGraph.getVertex(player3_origin1).setOwner(mockedPlayer3); // origin 1
        gameboardGraph.getRoad(46).setOwner(mockedPlayer3);
        gameboardGraph.getRoad(37).setOwner(mockedPlayer3);
        gameboardGraph.getVertex(player3_origin2).setOwner(mockedPlayer3); // origin 2
        gameboardGraph.getRoad(31).setOwner(mockedPlayer3);
        gameboardGraph.getRoad(32).setOwner(mockedPlayer3);
        gameboardGraph.getRoad(38).setOwner(mockedPlayer3);


        // Expectations
        // expect that nobody has the card
        EasyMock.expect(mockedPlayer1.hasLongestRoad()).andReturn(false);
        EasyMock.expect(mockedPlayer2.hasLongestRoad()).andReturn(false);
        EasyMock.expect(mockedPlayer3.hasLongestRoad()).andReturn(false);

        // player 3 gets it after the tie after break of player 1
        mockedPlayer3.giveLongestRoadCard();


        // Method call
        EasyMock.replay(mockedPlayer1, mockedPlayer2, mockedPlayer3);
        gameboardGraph.giveLongestRoadCard();
        EasyMock.verify(mockedPlayer1, mockedPlayer2, mockedPlayer3);
    }

    // ------------------------------------------------------------------------
    //
    // Here are my tests for the related setter methods in player
    //
    // ------------------------------------------------------------------------

    @Test
    public void testGiveLongestRoadCard() {
        Player player = new Player(0);
        assertEquals(0, player.getVictoryPoints());
        assertFalse(player.hasLongestRoad());

        player.giveLongestRoadCard();

        assertEquals(2, player.getVictoryPoints());
        assertTrue(player.hasLongestRoad());
    }

    @Test
    public void testRemoveLongestRoadCard() {
        Player player = new Player(0);
        assertEquals(0, player.getVictoryPoints());
        assertFalse(player.hasLongestRoad());

        // give the player the longest road
        player.giveLongestRoadCard();

        // assert that it was given
        assertEquals(2, player.getVictoryPoints());
        assertTrue(player.hasLongestRoad());

        // remove the card
        player.removeLongestRoadCard();

        // assert that it was given
        assertEquals(0, player.getVictoryPoints());
        assertFalse(player.hasLongestRoad());
    }
}
