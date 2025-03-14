package application.widgets;

import java.awt.Color;
import java.util.function.Consumer;
import java.util.function.Function;

import application.DatabaseAppContext;
import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.debug.ClickPasses;
import clutter.inputwidgets.CycleButton;
import clutter.inputwidgets.InputText;
import clutter.layoutwidgets.Flexible;
import clutter.layoutwidgets.enums.Alignment;
import database.ColumnType;

/**
 * A widget that represents a cell in the table rows mode.
 */
public class ValueCell extends StatefulWidget<DatabaseAppContext> {
    ColumnType type;
    String value;
    Boolean allowBlank;
    Consumer<String> onChange;
    Function<String, Boolean> validationFunction;

    /**
     * Constructor for the value cell widget.
     * 
     * @param context            The context of the application.
     * @param type               The type of the column.
     * @param allowBlank         Whether the column allows blank values.
     * @param value              The value of the cell.
     * @param onChange
     * @param validationFunction
     */
    public ValueCell(DatabaseAppContext context, ColumnType type, boolean allowBlank, String value,
            Consumer<String> onChange, Function<String, Boolean> validationFunction) {
        super(context);
        this.type = type;
        this.value = value;
        this.allowBlank = allowBlank;
        this.onChange = onChange;
        this.validationFunction = validationFunction;
    }

    /**
     * Builds the value cell widget.
     * 
     * @return The value cell widget.
     */
    @Override
    public Widget build() {
        return (type == ColumnType.BOOLEAN)
                ? new CycleButton(context,
                        (allowBlank)
                                ? new String[] { "TRUE", "FALSE", "" }
                                : new String[] { "TRUE", "FALSE" },
                        switch (value) {
                            case "TRUE" -> 0;
                            case "FALSE" -> 1;
                            default -> 2;
                        }, text -> {
                            setState(() -> {
                                System.out.println("ValueCell: " + text);
                                onChange.accept(text);
                            });
                        })
                : new InputText(context,
                        value,
                        text -> {
                            setState(() -> {
                                onChange.accept(text);
                            });
                        }).setValidationFunction(validationFunction).setBorderColor(Color.black);
    }
}
