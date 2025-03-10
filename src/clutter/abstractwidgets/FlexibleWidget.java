package clutter.abstractwidgets;

import clutter.core.Debug;
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

    public void layout(Dimension minSize, Dimension maxSize) {
        if (maxSize.getArea() == 0)
            Debug.log(this, "WARNING: FLEXIBLE HAS SIZE 0");
        super.layout(minSize, maxSize);
    }
}
