package clutter.decoratedwidgets;

import java.awt.Color;
import java.awt.Graphics;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;

/**
 * A widget that decorates its child widget.
 */
public class DecoratedBox extends SingleChildWidget {
    Color color;
    int BorderWidth;
    Color borderColor;

    /**
     * @param child the child widget
     */
    public DecoratedBox(Widget child) {
        super(child);
    }

    /**
     * @param color the color
     * @return self
     */
    public DecoratedBox setColor(Color color) {
        this.color = color;
        return this;
    }

    /**
     * @param borderWidth the border width
     * @return self
     */
    public DecoratedBox setBorderWidth(int borderWidth) {
        this.BorderWidth = borderWidth;
        return this;
    }

    /**
     * @param borderColor the border color
     * @return self
     */
    public DecoratedBox setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    /**
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
