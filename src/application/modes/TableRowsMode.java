package application.modes;

import application.DatabaseAppContext;
import application.screens.TableRowsModeView;

public class TableRowsMode extends DatabaseMode {
    public TableRowsMode(DatabaseAppContext context) {
        super(new TableRowsModeView(context));
    }
}
