package clutter.inputwidgets;

import static clutter.core.Dimension.contains;

import java.awt.event.MouseEvent;
import java.util.function.Consumer;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;
import clutter.debug.Debug;
import clutter.debug.DebugMode;

public class DragHandle extends SingleChildWidget {
    private Consumer<Dimension> onstartDragging;

    public DragHandle(Widget child, Consumer<Dimension> onstartDragging) {
        super(child);
        this.onstartDragging = onstartDragging;
    }

    @Override
    protected boolean runHitTest(int id, Dimension hitPos, int clickCount) {
        boolean claimed = super.runHitTest(id, hitPos, clickCount);
        if (claimed) {
            Debug.log(this, DebugMode.MOUSE, "claimed by child");
            return claimed;
        }
        if (id != MouseEvent.MOUSE_PRESSED || !contains(position, size, hitPos))
            return false;
        onstartDragging.accept(hitPos);
        return true;
    }

}
