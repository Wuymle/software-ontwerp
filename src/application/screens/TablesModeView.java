package application.screens;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import application.DatabaseAppContext;
import application.widgets.TablesModeRow;
import clutter.WidgetBuilder;
import clutter.abstractwidgets.Widget;
import clutter.decoratedwidgets.DecoratedBox;
import clutter.inputwidgets.Clickable;
import clutter.layoutwidgets.Column;
import clutter.layoutwidgets.ConstrainedBox;
import clutter.layoutwidgets.Flexible;
import clutter.layoutwidgets.enums.Alignment;

public class TablesModeView extends WidgetBuilder<DatabaseAppContext> {

    public TablesModeView(DatabaseAppContext context) {
        super(context);
    }

    @Override
    public Widget build() {
        // final List<String> dummyTables = context.getTables(5);
        List<Widget> rows = new ArrayList<Widget>();
        // for (int i = 0; i < dummyTables.size(); i++) {
        // rows[i] = new TablesModeRow(context, dummyTables.get(i));
        // }

        for (String table : context.getDatabase().getTables()) {
            rows.add(new TablesModeRow(context, table));
        }

        return new DecoratedBox(
                new Column(
                        new Column(rows.toArray(new Widget[0])),
                        new Flexible(
                                new Clickable(
                                        new DecoratedBox(
                                                new ConstrainedBox(null).setWidth(100).setHeight(100))
                                                .setColor(Color.red),
                                        () -> {
                                            System.out.println("add new table");
                                        }, 1))
                                .setHorizontalAlignment(Alignment.STRETCH)
                                .setVerticalAlignment(Alignment.STRETCH))
                        .setCrossAxisAlignment(Alignment.STRETCH))
                .setColor(Color.white);
    }
}
