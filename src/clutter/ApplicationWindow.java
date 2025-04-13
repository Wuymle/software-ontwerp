package clutter;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.function.Function;
import canvaswindow.CanvasWindow;
import clutter.abstractwidgets.Widget;
import clutter.core.ClickEventController;
import clutter.core.Context;
import clutter.core.Dimension;
import clutter.core.KeyEventController;

/**
 * The window for an application.
 */
public class ApplicationWindow extends CanvasWindow {

    private Widget application;
    private KeyEventController keyEventController = new KeyEventController();
    private ClickEventController clickEventController = new ClickEventController();
    
    private int calculationCount = 0;
    private long totalMeasureDuration = 0;
    private long totalLayoutDuration = 0;
    private long totalPaintDuration = 0;

    /**
     * Constructor for the application window.
     * 
     * @param title             The title of the application window.
     * @param createApplication The function to create the application.
     * @param createContext     The function to create the context.
     */
    public <C extends Context, W extends Widget> ApplicationWindow(String title, Function<C, W> createApplication,
            Function<ApplicationWindow, C> createContext) {
        super(title);
        this.application = createApplication.apply(createContext.apply(this));
        clickEventController.setClickHandler(application);
        application.setPosition(new Dimension(0, 0));
    }

    /**
     * Method to handle logic when the window is closed.
     */
    public void onClose() {
        printAverage();
    }


    /**
     * gets the key event controller.
     * 
     * @return the key event controller
     */
    public KeyEventController getKeyEventController() {
        return keyEventController;
    }

    /**
     * gets the click event controller.
     * 
     * @return the click event controller
     */
    public ClickEventController getClickEventController() {
        return clickEventController;
    }

    /**
     * Requests a repaint of the application.
     */
    public void requestRepaint() {
        repaint();
    }

    /**
     * Paint the application
     */
    @Override
    protected void paint(Graphics g) {
        // Custom painting code here
        // System.out.println("Clipbounds: " + g.getClipBounds().getWidth() + " " +
        // g.getClipBounds().getHeight());
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        long startmeasure = System.nanoTime();
        application.measure();
        long startLayout = System.nanoTime();
        application.layout(new Dimension(0, 0),
                new Dimension(g.getClipBounds().width, g.getClipBounds().height));
        long startPaint = System.nanoTime();
        application.paint(g);
        long end = System.nanoTime();
        
        totalMeasureDuration += (startLayout - startmeasure);
        totalLayoutDuration += (startPaint - startLayout);
        totalPaintDuration += (end - startPaint);
        calculationCount++;
    }

    public void printAverage() {
        System.out.println("Average Measure: " + totalMeasureDuration / (1000000 * calculationCount) + "ms, Average Layout: "
                + totalLayoutDuration / (1000000 * calculationCount) + "ms, Average Paint: "
                + totalPaintDuration / (1000000 * calculationCount) + "ms");
    }

    /**
     * Handle mouse events.
     * 
     * @param id         The ID of the mouse event.
     * @param x          The x position of the mouse event.
     * @param y          The y position of the mouse event.
     * @param clickCount The number of clicks.
     */
    @Override
    protected void handleMouseEvent(int id, int x, int y, int clickCount) {
        // Handle mouse events here
        // System.out.println("Mouse event: " + id + " at (" + x + ", " + y + ")");
        clickEventController.handleClickEvent(id, new Dimension(x, y), clickCount);
    }

    /**
     * Handle key events.
     * 
     * @param id      The ID of the key event.
     * @param keyCode The key code of the key event.
     * @param keyChar The key character of the key event.
     */
    @Override
    protected void handleKeyEvent(int id, int keyCode, char keyChar) {
        // Handle key events here
        // System.out.println("Key event: " + id + " keyCode: " + keyCode + " keyChar: "
        // + keyChar);
        keyEventController.handleKeyEvent(id, keyCode, keyChar);
    }
}
