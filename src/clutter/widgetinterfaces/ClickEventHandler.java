package clutter.widgetinterfaces;

import clutter.core.Dimension;

/**
 * Interface for handling click events.
 */
public interface ClickEventHandler {
    public boolean hitTest(int id, Dimension hitPos, int clickCount);

    default public void onClickHandlerRemoved() {
    }
}
