package controller;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import gamedatastructures.Game;
import gamedatastructures.GameType;
import gamedatastructures.Player;

import java.util.HashMap;
import java.util.Map;

public class DistributeResourcesTest{

    @Test
    public void test_rollDice(){
        Game mockedGame = EasyMock.createStrictMock(Game.class);
        Player player = new Player(1);

        GameType gameType = GameType.Advanced;
        Controller controller = new Controller(mockedGame, new Player[] {player}, gameType);

        HashMap<Integer, Boolean> dieResults = new HashMap<>();
        dieResults.put(2, false);
        dieResults.put(3, false);
        dieResults.put(4, false);
        dieResults.put(5, false);
        dieResults.put(6, false);
        dieResults.put(7, false);
        dieResults.put(8, false);
        dieResults.put(9, false);
        dieResults.put(10, false);
        dieResults.put(11, false);
        dieResults.put(12, false);


        for(int i = 0; i < 300; i++){
            int dieRolled = controller.rollDice();

            // make sure no bad rolls are made
            assertTrue(dieRolled >= 2);
            assertTrue(dieRolled <= 12);

            dieResults.put(dieRolled, true); // mark the die as rolled
        }

        // now check the make to make sure every die was rolled
        for (Map.Entry<Integer, Boolean> dieRoll: dieResults.entrySet()) {
            assertTrue(dieRoll.getValue(), "Die: " + dieRoll.getKey() + " not rolled");
        }
    }
}
