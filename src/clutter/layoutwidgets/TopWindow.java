package clutter.layoutwidgets;

import static clutter.core.Dimension.contains;
import java.util.List;
import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.core.Dimension;
import clutter.core.WindowController;
import clutter.core.WindowController.WindowEventListener;
import clutter.inputwidgets.IconButton;
import clutter.resources.Icons;

public class TopWindow extends StatefulWidget<Context> implements WindowEventListener {
    private WindowController controller;
    private Runnable undo;
    private IconButton undoButton;
    private Runnable redo;
    private IconButton redoButton;

    public TopWindow(Context context, WindowController controller) {
        super(context);
        this.controller = controller;
        controller.addWindowEventListener(this);
    }


    public TopWindow setUndo(Runnable undo) {
        this.undo = undo;
        return this;
    }

    public TopWindow setRedo(Runnable redo) {
        this.redo = redo;
        return this;
    }

    @Override
    public boolean hitTest(int id, Dimension hitPos, int clickCount) {
        if (!contains(position, size, hitPos))
            return false;
        if (undoButton.hitTest(id, hitPos, clickCount) || redoButton.hitTest(id, hitPos, clickCount))   
            return true;
            
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
        undoButton = new IconButton(context, Icons.ARROW_ALT_CIRCLE_LEFT, undo);
        redoButton = new IconButton(context, Icons.ARROW_ALT_CIRCLE_RIGHT, redo);
        return new Column(
            new Row(
                new Padding(undoButton)
                    .all(10),
                new Padding(redoButton)
                    .horizontal(0).vertical(10)),
            new ClampToFit(new Stack(controller.getWindows().stream()
                .<Widget>map((SubWindow window) -> new Offset(
                        window.isMaximized() ? position : controller.getWindowPosition(window),
                        new SizedBox(window.isMaximized() ? size : controller.getWindowSize(window),
                                window)))
                .toList())));
    }
}
