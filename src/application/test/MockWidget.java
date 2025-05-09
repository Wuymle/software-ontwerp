package application.test;

import java.lang.reflect.Field;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;
import clutter.abstractwidgets.MultiChildWidget;
import clutter.layoutwidgets.enums.Alignment; // Ensure Alignment is imported
import java.awt.Graphics;

public class MockWidget extends Widget {
    @Override
    public void runLayout(Dimension minSize, Dimension maxSize) {
        // Mock implementation
    }

    public void runPaint(Graphics g) {
        // Mock implementation
        System.out.println("Painting MockWidget");
    }

    public void runMeasure() {
        // Mock implementation
        System.out.println("Measuring MockWidget");
    }

    /**
     * Set the preferred size of the widget
     * 
     * @param preferredSize the preferred size
     */
    public void setPreferredSize(Dimension preferredSize) {
        this.preferredSize = preferredSize;
    }
}
