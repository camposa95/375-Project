package domain.game;

import domain.bank.Bank;
import domain.bank.Resource;
import domain.devcarddeck.DevCard;
import domain.devcarddeck.DevelopmentCardDeck;
import domain.devcarddeck.EmptyDevCardDeckException;
import domain.gameboard.GameBoard;
import domain.player.Hand;
import domain.player.Player;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.graphs.GameboardGraph;

import static org.junit.jupiter.api.Assertions.*;

public class GameDevCardTest {

    GameboardGraph mockGameboardGraph;
    GameBoard mockGameBoard;

    Player mockplayer, mockRobbed1, mockRobbed2;
    Hand mockHand;
    Player[] robbed;
    Bank bank;
    DevelopmentCardDeck deck;
    Game game;

    @BeforeEach
    public void setupMocks() {
        mockGameboardGraph =  EasyMock.createMock(GameboardGraph.class);
        mockGameBoard =  EasyMock.createMock(GameBoard.class);

        mockplayer = EasyMock.createMock(Player.class);
        mockRobbed1 = EasyMock.createNiceMock(Player.class);
        mockRobbed2 = EasyMock.createNiceMock(Player.class);
        mockHand = EasyMock.createMock(Hand.class);

        mockplayer.hand = mockHand;
        mockRobbed1.hand = mockHand;
        mockRobbed2.hand = mockHand;

        robbed = new Player[]{mockRobbed1, mockRobbed2};

        bank = new Bank();
        deck = new DevelopmentCardDeck();
        game = new Game(mockGameBoard, mockGameboardGraph, deck, bank);
    }

    private void replayMocks() {
        EasyMock.replay(mockGameBoard, mockGameboardGraph,mockplayer, mockRobbed1, mockRobbed2, mockHand);
    }

    private void verifyMocks() {
        EasyMock.verify(mockGameBoard, mockGameboardGraph,mockplayer, mockRobbed1, mockRobbed2, mockHand);
    }

    @Test
    public void testBuyCard_NoResources_NoCard_ExpectError() {
        replayMocks();
        emptyDeck(deck);
        int deckBefore = deck.getDeck().size();
        //int handBefore = player.hand.getDevCards();
        
        assertThrows(EmptyDevCardDeckException.class,()-> game.buyDevCard(mockplayer));

        int deckAfter = deck.getDeck().size();
        //int handAfter = player.hand.getDevCards();

        assertEquals(deckBefore, deckAfter);
        //assertEquals(handBefore, handAfter);

        verifyMocks();
    }

    @Test
    public void testBuyCard_Resources_NoCard_ExpectError() {
        replayMocks();
        emptyDeck(deck);
        int deckBefore = deck.getDeck().size();
        
        assertThrows(EmptyDevCardDeckException.class,()-> game.buyDevCard(mockplayer));

        int deckAfter = deck.getDeck().size();


        assertEquals(deckBefore, deckAfter);
        verifyMocks();
    }

    @Test
    public void testBuyCard_NoResources_Card_ExpectError() {
        emptyDeck(deck);
        deck.returnToDeck(DevCard.KNIGHT);

        EasyMock.expect(mockplayer.purchaseDevCard(DevCard.KNIGHT)).andReturn(false);
        replayMocks();

        int deckBefore = deck.getDeck().size();
        
        assertThrows(NotEnoughResourcesException.class,()-> game.buyDevCard(mockplayer));

        int deckAfter = deck.getDeck().size();


        assertEquals(deckBefore, deckAfter);
        verifyMocks();
    }

    @Test
    public void testBuyCard_Resources_Card() {
        emptyDeck(deck);
        deck.returnToDeck(DevCard.KNIGHT);

        EasyMock.expect(mockplayer.purchaseDevCard(DevCard.KNIGHT)).andReturn(true);
        replayMocks();

        int deckBefore = deck.getDeck().size();
        
        try {
            game.buyDevCard(mockplayer);
        } catch (NotEnoughResourcesException | EmptyDevCardDeckException e) {
            fail();
            e.printStackTrace();
        }    

        int deckAfter = deck.getDeck().size();


        assertEquals(--deckBefore, deckAfter);
        verifyMocks();
    }

    @Test
    public void testPlayMonopoly_GetCards() {
        //Resource to take
        Resource resource = Resource.BRICK;

        //Expect
        EasyMock.expect(mockplayer.useDevCard(DevCard.MONOPOLY)).andReturn(true);
        EasyMock.expect(mockRobbed1.hand.getResourceCount(Resource.BRICK)).andReturn(5);
        EasyMock.expect(mockRobbed2.hand.getResourceCount(Resource.BRICK)).andReturn(1);
        EasyMock.expect(mockRobbed1.hand.removeResource(Resource.BRICK, 5)).andReturn(true);
        EasyMock.expect(mockRobbed2.hand.removeResource(Resource.BRICK, 1)).andReturn(true);
        EasyMock.expect(mockplayer.hand.addResource(resource, 5)).andReturn(true);
        EasyMock.expect(mockplayer.hand.addResource(resource, 1)).andReturn(true);

        replayMocks();

        try {
            game.playMonopoly(mockplayer,robbed,resource);
        } catch (CardNotPlayableException e) {
            fail();
            e.printStackTrace();
        }

        verifyMocks();
    }

    @Test
    public void testPlayMonopoly_GetSome() {
        //Resource to take
        Resource resource = Resource.BRICK;

        //Expect
        EasyMock.expect(mockplayer.useDevCard(DevCard.MONOPOLY)).andReturn(true);
        EasyMock.expect(mockRobbed1.hand.getResourceCount(Resource.BRICK)).andReturn(5);
        EasyMock.expect(mockRobbed2.hand.getResourceCount(Resource.BRICK)).andReturn(0);
        EasyMock.expect(mockRobbed1.hand.removeResource(Resource.BRICK, 5)).andReturn(true);
        EasyMock.expect(mockplayer.hand.addResource(resource, 5)).andReturn(true);

        replayMocks();

        try {
            game.playMonopoly(mockplayer,robbed,resource);
        } catch (CardNotPlayableException e) {
            fail();
            e.printStackTrace();
        }

        verifyMocks();
    }

    @Test
    public void testPlayMonopoly_Error() {
        //Resource to take
        Resource resource = Resource.BRICK;


        //Expect
        EasyMock.expect(mockplayer.useDevCard(DevCard.MONOPOLY)).andReturn(false);

        replayMocks();

        assertThrows(CardNotPlayableException.class,()-> game.playMonopoly(mockplayer,robbed,resource));

        verifyMocks();

    } 

    @Test
    public void testPlayYearOfPlenty_NoDev() {
        //Resources to take
        Resource r1 = Resource.BRICK;
        Resource r2 = Resource.WOOL;

        //Expect
        EasyMock.expect(mockplayer.useDevCard(DevCard.PLENTY)).andReturn(false);

        replayMocks();

        assertThrows(CardNotPlayableException.class,()-> game.playYearOfPlenty(mockplayer,r1,r2));

        verifyMocks();
    }

    @Test
    public void testPlayYearOfPlenty_NoResources() {
        //Resources to take
        Resource r1 = Resource.BRICK;
        Resource r2 = Resource.WOOL;
        //Manually empty bank
        bank.removeResource(r1,bank.getResourceAmount(r1));
        bank.removeResource(r2,bank.getResourceAmount(r2));

        //Expect
        EasyMock.expect(mockplayer.useDevCard(DevCard.PLENTY)).andReturn(true);
        EasyMock.expect(mockplayer.hand.addDevelopmentCard(DevCard.PLENTY)).andReturn(true);


        replayMocks();

        assertThrows(NotEnoughResourcesException.class,()-> game.playYearOfPlenty(mockplayer,r1,r2));

        verifyMocks();
    }

    @Test
    public void testPlayYearOfPlenty_NoR1() {
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


        replayMocks();

        assertThrows(NotEnoughResourcesException.class,()-> game.playYearOfPlenty(mockplayer,r1,r2));

        verifyMocks();
    }

    @Test
    public void testPlayYearOfPlenty_NoR2() {
        //Resources to take
        Resource r1 = Resource.BRICK;
        Resource r2 = Resource.WOOL;
        //Manually empty bank
        bank.removeResource(r2, bank.getResourceAmount(r2));

        //Expect
        EasyMock.expect(mockplayer.useDevCard(DevCard.PLENTY)).andReturn(true);
        EasyMock.expect(mockplayer.hand.addDevelopmentCard(DevCard.PLENTY)).andReturn(true);

        replayMocks();

        assertThrows(NotEnoughResourcesException.class,()-> game.playYearOfPlenty(mockplayer,r1,r2));

        verifyMocks();
    }

    @Test
    public void testPlayYearOfPlenty_GetResources() {
        //Resources to take
        Resource r1 = Resource.BRICK;
        Resource r2 = Resource.WOOL;
        Resource[] resources = {r1,r2};

        //Expect
        EasyMock.expect(mockplayer.useDevCard(DevCard.PLENTY)).andReturn(true);
        EasyMock.expect(mockplayer.hand.addResources(resources)).andReturn(true);
        replayMocks();

        try{
            game.playYearOfPlenty(mockplayer,r1,r2);
        }
        catch(Exception e) {
            fail();
            e.printStackTrace();
        }

        verifyMocks();
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
