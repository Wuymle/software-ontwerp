package application.widgets;

import application.DatabaseAppContext;
import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.inputwidgets.InputText;

public class TableRowsCell extends StatefulWidget<DatabaseAppContext> {
    String column;
    int rowIndex;

    public TableRowsCell(DatabaseAppContext context, String column, int rowIndex) {
        super(context);
        this.column = column;
        this.rowIndex = rowIndex;
    }

    @Override
    public Widget build() {
        return new InputText(context, null, null);
    }

}
