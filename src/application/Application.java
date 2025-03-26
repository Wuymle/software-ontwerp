package application;

import java.util.Map;

import application.modes.DataBaseModes;
import application.screens.TableDesignModeView;
import application.screens.TableRowsModeView;
import application.screens.TablesModeView;
import application.widgets.Header;
import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.layoutwidgets.Column;
import clutter.layoutwidgets.Expanded;
import clutter.layoutwidgets.enums.Alignment;
import clutter.widgetinterfaces.Screen;

/**
 * The main application widget.
 */
public class Application extends StatefulWidget<DatabaseAppContext>
        implements DatabaseModeChangeSubscriber {
    Map<DataBaseModes, Screen<DatabaseAppContext>> views = Map.of(
            DataBaseModes.TABLES_MODE, new TablesModeView(context),
            DataBaseModes.TABLE_ROWS_MODE, new TableRowsModeView(context),
            DataBaseModes.TABLE_DESIGN_MODE, new TableDesignModeView(context));

    /**
     * Constructor for the application widget.
     * 
     * @param context The context of the application.
     */
    public Application(DatabaseAppContext context) {
        super(context);
        context.addModeChangeSubscriber(this);
        views.get(context.getDatabaseMode()).onShow();
    }

    /**
     * Builds the application widget.
     * 
     * @return The application widget.
     */
    @Override
    public Widget build() {
        return new Expanded(
                new Column(
                        new Header(context),
                        views.get(context.getDatabaseMode()))
                        .setCrossAxisAlignment(Alignment.STRETCH))
                .setHorizontalAlignment(Alignment.STRETCH);
    }

    /**
     * Handles database mode changes.
     * 
     * @param oldMode The old mode of the database.
     * @param newMode The new mode of the database.
     */
    @Override
    public void onDatabaseModeChange(DataBaseModes oldMode, DataBaseModes newMode) {
        setState(() -> {
            views.get(oldMode).onHide();
            views.get(newMode).onShow();
        });
    }
}
