package application;

import java.util.ArrayList;
import java.util.List;

import application.modes.DataBaseModes;
import clutter.ApplicationWindow;
import clutter.core.Context;
import database.Database;

/**
 * The context of the database application.
 */
public class DatabaseAppContext extends Context {
    private Database database = new Database();
    private DataBaseModes currentMode = DataBaseModes.TABLES_MODE;

    private String table = null;

    private List<DatabaseModeChangeSubscriber> modeChangeSubscribers = new ArrayList<>();

    /**
     * Constructor for the database application context.
     * 
     * @param applicationWindow The application window.
     */
    public DatabaseAppContext(ApplicationWindow applicationWindow) {
        super(applicationWindow);
    }

    /**
     * Gets the current mode of the database.
     * 
     * @return The current mode of the database.
     */
    public DataBaseModes getDatabaseMode() {
        return currentMode;
    }

    /**
     * Gets the database.
     * 
     * @return The database.
     */
    public Database getDatabase() {
        return database;
    }

    /**
     * Sets the mode of the database.
     * 
     * @param mode The mode to set.
     */
    public void setDatabaseMode(DataBaseModes mode) {
        if (currentMode == mode)
            return;
        final DataBaseModes oldMode = currentMode;
        currentMode = mode;
        modeChangeSubscribers.forEach(subscriber -> subscriber.onDatabaseModeChange(oldMode, currentMode));
    }

    /**
     * Adds a mode change subscriber.
     * 
     * @param subscriber The subscriber to add.
     */
    public void addModeChangeSubscriber(DatabaseModeChangeSubscriber subscriber) {
        if (modeChangeSubscribers.contains(subscriber))
            return;
        modeChangeSubscribers.add(subscriber);
    }

    /**
     * Removes a mode change subscriber.
     * 
     * @param subscriber The subscriber to remove.
     */
    public void removeModeChangeSubscriber(DatabaseModeChangeSubscriber subscriber) {
        modeChangeSubscribers.remove(subscriber);
    }

    /**
     * Sets the table.
     * 
     * @param table The table to set.
     */
    public void setTable(String table) {
        this.table = table;
    }

    /**
     * Gets the table.
     * 
     * @return The table.
     */
    public String getTable() {
        return table;
    }
}
