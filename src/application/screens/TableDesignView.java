package application.screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import application.DatabaseAppContext;
import application.widgets.Header;
import application.widgets.TableDesignViewRow;
import clutter.abstractwidgets.Widget;
import clutter.inputwidgets.Clickable;
import clutter.layoutwidgets.Column;
import clutter.layoutwidgets.ConstrainedBox;
import clutter.layoutwidgets.GrowToFit;
import clutter.layoutwidgets.ScrollableView;
import clutter.layoutwidgets.enums.Alignment;
import database.Database.TableDesignChangeListener;

/**
 * A screen that represents the table design mode view.
 */
public class TableDesignView extends DatabaseScreen implements TableDesignChangeListener {
    String tableName;
    List<String> selectedColumns = new ArrayList<String>();
    Consumer<String> onOpenRowsView;
    Runnable closeWindow;

    /**
     * Constructor for the table design mode view.
     * 
     * @param context The context of the application.
     */
    public TableDesignView(DatabaseAppContext context, String tableName,
            Consumer<String> onOpenRowsView) {
        super(context);
        this.tableName = tableName;
        context.getDatabase().addTableDesignChangeListener(tableName, this);
        this.onOpenRowsView = onOpenRowsView;
    }

    /**
     * Sets the action to be performed when the window is closed.
     * 
     * @param closeWindow The action to be performed when the window is closed.
     */
    public TableDesignView setCloseWindowFunction(Runnable closeWindow) {
        this.closeWindow = closeWindow;
        return this;
    }

    /**
     * Builds the table design mode view.
     * 
     * @return The table design mode view.
     */
    @Override
    public Widget build() {
        List<Widget> rows = context.getDatabase().getColumnNames(tableName).stream()
                .map(columnName -> (Widget) new TableDesignViewRow(context, tableName, columnName,
                        name -> selectedColumns.add(name), name -> selectedColumns.remove(name)))
                .toList();

        return new Column(new Header(context, tableName + ": design mode"), new ScrollableView(
                context,
                new GrowToFit(new Column(new Column(rows).setCrossAxisAlignment(Alignment.STRETCH),
                        new GrowToFit(new Clickable(new ConstrainedBox().setMinHeight(50),
                                () -> setState(() -> context.getDatabase().addColumn(tableName)),
                                2))).setCrossAxisAlignment(Alignment.STRETCH))))
                                        .setCrossAxisAlignment(Alignment.STRETCH);
    }

    /**
     * Handles key presses.
     * 
     * @param id The ID of the key event.
     * @param keyCode The key code of the key event.
     * @param keyChar The character of the key event.
     */
    @Override
    public boolean onKeyPress(int id, int keyCode, char keyChar) {
        switch (id) {
            case KeyEvent.KEY_PRESSED:
                switch (keyCode) {
                    case KeyEvent.VK_DELETE:
                        setState(() -> {
                            for (String column : selectedColumns) {
                                context.getDatabase().deleteColumn(tableName, column);
                            }
                            selectedColumns.clear();
                        });
                        return true;

                    case KeyEvent.VK_ESCAPE:
                        return true;

                    case KeyEvent.VK_ENTER: {
                        onOpenRowsView.accept(tableName);
                        return true;
                    }

                    default:
                        return false;
                }
            default:
                return false;
        }
    }

    @Override
    public void onTableChanged() {
        System.out.println("Table changed: " + context.getDatabase().getTables());
        if (context.getDatabase().getTables().contains(tableName))
            setState(() -> {
            });
        else {
            context.getDatabase().removeTableDesignChangeListener(tableName, this);
            closeWindow.run();
        }
    }
}
