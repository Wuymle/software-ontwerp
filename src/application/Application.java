package application;

import java.awt.event.KeyEvent;
import application.screens.TableDesignView;
import application.screens.TablesView;
import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.KeyEventController.KeyEventHandler;
import clutter.core.WindowController;
import clutter.layoutwidgets.ClampToFit;
import clutter.layoutwidgets.SubWindow;
import clutter.layoutwidgets.TopWindow;
import clutter.layoutwidgets.enums.Alignment;

/**
 * The main application widget.
 */
public class Application extends StatefulWidget<DatabaseAppContext> implements KeyEventHandler {
    WindowController windowController = new WindowController(context);

    /**
     * Constructor for the application widget.
     * 
     * @param context The context of the application.
     */
    public Application(DatabaseAppContext context) {
        super(context);
        context.getKeyEventController().setKeyHandler(this);
    }

    /**
     * Builds the application widget.
     * 
     * @return The application widget.
     */
    @Override
    public Widget build() {
        return new ClampToFit(new TopWindow(context, windowController))
                .setHorizontalAlignment(Alignment.STRETCH).setVerticalAlignment(Alignment.STRETCH);
    }

    @Override
    public boolean onKeyPress(int id, int keyCode, char keyChar) {
        if (id != KeyEvent.KEY_TYPED)
            return false;
        java.lang.System.out.println("Key Released: " + keyChar);
        if (keyChar == 't') {
            windowController.addWindow(new SubWindow(context, "Tables", windowController,
                    new TablesView(context, this::onOpenTable)));
            return true;
        }
        return false;
    }

    private void onOpenTable(String tableName) {
        windowController.addWindow(new SubWindow(context, tableName, windowController,
                new TableDesignView(context, tableName)));
    }
}
