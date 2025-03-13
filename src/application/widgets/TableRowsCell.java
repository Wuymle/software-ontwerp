package application.widgets;

import java.awt.Color;

import application.DatabaseAppContext;
import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.decoratedwidgets.DecoratedBox;
import clutter.inputwidgets.InputText;
import clutter.layoutwidgets.enums.Alignment;

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
        return new DecoratedBox(
                new InputText(context, context.getDatabase().getCell(context.getTable(), column, rowIndex), text -> {
                }))
                .setBorderColor(Color.black)
                .setHorizontalAlignment(Alignment.STRETCH);
    }
}
