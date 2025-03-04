package clutter.layoutwidgets;

import static clutter.core.Dimension.max;
import static clutter.core.Dimension.min;

import clutter.abstractwidgets.FlexibleWidget;
import clutter.abstractwidgets.MultiChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;

public class Row extends MultiChildWidget {

    public Row(Widget... children) {
        super(children);
    }

    @Override
    public void layout(Dimension maxSize) {
        size = new Dimension(0, 0);
        layoutInflexibleWidgets(maxSize);
        int remainingWidth = inflexibleChildren().reduce(maxSize.x(), (width, child) -> width - child.getSize().x());
        layoutFlexibleWidgets(maxSize.withX(remainingWidth));
    }

    @Override
    protected void layoutFlexibleWidgets(Dimension maxSize) {
        int totalFlex = flexibleChildren().reduce(0, (flex, child) -> flex + ((FlexibleWidget) child).getFlex());
        for (Widget child : flexibleChildren()) {
            int maxChildWidth = maxSize.x() * ((FlexibleWidget) child).getFlex() / totalFlex;
            child.layout(maxSize.withX(maxChildWidth));
            size = min(max(size, child.getSize()), maxSize);
            totalFlex -= ((FlexibleWidget) child).getFlex();
        }
    }

    @Override
    protected void layoutInflexibleWidgets(Dimension maxSize) {
        int remainingWidth = maxSize.x();
        for (Widget child : inflexibleChildren()) {
            child.layout(maxSize.withX(remainingWidth));
            size = min(max(size, child.getSize()), maxSize);
            remainingWidth = Math.max(0, remainingWidth - child.getSize().x());
        }
    }

    @Override
    protected void positionChildren() {
        int childX = position.x();
        for (Widget child : children) {
            child.setPosition(position.withX(childX));
            childX += child.getSize().x();
        }
    }

}
