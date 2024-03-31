package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import data.GameLoader;
import domain.player.HarvestBooster;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import domain.controller.Controller;
import domain.controller.GameState;
import domain.controller.SuccessCode;
import domain.bank.Bank;
import domain.devcarddeck.DevCard;
import domain.devcarddeck.DevelopmentCardDeck;
import domain.game.Game;
import domain.gameboard.GameBoard;
import domain.game.GameType;
import domain.player.Player;
import domain.bank.Resource;
import domain.graphs.RoadGraph;
import domain.graphs.VertexGraph;


/**
 * The purpose of this test class is to test feature 11 (F11):
 *      Ability for a Player to play one Development Card from their hand during their turn.
 */
public class F11Test {

    private static final Resource[] RESOURCES_FOR_CARD = {Resource.WOOL, Resource.GRAIN, Resource.ORE};

    private static final int VICTORY_POINTS_FROM_SETUP = 2;
    
    // helper method that will loop the game back to the current player by ending turn a bunch
    private void loopToBeginging(final Controller controller) {
        for (int i = 0; i < 4; i++) {
            controller.setState(GameState.DEFAULT);
            assertEquals(SuccessCode.SUCCESS, controller.endTurn());
        }
    }

    @Test
    public void testPlayCardCorrectly() throws IOException {
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
        
        // Assert that the begineer setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            controllerRef.set(new Controller(game, players, gameType));
        }, "Setup while loop timed out");
        Controller controller = controllerRef.get();

        // -------------------------- Start of Actual Test Stuff ---------------------------
        // Note: we assume everything about setup was correct because it was tested earlier

        // Note: at this point the players would have gotten some starter resources during the 
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the player1's hand and assert that the player has zero resources because the
        // longest road card doesn't need resources because it is free
        for (Resource resource: Resource.values()) {
            if (resource != Resource.ANY) { // skip this one used for trading
                int count = player1.hand.getResourceCardAmount(resource);
                if (count > 0) {
                    player1.hand.removeResource(resource, count);
                }
            }
        }
        assertEquals(0, player1.hand.getResourceCardCount());

        // ------------------------------------ for knight ------------------------------------------

        // give the player some resources to buy
        player1.hand.addResources(RESOURCES_FOR_CARD);

        // player purchases the card
        assertEquals(true, player1.purchaseDevCard(DevCard.KNIGHT));

        // wait till next turn to play card
        loopToBeginging(controller);
        assertEquals(player1, controller.getCurrentPlayer());
        assertEquals(true, controller.getDevCardsEnabled());

        // play knight card
        assertEquals(SuccessCode.SUCCESS, controller.playKnightCard());
        assertEquals(0, player1.hand.devCards.get(DevCard.KNIGHT));
        assertEquals(false, controller.getDevCardsEnabled());

        // ------------------------------------ for plenty ------------------------------------------

        // give the player some resources to buy
        player1.hand.addResources(RESOURCES_FOR_CARD);

        // player purchases the card
        assertEquals(true, player1.purchaseDevCard(DevCard.PLENTY));

        // wait till next turn to play card
        loopToBeginging(controller);
        assertEquals(player1, controller.getCurrentPlayer());
        assertEquals(true, controller.getDevCardsEnabled());

        // play card
        assertEquals(SuccessCode.SUCCESS, controller.playYearOfPlenty(Resource.WOOL, Resource.GRAIN));
        assertEquals(0, player1.hand.devCards.get(DevCard.PLENTY));
        assertEquals(false, controller.getDevCardsEnabled());

        // ------------------------------------ for Monopoly ------------------------------------------

        // give the player some resources to buy
        player1.hand.addResources(RESOURCES_FOR_CARD);

        // player purchases the card
        assertEquals(true, player1.purchaseDevCard(DevCard.MONOPOLY));

        // wait till next turn to play card
        loopToBeginging(controller);
        assertEquals(player1, controller.getCurrentPlayer());
        assertEquals(true, controller.getDevCardsEnabled());

        // play card
        assertEquals(SuccessCode.SUCCESS, controller.playMonopolyCard(Resource.WOOL));
        assertEquals(0, player1.hand.devCards.get(DevCard.MONOPOLY));
        assertEquals(false, controller.getDevCardsEnabled());

        // ------------------------------------ for Road ------------------------------------------

        // give the player some resources to buy
        player1.hand.addResources(RESOURCES_FOR_CARD);

        // player purchases the card
        assertEquals(true, player1.purchaseDevCard(DevCard.ROAD));

        // wait till next turn to play card
        loopToBeginging(controller);
        assertEquals(player1, controller.getCurrentPlayer());
        assertEquals(true, controller.getDevCardsEnabled());

        // play knight card
        assertEquals(SuccessCode.SUCCESS, controller.useRoadBuildingCard());
        assertEquals(0, player1.hand.devCards.get(DevCard.ROAD));
        assertEquals(false, controller.getDevCardsEnabled());

        // ------------------------------------ for Victory Point cards enabled ------------------------------------------

        // let dev cards be enabled, but note they don't need to be
        controller.setDevCardsEnabled(true);

        // give the player some resources to buy
        player1.hand.addResources(RESOURCES_FOR_CARD);

        // player purchases the card
        assertEquals(true, player1.purchaseDevCard(DevCard.VICTORY));

        // don't need to wait for next turn

        // card should be automatically played and victory points increased
        assertEquals(VICTORY_POINTS_FROM_SETUP + 1, player1.getVictoryPoints());

        // we don't need to disable cards after using this one
        assertEquals(true, controller.getDevCardsEnabled());

        // ------------------------------------ for Victory Point cards disabled ------------------------------------------

        // disable dev cards, should matter
        controller.setDevCardsEnabled(false);

        // give the player some resources to buy
        player1.hand.addResources(RESOURCES_FOR_CARD);

        // player purchases the card
        assertEquals(true, player1.purchaseDevCard(DevCard.VICTORY));

        // don't need to wait for next turn

        // card should be automatically played and victory points increased
        assertEquals(VICTORY_POINTS_FROM_SETUP + 1 + 1, player1.getVictoryPoints());

        // dev cards should stil be disabled, nothing changed
        assertEquals(false, controller.getDevCardsEnabled());
    }

    @Test
    public void testPlayCardTriedMoreThan1() {
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
        
        // Assert that the begineer setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            controllerRef.set(new Controller(game, players, gameType));
        }, "Setup while loop timed out");
        Controller controller = controllerRef.get();

        // -------------------------- Start of Actual Test Stuff ---------------------------
        // Note: we assume everything about setup was correct because it was tested earlier

        // Note: at this point the players would have gotten some starter resources during the 
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the player1's hand and assert that the player has zero resources because the
        // longest road card doesn't need resources because it is free
        for (Resource resource: Resource.values()) {
            if (resource != Resource.ANY) { // skip this one used for trading
                int count = player1.hand.getResourceCardAmount(resource);
                if (count > 0) {
                    player1.hand.removeResource(resource, count);
                }
            }
        }
        assertEquals(0, player1.hand.getResourceCardCount());

        // give the player some resources to buy
        player1.hand.addResources(RESOURCES_FOR_CARD);
        player1.hand.addResources(RESOURCES_FOR_CARD);
        player1.hand.addResources(RESOURCES_FOR_CARD);
        player1.hand.addResources(RESOURCES_FOR_CARD);

        // player purchases the card
        assertEquals(true, player1.purchaseDevCard(DevCard.ROAD));
        assertEquals(true, player1.purchaseDevCard(DevCard.KNIGHT));
        assertEquals(true, player1.purchaseDevCard(DevCard.PLENTY));
        assertEquals(true, player1.purchaseDevCard(DevCard.MONOPOLY));

        // wait till next turn to play cards
        loopToBeginging(controller);
        assertEquals(player1, controller.getCurrentPlayer());

        // assume devCardsEnabled is false because we alreay played one this turn. this was tested above
        controller.setDevCardsEnabled(false);

        // play knight card
        assertEquals(SuccessCode.CANNOT_PLAY_CARD, controller.playKnightCard());

        // play year of plenty card
        assertEquals(SuccessCode.CANNOT_PLAY_CARD, controller.playYearOfPlenty(Resource.WOOL, Resource.GRAIN));

        // play monopoly card
        assertEquals(SuccessCode.CANNOT_PLAY_CARD, controller.playMonopolyCard(Resource.WOOL));

        // play road card
        assertEquals(SuccessCode.CANNOT_PLAY_CARD, controller.useRoadBuildingCard());
    }

    @Test
    public void testPlayCardTriedToPlayBoughtSameTurn() {
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
        
        // Assert that the begineer setup does not time out to kill mutant
        final AtomicReference<Controller> controllerRef = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            controllerRef.set(new Controller(game, players, gameType));
        }, "Setup while loop timed out");
        Controller controller = controllerRef.get();

        // -------------------------- Start of Actual Test Stuff ---------------------------
        // Note: we assume everything about setup was correct because it was tested earlier

        // Note: at this point the players would have gotten some starter resources during the 
        // automated setup phase. These are kind of unknown at this point but so we will
        // clear out the player1's hand and assert that the player has zero resources because the
        // longest road card doesn't need resources because it is free
        for (Resource resource: Resource.values()) {
            if (resource != Resource.ANY) { // skip this one used for trading
                int count = player1.hand.getResourceCardAmount(resource);
                if (count > 0) {
                    player1.hand.removeResource(resource, count);
                }
            }
        }
        assertEquals(0, player1.hand.getResourceCardCount());

        // give the player some resources to buy
        player1.hand.addResources(RESOURCES_FOR_CARD);
        player1.hand.addResources(RESOURCES_FOR_CARD);
        player1.hand.addResources(RESOURCES_FOR_CARD);
        player1.hand.addResources(RESOURCES_FOR_CARD);

        // player purchases the card
        assertEquals(true, player1.purchaseDevCard(DevCard.ROAD));
        assertEquals(true, player1.purchaseDevCard(DevCard.KNIGHT));
        assertEquals(true, player1.purchaseDevCard(DevCard.PLENTY));
        assertEquals(true, player1.purchaseDevCard(DevCard.MONOPOLY));

        // don't wait till next turn to try to play card
        // dev cards enabled should be true
        assertEquals(true, controller.getDevCardsEnabled());

        // play knight card
        assertEquals(SuccessCode.CANNOT_PLAY_CARD, controller.playKnightCard());
        // since the cards wasn't played we don't disable other cards from trying to be played
        assertEquals(true, controller.getDevCardsEnabled());

        // play year of plenty card
        assertEquals(SuccessCode.CANNOT_PLAY_CARD, controller.playYearOfPlenty(Resource.WOOL, Resource.GRAIN));
        // since the cards wasn't played we don't disable other cards from trying to be played
        assertEquals(true, controller.getDevCardsEnabled());

        // play monopoly card
        assertEquals(SuccessCode.CANNOT_PLAY_CARD, controller.playMonopolyCard(Resource.WOOL));
        // since the cards wasn't played we don't disable other cards from trying to be played
        assertEquals(true, controller.getDevCardsEnabled());

        // play road card
        assertEquals(SuccessCode.CANNOT_PLAY_CARD, controller.useRoadBuildingCard());
        // since the cards wasn't played we don't disable other cards from trying to be played
        assertEquals(true, controller.getDevCardsEnabled());
    }
}
