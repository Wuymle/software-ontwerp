package application;

import application.modes.DataBaseModes;

public interface DatabaseModeChangeSubscriber {
    void onDatabaseModeChange(DataBaseModes oldMode, DataBaseModes newMode);
}
