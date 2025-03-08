package clutter.core;

import java.util.Stack;

import clutter.widgetinterfaces.KeyEventHandler;

public class KeyEventController {
    private Stack<KeyEventHandler> handlers = new Stack<>();

    public void setKeyHandler(KeyEventHandler handler) {
        handlers.add(handler);
    }

    public void removeKeyHandler(KeyEventHandler handler) {
        if (handlers.isEmpty())
            throw new RuntimeException("No key handlers to remove");
        while (!handlers.isEmpty()) {
            KeyEventHandler topHandler = handlers.pop();
            if (topHandler.equals(handler)) {
                break;
            }
        }
    }

    public void handleKeyEvent(int id, int keyCode, char keyChar) {
        if (handlers.isEmpty()) {
            return;
        }
        handlers.peek().onKeyPress(id, keyCode, keyChar);
    }
}
