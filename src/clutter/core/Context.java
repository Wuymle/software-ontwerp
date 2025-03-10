package clutter.core;

import clutter.ApplicationWindow;

public class Context {
    final private ApplicationWindow applicationWindow;

    public Context(ApplicationWindow applicationWindow) {
        this.applicationWindow = applicationWindow;
    }

    public KeyEventController getKeyEventController() {
        return applicationWindow.getKeyEventController();
    }

    public void requestRepaint() {
        applicationWindow.requestRepaint();
    }

    public ClickEventController getClickEventController() {
        return applicationWindow.getClickEventController();
    }
}
