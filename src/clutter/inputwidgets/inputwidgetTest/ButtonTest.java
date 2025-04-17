package clutter.inputwidgets.inputwidgetTest;

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
    public void testHitTestTriggersOnClick() throws Exception {
        Button button = new Button(context, buttonText, onClick);
        Clickable clickable = (Clickable) button.build();

        // Position the clickable for testing
        Dimension testPosition = new Dimension(0, 0);
        Dimension testSize = new Dimension(100, 50);

        clickable.setPosition(testPosition);
        clickable.setSize(testSize);

        // Perform complete layout cycle
        clickable.measure();
        clickable.layout(testPosition, testSize);
        clickable.positionChildren();

        // Ensure proper debugging - print position and size
        System.out.println("Clickable position: " + clickable.getPosition());
        System.out.println("Clickable size: " + clickable.getSize());

        // Reset clicked state
        clicked.set(false);

        // Ensure we're using the right click count
        int expectedClickCount = (int) TestHelper.getPrivateField(clickable, "clickCount");

        // Test with a position that should definitely be inside the widget
        Dimension clickPos = new Dimension(testPosition.x() + 5, testPosition.y() + 5);

        // Use MouseEvent.MOUSE_CLICKED which is what the Clickable expects
        boolean hitResult =
                clickable.hitTest(MouseEvent.MOUSE_CLICKED, clickPos, expectedClickCount);

        assertTrue(hitResult, "Hit test should return true for valid click at " + clickPos);
        assertTrue(clicked.get(), "onClick should be triggered by hitTest");
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
