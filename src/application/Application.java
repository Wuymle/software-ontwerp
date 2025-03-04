package application;

import java.awt.Color;

import assets.Icons;
import clutter.WidgetBuilder;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.core.Dimension;
import clutter.decoratedwidgets.DecoratedBox;
import clutter.decoratedwidgets.Icon;
import clutter.layoutwidgets.Center;
import clutter.layoutwidgets.ConstrainedBox;
import clutter.layoutwidgets.Row;

public class Application extends WidgetBuilder {

    public Application(Runnable repaint) {
        super(new Context());
        context.putProvider(ApplicationState.class, new ApplicationState(repaint));
        setPosition(new Dimension(0, 0));
    }

    @Override
    public Widget build(Context context) {
        return new Row(
                new ConstrainedBox(
                        new DecoratedBox(new Center(new Icon(Icons.DATABASE).setColor(Color.white).setFontSize(80))))
                        .setWidth(500),
                new DecoratedBox(null).setColor(Color.red));
    }
}
