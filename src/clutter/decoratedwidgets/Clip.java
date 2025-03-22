package clutter.decoratedwidgets;

import java.awt.Graphics;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Debug;

/**
 * A widget that clips its child widget.
 */
public class Clip extends SingleChildWidget {
    /**
     * @param child the child widget
     */
    public Clip(Widget child) {
        super(child);
    }

    /**
     * paint the widget
     * @param g the graphics object
     */
    @Override
    public void paint(Graphics g) {
        Debug.log(this, "position: ", position, "size: ", size);
        g.setClip(position.x(), position.y(), size.x(), size.y());
        super.paint(g);
        g.setClip(null);
    }

}
