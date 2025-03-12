package application;

import clutter.ApplicationWindow;

public class System {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            final ApplicationWindow window = new ApplicationWindow(
                    "My Canvas Window",
                    (DatabaseAppContext appContext) -> new Application(appContext),
                    (ApplicationWindow appWindow) -> new DatabaseAppContext(appWindow));
            window.show();
        });
    }
}
