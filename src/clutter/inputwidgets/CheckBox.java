package clutter.inputwidgets;

import static clutter.core.Dimension.contains;
import java.awt.Color;
import java.util.function.Consumer;
import java.util.function.Function;
import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.core.Dimension;
import clutter.core.KeyEventController.KeyEventHandler;
import clutter.debug.Debug;
import clutter.debug.DebugMode;
import clutter.resources.Icons;

/**
 * A check box widget.
 */
public class CheckBox extends StatefulWidget<Context> implements KeyEventHandler {
    boolean checked = false;
    Consumer<Boolean> onChange;
    Function<Boolean, Boolean> validationFunction;
    boolean forceClick = false;

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

    public CheckBox(Context context, Consumer<Boolean> onChange, boolean checked) {
        super(context);
        this.onChange = onChange;
        this.checked = checked;
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
        return
        // new Padding(
        new IconButton(context, checked ? Icons.CHECKBOX : Icons.NO_PEOPLE, () -> {
            setState(() -> {
                checked = !checked;
                if (isValid()) {
                    onChange.accept(checked);
                    if (forceClick) {
                        context.getKeyEventController().removeKeyHandler(this);
                        context.getClickEventController().removeClickHandler(this);
                    }
                    forceClick = false;
                } else {
                    if (!forceClick) {
                        context.getKeyEventController().setKeyHandler(this);
                        context.getClickEventController().setClickHandler(this);
                    }
                    forceClick = true;
                }
            });
        }).setFontColor(isValid() ? Color.black : Color.red)
        // ).all(3).setDecoration(new Decoration().setBorderColor(isValid() ? null : Color.red))
        ;
    }

    @Override
    protected boolean runHitTest(int id, Dimension hitPos, int clickCount) {
        if (!isValid())
            return child.hitTest(id, hitPos, clickCount) || true;

        if (!contains(position, size, hitPos))
            return false;
        Debug.log(this, DebugMode.MOUSE, position + " " + size + " " + hitPos);
        return child.hitTest(id, hitPos, clickCount);
    }

    @Override
    public boolean onKeyPress(int id, int keyCode, char keyChar, int modifiers) {
        return forceClick;
    }
}
