package clutter.layoutwidgets;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;
import clutter.core.Direction;

public class GrowToFit extends SingleChildWidget {
    private Direction direction = Direction.BOTH;


    public GrowToFit() {};

    public GrowToFit(Widget child) {
        super(child);
    }


    @Override
    protected void runLayout(Dimension minSize, Dimension maxSize) {
        size = Dimension.min(preferredSize, maxSize);
        switch (direction) {
            case HORIZONTAL:
                size = size.withX(maxSize.x());
                break;
            case VERTICAL:
                size = size.withY(maxSize.y());
                break;
            case BOTH:
                size = maxSize;
                break;
        }
        child.layout(size, maxSize);
    }

    public GrowToFit setDirection(Direction direction) {
        this.direction = direction;
        return this;
    }
}
