package application;

import java.util.List;

import assets.dummy.DummyRows;
import assets.dummy.DummyTables;
import clutter.ApplicationWindow;
import clutter.core.Context;

public class DatabaseAppContext extends Context {

    public DatabaseAppContext(ApplicationWindow applicationWindow) {
        super(applicationWindow);
    }

    public List<String[]> getRows(int n) {
        return DummyRows.generateDummyRows(n);
    }

    public List<String> getTables(int n) {
        return DummyTables.generateDummyTableNames(n);
    }
}
