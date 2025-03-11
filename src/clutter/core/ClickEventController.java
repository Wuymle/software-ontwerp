package clutter.core;

import java.util.Stack;

import clutter.widgetinterfaces.ClickEventHandler;
import clutter.widgetinterfaces.Interactable;

public class ClickEventController {
    private Stack<ClickEventHandler> handlers = new Stack<>();

    public void setClickHandler(ClickEventHandler handler) {
        handlers.add(handler);
    }

    public void removeClickHandler(ClickEventHandler handler) {
        if (handlers.isEmpty())
            throw new RuntimeException("No clickhandlers to remove");
        while (!handlers.isEmpty()) {
            ClickEventHandler topHandler = handlers.pop();
            if (topHandler.equals(handler))
                break;
        }
    }

    public void handleClickEvent(int id, Dimension hitPos, int clickCount) {
        if (handlers.isEmpty())
            return;

        for (int i = handlers.size() - 1; i >= 0; i--) {
            ClickEventHandler handler = handlers.get(i);
            Interactable clickedWidget = handler.hitTest(id, hitPos, clickCount);
            if (clickedWidget != null) {
                clickedWidget.onClick();
                break;
            }
        }
    }
}
