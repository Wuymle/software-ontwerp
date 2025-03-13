package application.widgets;

import java.awt.Color;
import java.util.function.Consumer;

import application.DatabaseAppContext;
import application.modes.DataBaseModes;
import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.decoratedwidgets.DecoratedBox;
import clutter.inputwidgets.CheckBox;
import clutter.inputwidgets.Clickable;
import clutter.inputwidgets.InputText;
import clutter.layoutwidgets.Flexible;
import clutter.layoutwidgets.Padding;
import clutter.layoutwidgets.Row;
import clutter.layoutwidgets.enums.Alignment;

public class TablesModeRow extends StatefulWidget<DatabaseAppContext> {
    String tableName;
    Consumer<String> onSelect;
    Consumer<String> onDeselect;

    public TablesModeRow(DatabaseAppContext context, String tableName, Consumer<String> onSelect,
            Consumer<String> onDeselect) {
        super(context);
        this.tableName = tableName;
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
                                onSelect.accept(tableName);
                            else
                                onDeselect.accept(tableName);
                        })).horizontal(5),
                        new Flexible(
                                new Clickable(
                                        new InputText(context, tableName, text -> {
                                            context.getDatabase().editTableName(tableName, text);
                                        })
                                            .setColor(Color.black)
                                            .setValidationFunction((String text) -> {
                                                return !(context.getDatabase().getTables().contains(text) && text != tableName);
                                            })
                                        , () -> {
                                            context.setCurrentTable(tableName);
                                            context.setDatabaseMode(DataBaseModes.TABLE_DESIGN_MODE);
                                        }, 2)))
                        .setCrossAxisAlignment(Alignment.STRETCH))
                // .horizontal(5))
                .setBorderColor(Color.black);
    }
}
