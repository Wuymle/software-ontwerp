package application.screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import application.DatabaseAppContext;
import application.widgets.Header;
import application.widgets.ValueCell;
import clutter.abstractwidgets.Widget;
import clutter.core.ResizableGridController;
import clutter.core.ScrollController;
import clutter.core.ResizableGridController.ResizableGridSubscriber;
import clutter.decoratedwidgets.Text;
import clutter.inputwidgets.CheckBox;
import clutter.inputwidgets.Clickable;
import clutter.layoutwidgets.Center;
import clutter.layoutwidgets.Column;
import clutter.layoutwidgets.ConstrainedBox;
import clutter.layoutwidgets.GrowToFit;
import clutter.layoutwidgets.NullWidget;
import clutter.layoutwidgets.Padding;
import clutter.layoutwidgets.ResizableGrid;
import clutter.layoutwidgets.ScrollableView;
import database.Database.TableDataChangeListener;

public class TableRowsView extends DatabaseScreen
        implements TableDataChangeListener, ResizableGridSubscriber {
    String tableName;
    ArrayList<Integer> selectedRows = new ArrayList<Integer>();
    Consumer<String> onOpenDesignView;
    Consumer<Void> onClose;
    final ScrollController scrollController = new ScrollController(context);
    final ResizableGridController resizableGridController;

    public TableRowsView(DatabaseAppContext context, String tableName,
            ResizableGridController resizableGridController, Consumer<String> onOpenDesignView,
            Consumer<Void> onClose) {
        super(context);
        this.tableName = tableName;
        context.getDatabase().addTableDataChangeListener(tableName, this);
        this.onOpenDesignView = onOpenDesignView;
        this.onClose = onClose;
        this.resizableGridController = resizableGridController;
    }

    @Override
    public Widget build() {
        return new Column(new Header(context, ""),
                new ScrollableView(context, new GrowToFit(new Column(_buildGrid(),
                        new GrowToFit(new Clickable(new ConstrainedBox().setMinHeight(50),
                                () -> setState(() -> context.getDatabase().addRow(tableName)),
                                2)))),
                        scrollController));
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
                        if ((modifiers & KeyEvent.CTRL_DOWN_MASK) != 0) {
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
        items.add(new NullWidget());

        ArrayList<ArrayList<String>> rows = context.getDatabase().getRows(tableName);
        ArrayList<String> columns = context.getDatabase().getColumnNames(tableName);

        for (String column : columns) {
            items.add(new Padding(new Text(column).setFontSize(20)).all(5));
        }

        for (int i = 0; i < rows.size(); i++) {
            final int index = i;
            items.add(new Center(new CheckBox(context, (b) -> {
                if (b) {
                    selectedRows.add(index);
                } else {
                    selectedRows.remove(index);
                }
            })));
            for (int j = 0; j < columns.size(); j++) {
                final int columnIndex = j;
                items.add(new ValueCell(context,
                        context.getDatabase().getColumnType(tableName, columns.get(columnIndex)),
                        context.getDatabase().columnAllowBlank(tableName, columns.get(columnIndex)),
                        context.getDatabase().getCell(tableName, columns.get(columnIndex), i),
                        text -> context.getDatabase().updateCell(tableName,
                                columns.get(columnIndex), index, text),
                        text -> context.getDatabase().isValidValue(tableName,
                                columns.get(columnIndex), text)));
            }
        }

        return new ResizableGrid(context,
                resizableGridController,
                items.toArray(new Widget[0]));
    }

    @Override
    public void onTableDataChanged() {
        if (context.getDatabase().getTables().contains(tableName)
                && !context.getDatabase().getColumnNames(tableName).isEmpty())
            setState(() -> {
            });
        else {
            context.getDatabase().removeTableDataChangeListener(tableName, this);
            onClose.accept(null);
        }
    }

    @Override
    public void onColumnResize() {
        setState(() -> {});
    }
}
