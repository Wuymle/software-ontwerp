package application;

import java.awt.Color;

import application.widgets.TableRow;
import assets.Icons;
import assets.dummy.DummyRows;
import clutter.WidgetBuilder;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.core.Dimension;
import clutter.decoratedwidgets.DecoratedBox;
import clutter.decoratedwidgets.Icon;
import clutter.decoratedwidgets.Text;
import clutter.layoutwidgets.Column;
import clutter.layoutwidgets.ConstrainedBox;
import clutter.layoutwidgets.Padding;
import clutter.layoutwidgets.Row;
import clutter.layoutwidgets.SizedBox;
import clutter.layoutwidgets.enums.Alignment;

public class Application extends WidgetBuilder {

    public Application(Context context) {
        super(context);
        setPosition(new Dimension(0, 0));
    }

    @Override
    public Widget build(Context context) {
        java.util.List<String[]> dummyRows = DummyRows.generateDummyRows(10);
        Widget[] rows = new Widget[dummyRows.size()];
        for (int i = 0; i < dummyRows.size(); i++) {
            rows[i] = new TableRow(context, dummyRows.get(i));
        }
        return new Column(
                new ConstrainedBox(
                        new DecoratedBox(
                                new Padding(new Row(
                                        new Icon(Icons.DATABASE).setColor(Color.white),
                                        new SizedBox(new Dimension(10, 0), null),
                                        new Text("SuperDBMS").setColor(Color.white)))
                                        .all(10))
                                .setColor(Color.blue))
                        .setHeight(50)
                        .setHorizontalAlignment(Alignment.STRETCH),
                new DecoratedBox(new Column(rows)).setColor(Color.white))
                .setCrossAxisAlignment(Alignment.STRETCH);
    }
}
