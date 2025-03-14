package application.widgets;

import java.util.function.Consumer;
import java.util.function.Function;

import application.DatabaseAppContext;
import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.inputwidgets.CycleButton;
import clutter.inputwidgets.InputText;
import clutter.layoutwidgets.Flexible;
import clutter.layoutwidgets.enums.Alignment;
import database.ColumnType;

public class ValueCell extends StatefulWidget<DatabaseAppContext> {
    ColumnType type;
    String value;
    Boolean allowBlank;
    Consumer<String> onChange;
    Function<String, Boolean> validationFunction;

    public ValueCell(DatabaseAppContext context, ColumnType type, boolean allowBlank, String value,
            Consumer<String> onChange, Function<String, Boolean> validationFunction) {
        super(context);
        this.type = type;
        this.value = value;
        this.allowBlank = allowBlank;
        this.onChange = onChange;
        this.validationFunction = validationFunction;
    }

    @Override
    public Widget build() {
        return new Flexible(
                (type == ColumnType.BOOLEAN)
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
                                        onChange.accept(text);
                                    });
                                })
                        : new InputText(context,
                                value,
                                text -> {
                                    setState(() -> {
                                        onChange.accept(text);
                                    });
                                }).setValidationFunction(validationFunction))
                .setHorizontalAlignment(Alignment.STRETCH);
    }
}
