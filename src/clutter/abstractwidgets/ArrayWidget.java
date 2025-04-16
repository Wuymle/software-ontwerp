package clutter.abstractwidgets;

import java.util.List;
import clutter.core.Dimension;
import clutter.layoutwidgets.enums.Alignment;

/**
 * A widget that can have multiple child widgets.
 */
public abstract class ArrayWidget extends MultiChildWidget {
    protected Alignment crossAxisAlignment = Alignment.START;

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
        List<FlexibleWidget> flexibles = new java.util.ArrayList<>();
        for (Widget child : children) {
            if (child instanceof FlexibleWidget) {
                flexibles.add((FlexibleWidget) child);
            }
        }
        return flexibles;
    }

    /**
     * return the array of inflexible children
     * 
     * @return the inflexible children
     */
    protected List<Widget> inflexibleChildren() {
        List<Widget> inflexibles = new java.util.ArrayList<>();
        for (Widget child : children) {
            if (!(child instanceof FlexibleWidget)) {
                inflexibles.add(child);
            }
        }
        return inflexibles;
    }


    /**
     * set the cross axis alignment
     * 
     * @param alignment the alignment
     * @return the widget
     */
    public abstract ArrayWidget setCrossAxisAlignment(Alignment alignment);
}
