package clutter.decoratedwidgets;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import clutter.abstractwidgets.Widget;
import clutter.core.Debug;
import clutter.core.Dimension;

public class Text extends Widget {
    String text;
    Color color = Color.black;
    Font font = new Font("Arial", Font.PLAIN, 24);
    FontMetrics metrics;

    public Text(String text) {
        this.text = text;
        setFontMetrics();
    }

    @Override
    public void measure() {
        preferredSize = getTextDimensions();
    }

    @Override
    public void paint(Graphics g) {
        if (debug) {
            g.setColor(Color.yellow);
            g.fillRect(position.x(), position.y(), size.x(), size.y());
        }
        Debug.log(this, "painting location: " + position);
        g.setColor(color);
        g.setFont(font);
        g.drawString(text, position.x(), position.y() + size.y() - metrics.getDescent());
    }

    public Text setColor(Color color) {
        this.color = color;
        return this;
    }

    public Text setFont(String fontName) {
        this.font = new Font(fontName, font.getStyle(), font.getSize());
        setFontMetrics();
        return this;
    }

    public Text setFont(Font font) {
        this.font = font;
        setFontMetrics();
        return this;
    }

    public Text setFontSize(float fontSize) {
        font = font.deriveFont(fontSize);
        setFontMetrics();
        return this;
    }

    private void setFontMetrics() {
        metrics = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)
                .getGraphics()
                .getFontMetrics(font);
    }

    protected Dimension getTextDimensions() {
        return new Dimension(metrics.stringWidth(text), metrics.getAscent() + metrics.getDescent());
    }
}
