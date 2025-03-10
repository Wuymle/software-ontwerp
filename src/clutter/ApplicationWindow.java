package clutter;

import java.awt.Graphics;
import java.util.function.Function;

import canvaswindow.CanvasWindow;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.core.Dimension;
import clutter.core.KeyEventController;
import clutter.widgetinterfaces.Interactable;

public class ApplicationWindow extends CanvasWindow {

    private Widget application;
    private KeyEventController keyEventController = new KeyEventController();

    public <C extends Context, W extends Widget> ApplicationWindow(String title, Function<C, W> createApplication,
            Function<ApplicationWindow, C> createContext) {
        super(title);
        this.application = createApplication.apply(createContext.apply(this));
        application.setPosition(new Dimension(0, 0));
    }

    public KeyEventController getKeyEventController() {
        return keyEventController;
    }

    public void requestRepaint() {
        repaint();
    }

    @Override
    protected void paint(Graphics g) {
        // Custom painting code here
        // System.out.println("Clipbounds: " + g.getClipBounds().getWidth() + " " + g.getClipBounds().getHeight());
        application.measure();
        application.layout(new Dimension(g.getClipBounds().width, g.getClipBounds().height));
        application.paint(g);
    }

    @Override
    protected void handleMouseEvent(int id, int x, int y, int clickCount) {
        // Handle mouse events here
        // System.out.println("Mouse event: " + id + " at (" + x + ", " + y + ")");
        Interactable hitResult = application.hitTest(id, new Dimension(x, y), clickCount);
        if (hitResult != null) {
            hitResult.onClick();
        }
    }

    @Override
    protected void handleKeyEvent(int id, int keyCode, char keyChar) {
        // Handle key events here
        // System.out.println("Key event: " + id + " keyCode: " + keyCode + " keyChar: "
        // + keyChar);
        keyEventController.handleKeyEvent(id, keyCode, keyChar);
    }
}
