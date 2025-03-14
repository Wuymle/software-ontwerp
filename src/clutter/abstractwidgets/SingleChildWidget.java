package clutter.abstractwidgets;

import static clutter.core.Dimension.contains;

import java.awt.Color;
import java.awt.Graphics;

import clutter.core.Debug;
import clutter.core.Dimension;
import clutter.layoutwidgets.enums.Alignment;
import clutter.widgetinterfaces.Interactable;

/**
 * A widget that can have only one child widget.
 */
public abstract class SingleChildWidget extends Widget {
    protected Alignment horizontalAlignment = Alignment.START;
    protected Alignment verticalAlignment = Alignment.START;

    protected Widget child;

    /**
     * @param child the child widget
     */
    public SingleChildWidget(Widget child) {
        this.child = child;
    }

    /**
     * @return self
     * @param alignment the horizontal alignment
     */
    public SingleChildWidget setHorizontalAlignment(Alignment alignment) {
        horizontalAlignment = alignment;
        return this;
    }

    /**
     * @return self
     * @param alignment the vertical alignment
     */
    public SingleChildWidget setVerticalAlignment(Alignment alignment) {
        verticalAlignment = alignment;
        return this;
    }

    /**
     * @param g the graphics object
     */
    @Override
    public void paint(Graphics g) {
        if (debug) {
            g.setColor(Color.black);
            g.drawRect(position.x(), position.y(), size.x(), size.y());
        }
        if (child == null)
            return;
        positionChild();
        child.paint(g);
    }

    /**
     * Position the child widget.
     */
    public void positionChild() {
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
                maxSize);
    }

    /**
     * @param id         the id of the clickEvent
     * @param hitPos     the position of the click
     * @param clickCount the number of clicks
     * @return the interactable
     */
    @Override
    public Interactable hitTest(int id, Dimension hitPos, int clickCount) {
        Debug.log(this, "HIT");
        if (child == null || !contains(position, size, hitPos))
            return null;
        Debug.log(this, position + " " + size + " " + hitPos);
        return child.hitTest(id, hitPos, clickCount);
    }
}
