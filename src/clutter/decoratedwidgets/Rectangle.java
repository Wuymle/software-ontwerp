package clutter.decoratedwidgets;

import java.awt.Color;
import java.awt.Graphics;

import clutter.abstractwidgets.Widget;

public class Rectangle extends Widget {
    private int rectWidth;
    private int rectHeight;
    private Color color;

    public Rectangle(int rectWidth, int rectHeight, Color color) {
        this.rectWidth = rectWidth;
        this.rectHeight = rectHeight;
        this.color = color;
    }

    public void paint(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, rectWidth, rectHeight);
    }

    @Override
    public void layout(int maxWidth, int maxHeight) {
        this.width = Math.min(maxWidth, rectWidth);
        this.height = Math.min(maxHeight, rectHeight);
    }
}
