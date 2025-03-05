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
    public void layout(Dimension maxSize) {
        if (child == null)
            return;
        child.layout(maxSize);
        if (horizontalAlignment == Alignment.STRETCH)
            child.setSize(child.getSize().withX(size.x()));
        if (verticalAlignment == Alignment.STRETCH)
            child.setSize(child.getSize().withY(size.y()));
        this.size = child.getSize();
    }

    @Override
    public Interactable hitTest(int id, Dimension hitPos, int clickCount) {
        if (child == null)
            return null;
        return child.hitTest(id, hitPos, clickCount);
    }
}
