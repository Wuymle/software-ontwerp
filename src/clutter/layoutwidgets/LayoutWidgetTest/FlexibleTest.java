package clutter.layoutwidgets.LayoutWidgetTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;
import clutter.layoutwidgets.Flexible;
import clutter.layoutwidgets.enums.Alignment;
import java.awt.Graphics;
import java.lang.reflect.Field;

/**
 * Unit tests for the Flexible widget class.
 */
public class FlexibleTest {

    private MockWidget mockWidget;
    private Flexible flexible;

    /**
     * Setup test widgets before each test.
     */
    @BeforeEach
    public void setUp() {
        mockWidget = new MockWidget();
        flexible = new Flexible(mockWidget);
    }

    /**
     * Test constructor sets the default flex value to 1.
     */
    @Test
    public void testConstructorSetsDefaultFlex() {
        assertEquals(1, flexible.getFlex(), "Default flex value should be 1");
    }

    /**
     * Test constructor properly assigns the child widget.
     */
    @Test
    public void testConstructorSetsChildWidget() throws Exception {
        // Use reflection to access the private child field from the parent class
        Field childField = getChildField();
        childField.setAccessible(true);

        Object child = childField.get(flexible);
        assertSame(mockWidget, child, "Child widget should be the same as provided in constructor");
    }

    /**
     * Test that setHorizontalAlignment method sets the alignment and returns this.
     */
    @Test
    public void testSetHorizontalAlignment() throws Exception {
        // Use reflection to access the horizontalAlignment field
        Field horizontalAlignmentField = getHorizontalAlignmentField();
        horizontalAlignmentField.setAccessible(true);

        // Test for each alignment value
        for (Alignment alignment : Alignment.values()) {
            Flexible result = flexible.setHorizontalAlignment(alignment);

            assertSame(flexible, result, "Method should return the same Flexible instance");
            assertEquals(alignment, horizontalAlignmentField.get(flexible),
                    "Horizontal alignment should be set to " + alignment);
        }
    }

    /**
     * Test that setVerticalAlignment method sets the alignment and returns this.
     */
    @Test
    public void testSetVerticalAlignment() throws Exception {
        // Use reflection to access the verticalAlignment field
        Field verticalAlignmentField = getVerticalAlignmentField();
        verticalAlignmentField.setAccessible(true);

        // Test for each alignment value
        for (Alignment alignment : Alignment.values()) {
            Flexible result = flexible.setVerticalAlignment(alignment);

            assertSame(flexible, result, "Method should return the same Flexible instance");
            assertEquals(alignment, verticalAlignmentField.get(flexible),
                    "Vertical alignment should be set to " + alignment);
        }
    }

    /**
     * Test method chaining of alignment setters.
     */
    @Test
    public void testMethodChaining() throws Exception {
        Field horizontalAlignmentField = getHorizontalAlignmentField();
        Field verticalAlignmentField = getVerticalAlignmentField();
        horizontalAlignmentField.setAccessible(true);
        verticalAlignmentField.setAccessible(true);

        flexible.setHorizontalAlignment(Alignment.CENTER).setVerticalAlignment(Alignment.END);

        assertEquals(Alignment.CENTER, horizontalAlignmentField.get(flexible),
                "Horizontal alignment should be CENTER after chaining");
        assertEquals(Alignment.END, verticalAlignmentField.get(flexible),
                "Vertical alignment should be END after chaining");
    }

    /**
     * Helper method to get the child field from the parent class hierarchy.
     */
    private Field getChildField() throws NoSuchFieldException {
        Class<?> clazz = flexible.getClass().getSuperclass(); // FlexibleWidget
        clazz = clazz.getSuperclass(); // SingleChildWidget
        return clazz.getDeclaredField("child");
    }

    /**
     * Helper method to get the horizontalAlignment field from the class hierarchy.
     */
    private Field getHorizontalAlignmentField() throws NoSuchFieldException {
        Class<?> clazz = flexible.getClass().getSuperclass(); // FlexibleWidget
        clazz = clazz.getSuperclass(); // SingleChildWidget
        return clazz.getDeclaredField("horizontalAlignment");
    }

    /**
     * Helper method to get the verticalAlignment field from the class hierarchy.
     */
    private Field getVerticalAlignmentField() throws NoSuchFieldException {
        Class<?> clazz = flexible.getClass().getSuperclass(); // FlexibleWidget
        clazz = clazz.getSuperclass(); // SingleChildWidget
        return clazz.getDeclaredField("verticalAlignment");
    }

    /**
     * Mock Widget implementation for testing.
     */
    private static class MockWidget extends Widget {
        public MockWidget() {
            // Initialize with some position and size to avoid NPEs
            this.position = new Dimension(0, 0);
            this.size = new Dimension(100, 100);
            this.preferredSize = new Dimension(100, 100);
        }

        @Override
        public void measure() {
            // Mock implementation
            preferredSize = new Dimension(100, 100);
        }

        @Override
        public void paint(Graphics g) {
            // Mock implementation, no-op for testing
        }
    }
}
