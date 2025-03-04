package clutter.abstractwidgets;

import clutter.core.Dimension;

public abstract class   FlexibleWidget extends SingleChildWidget {
    private int flex;
    private int maxWidth;
    private int maxHeight;

    protected FlexibleWidget(Widget child, int flex) {
        super(child);
        this.flex = flex;
    }

    public int getFlex() {
        return flex;
    }

    public FlexibleWidget setFlex(int flex) {
        this.flex = flex;
        return this;
    }

    public FlexibleWidget setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        return this;
    }

    public FlexibleWidget setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }

    public void layout(Dimension maxSize) {
        if (maxSize.getArea() == 0)
            System.err.println("WARNING: FLEXIBLE HAS SIZE 0");
        if (maxWidth != 0)
            maxSize = maxSize.withX(Math.min(maxWidth, maxSize.x()));
        if (maxHeight != 0)
            maxSize = maxSize.withY(Math.min(maxHeight, maxSize.y()));
        super.layout(maxSize);
        size = maxSize;
    }
}
