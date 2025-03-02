package ui.abstractwidgets;

import java.awt.Graphics;

public abstract class ContainerWidget extends Widget {
    public Widget[] children;

    public ContainerWidget(Widget... children) {
        this.children = children;
    }

    public void paint(Graphics g) {
        positionChildren();
        for (Widget child : children) {
            child.paint(g);
        }
    }

    protected abstract void positionChildren();
    
}
