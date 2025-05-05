package clutter.layoutwidgets.layoutwidgetsTest;

import clutter.abstractwidgets.LeafWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;
import clutter.layoutwidgets.Row;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.awt.Graphics;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Row widget in clutter.layoutwidgets
 */
public class RowTest {

    // Test widgets that will be used in multiple tests
    private MockWidget widget1;
    private MockWidget widget2;
    private MockWidget widget3;

    // Mock flexible widget for testing flexible layouts
    private MockFlexibleWidget flexWidget1;
    @BeforeEach
    public void setUp() {
        // Create widgets with specific dimensions for testing
        widget1 = new MockWidget(new Dimension(50, 30));
        widget2 = new MockWidget(new Dimension(70, 40));
        widget3 = new MockWidget(new Dimension(40, 50));

        // Create flexible widgets with different flex factors
        flexWidget1 = new MockFlexibleWidget(new Dimension(0, 30), 1);
        new MockFlexibleWidget(new Dimension(0, 40), 2);
    }

    @Test
    public void testConstructorWithVarargs() {
        // Create row with varargs constructor
        Row row = new Row(widget1, widget2);

        // Verify the row has the correct children
        assertEquals(2, row.children.size());
        assertSame(widget1, row.children.get(0));
        assertSame(widget2, row.children.get(1));
    }

    @Test
    public void testConstructorWithList() {
        // Create row with List constructor
        List<Widget> widgetList = Arrays.asList(widget1, widget2, widget3);
        Row row = new Row(widgetList);

        // Verify the row has the correct children
        assertEquals(3, row.children.size());
        assertSame(widget1, row.children.get(0));
        assertSame(widget2, row.children.get(1));
        assertSame(widget3, row.children.get(2));
    }

    @Test
    public void testMeasureWithFixedWidgets() {
        // Create row with fixed widgets
        Row row = new Row(widget1, widget2);

        // Measure the row
        row.measure();

        // Expected size: sum of widths (50+70=120) and max height (40)
        Dimension expectedSize = new Dimension(120, 40);
        assertEquals(expectedSize, row.getPreferredSize());
    }

    @Test
    public void testMeasureWithFlexibleWidgets() {
        // Create row with flexible widgets
        Row row = new Row(widget1, flexWidget1);

        // Measure the row
        row.measure();

        // Expected size: Integer.MAX_VALUE for width (because of flexible widgets)
        // and max height (30)
        Dimension expectedSize = new Dimension(Integer.MAX_VALUE, 30);
        assertEquals(expectedSize, row.getPreferredSize());
    }

    @Test
    public void testConstructWithChildren() {
        // Create row with initial children
        Row row = new Row(widget1, widget2);

        // Verify children were added
        assertEquals(2, row.children.size());
        assertSame(widget1, row.children.get(0));
        assertSame(widget2, row.children.get(1));

        // Create a new Row with different children
        Row updatedRow = new Row(widget2, widget3);

        // Verify the new children configuration
        assertEquals(2, updatedRow.children.size());
        assertSame(widget2, updatedRow.children.get(0));
        assertSame(widget3, updatedRow.children.get(1));
    }

    /**
     * Mock implementation of Widget for testing
     */
    private class MockWidget extends LeafWidget {
        private Dimension preferredSize;
        private Dimension size;
        private Dimension position = new Dimension(0, 0);

        @Override
        public void runMeasure() {
            // Mock implementation for testing
            this.size = preferredSize;
        }

        @Override
        public void runPaint(Graphics graphics) {
            // Mock implementation for testing
            // No actual painting in tests
        }

        public MockWidget(Dimension preferredSize) {
            this.preferredSize = preferredSize;
            this.size = preferredSize;
        }

        @Override
        public Dimension getPreferredSize() {
            return preferredSize;
        }

        @Override
        public Dimension getSize() {
            return size;
        }

        @Override
        public void setPosition(Dimension position) {
            this.position = position;
        }

        @Override
        public Dimension getPosition() {
            return position;
        }
    }

    /**
     * Mock implementation of FlexibleWidget for testing
     */
    private class MockFlexibleWidget extends clutter.abstractwidgets.FlexibleWidget {
        private Dimension preferredSize;
        private Dimension size;
        private Dimension position = new Dimension(0, 0);
        private int flex;

        public MockFlexibleWidget(Dimension preferredSize, int flex) {
            super(widget1, flex); // Explicitly call the superclass constructor with the flex parameter
            this.preferredSize = preferredSize;
            this.size = preferredSize;
            this.flex = flex;
        }

        @Override
        public Dimension getPreferredSize() {
            return preferredSize;
        }

        @Override
        public Dimension getSize() {
            return size;
        }

        @Override
        public void setPosition(Dimension position) {
            this.position = position;
        }

        @Override
        public Dimension getPosition() {
            return position;
        }

        @Override
        public int getFlex() {
            return flex;
        }
    }
}
