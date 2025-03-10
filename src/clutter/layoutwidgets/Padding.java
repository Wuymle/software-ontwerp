package clutter.layoutwidgets;

import static clutter.core.Dimension.add;
import static clutter.core.Dimension.subtract;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;
import clutter.layoutwidgets.enums.Alignment;

public class Padding extends SingleChildWidget {
    private int left;
    private int right;
    private int top;
    private int bottom;

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
    public void layout(Dimension maxSize) {
        Dimension extraSize = new Dimension(left + right, top + bottom);
        super.layout(subtract(maxSize, extraSize));
        size = add(size, extraSize);
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
