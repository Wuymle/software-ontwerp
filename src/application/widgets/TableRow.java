package application.widgets;

import java.awt.Color;

import clutter.WidgetBuilder;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.decoratedwidgets.DecoratedBox;
import clutter.decoratedwidgets.Text;
import clutter.layoutwidgets.Flexible;
import clutter.layoutwidgets.Padding;
import clutter.layoutwidgets.Row;
import clutter.layoutwidgets.enums.Alignment;;

public class TableRow extends WidgetBuilder<Context> {
    private String[] row;

    public TableRow(Context context, String[] row) {
        super(context);
        this.row = row;
    }

    @Override
    public Widget build() {
        Widget[] cells = new Widget[row.length];
        for (int i = 0; i < row.length; i++) {
            cells[i] = new Flexible(new DecoratedBox(new Padding(new Text(row[i])).all(5)).setBorderColor(Color.black)
                    .setColor(Color.white)).setHorizontalAlignment(Alignment.STRETCH);
        }
        return new Row(cells);
    }
}
