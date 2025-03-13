package application.screens;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.List;

import application.DatabaseAppContext;
import application.modes.DataBaseModes;
import application.widgets.TableRow;
import clutter.abstractwidgets.Widget;
import clutter.decoratedwidgets.DecoratedBox;
import clutter.layoutwidgets.Column;
import clutter.widgetinterfaces.KeyEventHandler;
import clutter.widgetinterfaces.Screen;

public class TableRowsModeView extends Screen<DatabaseAppContext> implements KeyEventHandler {

    public TableRowsModeView(DatabaseAppContext context) {
        super(context);
    }

    @Override
    public Widget build() {
        final List<String[]> dummyRows = List.of(new String[] { "1", "2", "3" }, new String[] { "4", "5", "6" },
                new String[] { "7", "8", "9" });
        Widget[] rows = new Widget[dummyRows.size()];
        for (int i = 0; i < dummyRows.size(); i++) {
            rows[i] = new TableRow(context, dummyRows.get(i));
        }
        return new DecoratedBox(new Column(rows)).setColor(Color.white);
    }

    @Override
    public void onKeyPress(int id, int keyCode, char keyChar) {
        switch (id) {
            case KeyEvent.KEY_PRESSED:
                switch (keyCode) {
                    case KeyEvent.VK_DELETE:
                        break;

                    case KeyEvent.VK_ESCAPE:
                        System.out.println("Switching to tables mode");
                        context.setDatabaseMode(DataBaseModes.TABLES_MODE);
                        break;

                    case KeyEvent.VK_ENTER:
                        context.setDatabaseMode(DataBaseModes.TABLE_DESIGN_MODE);
                        System.out.println("Switching to table design mode");
                        if ((keyCode & KeyEvent.CTRL_DOWN_MASK) != 0) {
                        }
                        break;

                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onShow() {
        context.getKeyEventController().setKeyHandler(this);
    }

    @Override
    public void onHide() {
        context.getKeyEventController().removeKeyHandler(this);
    }

}
