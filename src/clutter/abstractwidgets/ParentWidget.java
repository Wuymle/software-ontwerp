package clutter.abstractwidgets;

import java.awt.Graphics;

import clutter.core.Decoration;

public abstract class ParentWidget extends Widget {
    private Decoration decoration = new Decoration();

    public Decoration getDecoration() {
        return decoration;
    }

    public ParentWidget setDecoration(Decoration decoration) {
        this.decoration = decoration;
        return this;
    }

    protected abstract void positionChildren();

    protected abstract void paintChildren(Graphics g);

    @Override
    public void paint(Graphics g) {
        decoration.paint(g, position, size);
        positionChildren();
        paintChildren(g);
        decoration.afterPaint(g, position, size);
    }
}