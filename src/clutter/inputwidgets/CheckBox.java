package clutter.inputwidgets;

import java.awt.Color;
import java.util.function.Consumer;
import java.util.function.Function;

import assets.Icons;
import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.decoratedwidgets.DecoratedBox;
import clutter.layoutwidgets.Padding;

/**
 * A check box widget.
 */
public class CheckBox extends StatefulWidget<Context> {
    boolean checked = false;
    Consumer<Boolean> onChange;
    Function<Boolean, Boolean> validationFunction;

    /**
     * Constructor for the check box widget.
     * 
     * @param context  The context of the application.
     * @param onChange The consumer to call when the check box is clicked.
     */
    public CheckBox(Context context, Consumer<Boolean> onChange) {
        super(context);
        this.onChange = onChange;
    }

    /**
     * Constructor for the check box widget.
     * 
     * @param context  The context of the application.
     * @param checked  Whether the check box is checked.
     * @param onChange The consumer to call when the check box is clicked.
     */
    public CheckBox(Context context, boolean checked, Consumer<Boolean> onChange) {
        super(context);
        this.checked = checked;
        this.onChange = onChange;
    }

    /**
     * Constructor for the check box widget.
     * 
     * @param callback function to validate the text
     * @return the input text widget
     */
    @Override
    public Widget build() {
        Debug.log(this, "Building CheckBox");
        return new IconButton(context, checked ? Icons.CHECKBOX : Icons.NO_PEOPLE, () -> {
            setState(() -> {
                Debug.log(this, "CLICKED ICONBUTTON");
                checked = !checked;
            });
            onChange.accept(checked);
        });
    }

    /**
     * @return whether the check is valid
     */
    private boolean isValid() {
        return validationFunction == null || validationFunction.apply(checked);
    }

    @Override
    public Widget build() {
        return new DecoratedBox(new Padding(new IconButton(context, checked ? Icons.CHECKBOX : Icons.NO_PEOPLE, () -> {
            setState(() -> {
                checked = !checked;
                if (isValid())
                    onChange.accept(checked);
            });
        })).all(3)).setBorderColor(isValid() ? null : Color.red);
    }
}
