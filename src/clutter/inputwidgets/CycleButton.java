package clutter.inputwidgets;

import static clutter.core.Dimension.contains;
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

/**
 * A button that cycles through a list of options.
 */
public class CycleButton extends StatefulWidget<Context> {
    String[] options;
    int selectedOption;
    Consumer<String> onSelect;
    Function<String, Boolean> validationFunction;

    /**
     * Constructor for the cycle button widget.
     * 
     * @param context        The context of the application.
     * @param options        The list of options.
     * @param selectedOption The selected option.
     * @param onSelect       The consumer to call when an option is selected.
     */
    public CycleButton(Context context, String[] options, int selectedOption, Consumer<String> onSelect) {
        super(context);
        this.options = options;
        this.selectedOption = selectedOption;
        this.onSelect = onSelect;
    }

    public CycleButton setValidationFunction(Function<String, Boolean> f) {
        this.validationFunction = f;
        return this;
    }

    /**
     * Check whether the text is valid.
     * 
     * @param text the text to check
     * @return whether the cycle button state is valid
     */
    private boolean isValid(String text) {
        return validationFunction == null || validationFunction.apply(text);
    }

    /**
     * Builds the cycle button widget.
     * 
     * @return The cycle button widget.
     */
    @Override
    public Widget build() {
        return new Button(context, options[selectedOption], () -> {
            setState(() -> {
                selectedOption = (selectedOption + 1) % options.length;

                if (isValid(options[selectedOption])) 
                    onSelect.accept(options[selectedOption]);
            });
        }).setDecoration(new Decoration().setBorderColor(isValid(options[selectedOption]) ? Color.WHITE : Color.RED));
    }

    /**
     * Hit test the widget.
     * 
     * @param id the id of the clickEvent
     * @param hitPos the position of the click
     * @param clickCount the number of clicks
     * @return the interactable
     */
    @Override
    public boolean hitTest(int id, Dimension hitPos, int clickCount) {
        if (!isValid(options[selectedOption]))
            return child.hitTest(id, hitPos, clickCount) || true;

        if (!contains(position, size, hitPos))
            return false;
        Debug.log(this, DebugMode.MOUSE, position + " " + size + " " + hitPos);
        return child.hitTest(id, hitPos, clickCount);
    }
}
