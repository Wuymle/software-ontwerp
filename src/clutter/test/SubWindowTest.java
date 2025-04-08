package clutter.test;

import clutter.ApplicationWindow;
import clutter.core.Context;
import clutter.core.SubWindowController;

public class SubWindowTest {
    public static void main(String[] args) {
        ApplicationWindow window = new ApplicationWindow("SubWindow Test",
                context -> {
                    SubWindowController controller = new SubWindowController(context);
                    return TestWidgets.SubWindowTestWidget(context, controller);
                },
                appWindow -> new Context(appWindow));
        java.awt.EventQueue.invokeLater(() -> {
            window.show();
        });
    }
}
