package clutter.abstractwidgets;

import static clutter.core.Dimension.max;

import clutter.core.Dimension;
import clutter.core.Direction;

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

    public void layout(Dimension maxSize, Direction flexDirection) {
        if (maxSize.getArea() == 0)
            System.err.println("WARNING: FLEXIBLE HAS SIZE 0");
        super.layout(maxSize);
        size = max(size,
                flexDirection == Direction.HORIZONTAL
                        ? new Dimension(maxSize.x(), 0)
                        : new Dimension(0, maxSize.y()));
    }
}
