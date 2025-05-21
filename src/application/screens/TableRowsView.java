package application.screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import application.DatabaseAppContext;
import application.widgets.Header;
import application.widgets.TableRowsViewColumn;
import clutter.abstractwidgets.Widget;
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
import clutter.layoutwidgets.Padding;
import clutter.layoutwidgets.ResizableGrid;
import clutter.layoutwidgets.Row;
import clutter.layoutwidgets.ScrollableView;
import clutter.layoutwidgets.enums.Alignment;
import database.Database.TableDataChangeListener;

public class TableRowsView extends DatabaseScreen implements TableDataChangeListener {
    String tableName;
    ArrayList<Integer> selectedRows = new ArrayList<Integer>();
    Consumer<String> onOpenDesignView;
    Consumer<Void> onClose;
    final ScrollController scrollController = new ScrollController(context);

    public TableRowsView(DatabaseAppContext context, String tableName,
            Consumer<String> onOpenDesignView, Consumer<Void> onClose) {
        super(context);
        this.tableName = tableName;
        context.getDatabase().addTableDataChangeListener(tableName, this);
        this.onOpenDesignView = onOpenDesignView;
        this.onClose = onClose;
    }

    @Override
    public Widget build() {
        int rowAmount = context.getDatabase().getRows(tableName).size();
        List<Widget> selectWidgets = IntStream.range(0, rowAmount)
                .<Widget>mapToObj(idx -> new Padding(new CheckBox(context, b -> {
                    if (b)
                        selectedRows.add(idx);
                    else
                        selectedRows.remove(idx);
                })).vertical(3)).toList();
        List<String> columns = context.getDatabase().getColumnNames(tableName);
        List<Widget> columnWidgets = columns.stream()
                .<Widget>map(column -> new TableRowsViewColumn(context, tableName, column)
                        .setHorizontalAlignment(Alignment.STRETCH))
                .toList();

        return new Column(new Header(context, ""),
                new ScrollableView(context, new GrowToFit(new Column(
                        new Row(new Column(new Text("    "), new Column(selectWidgets)),
                                new Row(columnWidgets)),
                        new GrowToFit(new Clickable(new ConstrainedBox().setMinHeight(50),
                                () -> setState(() -> context.getDatabase().addRow(tableName)), 2))

                ).setCrossAxisAlignment(Alignment.STRETCH)), scrollController));
    }

    @Override
    public boolean onKeyPress(int id, int keyCode, char keyChar, int modifiers) {
        switch (id) {
            case KeyEvent.KEY_PRESSED:
                switch (keyCode) {
                    case KeyEvent.VK_DELETE:
                        setState(() -> {
                            context.getDatabase().deleteRows(tableName, selectedRows);
                            selectedRows.clear();
                        });
                        return true;

                    case KeyEvent.VK_ESCAPE:
                        return true;

                    case KeyEvent.VK_ENTER: {
                        if ((modifiers & KeyEvent.CTRL_DOWN_MASK) != 0){
                            onOpenDesignView.accept(tableName);
                            return true;
                        }
                        return false;
                    }

                    default:
                        return false;
                }
            default:
                return false;
        }
    }

    private Widget _buildGrid() {
        List<Widget> items = new ArrayList<Widget>();
        items.addAll(List.of(new Center(new CheckBox(context, (b) -> {
            if (b) {
                setState(() -> selectedTables.addAll(context.getDatabase().getTables()));
            } else {
                setState(() -> selectedTables.clear());
            }
        })), new Text("Table Name")~));
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

    @Override
    public void onTableDataChanged() {
        if (context.getDatabase().getTables().contains(tableName) && !context.getDatabase().getColumnNames(tableName).isEmpty())
            setState(() -> {
            });
        else {
            context.getDatabase().removeTableDataChangeListener(tableName, this);
            onClose.accept(null);
        }
    }
}
