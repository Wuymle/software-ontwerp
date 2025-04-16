package clutter.abstractwidgets;

import static clutter.core.Dimension.contains;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import clutter.core.Dimension;
import clutter.core.Rectangle;

public abstract class MultiChildWidget extends ParentWidget {
    public List<Widget> children = new ArrayList<>();

    /**
     * constructor for the multi child widget
     * 
     * @param children the child widgets
     */
    public MultiChildWidget(Widget... children) {
        for (int i = 0; i < children.length; i++) {
            this.children.add(children[i]);
        }
    }

    /**
     * constructor for the multi child widget
     * 
     * @param children the child widgets
     */
    public MultiChildWidget(List<Widget> children) {
        children.forEach(this.children::add);
    }


    /**
     * paint the widget
     * 
     * @param graphics the graphics object
     */
    public void paintChildren(Graphics g) {
        children.forEach(child -> {
            if (Rectangle.fromAWT(g.getClipBounds())
                    .intersects(new Rectangle(child.position, child.size)))
                child.paint(g);
        });
    }

    /**
     * hit test the widget
     * 
     * @param id the id of the clickEvent
     * @param hitPos the position of the click
     * @param clickCount the number of clicks
     */
    public boolean hitTest(int id, Dimension hitPos, int clickCount) {
        if (!contains(position, size, hitPos))
            return false;
        for (int i = children.size() - 1; i >= 0; i--) {
            if (children.get(i).hitTest(id, hitPos, clickCount))
                return true;
        }
        return false;
    }
}
