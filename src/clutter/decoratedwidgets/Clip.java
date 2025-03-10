package clutter.decoratedwidgets;

import java.awt.Graphics;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Debug;

public class Clip extends SingleChildWidget {
    public Clip(Widget child) {
        super(child);
    }

    @Override
    public void paint(Graphics g) {
        Debug.log(this, "position: ", position, "size: ", size);
        g.setClip(position.x(), position.y(), size.x(), size.y());
        super.paint(g);
        g.setClip(null);
    }

}
