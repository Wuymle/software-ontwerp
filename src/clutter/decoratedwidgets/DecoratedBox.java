package clutter.decoratedwidgets;

import java.awt.Color;
import java.awt.Graphics;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;

/**
 * 
 * A widget that decorates its child widget.
 */
@Deprecated
public class DecoratedBox extends SingleChildWidget {
    Color color;
    int BorderWidth;
    Color borderColor;
    int borderRadius;

    /**
     * constructor for the decorated box widget
     * 
     * @param child the child widget
     */
    public DecoratedBox(Widget child) {
        super(child);
    }

    /**
     * set the color
     * 
     * @param color the color
     * @return self
     */
    public DecoratedBox setColor(Color color) {
        this.color = color;
        return this;
    }

    /**
     * set the border width
     * 
     * @param borderWidth the border width
     * @return self
     */
    public DecoratedBox setBorderWidth(int borderWidth) {
        this.BorderWidth = borderWidth;
        return this;
    }

    /**
     * set the border color
     * 
     * @param borderColor the border color
     * @return self
     */
    public DecoratedBox setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public DecoratedBox setBorderRadius(int radius) {
        this.borderRadius = radius;
        return this;
    }

    /**
     * paint the widget
     * 
     * @param g the graphics object
     */
    @Override
    public void paint(Graphics g) {
        if (color != null) {
            g.setColor(color);
            if (borderRadius > 0)
                g.fillRoundRect(position.x(), position.y(), size.x(), size.y(), borderRadius, borderRadius);
            else
                g.fillRect(position.x(), position.y(), size.x(), size.y());
        }
        if (borderColor != null && BorderWidth > 0) {
            g.setColor(borderColor);
            for (int i = 0; i < BorderWidth; i++) {
                if (borderRadius > 0)
                    g.drawRoundRect(position.x() + i, position.y() + i, size.x() - 2 * i, size.y() - 2 * i,
                            borderRadius, borderRadius);
                else
                    g.drawRect(position.x() + i, position.y() + i, size.x() - 2 * i, size.y() - 2 * i);
            }
        }
        super.paint(g);
    }
}
