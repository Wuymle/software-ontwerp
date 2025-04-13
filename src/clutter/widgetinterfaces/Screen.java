package clutter.widgetinterfaces;

import clutter.abstractwidgets.StatefulWidget;
import clutter.core.Context;
import clutter.layoutwidgets.enums.Alignment;

/**
 * Abstract class for screens.
 */
public abstract class Screen<C extends Context> extends StatefulWidget<C> {

    /**
     * @param context the context
     */
    public Screen(C context) {
        super(context);
        setHorizontalAlignment(Alignment.STRETCH);
        setVerticalAlignment(Alignment.STRETCH);
    }

    public abstract void onGetFocus();

    public abstract void onLoseFocus();
}
