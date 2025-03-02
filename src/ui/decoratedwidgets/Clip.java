package ui.decoratedwidgets;

import java.awt.Graphics;

import ui.abstractwidgets.Widget;

public class Clip extends Widget {
    private Widget child;

    public Clip(Widget child) {
        this.child = child;
    }

    @Override
    public void layout(int maxWidth, int maxHeight) {
        child.layout(maxWidth, maxHeight);
        this.width = child.getWidth();
        this.height = child.getHeight();
    }

    @Override
    public void paint(Graphics g) {
        g.setClip(this.x, this.y, this.width, this.height);
        child.setPosition(x, y);
        child.paint(g);
        g.setClip(null);
    }

}
