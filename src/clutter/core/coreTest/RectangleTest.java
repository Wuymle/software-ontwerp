package clutter.core.coreTest;
import clutter.core.Rectangle;
import clutter.core.Dimension;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class RectangleTest {
    
    @Test
    public void testIntersects() {
        Rectangle rect1 = new Rectangle(new Dimension(0, 0), new Dimension(10, 10));
        Rectangle rect2 = new Rectangle(new Dimension(5, 5), new Dimension(10, 10));
        Rectangle rect3 = new Rectangle(new Dimension(15, 15), new Dimension(10, 10));

        assertTrue(rect1.intersects(rect2));
        assertFalse(rect1.intersects(rect3));
    }

    @Test
    public void testFromAWT() {
        java.awt.Rectangle awtRect = new java.awt.Rectangle(0, 0, 10, 10);
        assertDoesNotThrow(() -> Rectangle.fromAWT(awtRect));
    }
}
