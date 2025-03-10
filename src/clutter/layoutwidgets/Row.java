package clutter.layoutwidgets;

import static clutter.core.Dimension.max;
import static clutter.core.Dimension.min;

import clutter.abstractwidgets.FlexibleWidget;
import clutter.abstractwidgets.MultiChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;
import clutter.layoutwidgets.enums.Alignment;

public class Row extends MultiChildWidget {
    public Row(Widget... children) {
        super(children);
    }

    @Override
    public void measure() {
        preferredSize = new Dimension(0, 0);
        for (Widget child : children) {
            child.measure();
            preferredSize = preferredSize.addX(child.getPreferredSize().x());
            preferredSize = max(preferredSize, child.getPreferredSize());
        }
    }

    @Override
    public void layout(Dimension minSize, Dimension maxSize) {
        super.layout(minSize, maxSize);
        Dimension childMinSize = new Dimension(0, 0);
        if (crossAxisAlignment == Alignment.STRETCH)
            childMinSize = childMinSize.withY(maxSize.y());
        layoutInflexibleWidgets(childMinSize, maxSize);
        int remainingWidth = inflexibleChildren().reduce(maxSize.x(), (width, child) -> width - child.getSize().x());
        layoutFlexibleWidgets(childMinSize, maxSize.withX(remainingWidth));
    }

    @Override
    protected void layoutFlexibleWidgets(Dimension minSize, Dimension maxSize) {
        int totalFlex = flexibleChildren().reduce(0, (flex, child) -> flex + ((FlexibleWidget) child).getFlex());
        for (Widget child : flexibleChildren()) {
            int maxChildWidth = maxSize.x() * ((FlexibleWidget) child).getFlex() / totalFlex;
            ((FlexibleWidget) child).layout(minSize.withX(maxChildWidth), maxSize.withX(maxChildWidth));
        }
    }

    @Override
    protected void layoutInflexibleWidgets(Dimension minSize, Dimension maxSize) {
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
