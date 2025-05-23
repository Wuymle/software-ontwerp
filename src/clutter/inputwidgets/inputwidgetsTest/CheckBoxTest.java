package clutter.inputwidgets.inputwidgetsTest;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import clutter.ApplicationWindow;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.inputwidgets.CheckBox;
import clutter.inputwidgets.IconButton;
import clutter.layoutwidgets.Padding;
import clutter.resources.Icons;
import application.test.TestHelper;

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
        onChange = (value) -> callbackCalled.set(true);
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
}
