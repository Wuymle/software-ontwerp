package clutter.layoutwidgets;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;

public class GrowToFit extends SingleChildWidget {
    public GrowToFit() {};

    public GrowToFit(Widget child) {
        super(child);
    }


    @Override
    protected void runLayout(Dimension minSize, Dimension maxSize) {
        size = maxSize;
        child.layout(maxSize, maxSize);
    }
}
