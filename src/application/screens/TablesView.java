package application.screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import application.DatabaseAppContext;
import application.resources.Style;
import application.widgets.Header;
import clutter.abstractwidgets.Widget;
import clutter.core.ResizableGridController;
import clutter.core.ScrollController;
import clutter.decoratedwidgets.Text;
import clutter.inputwidgets.CheckBox;
import clutter.inputwidgets.Clickable;
import clutter.inputwidgets.InputText;
import clutter.layoutwidgets.Box;
import clutter.layoutwidgets.Center;
import clutter.layoutwidgets.Column;
import clutter.layoutwidgets.ConstrainedBox;
import clutter.layoutwidgets.GrowToFit;
import clutter.layoutwidgets.NullWidget;
import clutter.layoutwidgets.Padding;
import clutter.layoutwidgets.ResizableGrid;
import clutter.layoutwidgets.ScrollableView;
import clutter.layoutwidgets.enums.Alignment;
import newdatabase.Database.TablesChangeListener;
import newdatabase.Table;

/**
 * A screen that represents the tables mode view.
 */
public class TablesView extends DatabaseScreen implements TablesChangeListener {
    List<Table> selectedTables = new ArrayList<Table>();
    Consumer<Table> onOpenTable;
    Consumer<Table> onOpenForm;
    private ScrollController scrollController = new ScrollController(context);
    private ResizableGridController resizableGridController;

    public TablesView(DatabaseAppContext context, ResizableGridController resizableGridController,
            Consumer<Table> onOpenTable, Consumer<Table> onOpenForm) {
        super(context);
        this.onOpenTable = onOpenTable;
        this.onOpenForm = onOpenForm;
        context.getDatabase().addTablesChangeListener(this);
        this.resizableGridController = resizableGridController;
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
                                () -> setState(() -> context.getDatabase().createTable()), 2)))),
                        scrollController)).setCrossAxisAlignment(Alignment.STRETCH);
    }

    private Widget _buildGrid() {
        List<Widget> items = new ArrayList<Widget>();
        items.addAll(List.of(new NullWidget().setDecoration(Style.decorationHeader),
                new Box(new Padding(new Text("Tablename").setFontColor(Style.white).setFontSize(20))
                        .all(5)).setVerticalAlignment(Alignment.CENTER)
                                .setDecoration(Style.decorationHeader)));
        for (Table table : context.getDatabase().getTables()) {
            items.addAll(List.of(new Center(new CheckBox(context, (b) -> {
                if (b) {
                    selectedTables.add(table);
                } else {
                    selectedTables.remove(table);
                }
            }, selectedTables.contains(table))), new Padding(
                    new Clickable(new GrowToFit(new Padding(new InputText(context, table.getName(),
                            text -> context.getDatabase().updateTableName(table, text))
                                    .setValidationFunction(text -> context.getDatabase()
                                            .allowUpdateTableName(table, text))).left(5).vertical(5)
                    // .setDecoration(Style.decorationInput)
                    ), () -> onOpenTable.accept(table), 2)).all(5)));
        }

        return new Box(
                new ResizableGrid(context, resizableGridController, items.toArray(new Widget[0])));
    }

    /**
     * Handles key presses.
     * 
     * @param id The ID of the key event.
     * @param keyCode The key code of the key event.
     * @param keyChar The character of the key event.
     */
    @Override
    public boolean onKeyPress(int id, int keyCode, char keyChar, int modifiers) {
        if (keyCode == KeyEvent.VK_DELETE && id == KeyEvent.KEY_PRESSED) {
            setState(() -> {
                selectedTables.forEach(context.getDatabase()::deleteTable);
                selectedTables.clear();
            });
            return true;
        }
        if (keyCode == KeyEvent.VK_F && id == KeyEvent.KEY_PRESSED) {
            if ((modifiers & KeyEvent.CTRL_DOWN_MASK) == 0)
                return false;

            var tableName = selectedTables.isEmpty() ? null : selectedTables.get(0);
            if (tableName == null) {
                return false;
            }
            onOpenForm.accept(tableName);
            return true;
        }
        return false;
    }

    @Override
    public void onTablesChanged() {
        setState(() -> {
        });
    }

    @Override
    public void close() {
        context.getDatabase().removeTablesChangeListener(this);
    }
}
