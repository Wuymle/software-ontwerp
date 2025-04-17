package application.screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import application.DatabaseAppContext;
import application.widgets.Header;
import application.widgets.TablesViewRow;
import clutter.abstractwidgets.Widget;
import clutter.inputwidgets.Clickable;
import clutter.layoutwidgets.Column;
import clutter.layoutwidgets.ConstrainedBox;
import clutter.layoutwidgets.GrowToFit;
import clutter.layoutwidgets.ScrollableView;
import clutter.layoutwidgets.enums.Alignment;
import database.Database.TableNameChangeListener;

/**
 * A screen that represents the tables mode view.
 */
public class TablesView extends DatabaseScreen implements TableNameChangeListener {
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
            rows.add(new TablesViewRow(context, table, (tableName) -> selectedTables.add(tableName),
                    (tableName) -> selectedTables.remove(tableName), onOpenTable));
        }
        return new Column(new Header(context, "Tables"), new ScrollableView(context,
                new GrowToFit(new Column(new Column(rows).setCrossAxisAlignment(Alignment.STRETCH),
                        new GrowToFit(new Clickable(new ConstrainedBox().setMinHeight(50),
                                () -> setState(() -> context.getDatabase().createTable()), 2))))))
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

    @Override
    public void onTableNameChanged() {
        setState(() -> {});
    }
}
