package clutter.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import clutter.layoutwidgets.SubWindow;

public class SubWindowController extends DragController {
    private List<SubWindow> windows = new LinkedList<SubWindow>();
    private Map<SubWindow, Dimension> windowPositions = new HashMap<SubWindow, Dimension>();
    private Map<SubWindow, Dimension> windowSizes = new HashMap<SubWindow, Dimension>();

    private SubWindow currentWindow;
    private boolean moving = false;
    private boolean resizing = false;
    private boolean left = false;
    private boolean right = false;
    private boolean top = false;
    private boolean bottom = false;

    public SubWindowController(Context context) {
        super(context);
    }

    public void move(SubWindow window, Dimension startPosition) {
        if (dragging)
            return;
        currentWindow = window;
        startDragging(startPosition);
        moving = true;
        resizing = false;
        windows.remove(currentWindow);
        windows.add(currentWindow);
    }

    public void resize(SubWindow window, Dimension startPosition, boolean left, boolean right, boolean top,
            boolean bottom) {
        if (dragging)
            return;
        currentWindow = window;
        startDragging(startPosition);
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        resizing = true;
        moving = false;
        windows.remove(currentWindow);
        windows.add(currentWindow);
    }

    @Override
    protected void updateDragging() {
        if (moving) {
            Dimension oldPosition = windowPositions.get(currentWindow);
            windowPositions.put(currentWindow, oldPosition.add(dragPosition.subtract(startPosition)));
        } else if (resizing) {
            System.out.println("resizing from " + startPosition + " to " + dragPosition);
            Dimension newSize = windowSizes.get(currentWindow);
            Dimension newPosition = windowPositions.get(currentWindow);
            if (right)
                newSize = newSize.addX(dragPosition.x() - startPosition.x());
            if (bottom)
                newSize = newSize.addY(dragPosition.y() - startPosition.y());
            if (left) {
                newSize = newSize.addX(startPosition.x() - dragPosition.x());
                newPosition = newPosition.addX(dragPosition.x() - startPosition.x());
            }
            if (top) {
                newSize = newSize.addY(startPosition.y() - dragPosition.y());
                newPosition = newPosition.addY(dragPosition.y() - startPosition.y());
            }
            windowPositions.put(currentWindow, newPosition);
            windowSizes.put(currentWindow, Dimension.max(newSize, new Dimension(150, 150)));
        }
        startPosition = dragPosition;
        context.requestRepaint();
    }

    public void addWindow(SubWindow window) {
        windows.add(window);
        windowPositions.put(window, new Dimension(10, 10));
        windowSizes.put(window, new Dimension(300, 300));
        context.requestRepaint();
    }

    public void removeWindow(SubWindow window) {
        windows.remove(window);
        windowPositions.remove(window);
        windowSizes.remove(window);
        context.requestRepaint();
    }

    public List<SubWindow> getWindows() {
        return windows;
    }

    public Dimension getWindowPosition(SubWindow window) {
        return windowPositions.get(window);
    }

    public Dimension getWindowSize(SubWindow window) {
        return windowSizes.get(window);
    }
}
