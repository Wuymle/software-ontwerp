package application.widgets;

import java.awt.Color;
import java.util.function.Consumer;
import application.DatabaseAppContext;
import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Decoration;
import clutter.inputwidgets.Button;
import clutter.inputwidgets.CheckBox;
import clutter.inputwidgets.InputText;
import clutter.layoutwidgets.Flexible;
import clutter.layoutwidgets.Padding;
import clutter.layoutwidgets.Row;
import clutter.layoutwidgets.enums.Alignment;

/**
 * A widget that represents a row in the table design mode.
 */
public class TableDesignRow extends StatefulWidget<DatabaseAppContext> {
    String tableName;
    String columnName;
    Consumer<String> onSelect;
    Consumer<String> onDeselect;

    /**
     * Constructor for the table design row widget.
     * 
     * @param context The context of the application.
     * @param columnName The name of the column.
     * @param onSelect The consumer to call when the column is selected.
     * @param onDeselect The consumer to call when the column is deselected.
     */
    public TableDesignRow(DatabaseAppContext context, String tableName, String columnName,
            Consumer<String> onSelect, Consumer<String> onDeselect) {
        super(context);
        this.tableName = tableName;
        this.columnName = columnName;
        this.onSelect = onSelect;
        this.onDeselect = onDeselect;
    }

    /**
     * Builds the table design row widget.
     * 
     * @return The table design row widget.
     */
    @Override
    public Widget build() {
        return new Row(new Padding(new CheckBox(context, checked -> {
            if (checked)
                onSelect.accept(columnName);
            else
                onDeselect.accept(columnName);
        })).horizontal(5), new InputText(context, columnName, text -> {
            context.getDatabase().updateColumnName(tableName, columnName, text);
            columnName = text;
        }).setColor(Color.black).setValidationFunction((String name) -> {
            return !(context.getDatabase().getColumnNames(tableName).contains(name)
                    && name != columnName);
        }), new Padding(new Button(context,
                context.getDatabase().getColumnType(tableName, columnName).name(), () -> {
                    setState(() -> {
                        context.getDatabase().toggleColumnType(tableName, columnName);
                    });
                })).horizontal(5).setVerticalAlignment(Alignment.CENTER),
                new CheckBox(context, context.getDatabase().columnAllowBlank(tableName, columnName),
                        allowBlank -> {
                            context.getDatabase().setColumnAllowBlank(tableName, columnName,
                                    allowBlank);
                        }).setValidationFunction(b -> {
                            return context.getDatabase().isValidAllowBlankValue(tableName,
                                    columnName, b);
                        }),
                new Flexible(new ValueCell(context,
                        context.getDatabase().getColumnType(tableName, columnName),
                        context.getDatabase().columnAllowBlank(tableName, columnName),
                        context.getDatabase().getDefaultColumnValue(tableName, columnName),
                        text -> {
                            setState(() -> {
                                context.getDatabase().updateDefaultColumnValue(tableName, columnName,
                                        text);
                            });
                        }, text -> {
                            return context.getDatabase().isValidValue(tableName, columnName,
                                    text);
                        })).setHorizontalAlignment(Alignment.STRETCH))
                                .setCrossAxisAlignment(Alignment.CENTER)
                                .setDecoration(new Decoration().setBorderColor(Color.black));
    }

}
