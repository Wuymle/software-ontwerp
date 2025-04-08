package clutter.inputwidgets;

import static clutter.core.Dimension.contains;

import java.awt.event.MouseEvent;
import java.util.function.Consumer;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;

public class DragHandle extends SingleChildWidget{
    private Consumer<Dimension> onstartDragging;

    public DragHandle(Widget child, Consumer<Dimension> onstartDragging) {
        super(child);
        this.onstartDragging = onstartDragging;
    }

        @Override
    public boolean hitTest(int id, Dimension hitPos, int clickCount) {
        boolean claimed = super.hitTest(id, hitPos, clickCount);
        if (claimed) {
            return claimed;
        }
        if (id != MouseEvent.MOUSE_PRESSED || !contains(position, size, hitPos))
            return false;
        onstartDragging.accept(hitPos);
        return true;
    }
    
}
