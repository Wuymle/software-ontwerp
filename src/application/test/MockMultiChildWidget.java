package application.test;

import java.lang.reflect.Field;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;
import clutter.abstractwidgets.MultiChildWidget;
import clutter.layoutwidgets.enums.Alignment; // Ensure Alignment is imported
import java.awt.Graphics;

public class MockMultiChildWidget extends MultiChildWidget {
    public MockMultiChildWidget(Widget... children) {
        super(children);
    }

    @Override
    protected void layoutFlexibleWidgets(Dimension minSize, Dimension maxSize) {
        // Mock implementation
    }

    @Override
    protected void layoutInflexibleWidgets(Dimension minSize, Dimension maxSize) {
        // Mock implementation
    }

    @Override
    public MultiChildWidget setCrossAxisAlignment(Alignment alignment) {
        this.crossAxisAlignment = alignment;
        return this;
    }

    @Override
    protected void positionChildren() {
        // Mock implementation
    }

    public void measure() {
        // Mock implementation
    }
}