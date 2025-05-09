package clutter.inputwidgets;

import java.awt.Color;
import java.util.function.Consumer;
import java.util.function.Function;
import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.core.Decoration;
import clutter.core.Dimension;
import clutter.debug.Debug;
import clutter.debug.DebugMode;
import clutter.layoutwidgets.Padding;
import clutter.resources.Icons;
import static clutter.core.Dimension.contains;

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
     * @param context The context of the application.
     * @param onChange The consumer to call when the check box is clicked.
     */
    public CheckBox(Context context, Consumer<Boolean> onChange) {
        super(context);
        this.onChange = onChange;
    }

    /**
     * Constructor for the check box widget.
     * 
     * @param context The context of the application.
     * @param checked Whether the check box is checked.
     * @param onChange The consumer to call when the check box is clicked.
     */
    public CheckBox(Context context, boolean checked, Consumer<Boolean> onChange) {
        super(context);
        this.checked = checked;
        this.onChange = onChange;
    }

    public CheckBox setValidationFunction(Function<Boolean, Boolean> validationFunction) {
        this.validationFunction = validationFunction;
        return this;
    }

    /**
     * @return whether the check is valid
     */
    private boolean isValid() {
        return validationFunction == null || validationFunction.apply(checked);
    }

    /**
     * Constructor for the check box widget.
     * 
     * @param callback function to validate the text
     * @return the input text widget
     */
    @Override
    public Widget build() {
        return new Padding(
                new IconButton(context, checked ? Icons.CHECKBOX : Icons.NO_PEOPLE, () -> {
                    setState(() -> {
                        checked = !checked;
                        if (isValid())
                            onChange.accept(checked);
                    });
                })).all(3).setDecoration(
                        new Decoration().setBorderColor(isValid() ? null : Color.red));
    }

    @Override
    public boolean hitTest(int id, Dimension hitPos, int clickCount) {
        if (!isValid())
            return child.hitTest(id, hitPos, clickCount) || true;
            
        if (!contains(position, size, hitPos))
            return false;
        Debug.log(this, DebugMode.MOUSE, position + " " + size + " " + hitPos);
        return child.hitTest(id, hitPos, clickCount);
    }
}
