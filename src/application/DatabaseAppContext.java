package application;

import clutter.ApplicationWindow;
import clutter.core.Context;
import database.Database;

/**
 * The context of the database application.
 */
public class DatabaseAppContext extends Context {
    private Database database = new Database();

    /**
     * Constructor for the database application context.
     * 
     * @param applicationWindow The application window.
     */
    public DatabaseAppContext(ApplicationWindow applicationWindow) {
        super(applicationWindow);
    }

    /**
     * Gets the database.
     * 
     * @return The database.
     */
    public Database getDatabase() {
        return database;
    }
}
