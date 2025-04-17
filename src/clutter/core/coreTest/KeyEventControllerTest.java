package clutter.core.coreTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clutter.core.KeyEventController;
import clutter.widgetinterfaces.KeyEventHandler;

class KeyEventControllerTest {
    
    private KeyEventController controller;
    private MockKeyEventHandler handler1;
    private MockKeyEventHandler handler2;
    
    @BeforeEach
    void setUp() {
        controller = new KeyEventController();
        handler1 = new MockKeyEventHandler(true);  // Handler that consumes events
        handler2 = new MockKeyEventHandler(false); // Handler that doesn't consume events
    }
    
    @Test
    void testSetKeyHandler() {
        controller.setKeyHandler(handler1);
        // Verify handler works by sending an event
        controller.handleKeyEvent(1, 65, 'A');
        assertTrue(handler1.wasHandlerCalled);
    }
    
    @Test
    void testHandleKeyEventPropagation() {
        // Add handlers in order - last added is first to receive events
        controller.setKeyHandler(handler2); // Non-consuming handler
        controller.setKeyHandler(handler1); // Consuming handler
        
        controller.handleKeyEvent(1, 65, 'A');
        
        // Handler1 should be called and should consume the event
        assertTrue(handler1.wasHandlerCalled);
        // Handler2 shouldn't be called because handler1 consumed the event
        assertFalse(handler2.wasHandlerCalled);
    }
    
    @Test
    void testHandleKeyEventNonConsuming() {
        // Add only non-consuming handlers
        controller.setKeyHandler(handler2);
        MockKeyEventHandler anotherNonConsumingHandler = new MockKeyEventHandler(false);
        controller.setKeyHandler(anotherNonConsumingHandler);
        
        controller.handleKeyEvent(1, 65, 'A');
        
        // All handlers should be called since none consume the event
        assertTrue(handler2.wasHandlerCalled);
        assertTrue(anotherNonConsumingHandler.wasHandlerCalled);
    }
    
    @Test
    void testRemoveKeyHandler() {
        controller.setKeyHandler(handler1);
        controller.removeKeyHandler(handler1);
        
        // The handler should no longer receive events
        controller.handleKeyEvent(1, 65, 'A');
        assertFalse(handler1.wasHandlerCalled);
        assertTrue(handler1.handlerRemovedCalled);
    }
    
    @Test
    void testRemoveKeyHandlerStackBehavior() {
        controller.setKeyHandler(handler1);
        controller.setKeyHandler(handler2);
        
        // Remove the top handler (handler2)
        controller.removeKeyHandler(handler2);
        
        // Reset flags
        handler1.wasHandlerCalled = false;
        handler2.wasHandlerCalled = false;
        
        // Verify handler2 was notified about removal
        assertTrue(handler2.handlerRemovedCalled);
        
        // Now only handler1 should receive events
        controller.handleKeyEvent(1, 65, 'A');
        assertTrue(handler1.wasHandlerCalled);
        assertFalse(handler2.wasHandlerCalled);
    }
    
    @Test
    void testRemoveFromEmptyStack() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            controller.removeKeyHandler(handler1);
        });
        
        assertEquals("No key handlers to remove", exception.getMessage());
    }
    
    @Test
    void testNullKeyHandler() {
        Error exception = assertThrows(Error.class, () -> {
            controller.setKeyHandler(null);
        });
        
        assertEquals("Trying to add null to keyHandler", exception.getMessage());
    }
    
    @Test
    void testRemoveHandlerNotInStack() {
        // Add handler1 to the stack
        controller.setKeyHandler(handler1);
        
        // Try to remove handler2 which isn't in the stack
        // This should remove handler1 as per the implementation
        controller.removeKeyHandler(handler2);
        
        // handler1 should have been removed
        assertTrue(handler1.handlerRemovedCalled);
        
        // Reset flag
        handler1.wasHandlerCalled = false;
        
        // No handlers should receive events now
        controller.handleKeyEvent(1, 65, 'A');
        assertFalse(handler1.wasHandlerCalled);
    }
    
    /**
     * Mock implementation of KeyEventHandler for testing
     */
    private static class MockKeyEventHandler implements KeyEventHandler {
        private boolean consumesEvents;
        private boolean wasHandlerCalled = false;
        private boolean handlerRemovedCalled = false;
        
        public MockKeyEventHandler(boolean consumesEvents) {
            this.consumesEvents = consumesEvents;
        }
        
        @Override
        public boolean onKeyPress(int id, int keyCode, char keyChar) {
            wasHandlerCalled = true;
            return consumesEvents;
        }
        
        @Override
        public void onKeyHandlerRemoved() {
            handlerRemovedCalled = true;
        }
    }
}