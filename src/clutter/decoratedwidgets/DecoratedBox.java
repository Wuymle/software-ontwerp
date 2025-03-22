package clutter.decoratedwidgets;

import java.awt.Color;
import java.awt.Graphics;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;

/**
 * 
 * A widget that decorates its child widget.
 */
public class DecoratedBox extends SingleChildWidget {
    Color color;
    int BorderWidth;
    Color borderColor;

    /**
     * constructor for the decorated box widget
     * @param child the child widget
     */
    public DecoratedBox(Widget child) {
        super(child);
    }

    /**
     * set the color
     * @param color the color
     * @return self
     */
    public DecoratedBox setColor(Color color) {
        this.color = color;
        return this;
    }

    /**
     * set the border width
     * @param borderWidth the border width
     * @return self
     */
    public DecoratedBox setBorderWidth(int borderWidth) {
        this.BorderWidth = borderWidth;
        return this;
    }

    /**
     * set the border color
     * @param borderColor the border color
     * @return self
     */
    public DecoratedBox setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    /**
     * paint the widget
     * @param g the graphics object
     */
    @Override
    public void paint(Graphics g) {
        if (color != null) {
            g.setColor(color);
            g.fillRect(position.x(), position.y(), size.x(), size.y());
        }
        if (borderColor != null) {
            g.setColor(borderColor);
            // g.drawRect(position.x(), position.y(), size.x(), size.y());
            g.drawRoundRect(position.x(), position.y(), size.x(), size.y(), 5, 5);
        }
        super.paint(g);
    }
}
