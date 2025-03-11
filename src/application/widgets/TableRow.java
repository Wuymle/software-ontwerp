package application.widgets;

import java.awt.Color;

import clutter.WidgetBuilder;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.decoratedwidgets.DecoratedBox;
import clutter.decoratedwidgets.Text;
import clutter.layoutwidgets.Padding;
import clutter.layoutwidgets.Row;;

public class TableRow extends WidgetBuilder {
    private String[] row;

    public TableRow(Context context, String[] row) {
        super(context);
        this.row = row;
    }

    @Override
    public Widget build(Context context) {
        Widget[] cells = new Widget[row.length];
        for (int i = 0; i < row.length; i++) {
            cells[i] = new DecoratedBox(new Padding(new Text(row[i])).all(5)).setBorderColor(Color.black)
                    .setColor(Color.white);
        }
        return new Row(cells);
    }
}
