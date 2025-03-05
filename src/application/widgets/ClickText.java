package application.widgets;

import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.decoratedwidgets.Text;
import clutter.inputwidgets.Clickable;

public class ClickText extends StatefulWidget {
    private String text;

    public ClickText(Context context) {
        super(context);
        text = "wannes";
    }

    @Override
    public Widget build(Context context) {
        return new Clickable(new Text(text), () -> {
            setState(() -> {
                text = "Wannes michielssen";
            });
        });
    }

}
