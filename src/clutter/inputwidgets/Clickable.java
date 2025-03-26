package clutter.inputwidgets;

import static clutter.core.Dimension.contains;

import java.awt.event.MouseEvent;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Debug;
import clutter.core.Dimension;

/**
 * A clickable widget.
 */
public class Clickable extends SingleChildWidget {
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
     * hit test
     * 
     * @param id         the id
     * @param hitPos     the hit position
     * @param clickCount the click count
     * @return the interactable
     */
    @Override
    public boolean hitTest(int id, Dimension hitPos, int clickCount) {
        Debug.log(this, "Clicked");
        boolean claimed = super.hitTest(id, hitPos, clickCount);
        if (claimed) {
            Debug.log(this, "Claimed by child: ", claimed);
            return claimed;
        }
        Debug.log(this, "Claimed");
        if (id != MouseEvent.MOUSE_RELEASED) {
            Debug.log(this, "Wrong mouse event");
            return false;
        }
        Debug.log(this, position + " " + size + " " + hitPos);
        if (contains(position, size, hitPos) && this.clickCount == clickCount) {
            onClick.run();
            return true;
        }
        return false;
    }
}
