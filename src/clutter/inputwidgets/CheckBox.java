package clutter.inputwidgets;

import java.util.function.Consumer;

import assets.Icons;
import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.core.Debug;

/**
 * A check box widget.
 */
public class CheckBox extends StatefulWidget<Context> {
    boolean checked = false;
    Consumer<Boolean> onChange;

    public CheckBox(Context context, Consumer<Boolean> onChange) {
        super(context);
        this.onChange = onChange;
    }

    public CheckBox(Context context, boolean checked, Consumer<Boolean> onChange) {
        super(context);
        this.checked = checked;
        this.onChange = onChange;
    }

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

}
