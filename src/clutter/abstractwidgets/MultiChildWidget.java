package clutter.abstractwidgets;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.List;

import clutter.core.Debug;
import clutter.core.Dimension;
import clutter.layoutwidgets.enums.Alignment;
import clutter.widgetinterfaces.Interactable;

/**
 * A widget that can have multiple child widgets.
 */
public abstract class MultiChildWidget extends Widget {
    protected Alignment crossAxisAlignment = Alignment.START;
    public Widget[] children;

    /**
     * @param children the child widgets
     */
    public MultiChildWidget(Widget... children) {
        this.children = children;
    }

    /**
     * @param children the child widgets
     */
    public <W extends Widget> MultiChildWidget(List<W> children) {
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
     * @param graphics the graphics object
     */
    public void paint(Graphics g) {
        Debug.log(this, "position:", position);
        positionChildren();
        for (Widget child : children) {
            child.paint(g);
        }
        if (debug) {
            g.setColor(Color.black);
            g.drawRect(position.x(), position.y(), size.x(), size.y());
        }
    }

    /**
     * Position the child widgets.
     */
    protected abstract void positionChildren();

    /**
     * @param minSize the minimum size
     * @param maxSize the maximum size
     */
    protected abstract void layoutFlexibleWidgets(Dimension minSize, Dimension maxSize);

    /**
     * @param minSize the minimum size
     * @param maxSize the maximum size
     */
    protected abstract void layoutInflexibleWidgets(Dimension minSize, Dimension maxSize);

    /**
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
     * @param id         the id of the clickEvent
     * @param hitPos     the position of the click
     * @param clickCount the number of clicks
     */
    public Interactable hitTest(int id, Dimension hitPos, int clickCount) {
        Interactable hit = null;
        for (int i = children.length - 1; i >= 0; i--) {
            Debug.log(this, "Hit testing child: " + children[i].getClass().getSimpleName());
            hit = children[i].hitTest(id, hitPos, clickCount);
            Debug.log(this, "Hit test result: ", hit);
            if (hit != null)
                return hit;
        }
        return hit;
    }

    /**
     * @param alignment the alignment
     * @return the widget
     */
    public abstract MultiChildWidget setCrossAxisAlignment(Alignment alignment);
}
