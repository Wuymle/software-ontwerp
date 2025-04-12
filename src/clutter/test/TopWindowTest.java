package clutter.test;

import clutter.ApplicationWindow;
import clutter.core.Context;
import clutter.core.WindowController;
import clutter.layoutwidgets.TopWindow;

public class TopWindowTest {
    public static void main(String[] args) {
        ApplicationWindow window = new ApplicationWindow("TopWindow Test", context -> {
            WindowController controller = new WindowController(context);

            controller.addWindow(TestWidgets.SubWindowTestWidget(context, controller));
            controller.addWindow(TestWidgets.SubWindowTestWidget(context, controller));
            return new TopWindow(controller);
        }, appWindow -> new Context(appWindow));
        java.awt.EventQueue.invokeLater(() -> {
            window.show();
        });
    }
}
