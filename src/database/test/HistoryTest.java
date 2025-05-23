package database.test;

import database.Action;
import database.History;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

class HistoryTest {

    private History history;
    private AtomicInteger undoCount;
    private AtomicInteger redoCount;
    private Action testAction;

    @BeforeEach
    void setUp() {
        history = new History();
        undoCount = new AtomicInteger(0);
        redoCount = new AtomicInteger(0);
        testAction = new Action(undoCount::incrementAndGet, redoCount::incrementAndGet);
    }

    @Test
    void testInitialState() {
        assertFalse(history.canUndo());
        assertFalse(history.canRedo());
    }

    @Test
    void testRecordAction() {
        history.record(testAction);
        assertEquals(1, redoCount.get(), "Redo should be called immediately on record");
        assertEquals(0, undoCount.get());
        assertTrue(history.canUndo());
        assertFalse(history.canRedo());
    }

    @Test
    void testUndoAction() {
        history.record(testAction); // redoCount = 1
        redoCount.set(0); // Reset for clarity

        history.undo();
        assertEquals(1, undoCount.get());
        assertEquals(0, redoCount.get());
        assertFalse(history.canUndo());
        assertTrue(history.canRedo());
    }

    @Test
    void testRedoAction() {
        history.record(testAction); // redoCount = 1
        history.undo();         // undoCount = 1
        redoCount.set(0);       // Reset for clarity
        undoCount.set(0);       // Reset for clarity

        history.redo();
        assertEquals(0, undoCount.get());
        assertEquals(1, redoCount.get());
        assertTrue(history.canUndo());
        assertFalse(history.canRedo());
    }

    @Test
    void testUndoEmptyStack() {
        assertFalse(history.canUndo());
        history.undo(); // Should not throw an error and do nothing
        assertFalse(history.canUndo());
        assertEquals(0, undoCount.get());
    }

    @Test
    void testRedoEmptyStack() {
        assertFalse(history.canRedo());
        history.redo(); // Should not throw an error and do nothing
        assertFalse(history.canRedo());
        assertEquals(0, redoCount.get());
    }

    @Test
    void testRecordInvalidatesRedoStack() {
        history.record(testAction); // Action1: redoCount = 1
        history.undo();         // Action1: undoCount = 1, canRedo = true

        Action testAction2 = new Action(undoCount::incrementAndGet, redoCount::incrementAndGet);
        history.record(testAction2); // Action2: redoCount becomes 1 (for action2), canRedo for Action1 should be false

        assertFalse(history.canRedo(), "Recording a new action should clear the redo stack");
        // Try to redo what was Action1, should not happen
        history.redo(); // This should do nothing as redo stack is cleared
    }

    @Test
    void testMultipleUndoRedo() {
        Action action1 = new Action(() -> undoCount.addAndGet(1), () -> redoCount.addAndGet(1));
        Action action2 = new Action(() -> undoCount.addAndGet(10), () -> redoCount.addAndGet(10));

        history.record(action1); // redo1 (1)
        history.record(action2); // redo2 (10)
        // State: undoStack=[a1,a2], redoStack=[], undoCount=0, redoCount=11

        assertEquals(0, undoCount.get());
        assertEquals(11, redoCount.get());
        assertTrue(history.canUndo());
        assertFalse(history.canRedo());

        history.undo(); // undo2 (10)
        // State: undoStack=[a1], redoStack=[a2], undoCount=10, redoCount=11
        assertEquals(10, undoCount.get());
        assertEquals(11, redoCount.get());
        assertTrue(history.canUndo());
        assertTrue(history.canRedo());

        history.undo(); // undo1 (1)
        // State: undoStack=[], redoStack=[a2,a1], undoCount=11, redoCount=11
        assertEquals(11, undoCount.get());
        assertEquals(11, redoCount.get());
        assertFalse(history.canUndo());
        assertTrue(history.canRedo());

        history.redo(); // redo1 (1)
        // State: undoStack=[a1], redoStack=[a2], undoCount=11, redoCount=12
        assertEquals(11, undoCount.get());
        assertEquals(12, redoCount.get());
        assertTrue(history.canUndo());
        assertTrue(history.canRedo());

        history.redo(); // redo2 (10)
        // State: undoStack=[a1,a2], redoStack=[], undoCount=11, redoCount=22
        assertEquals(11, undoCount.get());
        assertEquals(22, redoCount.get());
        assertTrue(history.canUndo());
        assertFalse(history.canRedo());
    }
}
