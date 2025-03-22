package clutter.widgetinterfaces;

import clutter.abstractwidgets.Widget;

/**
 * Interface for setting debug mode.
 */
public interface Debuggable {
    public Widget setDebug();

    public boolean isDebug();
}
