package clutter.layoutwidgets;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;

public class Expanded extends SingleChildWidget {
    public Expanded(Widget child) {
        super(child);
    }

    @Override
    protected void runLayout(Dimension minSize, Dimension maxSize) {
        super.runLayout(maxSize, maxSize);
    }
}
