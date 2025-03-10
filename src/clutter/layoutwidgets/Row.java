package clutter.layoutwidgets;

import static clutter.core.Dimension.max;

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
        if (!flexibleChildren().isEmpty())
            preferredSize = preferredSize.withX(Integer.MAX_VALUE);
    }

    @Override
    public void layout(Dimension minSize, Dimension maxSize) {
        if (!flexibleChildren().isEmpty())
            minSize = minSize.withX(maxSize.x());
        super.layout(minSize, maxSize);
        Dimension childMinSize = new Dimension(0, 0);
        if (crossAxisAlignment == Alignment.STRETCH)
            childMinSize = childMinSize.withY(size.y());
        layoutInflexibleWidgets(childMinSize, maxSize);
        int remainingWidth = maxSize.x() - inflexibleChildren().stream().mapToInt(child -> child.getSize().x()).sum();
        layoutFlexibleWidgets(childMinSize, maxSize.withX(remainingWidth));
    }

    @Override
    protected void layoutFlexibleWidgets(Dimension minSize, Dimension maxSize) {
        int totalFlex = flexibleChildren().stream().mapToInt(FlexibleWidget::getFlex).sum();
        for (FlexibleWidget child : flexibleChildren()) {
            int maxChildWidth = maxSize.x() * child.getFlex() / totalFlex;
            child.layout(minSize.withX(maxChildWidth), maxSize.withX(maxChildWidth));
        }
    }

    @Override
    protected void layoutInflexibleWidgets(Dimension minSize, Dimension maxSize) {
        int remainingWidth = maxSize.x();
        for (Widget child : inflexibleChildren()) {
            child.layout(minSize, maxSize.withX(remainingWidth));
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
