package application;

import java.awt.event.KeyEvent;
import java.util.Map;

import application.modes.DatabaseMode;
import application.modes.DatabaseMode.DataBaseModes;
import application.modes.TableDesignMode;
import application.modes.TableRowsMode;
import application.modes.TablesMode;
import application.widgets.Header;
import clutter.WidgetBuilder;
import clutter.abstractwidgets.Widget;
import clutter.layoutwidgets.Column;
import clutter.layoutwidgets.Expanded;
import clutter.layoutwidgets.enums.Alignment;
import clutter.widgetinterfaces.KeyEventHandler;

public class Application extends WidgetBuilder<DatabaseAppContext> implements KeyEventHandler {
    Map<DataBaseModes, DatabaseMode> modes = Map.of(
            DataBaseModes.TABLES_MODE, new TablesMode(context),
            DataBaseModes.TABLE_ROWS_MODE, new TableRowsMode(context),
            DataBaseModes.TABLE_DESIGN_MODE, new TableDesignMode(context));
    DataBaseModes currentMode = DataBaseModes.TABLES_MODE;

    public Application(DatabaseAppContext context) {
        super(context);
        context.getKeyEventController().setKeyHandler(this);
    }

    @Override
    public Widget build() {
        return new Expanded(
                new Column(
                        new Header(context, currentMode),
                        modes.get(currentMode).getView())
                        .setCrossAxisAlignment(Alignment.STRETCH))
                .setHorizontalAlignment(Alignment.STRETCH);
    }

    @Override
    public void onKeyPress(int id, int keyCode, char keyChar) {
        if (id == KeyEvent.KEY_PRESSED) {
            switch (keyCode) {
                case KeyEvent.VK_ENTER:
                    if ((KeyEvent.CTRL_DOWN_MASK) != 0) {
                        System.out.println("Ctrl+Enter pressed");
                    }
                    break;

            }
        }
    }
}
