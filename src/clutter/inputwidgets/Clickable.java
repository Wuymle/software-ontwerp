package clutter.inputwidgets;

import static clutter.core.Dimension.contains;

import java.awt.event.MouseEvent;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Debug;
import clutter.core.Dimension;
import clutter.widgetinterfaces.Interactable;

public class Clickable extends SingleChildWidget implements Interactable {
    Runnable onClick;
    int clickCount;

    public Clickable(Widget child, Runnable onClick, int clickCount) {
        super(child);
        this.onClick = onClick;
        this.clickCount = clickCount;
    }

    @Override
    public void onClick() {
        onClick.run();
    }

    @Override
    public Interactable hitTest(int id, Dimension hitPos, int clickCount) {
        Interactable hit = super.hitTest(id, hitPos, clickCount);
        if (hit != null) {
            return hit;
        }
        if (id != MouseEvent.MOUSE_CLICKED)
            return null;
        Debug.log(this, position + " " + size + " " + hitPos);
        if (contains(position, size, hitPos) && this.clickCount == clickCount) {
            return this;
        }
        return null;
    }
}
