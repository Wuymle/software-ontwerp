package ui.layoutwidgets;

import ui.abstractwidgets.ContainerWidget;
import ui.abstractwidgets.Widget;

public class Row extends ContainerWidget {

    public Row(Widget... children) {
        super(children);
    }

    @Override
    public void layout(int maxWidth, int maxHeight) {
        int remainingWidth = maxWidth;
        for (Widget child : children) {
            child.layout(remainingWidth, maxHeight);
            height = Math.max(height, child.getHeight());
            remainingWidth = Math.max(0, remainingWidth - child.getWidth());
        }
        this.width = maxWidth - remainingWidth;
    }

    @Override
    protected void positionChildren() {
        int childX = x;
        for (Widget child : children) {
            child.setPosition(childX, y);
            childX += child.getWidth();
        }
    }

}
