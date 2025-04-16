package clutter.inputwidgets;

import clutter.abstractwidgets.Widget;
import clutter.abstractwidgets.WidgetBuilder;
import clutter.core.Context;
import clutter.decoratedwidgets.Icon;

/**
 * An icon button widget.
 */
public class IconButton extends WidgetBuilder<Context> {
    String icon;
    Runnable onClick;

    /**
     * Constructor for the icon button widget.
     * @param context the context
     * @param icon    the icon
     * @param onClick the on click action
     */
    public IconButton(Context context, String icon, Runnable onClick) {
        super(context);
        this.icon = icon;
        this.onClick = onClick;
    }

    /**
     * build the icon button widget
     * @return the icon button widget
     */
    @Override
    public Widget build() {
        return new Clickable(
                new Icon(icon).setFontSize(16f),
                onClick, 1);
    }
}
