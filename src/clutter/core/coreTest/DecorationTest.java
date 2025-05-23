package clutter.core.coreTest;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.image.BufferedImage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clutter.core.Decoration;
import clutter.core.Dimension;

class DecorationTest {

    private Decoration decoration;
    private Graphics2D graphics;
    private BufferedImage image;

    @BeforeEach
    void setUp() {
        decoration = new Decoration();
        // Create a small image and graphics object for testing
        image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        graphics = image.createGraphics();
    }

    @Test
    void testDefaultValues() {
        // Test default values
        assertNull(decoration.getColor());
        assertEquals(1, decoration.getBorderWidth());
        assertNull(decoration.getBorderColor());
        assertEquals(0, decoration.getBorderRadius());
        assertEquals(1.0f, decoration.getFillAlpha());
    }

    @Test
    void testSetColor() {
        Color testColor = Color.RED;
        Decoration result = decoration.setColor(testColor);

        assertEquals(testColor, decoration.getColor());
        // Verify fluent interface returns the same instance
        assertSame(decoration, result);
    }

    @Test
    void testSetBorderWidth() {
        int testWidth = 5;
        Decoration result = decoration.setBorderWidth(testWidth);

        assertEquals(testWidth, decoration.getBorderWidth());
        // Verify fluent interface returns the same instance
        assertSame(decoration, result);
    }

    @Test
    void testSetBorderColor() {
        Color testColor = Color.GREEN;
        Decoration result = decoration.setBorderColor(testColor);

        assertEquals(testColor, decoration.getBorderColor());
        // Verify fluent interface returns the same instance
        assertSame(decoration, result);
    }

    @Test
    void testSetBorderRadius() {
        int testRadius = 10;
        Decoration result = decoration.setBorderRadius(testRadius);

        assertEquals(testRadius, decoration.getBorderRadius());
        // Verify fluent interface returns the same instance
        assertSame(decoration, result);
    }

    @Test
    void testSetFillAlpha() {
        float testAlpha = 0.5f;
        Decoration result = decoration.setFillAlpha(testAlpha);

        assertEquals(testAlpha, decoration.getFillAlpha());
        // Verify fluent interface returns the same instance
        assertSame(decoration, result);
    }

    @Test
    void testFillFront() {
        Decoration result = decoration.fillFront();

        // Verify fluent interface returns the same instance
        assertSame(decoration, result);
    }

    @Test
    void testAfterPaintWithFillFront() {
        // Setup test conditions
        Color fillColor = Color.BLUE;
        decoration.setColor(fillColor).fillFront();
        Dimension position = new Dimension(10, 10);
        Dimension size = new Dimension(30, 30);

        // Set a specific clip for testing
        graphics.setClip(0, 0, 100, 100);
        Shape expectedClip = graphics.getClip();

        // Call beforePaint to setup
        decoration.beforePaint(graphics, position, size);

        // Call afterPaint where the filling should happen for fillFront
        decoration.afterPaint(graphics, position, size);

        // Verify clip is restored
        assertEquals(expectedClip, graphics.getClip());
    }

    @Test
    void testChainedDecorationSettings() {
        // Test the fluent interface with chained method calls
        Color fillColor = Color.YELLOW;
        Color borderColor = Color.BLACK;
        int borderWidth = 3;
        int borderRadius = 12;
        float alpha = 0.6f;

        decoration.setColor(fillColor).setBorderColor(borderColor).setBorderWidth(borderWidth)
                .setBorderRadius(borderRadius).setFillAlpha(alpha).fillFront();

        assertEquals(fillColor, decoration.getColor());
        assertEquals(borderColor, decoration.getBorderColor());
        assertEquals(borderWidth, decoration.getBorderWidth());
        assertEquals(borderRadius, decoration.getBorderRadius());
        assertEquals(alpha, decoration.getFillAlpha());
    }
}

