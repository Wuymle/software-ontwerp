package clutter.abstractwidgets;

import clutter.WidgetBuilder;
import clutter.core.Context;

/**
 * A widget that can have state.
 */
public abstract class StatefulWidget<C extends Context> extends WidgetBuilder<C> {

    /**
     * @param context the context
     */
    public StatefulWidget(C context) {
        super(context);
    }

    /**
     * Set the state of the widget.
     * 
     * @param f the function to set the state
     */
    public void setState(Runnable f) {
        f.run();
        this.child = build();
        context.requestRepaint();
    }
}
