package clutter.inputwidgets.inputwidgetsTest;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import clutter.ApplicationWindow;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.core.Decoration;
import clutter.inputwidgets.CheckBox;
import clutter.inputwidgets.IconButton;
import clutter.layoutwidgets.Padding;
import clutter.resources.Icons;
import application.test.TestHelper;
import clutter.abstractwidgets.ParentWidget;

/**
 * Test class for the CheckBox widget
 */
public class CheckBoxTest {

    private Context context;
    private AtomicBoolean callbackCalled;
    private Consumer<Boolean> onChange;

    @BeforeEach
    void setUp() {
        ApplicationWindow mockAppWindow = TestHelper.window; // For testing purposes only
        context = new Context(mockAppWindow);
        callbackCalled = new AtomicBoolean(false);
        onChange = (checked) -> callbackCalled.set(true);
    }

    /**
     * Test for the first constructor (without initial checked state)
     */
    @Test
    void testFirstConstructor() throws Exception {
        CheckBox checkBox = new CheckBox(context, onChange);

        // Use reflection to access private fields
        Field checkedField = CheckBox.class.getDeclaredField("checked");
        checkedField.setAccessible(true);
        Field onChangeField = CheckBox.class.getDeclaredField("onChange");
        onChangeField.setAccessible(true);
        Field validationField = CheckBox.class.getDeclaredField("validationFunction");
        validationField.setAccessible(true);

        // Verify initial state
        assertFalse((Boolean) checkedField.get(checkBox), "Default checked state should be false");
        assertSame(onChange, onChangeField.get(checkBox),
                "onChange callback should be set correctly");

        // Check that validation function is initially null
        checkBox.setValidationFunction(null);
        assertNull(validationField.get(checkBox), "Validation function should be null initially");
    }

    /**
     * Test for the second constructor (with initial checked state)
     */
    @Test
    void testSecondConstructor() throws Exception {
        CheckBox checkBox = new CheckBox(context, true, onChange);

        // Use reflection to access private fields
        Field checkedField = CheckBox.class.getDeclaredField("checked");
        checkedField.setAccessible(true);

        // Verify initial state
        assertTrue((Boolean) checkedField.get(checkBox), "Initial checked state should be true");
    }

    /**
     * Test for the validation function
     */
    @Test
    void testValidationFunction() throws Exception {
        CheckBox checkBox = new CheckBox(context, true, onChange);

        // Create a validation function that only allows true values
        Function<Boolean, Boolean> validationFunction = value -> value;
        checkBox.setValidationFunction(validationFunction);

        // Use reflection to access private methods
        Field validationField = CheckBox.class.getDeclaredField("validationFunction");
        validationField.setAccessible(true);

        assertSame(validationFunction, validationField.get(checkBox),
                "Validation function should be set correctly");

        // Test isValid method using reflection
        java.lang.reflect.Method isValidMethod = CheckBox.class.getDeclaredMethod("isValid");
        isValidMethod.setAccessible(true);

        // Should be valid when checked is true
        assertTrue((Boolean) isValidMethod.invoke(checkBox),
                "Should be valid when checked is true and validation requires true");

        // Set checked to false
        Field checkedField = CheckBox.class.getDeclaredField("checked");
        checkedField.setAccessible(true);
        checkedField.set(checkBox, false);

        // Should be invalid when checked is false
        assertFalse((Boolean) isValidMethod.invoke(checkBox),
                "Should be invalid when checked is false and validation requires true");
    }

    /**
     * Test the widget structure built by the build method
     */
    @Test
    void testBuild() throws Exception {
        CheckBox checkBox = new CheckBox(context, false, onChange);

        // Get the widget built by the build method
        Widget widget = checkBox.build();

        // Verify the widget structure: should be a Padding wrapping an IconButton
        assertTrue(widget instanceof Padding, "Root widget should be a Padding");

        // Get the child of the Padding widget using reflection
        Field childField = widget.getClass().getSuperclass().getDeclaredField("child");
        childField.setAccessible(true);
        Widget child = (Widget) childField.get(widget);

        assertTrue(child instanceof IconButton, "Child of Padding should be an IconButton");

        // Verify the icon used in the IconButton
        Field iconField = IconButton.class.getDeclaredField("icon");
        iconField.setAccessible(true);
        String icon = (String) iconField.get(child);

        assertEquals(Icons.NO_PEOPLE, icon, "Unchecked checkbox should use NO_PEOPLE icon");

        // Create a checked checkbox and test its icon
        CheckBox checkedBox = new CheckBox(context, true, onChange);
        Widget checkedWidget = checkedBox.build();
        child = (Widget) childField.get(checkedWidget);
        icon = (String) iconField.get(child);

        assertEquals(Icons.CHECKBOX, icon, "Checked checkbox should use CHECKBOX icon");
    }

    /**
     * Test the state change when checkbox is clicked
     */
    @Test
    void testStateChange() throws Exception {
        AtomicBoolean callbackValue = new AtomicBoolean(false);
        Consumer<Boolean> onChange = callbackValue::set;

        CheckBox checkBox = new CheckBox(context, false, onChange);
        Widget builtWidget = checkBox.build();

        // Get the child IconButton
        Field childField = builtWidget.getClass().getSuperclass().getDeclaredField("child");
        childField.setAccessible(true);
        IconButton iconButton = (IconButton) childField.get(builtWidget);

        // Get the onClick runnable
        Field onClickField = IconButton.class.getDeclaredField("onClick");
        onClickField.setAccessible(true);
        Runnable onClick = (Runnable) onClickField.get(iconButton);

        // Simulate a click
        onClick.run();

        // Check if the state was updated
        Field checkedField = CheckBox.class.getDeclaredField("checked");
        checkedField.setAccessible(true);
        assertTrue((Boolean) checkedField.get(checkBox),
                "Checked state should be toggled to true after click");

        // Check if the callback was called with the right value
        assertTrue(callbackValue.get(), "onChange callback should be called with true");
    }

    /**
     * Test validation during state change
     */
    @Test
    void testValidationDuringStateChange() throws Exception {
        AtomicBoolean callbackCalled = new AtomicBoolean(false);
        Consumer<Boolean> onChange = value -> callbackCalled.set(true);

        CheckBox checkBox = new CheckBox(context, false, onChange);

        // Set validation function that always returns false
        checkBox.setValidationFunction(value -> false);

        Widget builtWidget = checkBox.build();

        // Get the child IconButton and trigger click
        Field childField = builtWidget.getClass().getSuperclass().getDeclaredField("child");
        childField.setAccessible(true);
        IconButton iconButton = (IconButton) childField.get(builtWidget);

        Field onClickField = IconButton.class.getDeclaredField("onClick");
        onClickField.setAccessible(true);
        Runnable onClick = (Runnable) onClickField.get(iconButton);

        // Simulate a click
        onClick.run();

        // Check if the state was updated
        Field checkedField = CheckBox.class.getDeclaredField("checked");
        checkedField.setAccessible(true);
        assertTrue((Boolean) checkedField.get(checkBox),
                "Checked state should be toggled even if validation fails");

        // Callback should not be called when validation fails
        assertFalse(callbackCalled.get(),
                "onChange callback should not be called when validation fails");
    }

    /**
     * Test decoration based on validation
     */
    @Test
    void testDecorationBasedOnValidation() throws Exception {
        // Create a checkbox with a validation function that always returns false
        CheckBox checkBox = new CheckBox(context, true, onChange);
        checkBox.setValidationFunction(value -> false);

        Widget builtWidget = checkBox.build();

        // Check that the decoration has a red border due to invalid state
        Field decorationField = ParentWidget.class.getDeclaredField("decoration");
        decorationField.setAccessible(true);
        Decoration decoration = (Decoration) decorationField.get(builtWidget);

        Field borderColorField = Decoration.class.getDeclaredField("borderColor");
        borderColorField.setAccessible(true);
        assertEquals(Color.red, borderColorField.get(decoration),
                "Border should be red when validation fails");

        // Now create a checkbox with a validation that passes
        CheckBox validCheckBox = new CheckBox(context, true, onChange);
        validCheckBox.setValidationFunction(value -> true);

        Widget validWidget = validCheckBox.build();

        decoration = (Decoration) decorationField.get(validWidget);
        assertNull(borderColorField.get(decoration),
                "Border should be null when validation passes");
    }
}
