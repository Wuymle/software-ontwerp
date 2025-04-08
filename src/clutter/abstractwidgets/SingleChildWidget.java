package clutter.abstractwidgets;

import static clutter.core.Dimension.contains;

import java.awt.Graphics;

import clutter.core.Debug;
import clutter.core.Dimension;
import clutter.layoutwidgets.enums.Alignment;

/**
 * A widget that can have only one child widget.
 */
public abstract class SingleChildWidget extends ParentWidget {
    protected Alignment horizontalAlignment = Alignment.START;
    protected Alignment verticalAlignment = Alignment.START;

    protected Widget child;

    /**
     * Constructor for the single child widget.
     * 
     * @param child the child widget
     */
    public SingleChildWidget(Widget child) {
        this.child = child;
        if (child instanceof FlexibleWidget) {
            System.out.println("Should not put Flexible child in singleChildWidget:" + getClass().getSimpleName() + " has " + child.getClass().getSimpleName());
        }
    }

    /**
     * set the horizontal alignment
     * 
     * @return self
     * @param alignment the horizontal alignment
     */
    public SingleChildWidget setHorizontalAlignment(Alignment alignment) {
        horizontalAlignment = alignment;
        return this;
    }

    /**
     * set the vertical alignment
     * 
     * @return self
     * @param alignment the vertical alignment
     */
    public SingleChildWidget setVerticalAlignment(Alignment alignment) {
        verticalAlignment = alignment;
        return this;
    }

    /**
     * paint the widget
     * 
     * @param g the graphics object
     */
    @Override
    public void paintChildren(Graphics g) {
        if (child == null)
            return;
        child.paint(g);
    }

    /**
     * Position the child widget.
     */
    @Override
    public void positionChildren() {
        if (child == null)
            return;
        Dimension placementPosition = position;
        if (horizontalAlignment == Alignment.CENTER)
            placementPosition = placementPosition.addX((size.x() - child.getSize().x()) / 2);
        if (horizontalAlignment == Alignment.END)
            placementPosition = placementPosition.addX(size.x() - child.getSize().x());
        if (verticalAlignment == Alignment.CENTER)
            placementPosition = placementPosition.addY((size.y() - child.getSize().y()) / 2);
        if (verticalAlignment == Alignment.END)
            placementPosition = placementPosition.addY(size.y() - child.getSize().y());
        child.setPosition(placementPosition);
    }

    /**
     * Measure the widget.
     */
    @Override
    public void measure() {
        if (child == null) {
            preferredSize = new Dimension(0, 0);
        } else {
            child.measure();
            preferredSize = child.getPreferredSize();
        }
    }

    /**
     * Layout the widget.
     * 
     * @param minsize the minimum size
     * @param maxSize the maximum size
     */
    @Override
    public void layout(Dimension minsize, Dimension maxSize) {
        super.layout(minsize, maxSize);
        if (child == null)
            return;
        child.layout(
                new Dimension(
                        horizontalAlignment == Alignment.STRETCH ? size.x() : 0,
                        verticalAlignment == Alignment.STRETCH ? size.y() : 0),
                size);
    }

    /**
     * Hit test the widget.
     * 
     * @param id         the id of the clickEvent
     * @param hitPos     the position of the click
     * @param clickCount the number of clicks
     * @return the interactable
     */
    @Override
    public boolean hitTest(int id, Dimension hitPos, int clickCount) {
        Debug.log(this, "HIT");
        if (child == null || !contains(position, size, hitPos))
            return false;
        // Debug.log(this, position + " " + size + " " + hitPos);
        return child.hitTest(id, hitPos, clickCount);
    }
}
