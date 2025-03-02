package clutter.abstractwidgets;

import java.awt.Graphics;

import application.ApplicationState;
import clutter.WidgetBuilder;
import clutter.core.Context;

public abstract class StatefulWidget extends WidgetBuilder {
    protected StatefulWidget(Context context) {
        super(context);
    }

    public void setState(Runnable f) {
        f.run();
        this.child = build(context);
        context.getProvider(ApplicationState.class).requestRepaint();
    }
}
