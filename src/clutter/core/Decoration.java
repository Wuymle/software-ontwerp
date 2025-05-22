package clutter.core;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class Decoration {
    private Color color;
    private int borderWidth = 1;
    private Color borderColor;
    private int borderRadius;
    private float fillAlpha = 1.0f;
    private Shape originalClip;
    private boolean inFront = false;

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

    public Decoration fillFront() {
        inFront = true;
        return this;
    }

    public void setClip(Graphics2D g2d, Dimension position, Dimension size) {
        int x = position.x(), y = position.y(), w = size.x(), h = size.y();
        Shape newClip = getBorderRadius() > 0
                ? new RoundRectangle2D.Float(x, y, w, h, getBorderRadius(), getBorderRadius())
                : new Rectangle(x, y, w, h);
        if (originalClip.contains(x, y, w, h)) {
            g2d.setClip(newClip);
        } else if (originalClip instanceof Rectangle2D && newClip instanceof Rectangle2D) {
            Rectangle2D intersect =
                    ((Rectangle2D) originalClip).createIntersection((Rectangle2D) newClip);
            g2d.setClip(intersect);
        } else {
            Area area = new Area(originalClip);
            area.intersect(new Area(newClip));
            g2d.setClip(area);
        }
    }

    public void beforePaint(Graphics g, Dimension position, Dimension size) {
        Graphics2D g2d = (Graphics2D) g;
        originalClip = g2d.getClip();
        setClip(g2d, position, size);
        if (!inFront && getColor() != null) {
            float alpha = getFillAlpha();
            if (alpha < 1.0f)
                g2d.setComposite(
                        AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getFillAlpha()));
            g2d.setColor(getColor());
            g2d.fillRect(position.x(), position.y(), size.x(), size.y());
            if (alpha < 1.0f)
                g2d.setComposite(AlphaComposite.SrcOver);
        }
    }

    public void afterPaint(Graphics g, Dimension position, Dimension size) {
        Graphics2D g2d = (Graphics2D) g;
        if (inFront && getColor() != null) {
            float alpha = getFillAlpha();
            if (alpha < 1.0f)
                g2d.setComposite(
                        AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getFillAlpha()));
            g2d.setColor(getColor());
            g2d.fillRect(position.x(), position.y(), size.x(), size.y());
            if (alpha < 1.0f)
                g2d.setComposite(AlphaComposite.SrcOver);
        }

        g2d.setClip(originalClip);
        if (getBorderColor() != null) {
            g2d.setColor(getBorderColor());
            if (getBorderWidth() > 0)
                g2d.setStroke(new BasicStroke(getBorderWidth()));
            if (getBorderRadius() > 0) {
                g2d.draw(new RoundRectangle2D.Float(position.x(), position.y(), size.x() - 1,
                        size.y() - 1, getBorderRadius(), getBorderRadius()));
            } else {
                g2d.drawRect(position.x(), position.y(), size.x() - 1, size.y() - 1);
            }
            if (getBorderWidth() > 0) {
                g2d.setStroke(new BasicStroke(1));
            }
        }
    }
}
