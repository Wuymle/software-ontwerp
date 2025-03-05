package clutter.layoutwidgets;

import static clutter.core.Dimension.max;
import static clutter.core.Dimension.min;

import clutter.abstractwidgets.FlexibleWidget;
import clutter.abstractwidgets.MultiChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Debug;
import clutter.core.Dimension;
import clutter.core.Direction;
import clutter.layoutwidgets.enums.Alignment;

public class Row extends MultiChildWidget {
    public Row(Widget... children) {
        super(children);
    }

    @Override
    public void layout(Dimension maxSize) {
        layoutInflexibleWidgets(maxSize);
        int remainingWidth = inflexibleChildren().reduce(maxSize.x(), (width, child) -> width - child.getSize().x());
        layoutFlexibleWidgets(maxSize.withX(remainingWidth));
        size = size.withX(0);
        for (Widget child : children) {
            if (crossAxisAlignment == Alignment.STRETCH) {
                child.setSize(child.getSize().withY(size.y()));
            }
            size = size.addX(child.getSize().x());
        }
    }

    @Override
    protected void layoutFlexibleWidgets(Dimension maxSize) {
        int totalFlex = flexibleChildren().reduce(0, (flex, child) -> flex + ((FlexibleWidget) child).getFlex());
        for (Widget child : flexibleChildren()) {
            FlexibleWidget flexibleChild = (FlexibleWidget) child;
            int maxChildWidth = maxSize.x() * flexibleChild.getFlex() / totalFlex;
            flexibleChild.layout(maxSize.withX(maxChildWidth), Direction.HORIZONTAL);
            size = min(max(size, child.getSize()), maxSize);
            maxSize = maxSize.withX(maxSize.x() - child.getSize().x());
            totalFlex -= flexibleChild.getFlex();
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

    public Row setCrossAxisAlignment(Alignment alignment) {
        this.crossAxisAlignment = alignment;
        return this;
    }
}
