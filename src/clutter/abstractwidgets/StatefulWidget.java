package clutter.abstractwidgets;

import application.ApplicationState;
import clutter.WidgetBuilder;
import clutter.core.Context;

public abstract class StatefulWidget extends WidgetBuilder {

    public StatefulWidget(Context context) {
        super(context);
    }

    public void setState(Runnable f) {
        f.run();
        this.child = build(context);
        context.getProvider(ApplicationState.class).requestRepaint();
    }
}
