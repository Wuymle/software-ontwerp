package clutter.decoratedwidgets;

import java.awt.Color;
import java.awt.Graphics;

import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;

public class Rectangle extends Widget {
    private Dimension rectSize;
    private Color color;

    public Rectangle(Dimension rectSize, Color color) {
        this.rectSize = rectSize;
        this.color = color;
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(color);
        g.fillRect(position.x(), position.y(), rectSize.x(), rectSize.y());
    }

    @Override
    public void measure() {
        preferredSize = rectSize;
    }
}
