package clutter.layoutwidgets;

import java.awt.Graphics;
import clutter.abstractwidgets.LeafWidget;
import clutter.core.Dimension;

public class NullWidget extends LeafWidget {

    @Override
    protected void runMeasure() {
        preferredSize = new Dimension(0, 0);
    }

    @Override
    protected void runPaint(Graphics g) {
        return;
    }
}
