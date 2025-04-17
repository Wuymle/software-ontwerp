package clutter.decoratedwidgets;

import java.awt.Graphics;
import java.awt.Shape;
import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.debug.Debug;
import clutter.debug.DebugMode;

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
     * 
     * @param g the graphics object
     */
    @Override
    protected void runPaint(Graphics g) {
        Shape originalClip = g.getClip();
        g.setClip(position.x(), position.y(), size.x(), size.y());
        Debug.log(this, DebugMode.PAINT, "Set clip");
        super.runPaint(g);
        g.setClip(originalClip);
    }

}
