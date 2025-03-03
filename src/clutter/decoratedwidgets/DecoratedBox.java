package clutter.decoratedwidgets;

import java.awt.Color;
import java.awt.Graphics;

import clutter.abstractwidgets.FlexibleWidget;
import clutter.abstractwidgets.Widget;

public class DecoratedBox extends FlexibleWidget {
    Color color;
    int BorderWidth;
    Color borderColor;

    public DecoratedBox(Widget child) {
        super(child, 1);
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
        g.setColor(color);
        g.fillRect(position.x(), position.y(), size.x(), size.y());
        g.setColor(borderColor);
        g.drawRect(position.x(), position.y(), size.x(), size.y());
        super.paint(g);
    }
}
