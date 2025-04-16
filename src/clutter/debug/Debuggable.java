package clutter.debug;

import clutter.abstractwidgets.Widget;

/**
 * Interface for setting debug mode.
 */
public interface Debuggable {
    public Widget debug(DebugMode... mode);

    public boolean isDebug();

    public boolean hasDebugMode(DebugMode mode);
}
