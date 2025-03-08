package clutter.layoutwidgets;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.layoutwidgets.enums.Alignment;

public class Center extends SingleChildWidget {
    public Center(Widget child) {
        super(child);
        horizontalAlignment = Alignment.CENTER;
        verticalAlignment = Alignment.CENTER;
    }
}
