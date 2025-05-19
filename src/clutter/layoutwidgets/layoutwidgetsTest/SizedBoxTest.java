package clutter.layoutwidgets.layoutwidgetsTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.awt.Graphics;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import clutter.abstractwidgets.LeafWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;
import clutter.layoutwidgets.SizedBox;
import clutter.layoutwidgets.enums.Alignment;

/**
 * Test class for SizedBox widget
 */
class SizedBoxTest {

    private SizedBox sizedBox;
    private Widget mockChild;
    private Dimension boxSize;

    @BeforeEach
    void setUp() {
        // Create a mock child widget using anonymous class
        mockChild = new LeafWidget() {
            @Override
            protected void runMeasure() {
                preferredSize = new Dimension(50, 50);
            }

            @Override
            protected void runPaint(Graphics g) {
                // Empty implementation for testing
            }
        };

        // Create a fixed box size
        boxSize = new Dimension(100, 100);

        // Create the SizedBox with the mock child and box size
        sizedBox = new SizedBox(boxSize, mockChild);
    }

    // Helper method to get a protected field value using reflection
    private <T> T getProtectedField(Object obj, String fieldName) throws Exception {
        Field field = obj.getClass().getSuperclass().getDeclaredField(fieldName);
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        T value = (T) field.get(obj);
        return value;
    }

    @Test
    void testConstructorWithBoxSize() throws Exception {
        // Test constructor with only box size
        SizedBox boxWithoutChild = new SizedBox(boxSize);

        // Measure the widget
        boxWithoutChild.measure();

        // Assert that preferred size is set to boxSize
        assertEquals(boxSize, boxWithoutChild.getPreferredSize());

        // Get the child field using reflection
        Widget child = getProtectedField(boxWithoutChild, "child");
        // Check if it's a NullWidget
        assertTrue(child.getClass().getName().contains("NullWidget"));
    }

    @Test
    void testConstructorWithChildAndBoxSize() throws Exception {
        // Measure the widget
        sizedBox.measure();

        // Assert that preferred size is set to boxSize regardless of child's size
        assertEquals(boxSize, sizedBox.getPreferredSize());

        // Get the child field using reflection
        Widget child = getProtectedField(sizedBox, "child");
        assertNotNull(child);
        assertEquals(mockChild, child);
    }

    @Test
    void testMeasure() {
        // Measure the widget
        sizedBox.measure();

        // Assert that preferred size is set to boxSize
        assertEquals(boxSize, sizedBox.getPreferredSize());

        // Even with a different box size, it should override child's size
        Dimension newBoxSize = new Dimension(200, 200);
        SizedBox newSizedBox = new SizedBox(newBoxSize, mockChild);
        newSizedBox.measure();

        assertEquals(newBoxSize, newSizedBox.getPreferredSize());
    }

    @Test
    void testSetHorizontalAlignment() throws Exception {
        // Test setting alignment to START
        sizedBox.setHorizontalAlignment(Alignment.START);
        Alignment startAlignment = getProtectedField(sizedBox, "horizontalAlignment");
        assertEquals(Alignment.START, startAlignment);

        // Test setting alignment to END
        sizedBox.setHorizontalAlignment(Alignment.END);
        Alignment endAlignment = getProtectedField(sizedBox, "horizontalAlignment");
        assertEquals(Alignment.END, endAlignment);

        // Test method chaining
        SizedBox returnedBox = sizedBox.setHorizontalAlignment(Alignment.CENTER);
        Alignment centerAlignment = getProtectedField(sizedBox, "horizontalAlignment");
        assertEquals(Alignment.CENTER, centerAlignment);
        assertSame(sizedBox, returnedBox);
    }

    @Test
    void testChildPositioning() {
        // Create a layout with different size than the box
        Dimension layoutSize = new Dimension(200, 200);

        // Set an initial position for the sizedBox
        Dimension initialPosition = new Dimension(10, 10);
        sizedBox.setPosition(initialPosition);

        // Measure the child and sizedBox
        mockChild.measure();
        sizedBox.measure();

        // Layout the sizedBox
        sizedBox.layout(new Dimension(0, 0), layoutSize);

        // Check that sizedBox size is set to boxSize
        assertEquals(boxSize, sizedBox.getSize());

        // Explicitly call positionChildren to ensure child positioning
        try {
            // Use reflection to call the protected positionChildren method
            java.lang.reflect.Method positionMethod =
                    sizedBox.getClass().getSuperclass().getDeclaredMethod("positionChildren");
            positionMethod.setAccessible(true);
            positionMethod.invoke(sizedBox);
        } catch (Exception e) {
            fail("Failed to call positionChildren: " + e.getMessage());
        }

        // With default alignment, child should have a position
        assertNotNull(mockChild.getPosition());
    }

    @Test
    void testConstructorWithParameterOrder() {
        try {
            // This should work because we're already using this constructor
            Dimension testSize = new Dimension(150, 150);
            SizedBox testBox = new SizedBox(testSize, mockChild);
            testBox.measure();

            // Assert the preferred size is correctly set
            assertEquals(testSize, testBox.getPreferredSize());
        } catch (Exception e) {
            fail("Constructor with (Dimension, Widget) parameters failed: " + e.getMessage());
        }
    }
}
