package clutter.core;

import java.util.ArrayList;
import java.util.List;

public class ScrollController extends DragController {
    private boolean horizontalScroll = false;
    private boolean verticalScroll = false;
    private double scrollFactorX = 1;
    private double scrollFactorY = 1;
    private double relContentHeight = 1.0;
    private double relContentWidth = 1.0;
    private double scrollX = 0;
    private double scrollY = 0;

    public interface ScrollSubscriber {
        public void onHorizontalScroll(double scrollX);
    
        public void onVerticalScroll(double scrollY);
    }
    

    private AnimationController animationController = new AnimationController();

    private List<ScrollSubscriber> subscribers = new ArrayList<ScrollSubscriber>();

    public ScrollController(Context context) {
        super(context);
    }

    public void scrollHorizontalPages(double pages) {
        if (relContentWidth == 1)
            return;
        animationController.animate((Double d) -> {
            scrollX = Math.clamp(d, 0, 1);
            for (ScrollSubscriber subscriber : subscribers) {
                subscriber.onHorizontalScroll(scrollX);
            }
        }, scrollX, Math.clamp(scrollX + pages * (1.0 / (relContentWidth - 1)), 0, 1), 0.5);
    }

    public void scrollVerticalPages(double pages) {
        if (relContentHeight == 1)
            return;
        animationController.animate((Double d) -> {
            scrollY = Math.clamp(d, 0, 1);
            for (ScrollSubscriber subscriber : subscribers) {
                subscriber.onVerticalScroll(scrollY);
            }
        }, scrollY, Math.clamp(scrollY + pages * (1.0 / (relContentHeight - 1)), 0, 1), 0.5);
    }

    public void setScrollX(double scrollX) {
        Double newScrollX = Math.clamp(scrollX, 0, 1);
        if (newScrollX == this.scrollX)
            return;
        this.scrollX = newScrollX;
        for (ScrollSubscriber subscriber : subscribers) {
            subscriber.onHorizontalScroll(scrollX);
        }
    }

    public void setScrollY(double scrollY) {
        Double newScrollY = Math.clamp(scrollY, 0, 1);
        if (newScrollY == this.scrollY)
            return;
        this.scrollY = newScrollY;
        for (ScrollSubscriber subscriber : subscribers) {
            subscriber.onVerticalScroll(scrollY);
        }
    }

    public double getRelContentHeight() {
        return relContentHeight;
    }

    public void setRelContentHeight(double relContentHeight) {
        Double newRelContentHeight = Math.max(1.0, relContentHeight);
        if (newRelContentHeight == this.relContentHeight)
            return;
        this.relContentHeight = newRelContentHeight;
        for (ScrollSubscriber subscriber : subscribers) {
            subscriber.onVerticalScroll(scrollY);
        }
    }

    public double getRelContentWidth() {
        return relContentWidth;
    }

    public void setRelContentWidth(double relContentWidth) {
        Double newRelContentWidth = Math.max(1.0, relContentWidth);
        if (newRelContentWidth == this.relContentWidth)
            return;
        this.relContentWidth = newRelContentWidth;
        for (ScrollSubscriber subscriber : subscribers) {
            subscriber.onHorizontalScroll(scrollX);
        }
    }

    public void startDragging(Dimension startPosition, boolean horizontal, boolean vertical, double scrollFactorX,
            double scrollFactorY) {
        if (dragging)
            return;
        super.startDragging(startPosition);
        this.horizontalScroll = horizontal;
        this.verticalScroll = vertical;
        this.scrollFactorX = scrollFactorX;
        this.scrollFactorY = scrollFactorY;
    }

    public void addSubscriber(ScrollSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void removeSubscriber(ScrollSubscriber subscriber) {
        subscribers.remove(subscriber);
    }

    protected void updateDragging() {
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
                            + (dragPosition.y() - startPosition.y()) / (scrollFactorY * (relContentHeight - 1)),
                            0,
                            1);
            for (ScrollSubscriber subscriber : subscribers) {
                subscriber.onVerticalScroll(scrollY);
            }
        }
        startPosition = dragPosition;
    }
}
