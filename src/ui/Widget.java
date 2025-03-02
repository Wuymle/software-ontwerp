package ui;

import java.awt.Graphics;

public abstract class Widget {
    protected int x, y, width, height;

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public abstract void layout(int maxWidth, int maxHeight);

    public abstract void paint(Graphics g);
}
