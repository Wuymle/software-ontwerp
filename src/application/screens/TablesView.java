package application.screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import application.DatabaseAppContext;
import application.widgets.Header;
import application.widgets.TablesViewRow;
import clutter.abstractwidgets.Widget;
import clutter.core.Direction;
import clutter.core.Orientation;
import clutter.core.ResizableGridController;
import clutter.core.ScrollController;
import clutter.decoratedwidgets.Text;
import clutter.inputwidgets.CheckBox;
import clutter.inputwidgets.Clickable;
import clutter.inputwidgets.InputText;
import clutter.layoutwidgets.Center;
import clutter.layoutwidgets.Column;
import clutter.layoutwidgets.ConstrainedBox;
import clutter.layoutwidgets.GrowToFit;
import clutter.layoutwidgets.ResizableGrid;
import clutter.layoutwidgets.ScrollableView;
import clutter.layoutwidgets.enums.Alignment;
import database.Database.TableNameChangeListener;

/**
 * A screen that represents the tables mode view.
 */
public class TablesView extends DatabaseScreen implements TableNameChangeListener {
    List<String> selectedTables = new ArrayList<String>();
    Consumer<String> onOpenTable;
    private ScrollController scrollController = new ScrollController(context);

    public TablesView(DatabaseAppContext context, Consumer<String> onOpenTable) {
        super(context);
        this.onOpenTable = onOpenTable;
        context.getDatabase().addTableNameChangeListener(this);
    }

    /**
     * Builds the tables mode view.
     * 
     * @return The tables mode view.
     */
    @Override
    public Widget build() {
        return new Column(new Header(context, "Tables"),
                new ScrollableView(context, new GrowToFit(new Column(_buildGrid(),
                        new GrowToFit(new Clickable(new ConstrainedBox().setMinHeight(50),
                                () -> setState(() -> context.getDatabase().createTable()), 2)))), scrollController))
                                        .setCrossAxisAlignment(Alignment.STRETCH);
    }

    private Widget _buildGrid() {
        List<Widget> items = new ArrayList<Widget>();
        items.addAll(List.of(new Center(new CheckBox(context, (b) -> {
            if (b) {
                setState(() -> selectedTables.addAll(context.getDatabase().getTables()));
            } else {
                setState(() -> selectedTables.clear());
            }
        })), new Text("Table Name")));
        for (String table : context.getDatabase().getTables()) {
            items.addAll(List.of(new Center(new CheckBox(context, (b) -> {
                if (b) {
                    selectedTables.add(table);
                } else {
                    selectedTables.remove(table);
                }
            })), new Clickable(
                    new InputText(context, table,
                            text -> context.getDatabase().updateTableName(table, text))
                                    .setValidationFunction((String text) -> text.equals(table)
                                            || !(context.getDatabase().getTables().contains(text))),
                    () -> onOpenTable.accept(table), 2)));
        }

        return new ResizableGrid(context, new ResizableGridController(context, 2, 5),
                items.toArray(new Widget[0]));
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
        System.out.println("Table name changed");
        setState(() -> {
        });
    }
}
