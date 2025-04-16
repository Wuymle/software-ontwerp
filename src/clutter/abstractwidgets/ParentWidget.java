package clutter.abstractwidgets;

import java.awt.Graphics;

public abstract class ParentWidget extends Widget {

    protected abstract void positionChildren();

    protected abstract void paintChildren(Graphics g);

    @Override
    public void runPaint(Graphics g) {
        positionChildren();
        paintChildren(g);
    }
}
