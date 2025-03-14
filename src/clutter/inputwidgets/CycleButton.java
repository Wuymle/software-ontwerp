package clutter.inputwidgets;

import java.util.function.Consumer;

import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;

/**
 * A button that cycles through a list of options.
 */
public class CycleButton extends StatefulWidget<Context> {
    String[] options;
    int selectedOption;
    Consumer<String> onSelect;

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
                onSelect.accept(options[selectedOption]);
            });
        });
    }
}
