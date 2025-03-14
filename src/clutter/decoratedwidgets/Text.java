package clutter.decoratedwidgets;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import clutter.abstractwidgets.Widget;
import clutter.core.Debug;
import clutter.core.Dimension;

/**
 * A text widget.
 */
public class Text extends Widget {
    String text;
    Color color = Color.black;
    Font font = new Font("Arial", Font.PLAIN, 24);
    FontMetrics metrics;

    /**
     * Constructor for the text widget.dd
     * @param text the text
     */
    public Text(String text) {
        this.text = text;
        setFontMetrics();
    }

    /**
     * measure the size of the text
     * @return dimensions of the text
     */
    @Override
    public void measure() {
        preferredSize = getTextDimensions();
    }

    /**
     * paint the text
     * @param g the graphics object
     */
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

    /**
     * set the text color
     * @param color the color
     */
    public Text setColor(Color color) {
        this.color = color;
        return this;
    }

    /**
     * set the font name
     * @param fontName the font name
     */
    public Text setFont(String fontName) {
        this.font = new Font(fontName, font.getStyle(), font.getSize());
        setFontMetrics();
        return this;
    }

    /**
     * set the font style
     * @param font the font style
     * @return self
     */
    public Text setFont(Font font) {
        this.font = font;
        setFontMetrics();
        return this;
    }

    /**
     * set the font size
     * @param fontSize the font size
     * @return self
     */
    public Text setFontSize(float fontSize) {
        font = font.deriveFont(fontSize);
        setFontMetrics();
        return this;
    }

    /**
     * 
     * set the font metrics
     */
    private void setFontMetrics() {
        metrics = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)
                .getGraphics()
                .getFontMetrics(font);
    }

    /**
     * get the dimensions of the text
     * @return the dimensions of the text
     */
    protected Dimension getTextDimensions() {
        return new Dimension(metrics.stringWidth(text), metrics.getAscent() + metrics.getDescent());
    }
}
