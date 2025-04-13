package clutter.layoutwidgets;

import static clutter.core.Dimension.contains;
import java.awt.Graphics;
import java.util.List;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;
import clutter.core.WindowController;

public class TopWindow extends Widget {
    private WindowController controller;

    public TopWindow(WindowController controller) {
        this.controller = controller;
    }

    @Override
    public void measure() {
        preferredSize = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
        for (SubWindow window : controller.getWindows()) {
            window.measure();
        }
    }

    @Override
    public void layout(Dimension minSize, Dimension maxSize) {
        super.layout(minSize, maxSize);
        layoutWindows();
    }

    @Override
    public void paint(Graphics g) {
        java.awt.Image backgroundImage = java.awt.Toolkit.getDefaultToolkit()
                .getImage(getClass().getResource("/clutter/resources/desktop.jpg"));
        g.drawImage(backgroundImage, 0, 0, size.x(), size.y(), null);
        positionWindows();
        for (SubWindow window : controller.getWindows()) {
            window.paint(g);
        }
    }

    @Override
    public boolean hitTest(int id, Dimension hitPos, int clickCount) {
        if (!contains(position, size, hitPos))
            return false;
        List<SubWindow> windows = controller.getWindows();
        for (int i = windows.size() - 1; i >= 0; i--) {
            if (windows.get(i).hitTest(id, hitPos, clickCount))
                return true;
        }
        return false;
    }

    public void positionWindows() {
        for (SubWindow window : controller.getWindows()) {
            Dimension windowPosition = controller.getWindowPosition(window);
            if (window.isMaximized())
                windowPosition = position;
            window.setPosition(windowPosition);
        }
    }

    public void layoutWindows() {
        for (SubWindow window : controller.getWindows()) {
            Dimension windowSize = controller.getWindowSize(window);
            if (window.isMaximized())
                windowSize = size;
            window.layout(windowSize, windowSize);
        }
    }
}
