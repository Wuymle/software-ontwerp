package application.widgets;

import java.util.function.Consumer;
import java.util.function.Function;
import application.DatabaseAppContext;
import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.inputwidgets.CycleButton;
import clutter.inputwidgets.InputText;
import newdatabase.Column;
import newdatabase.ColumnType;

/**
 * A widget that represents a cell in the table rows mode.
 */
public class ValueCell extends StatefulWidget<DatabaseAppContext> {
    Column column;
    String value;
    Consumer<String> onChange;
    Function<String, Boolean> validationFunction;

    /**
     * Constructor for the value cell widget.
     * 
     * @param context The context of the application.
     * @param type The type of the column.
     * @param allowBlank Whether the column allows blank values.
     * @param value The value of the cell.
     * @param onChange
     * @param validationFunction
     */
    public ValueCell(DatabaseAppContext context, Column column, String value,
            Consumer<String> onChange, Function<String, Boolean> validationFunction) {
        super(context);
        this.column = column;
        this.value = value;
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
        return column.getType() == ColumnType.BOOLEAN ? new CycleButton<String>(context,
                column.getAllowBlank() ? new String[] {"TRUE", "FALSE", ""}
                        : new String[] {"TRUE", "FALSE"},
                switch (value) {
                    case "TRUE" -> 0;
                    case "FALSE" -> 1;
                    default -> 2;
                }, text -> setState(() -> onChange.accept(text)))
                : new InputText(context, value, text -> setState(() -> onChange.accept(text)))
                        .setValidationFunction(validationFunction);
    }
}
