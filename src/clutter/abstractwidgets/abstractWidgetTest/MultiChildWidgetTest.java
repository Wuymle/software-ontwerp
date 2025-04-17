package clutter.abstractwidgets.abstractWidgetTest;

import static org.junit.jupiter.api.Assertions.*;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import clutter.abstractwidgets.FlexibleWidget;
import clutter.abstractwidgets.MultiChildWidget;
import clutter.core.Dimension;
import clutter.core.Rectangle;
import clutter.layoutwidgets.enums.Alignment;
import clutter.abstractwidgets.Widget;
import application.test.MockMultiChildWidget;
import application.test.MockWidget;
import application.test.MockGraphics;

class MultiChildWidgetTest {

    private MultiChildWidget multiChildWidget;
    private Widget mockChild1;
    private Widget mockChild2;
    
    @BeforeEach
    void setUp() {
        mockChild1 = new MockWidget();
        mockChild2 = new MockWidget();
        multiChildWidget = new MockMultiChildWidget(mockChild1, mockChild2);
    }

    @Test
    void testConstructorWithVarArgs() {
        assertEquals(2, multiChildWidget.children.length);
        assertEquals(mockChild1, multiChildWidget.children[0]);
        assertEquals(mockChild2, multiChildWidget.children[1]);
    }

    @Test
    void testConstructorWithList() {
        MultiChildWidget widget = new MockMultiChildWidget(mockChild1, mockChild2);
        assertEquals(2, widget.children.length);
        assertEquals(mockChild1, widget.children[0]);
        assertEquals(mockChild2, widget.children[1]);
    }

    // @Test
    // void testFlexibleChildren() {
    //     Mockito.when(mockChild1 instanceof FlexibleWidget).thenReturn(true);
    //     Mockito.when(mockChild2 instanceof FlexibleWidget).thenReturn(false);

    //     List<FlexibleWidget> flexibleChildren = multiChildWidget.flexibleChildren();

    //     assertEquals(1, flexibleChildren.size());
    //     assertTrue(flexibleChildren.contains(mockChild1));
    // }

    // @Test
    // void testInflexibleChildren() {
    //     Mockito.when(mockChild1 instanceof FlexibleWidget).thenReturn(false);
    //     Mockito.when(mockChild2 instanceof FlexibleWidget).thenReturn(false);

    //     List<Widget> inflexibleChildren = multiChildWidget.inflexibleChildren();

    //     assertEquals(2, inflexibleChildren.size());
    //     assertTrue(inflexibleChildren.contains(mockChild1));
    //     assertTrue(inflexibleChildren.contains(mockChild2));
    // }

    // @Test
    // void testHitTest() {
    //     Dimension hitPos = new Dimension(15, 15);
    //     Mockito.when(mockChild1.hitTest(Mockito.anyInt(), Mockito.eq(hitPos), Mockito.anyInt())).thenReturn(true);
    //     Mockito.when(mockChild2.hitTest(Mockito.anyInt(), Mockito.eq(hitPos), Mockito.anyInt())).thenReturn(false);

    //     boolean result = multiChildWidget.hitTest(1, hitPos, 1);

    //     assertTrue(result);
    //     Mockito.verify(mockChild1).hitTest(Mockito.anyInt(), Mockito.eq(hitPos), Mockito.anyInt());
    //     Mockito.verify(mockChild2, Mockito.never()).hitTest(Mockito.anyInt(), Mockito.eq(hitPos), Mockito.anyInt());
    // }

    // @Test
    // void testSetCrossAxisAlignment() {
    //     multiChildWidget.setCrossAxisAlignment(Alignment.CENTER);
    //     assertEquals(Alignment.CENTER, multiChildWidget.crossAxisAlignment);
    // }
}