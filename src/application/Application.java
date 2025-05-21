package application;

import java.awt.Color;
import java.awt.event.KeyEvent;
import application.screens.TableDesignView;
import application.screens.TableFormsView;
import application.screens.TableRowsView;
import application.screens.TablesView;
import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.KeyEventController.KeyEventHandler;
import clutter.core.Decoration;
import clutter.core.WindowController;
import clutter.layoutwidgets.SubWindow;
import clutter.layoutwidgets.TopWindow;

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
        return new TopWindow(context, windowController).setUndo(() -> context.getDatabase().undo())
                .setRedo(() -> context.getDatabase().redo())
                .setDecoration(new Decoration().setColor(Color.lightGray));
    }

    @Override
    public boolean onKeyPress(int id, int keyCode, char keyChar, int modifiers) { // Check for
                                                                                  // modifier keys
        boolean isCtrlPressed = (modifiers & KeyEvent.CTRL_DOWN_MASK) != 0;
        boolean isShiftPressed = (modifiers & KeyEvent.SHIFT_DOWN_MASK) != 0;
        boolean isCtrlShiftPressed = isCtrlPressed && isShiftPressed;
        java.lang.System.out.println("modifier Pressed: " + isCtrlShiftPressed);

        // Handle KEY_PRESSED for T key
        if (id == KeyEvent.KEY_PRESSED && keyCode == KeyEvent.VK_T && isCtrlPressed) {
            java.lang.System.out.println("Key Released: CTRL T");
            windowController.addWindow(new SubWindow(context, "Tables", windowController)
                    .setContent(new TablesView(context, this::onOpenTable, this::onOpenFormView)));
            return true;

        }

        if (id == KeyEvent.KEY_PRESSED && keyCode == KeyEvent.VK_Z && isCtrlShiftPressed) {
            java.lang.System.out.println("Key Released: CTRL SHIFT Z");
            context.getDatabase().redo();
            return true;
        }

        if (id == KeyEvent.KEY_PRESSED && keyCode == KeyEvent.VK_Z && isCtrlPressed) {
            java.lang.System.out.println("Key Released: CTRL Z");
            context.getDatabase().undo();
            return true;
        }
        return false;
    }

    private void onOpenTable(String tableName) {
        if (context.getDatabase().getColumnNames(tableName).isEmpty())
            onOpenDesignView(tableName);
        else
            onOpenRowsView(tableName);
    }

    private void onOpenRowsView(String tableName) {
        SubWindow rowsWindow = new SubWindow(context, tableName + ": rows view", windowController);

        rowsWindow.setContent(new TableRowsView(context, tableName, this::onOpenDesignView)
                .setCloseWindowFunction(() -> {
                    windowController.removeWindow(rowsWindow);
                }));

        windowController.addWindow(rowsWindow);
    }

    private void onOpenDesignView(String tableName) {
        SubWindow designWindow =
                new SubWindow(context, tableName + ": design view", windowController);

        designWindow.setContent(new TableDesignView(context, tableName, this::onOpenRowsView)
                .setCloseWindowFunction(() -> {
                    windowController.removeWindow(designWindow);
                }));

        windowController.addWindow(designWindow);
    }

    private void onOpenFormView(String tableName) {
        SubWindow formWindow = new SubWindow(context, tableName + ": form view", windowController);

        formWindow.setContent(new TableFormsView(context, tableName).setCloseWindowFunction(() -> {
            windowController.removeWindow(formWindow);
        }));

        windowController.addWindow(formWindow);
    }
}
