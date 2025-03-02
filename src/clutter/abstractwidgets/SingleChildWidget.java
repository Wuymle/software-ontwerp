package clutter.abstractwidgets;

import java.awt.Graphics;

public abstract class SingleChildWidget extends Widget {
    protected Widget child;

    protected SingleChildWidget(Widget child) {
        this.child = child;
    }

    @Override
    public void paint(Graphics g) {
        if (child == null)
            return;
        child.setPosition(x, y);
        child.paint(g);
    }

    @Override
    public void layout(int maxWidth, int maxHeight) {
        if (child == null)
            return;
        child.layout(maxWidth, maxHeight);
    }
}
