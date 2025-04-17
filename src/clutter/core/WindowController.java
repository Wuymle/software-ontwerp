package clutter.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import clutter.layoutwidgets.SubWindow;

public class WindowController extends DragController {
    private Set<WindowEventListener> listeners = new HashSet<WindowEventListener>();
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

    private final Dimension WINDOW_SIZE = new Dimension(300, 300);
    private final Dimension WINDOW_POSITION = new Dimension(200, 200);

    public interface WindowEventListener {
        public void onWindowsUpdate();
    }

    public WindowController(Context context) {
        super(context);
    }

    public void move(SubWindow window, Dimension startPosition) {
        if (dragging)
            return;
        currentWindow = window;
        startDragging(startPosition);
        moving = true;
        resizing = false;
        if (window.isMaximized()) {
            windowPositions.replace(window,
                    startPosition.addX(-windowSizes.get(window).x() / 2).addY(-20));
        }
    }

    public void resize(SubWindow window, Dimension startPosition, boolean left, boolean right,
            boolean top, boolean bottom) {
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
    }

    @Override
    protected void updateDragging() {
        if (moving) {
            windowPositions.put(currentWindow,
                    Dimension.max(
                            windowPositions.get(currentWindow)
                                    .add(dragPosition.subtract(startPosition)),
                            new Dimension(-getWindowSize(currentWindow).x() + 10, -5)));
        } else if (resizing) {
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
        listeners.forEach(WindowEventListener::onWindowsUpdate);
    }

    public void addWindow(SubWindow window) {
        if (!windows.isEmpty())
            windows.getLast().setFocus(false);
        window.setFocus(true);
        windows.add(window);
        windowPositions.put(window, WINDOW_POSITION);
        windowSizes.put(window, WINDOW_SIZE);
        listeners.forEach(WindowEventListener::onWindowsUpdate);
    }

    public void removeWindow(SubWindow window) {
        windows.remove(window);
        windowPositions.remove(window);
        windowSizes.remove(window);
        window.setFocus(false);
        if (!windows.isEmpty())
            windows.getLast().setFocus(true);
        listeners.forEach(WindowEventListener::onWindowsUpdate);
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

    public void moveToTop(SubWindow window) {
        if (windows.isEmpty() || window == windows.getLast())
            return;
        windows.getLast().setFocus(false);
        window.setFocus(true);
        windows.remove(window);
        windows.add(window);
        listeners.forEach(WindowEventListener::onWindowsUpdate);
    }

    public void addWindowEventListener(WindowEventListener listener) {
        listeners.add(listener);
    }
}
