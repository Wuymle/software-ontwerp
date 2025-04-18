package clutter.inputwidgets.inputwidgetsTest;

import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.decoratedwidgets.Icon;
import clutter.inputwidgets.Clickable;
import clutter.inputwidgets.IconButton;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Font;
import java.util.concurrent.atomic.AtomicBoolean;
import application.test.TestHelper;

/**
 * Test class for IconButton
 */
public class IconButtonTest {
    private Context mockContext;
    private AtomicBoolean clickHandlerCalled;
    private Runnable mockClickHandler;
    private String testIconName;

    /**
     * Setup for tests
     */
    @BeforeEach
    public void setUp() {
        // Create a mock context
        mockContext = TestHelper.context;

        // Create a testable click handler
        clickHandlerCalled = new AtomicBoolean(false);
        mockClickHandler = () -> clickHandlerCalled.set(true);

        // Test icon name
        testIconName = "fi-bs-heart"; // Using an icon name from the available icons
    }

    /**
     * Test the constructor properly sets the fields
     */
    @Test
    public void testConstructor() {
        IconButton iconButton = new IconButton(mockContext, testIconName, mockClickHandler);

        // We need to use reflection to access private fields
        try {
            java.lang.reflect.Field iconField = IconButton.class.getDeclaredField("icon");
            iconField.setAccessible(true);
            assertEquals(testIconName, iconField.get(iconButton), "Icon name should match");

            java.lang.reflect.Field onClickField = IconButton.class.getDeclaredField("onClick");
            onClickField.setAccessible(true);
            assertEquals(mockClickHandler, onClickField.get(iconButton),
                    "Click handler should match");
        } catch (Exception e) {
            fail("Failed to access fields: " + e.getMessage());
        }
    }

    /**
     * Test the build method returns a Clickable with the proper icon
     */
    @Test
    public void testBuild() {
        IconButton iconButton = new IconButton(mockContext, testIconName, mockClickHandler);
        Widget builtWidget = iconButton.build();

        // Verify the built widget is a Clickable
        assertTrue(builtWidget instanceof Clickable, "Built widget should be a Clickable");

        Clickable clickable = (Clickable) builtWidget;

        // Verify the Clickable's child is an Icon with the correct icon name
        try {
            // Access the child field of the Clickable to check the Icon
            java.lang.reflect.Field childField =
                    clickable.getClass().getSuperclass().getDeclaredField("child");
            childField.setAccessible(true);
            Object child = childField.get(clickable);

            assertTrue(child instanceof Icon, "Clickable child should be an Icon");

            // Check the icon name
            Icon icon = (Icon) child;
            java.lang.reflect.Field textField =
                    icon.getClass().getSuperclass().getDeclaredField("text");
            textField.setAccessible(true);
            assertEquals(testIconName, textField.get(icon), "Icon name should match");

            // Check the font size
            java.lang.reflect.Field fontField =
                    icon.getClass().getSuperclass().getDeclaredField("font");
            fontField.setAccessible(true);
            Font font = (Font) fontField.get(icon);
            assertEquals(16f, font.getSize2D(), "Font size should be 16f");

        } catch (Exception e) {
            fail("Failed to access fields: " + e.getMessage());
        }
    }

    /**
     * Test the click handler is invoked when the button is clicked
     */
    @Test
    public void testOnClickHandler() {
        IconButton iconButton = new IconButton(mockContext, testIconName, mockClickHandler);
        Widget builtWidget = iconButton.build();

        assertTrue(builtWidget instanceof Clickable, "Built widget should be a Clickable");
        Clickable clickable = (Clickable) builtWidget;

        // Get the onClick handler from Clickable and invoke it
        try {
            java.lang.reflect.Field onClickField = Clickable.class.getDeclaredField("onClick");
            onClickField.setAccessible(true);
            Runnable onClick = (Runnable) onClickField.get(clickable);

            // Ensure our flag is initially false
            assertFalse(clickHandlerCalled.get(), "Click handler should not be called initially");

            // Invoke the handler
            onClick.run();

            // Verify our flag was set
            assertTrue(clickHandlerCalled.get(), "Click handler should be called after onclick");

        } catch (Exception e) {
            fail("Failed to access onClick field: " + e.getMessage());
        }
    }
}
