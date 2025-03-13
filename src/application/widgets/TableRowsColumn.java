package application.widgets;

import java.util.ArrayList;
import java.util.stream.IntStream;

import application.DatabaseAppContext;
import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
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
                .<Widget>mapToObj(rowIndex -> new TableRowsCell(context, column, rowIndex)).toList());
        cellWidgets.add(0, new Text(column));
        return new Column(cellWidgets).setCrossAxisAlignment(Alignment.STRETCH);
    }
}
