package clutter.test;

import clutter.ApplicationWindow;
import clutter.core.Context;

public class ScrollableViewTest {
    public static void main(String[] args) {
        ApplicationWindow window = new ApplicationWindow("Test Window",
                context -> {
                    return TestWidgets.ScrollableViewTestWidget(context);
                },
                appWindow -> new Context(appWindow));
        java.awt.EventQueue.invokeLater(() -> {
            window.show();
        });
    }
}