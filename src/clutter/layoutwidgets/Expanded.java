package clutter.layoutwidgets;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;

/**
 * A widget that expands its child widget.
 */
public class Expanded extends SingleChildWidget {

    /**
     * constructor for the expanded widget
     * @param child the child widget
     */
    public Expanded(Widget child) {
        super(child);
    }

    /**
     * measure the expanded widget
     * @param minSize the minimum size
     * @param maxSize the maximum size
     */
    @Override
    public void measure() {
        super.measure();
        preferredSize = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    /**
     * layout the expanded widget
     * @param minSize the minimum size
     * @param maxSize the maximum size
     */
    @Override
    public void layout(Dimension minSize, Dimension maxSize) {
        super.layout(maxSize, maxSize);
    }
}
