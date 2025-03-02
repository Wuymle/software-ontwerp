package application;

import java.awt.Color;

import application.widgets.ButtonClickCounter;
import clutter.WidgetBuilder;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.decoratedwidgets.Clip;
import clutter.decoratedwidgets.DecoratedBox;
import clutter.decoratedwidgets.Rectangle;
import clutter.inputwidgets.Clickable;
import clutter.layoutwidgets.Center;
import clutter.layoutwidgets.SizedBox;

public class Application extends WidgetBuilder {

    public Application(Runnable repaint) {
        super(new Context());
        context.putProvider(ApplicationState.class, new ApplicationState(repaint));
        // super(new Row(
        // new Flexible(
        // new Column(
        // new Flexible(
        // new Clickable(
        // new DecoratedBox(null).setColor(Color.red), () -> {
        // System.out.println("RED BUTTON PRESSED");
        // }),
        // 1),
        // new DecoratedBox(null).setColor(Color.blue)),
        // 1),
        // new Flexible(
        // new Column(
        // new DecoratedBox(null).setColor(Color.green),
        // new DecoratedBox(null).setColor(Color.yellow)),
        // 1)));
        setPosition(0, 0);
    }

    @Override
    public Widget build(Context context) {
        // return new ButtonClickCounter(context);
        return new Center(
                new SizedBox(
                        100,
                        100,
                        new Clip(
                                new Rectangle(200, 200, Color.red))));
    }
}
