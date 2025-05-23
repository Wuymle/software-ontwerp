package application.screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import application.DatabaseAppContext;
import application.resources.Style;
import application.widgets.Header;
import application.widgets.ValueCell;
import clutter.abstractwidgets.Widget;
import clutter.core.ResizableGridController;
import clutter.core.ScrollController;
import clutter.core.ResizableGridController.ResizableGridSubscriber;
import clutter.decoratedwidgets.Text;
import clutter.inputwidgets.CheckBox;
import clutter.inputwidgets.Clickable;
import clutter.layoutwidgets.Box;
import clutter.layoutwidgets.Center;
import clutter.layoutwidgets.Column;
import clutter.layoutwidgets.ConstrainedBox;
import clutter.layoutwidgets.GrowToFit;
import clutter.layoutwidgets.NullWidget;
import clutter.layoutwidgets.Padding;
import clutter.layoutwidgets.ResizableGrid;
import clutter.layoutwidgets.ScrollableView;
import newdatabase.Row;
import newdatabase.Table;
import newdatabase.Table.TableRowsChangeListener;

public class TableRowsView extends DatabaseScreen
        implements ResizableGridSubscriber, TableRowsChangeListener {
    Table table;
    Set<Row> selectedRows = new HashSet<Row>();
    Consumer<Table> onOpenDesignView;
    Consumer<Void> onClose;
    final ScrollController scrollController = new ScrollController(context);
    final ResizableGridController resizableGridController;

    public TableRowsView(DatabaseAppContext context, Table tableName,
            ResizableGridController resizableGridController, Consumer<Table> onOpenDesignView,
            Consumer<Void> onClose) {
        super(context);
        this.table = tableName;
        this.onOpenDesignView = onOpenDesignView;
        this.onClose = onClose;
        this.resizableGridController = resizableGridController;
        table.addTableRowsChangeListener(this);
    }

    @Override
    public Widget build() {
        return new Column(new Header(context, table.getName() + ": Rows mode"),
                new ScrollableView(context, new GrowToFit(new Column(_buildGrid(), new GrowToFit(
                        new Clickable(new ConstrainedBox().setMinHeight(50), () -> setState(() -> {
                            if (table.getColumns().size() > 0)
                                table.createRow();
                        }), 2)))), scrollController));
    }

    @Override
    public boolean onKeyPress(int id, int keyCode, char keyChar, int modifiers) {
        switch (id) {
            case KeyEvent.KEY_PRESSED:
                switch (keyCode) {
                    case KeyEvent.VK_DELETE:
                        setState(() -> {
                            table.deleteRows(selectedRows);
                            selectedRows.clear();
                        });
                        return true;

                    case KeyEvent.VK_ESCAPE:
                        return true;

                    case KeyEvent.VK_ENTER: {
                        if ((modifiers & KeyEvent.CTRL_DOWN_MASK) != 0) {
                            onOpenDesignView.accept(table);
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
        if (table.getColumns().isEmpty())
            return new Text("No columns in this table");
        List<Widget> items = new ArrayList<Widget>();
        items.add(new NullWidget().setDecoration(Style.decorationHeader));


        for (newdatabase.Column column : table.getColumns()) {
            items.add(new Padding(
                    new Text(column.getName()).setFontSize(20).setFontColor(Style.white)).all(5)
                            .setDecoration(Style.decorationHeader));
        }

        for (Row row : table.getRows()) {
            items.add(new Center(new CheckBox(context, (b) -> {
                if (b) {
                    selectedRows.add(row);
                } else {
                    selectedRows.remove(row);
                }
            }, selectedRows.contains(row))));
            for (newdatabase.Column column : table.getColumns()) {
                items.add(new Padding(new GrowToFit(new ValueCell(context, column,
                        row.getCell(column).getValue(), text -> row.updateCellValue(column, text),
                        text -> row.allowUpdateCellValue(column, text)))).right(5).vertical(5));
            }
        }

        return new Box(
                new ResizableGrid(context, resizableGridController, items.toArray(new Widget[0]))
                        .setDecoration(Style.decorationPanel))
                                .setDecoration(Style.decorationPanel2);
    }

    @Override
    public void onColumnResize() {
        setState(() -> {
        });
    }

    @Override
    public void onTableRowsChanged() {
        if (context.getDatabase().getTables().contains(table))
            setState(() -> {
            });
        else {
            close();
        }
    }

    @Override
    public void close() {
        table.removeTableRowsChangeListener(this);
        onClose.accept(null);
    }
}
