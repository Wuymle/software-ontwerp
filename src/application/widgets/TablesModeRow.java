package application.widgets;

import java.awt.Color;

import clutter.WidgetBuilder;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.decoratedwidgets.DecoratedBox;
import clutter.inputwidgets.IconButton;
import clutter.inputwidgets.InputText;
import clutter.layoutwidgets.Center;
import clutter.layoutwidgets.Flexible;
import clutter.layoutwidgets.Padding;
import clutter.layoutwidgets.Row;
import clutter.layoutwidgets.enums.Alignment;

public class TablesModeRow extends WidgetBuilder<Context> {
    String tableName;

    public TablesModeRow(Context context, String tableName) {
        super(context);
        this.tableName = tableName;

    }

    @Override
    public Widget build() {
        return new DecoratedBox(
                new Row(
                        new Flexible(
                                new Padding(new InputText(context, tableName, text -> {
                                    tableName = text;
                                })).horizontal(5)),
                        new Center(
                                new IconButton(context, "\uf026", () -> {
                                })))
                        .setCrossAxisAlignment(Alignment.STRETCH))
                .setBorderColor(Color.black);
    }
}
