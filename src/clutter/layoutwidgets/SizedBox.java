package clutter.layoutwidgets;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;
import clutter.layoutwidgets.enums.Alignment;

/**
 * A widget that centers its child widget.
 */
public class SizedBox extends SingleChildWidget {
    Dimension boxSize;

    /**
     * @param boxSize the size of the box
     * @param child   the child widget
     */
    public SizedBox(Dimension boxSize, Widget child) {
        super(child);
        this.boxSize = boxSize;
    }

    /**
     * measure the widget
     */
    @Override
    public void measure() {
        super.measure();
        preferredSize = boxSize;
    }

    /**
     * set the horizontal alignment
     * 
     * @param alignment the alignment
     */
    public SizedBox setHorizontalAlignment(Alignment alignment) {
        horizontalAlignment = alignment;
        return this;
    }
}
