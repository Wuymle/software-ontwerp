package clutter.layoutwidgets.layoutwidgetsTest;

import clutter.abstractwidgets.LeafWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;
import clutter.layoutwidgets.Stack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StackTest {

    private Stack stack;
    private MockWidget child1;
    private MockWidget child2;
    private MockWidget child3;

    @BeforeEach
    void setUp() {
        // Create mock widgets with different dimensions
        child1 = new MockWidget(new Dimension(100, 50));
        child2 = new MockWidget(new Dimension(80, 120));
        child3 = new MockWidget(new Dimension(120, 80));

        // Create a Stack with three children
        stack = new Stack(Arrays.asList(child1, child2, child3));
    }

    @Test
    void testConstructorWithList() {
        // Test constructor that takes a List
        List<Widget> children = new ArrayList<>();
        children.add(child1);
        children.add(child2);

        Stack stackWithList = new Stack(children);
        stackWithList.measure();

        // Size should be the maximum width and height among all children
        assertEquals(Math.max(child1.getPreferredSize().x(), child2.getPreferredSize().x()),
                stackWithList.getPreferredSize().x());
        assertEquals(Math.max(child1.getPreferredSize().y(), child2.getPreferredSize().y()),
                stackWithList.getPreferredSize().y());
    }

    @Test
    void testConstructorWithVarArgs() {
        // Test constructor that takes varargs
        Stack stackWithVarArgs = new Stack(child1, child2);
        stackWithVarArgs.measure();

        // Size should be the maximum width and height among all children
        assertEquals(Math.max(child1.getPreferredSize().x(), child2.getPreferredSize().x()),
                stackWithVarArgs.getPreferredSize().x());
        assertEquals(Math.max(child1.getPreferredSize().y(), child2.getPreferredSize().y()),
                stackWithVarArgs.getPreferredSize().y());
    }

    @Test
    void testMeasure() {
        stack.measure();

        // Stack should be measured to the largest dimensions of any child
        assertEquals(120, stack.getPreferredSize().x());
        assertEquals(120, stack.getPreferredSize().y());
    }

    @Test
    void testEmptyStack() {
        Stack emptyStack = new Stack(new ArrayList<>());
        emptyStack.measure();

        // Empty stack should have zero size
        assertEquals(new Dimension(0, 0), emptyStack.getPreferredSize());

        emptyStack.layout(new Dimension(100, 100), new Dimension(200, 200));
        assertEquals(new Dimension(100, 100), emptyStack.getSize());
    }

    @Test
    void testSingleChildStack() {
        Stack singleChildStack = new Stack(child1);
        singleChildStack.measure();

        // Size should match the single child
        assertEquals(child1.getPreferredSize(), singleChildStack.getPreferredSize());
    }

    // Mock Widget class for testing
    private static class MockWidget extends LeafWidget {
        private final Dimension customPreferredSize;

        public MockWidget(Dimension preferredSize) {
            this.customPreferredSize = preferredSize;
        }

        @Override
        public void runMeasure() {
            // Set the preferred size in the parent class
            this.preferredSize = customPreferredSize;
        }

        @Override
        public boolean hitTest(int id, Dimension hitPos, int clickCount) {
            return true;
        }

        @Override
        public void runPaint(Graphics g) {
            // Mock implementation
        }
    }
}

