package clutter.layoutwidgets;

import static clutter.core.Dimension.contains;
import java.util.List;
import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.core.Dimension;
import clutter.core.WindowController;
import clutter.core.WindowController.WindowEventListener;

public class TopWindowNew extends StatefulWidget<Context> implements WindowEventListener {
    private WindowController controller;

    public TopWindowNew(Context context, WindowController controller) {
        super(context);
        this.controller = controller;
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

    @Override
    public void onWindowsUpdate() {
        setState(() -> {
        });
    }

    @Override
    public Widget build() {
        return new Stack(controller.getWindows().stream().<Widget>map(
                (SubWindow window) -> new Offset(window, controller.getWindowPosition(window)))
                .toList());
    }
}
