package clutter.decoratedwidgets;

import java.awt.Graphics;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;

public class Clip extends SingleChildWidget {
    public Clip(Widget child) {
        super(child);
    }

    @Override
    public void paint(Graphics g) {
        child.setPosition(position);
        g.setClip(position.x(), position.y(), size.x(), size.y());
        child.paint(g);
        g.setClip(null);
    }

}
