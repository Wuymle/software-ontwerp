package ui.layoutwidgets;

import ui.abstractwidgets.ContainerWidget;
import ui.abstractwidgets.Widget;

public class Column extends ContainerWidget {

    public Column(Widget... children) {
        super(children);
    }

    @Override
    public void layout(int maxWidth, int maxHeight) {
        int remainingHeight = maxHeight;
        for (Widget child : children) {
            child.layout(maxWidth, remainingHeight);
            width = Math.max(width, child.getWidth());
            remainingHeight = Math.max(0, remainingHeight - child.getHeight());
        }
        this.height = maxHeight - remainingHeight;
    }

    @Override
    protected void positionChildren() {
        int childY = y;
        for (Widget child : children) {
            child.setPosition(x, childY);
            childY += child.getHeight();
        }
    }

}
