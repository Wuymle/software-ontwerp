package clutter.core;


import java.awt.event.MouseEvent;
import clutter.core.ClickEventController.ClickEventHandler;

public abstract class DragController implements ClickEventHandler {
    protected Context context;
    protected boolean dragging = false;
    protected Dimension startPosition;
    protected Dimension dragPosition;

    public DragController(Context context) {
        this.context = context;
    }

    public void startDragging(Dimension startPosition) {
        if (dragging)
            return;
        this.dragging = true;
        this.startPosition = startPosition;
        context.getClickEventController().setClickHandler(this);
    }

    @Override
    public boolean hitTest(int id, Dimension hitPos, int clickCount) {
        if (!dragging)
            return false;
        switch (id) {
            case MouseEvent.MOUSE_RELEASED:
                context.getClickEventController().removeClickHandler(this);
                this.dragging = false;
                this.startPosition = null;
                return true;
            case MouseEvent.MOUSE_DRAGGED:
                this.dragPosition = hitPos;
                updateDragging();
                return true;
            default:
                return false;
        }
    }

    protected abstract void updateDragging();    
}
