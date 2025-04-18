package clutter.layoutwidgets.layoutwidgetsTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.awt.Graphics;
import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;
import clutter.layoutwidgets.Expanded;

class ExpandedTest {

    private static class MockWidget extends Widget {
        @Override
        public void measure() {
            preferredSize = new Dimension(100, 100);
        }

        @Override
        public void paint(Graphics g) {
            // Mock implementation
        }
    }

    // Helper method to access the protected child field
    private Widget getChildFromWidget(Expanded expanded) throws Exception {
        Field childField = expanded.getClass().getSuperclass().getDeclaredField("child");
        childField.setAccessible(true);
        return (Widget) childField.get(expanded);
    }

    @Test
    void testConstructorWithChild() throws Exception {
        // Create a mock child widget
        MockWidget child = new MockWidget();

        // Create an Expanded widget with the child
        Expanded expanded = new Expanded(child);

        // Verify that the expanded widget has the child
        Widget childWidget = getChildFromWidget(expanded);
        assertSame(child, childWidget);
    }

    @Test
    void testConstructorWithNullChild() throws Exception {
        // Create an Expanded widget with null child
        Expanded expanded = new Expanded(null);

        // Verify that the expanded widget has null child
        Widget childWidget = getChildFromWidget(expanded);
        assertNull(childWidget);
    }

    @Test
    void testMeasure() {
        // Create a mock child widget
        MockWidget child = new MockWidget();

        // Create an Expanded widget with the child
        Expanded expanded = new Expanded(child);

        // Measure the expanded widget
        expanded.measure();

        // Verify that the preferred size is set to maximum value
        assertEquals(Integer.MAX_VALUE, expanded.getPreferredSize().x());
        assertEquals(Integer.MAX_VALUE, expanded.getPreferredSize().y());
    }

    @Test
    void testMeasureWithNullChild() {
        // Create an Expanded widget with null child
        Expanded expanded = new Expanded(null);

        // Measure the expanded widget
        expanded.measure();

        // Verify that the preferred size is set to maximum value
        assertEquals(Integer.MAX_VALUE, expanded.getPreferredSize().x());
        assertEquals(Integer.MAX_VALUE, expanded.getPreferredSize().y());
    }
}
