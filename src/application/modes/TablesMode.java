package application.modes;

import application.DatabaseAppContext;
import application.screens.TablesModeView;

public class TablesMode extends DatabaseMode {
    public TablesMode(DatabaseAppContext context) {
        super(new TablesModeView(context));
    }
}
