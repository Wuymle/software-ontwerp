package clutter.decoratedwidgets.decoratedwidgetsTest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import clutter.decoratedwidgets.Clip;

/**
 * Tests for the Button class.
 */
public class ClipTest {

    @Test
    public void testclipCreation() throws Exception {
        Clip textObject = new Clip(null);

        assertNotNull(textObject, "Button should be created");
    }
}