package clutter.abstractwidgets;

import java.awt.Graphics;

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

}
