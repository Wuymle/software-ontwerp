package clutter.inputwidgets;

import static clutter.core.Dimension.contains;

import java.awt.event.MouseEvent;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.core.Dimension;
import clutter.core.Direction;
import clutter.core.ScrollController;

public class Scrollbar extends SingleChildWidget {
    private ScrollController controller;
    private Direction direction;

    public Scrollbar(Context context, Widget child, ScrollController controller, Direction direction) {
        super(child);
        this.controller = controller;
        this.direction = direction;
    }

    @Override
    public boolean hitTest(int id, Dimension hitPos, int clickCount) {
        if (id != MouseEvent.MOUSE_PRESSED || !contains(position, size, hitPos))
            return false;
        controller.startDragging(hitPos, direction == Direction.HORIZONTAL,
                direction == Direction.VERTICAL, size.x(), size.y());
        return true;
    }

    @Override
    public void layout(Dimension minSize, Dimension maxSize) {
        Dimension fractionSize = maxSize
                .mulX(1 / (direction == Direction.HORIZONTAL ? controller.getRelContentWidth() : 1.0))
                .mulY(1 / (direction == Direction.VERTICAL ? controller.getRelContentHeight() : 1.0));
        super.layout(fractionSize, fractionSize);
    }
}
