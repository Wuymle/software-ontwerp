package clutter.layoutwidgets;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.debug.Debug;
import clutter.debug.DebugMode;

public class ScrollBox extends SingleChildWidget {
    private double scrollX = 0;
    private double scrollY = 0;

    public ScrollBox(Widget child, double scrollX, double scrollY) {
        super(child);
        this.scrollX = scrollX;
        this.scrollY = scrollY;
    }

    @Override
    public void positionChildren() {
        Debug.log(this, DebugMode.PAINT, "ScrollX:", scrollX, "ScrollY:", scrollY);
        Debug.log(this, DebugMode.PAINT, "Child size:", child.getSize());
        super.positionChildren();
        child.setPosition(child.getPosition()
                .subtract(child.getSize().subtract(size).mulX(scrollX).mulY(scrollY)));
    }
}
