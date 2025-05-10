package clutter.layoutwidgets.layoutwidgetsTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import clutter.abstractwidgets.Widget;
import clutter.layoutwidgets.Center;
import application.test.MockWidget;

class CenterTest {

    @Test
    void testConstructor() {
        // Create a mock widget
        Widget mockWidget = new MockWidget();

        // Create Center with mock widget
        assertDoesNotThrow(() -> new Center(mockWidget));
    }
}
