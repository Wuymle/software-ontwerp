package application.widgets;

import java.awt.Color;
import java.util.function.Consumer;
import application.DatabaseAppContext;
import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Decoration;
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
    Consumer<String> onOpenTable;

    public TablesModeRow(DatabaseAppContext context, String tableName, Consumer<String> onSelect,
            Consumer<String> onDeselect, Consumer<String> onOpenTable) {
        super(context);
        this.tableName = tableName;
        this.onSelect = onSelect;
        this.onDeselect = onDeselect;
        this.onOpenTable = onOpenTable;
    }

    @Override
    public Widget build() {
        return new Row(new Padding(new CheckBox(context, (b) -> {
            if (b)
                onSelect.accept(tableName);
            else
                onDeselect.accept(tableName);
        })).horizontal(5), new Flexible(new Clickable(new InputText(context, tableName, text -> {
            context.getDatabase().updateTableName(tableName, text);
        }).setColor(Color.black).setValidationFunction((String text) -> (text.equals(tableName)
                || !(context.getDatabase().getTables().contains(text)))), () -> {
                    onOpenTable.accept(tableName);
                }, 2))).setCrossAxisAlignment(Alignment.STRETCH)
                        .setDecoration(new Decoration().setBorderColor(Color.black));
    }
}
