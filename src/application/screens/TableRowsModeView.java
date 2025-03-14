package application.screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import application.DatabaseAppContext;
import application.modes.DataBaseModes;
import application.widgets.TableRowsColumn;
import clutter.abstractwidgets.Widget;
import clutter.decoratedwidgets.Text;
import clutter.inputwidgets.CheckBox;
import clutter.inputwidgets.Clickable;
import clutter.layoutwidgets.Column;
import clutter.layoutwidgets.Expanded;
import clutter.layoutwidgets.Flexible;
import clutter.layoutwidgets.Padding;
import clutter.layoutwidgets.Row;
import clutter.layoutwidgets.enums.Alignment;
import clutter.widgetinterfaces.KeyEventHandler;
import clutter.widgetinterfaces.Screen;

public class TableRowsModeView extends Screen<DatabaseAppContext> implements KeyEventHandler {
    List<Integer> selectedRows = new ArrayList<Integer>();

    public TableRowsModeView(DatabaseAppContext context) {
        super(context);
    }

    @Override
    public Widget build() {
        int rowAmount = context.getDatabase().getRows(context.getTable()).size();
        List<Widget> selectWidgets = IntStream.range(0, rowAmount)
                .<Widget>mapToObj(idx -> new Padding(new CheckBox(context, b -> {
                    if (b)
                        selectedRows.add(idx);
                    else
                        selectedRows.remove(idx);
                })).vertical(3)).toList();
        List<String> columns = context.getDatabase().getColumnNames(context.getTable());
        List<Widget> columnWidgets = columns.stream()
                .<Widget>map(column -> new Flexible(
                        new TableRowsColumn(context, column).setHorizontalAlignment(Alignment.STRETCH))
                        .setHorizontalAlignment(Alignment.STRETCH))
                .toList();

        return new Column(new Row(new Column(new Text("    "), new Column(selectWidgets)), new Row(columnWidgets)),
                new Flexible(new Clickable(new Expanded(null), () -> {
                    setState(() -> {
                        context.getDatabase().addRow(context.getTable());
                    });
                }, 2))

        ).setCrossAxisAlignment(Alignment.STRETCH);
    }

    @Override
    public void onKeyPress(int id, int keyCode, char keyChar) {
        switch (id) {
            case KeyEvent.KEY_PRESSED:
                switch (keyCode) {
                    case KeyEvent.VK_DELETE:
                        setState(
                                () -> {
                                    while (selectedRows.size() > 0) {
                                        System.out
                                                .println("ROWS: " + context.getDatabase().getRows(context.getTable()));
                                        System.out.println("REMOVING ROW: " + selectedRows.get(0));
                                        context.getDatabase().deleteRow(context.getTable(), selectedRows.remove(0));
                                        System.out.println("Selectedrowsbefore: " + selectedRows);
                                        selectedRows = new ArrayList<>(selectedRows.stream().map(i -> i - 1).toList());
                                        System.out.println("Selectedrowsafter: " + selectedRows);
                                    }
                                });
                        break;

                    case KeyEvent.VK_ESCAPE:
                        context.setDatabaseMode(DataBaseModes.TABLES_MODE);
                        break;

                    case KeyEvent.VK_ENTER:
                        context.setDatabaseMode(DataBaseModes.TABLE_DESIGN_MODE);
                        if ((keyCode & KeyEvent.CTRL_DOWN_MASK) != 0) {
                        }
                        break;

                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onShow() {
        setState(() -> {
        });
        context.getKeyEventController().setKeyHandler(this);
    }

    @Override
    public void onHide() {
        context.getKeyEventController().removeKeyHandler(this);
    }

}
