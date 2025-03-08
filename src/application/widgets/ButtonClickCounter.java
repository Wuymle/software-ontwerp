package application.widgets;

import java.awt.Color;

import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.decoratedwidgets.DecoratedBox;
import clutter.inputwidgets.Clickable;

public class ButtonClickCounter extends StatefulWidget {
    int count = 0;

    public ButtonClickCounter(Context context) {
        super(context);
    }

    @Override
    public Widget build() {
        return new Clickable(new DecoratedBox(null).setColor((count % 2 == 0) ? Color.blue : Color.red), () -> {
            setState(() -> {
                count += 1;
                System.out.println("Count changed: " + count);
            });
        });
    }
}
