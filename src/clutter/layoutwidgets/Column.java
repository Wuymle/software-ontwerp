package clutter.layoutwidgets;

import static clutter.core.Dimension.max;

import java.util.List;

import clutter.abstractwidgets.FlexibleWidget;
import clutter.abstractwidgets.MultiChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Debug;
import clutter.core.Dimension;
import clutter.layoutwidgets.enums.Alignment;

public class Column extends MultiChildWidget {

    public Column(Widget... children) {
        super(children);
    }

    public Column(List<Widget> children) {
        super(children);
    }

    @Override
    public void measure() {
        preferredSize = new Dimension(0, 0);
        for (Widget child : children) {
            child.measure();
            if (debug) Debug.log(this, "child preferredSize:", child.getPreferredSize());
            preferredSize = preferredSize.addY(child.getPreferredSize().y());
            preferredSize = max(preferredSize, child.getPreferredSize());
        }
        if (debug)
            Debug.log(this, "preferredSize:", preferredSize);
    }

    @Override
    public void layout(Dimension minSize, Dimension maxSize) {
        if (!flexibleChildren().isEmpty())
            minSize = minSize.withY(maxSize.y());
        super.layout(minSize, maxSize);
        Dimension childMinSize = new Dimension(0, 0);
        if (crossAxisAlignment == Alignment.STRETCH)
            childMinSize = childMinSize.withX(maxSize.x());
        layoutInflexibleWidgets(childMinSize, maxSize);
        int remainingHeight = maxSize.y() - inflexibleChildren().stream().mapToInt(child -> child.getSize().y()).sum();
        layoutFlexibleWidgets(childMinSize, maxSize.withY(remainingHeight));
    }

    @Override
    protected void layoutFlexibleWidgets(Dimension minSize, Dimension maxSize) {
        int totalFlex = flexibleChildren().stream().mapToInt(FlexibleWidget::getFlex).sum();
        for (FlexibleWidget child : flexibleChildren()) {
            int maxChildHeight = maxSize.y() * child.getFlex() / totalFlex;
            child.layout(minSize.withY(maxChildHeight), maxSize.withY(maxChildHeight));
        }
    }

    @Override
    protected void layoutInflexibleWidgets(Dimension minSize, Dimension maxSize) {
        int remainingHeight = maxSize.y();
        for (Widget child : inflexibleChildren()) {
            child.layout(minSize, maxSize.withY(remainingHeight));
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
