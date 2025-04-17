package application.screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import application.DatabaseAppContext;
import application.widgets.TableRowsColumn;
import clutter.abstractwidgets.Widget;
import clutter.decoratedwidgets.Text;
import clutter.inputwidgets.CheckBox;
import clutter.inputwidgets.Clickable;
import clutter.layoutwidgets.ClampToFit;
import clutter.layoutwidgets.Column;
import clutter.layoutwidgets.Flexible;
import clutter.layoutwidgets.Padding;
import clutter.layoutwidgets.Row;
import clutter.layoutwidgets.enums.Alignment;

public class TableRowsView extends DatabaseScreen {
    String tableName;
    ArrayList<Integer> selectedRows = new ArrayList<Integer>();

    public TableRowsView(DatabaseAppContext context, String tableName) {
        super(context);
        this.tableName = tableName;
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
                .<Widget>map(column -> new Flexible(new TableRowsColumn(context, tableName, column)
                        .setHorizontalAlignment(Alignment.STRETCH))
                                .setHorizontalAlignment(Alignment.STRETCH))
                .toList();

        return new Column(
                new Row(new Column(new Text("    "), new Column(selectWidgets)),
                        new Row(columnWidgets)),
                new Flexible(new Clickable(new ClampToFit(null), () -> {
                    setState(() -> {
                        context.getDatabase().addRow(tableName);
                    });
                }, 2))

        ).setCrossAxisAlignment(Alignment.STRETCH);
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

                    case KeyEvent.VK_ENTER:
                        return true;

                    default:
                        return false;
                }
            default:
                return false;
        }
    }
}
