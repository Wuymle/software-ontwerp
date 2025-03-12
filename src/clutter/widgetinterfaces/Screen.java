package clutter.widgetinterfaces;

import clutter.abstractwidgets.StatefulWidget;
import clutter.core.Context;

public abstract class Screen<C extends Context> extends StatefulWidget<C> {
    public Screen(C context) {
        super(context);
    }

    public abstract void onShow();

    public abstract void onHide();
}
