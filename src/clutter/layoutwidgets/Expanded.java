package clutter.layoutwidgets;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;

/**
 * A widget that expands its child widget.
 */
public class Expanded extends SingleChildWidget {

    /**
     * @param child the child widget
     */
    public Expanded(Widget child) {
        super(child);
    }

    /**
     * @param minSize the minimum size
     * @param maxSize the maximum size
     */
    @Override
    public void layout(Dimension minSize, Dimension maxSize) {
        super.layout(maxSize, maxSize);
    }
}
