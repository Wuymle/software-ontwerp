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
    void testBeforePaintWithoutRadius() {
        // Setup test conditions
        Color fillColor = Color.BLUE;
        decoration.setColor(fillColor);
        Dimension position = new Dimension(10, 10);
        Dimension size = new Dimension(30, 30);
        
        // Store original clip for verification
        Shape originalClip = graphics.getClip();
        
        // Call beforePaint method directly
        decoration.beforePaint(graphics, position, size);
        
        // Verify clip is preserved when border radius is 0
        assertEquals(originalClip, graphics.getClip());
        
        // Clean up
        decoration.afterPaint(graphics, position, size);
    }

    @Test
    void testBeforePaintWithRadius() {
        // Setup test conditions
        Color fillColor = Color.BLUE;
        int borderRadius = 5;
        decoration.setColor(fillColor)
                  .setBorderRadius(borderRadius);
        Dimension position = new Dimension(10, 10);
        Dimension size = new Dimension(30, 30);
        
        // Store original clip for verification
        Shape originalClip = graphics.getClip();
        
        // Call beforePaint method directly
        decoration.beforePaint(graphics, position, size);
        
        // The clip should have changed to a RoundRectangle2D
        assertNotEquals(originalClip, graphics.getClip());
        
        // Clean up
        decoration.afterPaint(graphics, position, size);
    }

    @Test
    void testAfterPaintWithFillFront() {
        // Setup test conditions
        Color fillColor = Color.BLUE;
        decoration.setColor(fillColor).fillFront();
        Dimension position = new Dimension(10, 10);
        Dimension size = new Dimension(30, 30);
        
        // Store original clip
        Shape originalClip = graphics.getClip();
        
        // Call beforePaint to setup
        decoration.beforePaint(graphics, position, size);
        
        // Call afterPaint where the filling should happen for fillFront
        decoration.afterPaint(graphics, position, size);
        
        // Verify clip is restored
        assertEquals(originalClip, graphics.getClip());
    }

    @Test
    void testChainedDecorationSettings() {
        // Test the fluent interface with chained method calls
        Color fillColor = Color.YELLOW;
        Color borderColor = Color.BLACK;
        int borderWidth = 3;
        int borderRadius = 12;
        float alpha = 0.6f;
        
        decoration.setColor(fillColor)
                  .setBorderColor(borderColor)
                  .setBorderWidth(borderWidth)
                  .setBorderRadius(borderRadius)
                  .setFillAlpha(alpha)
                  .fillFront();
        
        assertEquals(fillColor, decoration.getColor());
        assertEquals(borderColor, decoration.getBorderColor());
        assertEquals(borderWidth, decoration.getBorderWidth());
        assertEquals(borderRadius, decoration.getBorderRadius());
        assertEquals(alpha, decoration.getFillAlpha());
    }

    @Test
    void testBeforeAndAfterPaintSequence() {
        // Setup test conditions
        Color fillColor = Color.CYAN;
        Color borderColor = Color.MAGENTA;
        decoration.setColor(fillColor)
                  .setBorderColor(borderColor)
                  .setBorderWidth(2);
        Dimension position = new Dimension(15, 15);
        Dimension size = new Dimension(40, 40);
        
        // Store original state
        Shape originalClip = graphics.getClip();
        
        // Execute the complete painting sequence
        decoration.beforePaint(graphics, position, size);
        // Simulate component painting here
        decoration.afterPaint(graphics, position, size);
        
        // Verify clip is restored
        assertEquals(originalClip, graphics.getClip());
    }

    @Test
    void testBeforePaintWithNullColor() {
        // Setup test with null color
        decoration.setColor(null);
        Dimension position = new Dimension(10, 10);
        Dimension size = new Dimension(30, 30);
        
        // Should not throw exception
        assertDoesNotThrow(() -> decoration.beforePaint(graphics, position, size));
        
        // Clean up
        decoration.afterPaint(graphics, position, size);
    }
}

