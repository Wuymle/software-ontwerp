package application.widgets;

import java.awt.Color;
import java.util.ArrayList;
import java.util.stream.IntStream;

import application.DatabaseAppContext;
import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.decoratedwidgets.DecoratedBox;
import clutter.decoratedwidgets.Text;
import clutter.layoutwidgets.Column;
import clutter.layoutwidgets.enums.Alignment;

public class TableRowsColumn extends StatefulWidget<DatabaseAppContext> {
    String column;

    public TableRowsColumn(DatabaseAppContext context, String column) {
        super(context);
        this.column = column;
    }

    @Override
    public Widget build() {
        ArrayList<String> cells = context.getDatabase().getColumn(context.getTable(), column);
        ArrayList<Widget> cellWidgets = new ArrayList<>(IntStream.range(0, cells.size())
                .<Widget>mapToObj(rowIndex -> new ValueCell(context,
                        context.getDatabase().getColumnType(context.getTable(), column),
                        context.getDatabase().columnAllowBlank(context.getTable(), column),
                        context.getDatabase().getCell(context.getTable(), column, rowIndex),
                        text -> {
                            setState(() -> {
                                context.getDatabase().editCell(context.getTable(), column, rowIndex, text);
                            });
                        },
                        text -> context.getDatabase().isValidColumnValue(context.getTable(), column, text)))
                .toList());
        cellWidgets.add(0, new Text(column));
        return new DecoratedBox(new Column(cellWidgets).setCrossAxisAlignment(Alignment.STRETCH))
                .setBorderColor(Color.black)
                .setHorizontalAlignment(Alignment.STRETCH);
    }
}
