package clutter.inputwidgets;

import clutter.WidgetBuilder;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.decoratedwidgets.Icon;

public class IconButton extends WidgetBuilder<Context> {
    String icon;
    Runnable onClick;

    public IconButton(Context context, String icon, Runnable onClick) {
        super(context);
        this.icon = icon;
        this.onClick = onClick;
    }

    @Override
    public Widget build() {
        return new Clickable(
                new Icon(icon).setFontSize(16f),
                onClick, 1);
    }
}
