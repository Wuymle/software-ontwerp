package clutter.layoutwidgets;

import clutter.abstractwidgets.FlexibleWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;

public class ConstrainedBox extends FlexibleWidget {
    private int boxWidth;
    private int boxHeight;
    private int maxWidth;
    private int maxHeight;
    private int minWidth;
    private int minHeight;

    public ConstrainedBox(Widget child) {
        super(child, 1);
    }

    public ConstrainedBox setWidth(int width) {
        this.boxWidth = width;
        return this;
    }

    public ConstrainedBox setHeight(int height) {
        this.boxHeight = height;
        return this;
    }

    public ConstrainedBox setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        return this;
    }

    public ConstrainedBox setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }

    public ConstrainedBox setMinWidth(int minWidth) {
        this.minWidth = minWidth;
        return this;
    }

    public ConstrainedBox setMinHeight(int minHeight) {
        this.minHeight = minHeight;
        return this;
    }

    @Override
    public void layout(Dimension maxSize) {
        size = maxSize;
        if (boxWidth != 0)
            size = size.withX(boxWidth);
        if (boxHeight != 0)
            size = size.withY(boxHeight);
        if (maxWidth != 0)
            size = size.withX(Math.min(maxWidth, size.x()));
        if (maxHeight != 0)
            size = size.withY(Math.min(maxHeight, size.y()));
        if (minWidth != 0)
            size = size.withX(Math.max(minWidth, size.x()));
        if (minHeight != 0)
            size = size.withY(Math.max(minHeight, size.y()));
        super.layout(size);
    }
}
