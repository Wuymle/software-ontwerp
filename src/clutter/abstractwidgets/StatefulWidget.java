package clutter.abstractwidgets;

import clutter.WidgetBuilder;
import clutter.core.Context;

public abstract class StatefulWidget<C extends Context> extends WidgetBuilder<C> {

    public StatefulWidget(C context) {
        super(context);
    }

    public void setState(Runnable f) {
        f.run();
        this.child = build();
        context.requestRepaint();
    }
}
