package clutter.core;

public record Dimension(int x, int y) {

    public static Dimension min(Dimension dim1, Dimension dim2) {
        return new Dimension(
                Math.min(dim1.x, dim2.x),
                Math.min(dim1.y, dim2.y));
    }

    public static Dimension max(Dimension dim1, Dimension dim2) {
        return new Dimension(
                Math.max(dim1.x, dim2.x),
                Math.max(dim1.y, dim2.y));
    }

    public int getArea() {
        return x * y;
    }

    public static Dimension add(Dimension dim1, Dimension dim2) {
        return new Dimension(dim1.x + dim2.x, dim1.y + dim2.y);
    }

    public static Dimension subtract(Dimension dim1, Dimension dim2) {
        return new Dimension(dim1.x - dim2.x, dim1.y - dim2.y);
    }

    public static Dimension multiply(Dimension dim, int factor) {
        return new Dimension(dim.x * factor, dim.y * factor);
    }

    public static Dimension divide(Dimension dim, int divisor) {
        return new Dimension(dim.x / divisor, dim.y / divisor);
    }

    public Dimension withX(int x) {
        return new Dimension(x, y);
    }

    public Dimension withY(int y) {
        return new Dimension(x, y);
    }

    public Dimension addX(int x) {
        return new Dimension(this.x + x, y);
    }

    public Dimension addY(int y) {
        return new Dimension(x, this.y+y);
    }

    public static boolean contains(Dimension position, Dimension size, Dimension location) {
        Dimension otherCorner = add(position, size);
        return (position.x <= location.x && location.x <= otherCorner.x && position.y <= location.y
                && location.y <= otherCorner.y);
    }
}
