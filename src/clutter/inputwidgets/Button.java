package clutter.inputwidgets;

import java.awt.Color;

import clutter.WidgetBuilder;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.decoratedwidgets.DecoratedBox;
import clutter.decoratedwidgets.Text;
import clutter.layoutwidgets.Padding;
import clutter.layoutwidgets.enums.Alignment;

/**
 * A button widget.
 */
public class Button extends WidgetBuilder<Context> {
    String text;
    Runnable onClick;

    /**
     * @param context the context
     * @param text    the text
     * @param onClick the on click action
     */
    public Button(Context context, String text, Runnable onClick) {
        super(context);
        this.text = text;
        this.onClick = onClick;
    }

    /**
     * @return the button widget
     */
    @Override
    public Widget build() {
        return new DecoratedBox(new Clickable(
                new Padding(new Text(text).setFontSize(16)).all(2), onClick, 1)
                .setHorizontalAlignment(Alignment.CENTER))
                .setBorderColor(Color.black)
                .setColor(new Color(211, 211, 211))
                .setHorizontalAlignment(Alignment.STRETCH);
    }

}
