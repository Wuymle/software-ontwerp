package clutter.abstractwidgets;

import static clutter.core.Dimension.contains;

import java.awt.Graphics;

import clutter.core.Dimension;
import clutter.core.Rectangle;
import clutter.debug.Debug;
import clutter.debug.DebugMode;
import clutter.layoutwidgets.NullWidget;
import clutter.layoutwidgets.enums.Alignment;

/**
 * A widget that can have only one child widget.
 */
public abstract class SingleChildWidget extends ParentWidget {
    protected Alignment horizontalAlignment = Alignment.START;
    protected Alignment verticalAlignment = Alignment.START;

    protected Widget child = new NullWidget();

    public SingleChildWidget() {}

    /**
     * Constructor for the single child widget.
     * 
     * @param child the child widget
     */
    public SingleChildWidget(Widget child) {
        this.child = child;
        if (child == null)
            throw new IllegalArgumentException("Child should not be set to null");
        if (child instanceof FlexibleWidget) {
            System.out.println("Should not put Flexible child in singleChildWidget:"
                    + getClass().getSimpleName() + " has " + child.getClass().getSimpleName());
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
    protected void paintChildren(Graphics g) {
        if (Rectangle.fromAWT(g.getClipBounds())
                .intersects(new Rectangle(child.position, child.size)))
            child.paint(g);

    }

    /**
     * Position the child widget.
     */
    @Override
    protected void positionChildren() {
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
    protected void runMeasure() {
        child.measure();
        Debug.log(this, DebugMode.MEASURE, "Measure with child size: " + child.getSize());
        preferredSize = child.getPreferredSize();
    }

    /**
     * Layout the widget.
     * 
     * @param minSize the minimum size
     * @param maxSize the maximum size
     */
    @Override
    protected void runLayout(Dimension minSize, Dimension maxSize) {
        size = Dimension.max(minSize, Dimension.min(maxSize, preferredSize));
        child.layout(new Dimension(horizontalAlignment == Alignment.STRETCH ? size.x() : 0,
                verticalAlignment == Alignment.STRETCH ? size.y() : 0), size);
    }

    /**
     * Hit test the widget.
     * 
     * @param id the id of the clickEvent
     * @param hitPos the position of the click
     * @param clickCount the number of clicks
     * @return the interactable
     */
    @Override
    public boolean hitTest(int id, Dimension hitPos, int clickCount) {
        if (!contains(position, size, hitPos))
            return false;
        Debug.log(this, DebugMode.MOUSE, position + " " + size + " " + hitPos);
        return child.hitTest(id, hitPos, clickCount);
    }
}
