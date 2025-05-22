package clutter.layoutwidgets;

import java.awt.Color;
import java.util.List;
import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.core.Decoration;
import clutter.core.Dimension;
import clutter.core.Orientation;
import clutter.core.ResizableGridController;
import clutter.core.ResizableGridController.ResizableGridSubscriber;
import clutter.inputwidgets.DragHandle;
import clutter.layoutwidgets.enums.Alignment;

public class ResizableGrid extends StatefulWidget<Context> implements ResizableGridSubscriber {
    int numArrays;
    Widget[] children;
    // Orientation direction;
    ResizableGridController controller;

    public ResizableGrid(Context context, ResizableGridController controller, Widget... children) {
        super(context);
        // this.direction = direction;
        this.children = children;
        this.controller = controller;
        this.numArrays = controller.getArrayCount();
        controller.addSubscriber(this);
    }

    public ResizableGrid(Context context, ResizableGridController controller,
            List<Widget> children) {
        super(context);
        // this.direction = direction;
        this.children = children.toArray(new Widget[0]);
        this.controller = controller;
        this.numArrays = controller.getArrayCount();
        controller.addSubscriber(this);
    }

    @Override
    public Widget build() {
        Widget[] wrappedChildren = new Widget[children.length];
        for (int i = 0; i < children.length; i++) {
            final int index = i;
            final boolean evenRow = (i / numArrays) % 2 == 0;
            wrappedChildren[i] = new ConstrainedBox(new GrowToFit(i < numArrays ? new Stack(
                    new GrowToFit(children[i]),
                    new Expanded(new DragHandle(new SizedBox(new Dimension(5, 0)), startPos -> {
                        controller.startDragging(startPos, index);
                    })).setHorizontalAlignment(Alignment.END)
                            .setVerticalAlignment(Alignment.STRETCH))
                    : children[i])).setWidth(controller.getColWidth(index % numArrays))
                            .setMinWidth(30).setDecoration(new Decoration()
                                    .setColor(evenRow ? new Color(230, 230, 230) : Color.white));
        }
        return new Grid(numArrays, Orientation.VERTICAL, true, wrappedChildren)
                .setDecoration(new Decoration().setBorderColor(Color.black));
    }

    @Override
    public void onColumnResize() {
        setState(() -> {
        });
    }

}
