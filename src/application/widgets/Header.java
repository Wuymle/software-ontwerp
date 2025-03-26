package application.widgets;

import java.awt.Color;

import application.DatabaseAppContext;
import application.modes.DataBaseModes;
import clutter.resources.Icons;
import clutter.WidgetBuilder;
import clutter.abstractwidgets.Widget;
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

/**
 * A widget that represents the header of the application.
 */
public class Header extends WidgetBuilder<DatabaseAppContext> {
        DataBaseModes mode;

        /**
         * Constructor for the header widget.
         * 
         * @param context The context of the application.
         */
        public Header(DatabaseAppContext context) {
                super(context);
                this.mode = context.getDatabaseMode();
        }

        /**
         * Builds the header widget.
         * 
         * @return The header widget.
         */
        @Override
        public Widget build() {
                return new DecoratedBox(
                                new ConstrainedBox(
                                                new Row(
                                                                new Center(
                                                                                new Row(
                                                                                                new SizedBox(null,
                                                                                                                new Dimension(
                                                                                                                                10,
                                                                                                                                0)),
                                                                                                new Icon(Icons.DATABASE)
                                                                                                                .setColor(Color.white),
                                                                                                new SizedBox(null,
                                                                                                                new Dimension(
                                                                                                                                10,
                                                                                                                                0)),
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
