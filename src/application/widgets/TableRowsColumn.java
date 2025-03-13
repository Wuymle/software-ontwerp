package application.widgets;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import application.DatabaseAppContext;
import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.layoutwidgets.Row;

public class TableRowsColumn extends StatefulWidget<DatabaseAppContext> {
    String column;

    public TableRowsColumn(DatabaseAppContext context, String column) {
        super(context);
        this.column = column;
    }

    @Override
    public Widget build() {
        ArrayList<String> cells = context.getDatabase().getColumn(context.getTable(), column);
        List<Widget> cellWidgets = IntStream.range(0, cells.size())
                .<Widget>mapToObj(rowIndex -> new TableRowsCell(context, column, rowIndex)).toList();
        return new Row(cellWidgets);
    }
}
