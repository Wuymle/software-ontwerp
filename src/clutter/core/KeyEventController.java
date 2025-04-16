package clutter.core;

import java.util.Stack;

/**
 * A controller for handling key events.
 */
public class KeyEventController {
    private Stack<KeyEventHandler> handlers = new Stack<>();

    public interface KeyEventHandler {
        /**
         * @param id the id of the key event
         * @param keyCode the key code
         * @param keyChar the key character
         * @return true if the event was claimed by the handler else false
         */
        public boolean onKeyPress(int id, int keyCode, char keyChar);

        /**
         * Called when the handler is removed.
         */
        default public void onKeyHandlerRemoved() {}
    }

    /**
     * @param handler the key handler
     */
    public void setKeyHandler(KeyEventHandler handler) {
        if (handler == null)
            throw new Error("Trying to add null to keyHandler");
        handlers.add(handler);
    }

    /**
     * keeps removing keyhandlers from the stack until the given handler is found
     * 
     * @param handler the key handler
     */
    public void removeKeyHandler(KeyEventHandler handler) {
        if (handlers.isEmpty())
            throw new RuntimeException("No key handlers to remove");
        while (!handlers.isEmpty()) {
            KeyEventHandler topHandler = handlers.pop();
            topHandler.onKeyHandlerRemoved();
            if (topHandler.equals(handler)) {
                break;
            }
        }
    }

    /**
     * handle a key event
     * 
     * @param id the id of the key event
     * @param keyCode the key code
     * @param keyChar the key character
     */
    public void handleKeyEvent(int id, int keyCode, char keyChar) {
        for (int i = handlers.size() - 1; i >= 0; i--) {
            if (handlers.get(i).onKeyPress(id, keyCode, keyChar))
                break;
        }
    }
}
