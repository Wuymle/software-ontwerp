package application;

import clutter.ApplicationWindow;

/**
 * The main class of the application.
 */
public class System {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            final ApplicationWindow window = new ApplicationWindow("SuperDBMS",
                    (DatabaseAppContext appContext) -> new Application(appContext),
                    (ApplicationWindow appWindow) -> new DatabaseAppContext(appWindow));
            window.show();
        });
    }
}
