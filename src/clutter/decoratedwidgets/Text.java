package clutter.decoratedwidgets;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;
import static clutter.core.Dimension.min;

public class Text extends Widget {
    String text;

    public Text(String text) {
        this.text = text;
    }

    @Override
    public void layout(Dimension maxSize) {
        Dimension textSize = getTextDimensions();
        size = min(maxSize, textSize);
    }

    private Dimension getTextDimensions() {
        BufferedImage img = new BufferedImage(0, 0, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        return new Dimension(metrics.stringWidth(text), metrics.getHeight());
    }

    @Override
    public void paint(Graphics g) {
        g.drawString(text, position.x(), position.y());
    }

}
