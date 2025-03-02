import java.awt.Graphics;

import canvaswindow.CanvasWindow;
import clutter.abstractwidgets.Interactable;

public class ApplicationWindow extends CanvasWindow {

    private Application application;

    public ApplicationWindow(String title) {
        super(title);
        this.application = new Application();
    }

    @Override
    protected void paint(Graphics g) {
        // Custom painting code here
        System.out.println("Clipbounds: " + g.getClipBounds().getWidth() + " " + g.getClipBounds().getHeight());
        application.layout(g.getClipBounds().width, g.getClipBounds().height);
        application.paint(g);
    }

    @Override
    protected void handleMouseEvent(int id, int x, int y, int clickCount) {
        // Handle mouse events here
        System.out.println("Mouse event: " + id + " at (" + x + ", " + y + ")");
        Interactable hitResult = application.hitTest(id, x, y, clickCount);
        if (hitResult != null) {
            hitResult.onClick();
        }

    }

    @Override
    protected void handleKeyEvent(int id, int keyCode, char keyChar) {
        // Handle key events here
        System.out.println("Key event: " + id + " keyCode: " + keyCode + " keyChar: " + keyChar);
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            final ApplicationWindow window = new ApplicationWindow("My Canvas Window");
            window.show();
        });
    }
}
