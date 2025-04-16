package clutter.layoutwidgets;

import static clutter.core.Dimension.max;
import static clutter.core.Dimension.min;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;
import clutter.layoutwidgets.enums.Alignment;

/**
 * A widget that adds padding around its child widget.
 */
public class Padding extends SingleChildWidget {
    private int left = 0;
    private int right = 0;
    private int top = 0;
    private int bottom = 0;

    /**
     * constructor for the padding widget
     * 
     * @param child the child widget
     */
    public Padding(Widget child) {
        super(child);
        horizontalAlignment = Alignment.CENTER;
        verticalAlignment = Alignment.CENTER;
    }

    /**
     * measure the widget
     */
    @Override
    public void runMeasure() {
        super.runMeasure();
        preferredSize = preferredSize.addX(left + right).addY(top + bottom);
    }

    /**
     * layout the widget
     * 
     * @param minSize the minimum size
     * @param maxSize the maximum size
     */
    @Override
    public void runLayout(Dimension minSize, Dimension maxSize) {
        size = min(maxSize, max(minSize, preferredSize));
        child.layout(new Dimension(0, 0), size.addX(-left - right).addY(-top - bottom));
    }

    /**
     * set the left padding
     * 
     * @param left the left padding
     * @return self
     */
    public Padding left(int left) {
        this.left = left;
        return this;
    }

    /**
     * set the right padding
     * 
     * @param right the right padding
     * @return self
     */
    public Padding right(int right) {
        this.right = right;
        return this;
    }

    /**
     * set the top padding
     * 
     * @param top the top padding
     * @return self
     */
    public Padding top(int top) {
        this.top = top;
        return this;
    }

    /**
     * set the bottom padding
     * 
     * @param bottom the bottom padding
     * @return self
     */
    public Padding bottom(int bottom) {
        this.bottom = bottom;
        return this;
    }

    /**
     * set the horizontal padding
     * 
     * @param horizontal the horizontal padding
     * @return self
     */
    public Padding horizontal(int horizontal) {
        left = horizontal;
        right = horizontal;
        return this;
    }

    /**
     * set the vertical padding
     * 
     * @param vertical the vertical padding
     * @return self
     */
    public Padding vertical(int vertical) {
        top = vertical;
        bottom = vertical;
        return this;
    }

    /**
     * set all the padding
     * 
     * @param all the padding
     * @return self
     */
    public Padding all(int all) {
        top = all;
        bottom = all;
        left = all;
        right = all;
        return this;
    }
}
