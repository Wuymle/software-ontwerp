package clutter.core;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import clutter.widgetinterfaces.ClickEventHandler;
import clutter.widgetinterfaces.ScrollSubscriber;

public class ScrollController implements ClickEventHandler {
    private Context context;
    private boolean dragging = false;
    private Dimension startPosition;
    private Dimension dragPosition;
    private boolean horizontalScroll = false;
    private boolean verticalScroll = false;
    private double scrollFactorX = 1;
    private double scrollFactorY = 1;
    private double relContentHeight = 1.0;
    private double relContentWidth = 1.0;
    private double scrollX = 0;
    private double scrollY = 0;

    private List<ScrollSubscriber> subscribers = new ArrayList<ScrollSubscriber>();

    public ScrollController(Context context) {
        this.context = context;
    }

    public double getRelContentHeight() {
        return relContentHeight;
    }

    public void setRelContentHeight(double relContentHeight) {
        this.relContentHeight = relContentHeight;
    }

    public double getRelContentWidth() {
        return relContentWidth;
    }

    public void setRelContentWidth(double relContentWidth) {
        this.relContentWidth = relContentWidth;
    }

    public void startDragging(Dimension startPosition, boolean horizontal, boolean vertical, double scrollFactorX,
            double scrollFactorY) {
        if (dragging)
            return;
        this.dragging = true;
        this.startPosition = startPosition;
        this.horizontalScroll = horizontal;
        this.verticalScroll = vertical;
        this.scrollFactorX = scrollFactorX;
        this.scrollFactorY = scrollFactorY;
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

    public void addSubscriber(ScrollSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void removeSubscriber(ScrollSubscriber subscriber) {
        subscribers.remove(subscriber);
    }

    private void updateDragging() {
        if (horizontalScroll && relContentWidth != 1) {
            scrollX = Math
                    .clamp(scrollX
                            + (dragPosition.x() - startPosition.x()) / (scrollFactorX * (relContentWidth - 1)),
                            0,
                            1);
            for (ScrollSubscriber subscriber : subscribers) {
                subscriber.onHorizontalScroll(scrollX);
            }
        }
        if (verticalScroll && relContentHeight != 1) {
            scrollY = Math
                    .clamp(scrollY
                            + (dragPosition.y() - startPosition.y()) / (scrollFactorY * (relContentHeight  - 1)),
                            0,
                            1);
            for (ScrollSubscriber subscriber : subscribers) {
                subscriber.onVerticalScroll(scrollY);
            }
        }
        startPosition = dragPosition;
    }
}
