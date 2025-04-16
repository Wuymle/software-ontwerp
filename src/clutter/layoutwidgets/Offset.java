package clutter.layoutwidgets;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;


public class Offset extends SingleChildWidget {
    private final Dimension offset;

    public Offset(Dimension offset, Widget child) {
        super(child);
        this.offset = offset;
    }

    @Override
    public void positionChildren() {
        super.positionChildren();
        child.setPosition(position.add(offset));
    }

    @Override
    public boolean hitTest(int id, Dimension hitPos, int clickCount) {
        if (!Dimension.contains(position.add(offset), size, hitPos))
            return false;
        return child.hitTest(id, hitPos, clickCount);
    }
}
