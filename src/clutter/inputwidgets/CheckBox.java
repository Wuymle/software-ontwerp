package clutter.inputwidgets;

import java.util.function.Consumer;

import assets.Icons;
import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;

public class CheckBox extends StatefulWidget<Context> {
    Consumer<Boolean> onChange;
    boolean checked = false;

    public CheckBox(Context context, Consumer<Boolean> onChange) {
        super(context);
        this.onChange = onChange;
    }

    @Override
    public Widget build() {
        return new IconButton(context, checked ? Icons.CHECKBOX : Icons.NO_PEOPLE, () -> {
            setState(() -> {
                checked = !checked;
            });
            onChange.accept(checked);
        });
    }

}
