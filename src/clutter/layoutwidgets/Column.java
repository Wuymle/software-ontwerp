package clutter.layoutwidgets;

import static clutter.core.Dimension.max;
import static clutter.core.Dimension.min;

import java.util.List;

import clutter.abstractwidgets.FlexibleWidget;
import clutter.abstractwidgets.MultiChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;
import clutter.core.Direction;
import clutter.layoutwidgets.enums.Alignment;

public class Column extends MultiChildWidget {

    public Column(Widget... children) {
        super(children);
    }

    public Column(List<Widget> children) {
        super(children);
    }

    @Override
    public void layout(Dimension maxSize) {
        layoutInflexibleWidgets(maxSize);
        int remainingHeight = inflexibleChildren().reduce(maxSize.y(), (height, child) -> height - child.getSize().y());
        layoutFlexibleWidgets(maxSize.withY(remainingHeight));
        size = size.withY(0);
        for (Widget child : children) {
            if (crossAxisAlignment == Alignment.STRETCH) {
                child.setSize(child.getSize().withX(size.x()));
            }
            size = size.addY(child.getSize().y());
        }
    }

    @Override
    protected void layoutFlexibleWidgets(Dimension maxSize) {
        int totalFlex = flexibleChildren().reduce(0, (flex, child) -> flex + ((FlexibleWidget) child).getFlex());
        for (Widget child : flexibleChildren()) {
            FlexibleWidget flexibleChild = (FlexibleWidget) child;
            int maxChildHeight = maxSize.y() * flexibleChild.getFlex() / totalFlex;
            flexibleChild.layout(maxSize.withY(maxChildHeight), Direction.VERTICAL);
            size = min(max(size, child.getSize()), maxSize);
            maxSize = maxSize.withY(maxSize.y() - child.getSize().y());
            totalFlex -= flexibleChild.getFlex();
        }
    }

    @Override
    protected void layoutInflexibleWidgets(Dimension maxSize) {
        int remainingHeight = maxSize.y();
        for (Widget child : inflexibleChildren()) {
            child.layout(maxSize.withY(remainingHeight));
            size = min(max(size, child.getSize()), maxSize);
            remainingHeight = Math.max(0, remainingHeight - child.getSize().y());
        }
    }

    @Override
    protected void positionChildren() {
        int childY = position.y();
        for (Widget child : children) {
            child.setPosition(position.withY(childY));
            childY += child.getSize().y();
        }
    }

    @Override
    public Column setCrossAxisAlignment(Alignment alignment) {
        crossAxisAlignment = alignment;
        return this;
    }
}
