package clutter.layoutwidgets;

import static clutter.core.Dimension.add;
import static clutter.core.Dimension.divide;
import static clutter.core.Dimension.subtract;

import java.awt.Graphics;

import clutter.abstractwidgets.FlexibleWidget;
import clutter.abstractwidgets.Widget;

public class Center extends FlexibleWidget {

    public Center(Widget child) {
        super(child, 1);
    }

    @Override
    public void paint(Graphics g) {
        child.setPosition(add(divide(subtract(size, child.getSize()), 2), position));
        child.paint(g);
    }
}
