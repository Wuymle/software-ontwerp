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
                        new Padding(new CheckBox(context, b -> {
                            if (b)
                                onSelect.accept(columnName);
                            else
                                onDeselect.accept(columnName);
                        })).horizontal(5),
                        new InputText(context, columnName, text -> {
                            context.getDatabase().editColumnName(context.getTable(), columnName, text);
                            columnName = text;
                        }).setColor(Color.black).setValidationFunction((String name) -> {
                            return !(context.getDatabase().getColumnNames(context.getTable()).contains(name)
                                    && name != columnName);
                        }),
                        new Padding(
                                new Clickable(new Text(
                                        context.getDatabase()
                                                .getColumnType(context.getTable(), columnName)
                                                .name())
                                        .setFontSize(16),
                                        () -> {
                                            setState(() -> {
                                                context.getDatabase().toggleColumnType(context.getTable(), columnName);
                                            });
                                        }, 1))
                                .horizontal(5).setVerticalAlignment(Alignment.CENTER),
                        new CheckBox(context, allowBlank -> {
                            context.getDatabase().setColumnAllowBlank(context.getTable(), columnName, allowBlank);

                        
                            // FIXME: When setState is called on MOUSE_RELEASED, build() is called, then
                            // MOUSE_CLICKED is handled, then TEXTINPUT has no size
                        }),
                        new InputText(context, "", text -> {
                        }))
                        .setCrossAxisAlignment(Alignment.CENTER))
                // .horizontal(5))
                .setBorderColor(Color.black);
    }

}
