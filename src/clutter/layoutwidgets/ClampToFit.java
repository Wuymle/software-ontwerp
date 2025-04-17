package clutter.layoutwidgets;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;

/**
 * A widget that expands its child widget.
 */
public class ClampToFit extends SingleChildWidget {

    public ClampToFit() {};

    /**
     * constructor for the expanded widget
     * 
     * @param child the child widget
     */
    public ClampToFit(Widget child) {
        super(child);
    }

    /**
     * measure the expanded widget
     * 
     * @param minSize the minimum size
     * @param maxSize the maximum size
     */
    @Override
    protected void runMeasure() {
        super.runMeasure();
        preferredSize = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override
    protected void runLayout(Dimension minsize, Dimension maxSize) {
        size = maxSize;
        child.layout(maxSize, maxSize);
    }
}
