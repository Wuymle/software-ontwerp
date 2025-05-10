package application.widgets;

import java.util.function.Consumer;
import application.DatabaseAppContext;
import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.inputwidgets.CheckBox;
import clutter.inputwidgets.Clickable;
import clutter.inputwidgets.InputText;
import clutter.layoutwidgets.GrowToFit;
import clutter.layoutwidgets.Padding;
import clutter.layoutwidgets.Row;
import clutter.layoutwidgets.enums.Alignment;

public class TablesViewRow extends StatefulWidget<DatabaseAppContext> {
    String tableName;
    Consumer<String> onSelect;
    Consumer<String> onDeselect;
    Consumer<String> onOpenTable;

    public TablesViewRow(DatabaseAppContext context, String tableName, Consumer<String> onSelect,
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
        })).horizontal(5), new GrowToFit(new Clickable(
                new InputText(context, tableName,
                        text -> context.getDatabase().updateTableName(tableName, text))
                                .setValidationFunction((String text) -> text.equals(tableName)
                                        || !(context.getDatabase().getTables().contains(text))),
                () -> onOpenTable.accept(tableName), 2))).setCrossAxisAlignment(Alignment.STRETCH);
    }
}
