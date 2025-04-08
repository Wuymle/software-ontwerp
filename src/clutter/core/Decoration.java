package clutter.core;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.AlphaComposite;
import java.awt.Shape;

public class Decoration {
    private Color color;
    private int borderWidth = 1;
    private Color borderColor;
    private int borderRadius;
    private float fillAlpha = 1.0f;
    private Shape originalClip;

    public Color getColor() {
        return color;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public int getBorderRadius() {
        return borderRadius;
    }

    public float getFillAlpha() {
        return fillAlpha;
    }

    public Decoration setColor(Color color) {
        this.color = color;
        return this;
    }

    public Decoration setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        return this;
    }

    public Decoration setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public Decoration setBorderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
        return this;
    }

    public Decoration setFillAlpha(float fillAlpha) {
        this.fillAlpha = fillAlpha;
        return this;
    }

    public void paint(Graphics g, Dimension position, Dimension size) {
        Graphics2D g2d = (Graphics2D) g;
        originalClip = g2d.getClip();
        if (getBorderRadius() > 0) {
            g2d.setClip(new RoundRectangle2D.Float(
                    position.x(), position.y(), size.x(), size.y(),
                    getBorderRadius(), getBorderRadius()));
        }

        if (getColor() != null) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getFillAlpha()));
            g2d.setColor(getColor());
            g2d.fillRect(position.x(), position.y(), size.x(), size.y());
        }
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    public void afterPaint(Graphics g, Dimension position, Dimension size) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setClip(originalClip);
        if (getBorderColor() != null) {
            g2d.setColor(getBorderColor());
            g2d.setStroke(new BasicStroke(getBorderWidth()));
            if (getBorderRadius() > 0) {
                g2d.draw(new RoundRectangle2D.Float(
                        position.x(), position.y(), size.x()-1, size.y()-1,
                        getBorderRadius(), getBorderRadius()));
            } else {
                g2d.drawRect(position.x(), position.y(), size.x()-1, size.y()-1);
            }
        }
    }

}
