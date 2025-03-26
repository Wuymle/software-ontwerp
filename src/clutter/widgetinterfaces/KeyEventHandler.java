package clutter.widgetinterfaces;

/**
 * Interface for handling key events.
 */
public interface KeyEventHandler {
    /**
     * @param id      the id of the key event
     * @param keyCode the key code
     * @param keyChar the key character
     * @return true if the event was claimed by the handler else false
     */
    public boolean onKeyPress(int id, int keyCode, char keyChar);

    /**
     * Called when the handler is removed.
     */
    default public void onKeyHandlerRemoved() {
    }
}
