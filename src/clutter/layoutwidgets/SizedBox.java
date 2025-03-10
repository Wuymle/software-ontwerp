package clutter.layoutwidgets;

import static clutter.core.Dimension.min;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;
import clutter.layoutwidgets.enums.Alignment;

public class SizedBox extends SingleChildWidget {
    Dimension boxSize;

    public SizedBox(Dimension boxSize, Widget child) {
        super(child);
        this.boxSize = boxSize;
    }

    @Override
    public void measure() {
        super.measure();
        preferredSize = boxSize;
    }

    public SizedBox setHorizontalAlignment(Alignment alignment) {
        horizontalAlignment = alignment;
        return this;
    }
}
