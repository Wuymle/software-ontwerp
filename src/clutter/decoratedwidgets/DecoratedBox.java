package clutter.decoratedwidgets;

import java.awt.Color;
import java.awt.Graphics;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;

public class DecoratedBox extends SingleChildWidget {
    Color color;
    int BorderWidth;
    Color borderColor;

    public DecoratedBox(Widget child) {
        super(child);
    }

    public DecoratedBox setColor(Color color) {
        this.color = color;
        return this;
    }

    public DecoratedBox setBorderWidth(int borderWidth) {
        this.BorderWidth = borderWidth;
        return this;
    }

    public DecoratedBox setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    @Override
    public void paint(Graphics g) {
        if (color != null) {
            g.setColor(color);
            g.fillRect(position.x(), position.y(), size.x(), size.y());
        }
        if (borderColor != null) {
            g.setColor(borderColor);
            g.drawRect(position.x(), position.y(), size.x(), size.y());
        }
        super.paint(g);
    }
}
