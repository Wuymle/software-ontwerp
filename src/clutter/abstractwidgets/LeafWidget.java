package clutter.abstractwidgets;

import clutter.core.Dimension;

public abstract class LeafWidget extends Widget {
    /**
     * Layout the widget
     * 
     * @param minSize the minimum size
     * @param maxSize the maximum size
     */
    protected void runLayout(Dimension minSize, Dimension maxSize) {
        size = Dimension.max(minSize, Dimension.min(maxSize, preferredSize));
    }
}
