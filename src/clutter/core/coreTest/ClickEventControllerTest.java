package clutter.core.coreTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clutter.core.ClickEventController;
import clutter.core.Dimension;
import clutter.core.ClickEventController.ClickEventHandler;

class ClickEventControllerTest {
    
    private ClickEventController controller;
    private MockClickEventHandler handler1;
    private MockClickEventHandler handler2;
    
    @BeforeEach
    void setUp() {
        controller = new ClickEventController();
        handler1 = new MockClickEventHandler(true);  // Handler that consumes events
        handler2 = new MockClickEventHandler(false); // Handler that doesn't consume events
    }
    
    @Test
    void testSetClickHandler() {
        controller.setClickHandler(handler1);
        // Verify handler works by sending an event
        controller.handleClickEvent(1, new Dimension(10, 10), 1);
        assertTrue(handler1.wasHandlerCalled);
    }
    
    @Test
    void testHandleClickEventPropagation() {
        // Add handlers in order - last added is first to receive events
        controller.setClickHandler(handler2); // Non-consuming handler
        controller.setClickHandler(handler1); // Consuming handler
        
        controller.handleClickEvent(1, new Dimension(10, 10), 1);
        
        // Handler1 should be called and should consume the event
        assertTrue(handler1.wasHandlerCalled);
        // Handler2 shouldn't be called because handler1 consumed the event
        assertFalse(handler2.wasHandlerCalled);
    }
    
    @Test
    void testHandleClickEventNonConsuming() {
        // Add only non-consuming handlers
        controller.setClickHandler(handler2);
        controller.setClickHandler(new MockClickEventHandler(false));
        
        controller.handleClickEvent(1, new Dimension(10, 10), 1);
        
        // All handlers should be called
        assertTrue(handler2.wasHandlerCalled);
    }
    
    @Test
    void testRemoveClickHandler() {
        controller.setClickHandler(handler1);
        controller.removeClickHandler(handler1);
        
        // Reset the flag
        handler1.wasHandlerCalled = false;
        
        // The handler should no longer receive events
        controller.handleClickEvent(1, new Dimension(10, 10), 1);
        assertFalse(handler1.wasHandlerCalled);
    }
    
    @Test
    void testRemoveClickHandlerStackBehavior() {
        controller.setClickHandler(handler1);
        controller.setClickHandler(handler2);
        
        // Remove the top handler (handler2)
        controller.removeClickHandler(handler2);
        
        // Reset flags
        handler1.wasHandlerCalled = false;
        handler2.wasHandlerCalled = false;
        
        // Now only handler1 should receive events
        controller.handleClickEvent(1, new Dimension(10, 10), 1);
        assertTrue(handler1.wasHandlerCalled);
        assertFalse(handler2.wasHandlerCalled);
    }
    
    @Test
    void testRemoveFromEmptyStack() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            controller.removeClickHandler(handler1);
        });
        
        assertEquals("No clickhandlers to remove", exception.getMessage());
    }
    
    /**
     * Mock implementation of ClickEventHandler for testing
     */
    private static class MockClickEventHandler implements ClickEventHandler {
        private boolean consumesEvents;
        private boolean wasHandlerCalled = false;
        
        public MockClickEventHandler(boolean consumesEvents) {
            this.consumesEvents = consumesEvents;
        }
        
        @Override
        public boolean hitTest(int id, Dimension hitPos, int clickCount) {
            wasHandlerCalled = true;
            return consumesEvents;
        }
        
        @Override
        public void onClickHandlerRemoved() {
            // We could track this being called if needed
        }
    }
}

