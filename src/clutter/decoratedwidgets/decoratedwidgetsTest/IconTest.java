package clutter.decoratedwidgets.decoratedwidgetsTest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import clutter.decoratedwidgets.Icon;
import application.test.TestHelper;

/**
 * Tests for the Button class.
 */
public class IconTest {

    @Test
    public void testButtonCreation() throws Exception {
        Icon textObject = new Icon("text");

        assertNotNull(textObject, "Button should be created");
        assertEquals("text", TestHelper.getPrivateField(textObject, "text"),
                "Text object should have the correct text");
    }

    @Test
    public void testGetTextDimensions() throws Exception {
        // Create an Icon instance with a known text
        Icon icon = new Icon("test-icon");
        
        // Call the measure method that will internally use getTextDimensions
        icon.measure();
        
        // Verify dimensions are not null
        assertNotNull(TestHelper.getPrivateField(icon, "preferredSize"), "Icon dimensions should not be null");
    }
}