package clutter.layoutwidgets.LayoutWidgetTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;
import clutter.decoratedwidgets.Text;
import clutter.layoutwidgets.Box;
import application.test.MockWidget;
import application.test.TestHelper;

class BoxTest {

    @Test
    void testBoxConstructor() {
        // Create a mock child widget
        Widget childWidget = new MockWidget();
        
        // Create a Box with the child widget
        Box box = new Box(childWidget);
        
        // Assert that the box is created successfully
        assertNotNull(box);
    }
    
    @Test
    void testBoxMeasure() {
        // Create a mock child widget with specific size
        MockWidget childWidget = new MockWidget();
        childWidget.setSize(new Dimension(100, 50));
        
        // Create a Box with the child widget
        Box box = new Box(childWidget);
        
        // Measure the box
        box.measure();
        
        // The box should inherit the preferred size of its child
        assertEquals(childWidget.getPreferredSize(), box.getPreferredSize());
    }
    
    @Test
    void testBoxLayout() {
        // Create a mock child widget
        MockWidget childWidget = new MockWidget();
        
        // Create a Box with the child widget
        Box box = new Box(childWidget);
        
        // Define minimum and maximum sizes for layout
        Dimension minSize = new Dimension(50, 50);
        Dimension maxSize = new Dimension(200, 200);
        
        // Layout the box
        box.layout(minSize, maxSize);
        
        // The box should have applied the constraints to itself
        assertNotNull(box.getSize());
        assertTrue(box.getSize().x() >= minSize.x());
        assertTrue(box.getSize().y() >= minSize.y());
        assertTrue(box.getSize().x() <= maxSize.x());
        assertTrue(box.getSize().y() <= maxSize.y());
    }
    
    @Test
    void testBoxWithTextChild() {
        // Create a Text widget as a child
        Text textWidget = new Text("Test Text");
        
        // Create a Box with the text widget
        Box box = new Box(textWidget);
        
        // Measure the box
        box.measure();
        
        // The box should have a non-zero preferred size
        assertTrue(box.getPreferredSize().x() > 0);
        assertTrue(box.getPreferredSize().y() > 0);
    }
    
}