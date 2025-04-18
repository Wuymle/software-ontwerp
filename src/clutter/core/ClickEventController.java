package clutter.core;

import java.util.Stack;


/**
 * A controller for handling click events.
 */
public class ClickEventController {
    private Stack<ClickEventHandler> handlers = new Stack<>();

    public interface ClickEventHandler {
        public boolean hitTest(int id, Dimension hitPos, int clickCount);

        default public void onClickHandlerRemoved() {}
    }

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
        // System.out.println("removing clickhandler " + handler.getClass().getSimpleName());
        if (handlers.isEmpty())
            throw new RuntimeException("No clickhandlers to remove");
        boolean found = false;
        while (!handlers.isEmpty()) {
            ClickEventHandler topHandler = handlers.pop();
            topHandler.onClickHandlerRemoved();
            if (topHandler.equals(handler)) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new RuntimeException(
                    "Key handler not found: " + handler.getClass().getSimpleName());
        }
    }

    /**
     * handle a click event
     * 
     * @param id the id of the click event
     * @param hitPos the position of the click
     * @param clickCount the number of clicks
     */
    public void handleClickEvent(int id, Dimension hitPos, int clickCount) {
        System.out.println(
                "ClickHandlers: " + handlers.stream().map(h -> h.getClass().getSimpleName()).toList());
        for (int i = handlers.size() - 1; i >= 0; i--) {
            if (handlers.get(i).hitTest(id, hitPos, clickCount))
                break;
        }
    }
}
