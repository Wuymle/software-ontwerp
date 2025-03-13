package clutter.layoutwidgets;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;

public class Expanded extends SingleChildWidget {

    public Expanded(Widget child) {
        super(child);
    }

    @Override
    public void measure() {
        super.measure();
        preferredSize = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public void layout(Dimension minSize, Dimension maxSize) {
        super.layout(maxSize, maxSize);
    }
}
