package clutter.debug;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Debug;
import clutter.core.Dimension;
import clutter.widgetinterfaces.Interactable;

public class ClickPasses extends SingleChildWidget {
    int id;

    public ClickPasses(Widget child, int id) {
        super(child);
        this.id = id;
        setDebug();
    }

    @Override
    public Interactable hitTest(int id, Dimension hitPos, int clickCount) {
        Interactable hit = super.hitTest(id, hitPos, clickCount);
        Debug.log(this, "Clicked passed through, id: ", id, ", claimed?", hit != null);
        return hit;
    }
}
