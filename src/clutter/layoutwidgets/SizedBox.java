package clutter.layoutwidgets;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;

public class SizedBox extends SingleChildWidget {
    int boxWidth;
    int boxHeight;

    public SizedBox(int boxWidth, int boxHeight, Widget child) {
        super(child);
        this.boxWidth = boxWidth;
        this.boxHeight = boxHeight;
    }

    @Override
    public void layout(int maxWidth, int maxHeight) {
        width = Math.min(maxWidth, boxWidth);
        height = Math.min(maxHeight, boxHeight);
        child.layout(boxWidth, boxHeight);
    }
}
