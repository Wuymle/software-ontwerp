package clutter.layoutwidgets.layoutwidgetsTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;
import clutter.decoratedwidgets.Text;
import clutter.layoutwidgets.Column;
import clutter.layoutwidgets.Flexible;
import clutter.layoutwidgets.enums.Alignment;

import java.awt.Graphics;
import java.util.Arrays;
import java.util.List;
import java.lang.reflect.Method;

/**
 * Unit tests for the Column widget class.
 */
public class ColumnTest {
    private Text text1;
    private Text text2;
    private Text text3;
    private Flexible flexibleWidget;
    private Column column;

    /**
     * Setup test widgets before each test.
     */
    @BeforeEach
    public void setUp() {
        text1 = new Text("Test 1");
        text2 = new Text("Test 2 with longer text");
        text3 = new Text("Test 3");
        flexibleWidget = new Flexible(new MockWidget(new Dimension(50, 30)));
    }

    /**
     * Test creating a Column with varargs constructor.
     */
    @Test
    public void testColumnConstructorWithVarargs() {
        column = new Column(text1, text2, text3);
        assertNotNull(column);
        // Since we can't directly access the children list, we'll verify that measure works
        // correctly
        // which indicates children were properly added
        column.measure();
        assertTrue(column.getPreferredSize().y() > 0);
    }

    /**
     * Test creating a Column with List constructor.
     */
    @Test
    public void testColumnConstructorWithList() {
        List<Widget> widgets = Arrays.asList(text1, text2, text3);
        column = new Column(widgets);
        assertNotNull(column);
        // Since we can't directly access the children list, we'll verify that measure works
        // correctly
        // which indicates children were properly added
        column.measure();
        assertTrue(column.getPreferredSize().y() > 0);
    }

    /**
     * Test the measure method of Column calculates correct preferred size.
     */
    @Test
    public void testMeasure() {
        column = new Column(text1, text2, text3);

        // First measure each child individually to get their sizes
        text1.measure();
        text2.measure();
        text3.measure();

        // Then measure the column
        column.measure();

        // Column's preferred width should be the max width of all children
        int expectedWidth =
                Math.max(Math.max(text1.getPreferredSize().x(), text2.getPreferredSize().x()),
                        text3.getPreferredSize().x());

        // Column's preferred height should be the sum of all children's heights
        int expectedHeight = text1.getPreferredSize().y() + text2.getPreferredSize().y()
                + text3.getPreferredSize().y();

        assertEquals(expectedWidth, column.getPreferredSize().x());
        assertEquals(expectedHeight, column.getPreferredSize().y());
    }

    /**
     * Test that a Column with flexible children sets preferredSize.y() to MAX_VALUE.
     */
    @Test
    public void testMeasureWithFlexibleChildren() {
        column = new Column(text1, flexibleWidget, text2);
        column.measure();

        assertEquals(Integer.MAX_VALUE, column.getPreferredSize().y());
    }

    /**
     * Test the layout method of Column with only inflexible widgets.
     */
    @Test
    public void testLayoutWithInflexibleWidgets() {
        MockWidget widget1 = new MockWidget(new Dimension(100, 40));
        MockWidget widget2 = new MockWidget(new Dimension(80, 30));

        column = new Column(widget1, widget2);
        column.measure();

        Dimension minSize = new Dimension(0, 0);
        Dimension maxSize = new Dimension(200, 200);
        column.layout(minSize, maxSize);

        // Column should adapt to children's sizes
        assertEquals(100, column.getSize().x());
        assertEquals(70, column.getSize().y());

        // Set position of column and force calling positionChildren()
        column.setPosition(new Dimension(10, 10));
        invokePositionChildren(column);

        // First widget should be at column's position
        assertEquals(10, widget1.getPosition().x());
        assertEquals(10, widget1.getPosition().y());

        // Second widget should be positioned below the first one
        assertEquals(10, widget2.getPosition().x());
        assertEquals(50, widget2.getPosition().y()); // 10 + 40
    }

    /**
     * Test the layout method with crossAxisAlignment=CENTER.
     */
    @Test
    public void testLayoutWithCenterAlignment() {
        MockWidget widget1 = new MockWidget(new Dimension(60, 40));
        MockWidget widget2 = new MockWidget(new Dimension(100, 30));

        column = new Column(widget1, widget2);
        column.setCrossAxisAlignment(Alignment.CENTER);
        column.measure();

        Dimension minSize = new Dimension(0, 0);
        Dimension maxSize = new Dimension(200, 200);
        column.layout(minSize, maxSize);
        column.setPosition(new Dimension(10, 10));
        invokePositionChildren(column);

        // Widget1 should be centered horizontally (manually calculate expected positions)
        int widget1ExpectedX = 10 + (column.getSize().x() - widget1.getSize().x()) / 2;
        assertEquals(widget1ExpectedX, widget1.getPosition().x());
        assertEquals(10, widget1.getPosition().y());

        // Widget2 should be below widget1 and centered horizontally
        int widget2ExpectedX = 10 + (column.getSize().x() - widget2.getSize().x()) / 2;
        assertEquals(widget2ExpectedX, widget2.getPosition().x());
        assertEquals(50, widget2.getPosition().y()); // 10 + 40
    }

    /**
     * Test the layout method with crossAxisAlignment=END.
     */
    @Test
    public void testLayoutWithEndAlignment() {
        MockWidget widget1 = new MockWidget(new Dimension(60, 40));
        MockWidget widget2 = new MockWidget(new Dimension(100, 30));

        column = new Column(widget1, widget2);
        column.setCrossAxisAlignment(Alignment.END);
        column.measure();

        Dimension minSize = new Dimension(0, 0);
        Dimension maxSize = new Dimension(200, 200);
        column.layout(minSize, maxSize);
        column.setPosition(new Dimension(10, 10));
        invokePositionChildren(column);

        // Widget1 should be aligned to the right
        int widget1ExpectedX = 10 + column.getSize().x() - widget1.getSize().x();
        assertEquals(widget1ExpectedX, widget1.getPosition().x());
        assertEquals(10, widget1.getPosition().y());

        // Widget2 should be below widget1 and aligned to the right
        int widget2ExpectedX = 10 + column.getSize().x() - widget2.getSize().x();
        assertEquals(widget2ExpectedX, widget2.getPosition().x());
        assertEquals(50, widget2.getPosition().y()); // 10 + 40
    }

    /**
     * Test the layout method with crossAxisAlignment=STRETCH.
     */
    @Test
    public void testLayoutWithStretchAlignment() {
        MockWidget widget1 = new MockWidget(new Dimension(60, 40));
        MockWidget widget2 = new MockWidget(new Dimension(100, 30));

        column = new Column(widget1, widget2);
        column.setCrossAxisAlignment(Alignment.STRETCH);
        column.measure();

        Dimension minSize = new Dimension(0, 0);
        Dimension maxSize = new Dimension(200, 200);
        column.layout(minSize, maxSize);

        // With STRETCH, children should be laid out with column's full width
        assertEquals(100, column.getSize().x());
        assertEquals(70, column.getSize().y());
    }

    /**
     * Test the layout method with both flexible and inflexible widgets.
     */
    @Test
    public void testLayoutWithFlexibleWidgets() {
        Widget inflexibleWidget = new MockWidget(new Dimension(100, 40));
        Flexible flex1 = new Flexible(new MockWidget(new Dimension(80, 50)));
        Flexible flex2 = new Flexible(new MockWidget(new Dimension(90, 60)));

        // Set flex values
        flex1.setFlex(1); // Default
        flex2.setFlex(2); // Twice the flexibility of flex1

        column = new Column(inflexibleWidget, flex1, flex2);
        column.measure();

        Dimension minSize = new Dimension(0, 0);
        Dimension maxSize = new Dimension(200, 300);
        column.layout(minSize, maxSize);

        // Column should fill max height due to flexible children
        assertEquals(100, column.getSize().x());
        assertEquals(300, column.getSize().y());

        // Remaining space after inflexible widget (300 - 40 = 260)
        // should be divided according to flex ratio (1:2)
        // flex1 gets 1/3 of 260 = 86.67, flex2 gets 2/3 of 260 = 173.33
        int flex1ExpectedHeight = 260 / 3;
        int flex2ExpectedHeight = 2 * 260 / 3;

        // Allow some margin for rounding
        assertTrue(Math.abs(flex1.getSize().y() - flex1ExpectedHeight) <= 1);
        assertTrue(Math.abs(flex2.getSize().y() - flex2ExpectedHeight) <= 1);
    }

    /**
     * Test setCrossAxisAlignment method returns "this" for method chaining.
     */
    @Test
    public void testSetCrossAxisAlignmentReturnsThis() {
        column = new Column(text1, text2);
        Column result = column.setCrossAxisAlignment(Alignment.CENTER);
        assertSame(column, result);
    }

    /**
     * Helper method to invoke the private positionChildren() method using reflection
     */
    private void invokePositionChildren(Column column) {
        try {
            Method positionChildrenMethod = getPositionChildrenMethod();
            positionChildrenMethod.setAccessible(true);
            positionChildrenMethod.invoke(column);
        } catch (Exception e) {
            fail("Failed to invoke positionChildren method: " + e.getMessage());
        }
    }

    /**
     * Find the positionChildren method in the class hierarchy
     */
    private Method getPositionChildrenMethod() throws NoSuchMethodException {
        Class<?> clazz = Column.class;
        while (clazz != null) {
            try {
                return clazz.getDeclaredMethod("positionChildren");
            } catch (NoSuchMethodException e) {
                clazz = clazz.getSuperclass();
            }
        }
        throw new NoSuchMethodException("positionChildren method not found in class hierarchy");
    }

    /**
     * Mock Widget implementation for testing layout behaviors. This enhanced version includes
     * position verification.
     */
    private class MockWidget extends Widget {
        private Dimension fixedPreferredSize;

        public MockWidget(Dimension preferredSize) {
            this.fixedPreferredSize = preferredSize;
            // Initialize position to avoid NPEs
            this.position = new Dimension(0, 0);
        }

        @Override
        public void runMeasure() {
            preferredSize = fixedPreferredSize;
        }

        @Override
        public void runPaint(Graphics g) {
            // No-op for testing
        }
    }
}
