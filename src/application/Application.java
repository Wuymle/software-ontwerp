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
import newdatabase.Table;
import newdatabase.Database.TablesChangeListener;
import newdatabase.Table.TableDesignChangeListener;

/**
 * The main application widget.
 */
public class Application extends StatefulWidget<DatabaseAppContext>
        implements KeyEventHandler, TablesChangeListener, TableDesignChangeListener {
    WindowController windowController = new WindowController(context);
    ResizableGridController tablesViewGridController = new ResizableGridController(context, 2, 5);
    Map<Table, ResizableGridController> tableDesignViewGridControllers = new HashMap<>();
    Map<Table, ResizableGridController> tableRowsViewGridControllers = new HashMap<>();

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
                () -> context.getDatabase().getHistory().undo()).setFontColor(Color.black);
        Widget redoButton = new IconButton(context, Icons.ARROW_ALT_CIRCLE_RIGHT,
                () -> context.getDatabase().getHistory().redo()).setFontColor(Color.black);
        return new Stack(
                new Row(new Padding(undoButton).all(10),
                        new Padding(redoButton).horizontal(0).vertical(10)),
                new TopWindow(context, windowController))
                        .setDecoration(new Decoration().setColor(Color.lightGray));
    }

    @Override
    public boolean onKeyPress(int id, int keyCode, char keyChar, int modifiers) { // Check for
                                                                                  // modifier
                                                                                  // keys
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
            context.getDatabase().getHistory().redo();
            return true;
        }

        if (id == KeyEvent.KEY_PRESSED && keyCode == KeyEvent.VK_Z && isCtrlPressed) {
            java.lang.System.out.println("Key Released: CTRL Z");
            context.getDatabase().getHistory().undo();
            return true;
        }

        // Implement alt + tab
        if (id == KeyEvent.KEY_PRESSED && keyCode == KeyEvent.VK_TAB && isCtrlPressed) { // Changed
                                                                                         // from
                                                                                         // isCtrlPressed
                                                                                         // to
                                                                                         // isAltPressed
            java.lang.System.out.println("Key Pressed: ALT TAB"); // Changed log message
            windowController.focusNextWindow();
            return true;
        }

        return false;
    }

    private void onOpenTable(Table table) {
        if (table.getColumns().isEmpty())
            onOpenDesignView(table);
        else
            onOpenRowsView(table);
    }

    private void onOpenRowsView(Table table) {
        table.addTableDesignChangeListener(this);
        SubWindow rowsWindow =
                new SubWindow(context, table.getName() + ": rows view", windowController);

        if (!tableRowsViewGridControllers.containsKey(table)) {
            tableRowsViewGridControllers.put(table,
                    new ResizableGridController(context, table.getColumns().size() + 1, 5));
        }

        rowsWindow.setContent(
                new TableRowsView(context, table, tableRowsViewGridControllers.get(table),
                        this::onOpenDesignView, (a) -> windowController.removeWindow(rowsWindow)));
        windowController.addWindow(rowsWindow);
    }

    private void onOpenDesignView(Table table) {
        SubWindow designWindow =
                new SubWindow(context, table.getName() + ": design view", windowController);
        if (!tableDesignViewGridControllers.containsKey(table)) {
            tableDesignViewGridControllers.put(table, new ResizableGridController(context, 5, 5));
        }
        designWindow.setContent(
                new TableDesignView(context, table, tableDesignViewGridControllers.get(table),
                        this::onOpenRowsView, (a) -> windowController.removeWindow(designWindow)));
        windowController.addWindow(designWindow);
    }

    private void onOpenFormView(Table table) {
        SubWindow formWindow =
                new SubWindow(context, table.getName() + ": form view", windowController);

        formWindow.setContent(new TableFormsView(context, table,
                (a) -> windowController.removeWindow(formWindow)));

        windowController.addWindow(formWindow);
    }

    @Override
    public void onTableDesignChanged(Table table) {
        ResizableGridController resizableGridController = tableRowsViewGridControllers.get(table);
        if (resizableGridController != null
                && table.getColumns().size() + 1 != resizableGridController.getArrayCount()) {
            resizableGridController.setArrayCount(table.getColumns().size() + 1);
        }
    }

    @Override
    public void onTablesChanged() {
        setState(() -> {
            tableDesignViewGridControllers.keySet()
                    .removeIf(table -> !context.getDatabase().getTables().contains(table));
            tableRowsViewGridControllers.keySet()
                    .removeIf(table -> !context.getDatabase().getTables().contains(table));
        });
    }
}
