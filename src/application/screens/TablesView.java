package application.screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import application.DatabaseAppContext;
import application.widgets.Header;
import application.widgets.TablesModeRow;
import clutter.abstractwidgets.Screen;
import clutter.abstractwidgets.Widget;
import clutter.core.KeyEventController.KeyEventHandler;
import clutter.inputwidgets.Clickable;
import clutter.layoutwidgets.Column;
import clutter.layoutwidgets.ClampToFit;
import clutter.layoutwidgets.Flexible;
import clutter.layoutwidgets.enums.Alignment;

/**
 * A screen that represents the tables mode view.
 */
public class TablesView extends Screen<DatabaseAppContext> implements KeyEventHandler {
    List<String> selectedTables = new ArrayList<String>();

    public TablesView(DatabaseAppContext context) {
        super(context);
    }

    /**
     * Builds the tables mode view.
     * 
     * @return The tables mode view.
     */
    @Override
    public Widget build() {
        List<Widget> rows = new ArrayList<Widget>();

        for (String table : context.getDatabase().getTables()) {
            rows.add(new TablesModeRow(context, table, (tableName) -> {
                selectedTables.add(tableName);
            }, (tableName) -> {
                selectedTables.remove(tableName);
            }));
        }
        return new Column(new Header(context, "Tables"), new Column(rows),
                new Flexible(new Clickable(new Expanded(),
                        () -> setState(() -> context.getDatabase().createTable()), 2))
                                .setHorizontalAlignment(Alignment.STRETCH)
                                .setVerticalAlignment(Alignment.STRETCH))
                                        .setCrossAxisAlignment(Alignment.STRETCH);
    }

    /**
     * Handles key presses.
     * 
     * @param id The ID of the key event.
     * @param keyCode The key code of the key event.
     * @param keyChar The character of the key event.
     */
    @Override
    public boolean onKeyPress(int id, int keyCode, char keyChar) {
        if (keyCode == KeyEvent.VK_DELETE && id == KeyEvent.KEY_PRESSED) {
            setState(() -> {
                for (String table : selectedTables) {
                    context.getDatabase().deleteTable(table);
                }
                selectedTables.clear();
            });
            return true;
        }
        return false;
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
