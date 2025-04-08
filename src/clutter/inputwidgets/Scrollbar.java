package clutter.inputwidgets;

import clutter.WidgetBuilder;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.core.Dimension;
import clutter.core.Direction;
import clutter.core.ScrollController;

public class Scrollbar extends WidgetBuilder<Context> {
    private ScrollController controller;
    private Direction direction;
    private Widget content;

    public Scrollbar(Context context, Widget content, ScrollController controller, Direction direction) {
        super(context);
        this.controller = controller;
        this.direction = direction;
        this.content = content;
    }

    @Override
    public void layout(Dimension minSize, Dimension maxSize) {
        Dimension fractionSize = maxSize
                .mulX(1 / (direction == Direction.HORIZONTAL ? controller.getRelContentWidth() : 1.0))
                .mulY(1 / (direction == Direction.VERTICAL ? controller.getRelContentHeight() : 1.0));
        super.layout(fractionSize, fractionSize);
    }

    @Override
    public Widget build() {
        return new DragHandle(content, (Dimension startPos) -> {
            controller.startDragging(startPos, direction == Direction.HORIZONTAL,
                    direction == Direction.VERTICAL, size.x(), size.y());
        });
    }
}
