package application.screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import application.DatabaseAppContext;
import application.modes.DataBaseModes;
import application.widgets.TableDesignRow;
import clutter.abstractwidgets.Widget;
import clutter.inputwidgets.Clickable;
import clutter.layoutwidgets.Column;
import clutter.layoutwidgets.Expanded;
import clutter.layoutwidgets.Flexible;
import clutter.layoutwidgets.enums.Alignment;
import clutter.widgetinterfaces.KeyEventHandler;
import clutter.widgetinterfaces.Screen;

/**
 * A screen that represents the table design mode view.
 */
public class TableDesignModeView extends Screen<DatabaseAppContext> implements KeyEventHandler {
    List<String> selectedColumns = new ArrayList<String>();

    /**
     * Constructor for the table design mode view.
     * 
     * @param context The context of the application.
     */
    public TableDesignModeView(DatabaseAppContext context) {
        super(context);
    }

    /**
     * Builds the table design mode view.
     * 
     * @return The table design mode view.
     */
    @Override
    public Widget build() {
        List<Widget> rows = context.getDatabase().getColumnNames(context.getTable()).stream().map(
                columnName -> (Widget) new TableDesignRow(context, columnName, (name) -> {
                    selectedColumns.add(name);
                }, (name) -> {
                    selectedColumns.remove(name);
                })).toList();

        return new Column(new Column(rows).setCrossAxisAlignment(Alignment.STRETCH), new Flexible(
                new Clickable(
                        new Expanded(null),
                        () -> {
                            setState(() -> {
                                context.getDatabase().addColumn(context.getTable());
                            });
                        }, 2))
                .setHorizontalAlignment(Alignment.STRETCH)
                .setVerticalAlignment(Alignment.STRETCH)).setCrossAxisAlignment(Alignment.STRETCH);
    }

    /**
     * Handles key presses.
     * 
     * @param id      The ID of the key event.
     * @param keyCode The key code of the key event.
     * @param keyChar The character of the key event.
     */
    @Override
    public boolean onKeyPress(int id, int keyCode, char keyChar) {
        switch (id) {
            case KeyEvent.KEY_PRESSED:
                switch (keyCode) {
                    case KeyEvent.VK_DELETE:
                        setState(() -> {
                            for (String column : selectedColumns) {
                                context.getDatabase().deleteColumn(context.getTable(), column);
                            }
                            selectedColumns.clear();
                        });
                        return true;

                    case KeyEvent.VK_ESCAPE:
                        context.setDatabaseMode(DataBaseModes.TABLES_MODE);
                        return true;

                    case KeyEvent.VK_ENTER:
                        context.setDatabaseMode(DataBaseModes.TABLE_ROWS_MODE);
                        return true;

                    default:
                        return false;
                }
            default:
                return false;
        }
    }

    /**
     * Shows the table design mode view.
     */
    @Override
    public void onShow() {
        setState(() -> {
        });
        context.getKeyEventController().setKeyHandler(this);
    }

    /**
     * Hides the table design mode view.
     */
    @Override
    public void onHide() {
        context.getKeyEventController().removeKeyHandler(this);
    }
}
