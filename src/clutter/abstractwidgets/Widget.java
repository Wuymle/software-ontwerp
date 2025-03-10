package clutter.abstractwidgets;

import static clutter.core.Dimension.max;
import static clutter.core.Dimension.min;

import java.awt.Graphics;

import clutter.core.Dimension;
import clutter.decoratedwidgets.DecoratedBox;
import clutter.widgetinterfaces.Interactable;

public abstract class Widget {
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

    public abstract void measure();

    public void layout(Dimension minSize, Dimension maxSize) {
        size = max(minSize, min(maxSize, preferredSize));
    }

    public void layout(Dimension maxSize) {
        layout(new Dimension(0, 0), maxSize);
    }

    public abstract void paint(Graphics g);
}
