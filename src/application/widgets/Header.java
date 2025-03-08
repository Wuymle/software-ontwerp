package application.widgets;

import java.awt.Color;

import assets.Icons;
import clutter.WidgetBuilder;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.core.Dimension;
import clutter.decoratedwidgets.DecoratedBox;
import clutter.decoratedwidgets.Icon;
import clutter.decoratedwidgets.Text;
import clutter.layoutwidgets.ConstrainedBox;
import clutter.layoutwidgets.Padding;
import clutter.layoutwidgets.Row;
import clutter.layoutwidgets.SizedBox;
import clutter.layoutwidgets.enums.Alignment;

public class Header extends WidgetBuilder<Context> {

    public Header(Context context) {
        super(context);
    }

    @Override
    public Widget build() {
        return new ConstrainedBox(
                new DecoratedBox(
                        new Padding(new Row(
                                new Icon(Icons.DATABASE).setColor(Color.white),
                                new SizedBox(new Dimension(10, 0), null),
                                new Text("SuperDBMS").setColor(Color.white)))
                                .all(10))
                        .setColor(Color.blue))
                .setHeight(50)
                .setHorizontalAlignment(Alignment.STRETCH);
    }

}
