package application.screens;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import application.DatabaseAppContext;
import application.modes.DataBaseModes;
import application.widgets.TableRowsColumn;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;
import clutter.decoratedwidgets.DecoratedBox;
import clutter.inputwidgets.Clickable;
import clutter.layoutwidgets.Column;
import clutter.layoutwidgets.Expanded;
import clutter.layoutwidgets.Flexible;
import clutter.layoutwidgets.Row;
import clutter.layoutwidgets.SizedBox;
import clutter.layoutwidgets.enums.Alignment;
import clutter.widgetinterfaces.KeyEventHandler;
import clutter.widgetinterfaces.Screen;

public class TableRowsModeView extends Screen<DatabaseAppContext> implements KeyEventHandler {

    public TableRowsModeView(DatabaseAppContext context) {
        super(context);
    }

    @Override
    public Widget build() {
        ArrayList<String> columns = context.getDatabase().getColumnNames(context.getTable());
        List<Widget> columnWidgets = columns.stream()
                .<Widget>map(column -> new Flexible(new TableRowsColumn(context, column))
                        .setHorizontalAlignment(Alignment.STRETCH))
                .toList();
        return new Column(
                new Row(columnWidgets),
                new DecoratedBox(new SizedBox(null, new Dimension(0, 10))).setColor(Color.GREEN),
                new Flexible(
                        new Clickable(
                                new DecoratedBox(new Expanded(null)).setColor(Color.orange),
                                () -> {
                                    System.out.println("Adding row");
                                    setState(() -> {
                                        System.out.println("Adding row");
                                        context.getDatabase().addRow(context.getTable());
                                    });
                                }, 2)),

                new DecoratedBox(new SizedBox(null, new Dimension(100, 100))).setColor(Color.red))
                .setCrossAxisAlignment(Alignment.STRETCH);
    }

    @Override
    public void onKeyPress(int id, int keyCode, char keyChar) {
        switch (id) {
            case KeyEvent.KEY_PRESSED:
                switch (keyCode) {
                    case KeyEvent.VK_DELETE:
                        break;

                    case KeyEvent.VK_ESCAPE:
                        System.out.println("Switching to tables mode");
                        context.setDatabaseMode(DataBaseModes.TABLES_MODE);
                        break;

                    case KeyEvent.VK_ENTER:
                        context.setDatabaseMode(DataBaseModes.TABLE_DESIGN_MODE);
                        System.out.println("Switching to table design mode");
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
        System.out.println("Showing table rows mode" + context.getTable());
        System.out.println("Showing table rows mode" + context.getDatabase().getColumnNames(context.getTable()));
        setState(() -> {
        });
        context.getKeyEventController().setKeyHandler(this);
    }

    @Override
    public void onHide() {
        context.getKeyEventController().removeKeyHandler(this);
    }

}
