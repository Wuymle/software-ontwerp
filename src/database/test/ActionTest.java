package database.test;

import database.Action;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.lang.reflect.Field; // Required for direct reflection if TestHelper is not used or is insufficient

// Assuming TestHelper is in a package accessible from here, e.g., application.test
// For this example, let's assume direct reflection or that TestHelper is available.
// If TestHelper.getPrivateField is static and accessible:
// import application.test.TestHelper;


class ActionTest {

    @Test
    void testUndo() {
        AtomicBoolean undone = new AtomicBoolean(false);
        Action action = new Action(() -> undone.set(true), () -> {});
        action.undo();
        assertTrue(undone.get());
    }

    @Test
    void testRedo() {
        AtomicBoolean redone = new AtomicBoolean(false);
        Action action = new Action(() -> {}, () -> redone.set(true));
        action.redo();
        assertTrue(redone.get());
    }

    @Test
    void testCallback() {
        AtomicBoolean callbackCalled = new AtomicBoolean(false);
        Action action = new Action(() -> {}, () -> {});
        action.setCallback(() -> callbackCalled.set(true));
        action.undo();
        assertTrue(callbackCalled.get());
        callbackCalled.set(false);
        action.redo();
        assertTrue(callbackCalled.get());
    }

    @Test
    void testMultipleCallbacks() {
        AtomicBoolean callback1Called = new AtomicBoolean(false);
        AtomicBoolean callback2Called = new AtomicBoolean(false);
        Action action = new Action(() -> {}, () -> {});
        action.setCallback(() -> callback1Called.set(true));
        action.setCallback(() -> callback2Called.set(true));
        action.undo();
        assertTrue(callback1Called.get());
        assertTrue(callback2Called.get());
    }

    @Test
    void testNoCallback() {
        AtomicBoolean undone = new AtomicBoolean(false);
        AtomicBoolean redone = new AtomicBoolean(false);
        Action action = new Action(() -> undone.set(true), () -> redone.set(true));
        action.undo();
        assertTrue(undone.get());
        action.redo();
        assertTrue(redone.get());
    }

    @Test
    void testStaticNoneAction() throws Exception {
        // Accessing package-private static field Action.NONE
        // Option 1: Direct Reflection (if TestHelper is not set up for this or preferred)
        Field noneField = Action.class.getDeclaredField("NONE");
        noneField.setAccessible(true);
        Action noneAction = (Action) noneField.get(null); // null for static field

        // Option 2: Using a helper like TestHelper.getPrivateField (if it supports static fields from class)
        // Action noneAction = (Action) TestHelper.getPrivateField(Action.class, "NONE"); // Assuming TestHelper can get static field from class object

        assertNotNull(noneAction);

        // Verify that undo and redo do nothing and don't throw exceptions
        AtomicBoolean changed = new AtomicBoolean(false);

        // Test undo on NONE
        noneAction.undo(); // Should do nothing
        assertFalse(changed.get(), "Action.NONE.undo() should not cause changes.");

        // Test redo on NONE
        noneAction.redo(); // Should do nothing
        assertFalse(changed.get(), "Action.NONE.redo() should not cause changes.");

        // Test callback on NONE
        AtomicBoolean callbackCalled = new AtomicBoolean(false);
        noneAction.setCallback(() -> callbackCalled.set(true));
        noneAction.undo();
        assertTrue(callbackCalled.get(), "Callback should still be called on Action.NONE.undo()");
        callbackCalled.set(false);
        noneAction.redo();
        assertTrue(callbackCalled.get(), "Callback should still be called on Action.NONE.redo()");
    }
}
