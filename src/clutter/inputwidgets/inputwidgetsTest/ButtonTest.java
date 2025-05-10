package clutter.inputwidgets.inputwidgetsTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.concurrent.atomic.AtomicBoolean;

import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.core.Decoration;
import clutter.inputwidgets.Button;
import clutter.inputwidgets.Clickable;
import clutter.ApplicationWindow;
import application.test.TestHelper;

/**
 * Tests for the Button class.
 */
public class ButtonTest {

    private Context context;
    private String buttonText;
    private AtomicBoolean clicked;
    private Runnable onClick;

    @BeforeEach
    public void setUp() {
        // Mock ApplicationWindow for Context
        ApplicationWindow mockAppWindow = null; // For testing purposes only
        context = new Context(mockAppWindow);
        buttonText = "Test Button";
        clicked = new AtomicBoolean(false);
        onClick = () -> clicked.set(true);
    }

    @Test
    public void testButtonCreation() throws Exception {
        Button button = new Button(context, buttonText, onClick);

        assertNotNull(button, "Button should be created");
        assertEquals(buttonText, TestHelper.getPrivateField(button, "text"),
                "Button should have the correct text");
        assertEquals(onClick, TestHelper.getPrivateField(button, "onClick"),
                "Button should have the correct onClick handler");
    }

    @Test
    public void testBuildReturnsClickable() throws Exception {
        Button button = new Button(context, buttonText, onClick);
        Widget builtWidget = button.build();

        assertNotNull(builtWidget, "Built widget should not be null");
        assertTrue(builtWidget instanceof Clickable, "Built widget should be a Clickable");

        // Verify the click handler was passed to the Clickable
        Clickable clickable = (Clickable) builtWidget;
        Runnable clickHandler = (Runnable) TestHelper.getPrivateField(clickable, "onClick");
        assertNotNull(clickHandler, "Click handler should not be null");

        // Test the click handler
        clickHandler.run();
        assertTrue(clicked.get(), "Click handler should set clicked to true when run");
    }

    @Test
    public void testButtonStyling() throws Exception {
        Button button = new Button(context, buttonText, onClick);
        Clickable clickable = (Clickable) button.build();

        // Test alignment using reflection since horizontalAlignment is private
        assertEquals(clutter.layoutwidgets.enums.Alignment.CENTER,
                TestHelper.getPrivateField(clickable, "horizontalAlignment"),
                "Button should be center aligned horizontally");

        // Test decoration
        Decoration decoration = clickable.getDecoration();
        assertNotNull(decoration, "Button should have decoration");
        assertEquals(Color.black, decoration.getBorderColor(), "Button should have black border");

        // The background color should be light gray (211, 211, 211)
        Color bgColor = decoration.getColor();
        assertNotNull(bgColor, "Background color should not be null");
        assertEquals(211, bgColor.getRed(), "Background color red component should be 211");
        assertEquals(211, bgColor.getGreen(), "Background color green component should be 211");
        assertEquals(211, bgColor.getBlue(), "Background color blue component should be 211");
    }
}
