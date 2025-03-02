package clutter.layoutwidgets;

import java.awt.Graphics;

import clutter.abstractwidgets.FlexibleWidget;
import clutter.abstractwidgets.Widget;

public class Center extends FlexibleWidget {

    public Center(Widget child) {
        super(child, 1);
    }

    @Override
    public void paint(Graphics g) {
        child.setPosition((width - child.getWidth()) / 2, (height - child.getHeight()) / 2);
        child.paint(g);
    }
}
