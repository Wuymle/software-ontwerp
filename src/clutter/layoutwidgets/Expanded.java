package clutter.layoutwidgets;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;

public class Expanded extends SingleChildWidget {

    public Expanded(Widget child) {
        super(child);
    }

    @Override
    public void layout(Dimension minSize, Dimension maxSize) {
        super.layout(maxSize, maxSize);
    }
}
