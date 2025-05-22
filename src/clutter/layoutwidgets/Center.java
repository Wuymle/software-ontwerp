package clutter.layoutwidgets;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;
import clutter.layoutwidgets.enums.Alignment;

/**
 * @author Willem Michielssen A widget that centers its child widget.
 */
public class Center extends SingleChildWidget {
    public Center(Widget child) {
        super(child);
        horizontalAlignment = Alignment.CENTER;
        verticalAlignment = Alignment.CENTER;
    }

    @Override
    protected void runLayout(Dimension minSize, Dimension maxSize) {
        super.runLayout(maxSize, maxSize);
    }
}
