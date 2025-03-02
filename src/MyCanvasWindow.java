import canvaswindow.CanvasWindow;
import ui.decoratedwidgets.Rectangle;

import java.awt.Graphics;

public class MyCanvasWindow extends CanvasWindow {

    private Rectangle rectangle;

    public MyCanvasWindow(String title) {
        super(title);
    }

    @Override
    protected void paint(Graphics g) {
        // Custom painting code here
        rectangle.paint(g);
    }

    @Override
    protected void handleMouseEvent(int id, int x, int y, int clickCount) {
        // Handle mouse events here
        System.out.println("Mouse event: " + id + " at (" + x + ", " + y + ")");
    }

    @Override
    protected void handleKeyEvent(int id, int keyCode, char keyChar) {
        // Handle key events here
        System.out.println("Key event: " + id + " keyCode: " + keyCode + " keyChar: " + keyChar);
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            final MyCanvasWindow window = new MyCanvasWindow("My Canvas Window");
            window.rectangle = new Rectangle(100, 100);
            window.show();
        });
    }
}
