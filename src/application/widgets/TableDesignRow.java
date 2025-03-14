package application.widgets;

import java.awt.Color;
import java.util.function.Consumer;

import application.DatabaseAppContext;
import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.decoratedwidgets.DecoratedBox;
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
        String columnName;
        Consumer<String> onSelect;
        Consumer<String> onDeselect;

        /**
         * Constructor for the table design row widget.
         * 
         * @param context    The context of the application.
         * @param columnName The name of the column.
         * @param onSelect   The consumer to call when the column is selected.
         * @param onDeselect The consumer to call when the column is deselected.
         */
        public TableDesignRow(DatabaseAppContext context, String columnName, Consumer<String> onSelect,
                        Consumer<String> onDeselect) {
                super(context);
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
                return new DecoratedBox(
                                // new Padding(
                                new Row(
                                                new Padding(
                                                                new CheckBox(context, b -> {
                                                                        System.out.println("CLICKED");
                                                                        if (b)
                                                                                onSelect.accept(columnName);
                                                                        else
                                                                                onDeselect.accept(columnName);
                                                                })).horizontal(5),
                                                new InputText(context, columnName, text -> {
                                                        context.getDatabase().editColumnName(context.getTable(),
                                                                        columnName, text);
                                                        columnName = text;
                                                })
                                                                .setColor(Color.black)
                                                                .setValidationFunction((String name) -> {
                                                                        return !(context.getDatabase()
                                                                                        .getColumnNames(context
                                                                                                        .getTable())
                                                                                        .contains(name)
                                                                                        && name != columnName);
                                                                }),
                                                new Padding(
                                                                new Button(context,
                                                                                context.getDatabase()
                                                                                                .getColumnType(context
                                                                                                                .getTable(),
                                                                                                                columnName)
                                                                                                .name(),
                                                                                () -> {
                                                                                        setState(() -> {
                                                                                                context.getDatabase()
                                                                                                                .toggleColumnType(
                                                                                                                                context.getTable(),
                                                                                                                                columnName);
                                                                                        });
                                                                                }))
                                                                .horizontal(5).setVerticalAlignment(Alignment.CENTER),
                                                new CheckBox(context,
                                                                context.getDatabase().columnAllowBlank(
                                                                                context.getTable(), columnName),
                                                                allowBlank -> {
                                                                        context.getDatabase().setColumnAllowBlank(
                                                                                        context.getTable(), columnName,
                                                                                        allowBlank);
                                                                }).setValidationFunction(b -> {
                                                                        // System.out.println("isValid" +
                                                                        // context.getDatabase()
                                                                        // .getValidDefaultValue(context.getTable(),
                                                                        // columnName));
                                                                        // return
                                                                        // context.getDatabase().getValidDefaultValue(context.getTable(),
                                                                        // columnName);
                                                                        return context.getDatabase()
                                                                                        .isValidAllowBlankValue(context
                                                                                                        .getTable(),
                                                                                                        columnName,
                                                                                                        b);
                                                                }),
                                                new Flexible(
                                                                new ValueCell(
                                                                                context,
                                                                                context.getDatabase().getColumnType(
                                                                                                context.getTable(),
                                                                                                columnName),
                                                                                context.getDatabase().columnAllowBlank(
                                                                                                context.getTable(),
                                                                                                columnName),
                                                                                context.getDatabase()
                                                                                                .getDefaultColumnValue(
                                                                                                                context.getTable(),
                                                                                                                columnName),
                                                                                text -> {
                                                                                        setState(
                                                                                                        () -> {
                                                                                                                context.getDatabase()
                                                                                                                                .editDefaultColumnValue(
                                                                                                                                                context.getTable(),
                                                                                                                                                columnName,
                                                                                                                                                text);
                                                                                                        });
                                                                                },
                                                                                text -> {
                                                                                        return context.getDatabase()
                                                                                                        .isValidColumnValue(
                                                                                                                        context.getTable(),
                                                                                                                        columnName,
                                                                                                                        text);
                                                                                }))
                                                                .setHorizontalAlignment(Alignment.STRETCH))
                                                .setCrossAxisAlignment(Alignment.CENTER))
                                // .horizontal(5))
                                .setBorderColor(Color.black);
        }

}
