package clutter.abstractwidgets;

import clutter.core.Dimension;
import clutter.debug.Debug;
import clutter.debug.DebugMode;

/**
 * A widget that can have a flexible size.
 */
public abstract class FlexibleWidget extends SingleChildWidget {
    private int flex;

    /**
     * Constructor for the flexible widget.
     * 
     * @param child the child widget
     * @param flex the flex value
     */
    protected FlexibleWidget(Widget child, int flex) {
        super(child);
        this.flex = flex;
    }

    /**
     * get the flex value
     * 
     * @return the flex value
     */
    public int getFlex() {
        return flex;
    }

    /**
     * set the flex value
     * 
     * @param flex the flex value
     * @return self
     */
    public FlexibleWidget setFlex(int flex) {
        this.flex = flex;
        return this;
    }

    /**
     * Layout the widget
     * 
     * @param minSize the minimum size
     * @param maxSize the maximum size
     */
    public void layout(Dimension minSize, Dimension maxSize) {
        if (maxSize.getArea() == 0)
            Debug.warn(this, DebugMode.LAYOUT, "WARNING: FLEXIBLE HAS SIZE 0");
        super.layout(minSize, maxSize);
    }
}
