package application.widgets;

import java.awt.Color;

import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.decoratedwidgets.DecoratedBox;
import clutter.inputwidgets.Clickable;

public class ButtonClickCounter extends StatefulWidget {
    public ButtonClickCounter(Context context) {
        super(context);
    }

    int count = 0;

    @Override
    public Widget build(Context context) {
        return new Clickable(new DecoratedBox(null).setColor((count % 2 == 0) ? Color.blue : Color.red), () -> {
            setState(() -> {
                count += 1;
                System.out.println("Count changed: " + count);
            });
        });
    }
}
