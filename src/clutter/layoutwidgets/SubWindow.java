package clutter.layoutwidgets;

import java.awt.Color;

import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.AnimationController;
import clutter.core.Context;
import clutter.core.Decoration;
import clutter.core.Dimension;
import clutter.core.WindowController;
import clutter.decoratedwidgets.Icon;
import clutter.decoratedwidgets.Text;
import clutter.inputwidgets.Clickable;
import clutter.inputwidgets.DragHandle;
import clutter.layoutwidgets.enums.Alignment;
import clutter.resources.Icons;

public class SubWindow extends StatefulWidget<Context> {
    private Widget content;
    private String name;
    private WindowController controller;
    private int resizeHandleWidth = 5;

    public SubWindow(Context context, String name, WindowController controller, Widget content) {
        super(context);
        this.name = name;
        this.content = content;
        this.controller = controller;
    }

    @Override
    public boolean hitTest(int id, Dimension hitPos, int clickCount) {
        if (Dimension.contains(position, size, hitPos)) {
            controller.moveToTop(this);
            super.hitTest(id, hitPos, clickCount);
            return true;
        }
        return false;
    }

    @Override
    public Widget build() {
        return new Column(
                new Row(
                        new DragHandle(new SizedBox(Dimension.square(resizeHandleWidth), null), (startPos) -> {
                            controller.resize(this, startPos, true, false, true, false);
                        }),
                        new Flexible(
                                new DragHandle(new ConstrainedBox(null).setHeight(resizeHandleWidth), (startPos) -> {
                                    controller.resize(this, startPos, false, false, true, false);
                                }))
                                .setHorizontalAlignment(Alignment.STRETCH),
                        new DragHandle(new SizedBox(Dimension.square(resizeHandleWidth), null), (startPos) -> {
                            controller.resize(this, startPos, false, true, true, false);
                        })),
                new Flexible(new Row(
                        new DragHandle(new ConstrainedBox(null).setWidth(resizeHandleWidth), (startPos) -> {
                            controller.resize(this, startPos, true, false, false, false);
                        }),
                        new Flexible(new Column(
                                new DragHandle(new Row(
                                        new SizedBox(new Dimension(0, 30), null),
                                        new Flexible(new Padding(new Text(name).setFontSize(12))
                                                .horizontal(5)).setVerticalAlignment(Alignment.CENTER),
                                        new Clickable(new Padding(new Icon(Icons.CROSS)
                                                .setColor(Color.white).setFontSize(16))
                                                .horizontal(20)
                                                .setDecoration(new Decoration().setColor(Color.red)
                                                        .setBorderColor(Color.black)),
                                                () -> {
                                                    controller.removeWindow(this);
                                                },
                                                1).setVerticalAlignment(Alignment.STRETCH))
                                        .setCrossAxisAlignment(Alignment.STRETCH)
                                        .setDecoration(new Decoration().setBorderColor(Color.black)
                                                .setColor(Color.orange)),
                                        (StartPos) -> {
                                            controller.move(this, StartPos);
                                        }),
                                content)
                                .setDecoration(new Decoration().setBorderColor(Color.black).setBorderRadius(10)
                                        .setBorderWidth(1).setColor(Color.white))),
                        new DragHandle(new ConstrainedBox(null).setWidth(resizeHandleWidth), (startPos) -> {
                            controller.resize(this, startPos, false, true, false, false);
                        }))
                        .setCrossAxisAlignment(Alignment.STRETCH)),
                new Row(
                        new DragHandle(new SizedBox(Dimension.square(resizeHandleWidth), null), (startPos) -> {
                            controller.resize(this, startPos, true, false, false, true);
                        }),
                        new Flexible(
                                new DragHandle(new ConstrainedBox(null).setHeight(resizeHandleWidth), (startPos) -> {
                                    controller.resize(this, startPos, false, false, false, true);
                                })).setHorizontalAlignment(Alignment.STRETCH),
                        new DragHandle(new SizedBox(Dimension.square(resizeHandleWidth), null), (startPos) -> {
                            controller.resize(this, startPos, false, true, false, true);
                        })))
                .setDecoration(
                        new Decoration().setColor(Color.black).setFillAlpha(0.05f).setBorderRadius(resizeHandleWidth));
    }
}
