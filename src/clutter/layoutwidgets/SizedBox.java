package clutter.layoutwidgets;

import static clutter.core.Dimension.min;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;

public class SizedBox extends SingleChildWidget {
    Dimension boxSize;

    public SizedBox(Dimension boxSize, Widget child) {
        super(child);
        this.boxSize = boxSize;
    }

    @Override
    public void layout(Dimension maxSize) {
        super.layout(min(maxSize, boxSize));
        size = min(maxSize, boxSize);
    }
}
