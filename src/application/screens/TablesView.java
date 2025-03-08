package application.screens;

import application.DatabaseAppContext;
import clutter.WidgetBuilder;
import clutter.abstractwidgets.Widget;

public class TablesView extends WidgetBuilder<DatabaseAppContext> {

    public TablesView(DatabaseAppContext context) {
        super(context);
    }

    @Override
    public Widget build() {
        throw new UnsupportedOperationException("Unimplemented method 'build'");
    }
}
