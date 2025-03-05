package clutter.decoratedwidgets;

import static clutter.core.Dimension.min;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;

public class Text extends Widget {
    String text;
    Color color = Color.black;
    Font font = new Font("Arial", Font.PLAIN, 24);

    public Text(String text) {
        this.text = text;
    }

    @Override
    public void layout(Dimension maxSize) {
        Dimension textSize = getTextDimensions();
        size = min(maxSize, textSize);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(color);
        g.setFont(font);
        // System.out.println("Painting text: " + text);
        g.drawString(text, position.x(), position.y() + size.y());
        // g.drawRect(position.x(), position.y(), size.x(), size.y());
    }

    public Text setColor(Color color) {
        this.color = color;
        return this;
    }

    public Text setFont(String fontName) {
        this.font = new Font(fontName, font.getStyle(), font.getSize());
        return this;
    }

    public Text setFont(Font font) {
        this.font = font;
        return this;
    }

    public Text setFontSize(float fontSize) {
        font = font.deriveFont(fontSize);
        return this;
    }

    private Dimension getTextDimensions() {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        FontMetrics metrics = g.getFontMetrics(font);
        return new Dimension(metrics.stringWidth(text), metrics.getAscent());
    }

}
