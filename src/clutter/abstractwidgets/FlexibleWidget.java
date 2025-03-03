package clutter.abstractwidgets;

import clutter.core.Dimension;

public abstract class FlexibleWidget extends SingleChildWidget {
    private int flex;

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

    public void layout(Dimension maxSize) {
        super.layout(maxSize);
        if (maxSize.getArea() == 0)
            System.err.println("WARNING: FLEXIBLE HAS SIZE 0");
        size = maxSize;
    }
}
