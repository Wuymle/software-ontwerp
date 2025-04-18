package clutter.decoratedwidgets.decoratedwidgetsTest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import clutter.decoratedwidgets.Clip;
import clutter.decoratedwidgets.Icon;

/**
 * Tests for the Button class.
 */
public class ClipTest {

    @Test
    public void testclipCreation() throws Exception {
        Clip textObject = new Clip(new Icon("X"));

        assertNotNull(textObject, "Button should be created");
    }
}