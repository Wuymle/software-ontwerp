package clutter.layoutwidgets;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;

public class ConstrainedBox extends SingleChildWidget {
    private int boxWidth;
    private int boxHeight;
    private int maxWidth;
    private int maxHeight;
    private int minWidth;
    private int minHeight;

    public ConstrainedBox(Widget child) {
        super(child);
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
