package clutter.abstractwidgets;

import static clutter.core.Dimension.max;
import static clutter.core.Dimension.min;

import java.awt.Graphics;

import clutter.core.Dimension;
import clutter.widgetinterfaces.ClickEventHandler;
import clutter.widgetinterfaces.Debuggable;
import clutter.widgetinterfaces.Interactable;

public abstract class Widget implements Debuggable, ClickEventHandler {
    protected Dimension position, size, preferredSize = new Dimension(0, 0);
    protected boolean debug = false;

    public void setPosition(Dimension position) {
        this.position = position;
    }

    public Dimension getSize() {
        return size;
    }

    public void setSize(Dimension size) {
        this.size = size;
    }

    public Dimension getPreferredSize() {
        return preferredSize;
    }

    public Interactable hitTest(int id, Dimension hitPos, int clickCount) {
        return null;
    }

    public Widget setDebug() {
        this.debug = true;
        return this;
    }

    public boolean isDebug() {
        return debug;
    }

    public abstract void measure();

    public void layout(Dimension minSize, Dimension maxSize) {
        // Debug.log(this, "minSize:", minSize, "maxSize:", maxSize, "preferredSize:",
        // preferredSize);
        size = max(minSize, min(maxSize, preferredSize));
        // Debug.log(this, "Chosen size:", size);
        // if (size.getArea() == 0)
        //     Debug.warn(this, "Widget has zero size:", size);
    }

    public abstract void paint(Graphics g);
}
