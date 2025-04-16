package clutter.layoutwidgets;

import java.awt.Color;
import clutter.abstractwidgets.Screen;
import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.core.Decoration;
import clutter.core.Dimension;
import clutter.core.WindowController;
import clutter.decoratedwidgets.Clip;
import clutter.decoratedwidgets.Icon;
import clutter.decoratedwidgets.Text;
import clutter.inputwidgets.Clickable;
import clutter.inputwidgets.DragHandle;
import clutter.layoutwidgets.enums.Alignment;
import clutter.resources.Icons;

public class SubWindow extends StatefulWidget<Context> {
    private Screen<?> content;
    private String name;
    private WindowController controller;
    private int resizeHandleWidth = 5;
    private int borderRadius = 10;
    private boolean active = true;
    private boolean maximized = false;

    public <C extends Context, S extends Screen<C>> SubWindow(Context context, String name, WindowController controller, S content) {
        super(context);
        this.name = name;
        this.content = content;
        this.controller = controller;
    }

    public boolean isMaximized() {
        return maximized;
    }

    public void setFocus(boolean active) {
        setState(() -> {
            if (active) 
                content.onGetFocus();
            else
                content.onLoseFocus();
            this.active = active;});
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
                maximized ? new NullWidget()
                        : new Row(
                                new DragHandle(
                                        new SizedBox(Dimension.square(resizeHandleWidth), null),
                                        (startPos) -> controller.resize(this, startPos, true, false,
                                                true, false)),
                                new Flexible(new DragHandle(
                                        new ConstrainedBox(null).setHeight(resizeHandleWidth),
                                        (startPos) -> controller
                                                .resize(this, startPos, false, false, true, false)))
                                                        .setHorizontalAlignment(Alignment.STRETCH),
                                new DragHandle(
                                        new SizedBox(Dimension.square(resizeHandleWidth), null),
                                        (startPos) -> controller.resize(this, startPos, false, true,
                                                true, false))),
                new Flexible(new Row(
                        maximized ? new NullWidget()
                                : new DragHandle(
                                        new ConstrainedBox(null).setWidth(resizeHandleWidth),
                                        (startPos) -> controller.resize(this, startPos, true, false,
                                                false, false)),
                        new Flexible(new Column(new DragHandle(new Row(
                                new SizedBox(new Dimension(0, 30), null),
                                new Padding(new Icon(Icons.DATABASE).setFontSize(16)).left(10)
                                        .right(5),
                                new Flexible(new Clip(new Text(name).setFontSize(12)))
                                        .setVerticalAlignment(Alignment.CENTER),
                                new Padding(new Icon(Icons.WINDOW_MINIMIZE).setFontSize(11))
                                        .horizontal(18),
                                new Clickable(new Padding(
                                        new Icon(maximized ? Icons.WINDOW_RESTORE : Icons.SQUARE)
                                                .setFontSize(10)).horizontal(18),
                                        () -> {
                                            maximized = !maximized;
                                            context.requestRepaint();
                                        }, 1).setVerticalAlignment(Alignment.STRETCH),
                                new Clickable(new Padding(
                                        new Icon(Icons.CROSS).setColor(Color.black).setFontSize(11))
                                                .horizontal(18)
                                // .setDecoration(new
                                // Decoration().setColor(Color.red)
                                // .setBorderColor(Color.black)
                                // )
                                        , () -> controller.removeWindow(this), 1)
                                                .setVerticalAlignment(Alignment.STRETCH))
                                                        .setCrossAxisAlignment(Alignment.STRETCH)
                                                        .setDecoration(new Decoration()
                                                                .setBorderColor(
                                                                        active ? Color.orange
                                                                                : new Color(243,
                                                                                        243, 243))
                                                                .setColor(active ? Color.orange
                                                                        : new Color(243, 243,
                                                                                243))),
                                (StartPos) -> {
                                    controller.move(this, StartPos);
                                    maximized = false;
                                }), new Padding(content).horizontal(1)
                                        .bottom(1))
                                                .setDecoration(
                                                        new Decoration()
                                                                .setBorderColor(
                                                                        active ? Color.orange
                                                                                : new Color(243,
                                                                                        243, 243))
                                                                .setBorderRadius(maximized ? 0
                                                                        : borderRadius)
                                                                .setColor(Color.white)))
                        // .setDecoration(new Decoration().fillFront()
                        // .setColor(Color.black)
                        // .setFillAlpha(active ? 0 : 0.15f)
                        // .setBorderRadius(borderRadius))
                        ,
                        maximized ? new NullWidget()
                                : new DragHandle(
                                        new ConstrainedBox(null).setWidth(resizeHandleWidth),
                                        (startPos) -> controller.resize(this, startPos, false, true,
                                                false, false)))
                                                        .setCrossAxisAlignment(Alignment.STRETCH)),
                maximized ? new NullWidget()
                        : new Row(
                                new DragHandle(
                                        new SizedBox(Dimension.square(resizeHandleWidth), null),
                                        (startPos) -> controller.resize(this, startPos, true, false,
                                                false, true)),
                                new Flexible(new DragHandle(
                                        new ConstrainedBox(null).setHeight(resizeHandleWidth),
                                        (startPos) -> controller.resize(this, startPos, false,
                                                false, false, true)))
                                                        .setHorizontalAlignment(Alignment.STRETCH),
                                new DragHandle(
                                        new SizedBox(Dimension.square(resizeHandleWidth), null),
                                        (startPos) -> controller.resize(this, startPos, false, true,
                                                false, true))))
        // .setDecoration(
        // new Decoration().setColor(Color.black)
        // .setFillAlpha(0.05f).setBorderRadius(borderRadius + resizeHandleWidth))
        ;
    }
}
