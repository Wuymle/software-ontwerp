package clutter.core;

import clutter.ApplicationWindow;

/**
 * A context for widgets.
 */
public class Context {
    final private ApplicationWindow applicationWindow;

    /**
     * @param applicationWindow the application window
     */
    public Context(ApplicationWindow applicationWindow) {
        this.applicationWindow = applicationWindow;
    }

    /**
     * @return the application window
     */
    public KeyEventController getKeyEventController() {
        return applicationWindow.getKeyEventController();
    }

    /**
     * Request a repaint of the application window.
     */
    public void requestRepaint() {
        applicationWindow.requestRepaint();
    }

    /**
     * @return the click event controller
     */
    public ClickEventController getClickEventController() {
        return applicationWindow.getClickEventController();
    }
}
