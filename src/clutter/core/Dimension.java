package clutter.core;

/**
 * A class representing a dimension.
 */
public record Dimension(int x, int y) {

    /**
     * constructor for the dimension
     * @param x the x dimension
     * @param y the y dimension
     */
    public static Dimension min(Dimension dim1, Dimension dim2) {
        return new Dimension(
                Math.min(dim1.x, dim2.x),
                Math.min(dim1.y, dim2.y));
    }

    /**
     * get the maximum of two dimensions
     * @param x the x dimension
     * @param y the y dimension
     */
    public static Dimension max(Dimension dim1, Dimension dim2) {
        return new Dimension(
                Math.max(dim1.x, dim2.x),
                Math.max(dim1.y, dim2.y));
    }

    /**
     * get the area of the dimension
     * @return the area of the dimension
     */
    public int getArea() {
        return x * y;
    }

    /**
     * get the aspect ratio of the dimension
     * @return the aspect ratio of the dimension
     */
    public static Dimension add(Dimension dim1, Dimension dim2) {
        return new Dimension(dim1.x + dim2.x, dim1.y + dim2.y);
    }

    /**
     * subtract the second dimension from the first
     * @return subtracts the second dimension from the first
     */
    public static Dimension subtract(Dimension dim1, Dimension dim2) {
        if (dim1.x < dim2.x || dim1.y < dim2.y)
            throw new IllegalArgumentException("Cannot subtract a larger dimension from a smaller one");
        return new Dimension(dim1.x - dim2.x, dim1.y - dim2.y);
    }

    /**
     * multiply the dimension by a factor
     * @return the dimension multiplied by a factor
     */
    public static Dimension multiply(Dimension dim, int factor) {
        return new Dimension(dim.x * factor, dim.y * factor);
    }

    /**
     * divide the dimension by a divisor
     * @return the dimension divided by a divisor
     */
    public static Dimension divide(Dimension dim, int divisor) {
        return new Dimension(dim.x / divisor, dim.y / divisor);
    }

    /**
     * return a new dimension with the x value set
     * @param x the x value
     * @return a dimentions with the x value set
     */
    public Dimension withX(int x) {
        return new Dimension(x, y);
    }

    /**
     * return a new dimension with the y value set
     * @param y the y value
     * @return a dimentions with the y value set
     */
    public Dimension withY(int y) {
        return new Dimension(x, y);
    }

    /**
     * add the x value to the dimension
     * @param x the x value
     * @return the dimention with the x value added
     */
    public Dimension addX(int x) {
        return new Dimension(this.x + x, y);
    }

    /**
     * add the y value to the dimension
     * @param y the y value
     * @return the dimention with the y value added
     */
    public Dimension addY(int y) {
        return new Dimension(x, this.y + y);
    }

    /**
     * return wheter the location is contained in the dimension
     * @param position the position
     * @param size     the size
     * @param location the location
     */
    public static boolean contains(Dimension position, Dimension size, Dimension location) {
        Dimension otherCorner = add(position, size);
        return (position.x <= location.x && location.x <= otherCorner.x && position.y <= location.y
                && location.y <= otherCorner.y);
    }
}
