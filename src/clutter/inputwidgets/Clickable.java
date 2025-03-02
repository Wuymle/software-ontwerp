package clutter.inputwidgets;

import java.awt.event.MouseEvent;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
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
    public Interactable hitTest(int id, int x, int y, int clickCount) {
        Interactable hit = super.hitTest(id, x, y, clickCount);
        if (hit != null) {
            return hit;
        }
        if (id != MouseEvent.MOUSE_CLICKED)
            return null;
        if (this.x <= x && x <= this.x + width && this.y <= y && y <= this.y + height) {
            return this;
        }
        return null;
    }

}
