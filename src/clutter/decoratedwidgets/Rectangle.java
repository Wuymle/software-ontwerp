package clutter.decoratedwidgets;

import java.awt.Color;
import java.awt.Graphics;

import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;

/**
 * A rectangle widget.
 */
public class Rectangle extends Widget {
    private Dimension rectSize;
    private Color color;

    /**
     * Constructor for the rectangle widget.
     * 
     * @param rectSize the size of the rectangle
     * @param color the color of the rectangle
     */
    public Rectangle(Dimension rectSize, Color color) {
        this.rectSize = rectSize;
        this.color = color;
    }

    /**
     * Paint the rectangle.
     * 
     * @param g graphics object
     */
    @Override
    public void runPaint(Graphics g) {
        g.setColor(color);
        g.fillRect(position.x(), position.y(), rectSize.x(), rectSize.y());
    }

    /**
     * 
     * Measure the size of the rectangle.
     */
    @Override
    public void runMeasure() {
        preferredSize = rectSize;
    }
}
