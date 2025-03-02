package ui.decoratedwidgets;

import java.awt.Graphics;

import ui.Widget;

public class Rectangle extends Widget {
    private int rectWidth;
    private int rectHeight;

    public Rectangle(int rectWidth, int rectHeight) {
        this.rectWidth = rectWidth;
        this.rectHeight = rectHeight;
    }

    public void paint(Graphics g) {
        g.drawRect(x, y, rectWidth, rectHeight);
    }

    @Override
    public void layout(int maxWidth, int maxHeight) {
        this.width = Math.min(width, rectWidth);
        this.height = Math.min(height, rectHeight);
    }
}
