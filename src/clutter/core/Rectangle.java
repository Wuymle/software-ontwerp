package clutter.core;

public record Rectangle(Dimension position, Dimension size) {
    public Rectangle(Dimension position, Dimension size) {
        this.position = position;
        this.size = size;
    }

    public boolean intersects(Rectangle other) {
        return position.x() < other.position.x() + other.size.x()
                && position.x() + size.x() > other.position.x()
                && position.y() < other.position.y() + other.size.y()
                && position.y() + size.y() > other.position.y();
    }

    public static Rectangle fromAWT(java.awt.Rectangle rectangle) {
        return new Rectangle(new Dimension(rectangle.x, rectangle.y),
                new Dimension(rectangle.width, rectangle.height));
    }
}
