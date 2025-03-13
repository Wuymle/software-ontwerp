package application.widgets;

import java.util.ArrayList;

import application.DatabaseAppContext;
import clutter.WidgetBuilder;
import clutter.abstractwidgets.Widget;

public class TableRowsRow extends WidgetBuilder<DatabaseAppContext> {

    public TableRowsRow(DatabaseAppContext context, ArrayList<String> columns) {
        super(context);
    }

    @Override
    public Widget build() {
        return null;
    }

}
