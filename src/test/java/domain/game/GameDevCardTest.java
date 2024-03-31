package domain.game;

import domain.bank.Bank;
import domain.bank.Resource;
import domain.devcarddeck.DevCard;
import domain.devcarddeck.DevelopmentCardDeck;
import domain.devcarddeck.EmptyDevCardDeckException;
import domain.game.CardNotPlayableException;
import domain.game.Game;
import domain.game.NotEnoughResourcesException;
import domain.gameboard.GameBoard;
import domain.player.Hand;
import domain.player.Player;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import domain.graphs.RoadGraph;
import domain.graphs.VertexGraph;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GameDevCardTest {
    @Test
    public void testBuyCard_NoResources_NoCard_ExpectError(){
        VertexGraph mockVertexGraph =  EasyMock.createMock(VertexGraph.class);
        GameBoard mockGameBoard =  EasyMock.createMock(GameBoard.class);
        RoadGraph mockRoadGraph =  EasyMock.createMock(RoadGraph.class);
        DevelopmentCardDeck deck = new DevelopmentCardDeck();
        Player mockplayer = EasyMock.createMock(Player.class);
        
        Game game = new Game(mockGameBoard, mockVertexGraph, mockRoadGraph, deck,  null);
       
        EasyMock.replay(mockGameBoard,mockRoadGraph,mockVertexGraph,mockplayer);
       
        emptyDeck(deck);
        int deckBefore = deck.getDeck().size();
        //int handBefore = player.hand.getDevCards();
        
        assertThrows(EmptyDevCardDeckException.class,()->{game.buyDevCard(mockplayer);});

        int deckAfter = deck.getDeck().size();
        //int handAfter = player.hand.getDevCards();

        assertEquals(deckBefore, deckAfter);
        //assertEquals(handBefore, handAfter);

        EasyMock.verify(mockGameBoard,mockRoadGraph,mockVertexGraph,mockplayer);
    }
    @Test
    public void testBuyCard_Resources_NoCard_ExpectError(){
        VertexGraph mockVertexGraph =  EasyMock.createMock(VertexGraph.class);
        GameBoard mockGameBoard =  EasyMock.createMock(GameBoard.class);
        RoadGraph mockRoadGraph =  EasyMock.createMock(RoadGraph.class);
        DevelopmentCardDeck deck = new DevelopmentCardDeck();
        Player mockplayer = EasyMock.createMock(Player.class);
      
        Game game = new Game(mockGameBoard, mockVertexGraph, mockRoadGraph, deck, null);
        
        EasyMock.replay(mockGameBoard,mockRoadGraph,mockVertexGraph,mockplayer);

        emptyDeck(deck);
        int deckBefore = deck.getDeck().size();
        
        assertThrows(EmptyDevCardDeckException.class,()->{game.buyDevCard(mockplayer);});        

        int deckAfter = deck.getDeck().size();


        assertEquals(deckBefore, deckAfter);
        EasyMock.verify(mockGameBoard,mockRoadGraph,mockVertexGraph,mockplayer);
    }
    @Test
    public void testBuyCard_NoResources_Card_ExpectError(){
        VertexGraph mockVertexGraph =  EasyMock.createMock(VertexGraph.class);
        GameBoard mockGameBoard =  EasyMock.createMock(GameBoard.class);
        RoadGraph mockRoadGraph =  EasyMock.createMock(RoadGraph.class);
        DevelopmentCardDeck deck = new DevelopmentCardDeck();
        Player mockplayer = EasyMock.createMock(Player.class);

        Game game = new Game(mockGameBoard, mockVertexGraph, mockRoadGraph, deck, null);
        
        emptyDeck(deck);
        deck.returnToDeck(DevCard.KNIGHT);

        EasyMock.expect(mockplayer.purchaseDevCard(DevCard.KNIGHT)).andReturn(false);
        EasyMock.replay(mockGameBoard,mockRoadGraph,mockVertexGraph,mockplayer);

        int deckBefore = deck.getDeck().size();
        
        assertThrows(NotEnoughResourcesException.class,()->{game.buyDevCard(mockplayer);});

        int deckAfter = deck.getDeck().size();


        assertEquals(deckBefore, deckAfter);
        EasyMock.verify(mockGameBoard,mockRoadGraph,mockVertexGraph,mockplayer);
    }
    @Test
    public void testBuyCard_Resources_Card(){
        VertexGraph mockVertexGraph =  EasyMock.createMock(VertexGraph.class);
        GameBoard mockGameBoard =  EasyMock.createMock(GameBoard.class);
        RoadGraph mockRoadGraph =  EasyMock.createMock(RoadGraph.class);
        DevelopmentCardDeck deck = new DevelopmentCardDeck();
        Player mockplayer = EasyMock.createMock(Player.class);

        Game game = new Game(mockGameBoard, mockVertexGraph, mockRoadGraph, deck, null);
        
        emptyDeck(deck);
        deck.returnToDeck(DevCard.KNIGHT);

        EasyMock.expect(mockplayer.purchaseDevCard(DevCard.KNIGHT)).andReturn(true);
        EasyMock.replay(mockGameBoard,mockRoadGraph,mockVertexGraph,mockplayer);

        int deckBefore = deck.getDeck().size();
        
        try {
            game.buyDevCard(mockplayer);
        } catch (NotEnoughResourcesException | EmptyDevCardDeckException e) {
            assertFalse(true);
            e.printStackTrace();
        }    

        int deckAfter = deck.getDeck().size();


        assertEquals(--deckBefore, deckAfter);
        EasyMock.verify(mockGameBoard,mockRoadGraph,mockVertexGraph,mockplayer);
    }
    @Test
    public void testPlayMonopoly_GetCards(){
        VertexGraph mockVertexGraph =  EasyMock.createMock(VertexGraph.class);
        GameBoard mockGameBoard =  EasyMock.createMock(GameBoard.class);
        RoadGraph mockRoadGraph =  EasyMock.createMock(RoadGraph.class);

        Player mockplayer = EasyMock.createMock(Player.class);
        Player mockRobbed1 = EasyMock.createMock(Player.class);
        Player mockRobbed2 = EasyMock.createMock(Player.class);
        Hand mockHand = EasyMock.createMock(Hand.class);

        mockplayer.hand = mockHand;
        mockRobbed1.hand = mockHand;
        mockRobbed2.hand = mockHand;

        Player[] robbed = {mockRobbed1,mockRobbed2};

        Game game = new Game(mockGameBoard, mockVertexGraph, mockRoadGraph, null, null);

        //Resource to take
        Resource resource = Resource.BRICK;


        //Expect
        EasyMock.expect(mockplayer.useDevCard(DevCard.MONOPOLY)).andReturn(true);
        EasyMock.expect(mockRobbed1.hand.getResourceCardAmount(Resource.BRICK)).andReturn(5);
        EasyMock.expect(mockRobbed2.hand.getResourceCardAmount(Resource.BRICK)).andReturn(1);
        EasyMock.expect(mockRobbed1.hand.removeResource(Resource.BRICK, 5)).andReturn(true);
        EasyMock.expect(mockRobbed2.hand.removeResource(Resource.BRICK, 1)).andReturn(true);
        EasyMock.expect(mockplayer.hand.addResource(resource, 5)).andReturn(true);
        EasyMock.expect(mockplayer.hand.addResource(resource, 1)).andReturn(true);
      
        EasyMock.replay(mockGameBoard,mockRoadGraph,mockVertexGraph,mockplayer,mockRobbed1,mockRobbed2,mockHand);
        
        try {
            game.playMonopoly(mockplayer,robbed,resource);
        } catch (CardNotPlayableException e) {
            assertFalse(true);
            e.printStackTrace();
        } 

        EasyMock.verify(mockGameBoard,mockRoadGraph,mockVertexGraph,mockplayer,mockRobbed1,mockRobbed2,mockHand);
    }
    @Test
    public void testPlayMonopoly_GetSome(){
        VertexGraph mockVertexGraph =  EasyMock.createMock(VertexGraph.class);
        GameBoard mockGameBoard =  EasyMock.createMock(GameBoard.class);
        RoadGraph mockRoadGraph =  EasyMock.createMock(RoadGraph.class);

        Player mockplayer = EasyMock.createMock(Player.class);
        Player mockRobbed1 = EasyMock.createMock(Player.class);
        Player mockRobbed2 = EasyMock.createMock(Player.class);
        Hand mockHand = EasyMock.createMock(Hand.class);

        mockplayer.hand = mockHand;
        mockRobbed1.hand = mockHand;
        mockRobbed2.hand = mockHand;

        Player[] robbed = {mockRobbed1,mockRobbed2};

        Game game = new Game(mockGameBoard, mockVertexGraph, mockRoadGraph, null, null);

        //Resource to take
        Resource resource = Resource.BRICK;


        //Expect
        EasyMock.expect(mockplayer.useDevCard(DevCard.MONOPOLY)).andReturn(true);
        EasyMock.expect(mockRobbed1.hand.getResourceCardAmount(Resource.BRICK)).andReturn(5);
        EasyMock.expect(mockRobbed2.hand.getResourceCardAmount(Resource.BRICK)).andReturn(0);
        EasyMock.expect(mockRobbed1.hand.removeResource(Resource.BRICK, 5)).andReturn(true);
        EasyMock.expect(mockplayer.hand.addResource(resource, 5)).andReturn(true);
       
        EasyMock.replay(mockGameBoard,mockRoadGraph,mockVertexGraph,mockplayer,mockRobbed1,mockRobbed2,mockHand);
        
        try {
            game.playMonopoly(mockplayer,robbed,resource);
        } catch (CardNotPlayableException e) {
            assertFalse(true);
            e.printStackTrace();
        } 

        EasyMock.verify(mockGameBoard,mockRoadGraph,mockVertexGraph,mockplayer,mockRobbed1,mockRobbed2,mockHand);
    }
    @Test
    public void testPlayMonopoly_Error(){
        VertexGraph mockVertexGraph =  EasyMock.createMock(VertexGraph.class);
        GameBoard mockGameBoard =  EasyMock.createMock(GameBoard.class);
        RoadGraph mockRoadGraph =  EasyMock.createMock(RoadGraph.class);

        Player mockplayer = EasyMock.createMock(Player.class);
        Player mockRobbed1 = EasyMock.createMock(Player.class);
        Player mockRobbed2 = EasyMock.createMock(Player.class);
        Hand mockHand = EasyMock.createMock(Hand.class);

        mockplayer.hand = mockHand;
        mockRobbed1.hand = mockHand;
        mockRobbed2.hand = mockHand;

        Player[] robbed = {mockRobbed1,mockRobbed2};

        Game game = new Game(mockGameBoard, mockVertexGraph, mockRoadGraph, null, null);

        //Resource to take
        Resource resource = Resource.BRICK;


        //Expect
        EasyMock.expect(mockplayer.useDevCard(DevCard.MONOPOLY)).andReturn(false);
       
        EasyMock.replay(mockGameBoard,mockRoadGraph,mockVertexGraph,mockplayer,mockRobbed1,mockRobbed2,mockHand);
        
        assertThrows(CardNotPlayableException.class,()->{game.playMonopoly(mockplayer,robbed,resource);});
        
        EasyMock.verify(mockGameBoard,mockRoadGraph,mockVertexGraph,mockplayer,mockRobbed1,mockRobbed2,mockHand);
   
    } 
    @Test
    public void testPlayYearOfPlenty_NoDev(){
        VertexGraph mockVertexGraph =  EasyMock.createMock(VertexGraph.class);
        GameBoard mockGameBoard =  EasyMock.createMock(GameBoard.class);
        RoadGraph mockRoadGraph =  EasyMock.createMock(RoadGraph.class);
        Player mockplayer = EasyMock.createMock(Player.class);
        Hand mockHand = EasyMock.createMock(Hand.class);

        mockplayer.hand = mockHand;

        Game game = new Game(mockGameBoard, mockVertexGraph, mockRoadGraph, null, null);

        //Resources to take
        Resource r1 = Resource.BRICK;
        Resource r2 = Resource.WOOL;

        //Expect
        EasyMock.expect(mockplayer.useDevCard(DevCard.PLENTY)).andReturn(false);
      
        EasyMock.replay(mockGameBoard,mockRoadGraph,mockVertexGraph,mockplayer,mockHand);
        
        assertThrows(CardNotPlayableException.class,()->{game.playYearOfPlenty(mockplayer,r1,r2);});

        EasyMock.verify(mockGameBoard,mockRoadGraph,mockVertexGraph,mockplayer,mockHand);
    }
    @Test
    public void testPlayYearOfPlenty_NoResources(){
        VertexGraph mockVertexGraph =  EasyMock.createMock(VertexGraph.class);
        GameBoard mockGameBoard =  EasyMock.createMock(GameBoard.class);
        RoadGraph mockRoadGraph =  EasyMock.createMock(RoadGraph.class);
        Player mockplayer = EasyMock.createMock(Player.class);
        Hand mockHand = EasyMock.createMock(Hand.class);
        Bank bank = new Bank();
        mockplayer.hand = mockHand;

        Game game = new Game(mockGameBoard, mockVertexGraph, mockRoadGraph, null, bank);

        //Resources to take
        Resource r1 = Resource.BRICK;
        Resource r2 = Resource.WOOL;
        //Manually empty bank
        bank.removeResource(r1,bank.getResourceAmount(r1));
        bank.removeResource(r2,bank.getResourceAmount(r2));

        //Expect
        EasyMock.expect(mockplayer.useDevCard(DevCard.PLENTY)).andReturn(true);
        EasyMock.expect(mockplayer.hand.addDevelopmentCard(DevCard.PLENTY)).andReturn(true);
        
      
        EasyMock.replay(mockGameBoard,mockRoadGraph,mockVertexGraph,mockplayer,mockHand);
        
        assertThrows(NotEnoughResourcesException.class,()->{game.playYearOfPlenty(mockplayer,r1,r2);});

        EasyMock.verify(mockGameBoard,mockRoadGraph,mockVertexGraph,mockplayer,mockHand);
    }
    @Test
    public void testPlayYearOfPlenty_NoR1(){
        VertexGraph mockVertexGraph =  EasyMock.createMock(VertexGraph.class);
        GameBoard mockGameBoard =  EasyMock.createMock(GameBoard.class);
        RoadGraph mockRoadGraph =  EasyMock.createMock(RoadGraph.class);
        Player mockplayer = EasyMock.createMock(Player.class);
        Hand mockHand = EasyMock.createMock(Hand.class);
        
        Bank bank = new Bank();

        mockplayer.hand = mockHand;

        Game game = new Game(mockGameBoard, mockVertexGraph, mockRoadGraph, null,  bank);

        //Resources to take
        Resource r1 = Resource.BRICK;
        Resource r2 = Resource.WOOL;

        int numToRemove = bank.getResourceAmount(r1);
        assertEquals(19, numToRemove);


        //Manually empty bank
        bank.removeResource(r1, numToRemove);
        

        //Expect
        EasyMock.expect(mockplayer.useDevCard(DevCard.PLENTY)).andReturn(true);
        EasyMock.expect(mockplayer.hand.addDevelopmentCard(DevCard.PLENTY)).andReturn(true);
        
      
        EasyMock.replay(mockGameBoard,mockRoadGraph,mockVertexGraph,mockplayer,mockHand);
        
        assertThrows(NotEnoughResourcesException.class,()->{game.playYearOfPlenty(mockplayer,r1,r2);});

        EasyMock.verify(mockGameBoard,mockRoadGraph,mockVertexGraph,mockplayer,mockHand);
    }
    @Test
    public void testPlayYearOfPlenty_NoR2() {
        VertexGraph mockVertexGraph =  EasyMock.createMock(VertexGraph.class);
        GameBoard mockGameBoard =  EasyMock.createMock(GameBoard.class);
        RoadGraph mockRoadGraph =  EasyMock.createMock(RoadGraph.class);
        Player mockplayer = EasyMock.createMock(Player.class);
        Hand mockHand = EasyMock.createMock(Hand.class);
        mockplayer.hand = mockHand;
        Bank bank = new Bank();

        Game game = new Game(mockGameBoard, mockVertexGraph, mockRoadGraph, null, bank);

        //Resources to take
        Resource r1 = Resource.BRICK;
        Resource r2 = Resource.WOOL;
        //Manually empty bank
        bank.removeResource(r2, bank.getResourceAmount(r2));

        //Expect
        EasyMock.expect(mockplayer.useDevCard(DevCard.PLENTY)).andReturn(true);
        EasyMock.expect(mockplayer.hand.addDevelopmentCard(DevCard.PLENTY)).andReturn(true);
      
        EasyMock.replay(mockGameBoard,mockRoadGraph,mockVertexGraph,mockplayer,mockHand);
        
        assertThrows(NotEnoughResourcesException.class,()->{game.playYearOfPlenty(mockplayer,r1,r2);});

        EasyMock.verify(mockGameBoard,mockRoadGraph,mockVertexGraph,mockplayer,mockHand);
    }
    @Test
    public void testPlayYearOfPlenty_GetResources(){
        VertexGraph mockVertexGraph =  EasyMock.createMock(VertexGraph.class);
        GameBoard mockGameBoard =  EasyMock.createMock(GameBoard.class);
        RoadGraph mockRoadGraph =  EasyMock.createMock(RoadGraph.class);
        Player mockplayer = EasyMock.createMock(Player.class);
        Hand mockHand = EasyMock.createMock(Hand.class);
        mockplayer.hand = mockHand;

        Bank bank = new Bank();
        Game game = new Game(mockGameBoard, mockVertexGraph, mockRoadGraph, null, bank);
        

        //Resources to take
        Resource r1 = Resource.BRICK;
        Resource r2 = Resource.WOOL;
        Resource[] resources = {r1,r2};

        //Expect
        EasyMock.expect(mockplayer.useDevCard(DevCard.PLENTY)).andReturn(true);
        EasyMock.expect(mockplayer.hand.addResources(resources)).andReturn(true);
        EasyMock.replay(mockGameBoard,mockRoadGraph,mockVertexGraph,mockplayer,mockHand);
        
        try{
            game.playYearOfPlenty(mockplayer,r1,r2);
        }
        catch(Exception e) {
            fail();
            e.printStackTrace();
        }

        EasyMock.verify(mockGameBoard,mockRoadGraph,mockVertexGraph,mockplayer,mockHand);
    }

    private void emptyDeck(DevelopmentCardDeck deck) {
        while (true) {
            try {
                deck.draw();
            } catch (EmptyDevCardDeckException e) {
                return;
            }
        }
    }
}