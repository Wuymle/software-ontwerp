package clutter.inputwidgets.inputwidgetsTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.core.Dimension;
import clutter.inputwidgets.InputText;
import clutter.layoutwidgets.Box;
import clutter.ApplicationWindow;
import application.test.TestHelper;

/**
 * Tests for the InputText class.
 */
public class InputTextTest {

    private Context context;
    private String defaultText;
    private AtomicReference<String> changedText;
    private Consumer<String> onTextChange;

    @BeforeEach
    public void setUp() {
        // Mock ApplicationWindow for Context
        ApplicationWindow mockAppWindow = TestHelper.window;
        context = new Context(mockAppWindow);
        defaultText = "Initial Text";
        changedText = new AtomicReference<>(defaultText);
        onTextChange = (text) -> changedText.set(text);
    }

    @Test
    public void testInputTextCreation() throws Exception {
        InputText input = new InputText(context, defaultText, onTextChange);

        assertNotNull(input, "InputText should be created");
        assertEquals(defaultText, TestHelper.getPrivateField(input, "text"),
                "InputText should have the correct text");
        assertEquals(defaultText, TestHelper.getPrivateField(input, "originalText"),
                "InputText should have the correct original text");
        assertEquals(onTextChange, TestHelper.getPrivateField(input, "onTextChange"),
                "InputText should have the correct onTextChange handler");
        assertFalse((Boolean) TestHelper.getPrivateField(input, "editable"),
                "InputText should not be editable initially");
    }

    @Test
    public void testBuild() throws Exception {
        InputText input = new InputText(context, defaultText, onTextChange);
        Widget builtWidget = input.build();

        assertNotNull(builtWidget, "Built widget should not be null");
        assertTrue(builtWidget instanceof Box, "Built widget should be a Box");
    }

    @Test
    public void testSetColor() throws Exception {
        InputText input = new InputText(context, defaultText, onTextChange);
        Color testColor = Color.BLUE;

        InputText returnedInput = input.setColor(testColor);

        assertSame(input, returnedInput, "setColor should return this for method chaining");
        assertEquals(testColor, TestHelper.getPrivateField(input, "color"),
                "InputText should have the correct color");
    }

    @Test
    public void testSetBorderColor() throws Exception {
        InputText input = new InputText(context, defaultText, onTextChange);
        Color testColor = Color.GREEN;

        InputText returnedInput = input.setBorderColor(testColor);

        assertSame(input, returnedInput, "setBorderColor should return this for method chaining");
        assertEquals(testColor, TestHelper.getPrivateField(input, "borderColor"),
                "InputText should have the correct border color");
    }

    @Test
    public void testSetValidationFunction() throws Exception {
        InputText input = new InputText(context, defaultText, onTextChange);
        Function<String, Boolean> validationFunction = text -> text.length() > 3;

        InputText returnedInput = input.setValidationFunction(validationFunction);

        assertSame(input, returnedInput,
                "setValidationFunction should return this for method chaining");
        assertEquals(validationFunction, TestHelper.getPrivateField(input, "validationFunction"),
                "InputText should have the correct validation function");
    }

    @Test
    public void testIsValid() throws Exception {
        InputText input = new InputText(context, defaultText, onTextChange);

        // Get the private isValid method using reflection
        Method isValidMethod = InputText.class.getDeclaredMethod("isValid");
        isValidMethod.setAccessible(true);

        // Without validation function, text is always valid
        boolean isValid = (Boolean) isValidMethod.invoke(input);
        assertTrue(isValid, "Text should be valid without validation function");

        // With validation function that requires length > 5
        Function<String, Boolean> validationFunction = text -> text.length() > 5;
        input.setValidationFunction(validationFunction);

        // Default text is longer than 5, should be valid
        isValid = (Boolean) isValidMethod.invoke(input);
        assertTrue(isValid, "Text should be valid with validationFunction when conditions are met");

        // Change text to something that fails validation
        TestHelper.setPrivateField(input, "text", "abc");
        isValid = (Boolean) isValidMethod.invoke(input);
        assertFalse(isValid,
                "Text should be invalid with validationFunction when conditions aren't met");

        // Original text is always valid regardless of validation function
        TestHelper.setPrivateField(input, "text",
                TestHelper.getPrivateField(input, "originalText"));
        isValid = (Boolean) isValidMethod.invoke(input);
        assertTrue(isValid,
                "Original text should always be valid regardless of validation function");
    }

    @Test
    public void testHitTestBecomesEditableOnClick() throws Exception {
        InputText input = new InputText(context, defaultText, onTextChange);

        // Position the input for testing
        input.setPosition(new Dimension(0, 0));
        input.setSize(new Dimension(100, 20));

        // Simulate click inside the input
        Dimension clickPos = new Dimension(5, 5);
        boolean result = input.hitTest(MouseEvent.MOUSE_CLICKED, clickPos, 1);

        assertTrue(result, "hitTest should return true for valid click");
        assertTrue((Boolean) TestHelper.getPrivateField(input, "editable"),
                "InputText should become editable after click");
    }

    @Test
    public void testHitTestOutsideInputWithValidText() throws Exception {
        InputText input = new InputText(context, defaultText, onTextChange);

        // Position the input for testing
        input.setPosition(new Dimension(0, 0));
        input.setSize(new Dimension(100, 20));

        // First simulate a click inside to make it editable properly
        Dimension insideClickPos = new Dimension(5, 5);
        input.hitTest(MouseEvent.MOUSE_CLICKED, insideClickPos, 1);

        // Verify it's now editable
        assertTrue((Boolean) TestHelper.getPrivateField(input, "editable"),
                "InputText should become editable after click inside");

        // Now simulate click outside the input
        Dimension outsideClickPos = new Dimension(150, 150);
        boolean result = input.hitTest(MouseEvent.MOUSE_CLICKED, outsideClickPos, 1);

        assertFalse(result, "hitTest should return false for click outside with valid text");
        assertFalse((Boolean) TestHelper.getPrivateField(input, "editable"),
                "InputText should become non-editable after click outside");
        assertEquals(defaultText, changedText.get(),
                "Text change handler should be called when clicking outside");
    }

    @Test
    public void testOnKeyPressTypeCharacter() throws Exception {
        InputText input = new InputText(context, defaultText, onTextChange);

        // Position the input for testing
        input.setPosition(new Dimension(0, 0));
        input.setSize(new Dimension(100, 20));

        // Make the input editable properly by simulating a click
        Dimension clickPos = new Dimension(5, 5);
        input.hitTest(MouseEvent.MOUSE_CLICKED, clickPos, 1);

        // Simulate typing 'X'
        boolean handled = input.onKeyPress(KeyEvent.KEY_TYPED, 0, 'X');

        assertTrue(handled, "Key press should be handled");
        assertEquals(defaultText + "X", TestHelper.getPrivateField(input, "text"),
                "Character should be added to text");
    }

    @Test
    public void testOnKeyPressBackspace() throws Exception {
        InputText input = new InputText(context, defaultText, onTextChange);

        // Position the input for testing
        input.setPosition(new Dimension(0, 0));
        input.setSize(new Dimension(100, 20));

        // Make the input editable properly by simulating a click
        Dimension clickPos = new Dimension(5, 5);
        input.hitTest(MouseEvent.MOUSE_CLICKED, clickPos, 1);

        // Simulate pressing backspace
        boolean handled = input.onKeyPress(KeyEvent.KEY_PRESSED, KeyEvent.VK_BACK_SPACE,
                (char) KeyEvent.VK_BACK_SPACE);

        assertTrue(handled, "Backspace key press should be handled");
        assertEquals(defaultText.substring(0, defaultText.length() - 1),
                TestHelper.getPrivateField(input, "text"),
                "Last character should be removed from text");
    }

    @Test
    public void testOnKeyPressEnter() throws Exception {
        InputText input = new InputText(context, defaultText, onTextChange);

        // Position the input for testing
        input.setPosition(new Dimension(0, 0));
        input.setSize(new Dimension(100, 20));

        // Make the input editable properly by simulating a click
        Dimension clickPos = new Dimension(5, 5);
        input.hitTest(MouseEvent.MOUSE_CLICKED, clickPos, 1);

        // Simulate pressing enter
        boolean handled =
                input.onKeyPress(KeyEvent.KEY_PRESSED, KeyEvent.VK_ENTER, (char) KeyEvent.VK_ENTER);

        assertTrue(handled, "Enter key press should be handled");
        assertFalse((Boolean) TestHelper.getPrivateField(input, "editable"),
                "InputText should become non-editable after pressing enter");
        assertEquals(defaultText, changedText.get(),
                "Text change handler should be called when pressing enter");
    }

    @Test
    public void testOnKeyPressEscape() throws Exception {
        InputText input = new InputText(context, defaultText, onTextChange);

        // Position the input for testing
        input.setPosition(new Dimension(0, 0));
        input.setSize(new Dimension(100, 20));

        // Make the input editable properly by simulating a click
        Dimension clickPos = new Dimension(5, 5);
        input.hitTest(MouseEvent.MOUSE_CLICKED, clickPos, 1);

        // Simulate pressing escape
        boolean handled = input.onKeyPress(KeyEvent.KEY_PRESSED, KeyEvent.VK_ESCAPE,
                (char) KeyEvent.VK_ESCAPE);

        assertTrue(handled, "Escape key press should be handled");
        assertFalse((Boolean) TestHelper.getPrivateField(input, "editable"),
                "InputText should become non-editable after pressing escape");
        // No text change should occur with escape
        assertEquals(defaultText, changedText.get());
    }

    @Test
    public void testInvalidTextPreventsExit() throws Exception {
        InputText input = new InputText(context, defaultText, onTextChange);

        // Position the input for testing
        input.setPosition(new Dimension(0, 0));
        input.setSize(new Dimension(100, 20));

        // Make the input editable properly by simulating a click
        Dimension clickPos = new Dimension(5, 5);
        input.hitTest(MouseEvent.MOUSE_CLICKED, clickPos, 1);

        // Set a validation function that fails
        input.setValidationFunction(text -> false);

        // Change the text to something that will fail validation
        TestHelper.setPrivateField(input, "text", "invalid");

        // Try to exit with enter key
        boolean handled =
                input.onKeyPress(KeyEvent.KEY_PRESSED, KeyEvent.VK_ENTER, (char) KeyEvent.VK_ENTER);

        assertTrue(handled, "Key press should be handled");
        assertTrue((Boolean) TestHelper.getPrivateField(input, "editable"),
                "InputText should remain editable when text is invalid");
        assertEquals(defaultText, changedText.get(),
                "Text change handler should not be called when text is invalid");
    }
}
