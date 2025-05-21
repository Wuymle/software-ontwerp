package clutter.inputwidgets;

import clutter.abstractwidgets.Widget;
import clutter.abstractwidgets.WidgetBuilder;
import clutter.core.Context;
import clutter.core.Dimension;
import clutter.core.Orientation;
import clutter.core.ScrollController;

public class Scrollbar extends WidgetBuilder<Context> {
    private ScrollController controller;
    private Orientation direction;
    private Widget content;

    public Scrollbar(Context context, Widget content, ScrollController controller,
            Orientation direction) {
        super(context);
        this.controller = controller;
        this.direction = direction;
        this.content = content;
    }

    @Override
    protected void runLayout(Dimension minSize, Dimension maxSize) {
        Dimension fractionSize = maxSize.mulX(
                1 / (direction == Orientation.HORIZONTAL ? controller.getRelContentWidth() : 1.0))
                .mulY(1 / (direction == Orientation.VERTICAL ? controller.getRelContentHeight()
                        : 1.0));
        super.runLayout(fractionSize, fractionSize);
    }

    @Override
    public Widget build() {
        return new DragHandle(content, (Dimension startPos) -> {
            controller.startDragging(startPos, direction == Orientation.HORIZONTAL,
                    direction == Orientation.VERTICAL, size.x(), size.y());
        });
    }
}
