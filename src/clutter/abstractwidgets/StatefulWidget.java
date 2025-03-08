package clutter.abstractwidgets;

import clutter.WidgetBuilder;
import clutter.core.Context;

public abstract class StatefulWidget extends WidgetBuilder<Context> {

    public StatefulWidget(Context context) {
        super(context);
    }

    public void setState(Runnable f) {
        f.run();
        this.child = build();
        context.requestRepaint();
    }
}
