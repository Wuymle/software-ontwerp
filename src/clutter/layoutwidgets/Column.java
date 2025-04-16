package clutter.layoutwidgets;

import static clutter.core.Dimension.max;

import java.util.List;

import clutter.abstractwidgets.FlexibleWidget;
import clutter.abstractwidgets.ArrayWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;
import clutter.debug.Debug;
import clutter.debug.DebugMode;
import clutter.layoutwidgets.enums.Alignment;

/**
 * A widget that lays out its children in a column.
 */
public class Column extends ArrayWidget {

    /**
     * The alignment of the children in the cross axis.
     */
    public Column(Widget... children) {
        super(children);
    }

    /**
     * The alignment of the children for an array of widgets.
     */
    public Column(List<Widget> children) {
        super(children);
    }

    /**
     * Measure the size of the column.
     */
    @Override
    public void measure() {
        preferredSize = new Dimension(0, 0);
        for (Widget child : children) {
            Debug.log(this, DebugMode.MEASURE, () -> {
                child.measure();
            });
            preferredSize = preferredSize.addY(child.getPreferredSize().y());
            preferredSize = max(preferredSize, child.getPreferredSize());
        }
        if (!flexibleChildren().isEmpty())
            preferredSize = preferredSize.withY(Integer.MAX_VALUE);
    }

    /**
     * Layout the column.
     * 
     * @param minSize the minimum size
     * @param maxSize the maximum size
     */
    @Override
    public void layout(Dimension minSize, Dimension maxSize) {
        if (!flexibleChildren().isEmpty())
            minSize = minSize.withY(maxSize.y());
        super.layout(minSize, maxSize);
        Dimension childMinSize = new Dimension(0, 0);
        if (crossAxisAlignment == Alignment.STRETCH)
            childMinSize = childMinSize.withX(maxSize.x());
        layoutInflexibleWidgets(childMinSize, maxSize);
        int remainingHeight = maxSize.y()
                - inflexibleChildren().stream().mapToInt(child -> child.getSize().y()).sum();
        layoutFlexibleWidgets(childMinSize, maxSize.withY(remainingHeight));
    }

    /**
     * Layout the flexible widgets.
     * 
     * @param minSize the minimum size
     * @param maxSize the maximum size
     */
    @Override
    protected void layoutFlexibleWidgets(Dimension minSize, Dimension maxSize) {
        int totalFlex = flexibleChildren().stream().mapToInt(FlexibleWidget::getFlex).sum();
        for (FlexibleWidget child : flexibleChildren()) {
            int maxChildHeight = maxSize.y() * child.getFlex() / totalFlex;
            child.layout(minSize.withY(maxChildHeight), maxSize.withY(maxChildHeight));
        }
    }

    /**
     * Layout the inflexible widgets.
     * 
     * @param minSize the minimum size
     * @param maxSize the maximum size
     */
    @Override
    protected void layoutInflexibleWidgets(Dimension minSize, Dimension maxSize) {
        int remainingHeight = maxSize.y();
        for (Widget child : inflexibleChildren()) {
            child.layout(minSize, maxSize.withY(remainingHeight));
            remainingHeight = Math.max(0, remainingHeight - child.getSize().y());
        }
    }

    /**
     * Position the children.
     */
    @Override
    protected void positionChildren() {
        int childY = position.y();
        for (Widget child : children) {
            Dimension placementPosition = new Dimension(position.x(), childY);
            if (crossAxisAlignment == Alignment.CENTER)
                placementPosition = placementPosition.addX((size.x() - child.getSize().x()) / 2);
            if (crossAxisAlignment == Alignment.END)
                placementPosition = placementPosition.addX(size.x() - child.getSize().x());
            child.setPosition(placementPosition);
            childY += child.getSize().y();
        }
    }

    /**
     * set the cross axis alignment
     * 
     * @param alignment the alignment
     * @return self
     */
    @Override
    public Column setCrossAxisAlignment(Alignment alignment) {
        crossAxisAlignment = alignment;
        return this;
    }
}
