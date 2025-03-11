package application.widgets;

import java.awt.Color;
import java.util.function.Consumer;

import application.DatabaseAppContext;
import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.decoratedwidgets.DecoratedBox;
import clutter.decoratedwidgets.Text;
import clutter.inputwidgets.CheckBox;
import clutter.inputwidgets.Clickable;
import clutter.inputwidgets.InputText;
import clutter.layoutwidgets.Center;
import clutter.layoutwidgets.Flexible;
import clutter.layoutwidgets.Padding;
import clutter.layoutwidgets.Row;
import clutter.layoutwidgets.enums.Alignment;

public class TableDesignRow extends StatefulWidget<DatabaseAppContext> {
    String columnName;
    Consumer<String> onSelect;
    Consumer<String> onDeselect;

    public TableDesignRow(DatabaseAppContext context, String columnName, Consumer<String> onSelect,
            Consumer<String> onDeselect) {
        super(context);
        this.columnName = columnName;
        this.onSelect = onSelect;
        this.onDeselect = onDeselect;

    }

    @Override
    public Widget build() {
        return new DecoratedBox(
                // new Padding(
                new Row(
                        new Padding(new CheckBox(context, (b) -> {
                            if (b)
                                onSelect.accept(columnName);
                            else
                                onDeselect.accept(columnName);
                        })).horizontal(5),
                        new InputText(context, columnName, text -> {
                            context.getDatabase().editColumnName(context.getCurrentTable(), columnName, text);
                        }).setColor(Color.black),
                        new Padding(
                                new Clickable(new Text(
                                        context.getDatabase()
                                                .getColumnType(context.getCurrentTable(), columnName)
                                                .name())
                                        .setFontSize(16),
                                        null, 1))
                                .horizontal(5).setVerticalAlignment(Alignment.CENTER),
                        new Center(new CheckBox(context, (b) -> {
                            setState(() -> {
                            });
                        })))
                        .setCrossAxisAlignment(Alignment.CENTER))
                // .horizontal(5))
                .setBorderColor(Color.black);
    }

}
