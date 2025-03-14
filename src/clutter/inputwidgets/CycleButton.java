package clutter.inputwidgets;

import java.util.function.Consumer;

import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;

public class CycleButton extends StatefulWidget<Context> {
    String[] options;
    int selectedOption;
    Consumer<String> onSelect;

    public CycleButton(Context context, String[] options, int selectedOption, Consumer<String> onSelect) {
        super(context);
        this.options = options;
        this.selectedOption = selectedOption;
        this.onSelect = onSelect;
    }

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
