package clutter.abstractwidgets;

import static clutter.core.Dimension.max;
import static clutter.core.Dimension.min;

import java.awt.Graphics;

import clutter.core.Debug;
import clutter.core.Dimension;
import clutter.widgetinterfaces.ClickEventHandler;
import clutter.widgetinterfaces.Debuggable;
import clutter.widgetinterfaces.Interactable;

/**
 * The base class for all widgets in the Clutter framework. Widgets are the
 * building blocks of the UI
 * and are responsible for rendering themselves and handling user input.
 */
public abstract class Widget implements Debuggable, ClickEventHandler {
    protected Dimension position, size, preferredSize = new Dimension(0, 0);
    protected boolean debug = false;

    /**
     * @return the position of the widget
     */
    public void setPosition(Dimension position) {
        this.position = position;
    }

    /**
     * @return the size of the widget
     */
    public Dimension getSize() {
        return size;
    }

    /**
     * @param size the size of the widget
     */
    public void setSize(Dimension size) {
        this.size = size;
    }

    /**
     * @return the preferred size of the widget
     */
    public Dimension getPreferredSize() {
        return preferredSize;
    }

    /**
     * @param id         the id of the clickEvent
     * @param hitPos     the position of the click
     * @param clickCount the number of clicks
     */
    public Interactable hitTest(int id, Dimension hitPos, int clickCount) {
        return null;
    }

    /**
     * @param debug set the debug flag
     * @return the widget
     */
    public Widget setDebug() {
        this.debug = true;
        return this;
    }

    /**
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
