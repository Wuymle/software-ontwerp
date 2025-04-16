package clutter.abstractwidgets.abstractWidgetTest;

import clutter.abstractwidgets.FlexibleWidget;
import clutter.core.Dimension;
import clutter.abstractwidgets.Widget;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.awt.Graphics;

class FlexibleWidgetTest {

    private static class MockWidget extends Widget {
        @Override
        public void layout(Dimension minSize, Dimension maxSize) {
            // Mock implementation
        }

        public void paint(Graphics g) {
            // Mock implementation
            System.out.println("Painting MockWidget");
        }

        public void measure() {
            // Mock implementation
            System.out.println("Measuring MockWidget");
        }
    }

    @Test
    void testGetFlex() {
        FlexibleWidget widget = new FlexibleWidget(new MockWidget(), 5) {};
        assertEquals(5, widget.getFlex());
    }

    @Test
    void testSetFlex() {
        FlexibleWidget widget = new FlexibleWidget(new MockWidget(), 5) {};
        widget.setFlex(10);
        assertEquals(10, widget.getFlex());
    }

    @Test
    void testLayoutWithZeroArea() {
        FlexibleWidget widget = new FlexibleWidget(new MockWidget(), 5) {};
        Dimension minSize = new Dimension(0, 0);
        Dimension maxSize = new Dimension(0, 0);

        widget.layout(minSize, maxSize);
        // No exception should be thrown, but a warning should be logged
    }

    @Test
    void testLayoutWithValidDimensions() {
        FlexibleWidget widget = new FlexibleWidget(new MockWidget(), 5) {};
        Dimension minSize = new Dimension(10, 10);
        Dimension maxSize = new Dimension(100, 100);

        widget.layout(minSize, maxSize);
        // Ensure no exceptions are thrown
    }
}