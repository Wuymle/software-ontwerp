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
}
