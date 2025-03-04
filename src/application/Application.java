package application;

import java.awt.Color;

import clutter.WidgetBuilder;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.core.Dimension;
import clutter.decoratedwidgets.DecoratedBox;
import clutter.inputwidgets.InputText;
import clutter.layoutwidgets.Center;
import clutter.layoutwidgets.ConstrainedBox;
import clutter.layoutwidgets.Row;

public class Application extends WidgetBuilder {

    public Application(Context context) {
        super(context);
        setPosition(new Dimension(0, 0));
    }

    @Override
    public Widget build(Context context) {
        return new Row(
                new ConstrainedBox(
                        new DecoratedBox(new Center(new InputText(context, "defaulttext"))))
                        .setWidth(500),
                new DecoratedBox(null).setColor(Color.red));
    }
}
