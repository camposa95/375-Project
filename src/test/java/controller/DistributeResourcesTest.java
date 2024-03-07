package controller;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import gamedatastructures.Game;
import gamedatastructures.GameType;
import gamedatastructures.Player;

public class DistributeResourcesTest{

    @Test
    public void test_rollDice(){
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        GameType gameType = GameType.Advanced;
        Controller controller = new Controller(mockedGame, new Player[1], gameType);
        for(int i =0; i<50; i++){
            controller.rollDice();
            assertTrue(controller.getDie() >=2);
            assertTrue(controller.getDie() <=12);
            System.out.println(controller.getDie());
        }
    }
    // @Test
    // public void test_mocked() {
    //     Game game = EasyMock.createStrictMock(Game.class);
    //     GameType gameType = GameType.Advanced;
    //     Player player = new Player(1);
    //     Controller controller = new Controller(game, new Player[]{player}, gameType);
        
    //     game.distributeResources()

    //     EasyMock.replay(game);

    //     controller.rollDice();

    //     EasyMock.verify(game);
    // }
}
