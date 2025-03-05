package clutter.layoutwidgets;

import clutter.abstractwidgets.FlexibleWidget;
import clutter.abstractwidgets.Widget;
import clutter.layoutwidgets.enums.Alignment;

public class Center extends FlexibleWidget {

    public Center(Widget child) {
        super(child, 1);
        horizontalAlignment = Alignment.CENTER;
        verticalAlignment = Alignment.CENTER;
    }
}
