package clutter.decoratedwidgets;

import static clutter.core.Dimension.min;

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

    public void paint(Graphics g) {
        g.setColor(color);
        g.fillRect(position.x(), position.y(), rectSize.x(), rectSize.y());
    }

    @Override
    public void layout(Dimension maxSize) {
        size = min(maxSize, rectSize);
    }
}
