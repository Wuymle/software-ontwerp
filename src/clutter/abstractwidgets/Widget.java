package clutter.abstractwidgets;


import java.awt.Graphics;

import clutter.core.Dimension;
import clutter.widgetinterfaces.Interactable;

public abstract class Widget {
    protected Dimension position, size = new Dimension(0, 0);

    public void setPosition(Dimension position) {
        this.position = position;
    }

    public Dimension getSize() {
        return this.size;
    }

    public void setSize(Dimension size) {
        this.size = size;
    }

    public Interactable hitTest(int id, Dimension hitPos, int clickCount) {
        return null;
    }

    public abstract void layout(Dimension maxSize);

    public abstract void paint(Graphics g);
}
