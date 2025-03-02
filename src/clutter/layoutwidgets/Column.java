package clutter.layoutwidgets;

import clutter.abstractwidgets.FlexibleWidget;
import clutter.abstractwidgets.MultiChildWidget;
import clutter.abstractwidgets.Widget;

public class Column extends MultiChildWidget {

    public Column(Widget... children) {
        super(children);
    }

    @Override
    public void layout(int maxWidth, int maxHeight) {
        layoutInflexibleWidgets(maxWidth, maxHeight);
        int remainingHeight = inflexibleChildren().reduce(maxHeight, (height, child) -> height - child.getHeight());
        layoutFlexibleWidgets(maxWidth, remainingHeight);
    }

    @Override
    protected void layoutFlexibleWidgets(int maxWidth, int maxHeight) {
        int totalFlex = flexibleChildren().reduce(0, (flex, child) -> flex + ((FlexibleWidget) child).getFlex());
        int remainingHeight = maxHeight;
        for (Widget child : flexibleChildren()) {
            int maxChildHeight = child.getHeight() + remainingHeight * ((FlexibleWidget) child).getFlex() / totalFlex;
            child.layout(maxWidth, maxChildHeight);
            width = Math.min(Math.max(width, child.getWidth()), maxWidth);
            remainingHeight = Math.max(0, remainingHeight - child.getHeight());
        }
    }

    @Override
    protected void layoutInflexibleWidgets(int maxWidth, int maxHeight) {
        int remainingHeight = maxHeight;
        for (Widget child : inflexibleChildren()) {
            child.layout(maxWidth, remainingHeight);
            width = Math.min(Math.max(width, child.getWidth()), maxWidth);
            remainingHeight = Math.max(0, remainingHeight - child.getHeight());
        }
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
