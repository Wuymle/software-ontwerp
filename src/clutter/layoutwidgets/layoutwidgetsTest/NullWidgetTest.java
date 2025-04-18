package clutter.layoutwidgets.layoutwidgetsTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.awt.Graphics;

import clutter.core.Dimension;
import clutter.layoutwidgets.NullWidget;
import application.test.MockWidget;

class NullWidgetTest {

    @Test
    void testNullWidgetConstructor() {
        // Create a NullWidget
        NullWidget nullWidget = new NullWidget();

        // Assert that the widget is created successfully
        assertNotNull(nullWidget);
    }

    @Test
    void testNullWidgetMeasure() {
        // Create a NullWidget
        NullWidget nullWidget = new NullWidget();

        // Measure the widget
        nullWidget.measure();

        // The NullWidget should have zero preferred size
        assertEquals(new Dimension(0, 0), nullWidget.getPreferredSize());
    }

    @Test
    void testNullWidgetLayout() {
        // Create a NullWidget
        NullWidget nullWidget = new NullWidget();

        // Define minimum and maximum sizes for layout
        Dimension minSize = new Dimension(50, 50);
        Dimension maxSize = new Dimension(200, 200);

        // Layout the widget
        nullWidget.layout(minSize, maxSize);

        // The NullWidget should have the minimum size (as its preferred size is zero)
        assertEquals(minSize, nullWidget.getSize());
    }

    @Test
    void testNullWidgetPaint() {
        // Create a NullWidget
        NullWidget nullWidget = new NullWidget();

        // Since we can't directly test void methods without side effects,
        // we can just verify that calling paint doesn't throw exceptions
        assertDoesNotThrow(() -> {
            // This will be a null operation but shouldn't cause errors
            nullWidget.paint(null);
        });
    }

    @Test
    void testNullWidgetHitTest() {
        // Create a NullWidget
        NullWidget nullWidget = new NullWidget();

        // Set position and size for hit testing
        nullWidget.setPosition(new Dimension(10, 10));
        nullWidget.setSize(new Dimension(20, 20));

        // Hit test - should return false as NullWidget doesn't handle interactions
        boolean hitResult = nullWidget.hitTest(1, new Dimension(15, 15), 1);

        // The NullWidget should not respond to hit tests
        assertFalse(hitResult);
    }
}
