package clutter.abstractwidgets;

import static clutter.core.Dimension.max;
import static clutter.core.Dimension.min;
import java.awt.Graphics;
import java.util.Set;
import clutter.core.ClickEventController.ClickEventHandler;
import clutter.core.Decoration;
import clutter.core.Dimension;
import clutter.debug.Debug;
import clutter.debug.DebugMode;
import clutter.debug.Debuggable;

/**
 * The base class for all widgets in the Clutter framework. Widgets are the building blocks of the
 * UI and are responsible for rendering themselves and handling user input.
 */
public abstract class Widget implements Debuggable, ClickEventHandler {
    protected Dimension position, size, preferredSize = new Dimension(0, 0);
    protected Set<DebugMode> debugModes = Set.of();
    private Decoration decoration = new Decoration();
    private boolean expanded = false;

    public Decoration getDecoration() {
        return decoration;
    }

    public Widget setDecoration(Decoration decoration) {
        this.decoration = decoration;
        return this;
    }

    /**
     * set the position of the widget
     * 
     * @return the position of the widget
     */
    public void setPosition(Dimension position) {
        if (position == null)
            throw new IllegalArgumentException("Position cannot be null");
        this.position = position;
    }

    /**
     * get the position of the widget
     * 
     * @return the position of the widget
     */
    public Dimension getPosition() {
        return position;
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
     * @param id the id of the clickEvent
     * @param hitPos the position of the click
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
    public Widget debug(DebugMode... modes) {
        debugModes = Set.of(modes);
        return this;
    }

    /**
     * get the debug flag
     * 
     * @return the debug flag
     */
    public boolean isDebug() {
        return !debugModes.isEmpty();
    }

    public boolean hasDebugMode(DebugMode mode) {
        return debugModes.contains(mode);
    }

    public Widget expand() {
        this.expanded = true;
        return this;
    }

    /**
     * Measure the widget
     */
    public final void measure() {
        Debug.log(this, DebugMode.MEASURE, () -> runMeasure());
    }

    protected abstract void runMeasure();

    public final void layout(Dimension minSize, Dimension maxSize) {
        Debug.log(this, DebugMode.LAYOUT, () -> runLayout(minSize, maxSize));
    }

    /**
     * Layout the widget
     * 
     * @param minSize the minimum size
     * @param maxSize the maximum size
     */
    protected void runLayout(Dimension minSize, Dimension maxSize) {
        size = expanded ? maxSize : max(minSize, min(maxSize, preferredSize));
        Debug.log(this, DebugMode.LAYOUT, "min:", minSize, "max:", maxSize, "preferred:",
                preferredSize, "->", size);
        if (size.getArea() == 0)
            Debug.warn(this, DebugMode.LAYOUT, "Widget has zero size:", size);
    }

    public final void paint(Graphics g) {
        Debug.log(this, DebugMode.PAINT, () -> {
            decoration.beforePaint(g, position, size);
            runPaint(g);
            decoration.afterPaint(g, position, size);
        });
    }

    /**
     * Paint the widget
     * 
     * @param g the graphics object
     */
    protected abstract void runPaint(Graphics g);
}
