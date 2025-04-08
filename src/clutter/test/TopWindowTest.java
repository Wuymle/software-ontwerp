package clutter.test;

import clutter.ApplicationWindow;
import clutter.core.Context;
import clutter.layoutwidgets.TopWindow;

public class TopWindowTest {
    public static void main(String[] args) {
        ApplicationWindow window = new ApplicationWindow("TopWindow Test",
                context -> {
                    if (context == null) {
                        throw new IllegalArgumentException("Context cannot be null");
                    }
                    return new TopWindow(context);},
                appWindow -> new Context(appWindow));
        java.awt.EventQueue.invokeLater(() -> {
            window.show();
        });
    }
}
