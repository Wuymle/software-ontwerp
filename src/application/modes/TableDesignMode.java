package application.modes;

import application.DatabaseAppContext;
import application.screens.TableDesignModeView;

public class TableDesignMode extends DatabaseMode {
    public TableDesignMode(DatabaseAppContext context) {
        super(new TableDesignModeView(context));
    }
}
