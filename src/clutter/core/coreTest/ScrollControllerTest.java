package clutter.core.coreTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clutter.core.Context;
import clutter.core.ScrollController;
import clutter.core.ScrollController.ScrollSubscriber;

class ScrollControllerTest {
    
    private ScrollController scrollController;
    private MockContext mockContext;
    private MockScrollSubscriber subscriber;
    
    @BeforeEach
    void setUp() {
        mockContext = new MockContext();
        scrollController = new ScrollController(mockContext);
        subscriber = new MockScrollSubscriber();
        scrollController.addSubscriber(subscriber);
    }
    
    @Test
    void testInitialState() {
        // Test initial conditions
        assertEquals(1.0, scrollController.getRelContentHeight());
        assertEquals(1.0, scrollController.getRelContentWidth());
    }
    
    @Test
    void testSetRelContentHeight() {
        // Test setting content height
        scrollController.setRelContentHeight(2.0);
        assertEquals(2.0, scrollController.getRelContentHeight());
        
        // Test minimum value enforcement
        scrollController.setRelContentHeight(0.5);  // Should be clamped to 1.0
        assertEquals(1.0, scrollController.getRelContentHeight());
    }
    
    @Test
    void testSetRelContentWidth() {
        // Test setting content width
        scrollController.setRelContentWidth(3.0);
        assertEquals(3.0, scrollController.getRelContentWidth());
        
        // Test minimum value enforcement
        scrollController.setRelContentWidth(0.5);  // Should be clamped to 1.0
        assertEquals(1.0, scrollController.getRelContentWidth());
    }

    
    @Test
    void testSubscriberManagement() {
        // Add another subscriber
        MockScrollSubscriber subscriber2 = new MockScrollSubscriber();
        scrollController.addSubscriber(subscriber2);
        
        // Both subscribers should receive updates
        scrollController.setRelContentWidth(2.0);
        scrollController.setScrollX(0.5);
        assertEquals(0.5, subscriber.lastHorizontalScrollValue);
        assertEquals(0.5, subscriber2.lastHorizontalScrollValue);
        
        // Remove first subscriber
        scrollController.removeSubscriber(subscriber);
        scrollController.setScrollX(0.75);
        assertEquals(0.5, subscriber.lastHorizontalScrollValue);  // Unchanged
        assertEquals(0.75, subscriber2.lastHorizontalScrollValue);  // Updated
    }
    
    /**
     * Mock Context for testing
     */
    private static class MockContext extends Context {
        public MockContext() {
            super(null); // Pass null as we're mocking the ApplicationWindow
        }
    }
    
    /**
     * Mock ScrollSubscriber for testing
     */
    private static class MockScrollSubscriber implements ScrollSubscriber {
        double lastHorizontalScrollValue = 0.0;
        CountDownLatch horizontalScrollLatch = null;
        CountDownLatch verticalScrollLatch = null;
        
        @Override
        public void onHorizontalScroll(double scrollX) {
            lastHorizontalScrollValue = scrollX;
            if (horizontalScrollLatch != null) {
                horizontalScrollLatch.countDown();
            }
        }
        
        @Override
        public void onVerticalScroll(double scrollY) {
            if (verticalScrollLatch != null) {
                verticalScrollLatch.countDown();
            }
        }
    }
}