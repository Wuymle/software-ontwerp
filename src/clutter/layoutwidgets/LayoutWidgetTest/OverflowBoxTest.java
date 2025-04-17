package clutter.layoutwidgets.LayoutWidgetTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clutter.core.Dimension;
import clutter.layoutwidgets.OverflowBox;
import application.test.MockWidget;

class OverflowBoxTest {

    private MockWidget childWidget;
    private AtomicReference<Double> overflowX;
    private AtomicReference<Double> overflowY;
    private BiConsumer<Double, Double> overflowCallback;

    @BeforeEach
    void setUp() {
        childWidget = new MockWidget();
        overflowX = new AtomicReference<>();
        overflowY = new AtomicReference<>();
        overflowCallback = (x, y) -> {
            overflowX.set(x);
            overflowY.set(y);
        };
    }

    @Test
    void testConstructor() {
        // Simple test to ensure constructor works properly
        OverflowBox overflowBox = new OverflowBox(childWidget, overflowCallback);
        assertNotNull(overflowBox);
    }
}
