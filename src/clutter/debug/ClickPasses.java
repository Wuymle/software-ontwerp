package clutter.debug;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Debug;
import clutter.core.Dimension;

public class ClickPasses extends SingleChildWidget {
    int id;

    public ClickPasses(Widget child, int id) {
        super(child);
        this.id = id;
        setDebug();
    }

    @Override
    public boolean hitTest(int id, Dimension hitPos, int clickCount) {
        boolean hit = super.hitTest(id, hitPos, clickCount);
        Debug.log(this, "Clicked passed through, id: ", id, ", claimed?", hit);
        return hit;
    }
}
