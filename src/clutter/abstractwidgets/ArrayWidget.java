package clutter.abstractwidgets;

import java.util.List;
import clutter.core.Dimension;
import clutter.layoutwidgets.enums.Alignment;
import clutter.layoutwidgets.enums.Distribution;

/**
 * A widget that can have multiple child widgets.
 */
public abstract class ArrayWidget extends MultiChildWidget {
    protected Alignment crossAxisAlignment = Alignment.START;
    protected Distribution distribution = Distribution.START;

    /**
     * constructor for the multi child widget
     * 
     * @param children the child widgets
     */
    public ArrayWidget(Widget... children) {
        super(children);
    }

    /**
     * constructor for the multi child widget
     * 
     * @param children the child widgets
     */
    public ArrayWidget(List<Widget> children) {
        super(children);
    }

    /**
     * Layout the flexible widgets
     * 
     * @param minSize the minimum size
     * @param maxSize the maximum size
     */
    protected abstract void layoutFlexibleWidgets(Dimension minSize, Dimension maxSize);

    /**
     * Layout the inflexible widgets
     * 
     * @param minSize the minimum size
     * @param maxSize the maximum size
     */
    protected abstract void layoutInflexibleWidgets(Dimension minSize, Dimension maxSize);

    /**
     * return the array of flexible children
     * 
     * @return the flexible children
     */
    protected List<FlexibleWidget> flexibleChildren() {
        return children.stream().filter(child -> child instanceof FlexibleWidget)
                .map(child -> (FlexibleWidget) child).toList();
    }

    /**
     * return the array of inflexible children
     * 
     * @return the inflexible children
     */
    protected List<Widget> inflexibleChildren() {
        return children.stream().filter(child -> !(child instanceof FlexibleWidget)).toList();
    }

    protected abstract void positionChild(int mainAxisOffset, int crossAxisOffset, Widget child);

    protected abstract int getMainAxisExtraSpace();

    protected abstract int getCrossAxisSize();

    protected abstract int getChildCrossAxisSize(Widget child);

    protected abstract int getChildMainAxisSize(Widget child);

    @Override
    protected void positionChildren() {
        int mainAxisOffset = 0;
        int extraSpace = getMainAxisExtraSpace();
        int spacingBetween = 0;
        if (extraSpace > 0) {
            switch (distribution) {
                case Distribution.CENTER:
                    mainAxisOffset += extraSpace / 2;
                    break;
                case Distribution.END:
                    mainAxisOffset += extraSpace;
                    break;
                case Distribution.SPACE_BETWEEN:
                    spacingBetween = extraSpace / (children.size() - 1);
                    break;
                case Distribution.SPACE_AROUND:
                    spacingBetween = extraSpace / children.size();
                    mainAxisOffset += spacingBetween / 2;
                    break;
                case Distribution.SPACE_EVENLY:
                    spacingBetween = extraSpace / (children.size() + 1);
                    mainAxisOffset += spacingBetween;
                    break;
                default:
                    break;
            }
        }
        for (Widget child : children) {
            int crossAxisOffset = 0;
            int crossAxisExtraSpace = getCrossAxisSize() - getChildCrossAxisSize(child);
            if (crossAxisAlignment == Alignment.CENTER)
                crossAxisOffset = crossAxisExtraSpace / 2;
            if (crossAxisAlignment == Alignment.END)
                crossAxisOffset = crossAxisExtraSpace;
            positionChild(mainAxisOffset, crossAxisOffset, child);
            mainAxisOffset += getChildMainAxisSize(child) + spacingBetween;
        }
    }


    /**
     * set the cross axis alignment
     * 
     * @param alignment the alignment
     * @return the widget
     */
    public abstract ArrayWidget setCrossAxisAlignment(Alignment alignment);

    public abstract ArrayWidget setDistribution(Distribution distribution);
}
