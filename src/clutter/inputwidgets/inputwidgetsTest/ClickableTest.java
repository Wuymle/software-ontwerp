package clutter.inputwidgets.inputwidgetsTest;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;
import clutter.decoratedwidgets.Text;
import clutter.inputwidgets.Clickable;

/**
 * Test class for the Clickable widget
 */
public class ClickableTest {

    private Widget childWidget;
    private AtomicBoolean clickPerformed;
    private Runnable onClick;
    private Clickable clickable;

    @BeforeEach
    void setUp() {
        childWidget = new Text("Test Button");
        clickPerformed = new AtomicBoolean(false);
        onClick = () -> clickPerformed.set(true);
        clickable = new Clickable(childWidget, onClick, 1);
    }

    /**
     * Test for the constructor to ensure it properly initializes fields
     */
    @Test
    void testConstructor() throws Exception {
        // Verify child widget was set correctly (via superclass)
        Field childField = clickable.getClass().getSuperclass().getDeclaredField("child");
        childField.setAccessible(true);
        assertEquals(childWidget, childField.get(clickable),
                "Child widget should be set correctly");

        // Verify onClick and clickCount fields
        Field onClickField = Clickable.class.getDeclaredField("onClick");
        onClickField.setAccessible(true);
        assertEquals(onClick, onClickField.get(clickable), "onClick should be set correctly");

        Field clickCountField = Clickable.class.getDeclaredField("clickCount");
        clickCountField.setAccessible(true);
        assertEquals(1, clickCountField.get(clickable), "clickCount should be 1");
    }

    /**
     * Test hitTest method when the click is not a MOUSE_CLICKED event
     */
    @Test
    void testHitTestWithNonClickEvent() {
        // Set position and size for the widget
        clickable.setPosition(new Dimension(10, 10));
        clickable.setSize(new Dimension(100, 30));

        // Test with MOUSE_PRESSED event instead of MOUSE_CLICKED
        boolean result = clickable.hitTest(MouseEvent.MOUSE_PRESSED, new Dimension(50, 25), 1);

        // Should return false for non-MOUSE_CLICKED events
        assertFalse(result, "Should return false for non-MOUSE_CLICKED events");
        assertFalse(clickPerformed.get(), "onClick should not be triggered");
    }

    /**
     * Test hitTest method when the click position is within bounds and click count matches
     */
    @Test
    void testHitTestWithCorrectClickAndPosition() {
        // Set position and size for the widget
        clickable.setPosition(new Dimension(10, 10));
        clickable.setSize(new Dimension(100, 30));

        // Test with MOUSE_CLICKED event inside the widget's bounds
        boolean result = clickable.hitTest(MouseEvent.MOUSE_CLICKED, new Dimension(50, 25), 1);

        // Should return true and trigger onClick
        assertTrue(result, "Should return true for valid click inside bounds");
        assertTrue(clickPerformed.get(), "onClick should be triggered");
    }

    /**
     * Test hitTest method when the click position is outside bounds
     */
    @Test
    void testHitTestWithClickOutsideBounds() {
        // Set position and size for the widget
        clickable.setPosition(new Dimension(10, 10));
        clickable.setSize(new Dimension(100, 30));

        // Test with MOUSE_CLICKED event outside the widget's bounds
        boolean result = clickable.hitTest(MouseEvent.MOUSE_CLICKED, new Dimension(150, 50), 1);

        // Should return false and not trigger onClick
        assertFalse(result, "Should return false for click outside bounds");
        assertFalse(clickPerformed.get(), "onClick should not be triggered");
    }

    /**
     * Test hitTest method when click count doesn't match
     */
    @Test
    void testHitTestWithWrongClickCount() {
        // Set position and size for the widget
        clickable.setPosition(new Dimension(10, 10));
        clickable.setSize(new Dimension(100, 30));

        // Test with correct event and position but wrong click count (2 instead of 1)
        boolean result = clickable.hitTest(MouseEvent.MOUSE_CLICKED, new Dimension(50, 25), 2);

        // Should return false and not trigger onClick
        assertFalse(result, "Should return false for incorrect click count");
        assertFalse(clickPerformed.get(), "onClick should not be triggered");
    }

    /**
     * Test hitTest method with double-click Clickable
     */
    @Test
    void testHitTestWithDoubleClick() {
        // Create a clickable that responds to double clicks
        Clickable doubleClickable = new Clickable(childWidget, onClick, 2);
        doubleClickable.setPosition(new Dimension(10, 10));
        doubleClickable.setSize(new Dimension(100, 30));

        // Test with single click (should fail)
        boolean resultSingle =
                doubleClickable.hitTest(MouseEvent.MOUSE_CLICKED, new Dimension(50, 25), 1);
        assertFalse(resultSingle,
                "Should return false for single click when double click is expected");
        assertFalse(clickPerformed.get(), "onClick should not be triggered");

        // Test with double click (should succeed)
        boolean resultDouble =
                doubleClickable.hitTest(MouseEvent.MOUSE_CLICKED, new Dimension(50, 25), 2);
        assertTrue(resultDouble, "Should return true for double click");
        assertTrue(clickPerformed.get(), "onClick should be triggered");
    }

    /**
     * Test that child widget's hitTest is respected
     */
    @Test
    void testChildHitTestTakesPrecedence() throws Exception {
        // Create a mock child widget that always claims hits
        Widget mockChild = new Widget() {
            @Override
            public void measure() {}

            @Override
            public void paint(java.awt.Graphics g) {}

            @Override
            public boolean hitTest(int id, Dimension hitPos, int clickCount) {
                return true; // Always claim the hit
            }
        };

        Clickable clickableWithMockChild = new Clickable(mockChild, onClick, 1);
        clickableWithMockChild.setPosition(new Dimension(10, 10));
        clickableWithMockChild.setSize(new Dimension(100, 30));

        // When child claims the hit, parent's onClick should not be triggered
        boolean result =
                clickableWithMockChild.hitTest(MouseEvent.MOUSE_CLICKED, new Dimension(50, 25), 1);

        assertTrue(result, "Should return true when child claims the hit");
        assertFalse(clickPerformed.get(), "onClick should not be triggered when child claims hit");
    }
}
