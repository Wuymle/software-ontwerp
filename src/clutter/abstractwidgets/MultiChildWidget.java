package clutter.abstractwidgets;

import java.awt.Graphics;

import clutter.widgetinterfaces.Interactable;

public abstract class MultiChildWidget extends Widget {
    public Widget[] children;

    public MultiChildWidget(Widget... children) {
        this.children = children;
    }

    public void paint(Graphics g) {
        positionChildren();
        for (Widget child : children) {
            child.paint(g);
        }
    }

    protected abstract void positionChildren();

    protected abstract void layoutFlexibleWidgets(int maxWidth, int maxHeight);

    protected abstract void layoutInflexibleWidgets(int maxWidth, int maxHeight);

    protected MultiChildIterable flexibleChildren() {
        return new MultiChildIterable(children).filter(
                (Widget child) -> child instanceof FlexibleWidget);
    }

    protected MultiChildIterable inflexibleChildren() {
        return new MultiChildIterable(children).filter(
                (Widget child) -> !(child instanceof FlexibleWidget));
    }

    public Interactable hitTest(int id, int x, int y, int clickCount) {
        Interactable hit = null;
        for (int i = children.length - 1; i >= 0; i--) {
            hit = children[i].hitTest(id, x, y, clickCount);
            if (hit != null)
                return hit;
        }
        return hit;
    }
}
