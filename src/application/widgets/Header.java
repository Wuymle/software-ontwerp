package application.widgets;

import java.awt.Color;
import application.DatabaseAppContext;
import clutter.abstractwidgets.Widget;
import clutter.abstractwidgets.WidgetBuilder;
import clutter.core.Decoration;
import clutter.core.Dimension;
import clutter.decoratedwidgets.Icon;
import clutter.decoratedwidgets.Text;
import clutter.layoutwidgets.Center;
import clutter.layoutwidgets.ClampToFit;
import clutter.layoutwidgets.ConstrainedBox;
import clutter.layoutwidgets.Expanded;
import clutter.layoutwidgets.Row;
import clutter.layoutwidgets.SizedBox;
import clutter.layoutwidgets.Stack;
import clutter.layoutwidgets.enums.Alignment;
import clutter.resources.Icons;

/**
 * A widget that represents the header of the application.
 */
public class Header extends WidgetBuilder<DatabaseAppContext> {
    private String viewName;

    /**
     * Constructor for the header widget.
     * 
     * @param context The context of the application.
     */
    public Header(DatabaseAppContext context, String viewName) {
        super(context);
        this.viewName = viewName;
    }

    /**
     * Builds the header widget.
     * 
     * @return The header widget.
     */
    @Override
    public Widget build() {
        return new ConstrainedBox(new Stack(
                new ClampToFit(new Center(new Row(new SizedBox(new Dimension(10, 0)),
                        new Icon(Icons.DATABASE).setFontColor(Color.white).setFontSize(24),
                        new SizedBox(new Dimension(10, 0)),
                        new Text("SuperDBMS").setFontColor(Color.white).setFontSize(24))
                                .setCrossAxisAlignment(Alignment.CENTER))),
                new Expanded(new Text(viewName).setFontSize(12).setFontColor(Color.white))
                        .setHorizontalAlignment(Alignment.END).setVerticalAlignment(Alignment.END)))
                                .setHeight(50).setVerticalAlignment(Alignment.STRETCH)
                                .setDecoration(new Decoration().setColor(Color.blue));
    }
}
