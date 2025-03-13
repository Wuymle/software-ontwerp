package clutter.layoutwidgets;

import clutter.abstractwidgets.FlexibleWidget;
import clutter.abstractwidgets.Widget;
import clutter.layoutwidgets.enums.Alignment;

/**
 * A widget that expands its child widget.
 */
public class Flexible extends FlexibleWidget {

    /**
     * @param child the child widget
     */
    public Flexible(Widget child) {
        super(child, 1);
    }

    /**
     * @param flex the flex
     * @return self
     */
    public Flexible setHorizontalAlignment(Alignment alignment) {
        horizontalAlignment = alignment;
        return this;
    }

    /**
     * @param flex the flex
     * @return self
     */
    public Flexible setVerticalAlignment(Alignment alignment) {
        verticalAlignment = alignment;
        return this;
    }
}
