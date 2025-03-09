package clutter.abstractwidgets;

import java.awt.Graphics;
import java.util.List;

import clutter.core.Dimension;
import clutter.layoutwidgets.enums.Alignment;
import clutter.widgetinterfaces.Interactable;

public abstract class MultiChildWidget extends ChildWidget {
    protected Alignment crossAxisAlignment = Alignment.START;
    public Widget[] children;

    public MultiChildWidget(Widget... children) {
        this.children = children;
    }

    public MultiChildWidget(List<Widget> children) {
        this.children = children.toArray(new Widget[0]);
    }

    public void paint(Graphics g) {
        positionChildren();
        for (Widget child : children) {
            child.paint(g);
        }
    }

    protected abstract void positionChildren();

    protected abstract void layoutFlexibleWidgets(Dimension maxSize);

    protected abstract void layoutInflexibleWidgets(Dimension maxSize);

    protected MultiChildIterable flexibleChildren() {
        return new MultiChildIterable(children).filter(
                (Widget child) -> child instanceof FlexibleWidget);
    }

    protected MultiChildIterable inflexibleChildren() {
        return new MultiChildIterable(children).filter(
                (Widget child) -> !(child instanceof FlexibleWidget));
    }

    public Interactable hitTest(int id, Dimension hitPos, int clickCount) {
        Interactable hit = null;
        for (int i = children.length - 1; i >= 0; i--) {
            hit = children[i].hitTest(id, hitPos, clickCount);
            if (hit != null)
                return hit;
        }
        return hit;
    }

    public abstract MultiChildWidget setCrossAxisAlignment(Alignment alignment);
}
