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
    public void runLayout(Dimension minsize, Dimension maxSize) {
        super.runLayout(maxSize, maxSize);
    }
}
