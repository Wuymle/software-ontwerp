package application.modes;

import clutter.abstractwidgets.Widget;

public abstract class DatabaseMode {
    private Widget view;

    public enum DataBaseModes {
        TABLES_MODE,
        TABLE_ROWS_MODE,
        TABLE_DESIGN_MODE
    }

    public DatabaseMode(Widget view) {
        this.view = view;
    }

    public Widget getView() {
        return view;
    }
}
