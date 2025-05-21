package clutter.layoutwidgets;

import java.awt.Graphics;
import clutter.abstractwidgets.LeafWidget;

public class NullWidget extends LeafWidget {

    @Override
    protected void runMeasure() {}

    @Override
    protected void runPaint(Graphics g) {}
}
