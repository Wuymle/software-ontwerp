package clutter.decoratedwidgets.decoratedwidgetsTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicBoolean;

import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.core.Decoration;
import clutter.core.Dimension;
import clutter.decoratedwidgets.Text;
import clutter.ApplicationWindow;
import application.test.TestHelper;

/**
 * Tests for the Button class.
 */
public class TextTest {

    private Context context;
    private String text;
    private AtomicBoolean clicked;
    private Runnable onClick;

    @BeforeEach
    public void setUp() {
        // Mock ApplicationWindow for Context
        ApplicationWindow mockAppWindow = null; // For testing purposes only
        context = new Context(mockAppWindow);
        text = "Test Button";
    }

    @Test
    public void testButtonCreation() throws Exception {
        Text textObject = new Text(text);

        assertNotNull(textObject, "Button should be created");
        assertEquals(text, TestHelper.getPrivateField(textObject, "text"),
                "Text object should have the correct text");
    }

    @Test
    public void testSetColor() throws Exception {
        Text textObject = new Text(text);
        Color testColor = Color.RED;
        textObject.setColor(testColor);
        
        assertEquals(testColor, TestHelper.getPrivateField(textObject, "color"),
                "Text color should be set correctly");
    }
    
    @Test
    public void testSetFont() throws Exception {
        Text textObject = new Text(text);
        String newFontName = "Courier New";
        textObject.setFont(newFontName);
        
        assertEquals(newFontName, ((java.awt.Font)TestHelper.getPrivateField(textObject, "font")).getFamily(),
                "Font family should be set correctly");
    }
    
    @Test
    public void testSetFontSize() throws Exception {
        Text textObject = new Text(text);
        float newSize = 36.0f;
        textObject.setFontSize(newSize);
        
        assertEquals(newSize, ((java.awt.Font)TestHelper.getPrivateField(textObject, "font")).getSize2D(), 0.001,
                "Font size should be set correctly");
    }
    
    @Test
    public void testMeasure() throws Exception {
        Text textObject = new Text(text);
        textObject.measure();
        
        assertNotNull(TestHelper.getPrivateField(textObject, "preferredSize"),
                "Preferred size should be set after measuring");
    }
    
    @Test
    public void testLayout() throws Exception {
        Text textObject = new Text(text);
        Dimension minSize = new Dimension(100, 30);
        Dimension maxSize = new Dimension(200, 50);
        
        textObject.layout(minSize, maxSize);
        
        assertNotNull(TestHelper.getPrivateField(textObject, "size"),
                "Size should be set after layout");
    }
}