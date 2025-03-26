package clutter.core;

import java.util.Stack;

import clutter.widgetinterfaces.ClickEventHandler;

/**
 * A controller for handling click events.
 */
public class ClickEventController {
    private Stack<ClickEventHandler> handlers = new Stack<>();

    /**
     * @param handler the click handler
     */
    public void setClickHandler(ClickEventHandler handler) {
        handlers.add(handler);
    }

    /**
     * remove the click handler
     * 
     * @param handler the click handler
     */
    public void removeClickHandler(ClickEventHandler handler) {
        if (handlers.isEmpty())
            throw new RuntimeException("No clickhandlers to remove");
        while (!handlers.isEmpty()) {
            ClickEventHandler topHandler = handlers.pop();
            if (topHandler.equals(handler))
                break;
        }
    }

    /**
     * handle a click event
     * 
     * @param id         the id of the click event
     * @param hitPos     the position of the click
     * @param clickCount the number of clicks
     */
    public void handleClickEvent(int id, Dimension hitPos, int clickCount) {
        for (int i = handlers.size() - 1; i >= 0; i--) {
            if (handlers.get(i).hitTest(id, hitPos, clickCount))
                break;
        }
    }
}
