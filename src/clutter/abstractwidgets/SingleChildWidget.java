package clutter.abstractwidgets;

import java.awt.Graphics;

import clutter.core.Dimension;
import clutter.layoutwidgets.enums.Alignment;
import clutter.widgetinterfaces.Interactable;

public abstract class SingleChildWidget extends ChildWidget {
    protected Alignment horizontalAlignment = Alignment.START;
    protected Alignment verticalAlignment = Alignment.START;

    protected Widget child;

    public SingleChildWidget(Widget child) {
        this.child = child;
    }

    @Override
    public void paint(Graphics g) {
        if (child == null)
            return;
        positionChild();
        child.paint(g);
    }

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

    @Override
    public void measure() {
        if (child == null) {
            preferredSize = new Dimension(0, 0);
        } else {
            child.measure();
            preferredSize = child.getPreferredSize();
        }
    }

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

    @Override
    public Interactable hitTest(int id, Dimension hitPos, int clickCount) {
        if (child == null)
            return null;
        return child.hitTest(id, hitPos, clickCount);
    }
}
