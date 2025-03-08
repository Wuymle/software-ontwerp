package application.screens;

import java.awt.Color;
import java.util.List;

import application.DatabaseAppContext;
import application.widgets.TableRow;
import clutter.WidgetBuilder;
import clutter.abstractwidgets.Widget;
import clutter.decoratedwidgets.DecoratedBox;
import clutter.layoutwidgets.Column;

public class TableRowsModeView extends WidgetBuilder<DatabaseAppContext> {

    public TableRowsModeView(DatabaseAppContext context) {
        super(context);
    }

    @Override
    public Widget build() {
        final List<String[]> dummyRows = context.getRows(5);
        Widget[] rows = new Widget[dummyRows.size()];
        for (int i = 0; i < dummyRows.size(); i++) {
            rows[i] = new TableRow(context, dummyRows.get(i));
        }
        return new DecoratedBox(new Column(rows)).setColor(Color.white);
    }

}
