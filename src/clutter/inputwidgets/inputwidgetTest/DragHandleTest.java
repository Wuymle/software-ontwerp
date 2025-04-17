package clutter.inputwidgets.inputwidgetTest;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.event.MouseEvent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;
import clutter.inputwidgets.DragHandle;
import application.test.MockWidget;
import application.test.TestHelper;

class DragHandleTest {

    private boolean dragCallbackCalled;
    private Dimension callbackDimension;
    private TestDragHandle dragHandle;
    private MockWidget childWidget;

    @BeforeEach
    void setUp() {
        dragCallbackCalled = false;
        callbackDimension = null;
        childWidget = new MockWidget();
        dragHandle = new TestDragHandle(childWidget, position -> {
            dragCallbackCalled = true;
            callbackDimension = position;
        });

        // Set position and size for the drag handle
        dragHandle.setPosition(new Dimension(0, 0));
        dragHandle.setSize(new Dimension(100, 100));
        dragHandle.positionChildren(); // Position the child widget
    }

    @Test
    void testConstructor() {
        // Test that the drag handle was properly initialized
        assertNotNull(dragHandle);
        assertEquals(childWidget, dragHandle.getChild());
    }

    @Test
    void testHitTestWithinBounds() {
        // Simulate a mouse press within the bounds of the drag handle
        boolean handled = dragHandle.hitTest(MouseEvent.MOUSE_PRESSED, new Dimension(50, 50), 1);

        // Verify the callback was called with the correct position
        assertTrue(handled);
        assertTrue(dragCallbackCalled);
        assertEquals(new Dimension(50, 50), callbackDimension);
    }

    @Test
    void testHitTestOutsideBounds() {
        // Simulate a mouse press outside the bounds of the drag handle
        boolean handled = dragHandle.hitTest(MouseEvent.MOUSE_PRESSED, new Dimension(150, 150), 1);

        // Verify the callback was not called
        assertFalse(handled);
        assertFalse(dragCallbackCalled);
        assertNull(callbackDimension);
    }

    @Test
    void testHitTestWithOtherMouseEvent() {
        // Simulate a mouse event that is not MOUSE_PRESSED
        boolean handled = dragHandle.hitTest(MouseEvent.MOUSE_RELEASED, new Dimension(50, 50), 1);

        // Verify the callback was not called
        assertFalse(handled);
        assertFalse(dragCallbackCalled);
        assertNull(callbackDimension);
    }

    @Test
    void testHitTestWithMultipleClicks() {
        // Simulate a double-click event
        boolean handled = dragHandle.hitTest(MouseEvent.MOUSE_PRESSED, new Dimension(50, 50), 2);

        // Verify the callback was still called regardless of click count
        assertTrue(handled);
        assertTrue(dragCallbackCalled);
        assertEquals(new Dimension(50, 50), callbackDimension);
    }

    @Test
    void testHitTestWithEdgeCases() {
        // Test the edge of the bounds (0,0)
        dragCallbackCalled = false;
        callbackDimension = null;
        boolean handled = dragHandle.hitTest(MouseEvent.MOUSE_PRESSED, new Dimension(0, 0), 1);
        assertTrue(handled);
        assertTrue(dragCallbackCalled);

        // Test the edge of the bounds (100,100)
        dragCallbackCalled = false;
        callbackDimension = null;
        handled = dragHandle.hitTest(MouseEvent.MOUSE_PRESSED, new Dimension(99, 99), 1);
        assertTrue(handled);
        assertTrue(dragCallbackCalled);
    }

    @Test
    void testWithNullChild() {
        // Create a drag handle with null child
        TestDragHandle nullChildHandle = new TestDragHandle(null, position -> {
            dragCallbackCalled = true;
            callbackDimension = position;
        });
        nullChildHandle.setPosition(new Dimension(0, 0));
        nullChildHandle.setSize(new Dimension(100, 100));

        // Should still work with a null child
        boolean handled =
                nullChildHandle.hitTest(MouseEvent.MOUSE_PRESSED, new Dimension(50, 50), 1);
        assertTrue(handled);
        assertTrue(dragCallbackCalled);
    }

    /**
     * Helper class to access the child widget for testing
     */
    private static class TestDragHandle extends DragHandle {
        public TestDragHandle(Widget child,
                java.util.function.Consumer<Dimension> onStartDragging) {
            super(child, onStartDragging);
        }

        public Widget getChild() {
            return child;
        }
    }
}
