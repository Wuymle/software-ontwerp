package application.screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import application.DatabaseAppContext;
import application.widgets.Header;
import application.widgets.TablesModeRow;
import clutter.abstractwidgets.Widget;
import clutter.inputwidgets.Clickable;
import clutter.layoutwidgets.Column;
import clutter.layoutwidgets.ConstrainedBox;
import clutter.layoutwidgets.ClampToFit;
import clutter.layoutwidgets.Flexible;
import clutter.layoutwidgets.ScrollableView;
import clutter.layoutwidgets.enums.Alignment;

/**
 * A screen that represents the tables mode view.
 */
public class TablesView extends DatabaseScreen {
    List<String> selectedTables = new ArrayList<String>();
    Consumer<String> onOpenTable;

    public TablesView(DatabaseAppContext context, Consumer<String> onOpenTable) {
        super(context);
        this.onOpenTable = onOpenTable;
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
            }, onOpenTable));
        }
        return new Column(new Header(context, "Tables"), new ScrollableView(context, new Column(
                new Column(rows),
                new Flexible(new Clickable(new ConstrainedBox(new ClampToFit()).setHeight(200),
                        () -> setState(() -> context.getDatabase().createTable()), 2))
                                .setHorizontalAlignment(Alignment.STRETCH)
                                .setVerticalAlignment(Alignment.STRETCH))))
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
}
