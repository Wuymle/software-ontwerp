package clutter.layoutwidgets;

import static clutter.core.Dimension.max;

import java.util.List;

import clutter.abstractwidgets.FlexibleWidget;
import clutter.abstractwidgets.ArrayWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;
import clutter.layoutwidgets.enums.Alignment;
import clutter.layoutwidgets.enums.Distribution;

/**
 * A widget that lays out its children in a row.
 */
public class Row extends ArrayWidget {
    public Row(Widget... children) {
        super(children);
    }

    /**
     * The alignment of the children for an array of widgets.
     */
    public Row(List<Widget> children) {
        super(children);
    }

    /**
     * Measure the size of the row.
     */
    @Override
    protected void runMeasure() {
        for (Widget child : children) {
            child.measure();
            preferredSize = preferredSize.addX(child.getPreferredSize().x());
            preferredSize = max(preferredSize, child.getPreferredSize());
        }
        if (!flexibleChildren().isEmpty())
            preferredSize = preferredSize.withX(Integer.MAX_VALUE);
    }

    /**
     * Layout the row.
     * 
     * @param minSize the minimum size
     * @param maxSize the maximum size
     */
    @Override
    protected void runLayout(Dimension minSize, Dimension maxSize) {
        if (!flexibleChildren().isEmpty())
            minSize = minSize.withX(maxSize.x());
        size = Dimension.max(minSize, Dimension.min(maxSize, preferredSize));
        Dimension childMinSize = Dimension.ZERO;
        if (crossAxisAlignment == Alignment.STRETCH)
            childMinSize = childMinSize.withY(size.y());
        layoutInflexibleWidgets(childMinSize, maxSize);
        int remainingWidth = maxSize.x()
                - inflexibleChildren().stream().mapToInt(child -> child.getSize().x()).sum();
        layoutFlexibleWidgets(childMinSize, maxSize.withX(remainingWidth));
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
            int maxChildWidth = maxSize.x() * child.getFlex() / totalFlex;
            child.layout(minSize.withX(maxChildWidth), maxSize.withX(maxChildWidth));
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
        int remainingWidth = maxSize.x();
        for (Widget child : inflexibleChildren()) {
            child.layout(minSize, maxSize.withX(remainingWidth));
            remainingWidth = Math.max(0, remainingWidth - child.getSize().x());
        }
    }

    /**
     * Position the children.
     */
    // @Override
    // protected void positionChildren() {
    // int childX = position.x();
    // int extraSpace = size.x() - preferredSize.x();
    // int spacingBetween = 0;
    // if (extraSpace > 0) {
    // switch (distribution) {
    // case Distribution.CENTER:
    // childX += extraSpace / 2;
    // break;
    // case Distribution.END:
    // childX += extraSpace;
    // break;
    // case Distribution.SPACE_BETWEEN:
    // spacingBetween = extraSpace / (children.size() - 1);
    // break;
    // case Distribution.SPACE_AROUND:
    // spacingBetween = extraSpace / children.size();
    // childX += spacingBetween / 2;
    // break;
    // case Distribution.SPACE_EVENLY:
    // spacingBetween = extraSpace / (children.size() + 1);
    // childX += spacingBetween;
    // break;
    // default:
    // break;
    // }
    // }
    // for (Widget child : children) {
    // Dimension placementPosition = new Dimension(childX, position.y());
    // if (crossAxisAlignment == Alignment.CENTER)
    // placementPosition = placementPosition.addY((size.y() - child.getSize().y()) / 2);
    // if (crossAxisAlignment == Alignment.END)
    // placementPosition = placementPosition.addY(size.y() - child.getSize().y());
    // child.setPosition(placementPosition);
    // childX += child.getSize().x();
    // }
    // }

    /**
     * The alignment of the children.
     * 
     * @param alignment the alignment
     * @return self
     */
    @Override
    public Row setCrossAxisAlignment(Alignment alignment) {
        this.crossAxisAlignment = alignment;
        return this;
    }

    @Override
    public Row setDistribution(Distribution distribution) {
        this.distribution = distribution;
        return this;
    }

    @Override
    protected void positionChild(int mainAxisOffset, int crossAxisOffset, Widget child) {
        child.setPosition(position.addX(mainAxisOffset).addY(crossAxisOffset));
    }

    @Override
    protected int getMainAxisExtraSpace() {
        return size.x() - preferredSize.x();
    }

    @Override
    protected int getCrossAxisSize() {
        return size.y();
    }

    @Override
    protected int getChildCrossAxisSize(Widget child) {
        return child.getSize().y();
    }

    @Override
    protected int getChildMainAxisSize(Widget child) {
        return child.getSize().x();
    }
}
