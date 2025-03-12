package application;

import application.modes.DatabaseMode.DataBaseModes;

public interface DatabaseModeChangeSubscriber {
    void onDatabaseModeChange(DataBaseModes oldMode, DataBaseModes newMode);
}
