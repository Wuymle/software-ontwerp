package clutter.inputwidgets;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;

public class ForceClick extends SingleChildWidget {

    public ForceClick(Context context, Widget child, boolean active) {
        super(child);
        if (active)
            context.getClickEventController().setClickHandler(this);
    }
}
