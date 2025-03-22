package clutter.inputwidgets;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;

/**
 * A force click widget.
 */
public class ForceClick extends SingleChildWidget {

    /**
     * force click constructor
     * @param context the context
     * @param child   the child widget
     * @param active  the active state
     */
    public ForceClick(Context context, Widget child, boolean active) {
        super(child);
        if (active)
            context.getClickEventController().setClickHandler(this);
    }
}
