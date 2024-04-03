package domain.game;

import domain.bank.Resource;
import domain.gameboard.GameBoard;
import domain.graphs.RoadGraph;
import domain.graphs.VertexGraph;
import domain.player.Hand;
import domain.player.Player;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class DropCardsTest {

    GameBoard gameBoard;
    VertexGraph vertexGraph;
    RoadGraph mockedRoadGraph;
    Player mockedPlayer1, mockedPlayer2, mockedPlayer3;
    Hand mockedHand1, mockedHand2, mockedHand3;
    Game game;

    @BeforeEach
    public void setup() {
        gameBoard =  EasyMock.createStrictMock(GameBoard.class);
        vertexGraph = EasyMock.createStrictMock(VertexGraph.class);
        mockedRoadGraph = EasyMock.createStrictMock(RoadGraph.class);

        mockedPlayer1 = EasyMock.createStrictMock(Player.class);
        mockedPlayer2 = EasyMock.createStrictMock(Player.class);
        mockedPlayer3 = EasyMock.createStrictMock(Player.class);

        mockedHand1 = EasyMock.createStrictMock(Hand.class);
        mockedHand2 = EasyMock.createStrictMock(Hand.class);
        mockedHand3 = EasyMock.createStrictMock(Hand.class);

        mockedPlayer1.hand = mockedHand1;
        mockedPlayer2.hand = mockedHand2;
        mockedPlayer3.hand = mockedHand3;

        game = new Game(gameBoard, vertexGraph, mockedRoadGraph, null, null);
    }

    @Test
    public void testDropCards_empty() {

        //Create Hashmap
        HashMap<Player, Resource[]> testHash = new HashMap<>();

        //Replay
        EasyMock.replay(gameBoard, vertexGraph, mockedRoadGraph);

        //Call method
        try{
            game.dropCards(testHash);
        } catch( Exception e) {
            fail();
        }

        //Verify
        EasyMock.verify(gameBoard, vertexGraph, mockedRoadGraph);
    }

    @Test
    public void testDropCards_1player_nothing() {

        //Replay
        EasyMock.replay(gameBoard, vertexGraph, mockedRoadGraph,mockedPlayer1);

        //Create Hashmap

        Resource[] resources = new Resource[0];

        HashMap<Player,Resource[]> testHash = new HashMap<>();
        testHash.put(mockedPlayer1, resources);

        //Call method
        try{
            game.dropCards(testHash);
        } catch( Exception e) {
            //Make sure nothing is thrown
            e.printStackTrace();
            fail();
        }

        //Verify
        EasyMock.verify(gameBoard, vertexGraph, mockedRoadGraph,mockedPlayer1);
    }

    @Test
    public void testDropCards_1player_1card() {
        //Expect
        Resource[] resources = {Resource.WOOL};
        EasyMock.expect(mockedPlayer1.hand.removeResources(resources)).andReturn(true);
        //Replay
        EasyMock.replay(gameBoard, vertexGraph, mockedRoadGraph,mockedPlayer1,mockedHand1);

        //Create Hashmap
        HashMap<Player,Resource[]> testHash = new HashMap<>();
        testHash.put(mockedPlayer1, resources);

        //Call method
        try{
            game.dropCards(testHash);
        } catch( Exception e) {
            //Make sure nothing is thrown
            e.printStackTrace();
            fail();
        }

        //Verify
        EasyMock.verify(gameBoard, vertexGraph, mockedRoadGraph,mockedPlayer1,mockedHand1);
    }

    @Test
    public void testDropCards_ManyPlayer_MixCard() {
        //Setup resources
        Resource[] resources1 = new Resource[0];
        Resource[] resources2 = {Resource.WOOL,Resource.BRICK, Resource.GRAIN, Resource.LUMBER, Resource.WOOL};
        Resource[] resources3 = new Resource[110];
        for(int i = 0; i<110; i+=5){
            resources3[i] = Resource.WOOL;
            resources3[i+1] = Resource.BRICK;
            resources3[i+2] =  Resource.GRAIN;
            resources3[i+3] = Resource.LUMBER;
            resources3[i+4] = Resource.WOOL;
        }
        //Expect
        EasyMock.expect(mockedPlayer2.hand.removeResources(resources2)).andReturn(true);
        EasyMock.expect(mockedPlayer3.hand.removeResources(resources3)).andReturn(true);
        //Replay
        EasyMock.replay(gameBoard, vertexGraph, mockedRoadGraph,mockedPlayer1,mockedHand1,mockedPlayer2,mockedHand2,mockedPlayer3,mockedHand3);

        //Create Hashmap
        HashMap<Player,Resource[]> testHash = new HashMap<>();
        testHash.put(mockedPlayer1, resources1);
        testHash.put(mockedPlayer2, resources2);
        testHash.put(mockedPlayer3, resources3);

        //Call method
        try{
            game.dropCards(testHash);
        } catch( Exception e) {
            //Make sure nothing is thrown
            e.printStackTrace();
            fail();
        }

        //Verify
        EasyMock.verify(gameBoard, vertexGraph, mockedRoadGraph,mockedPlayer1,mockedHand1,mockedPlayer2,mockedHand2,mockedPlayer3,mockedHand3);
    }

    @Test
    void testDropCards_1player_Invalid() {

        //Expect
        Resource[] resources = {Resource.WOOL};
        EasyMock.expect(mockedPlayer1.hand.removeResources(resources)).andReturn(false);
        //Replay
        EasyMock.replay(gameBoard, vertexGraph, mockedRoadGraph,mockedPlayer1,mockedHand1);

        //Create Hashmap
        HashMap<Player,Resource[]> testHash = new HashMap<>();
        testHash.put(mockedPlayer1, resources);

        //Call method
        assertThrows(IllegalArgumentException.class, ()-> game.dropCards(testHash),"DropCards was called on player with not enough cards - synchronization issue");

        //Verify
        EasyMock.verify(gameBoard, vertexGraph, mockedRoadGraph,mockedPlayer1,mockedHand1);
    }
}
