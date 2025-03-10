package application.widgets;

import java.awt.Color;

import application.modes.DatabaseMode.DataBaseModes;
import assets.Icons;
import clutter.WidgetBuilder;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.core.Dimension;
import clutter.decoratedwidgets.DecoratedBox;
import clutter.decoratedwidgets.Icon;
import clutter.decoratedwidgets.Text;
import clutter.layoutwidgets.Center;
import clutter.layoutwidgets.ConstrainedBox;
import clutter.layoutwidgets.Flexible;
import clutter.layoutwidgets.Row;
import clutter.layoutwidgets.SizedBox;
import clutter.layoutwidgets.enums.Alignment;

public class Header extends WidgetBuilder<Context> {
    DataBaseModes mode;

    public Header(Context context, DataBaseModes mode) {
        super(context);
        this.mode = mode;
    }

    @Override
    public Widget build() {
        return new DecoratedBox(
                new ConstrainedBox(
                        new Row(
                                new Center(
                                        new Row(
                                                new SizedBox(new Dimension(
                                                        10, 0),
                                                        null),
                                                new Icon(Icons.DATABASE)
                                                        .setColor(Color.white),
                                                new SizedBox(new Dimension(
                                                        10, 0),
                                                        null),
                                                new Text("SuperDBMS")
                                                        .setColor(Color.white))
                                                .setCrossAxisAlignment(
                                                        Alignment.CENTER)),
                                new Flexible(
                                        new Text(mode.name()).setFontSize(12)
                                                .setColor(Color.white))
                                        .setHorizontalAlignment(Alignment.END)
                                        .setVerticalAlignment(Alignment.END))
                                .setCrossAxisAlignment(Alignment.STRETCH))
                        .setHeight(50)
                        .setVerticalAlignment(Alignment.STRETCH))
                .setColor(Color.blue).setHorizontalAlignment(Alignment.STRETCH);
    }
}
