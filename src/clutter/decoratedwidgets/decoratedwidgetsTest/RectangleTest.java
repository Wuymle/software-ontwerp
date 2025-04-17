package clutter.decoratedwidgets.decoratedwidgetsTest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.Color;
import clutter.decoratedwidgets.Rectangle;
import clutter.core.Dimension;
import application.test.TestHelper;

/**
 * Tests for the Button class.
 */
public class RectangleTest {

    @Test
    public void testButtonCreation() throws Exception {
        Rectangle rectangle = new Rectangle(new Dimension(10,20), new Color(10, 10, 10, 10));

        assertNotNull(rectangle, "Button should be created");
        assertEquals(new Dimension(10,20), TestHelper.getPrivateField(rectangle, "rectSize"),
                "Rectangle should have the correct size");
        assertEquals(new Color(10, 10, 10, 10), TestHelper.getPrivateField(rectangle, "color"),
                "Rectangle should have the correct color");
    }

    @Test
    public void testMeasure() throws Exception {
        Rectangle rectangle = new Rectangle(new Dimension(10,20), new Color(10, 10, 10, 10));
        rectangle.measure();
        
        // Verify dimensions are not null
        assertEquals(TestHelper.getPrivateField(rectangle, "preferredSize"), new Dimension(10,20),
                "Rectangle dimensions should be set correctly after measure");
    }
}