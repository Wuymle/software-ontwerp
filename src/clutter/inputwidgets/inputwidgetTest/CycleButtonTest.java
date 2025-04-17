package clutter.inputwidgets.inputwidgetTest;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.inputwidgets.Button;
import clutter.inputwidgets.CycleButton;
import application.test.TestHelper;

/**
 * Test class for the CycleButton widget
 */
public class CycleButtonTest {

    private Context context;
    private String[] options;
    private AtomicReference<String> selectedValue;
    private Consumer<String> onSelect;

    @BeforeEach
    void setUp() {
        context = new Context(TestHelper.window);
        options = new String[] {"Option 1", "Option 2", "Option 3"};
        selectedValue = new AtomicReference<>();
        onSelect = selectedValue::set;
    }

    /**
     * Test for the constructor to ensure it properly initializes fields
     */
    @Test
    void testConstructor() throws Exception {
        // Create a cycle button with initial selected option
        CycleButton cycleButton = new CycleButton(context, options, 1, onSelect);

        // Use reflection to access private fields
        Field optionsField = CycleButton.class.getDeclaredField("options");
        optionsField.setAccessible(true);

        Field selectedOptionField = CycleButton.class.getDeclaredField("selectedOption");
        selectedOptionField.setAccessible(true);

        Field onSelectField = CycleButton.class.getDeclaredField("onSelect");
        onSelectField.setAccessible(true);

        // Verify the fields were properly initialized
        assertArrayEquals(options, (String[]) optionsField.get(cycleButton),
                "Options array should be set correctly");
        assertEquals(1, selectedOptionField.get(cycleButton),
                "Selected option should be initialized to 1");
        assertSame(onSelect, onSelectField.get(cycleButton),
                "onSelect callback should be set correctly");
    }

    /**
     * Test that the build method returns a Button with the correct text
     */
    @Test
    void testBuild() throws Exception {
        // Create a cycle button with initial selected option
        CycleButton cycleButton = new CycleButton(context, options, 1, onSelect);

        // Get the built widget
        Widget builtWidget = cycleButton.build();

        // Verify it's a Button
        assertTrue(builtWidget instanceof Button, "Built widget should be a Button");

        // Access the text field of the Button to verify it displays the correct option
        Field textField = Button.class.getDeclaredField("text");
        textField.setAccessible(true);

        assertEquals(options[1], textField.get(builtWidget),
                "Button should display the selected option text");
    }

    /**
     * Test the cycle behavior when the button is clicked
     */
    @Test
    void testCycleBehavior() throws Exception {
        // Create a cycle button with initial selected option
        CycleButton cycleButton = new CycleButton(context, options, 0, onSelect);

        // Get the onClick Runnable from the built Button
        Widget builtWidget = cycleButton.build();
        Field onClickField = Button.class.getDeclaredField("onClick");
        onClickField.setAccessible(true);
        Runnable onClick = (Runnable) onClickField.get(builtWidget);

        // Access the selectedOption field to verify changes
        Field selectedOptionField = CycleButton.class.getDeclaredField("selectedOption");
        selectedOptionField.setAccessible(true);

        // Simulate first click
        onClick.run();

        // Verify selectedOption changed to 1 and callback was called with "Option 2"
        assertEquals(1, selectedOptionField.get(cycleButton),
                "Selected option should be incremented to 1");
        assertEquals("Option 2", selectedValue.get(), "Callback should receive 'Option 2'");

        // Simulate second click
        onClick.run();

        // Verify selectedOption changed to 2 and callback was called with "Option 3"
        assertEquals(2, selectedOptionField.get(cycleButton),
                "Selected option should be incremented to 2");
        assertEquals("Option 3", selectedValue.get(), "Callback should receive 'Option 3'");

        // Simulate third click - should wrap around to first option
        onClick.run();

        // Verify selectedOption wrapped to 0 and callback was called with "Option 1"
        assertEquals(0, selectedOptionField.get(cycleButton),
                "Selected option should wrap around to 0");
        assertEquals("Option 1", selectedValue.get(), "Callback should receive 'Option 1'");
    }

    /**
     * Test behavior with empty options array
     */
    @Test
    void testEmptyOptions() {
        // We expect an ArrayIndexOutOfBoundsException when building with empty options
        Exception exception = assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            CycleButton cycleButton = new CycleButton(context, new String[] {}, 0, onSelect);
            cycleButton.build();
        });

        assertNotNull(exception, "Should throw exception with empty options array");
    }

    /**
     * Test with invalid selected option index
     */
    @Test
    void testInvalidSelectedOption() {
        // We expect an ArrayIndexOutOfBoundsException when selected option is out of bounds
        Exception exception = assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            CycleButton cycleButton = new CycleButton(context, options, 5, onSelect);
            cycleButton.build();
        });

        assertNotNull(exception, "Should throw exception with invalid selected option");
    }

    /**
     * Test that the widget rebuilds after state change
     */
    @Test
    void testRebuildAfterStateChange() throws Exception {
        // Create a cycle button
        CycleButton cycleButton = new CycleButton(context, options, 0, onSelect);

        // First build
        Widget firstBuild = cycleButton.build();

        // Get the onClick and trigger it
        Field onClickField = Button.class.getDeclaredField("onClick");
        onClickField.setAccessible(true);
        Runnable onClick = (Runnable) onClickField.get(firstBuild);
        onClick.run();

        // Access the text of the first build button
        Field textField = Button.class.getDeclaredField("text");
        textField.setAccessible(true);

        // Get text from second build (after state change)
        Widget secondBuild = cycleButton.build();
        String secondBuildText = (String) textField.get(secondBuild);

        // Verify second build shows updated text
        assertEquals(options[1], secondBuildText, "Rebuilt button should show the updated text");
    }
}
