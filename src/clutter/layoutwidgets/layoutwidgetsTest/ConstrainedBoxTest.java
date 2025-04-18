package clutter.layoutwidgets.layoutwidgetsTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;
import clutter.decoratedwidgets.Text;
import clutter.layoutwidgets.ConstrainedBox;
import application.test.MockWidget;
import java.lang.reflect.Field;

class ConstrainedBoxTest {

    // Helper method to set preferred size using reflection since there's no public setter
    private void setPreferredSize(Widget widget, Dimension size) {
        try {
            Field preferredSizeField = Widget.class.getDeclaredField("preferredSize");
            preferredSizeField.setAccessible(true);
            preferredSizeField.set(widget, size);
        } catch (Exception e) {
            fail("Failed to set preferred size: " + e.getMessage());
        }
    }

    @Test
    void testConstrainedBoxConstructor() {
        // Create a mock child widget
        Widget childWidget = new MockWidget();

        // Create a ConstrainedBox with the child widget
        ConstrainedBox box = new ConstrainedBox(childWidget);

        // Assert that the box is created successfully
        assertNotNull(box);
    }

    @Test
    void testSetWidth() {
        // Create a mock child widget with specific size
        MockWidget childWidget = new MockWidget();
        childWidget.setSize(new Dimension(100, 50));
        // Set the preferred size using reflection
        setPreferredSize(childWidget, new Dimension(100, 50));

        // Create a ConstrainedBox with the child widget and set width
        ConstrainedBox box = new ConstrainedBox(childWidget);
        ConstrainedBox returnedBox = box.setWidth(200);

        // Method should return itself for method chaining
        assertSame(box, returnedBox);

        // Measure to apply constraints
        box.measure();

        // The box preferred size should now have the constrained width
        assertEquals(200, box.getPreferredSize().x());
    }

    @Test
    void testSetHeight() {
        // Create a mock child widget with specific size
        MockWidget childWidget = new MockWidget();
        childWidget.setSize(new Dimension(100, 50));
        // Set the preferred size using reflection
        setPreferredSize(childWidget, new Dimension(100, 50));

        // Create a ConstrainedBox with the child widget and set height
        ConstrainedBox box = new ConstrainedBox(childWidget);
        ConstrainedBox returnedBox = box.setHeight(150);

        // Method should return itself for method chaining
        assertSame(box, returnedBox);

        // Measure to apply constraints
        box.measure();

        // The box preferred size should now have the constrained height
        assertEquals(150, box.getPreferredSize().y());
    }

    @Test
    void testSetMaxWidth() {
        // Create a mock child widget with specific size
        MockWidget childWidget = new MockWidget();
        childWidget.setSize(new Dimension(200, 50));
        // Set the preferred size using reflection
        setPreferredSize(childWidget, new Dimension(200, 50));

        // Create a ConstrainedBox with the child widget and set max width
        ConstrainedBox box = new ConstrainedBox(childWidget);
        box.setMaxWidth(150);

        // Measure to apply constraints
        box.measure();

        // The box preferred size should be constrained to max width
        assertEquals(150, box.getPreferredSize().x());
    }

    @Test
    void testSetMaxHeight() {
        // Create a mock child widget with specific size
        MockWidget childWidget = new MockWidget();
        childWidget.setSize(new Dimension(100, 200));
        // Set the preferred size using reflection
        setPreferredSize(childWidget, new Dimension(100, 200));

        // Create a ConstrainedBox with the child widget and set max height
        ConstrainedBox box = new ConstrainedBox(childWidget);
        box.setMaxHeight(150);

        // Measure to apply constraints
        box.measure();

        // The box preferred size should be constrained to max height
        assertEquals(150, box.getPreferredSize().y());
    }

    @Test
    void testSetMinWidth() {
        // Create a mock child widget with specific size
        MockWidget childWidget = new MockWidget();
        childWidget.setSize(new Dimension(50, 100));
        // Set the preferred size using reflection
        setPreferredSize(childWidget, new Dimension(50, 100));

        // Create a ConstrainedBox with the child widget and set min width
        ConstrainedBox box = new ConstrainedBox(childWidget);
        box.setMinWidth(100);

        // Measure to apply constraints
        box.measure();

        // The box preferred size should be at least the min width
        assertEquals(100, box.getPreferredSize().x());
    }

    @Test
    void testSetMinHeight() {
        // Create a mock child widget with specific size
        MockWidget childWidget = new MockWidget();
        childWidget.setSize(new Dimension(100, 50));
        // Set the preferred size using reflection
        setPreferredSize(childWidget, new Dimension(100, 50));

        // Create a ConstrainedBox with the child widget and set min height
        ConstrainedBox box = new ConstrainedBox(childWidget);
        box.setMinHeight(100);

        // Measure to apply constraints
        box.measure();

        // The box preferred size should be at least the min height
        assertEquals(100, box.getPreferredSize().y());
    }

    @Test
    void testCombinedConstraints() {
        // Create a mock child widget with specific size
        MockWidget childWidget = new MockWidget();
        childWidget.setSize(new Dimension(75, 75));
        // Set the preferred size using reflection
        setPreferredSize(childWidget, new Dimension(75, 75));

        // Create a ConstrainedBox with the child widget and multiple constraints
        ConstrainedBox box = new ConstrainedBox(childWidget).setMinWidth(50).setMaxWidth(100)
                .setMinHeight(50).setMaxHeight(100);

        // Measure to apply constraints
        box.measure();

        // The box preferred size should satisfy all constraints
        assertEquals(75, box.getPreferredSize().x()); // Between min 50 and max 100
        assertEquals(75, box.getPreferredSize().y()); // Between min 50 and max 100

        // Create another box with constraints that will cause adjustments
        childWidget = new MockWidget();
        childWidget.setSize(new Dimension(25, 125));
        // Set the preferred size using reflection
        setPreferredSize(childWidget, new Dimension(25, 125));

        box = new ConstrainedBox(childWidget).setMinWidth(50).setMaxWidth(100).setMinHeight(50)
                .setMaxHeight(100);

        // Measure to apply constraints
        box.measure();

        // The box preferred size should be adjusted to meet constraints
        assertEquals(50, box.getPreferredSize().x()); // Increased to minimum
        assertEquals(100, box.getPreferredSize().y()); // Decreased to maximum
    }

    @Test
    void testExplicitSizeOverridesChildSize() {
        // Create a mock child widget with specific size
        MockWidget childWidget = new MockWidget();
        childWidget.setSize(new Dimension(100, 100));
        // Set the preferred size using reflection
        setPreferredSize(childWidget, new Dimension(100, 100));

        // Create a ConstrainedBox with explicit dimensions
        ConstrainedBox box = new ConstrainedBox(childWidget).setWidth(200).setHeight(150);

        // Measure to apply constraints
        box.measure();

        // Explicit dimensions should override child dimensions
        assertEquals(200, box.getPreferredSize().x());
        assertEquals(150, box.getPreferredSize().y());
    }

    @Test
    void testWithTextChild() {
        // Create a Text widget as a child
        Text textWidget = new Text("Test Text");

        // Create a ConstrainedBox with constraints
        ConstrainedBox box = new ConstrainedBox(textWidget).setMinWidth(100).setMinHeight(50);

        // Measure the box
        box.measure();

        // The box should have at least the minimum dimensions
        assertTrue(box.getPreferredSize().x() >= 100);
        assertTrue(box.getPreferredSize().y() >= 50);
    }

    @Test
    void testLayout() {
        // Create a mock child widget
        MockWidget childWidget = new MockWidget();
        // Set the preferred size using reflection
        setPreferredSize(childWidget, new Dimension(75, 75));

        // Set a position for the child widget
        childWidget.setPosition(new Dimension(0, 0));

        // Create a ConstrainedBox with constraints
        ConstrainedBox box = new ConstrainedBox(childWidget).setMinWidth(50).setMaxWidth(100)
                .setMinHeight(50).setMaxHeight(100);

        // Measure and layout
        box.measure();
        box.layout(new Dimension(0, 0), new Dimension(200, 200));

        // The box size should be set
        assertNotNull(box.getSize());

        // Since we explicitly set the position of the child widget, it should be non-null
        assertNotNull(childWidget.getPosition());
    }
}
