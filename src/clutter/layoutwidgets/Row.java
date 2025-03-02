package clutter.layoutwidgets;

import clutter.abstractwidgets.FlexibleWidget;
import clutter.abstractwidgets.MultiChildWidget;
import clutter.abstractwidgets.Widget;

public class Row extends MultiChildWidget {

    public Row(Widget... children) {
        super(children);
    }

    @Override
    public void layout(int maxWidth, int maxHeight) {
        layoutInflexibleWidgets(maxWidth, maxHeight);
        int remainingWidth = inflexibleChildren().reduce(maxWidth, (width, child) -> width - child.getWidth());
        layoutFlexibleWidgets(remainingWidth, maxHeight);
    }

    @Override
    protected void layoutFlexibleWidgets(int maxWidth, int maxHeight) {
        int totalFlex = flexibleChildren().reduce(0, (flex, child) -> flex + ((FlexibleWidget) child).getFlex());
        int remainingWidth = maxWidth;
        for (Widget child : flexibleChildren()) {
            int maxChildWidth = child.getWidth() + remainingWidth * ((FlexibleWidget) child).getFlex() / totalFlex;
            child.layout(maxChildWidth, maxHeight);
            height = Math.min(Math.max(height, child.getHeight()), maxHeight);
            remainingWidth = Math.max(0, remainingWidth - child.getWidth());
        }
    }

    @Override
    protected void layoutInflexibleWidgets(int maxWidth, int maxHeight) {
        int remainingWidth = maxWidth;
        for (Widget child : inflexibleChildren()) {
            child.layout(remainingWidth, maxHeight);
            height = Math.min(Math.max(height, child.getHeight()), maxHeight);
            remainingWidth = Math.max(0, remainingWidth - child.getWidth());
        }
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
