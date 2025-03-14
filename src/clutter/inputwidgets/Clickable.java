package clutter.inputwidgets;

import static clutter.core.Dimension.contains;

import java.awt.event.MouseEvent;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Debug;
import clutter.core.Dimension;
import clutter.widgetinterfaces.Interactable;

/**
 * A clickable widget.
 */
public class Clickable extends SingleChildWidget implements Interactable {
    Runnable onClick;
    int clickCount;

    /**
     * @param child      the child widget
     * @param onClick    the on click action
     * @param clickCount the click count
     */
    public Clickable(Widget child, Runnable onClick, int clickCount) {
        super(child);
        this.onClick = onClick;
        this.clickCount = clickCount;
    }

    /**
     * onClick action.
     */
    @Override
    public void onClick() {
        onClick.run();
    }

    /**
     * @param id         the id
     * @param hitPos     the hit position
     * @param clickCount the click count
     * @return the interactable
     */
    @Override
    public Interactable hitTest(int id, Dimension hitPos, int clickCount) {
        Debug.log(this, "Clicked");
        Interactable hit = super.hitTest(id, hitPos, clickCount);
        if (hit != null) {
            return hit;
        }
        Debug.log(this, "Claimed");
        if (id != MouseEvent.MOUSE_RELEASED)
            return null;
        Debug.log(this, position + " " + size + " " + hitPos);
        if (contains(position, size, hitPos) && this.clickCount == clickCount) {
            return this;
        }
        return null;
    }
}
