package clutter.decoratedwidgets;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
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

        Graphics2D g2d = (Graphics2D) g;
        Shape originalClip = g2d.getClip();
        Area intersectionClip = new Area(originalClip);
        intersectionClip
                .intersect(new Area(new Rectangle(position.x(), position.y(), size.x(), size.y())));
        g2d.setClip(intersectionClip);

        Debug.log(this, DebugMode.PAINT, "Set clip");
        super.runPaint(g2d);
        g2d.setClip(originalClip);
    }

}
