package clutter.layoutwidgets;

import static clutter.core.Dimension.add;
import static clutter.core.Dimension.max;
import static clutter.core.Dimension.min;
import static clutter.core.Dimension.subtract;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;
import clutter.layoutwidgets.enums.Alignment;

public class Padding extends SingleChildWidget {
    private int left = 0;
    private int right = 0;
    private int top = 0;
    private int bottom = 0;

    public Padding(Widget child) {
        super(child);
        horizontalAlignment = Alignment.CENTER;
        verticalAlignment = Alignment.CENTER;
    }

    @Override
    public void measure() {
        super.measure();
        preferredSize = add(preferredSize, new Dimension(left + right, top + bottom));
    }

    @Override
    public void layout(Dimension minSize, Dimension maxSize) {
        size = min(maxSize, max(minSize, preferredSize));
        child.layout(new Dimension(0, 0),
                subtract(maxSize, new Dimension(left + right, top + bottom)));
    }

    public Padding left(int left) {
        this.left = left;
        return this;
    }

    public Padding right(int right) {
        this.right = right;
        return this;
    }

    public Padding top(int top) {
        this.top = top;
        return this;
    }

    public Padding bottom(int bottom) {
        this.bottom = bottom;
        return this;
    }

    public Padding horizontal(int horizontal) {
        left = horizontal;
        right = horizontal;
        return this;
    }

    public Padding vertical(int vertical) {
        top = vertical;
        bottom = vertical;
        return this;
    }

    public Padding all(int all) {
        top = all;
        bottom = all;
        left = all;
        right = all;
        return this;
    }
}
