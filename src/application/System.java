package application;

import clutter.ApplicationWindow;
import clutter.layoutwidgets.GrowToFit;

/**
 * The main class of the application.
 */
public class System {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            final ApplicationWindow window = new ApplicationWindow("SuperDBMS",
                    (DatabaseAppContext appContext) -> new GrowToFit(new Application(appContext)),
                    (ApplicationWindow appWindow) -> new DatabaseAppContext(appWindow));
            window.show();
        });
    }
}
