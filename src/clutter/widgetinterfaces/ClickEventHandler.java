package clutter.widgetinterfaces;

import clutter.core.Dimension;

/**
 * Interface for handling click events.
 */
public interface ClickEventHandler {
    public Interactable hitTest(int id, Dimension hitPos, int clickCount);
}
