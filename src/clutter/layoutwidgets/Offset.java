package clutter.layoutwidgets;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;


public class Offset extends SingleChildWidget {
    private final Dimension offset;

    public Offset(Widget child, Dimension offset) {
        super(child);
        this.offset = offset;
    }

    @Override
    public void positionChildren() {
        super.positionChildren();
        child.setPosition(position.add(offset));
    }
}
