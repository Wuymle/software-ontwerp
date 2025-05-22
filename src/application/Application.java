package application;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import application.screens.TableDesignView;
import application.screens.TableFormsView;
import application.screens.TableRowsView;
import application.screens.TablesView;
import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.KeyEventController.KeyEventHandler;
import clutter.core.Decoration;
import clutter.core.ResizableGridController;
import clutter.core.WindowController;
import clutter.inputwidgets.IconButton;
import clutter.layoutwidgets.Padding;
import clutter.layoutwidgets.Row;
import clutter.layoutwidgets.Stack;
import clutter.layoutwidgets.SubWindow;
import clutter.layoutwidgets.TopWindow;
import clutter.resources.Icons;

/**
 * The main application widget.
 */
public class Application extends StatefulWidget<DatabaseAppContext> implements KeyEventHandler {
    WindowController windowController = new WindowController(context);
    ResizableGridController tablesViewGridController = new ResizableGridController(context, 2, 5);
    Map<String, ResizableGridController> tableDesignViewGridControllers =
            new HashMap<String, ResizableGridController>();
    Map<String, ResizableGridController> tableRowsViewGridControllers =
            new HashMap<String, ResizableGridController>();
    Map<String, ResizableGridController> tableFormsViewGridControllers =
            new HashMap<String, ResizableGridController>();

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
        Widget undoButton = new IconButton(context, Icons.ARROW_ALT_CIRCLE_LEFT,
                () -> context.getDatabase().undo());
        Widget redoButton = new IconButton(context, Icons.ARROW_ALT_CIRCLE_RIGHT,
                () -> context.getDatabase().redo());
        return new Stack(
                new Row(new Padding(undoButton).all(10),
                        new Padding(redoButton).horizontal(0).vertical(10)),
                new TopWindow(context, windowController))
                        .setDecoration(new Decoration().setColor(Color.lightGray));
    }

    @Override
    public boolean onKeyPress(int id, int keyCode, char keyChar, int modifiers) { // Check for
                                                                                  // modifier keys
        boolean isCtrlPressed = (modifiers & KeyEvent.CTRL_DOWN_MASK) != 0;
        boolean isShiftPressed = (modifiers & KeyEvent.SHIFT_DOWN_MASK) != 0;

        // Handle KEY_PRESSED for T key
        if (id == KeyEvent.KEY_PRESSED && keyCode == KeyEvent.VK_T && isCtrlPressed) {
            java.lang.System.out.println("Key Released: CTRL T");
            windowController.addWindow(new SubWindow(context, "Tables", windowController)
                    .setContent(new TablesView(context, tablesViewGridController, this::onOpenTable,
                            this::onOpenFormView)));
            return true;

        }

        if (id == KeyEvent.KEY_PRESSED && keyCode == KeyEvent.VK_Z && isCtrlPressed
                && isShiftPressed) {
            java.lang.System.out.println("Key Released: CTRL SHIFT Z");
            context.getDatabase().redo();
            return true;
        }

        if (id == KeyEvent.KEY_PRESSED && keyCode == KeyEvent.VK_Z && isCtrlPressed) {
            java.lang.System.out.println("Key Released: CTRL Z");
            context.getDatabase().undo();
            return true;
        }

        // Implement alt + tab
        if (id == KeyEvent.KEY_PRESSED && keyCode == KeyEvent.VK_TAB && isCtrlPressed) { // Changed from isCtrlPressed to isAltPressed
            java.lang.System.out.println("Key Pressed: ALT TAB"); // Changed log message
            windowController.focusNextWindow();
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

        rowsWindow.setContent(new TableRowsView(context, tableName, this::onOpenDesignView,
                (a) -> windowController.removeWindow(rowsWindow)));

        windowController.addWindow(rowsWindow);
    }

    private void onOpenDesignView(String tableName) {
        SubWindow designWindow =
                new SubWindow(context, tableName + ": design view", windowController);
        if (!tableDesignViewGridControllers.containsKey(tableName)) {
            tableDesignViewGridControllers.put(tableName,
                    new ResizableGridController(context, 5, 5));
        }
        designWindow.setContent(new TableDesignView(context, tableName,
                tableDesignViewGridControllers.get(tableName), this::onOpenRowsView,
                (a) -> windowController.removeWindow(designWindow)));

        windowController.addWindow(designWindow);
    }

    private void onOpenFormView(String tableName) {
        SubWindow formWindow = new SubWindow(context, tableName + ": form view", windowController);

        formWindow.setContent(new TableFormsView(context, tableName,
                (a) -> windowController.removeWindow(formWindow)));

        windowController.addWindow(formWindow);
    }
}
