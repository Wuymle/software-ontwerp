package application;

import application.modes.DataBaseModes;

/**
 * Interface for a subscriber to the database mode change event.
 */
public interface DatabaseModeChangeSubscriber {
    void onDatabaseModeChange(DataBaseModes oldMode, DataBaseModes newMode);
}
