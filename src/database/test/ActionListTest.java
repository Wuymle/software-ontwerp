package database.test;

import database.Action;
import database.ActionList;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

class ActionListTest {

    @Test
    void testUndo() {
        AtomicInteger undoCounter = new AtomicInteger(0);
        Action action1 = new Action(undoCounter::incrementAndGet, () -> {});
        Action action2 = new Action(undoCounter::incrementAndGet, () -> {});
        List<Action> actions = new ArrayList<>();
        actions.add(action1);
        actions.add(action2);
        ActionList actionList = new ActionList(actions);

        actionList.undo();
        assertEquals(2, undoCounter.get());
    }

    @Test
    void testRedo() {
        AtomicInteger redoCounter = new AtomicInteger(0);
        Action action1 = new Action(() -> {}, redoCounter::incrementAndGet);
        Action action2 = new Action(() -> {}, redoCounter::incrementAndGet);
        List<Action> actions = new ArrayList<>();
        actions.add(action1);
        actions.add(action2);
        ActionList actionList = new ActionList(actions);

        actionList.redo();
        assertEquals(2, redoCounter.get());
    }

    @Test
    void testUndoOrder() {
        List<String> order = new ArrayList<>();
        Action action1 = new Action(() -> order.add("action1_undo"), () -> {});
        Action action2 = new Action(() -> order.add("action2_undo"), () -> {});
        List<Action> actions = new ArrayList<>();
        actions.add(action1);
        actions.add(action2);
        ActionList actionList = new ActionList(actions);

        actionList.undo();
        assertEquals("action2_undo", order.get(0));
        assertEquals("action1_undo", order.get(1));
    }

    @Test
    void testRedoOrder() {
        List<String> order = new ArrayList<>();
        Action action1 = new Action(() -> {}, () -> order.add("action1_redo"));
        Action action2 = new Action(() -> {}, () -> order.add("action2_redo"));
        List<Action> actions = new ArrayList<>();
        actions.add(action1);
        actions.add(action2);
        ActionList actionList = new ActionList(actions);

        actionList.redo();
        assertEquals("action1_redo", order.get(0));
        assertEquals("action2_redo", order.get(1));
    }

    @Test
    void testEmptyActionList() {
        ActionList emptyActionList = new ActionList(new ArrayList<>());
        assertDoesNotThrow(emptyActionList::undo);
        assertDoesNotThrow(emptyActionList::redo);
    }
}
