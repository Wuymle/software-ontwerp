package clutter.inputwidgets;

import static clutter.core.Dimension.contains;

import java.awt.event.MouseEvent;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;
import clutter.widgetinterfaces.Interactable;

public class Clickable extends SingleChildWidget implements Interactable {
    Runnable onClick;

    public Clickable(Widget child, Runnable onClick) {
        super(child);
        this.onClick = onClick;
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
        if (contains(position, size, hitPos)) {
            return this;
        }
        return null;
    }

}
