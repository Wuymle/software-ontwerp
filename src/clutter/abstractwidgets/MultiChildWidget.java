package clutter.abstractwidgets;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.List;

import clutter.core.Debug;
import clutter.core.Dimension;
import clutter.layoutwidgets.enums.Alignment;
import clutter.widgetinterfaces.Interactable;

public abstract class MultiChildWidget extends ChildWidget {
    protected Alignment crossAxisAlignment = Alignment.START;
    public Widget[] children;

    public MultiChildWidget(Widget... children) {
        this.children = children;
    }

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

    protected abstract void positionChildren();

    protected abstract void layoutFlexibleWidgets(Dimension minSize, Dimension maxSize);

    protected abstract void layoutInflexibleWidgets(Dimension minSize, Dimension maxSize);

    protected List<FlexibleWidget> flexibleChildren() {
        List<FlexibleWidget> flexibles = new java.util.ArrayList<>();
        for (Widget child : children) {
            if (child instanceof FlexibleWidget) {
                flexibles.add((FlexibleWidget) child);
            }
        }
        return flexibles;
    }

    protected List<Widget> inflexibleChildren() {
        List<Widget> inflexibles = new java.util.ArrayList<>();
        for (Widget child : children) {
            if (!(child instanceof FlexibleWidget)) {
                inflexibles.add(child);
            }
        }
        return inflexibles;
    }

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

    public abstract MultiChildWidget setCrossAxisAlignment(Alignment alignment);
}
