package clutter.layoutwidgets.layoutwidgetsTest;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Graphics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;
import clutter.layoutwidgets.Padding;
import clutter.layoutwidgets.enums.Alignment;

class PaddingTest {

    private MockWidget mockChild;
    private Padding padding;

    @BeforeEach
    void setUp() {
        mockChild = new MockWidget();
        padding = new Padding(mockChild);
    }

    @Test
    void testConstructor() {
        // Test initial padding values
        assertEquals(0, (int) getPrivateField(padding, "left"),
                "Left padding should be initialized to 0");
        assertEquals(0, (int) getPrivateField(padding, "right"),
                "Right padding should be initialized to 0");
        assertEquals(0, (int) getPrivateField(padding, "top"),
                "Top padding should be initialized to 0");
        assertEquals(0, (int) getPrivateField(padding, "bottom"),
                "Bottom padding should be initialized to 0");

        // Test alignment settings
        assertEquals(Alignment.CENTER, getPrivateField(padding, "horizontalAlignment"),
                "Horizontal alignment should be CENTER");
        assertEquals(Alignment.CENTER, getPrivateField(padding, "verticalAlignment"),
                "Vertical alignment should be CENTER");
    }

    @Test
    void testLeftPadding() {
        Padding result = padding.left(10);
        assertEquals(10, (int) getPrivateField(padding, "left"),
                "Left padding should be set to 10");
        assertEquals(0, (int) getPrivateField(padding, "right"), "Right padding should remain 0");
        assertEquals(0, (int) getPrivateField(padding, "top"), "Top padding should remain 0");
        assertEquals(0, (int) getPrivateField(padding, "bottom"), "Bottom padding should remain 0");
        assertSame(padding, result, "Method should return this for chaining");
    }

    @Test
    void testRightPadding() {
        Padding result = padding.right(15);
        assertEquals(0, (int) getPrivateField(padding, "left"), "Left padding should remain 0");
        assertEquals(15, (int) getPrivateField(padding, "right"),
                "Right padding should be set to 15");
        assertEquals(0, (int) getPrivateField(padding, "top"), "Top padding should remain 0");
        assertEquals(0, (int) getPrivateField(padding, "bottom"), "Bottom padding should remain 0");
        assertSame(padding, result, "Method should return this for chaining");
    }

    @Test
    void testTopPadding() {
        Padding result = padding.top(20);
        assertEquals(0, (int) getPrivateField(padding, "left"), "Left padding should remain 0");
        assertEquals(0, (int) getPrivateField(padding, "right"), "Right padding should remain 0");
        assertEquals(20, (int) getPrivateField(padding, "top"), "Top padding should be set to 20");
        assertEquals(0, (int) getPrivateField(padding, "bottom"), "Bottom padding should remain 0");
        assertSame(padding, result, "Method should return this for chaining");
    }

    @Test
    void testBottomPadding() {
        Padding result = padding.bottom(25);
        assertEquals(0, (int) getPrivateField(padding, "left"), "Left padding should remain 0");
        assertEquals(0, (int) getPrivateField(padding, "right"), "Right padding should remain 0");
        assertEquals(0, (int) getPrivateField(padding, "top"), "Top padding should remain 0");
        assertEquals(25, (int) getPrivateField(padding, "bottom"),
                "Bottom padding should be set to 25");
        assertSame(padding, result, "Method should return this for chaining");
    }

    @Test
    void testHorizontalPadding() {
        Padding result = padding.horizontal(30);
        assertEquals(30, (int) getPrivateField(padding, "left"),
                "Left padding should be set to 30");
        assertEquals(30, (int) getPrivateField(padding, "right"),
                "Right padding should be set to 30");
        assertEquals(0, (int) getPrivateField(padding, "top"), "Top padding should remain 0");
        assertEquals(0, (int) getPrivateField(padding, "bottom"), "Bottom padding should remain 0");
        assertSame(padding, result, "Method should return this for chaining");
    }

    @Test
    void testVerticalPadding() {
        Padding result = padding.vertical(35);
        assertEquals(0, (int) getPrivateField(padding, "left"), "Left padding should remain 0");
        assertEquals(0, (int) getPrivateField(padding, "right"), "Right padding should remain 0");
        assertEquals(35, (int) getPrivateField(padding, "top"), "Top padding should be set to 35");
        assertEquals(35, (int) getPrivateField(padding, "bottom"),
                "Bottom padding should be set to 35");
        assertSame(padding, result, "Method should return this for chaining");
    }

    @Test
    void testAllPadding() {
        Padding result = padding.all(40);
        assertEquals(40, (int) getPrivateField(padding, "left"),
                "Left padding should be set to 40");
        assertEquals(40, (int) getPrivateField(padding, "right"),
                "Right padding should be set to 40");
        assertEquals(40, (int) getPrivateField(padding, "top"), "Top padding should be set to 40");
        assertEquals(40, (int) getPrivateField(padding, "bottom"),
                "Bottom padding should be set to 40");
        assertSame(padding, result, "Method should return this for chaining");
    }

    @Test
    void testChainedPaddingMethods() {
        padding.left(5).right(10).top(15).bottom(20);
        assertEquals(5, (int) getPrivateField(padding, "left"), "Left padding should be 5");
        assertEquals(10, (int) getPrivateField(padding, "right"), "Right padding should be 10");
        assertEquals(15, (int) getPrivateField(padding, "top"), "Top padding should be 15");
        assertEquals(20, (int) getPrivateField(padding, "bottom"), "Bottom padding should be 20");
    }

    @Test
    void testMeasure() {
        // Set child's preferred size
        mockChild.setPreferredSize(new Dimension(100, 80));

        // Set padding values
        padding.left(10).right(20).top(5).bottom(15);

        // Call measure
        padding.measure();

        // Check padding's preferred size includes padding
        Dimension preferredSize = padding.getPreferredSize();
        assertEquals(130, preferredSize.x(), "Width should be child width + left + right padding");
        assertEquals(100, preferredSize.y(),
                "Height should be child height + top + bottom padding");
    }

    @Test
    void testLayout() {
        // Setup
        mockChild.setPreferredSize(new Dimension(100, 80));
        padding.left(10).right(20).top(5).bottom(15);
        padding.measure(); // Need to measure first

        // Perform layout
        Dimension minSize = new Dimension(0, 0);
        Dimension maxSize = new Dimension(200, 200);
        padding.layout(minSize, maxSize);

        // Verify child was laid out with correct dimensions
        assertEquals(new Dimension(0, 0), mockChild.getLastLayoutMinSize(),
                "Child min size should be (0,0)");

        Dimension expectedMaxSize = new Dimension(100, 80);
        assertEquals(expectedMaxSize, mockChild.getLastLayoutMaxSize(),
                "Child max size should be adjusted for padding");
    }

    // Custom MockWidget inner class with required methods
    private static class MockWidget extends Widget {
        private Dimension lastLayoutMinSize;
        private Dimension lastLayoutMaxSize;

        @Override
        public void layout(Dimension minSize, Dimension maxSize) {
            this.lastLayoutMinSize = minSize;
            this.lastLayoutMaxSize = maxSize;
            this.size = maxSize;
        }

        @Override
        public void measure() {
            // Do nothing, we'll set preferred size manually
        }

        @Override
        public void paint(Graphics g) {
            // Empty implementation for testing purposes
        }

        public void setPreferredSize(Dimension size) {
            this.preferredSize = size;
        }

        public Dimension getLastLayoutMinSize() {
            return lastLayoutMinSize;
        }

        public Dimension getLastLayoutMaxSize() {
            return lastLayoutMaxSize;
        }
    }

    // Helper method to access private fields using reflection
    @SuppressWarnings("unchecked")
    private <T> T getPrivateField(Object obj, String fieldName) {
        try {
            var field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(obj);
        } catch (Exception e) {
            // Check in superclass if not found
            try {
                var field = obj.getClass().getSuperclass().getDeclaredField(fieldName);
                field.setAccessible(true);
                return (T) field.get(obj);
            } catch (Exception ex) {
                fail("Could not access field: " + fieldName + ", error: " + ex.getMessage());
                return null;
            }
        }
    }
}
