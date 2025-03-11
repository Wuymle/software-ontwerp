package application.screens;

import java.util.ArrayList;
import java.util.List;

import application.DatabaseAppContext;
import application.widgets.TableDesignRow;
import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.inputwidgets.Clickable;
import clutter.layoutwidgets.Column;
import clutter.layoutwidgets.Expanded;
import clutter.layoutwidgets.Flexible;
import clutter.layoutwidgets.enums.Alignment;
import database.ColumnType;

public class TableDesignModeView extends StatefulWidget<DatabaseAppContext> {
    List<String> selectedTables = new ArrayList<String>();

    public TableDesignModeView(DatabaseAppContext context) {
        super(context);
    }

    @Override
    public Widget build() {
        List<Widget> rows = context.getDatabase().getColumnNames(context.getCurrentTable()).stream().map(
                columnName -> (Widget) new TableDesignRow(context, columnName, (name) -> {
                    selectedTables.add(name);
                }, (name) -> {
                    selectedTables.remove(name);
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
}
