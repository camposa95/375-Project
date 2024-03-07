package controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import gamedatastructures.EmptyDevCardDeckException;
import gamedatastructures.Game;
import gamedatastructures.GameType;
import gamedatastructures.NotEnoughResourcesException;
import gamedatastructures.Player;

public class ClickedBuyDevCardTest {
    
    @Test
    public void testClickedBuyDevCard1() throws NotEnoughResourcesException, EmptyDevCardDeckException {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        Player mockedPlayer = EasyMock.createStrictMock(Player.class);
        GameType gameType = GameType.Advanced;

        Player[] players = {mockedPlayer};
        Controller controller = new Controller(mockedGame, players, gameType);
        
        controller.setPhase(GamePhase.REGULAR_PLAY);
        controller.setState(GameState.DEFAULT); // not the state doesn't matter
        controller.setCurrentPlayer(mockedPlayer);

        mockedGame.buyDevCard(mockedPlayer);
        // game gives us no errors

        // player did not gain enough points to win
        EasyMock.expect(mockedPlayer.getVictoryPoints()).andReturn(9);

        // method call
        EasyMock.replay(mockedGame, mockedPlayer);
        SuccessCode actual = controller.clickedBuyDevCard();
        EasyMock.verify(mockedGame, mockedPlayer);

        // assert on expected result
        assertEquals(SuccessCode.SUCCESS, actual);
    }

    @Test
    public void testClickedBuyDevCard2() throws NotEnoughResourcesException, EmptyDevCardDeckException {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        Player mockedPlayer = EasyMock.createStrictMock(Player.class);
        GameType gameType = GameType.Advanced;

        Player[] players = {mockedPlayer};
        Controller controller = new Controller(mockedGame, players, gameType);
        
        controller.setPhase(GamePhase.REGULAR_PLAY);
        controller.setState(GameState.DEFAULT); // not the state doesn't matter
        controller.setCurrentPlayer(mockedPlayer);

        mockedGame.buyDevCard(mockedPlayer);
        EasyMock.expectLastCall().andThrow(new NotEnoughResourcesException());
        // game tells us the player does not have enough resources

        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.clickedBuyDevCard();
        EasyMock.verify(mockedGame);

        // assert on expected result
        assertEquals(SuccessCode.INSUFFICIENT_RESOURCES, actual);
    }

    @Test
    public void testClickedBuyDevCard3() throws NotEnoughResourcesException, EmptyDevCardDeckException {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        Player mockedPlayer = EasyMock.createStrictMock(Player.class);
        GameType gameType = GameType.Advanced;

        Player[] players = {mockedPlayer};
        Controller controller = new Controller(mockedGame, players, gameType);
        
        controller.setPhase(GamePhase.REGULAR_PLAY);
        controller.setState(GameState.DEFAULT); // not the state doesn't matter
        controller.setCurrentPlayer(mockedPlayer);

        mockedGame.buyDevCard(mockedPlayer);
        EasyMock.expectLastCall().andThrow(new EmptyDevCardDeckException());
        // game tells us the dev card deck is empty

        // method call
        EasyMock.replay(mockedGame);
        SuccessCode actual = controller.clickedBuyDevCard();
        EasyMock.verify(mockedGame);

        // assert on expected result
        assertEquals(SuccessCode.EMPTY_DEV_CARD_DECK, actual);
    }

    @Test
    public void testClickedBuyDevCard4() throws NotEnoughResourcesException, EmptyDevCardDeckException {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        Player mockedPlayer = EasyMock.createStrictMock(Player.class);
        GameType gameType = GameType.Advanced;

        Player[] players = {mockedPlayer};
        Controller controller = new Controller(mockedGame, players, gameType);
        
        controller.setPhase(GamePhase.SETUP);
        controller.setState(GameState.DEFAULT); // not the state doesn't matter
        controller.setCurrentPlayer(mockedPlayer);

        // game should not be called

        // method call
        EasyMock.replay(mockedGame);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            controller.clickedBuyDevCard();
        });
        EasyMock.verify(mockedGame);

        // assert the message is correct
        String expectedMessage = "Clicked buy dev card during setup";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testClickedBuyDevCard15_Win() throws NotEnoughResourcesException, EmptyDevCardDeckException {
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        Player mockedPlayer = EasyMock.createStrictMock(Player.class);
        GameType gameType = GameType.Advanced;

        Player[] players = {mockedPlayer};
        Controller controller = new Controller(mockedGame, players, gameType);
        
        controller.setPhase(GamePhase.REGULAR_PLAY);
        controller.setState(GameState.DEFAULT); // note the state doesn't matter
        controller.setCurrentPlayer(mockedPlayer);

        mockedGame.buyDevCard(mockedPlayer);
        // game gives us no errors

        // player gained enough points to win
        EasyMock.expect(mockedPlayer.getVictoryPoints()).andReturn(10);

        // method call
        EasyMock.replay(mockedGame, mockedPlayer);
        SuccessCode actual = controller.clickedBuyDevCard();
        EasyMock.verify(mockedGame, mockedPlayer);

        // assert on expected result
        assertEquals(SuccessCode.GAME_WIN, actual);
    }
}
