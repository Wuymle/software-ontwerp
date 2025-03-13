package application.screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import application.DatabaseAppContext;
import application.modes.DataBaseModes;
import application.widgets.TableDesignRow;
import clutter.abstractwidgets.Widget;
import clutter.inputwidgets.Clickable;
import clutter.layoutwidgets.Column;
import clutter.layoutwidgets.Expanded;
import clutter.layoutwidgets.Flexible;
import clutter.layoutwidgets.enums.Alignment;
import clutter.widgetinterfaces.KeyEventHandler;
import clutter.widgetinterfaces.Screen;
import database.ColumnType;

public class TableDesignModeView extends Screen<DatabaseAppContext> implements KeyEventHandler {
    List<String> selectedColumns = new ArrayList<String>();

    public TableDesignModeView(DatabaseAppContext context) {
        super(context);
    }

    @Override
    public Widget build() {
        List<Widget> rows = context.getDatabase().getColumnNames(context.getCurrentTable()).stream().map(
                columnName -> (Widget) new TableDesignRow(context, columnName, (name) -> {
                    selectedColumns.add(name);
                }, (name) -> {
                    selectedColumns.remove(name);
                })).toList();

        return new Column(new Column(rows).setCrossAxisAlignment(Alignment.STRETCH), new Flexible(
                new Clickable(
                        new Expanded(null),
                        () -> {
                            setState(
                                    () -> {
                                        context.getDatabase().addColumn(context.getCurrentTable(), "columnName",
                                                ColumnType.INTEGER, true);
                                    });
                        }, 2))
                .setHorizontalAlignment(Alignment.STRETCH)
                .setVerticalAlignment(Alignment.STRETCH)).setCrossAxisAlignment(Alignment.STRETCH);
    }

    @Override
    public void onKeyPress(int id, int keyCode, char keyChar) {
        switch (id) {
            case KeyEvent.KEY_PRESSED:
                switch (keyCode) {
                    case KeyEvent.VK_DELETE:
                        setState(() -> {
                            for (String column : selectedColumns) {
                                context.getDatabase().deleteColumn(context.getCurrentTable(), column);
                            }
                            selectedColumns.clear();
                        });
                        break;

                    case KeyEvent.VK_ESCAPE:
                        context.setDatabaseMode(DataBaseModes.TABLES_MODE);
                        break;

                    case KeyEvent.VK_ENTER:
                        context.setDatabaseMode(DataBaseModes.TABLE_ROWS_MODE);
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
        context.getKeyEventController().setKeyHandler(this);
    }

    @Override
    public void onHide() {
        context.getKeyEventController().removeKeyHandler(this);
    }
}
