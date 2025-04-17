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
    void testPaintWithoutRadius() {
        // Setup test conditions
        Color fillColor = Color.BLUE;
        decoration.setColor(fillColor);
        Dimension position = new Dimension(10, 10);
        Dimension size = new Dimension(30, 30);
        
        // Store original clip for verification
        Shape originalClip = graphics.getClip();
        
        // Call paint method
        decoration.paint(graphics, position, size);
        
        // Verify clip is preserved when border radius is 0
        assertEquals(originalClip, graphics.getClip());
        
        // Call afterPaint to complete the decoration process
        decoration.afterPaint(graphics, position, size);
    }
    
    @Test
    void testPaintWithRadius() {
        // Setup test conditions
        Color fillColor = Color.BLUE;
        Color borderColor = Color.RED;
        int borderRadius = 5;
        decoration.setColor(fillColor)
                 .setBorderColor(borderColor)
                 .setBorderRadius(borderRadius);
        Dimension position = new Dimension(10, 10);
        Dimension size = new Dimension(30, 30);
        
        // Store original clip for verification
        Shape originalClip = graphics.getClip();
        
        // Call paint method
        decoration.paint(graphics, position, size);
        
        // The clip should have changed to a RoundRectangle2D
        assertNotEquals(originalClip, graphics.getClip());
        
        // Call afterPaint to complete the decoration process
        decoration.afterPaint(graphics, position, size);
        
        // Verify the clip is restored after afterPaint
        assertEquals(originalClip, graphics.getClip());
    }
    
    @Test
    void testPaintWithFillFront() {
        // Setup test conditions
        Color fillColor = Color.BLUE;
        decoration.setColor(fillColor).fillFront();
        Dimension position = new Dimension(10, 10);
        Dimension size = new Dimension(30, 30);
        
        // Call paint method
        decoration.paint(graphics, position, size);
        
        // Call afterPaint where the filling should happen for fillFront
        decoration.afterPaint(graphics, position, size);
    }
    
    @Test
    void testPaintWithBorderOnly() {
        // Setup test conditions
        Color borderColor = Color.RED;
        decoration.setBorderColor(borderColor);
        Dimension position = new Dimension(10, 10);
        Dimension size = new Dimension(30, 30);
        
        // Call paint method
        decoration.paint(graphics, position, size);
        
        // Call afterPaint to draw the border
        decoration.afterPaint(graphics, position, size);
    }
    
    @Test
    void testPaintWithoutColor() {
        // Setup test conditions with null color
        decoration.setColor(null);
        Dimension position = new Dimension(10, 10);
        Dimension size = new Dimension(30, 30);
        
        // Call paint method
        decoration.paint(graphics, position, size);
        
        // Call afterPaint
        decoration.afterPaint(graphics, position, size);
    }
    
    @Test
    void testCompleteDecoration() {
        // Setup a complete decoration with all properties set
        Color fillColor = Color.BLUE;
        Color borderColor = Color.RED;
        int borderWidth = 2;
        int borderRadius = 8;
        float alpha = 0.75f;
        
        decoration.setColor(fillColor)
                 .setBorderColor(borderColor)
                 .setBorderWidth(borderWidth)
                 .setBorderRadius(borderRadius)
                 .setFillAlpha(alpha);
        
        Dimension position = new Dimension(10, 10);
        Dimension size = new Dimension(30, 30);
        
        // Call the paint methods
        decoration.paint(graphics, position, size);
        decoration.afterPaint(graphics, position, size);
    }
}

