package clutter.layoutwidgets;

import static clutter.core.Dimension.max;
import static clutter.core.Dimension.min;

import clutter.abstractwidgets.FlexibleWidget;
import clutter.abstractwidgets.MultiChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;

public class Column extends MultiChildWidget {

    public Column(Widget... children) {
        super(children);
    }

    @Override
    public void layout(Dimension maxSize) {
        layoutInflexibleWidgets(maxSize);
        int remainingHeight = inflexibleChildren().reduce(maxSize.y(), (height, child) -> height - child.getSize().y());
        layoutFlexibleWidgets(maxSize.withY(remainingHeight));
    }

    @Override
    protected void layoutFlexibleWidgets(Dimension maxSize) {
        int totalFlex = flexibleChildren().reduce(0, (flex, child) -> flex + ((FlexibleWidget) child).getFlex());
        for (Widget child : flexibleChildren()) {
            int maxChildHeight = maxSize.y() * ((FlexibleWidget) child).getFlex() / totalFlex;
            child.layout(maxSize.withY(maxChildHeight));
            size = min(max(size, child.getSize()), maxSize);
            // size = size.withX(Math.min(Math.max(size.x(), child.getSize().x()),
            // maxSize.x()));
        }
    }

    @Override
    protected void layoutInflexibleWidgets(Dimension maxSize) {
        int remainingHeight = maxSize.y();
        for (Widget child : inflexibleChildren()) {
            child.layout(maxSize.withY(remainingHeight));
            size = min(max(size, child.getSize()), maxSize);
            // width = Math.min(Math.max(width, child.getWidth()), maxWidth);
            remainingHeight = Math.max(0, remainingHeight - child.getSize().y());
        }
    }

    @Override
    protected void positionChildren() {
        int childY = size.y();
        for (Widget child : children) {
            child.setPosition(position.withY(childY));
            childY += child.getSize().y();
        }
    }

}
