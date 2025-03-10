package clutter;

import java.awt.Graphics;
import java.util.function.Function;

import canvaswindow.CanvasWindow;
import clutter.abstractwidgets.Widget;
import clutter.core.ClickEventController;
import clutter.core.Context;
import clutter.core.Dimension;
import clutter.core.KeyEventController;

public class ApplicationWindow extends CanvasWindow {

    private Widget application;
    private KeyEventController keyEventController = new KeyEventController();
    private ClickEventController clickEventController = new ClickEventController();

    public <C extends Context, W extends Widget> ApplicationWindow(String title, Function<C, W> createApplication,
            Function<ApplicationWindow, C> createContext) {
        super(title);
        this.application = createApplication.apply(createContext.apply(this));
        clickEventController.setClickHandler(application);
        application.setPosition(new Dimension(0, 0));
    }

    public KeyEventController getKeyEventController() {
        return keyEventController;
    }

    public ClickEventController getClickEventController() {
        return clickEventController;
    }

    public void requestRepaint() {
        repaint();
    }

    @Override
    protected void paint(Graphics g) {
        // Custom painting code here
        // System.out.println("Clipbounds: " + g.getClipBounds().getWidth() + " " +
        // g.getClipBounds().getHeight());
        application.measure();
        application.layout(new Dimension(0, 0), new Dimension(g.getClipBounds().width, g.getClipBounds().height));
        application.paint(g);
    }

    @Override
    protected void handleMouseEvent(int id, int x, int y, int clickCount) {
        // Handle mouse events here
        // System.out.println("Mouse event: " + id + " at (" + x + ", " + y + ")");
        clickEventController.handleClickEvent(id, new Dimension(x, y), clickCount);
    }

    @Override
    protected void handleKeyEvent(int id, int keyCode, char keyChar) {
        // Handle key events here
        // System.out.println("Key event: " + id + " keyCode: " + keyCode + " keyChar: "
        // + keyChar);
        keyEventController.handleKeyEvent(id, keyCode, keyChar);
    }
}
