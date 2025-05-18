package application.widgets;

import java.awt.Color;
import java.util.Arrays;
import java.util.function.Consumer;
import application.DatabaseAppContext;
import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.AnimationController;
import clutter.core.Decoration;
import clutter.inputwidgets.CheckBox;
import clutter.inputwidgets.CycleButton;
import clutter.inputwidgets.InputText;
import clutter.layoutwidgets.GrowToFit;
import clutter.layoutwidgets.Padding;
import clutter.layoutwidgets.Row;
import clutter.layoutwidgets.enums.Alignment;
import database.ColumnType;

/**
 * A widget that represents a row in the table design mode.
 */
public class TableDesignViewRow extends StatefulWidget<DatabaseAppContext> {
    String tableName;
    String columnName;
    Consumer<String> onSelect;
    Consumer<String> onDeselect;
    AnimationController animationController = new AnimationController();
    private final String[] COLUMN_TYPES = { "STRING", "INTEGER", "BOOLEAN", "EMAIL" };

    /**
     * Constructor for the table design row widget.
     * 
     * @param context The context of the application.
     * @param columnName The name of the column.
     * @param onSelect The consumer to call when the column is selected.
     * @param onDeselect The consumer to call when the column is deselected.
     */
    public TableDesignViewRow(DatabaseAppContext context, String tableName, String columnName,
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
        return new Row(new Padding(new CheckBox(context,
                checked -> (checked ? onSelect : onDeselect).accept(columnName))).horizontal(5),
                new InputText(context, columnName, text -> {
                    context.getDatabase().updateColumnName(tableName, columnName, text);
                    columnName = text;
                }).setValidationFunction(
                        name -> !(context.getDatabase().getColumnNames(tableName).contains(name)
                                && name != columnName && !name.isEmpty())),
                new Padding(new CycleButton(context, this.COLUMN_TYPES, 
                                Arrays.asList(this.COLUMN_TYPES).indexOf(context.getDatabase().getColumnType(tableName, columnName).name()), 
                                type -> context.getDatabase().updateColumnType(tableName, columnName, ColumnType.valueOf(type)))
                        .setValidationFunction(type -> context.getDatabase()
                                .isValidColumnType(tableName, columnName, ColumnType.valueOf(type)))
                                        
                ).horizontal(5).setVerticalAlignment(Alignment.CENTER),
                new CheckBox(context, context.getDatabase().columnAllowBlank(tableName, columnName),
                        allowBlank -> context.getDatabase().setColumnAllowBlank(tableName,
                                columnName, allowBlank))
                                        .setValidationFunction(b -> context.getDatabase()
                                                .isValidAllowBlankValue(tableName, columnName, b)),
                new GrowToFit(new ValueCell(context,
                        context.getDatabase().getColumnType(tableName, columnName),
                        context.getDatabase().columnAllowBlank(tableName, columnName),
                        context.getDatabase().getDefaultColumnValue(tableName, columnName),
                        text -> context.getDatabase().updateDefaultColumnValue(tableName,
                                columnName, text),
                        text -> context.getDatabase().isValidValue(tableName, columnName, text)))
                                .setDecoration(new Decoration().setBorderColor(Color.black)))
                                        .setCrossAxisAlignment(Alignment.STRETCH);
    }
}
