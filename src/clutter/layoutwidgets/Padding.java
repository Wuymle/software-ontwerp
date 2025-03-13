package clutter.layoutwidgets;

import static clutter.core.Dimension.add;
import static clutter.core.Dimension.max;
import static clutter.core.Dimension.min;
import static clutter.core.Dimension.subtract;

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
    public void measure() {
        super.measure();
        preferredSize = add(preferredSize, new Dimension(left + right, top + bottom));
    }

    /**
     * layout the widget
     * @param minSize the minimum size
     * @param maxSize the maximum size
     */
    @Override
    public void layout(Dimension minSize, Dimension maxSize) {
        size = min(maxSize, max(minSize, preferredSize));
        child.layout(new Dimension(0, 0),
                subtract(maxSize, new Dimension(left + right, top + bottom)));
    }

    /**
     * @param left the left padding
     * @return self
     */
    public Padding left(int left) {
        this.left = left;
        return this;
    }

    /**
     * @param right the right padding
     * @return self
     */
    public Padding right(int right) {
        this.right = right;
        return this;
    }

    /**
     * @param top the top padding
     * @return self
     */
    public Padding top(int top) {
        this.top = top;
        return this;
    }

    /**
     * @param bottom the bottom padding
     * @return self
     */
    public Padding bottom(int bottom) {
        this.bottom = bottom;
        return this;
    }

    /**
     * @param horizontal the horizontal padding
     * @return self
     */
    public Padding horizontal(int horizontal) {
        left = horizontal;
        right = horizontal;
        return this;
    }

    /**
     * @param vertical the vertical padding
     * @return self
     */
    public Padding vertical(int vertical) {
        top = vertical;
        bottom = vertical;
        return this;
    }

    /**
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
