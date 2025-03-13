package clutter.widgetinterfaces;

import clutter.abstractwidgets.StatefulWidget;
import clutter.core.Context;

/**
 * Abstract class for screens.
 */
public abstract class Screen<C extends Context> extends StatefulWidget<C> {

    /**
     * @param context the context
     */
    public Screen(C context) {
        super(context);
    }

    public abstract void onShow();

    public abstract void onHide();
}
