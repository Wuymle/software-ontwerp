package clutter.layoutwidgets;

import static clutter.core.Dimension.max;
import static clutter.core.Dimension.min;

import clutter.abstractwidgets.FlexibleWidget;
import clutter.abstractwidgets.MultiChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;
import clutter.layoutwidgets.enums.Alignment;

public class Column extends MultiChildWidget {

    public Column(Widget... children) {
        super(children);
    }

    @Override
    public void measure() {
        preferredSize = new Dimension(0, 0);
        for (Widget child : children) {
            child.measure();
            preferredSize = preferredSize.addY(child.getPreferredSize().y());
            preferredSize = max(preferredSize, child.getPreferredSize());
        }
    }

    @Override
    public void layout(Dimension minSize, Dimension maxSize) {
        super.layout(minSize, maxSize);
        Dimension childMinSize = new Dimension(0, 0);
        if (crossAxisAlignment == Alignment.STRETCH)
            childMinSize = childMinSize.withX(maxSize.x());
        layoutInflexibleWidgets(childMinSize, maxSize);
        int remainingHeight = inflexibleChildren().reduce(maxSize.y(), (height, child) -> height - child.getSize().y());
        layoutFlexibleWidgets(childMinSize, maxSize.withY(remainingHeight));
    }

    @Override
    protected void layoutFlexibleWidgets(Dimension minSize, Dimension maxSize) {
        int totalFlex = flexibleChildren().reduce(0, (flex, child) -> flex + ((FlexibleWidget) child).getFlex());
        for (Widget child : flexibleChildren()) {
            int maxChildHeight = maxSize.y() * ((FlexibleWidget) child).getFlex() / totalFlex;
            ((FlexibleWidget) child).layout(minSize.withY(maxChildHeight), maxSize.withY(maxChildHeight));
        }
    }

    @Override
    protected void layoutInflexibleWidgets(Dimension minSize, Dimension maxSize) {
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
