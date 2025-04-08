package clutter.layoutwidgets;

import static clutter.core.Dimension.contains;

import java.awt.Graphics;
import java.util.List;

import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.core.Dimension;
import clutter.core.SubWindowController;
import clutter.test.TestWidgets;

public class TopWindow extends Widget {
    private Context context;
    private SubWindowController controller;

    public TopWindow(Context context) {
        this.context = context;
        controller = new SubWindowController(context);
        controller.addWindow((SubWindow) TestWidgets.SubWindowTestWidget(this.context, controller).setDebug());
        controller.addWindow((SubWindow) TestWidgets.SubWindowTestWidget(this.context, controller).setDebug());
        controller.addWindow((SubWindow) TestWidgets.SubWindowTestWidget(this.context, controller).setDebug());

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
        positionWindows();
        // g.setColor(Color.yellow);
        // g.fillRect(position.x(), position.y(), size.x(), size.y());
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
            window.setPosition(controller.getWindowPosition(window));
        }
    }

    public void layoutWindows() {
        for (SubWindow window : controller.getWindows()) {
            Dimension windowSize = controller.getWindowSize(window);
            window.layout(windowSize, windowSize);
        }
    }
}
