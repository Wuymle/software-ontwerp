package clutter.core;

import java.util.Stack;

import clutter.widgetinterfaces.KeyEventHandler;

/**
 * A controller for handling key events.
 */
public class KeyEventController {
    private Stack<KeyEventHandler> handlers = new Stack<>();

    /**
     * @param handler the key handler
     */
    public void setKeyHandler(KeyEventHandler handler) {
        if (handler == null)
            throw new Error("Trying to add null to keyHandler");
        handlers.add(handler);
    }

    /**
     * remove the key handler
     * @param handler the key handler
     */
    public void removeKeyHandler(KeyEventHandler handler) {
        if (handlers.isEmpty())
            throw new RuntimeException("No key handlers to remove");
        while (!handlers.isEmpty()) {
            KeyEventHandler topHandler = handlers.pop();
            if (topHandler.equals(handler)) {
                break;
            }
        }
        // if (handlers.isEmpty())
        // System.out.println("No key handlers left");
    }

    /**
     * handle a key event
     * @param id      the id of the key event
     * @param keyCode the key code
     * @param keyChar the key character
     */
    public void handleKeyEvent(int id, int keyCode, char keyChar) {
        if (handlers.isEmpty())
            return;
        // handlers.forEach(handler ->
        // System.out.println(handler.getClass().getSimpleName()));
        handlers.peek().onKeyPress(id, keyCode, keyChar);
    }
}
