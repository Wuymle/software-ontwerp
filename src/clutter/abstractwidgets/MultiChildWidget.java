package clutter.abstractwidgets;

import static clutter.core.Dimension.contains;

import java.awt.Graphics;
import java.util.List;

import clutter.core.Debug;
import clutter.core.Dimension;
import clutter.layoutwidgets.enums.Alignment;

/**
 * A widget that can have multiple child widgets.
 */
public abstract class MultiChildWidget extends ParentWidget {
    protected Alignment crossAxisAlignment = Alignment.START;
    public Widget[] children;

    /**
     * constructor for the multi child widget
     * 
     * @param children the child widgets
     */
    public MultiChildWidget(Widget... children) {
        this.children = children;
    }

    /**
     * constructor for the multi child widget
     * 
     * @param children the child widgets
     */
    public MultiChildWidget(List<Widget> children) {
        this.children = children.toArray(new Widget[0]);
    }

    // public <W extends Widget, L extends Iterable<W>> MultiChildWidget(L children)
    // {
    // Iterator<W> iterator = children.iterator();
    // this.children = new Widget[children.toString().length()];
    // for (int i = 0; iterator.hasNext(); i++) {
    // this.children[i] = iterator.next();
    // }
    // }

    /**
     * paint the widget
     * 
     * @param graphics the graphics object
     */
    public void paintChildren(Graphics g) {
        for (Widget child : children) {
            child.paint(g);
            Debug.log(this, "painting child:", child.getClass().getSimpleName(), "at:", child.position,
                    "size:", child.size);
        }
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
     * hit test the widget
     * 
     * @param id         the id of the clickEvent
     * @param hitPos     the position of the click
     * @param clickCount the number of clicks
     */
    public boolean hitTest(int id, Dimension hitPos, int clickCount) {
        if (!contains(position, size, hitPos))
            return false;
        for (int i = children.length - 1; i >= 0; i--) {
            if (children[i].hitTest(id, hitPos, clickCount))
                return true;
        }
        return false;
    }

    /**
     * set the cross axis alignment
     * 
     * @param alignment the alignment
     * @return the widget
     */
    public abstract MultiChildWidget setCrossAxisAlignment(Alignment alignment);
}
