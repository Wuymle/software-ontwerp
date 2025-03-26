package clutter.abstractwidgets;

import static clutter.core.Dimension.max;
import static clutter.core.Dimension.min;

import java.awt.Graphics;

import clutter.core.Debug;
import clutter.core.Dimension;
import clutter.widgetinterfaces.ClickEventHandler;
import clutter.widgetinterfaces.Debuggable;

/**
 * The base class for all widgets in the Clutter framework. Widgets are the
 * building blocks of the UI
 * and are responsible for rendering themselves and handling user input.
 */
public abstract class Widget implements Debuggable, ClickEventHandler {
    protected Dimension position, size, preferredSize = new Dimension(0, 0);
    protected boolean debug = false;

    /**
     * set the position of the widget
     * 
     * @return the position of the widget
     */
    public void setPosition(Dimension position) {
        this.position = position;
    }

    /**
     * get the position of the widget
     * 
     * @return the size of the widget
     */
    public Dimension getSize() {
        return size;
    }

    /**
     * set the size of the widget
     * 
     * @param size the size of the widget
     */
    public void setSize(Dimension size) {
        this.size = size;
    }

    /**
     * get the preferred size of the widget
     * 
     * @return the preferred size of the widget
     */
    public Dimension getPreferredSize() {
        return preferredSize;
    }

    /**
     * hit test the widget
     * 
     * @param id         the id of the clickEvent
     * @param hitPos     the position of the click
     * @param clickCount the number of clicks
     */
    public boolean hitTest(int id, Dimension hitPos, int clickCount) {
        return false;
    }

    /**
     * set the debug flag
     * 
     * @param debug set the debug flag
     * @return the widget
     */
    public Widget setDebug() {
        this.debug = true;
        return this;
    }

    /**
     * get the debug flag
     * 
     * @return the debug flag
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * Measure the widget
     */
    public abstract void measure();

    /**
     * Layout the widget
     * 
     * @param minSize the minimum size
     * @param maxSize the maximum size
     */
    public void layout(Dimension minSize, Dimension maxSize) {
        Debug.log(this, "minSize:", minSize, "maxSize:", maxSize, "preferredSize:",
                preferredSize);
        size = max(minSize, min(maxSize, preferredSize));
        // Debug.log(this, "Chosen size:", size);
        // if (size.getArea() == 0)
        // Debug.warn(this, "Widget has zero size:", size);
    }

    /**
     * Paint the widget
     * 
     * @param g the graphics object
     */
    public abstract void paint(Graphics g);
}
