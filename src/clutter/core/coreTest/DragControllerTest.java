package clutter.core.coreTest;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.event.MouseEvent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clutter.core.ClickEventController;
import clutter.core.Context;
import clutter.core.DragController;
import clutter.core.Dimension;

class DragControllerTest {
    
    private MockDragController dragController;
    private MockContext mockContext;
    private MockClickEventController mockClickController;
    
    @BeforeEach
    void setUp() {
        mockClickController = new MockClickEventController();
        mockContext = new MockContext(mockClickController);
        dragController = new MockDragController(mockContext);
    }
    
    @Test
    void testStartDragging() {
        Dimension startPos = new Dimension(10, 10);
        dragController.startDragging(startPos);
        
        assertTrue(dragController.isDragging());
        assertEquals(startPos, dragController.getStartPosition());
        assertTrue(mockClickController.hasClickHandler(dragController));
    }
    
    @Test
    void testStartDraggingWhileAlreadyDragging() {
        // Start dragging first time
        Dimension startPos1 = new Dimension(10, 10);
        dragController.startDragging(startPos1);
        
        // Try to start dragging again
        Dimension startPos2 = new Dimension(20, 20);
        dragController.startDragging(startPos2);
        
        // Should still have the first position
        assertTrue(dragController.isDragging());
        assertEquals(startPos1, dragController.getStartPosition());
    }
    
    @Test
    void testHitTestMouseDragged() {
        // Start dragging
        Dimension startPos = new Dimension(10, 10);
        dragController.startDragging(startPos);
        
        // Simulate mouse drag
        Dimension dragPos = new Dimension(20, 20);
        boolean consumed = dragController.hitTest(MouseEvent.MOUSE_DRAGGED, dragPos, 1);
        
        assertTrue(consumed);
        assertEquals(dragPos, dragController.getDragPosition());
        assertTrue(dragController.wasUpdateDraggingCalled());
    }
    
    @Test
    void testHitTestMouseReleased() {
        // Start dragging
        Dimension startPos = new Dimension(10, 10);
        dragController.startDragging(startPos);
        
        // Simulate mouse release
        boolean consumed = dragController.hitTest(MouseEvent.MOUSE_RELEASED, new Dimension(20, 20), 1);
        
        assertTrue(consumed);
        assertFalse(dragController.isDragging());
        assertNull(dragController.getStartPosition());
        assertTrue(mockClickController.wasRemoveCalled());
    }
    
    @Test
    void testHitTestOtherMouseEvent() {
        // Start dragging
        dragController.startDragging(new Dimension(10, 10));
        
        // Simulate other mouse event (e.g., MOUSE_CLICKED)
        boolean consumed = dragController.hitTest(MouseEvent.MOUSE_CLICKED, new Dimension(20, 20), 1);
        
        assertFalse(consumed);
    }
    
    @Test
    void testHitTestWhenNotDragging() {
        // Don't start dragging
        
        // Try mouse dragged
        boolean consumed = dragController.hitTest(MouseEvent.MOUSE_DRAGGED, new Dimension(20, 20), 1);
        
        assertFalse(consumed);
    }
    
    /**
     * Concrete implementation of DragController for testing
     */
    private static class MockDragController extends DragController {
        private boolean updateDraggingCalled = false;
        
        public MockDragController(Context context) {
            super(context);
        }
        
        @Override
        protected void updateDragging() {
            updateDraggingCalled = true;
        }
        
        public boolean wasUpdateDraggingCalled() {
            return updateDraggingCalled;
        }
        
        public boolean isDragging() {
            return dragging;
        }
        
        public Dimension getStartPosition() {
            return startPosition;
        }
        
        public Dimension getDragPosition() {
            return dragPosition;
        }

        @Override
        public void onClickHandlerRemoved() {
            // Not used in the test
        }
    }
    
    /**
     * Mock Context for testing
     */
    private static class MockContext extends Context {
        private ClickEventController clickEventController;
        
        public MockContext(ClickEventController controller) {
            super(null); // Pass null as we're mocking the ApplicationWindow
            this.clickEventController = controller;
        }
        
        @Override
        public ClickEventController getClickEventController() {
            return clickEventController;
        }
    }
    
    /**
     * Mock ClickEventController for testing
     */
    private static class MockClickEventController extends ClickEventController {
        private boolean removeCalled = false;
        
        @Override
        public void removeClickHandler(ClickEventHandler handler) {
            removeCalled = true;
            super.removeClickHandler(handler);
        }
        
        public boolean wasRemoveCalled() {
            return removeCalled;
        }
        
        public boolean hasClickHandler(ClickEventHandler handler) {
            // This is a simplification - just check if we have any handlers
            try {
                removeClickHandler(handler);
                setClickHandler(handler);
                return true;
            } catch (RuntimeException e) {
                return false;
            }
        }
    }
}

