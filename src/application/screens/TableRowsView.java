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
import clutter.decoratedwidgets.Text;
import clutter.inputwidgets.CheckBox;
import clutter.inputwidgets.Clickable;
import clutter.layoutwidgets.Column;
import clutter.layoutwidgets.ConstrainedBox;
import clutter.layoutwidgets.GrowToFit;
import clutter.layoutwidgets.Padding;
import clutter.layoutwidgets.Row;
import clutter.layoutwidgets.ScrollableView;
import clutter.layoutwidgets.enums.Alignment;
import database.Database.TableDataChangeListener;

public class TableRowsView extends DatabaseScreen implements TableDataChangeListener {
    String tableName;
    ArrayList<Integer> selectedRows = new ArrayList<Integer>();
    Consumer<String> onOpenDesignView;

    public TableRowsView(DatabaseAppContext context, String tableName,
            Consumer<String> onOpenDesignView) {
        super(context);
        this.tableName = tableName;
        context.getDatabase().addTableDataChangeListener(tableName, this);
        this.onOpenDesignView = onOpenDesignView;
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

                ).setCrossAxisAlignment(Alignment.STRETCH))));
    }

    @Override
    public boolean onKeyPress(int id, int keyCode, char keyChar) {
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
                        onOpenDesignView.accept(tableName);
                        return true;
                    }

                    default:
                        return false;
                }
            default:
                return false;
        }
    }

    @Override
    public void onTableDataChanged() {
        setState(() -> {
        });
    }
}
