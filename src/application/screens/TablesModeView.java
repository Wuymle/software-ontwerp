package application.screens;

import java.awt.Color;
import java.util.List;

import application.DatabaseAppContext;
import application.widgets.TablesModeRow;
import clutter.WidgetBuilder;
import clutter.abstractwidgets.Widget;
import clutter.decoratedwidgets.DecoratedBox;
import clutter.layoutwidgets.Column;

public class TablesModeView extends WidgetBuilder<DatabaseAppContext> {

    public TablesModeView(DatabaseAppContext context) {
        super(context);
    }

    @Override
    public Widget build() {
        final List<String> dummyTables = context.getTables(5);
        Widget[] rows = new Widget[dummyTables.size()];
        for (int i = 0; i < dummyTables.size(); i++) {
            rows[i] = new TablesModeRow(context, dummyTables.get(i));
        }
        return new DecoratedBox(new Column(rows)).setColor(Color.white);
    }
}
