package application.screens;

import application.DatabaseAppContext;
import clutter.abstractwidgets.Screen;
import clutter.core.KeyEventController.KeyEventHandler;

public abstract class DatabaseScreen extends Screen<DatabaseAppContext> implements KeyEventHandler {
    public DatabaseScreen(DatabaseAppContext context) {
        super(context);
    }

    /**
     * Sets the key event controller to this screen.
     */
    @Override
    public void onGetFocus() {
        context.getKeyEventController().setKeyHandler(this);
    }

    /**
     * Removes the key event controller from this screen.
     */
    @Override
    public void onLoseFocus() {
        context.getKeyEventController().removeKeyHandler(this);
    }
}
