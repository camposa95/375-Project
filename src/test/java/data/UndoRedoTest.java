package data;

import domain.controller.Controller;
import domain.controller.GameState;
import domain.game.GameType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UndoRedoTest {

    GameLoader loader = GameLoader.getInstance();
    Controller controller;

    @BeforeEach
    public void setup() {
        controller = loader.createNewGame(GameType.Beginner, 4, "English");
    }


    // Basic undo testing
    @Test
    public void testUndo_InitTurn_Start() {
        assertFalse(loader.undo()); // start of init turn is end of turn history, can't do any earlier
    }

    @Test
    public void testUndo_InitTurn_InProgress() {
        controller.setState(GameState.DEFAULT); // not start of init turn
        assertTrue(loader.undo()); // reverts us to the start of the init turn
        assertFalse(loader.undo()); // Can't undo past that since we are at the start of the init turn
    }

    @Test
    public void testMultipleUndo_CurrentAtStart() {
        for (int i = 0; i < 5; i++)  {
            loader.notifyOfTurnStart(); // init turn + 5 more turns added to undo stack
        }

        for (int i = 0; i < 5; i++) {
            assertTrue(loader.undo()); // should be able to undo all of these turns
        }

        assertFalse(loader.undo()); // except the init turn
    }

    @Test
    public void testMultipleUndo_CurrentInProgress() {
        for (int i = 0; i < 5; i++)  {
            loader.notifyOfTurnStart(); // init turn + 5 more turns added to undo stack
        }
        controller.setState(GameState.DEFAULT); // turn 6 not in progress
        assertTrue(loader.undo()); // revert to beginning of turn 6

        for (int i = 0; i < 5; i++) {
            assertTrue(loader.undo()); // should be able to undo all of these turns now
        }

        assertFalse(loader.undo()); // except the init turn
    }

    // Redo testing
    @Test
    public void testRedo_InitTurn_Start() {
        assertFalse(loader.redo()); // no undone turns yet at start of init turn
    }

    @Test
    public void testRedo_CurrentInProgress_NoUndoHistory() {
        loader.notifyOfTurnStart();
        loader.notifyOfTurnStart(); // a couple of turns in

        controller.setState(GameState.DEFAULT); // not start of current turn
        assertTrue(loader.undo()); // reverts us to the start of the current turn
        assertFalse(loader.redo()); // Can't redo since mid-turn state is never saved for redo,
                                    // Only turn starts which are undone, so not history is available for redo yet
    }

    @Test
    public void testMultipleRedo_TurnStart() {
        for (int i = 0; i < 5; i++)  {
            loader.notifyOfTurnStart(); // init turn + 5 more turns added to undo stack
        }

        for (int i = 0; i < 5; i++) {
            assertTrue(loader.undo()); // should be able to undo all of these turns
        }
        assertFalse(loader.undo()); // except the init turn



        for (int i = 0; i < 5; i++) {
            assertTrue(loader.redo()); // should be able to redo all of these turns
        }
        assertFalse(loader.redo()); // no more turns to redo left
    }

    @Test
    public void testMultipleUndo_TurnInProgress() {
        for (int i = 0; i < 5; i++)  {
            loader.notifyOfTurnStart(); // init turn + 5 more turns added to undo stack
        }
        controller.setState(GameState.DEFAULT); // turn 6 not in progress
        assertTrue(loader.undo()); // revert to beginning of turn 6

        for (int i = 0; i < 5; i++) {
            assertTrue(loader.undo()); // should be able to undo all of these turns now
        }
        assertFalse(loader.undo()); // except the init turn



        for (int i = 0; i < 5; i++) {
            assertTrue(loader.redo()); // should be able to redo all of these turns
        }
        assertFalse(loader.redo()); // no more turns to redo left

        // Notice redo does not know about mid-turn undo, only the going back a turn
    }

    @Test
    public void testRedo_HistoryFork() {
        for (int i = 0; i < 5; i++)  {
            loader.notifyOfTurnStart(); // init turn + 5 more turns added to undo stack
        }

        for (int i = 0; i < 5; i++) {
            assertTrue(loader.undo()); // should be able to undo all of these turns
        }
        assertFalse(loader.undo()); // except the init turn


        // At this point 5 turns would normally be redo-able
        loader.notifyOfTurnStart(); // fork the turn history

        assertFalse(loader.redo()); // Turn history fork forces redo timeline to be cleared
    }
}
