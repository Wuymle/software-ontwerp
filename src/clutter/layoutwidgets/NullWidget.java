package clutter.layoutwidgets;

import java.awt.Graphics;

import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;

public class NullWidget extends Widget {

    @Override
    public void runMeasure() {
        preferredSize = new Dimension(0, 0);
    }

    @Override
    public void runPaint(Graphics g) {
        return;
    }
}
