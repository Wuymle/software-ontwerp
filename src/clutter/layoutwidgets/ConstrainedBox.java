package clutter.layoutwidgets;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;

/**
 * A widget that constrains its child widget.
 */
public class ConstrainedBox extends SingleChildWidget {
    private int boxWidth;
    private int boxHeight;
    private int maxWidth;
    private int maxHeight;
    private int minWidth;
    private int minHeight;

    /**
     * constructor for the constrained box widget
     * @param child the child widget
     */
    public ConstrainedBox(Widget child) {
        super(child);
    }

    /**
     * set the width
     * @param width the width
     * @return self
     */
    public ConstrainedBox setWidth(int width) {
        this.boxWidth = width;
        return this;
    }

    /**
     * set the height
     * @param height the height
     * @return self
     */
    public ConstrainedBox setHeight(int height) {
        this.boxHeight = height;
        return this;
    }

    /**
     * set the max width
     * @param maxWidth the max width
     * @return self
     */
    public ConstrainedBox setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        return this;
    }

    /**
     * set the max height
     * @param maxHeight the max height
     * @return self
     */
    public ConstrainedBox setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }

    /**
     * set the min width
     * @param minWidth the min width
     * @return self
     */
    public ConstrainedBox setMinWidth(int minWidth) {
        this.minWidth = minWidth;
        return this;
    }

    /**
     * set the min height
     * @param minHeight the min height
     * @return self
     */
    public ConstrainedBox setMinHeight(int minHeight) {
        this.minHeight = minHeight;
        return this;
    }

    /**
     * Measure the widget.
     */
    @Override
    public void measure() {
        super.measure();
        if (boxWidth != 0)
            preferredSize = preferredSize.withX(boxWidth);
        if (boxHeight != 0)
            preferredSize = preferredSize.withY(boxHeight);
        if (maxWidth != 0)
            preferredSize = preferredSize.withX(Math.min(maxWidth, preferredSize.x()));
        if (maxHeight != 0)
            preferredSize = preferredSize.withY(Math.min(maxHeight, preferredSize.y()));
        if (minWidth != 0)
            preferredSize = preferredSize.withX(Math.max(minWidth, preferredSize.x()));
        if (minHeight != 0)
            preferredSize = preferredSize.withY(Math.max(minHeight, preferredSize.y()));
    }
}
