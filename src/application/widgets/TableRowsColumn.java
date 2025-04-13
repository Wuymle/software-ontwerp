package application.widgets;

import java.awt.Color;
import java.util.ArrayList;
import java.util.stream.IntStream;
import application.DatabaseAppContext;
import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Decoration;
import clutter.decoratedwidgets.Text;
import clutter.layoutwidgets.Column;
import clutter.layoutwidgets.enums.Alignment;

/**
 * A widget that represents a column in the table rows mode.
 */
public class TableRowsColumn extends StatefulWidget<DatabaseAppContext> {
    String tableName;
    String column;

    /**
     * Constructor for the table rows column widget.
     * 
     * @param context The context of the application.
     * @param column The name of the column.
     */
    public TableRowsColumn(DatabaseAppContext context, String tableName, String column) {
        super(context);
        this.tableName = tableName;
        this.column = column;
    }

    /**
     * Builds the table rows column widget.
     * 
     * @return The table rows column widget.
     */
    @Override
    public Widget build() {
        ArrayList<String> cells = context.getDatabase().getColumn(tableName, column);
        ArrayList<Widget> cellWidgets = new ArrayList<>(IntStream.range(0, cells.size())
                .<Widget>mapToObj(rowIndex -> new ValueCell(context,
                        context.getDatabase().getColumnType(tableName, column),
                        context.getDatabase().columnAllowBlank(tableName, column),
                        context.getDatabase().getCell(tableName, column, rowIndex), text -> {
                            setState(() -> {
                                context.getDatabase().editCell(tableName, column, rowIndex, text);
                            });
                        },
                        text -> context.getDatabase().isValidColumnValue(tableName, column, text)))
                .toList());
        cellWidgets.add(0, new Text(column));
        return new Column(cellWidgets).setCrossAxisAlignment(Alignment.STRETCH)
                .setDecoration(new Decoration().setBorderColor(Color.black));
    }
}
