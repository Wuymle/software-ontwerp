package application;

import java.util.ArrayList;
import java.util.List;

import application.modes.DatabaseMode.DataBaseModes;
import clutter.ApplicationWindow;
import clutter.core.Context;
import database.Database;

public class DatabaseAppContext extends Context {
    private Database database = new Database();
    private DataBaseModes currentMode = DataBaseModes.TABLES_MODE;

    private String currentTable = null;

    private List<DatabaseModeChangeSubscriber> modeChangeSubscribers = new ArrayList<>();

    public DatabaseAppContext(ApplicationWindow applicationWindow) {
        super(applicationWindow);
    }

    public DataBaseModes getDatabaseMode() {
        return currentMode;
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabaseMode(DataBaseModes mode) {
        if (currentMode == mode)
            return;
        final DataBaseModes oldMode = currentMode;
        currentMode = mode;
        modeChangeSubscribers.forEach(subscriber -> subscriber.onDatabaseModeChange(oldMode, currentMode));
    }

    public void addModeChangeSubscriber(DatabaseModeChangeSubscriber subscriber) {
        if (modeChangeSubscribers.contains(subscriber))
            return;
        modeChangeSubscribers.add(subscriber);
    }

    public void removeModeChangeSubscriber(DatabaseModeChangeSubscriber subscriber) {
        modeChangeSubscribers.remove(subscriber);
    }

    public void setCurrentTable(String table) {
        currentTable = table;
    }

    public String getCurrentTable() {
        return currentTable;
    }
}
