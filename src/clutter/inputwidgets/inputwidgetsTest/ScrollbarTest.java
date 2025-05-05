package clutter.inputwidgets.inputwidgetsTest;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Graphics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.core.Dimension;
import clutter.core.Direction;
import clutter.core.ScrollController;
import clutter.inputwidgets.DragHandle;
import clutter.inputwidgets.Scrollbar;
import clutter.core.ScrollController.ScrollSubscriber;

class ScrollbarTest {

    private MockContext mockContext;
    private ScrollController scrollController;
    private MockWidget mockContent;
    private Scrollbar horizontalScrollbar;
    private Scrollbar verticalScrollbar;
    private MockScrollSubscriber scrollSubscriber;

    @BeforeEach
    void setUp() {
        mockContext = new MockContext();
        scrollController = new ScrollController(mockContext);
        mockContent = new MockWidget();
        scrollSubscriber = new MockScrollSubscriber();

        // Add subscriber to controller to verify scroll events
        scrollController.addSubscriber(scrollSubscriber);

        // Create horizontal and vertical scrollbars for testing
        horizontalScrollbar =
                new Scrollbar(mockContext, mockContent, scrollController, Direction.HORIZONTAL);
        verticalScrollbar =
                new Scrollbar(mockContext, mockContent, scrollController, Direction.VERTICAL);
    }

    @Test
    void testHorizontalScrollbarLayout() {
        // Set content width to 2.0 (twice the viewport width)
        scrollController.setRelContentWidth(2.0);

        // Create a max size dimension (e.g., viewport size)
        Dimension maxSize = new Dimension(100, 20);

        // Call layout with this max size
        horizontalScrollbar.layout(maxSize, maxSize);

        // For a horizontal scrollbar with content width 2.0, the scrollbar width should be half the
        // viewport
        assertEquals(50, horizontalScrollbar.getSize().x());
        // Height should remain the same
        assertEquals(20, horizontalScrollbar.getSize().y());
    }

    @Test
    void testVerticalScrollbarLayout() {
        // Set content height to 3.0 (three times the viewport height)
        scrollController.setRelContentHeight(3.0);

        // Create a max size dimension
        Dimension maxSize = new Dimension(20, 300);

        // Call layout with this max size
        verticalScrollbar.layout(maxSize, maxSize);

        // For a vertical scrollbar with content height 3.0, the scrollbar height should be
        // one-third the viewport
        assertEquals(100, verticalScrollbar.getSize().y());
        // Width should remain the same
        assertEquals(20, verticalScrollbar.getSize().x());
    }

    @Test
    void testBuildReturnsDragHandle() {
        // Build should return a DragHandle widget
        Widget result = horizontalScrollbar.build();

        // Verify it's a DragHandle
        assertTrue(result instanceof DragHandle);
    }

    @Test
    void testNoRelContentScaling() {
        // When rel content is 1.0 (exact size), scrollbar should be same size as max
        scrollController.setRelContentWidth(1.0);
        scrollController.setRelContentHeight(1.0);

        Dimension maxSize = new Dimension(100, 100);

        horizontalScrollbar.layout(maxSize, maxSize);
        verticalScrollbar.layout(maxSize, maxSize);

        // Both scrollbars should have same dimensions as max size
        assertEquals(maxSize.x(), horizontalScrollbar.getSize().x());
        assertEquals(maxSize.y(), horizontalScrollbar.getSize().y());
        assertEquals(maxSize.x(), verticalScrollbar.getSize().x());
        assertEquals(maxSize.y(), verticalScrollbar.getSize().y());
    }

    @Test
    void testExtremeContentScaling() {
        // Test with very large content scaling
        scrollController.setRelContentWidth(10.0);
        scrollController.setRelContentHeight(10.0);

        Dimension maxSize = new Dimension(100, 100);

        horizontalScrollbar.layout(maxSize, maxSize);
        verticalScrollbar.layout(maxSize, maxSize);

        // Horizontal scrollbar width should be 1/10 of max
        assertEquals(10, horizontalScrollbar.getSize().x());
        // Vertical scrollbar height should be 1/10 of max
        assertEquals(10, verticalScrollbar.getSize().y());
    }

    @Test
    void testDifferentMinAndMaxSizes() {
        // Set up different min and max sizes
        scrollController.setRelContentWidth(2.0);
        scrollController.setRelContentHeight(2.0);

        Dimension minSize = new Dimension(10, 10);
        Dimension maxSize = new Dimension(200, 200);

        horizontalScrollbar.layout(minSize, maxSize);

        // Layout should use maxSize for calculations
        assertEquals(100, horizontalScrollbar.getSize().x()); // maxSize.x / relContentWidth
        assertEquals(200, horizontalScrollbar.getSize().y()); // maxSize.y unchanged
    }

    @Test
    void testZeroSizeHandling() {
        // Test with zero dimensions in maxSize
        scrollController.setRelContentWidth(2.0);
        scrollController.setRelContentHeight(2.0);

        Dimension zeroSize = new Dimension(0, 0);

        // Should not throw exceptions
        horizontalScrollbar.layout(zeroSize, zeroSize);
        verticalScrollbar.layout(zeroSize, zeroSize);

        // Size should be set to zero
        assertEquals(0, horizontalScrollbar.getSize().x());
        assertEquals(0, horizontalScrollbar.getSize().y());
        assertEquals(0, verticalScrollbar.getSize().x());
        assertEquals(0, verticalScrollbar.getSize().y());
    }

    @Test
    void testVerticalAndHorizontalScrollBarInteraction() {
        // Test that horizontal scrollbar width doesn't affect vertical scrollbar calculations and
        // vice versa
        scrollController.setRelContentWidth(2.0);
        scrollController.setRelContentHeight(3.0);

        Dimension maxSize = new Dimension(100, 300);

        horizontalScrollbar.layout(maxSize, maxSize);
        verticalScrollbar.layout(maxSize, maxSize);

        // Horizontal scrollbar dimensions
        assertEquals(50, horizontalScrollbar.getSize().x()); // Width affected (100/2)
        assertEquals(300, horizontalScrollbar.getSize().y()); // Height unchanged

        // Vertical scrollbar dimensions
        assertEquals(100, verticalScrollbar.getSize().x()); // Width unchanged
        assertEquals(100, verticalScrollbar.getSize().y()); // Height affected (300/3)
    }

    @Test
    void testControllerInteraction() {
        // Test that controller is properly set and used
        // This is a more behavior-focused test

        // Set up content dimensions
        scrollController.setRelContentWidth(2.0);
        scrollController.setRelContentHeight(2.0);

        // Set scrollbar sizes
        Dimension maxSize = new Dimension(100, 100);
        horizontalScrollbar.layout(maxSize, maxSize);
        verticalScrollbar.layout(maxSize, maxSize);

        // Simulate scrollX update
        scrollController.setScrollX(0.5);

        // Subscriber should have received the scroll update
        assertEquals(0.5, scrollSubscriber.lastHorizontalScrollValue);

        // Simulate scrollY update
        scrollController.setScrollY(0.75);

        // Subscriber should have received the scroll update
        assertEquals(0.75, scrollSubscriber.lastVerticalScrollValue);
    }

    /**
     * Mock Context for testing
     */
    private static class MockContext extends Context {
        public MockContext() {
            super(null); // Pass null as we're mocking the ApplicationWindow
        }
    }

    /**
     * Mock Widget for testing
     */
    private static class MockWidget extends Widget {
        @Override
        protected void runLayout(Dimension minSize, Dimension maxSize) {
            // Simple mock implementation that sets size to maxSize
            this.size = maxSize;
        }

        @Override
        public void runPaint(Graphics g) {
            // Mock implementation, does nothing
        }
        
        @Override
        protected void runMeasure() {
            // Mock implementation, does nothing
        }
    }

    /**
     * Mock ScrollSubscriber for testing
     */
    private static class MockScrollSubscriber implements ScrollSubscriber {
        double lastHorizontalScrollValue = 0.0;
        double lastVerticalScrollValue = 0.0;

        @Override
        public void onHorizontalScroll(double scrollX) {
            lastHorizontalScrollValue = scrollX;
        }

        @Override
        public void onVerticalScroll(double scrollY) {
            lastVerticalScrollValue = scrollY;
        }
    }
}
