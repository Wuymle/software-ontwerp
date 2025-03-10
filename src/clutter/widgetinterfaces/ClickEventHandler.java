package clutter.widgetinterfaces;

import clutter.core.Dimension;

public interface ClickEventHandler {
    public Interactable hitTest(int id, Dimension hitPos, int clickCount);
}
