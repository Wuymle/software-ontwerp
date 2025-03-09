package clutter.layoutwidgets;

import clutter.abstractwidgets.FlexibleWidget;
import clutter.abstractwidgets.Widget;
import clutter.layoutwidgets.enums.Alignment;

public class Flexible extends FlexibleWidget {

    public Flexible(Widget child) {
        super(child, 1);
    }

    public Flexible setHorizontalAlignment(Alignment alignment) {
        horizontalAlignment = alignment;
        return this;
    }

    public Flexible setVerticalAlignment(Alignment alignment) {
        verticalAlignment = alignment;
        return this;
    }
}
