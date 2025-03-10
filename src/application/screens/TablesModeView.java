package application.screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import application.DatabaseAppContext;
import application.widgets.TablesModeRow;
import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.inputwidgets.Button;
import clutter.inputwidgets.Clickable;
import clutter.layoutwidgets.Column;
import clutter.layoutwidgets.Expanded;
import clutter.layoutwidgets.Flexible;
import clutter.layoutwidgets.Padding;
import clutter.layoutwidgets.Row;
import clutter.layoutwidgets.enums.Alignment;
import clutter.widgetinterfaces.KeyEventHandler;

public class TablesModeView extends StatefulWidget<DatabaseAppContext> implements KeyEventHandler {
    List<String> selectedTables = new ArrayList<String>();

    public TablesModeView(DatabaseAppContext context) {
        super(context);
    }

    @Override
    public Widget build() {
        List<Widget> rows = new ArrayList<Widget>();

        for (String table : context.getDatabase().getTables()) {
            rows.add(new TablesModeRow(context, table, (tableName) -> {
                selectedTables.add(tableName);
            }, (tableName) -> {
                selectedTables.remove(tableName);
            }));
        }
        return new Column(
                new Row(new Padding(new Button(context, "Delete tables", () -> {
                    setState(() -> {
                        for (String table : selectedTables) {
                            context.getDatabase().deleteTable(table);
                        }
                        selectedTables.clear();
                    });
                })).all(5)),
                new Column(rows),
                new Flexible(
                        new Clickable(
                                new Expanded(null),
                                () -> {
                                    setState(
                                            () -> {
                                                context.getDatabase().createTable();
                                            });
                                }, 2))
                        .setHorizontalAlignment(Alignment.STRETCH)
                        .setVerticalAlignment(Alignment.STRETCH))
                .setCrossAxisAlignment(Alignment.STRETCH);
    }

    @Override
    public void onKeyPress(int id, int keyCode, char keyChar) {
        if (keyCode == KeyEvent.VK_DELETE && id == KeyEvent.KEY_PRESSED) {
            setState(() -> {
                for (String table : selectedTables) {
                    context.getDatabase().deleteTable(table);
                }
                selectedTables.clear();
            });
        }
    }
}
