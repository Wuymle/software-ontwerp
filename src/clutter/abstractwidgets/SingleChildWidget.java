package clutter.abstractwidgets;

import java.awt.Graphics;

import clutter.core.Dimension;
import clutter.widgetinterfaces.Interactable;

public abstract class SingleChildWidget extends Widget {
    protected Widget child;

    protected SingleChildWidget(Widget child) {
        this.child = child;
    }

    @Override
    public void paint(Graphics g) {
        if (child == null)
            return;
        child.setPosition(position);
        child.paint(g);
    }

    @Override
    public void layout(Dimension maxSize) {
        if (child == null)
            return;
        child.layout(maxSize);
        this.size = child.getSize();
    }

    @Override
    public Interactable hitTest(int id, Dimension hitPos, int clickCount) {
        if (child == null)
            return null;
        return child.hitTest(id, hitPos, clickCount);
    }
}
