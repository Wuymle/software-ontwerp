package application.screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import application.DatabaseAppContext;
import application.widgets.Header;
import application.widgets.ValueCell;
import clutter.abstractwidgets.Widget;
import clutter.core.ResizableGridController;
import clutter.core.ScrollController;
import clutter.decoratedwidgets.Text;
import clutter.inputwidgets.CheckBox;
import clutter.inputwidgets.Clickable;
import clutter.inputwidgets.CycleButton;
import clutter.inputwidgets.InputText;
import clutter.layoutwidgets.Center;
import clutter.layoutwidgets.Column;
import clutter.layoutwidgets.ConstrainedBox;
import clutter.layoutwidgets.GrowToFit;
import clutter.layoutwidgets.ResizableGrid;
import clutter.layoutwidgets.ScrollableView;
import clutter.layoutwidgets.enums.Alignment;
import database.ColumnType;
import database.Database.TableDataChangeListener;
import database.Database.TableDesignChangeListener;

/**
 * A screen that represents the table design mode view.
 */
public class TableFormsView extends DatabaseScreen implements TableDataChangeListener {
    Integer rowNumber;
    String tableName;
    List<String> selectedColumns = new ArrayList<String>();
    Runnable closeWindow;
    final ScrollController scrollController = new ScrollController(context);

    /**
     * Constructor for the table design mode view.
     * 
     * @param context The context of the application.
     */
    public TableFormsView(DatabaseAppContext context, String tableName) {
        super(context);
        this.rowNumber = 0;
        this.tableName = tableName;
        context.getDatabase().addTableDataChangeListener(tableName, this);
    }

    /**
     * Sets the action to be performed when the window is closed.
     * 
     * @param closeWindow The action to be performed when the window is closed.
     */
    public TableFormsView setCloseWindowFunction(Runnable closeWindow) {
        this.closeWindow = closeWindow;
        return this;
    }

    /**
     * Handles key presses.
     * 
     * @param id The ID of the key event.
     * @param keyCode The key code of the key event.
     * @param keyChar The character of the key event.
     */
    @Override
    public boolean onKeyPress(int id, int keyCode, char keyChar, int modifiers) {
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

                    // case KeyEvent.VK_ENTER: {
                    // onOpenRowsView.accept(tableName);
                    // return true;
                    // }

                    default:
                        return false;
                }
            default:
                return false;
        }
    }
    
    /**
     * Builds the table design mode view.
     * 
     * @return The table design mode view.
     */
    @Override
    public Widget build() {
        return new Column(new Header(context, tableName + " Row " + String.valueOf(rowNumber) + ": form mode"),
                new ScrollableView(context, new GrowToFit(new Column(buildGrid(),
                        new GrowToFit())),
                        scrollController)).setCrossAxisAlignment(Alignment.STRETCH);
    }

    private Widget buildGrid() {
        List<Widget> items = new ArrayList<Widget>();
        ArrayList<ArrayList<String>> rows = context.getDatabase().getRows(tableName);
        List<String> columnNames = context.getDatabase().getColumnNames(tableName);

        if (rowNumber < rows.size()) {
            List<String> row = rows.get(rowNumber);
            int size = Math.min(row.size(), columnNames.size());
            for (int i = 0; i < size; i++) {
            String columnValue = row.get(i);
            String columnName = columnNames.get(i);

            items.addAll(List.of(
                    // Column name
                    new InputText(context, columnValue, text -> {
                        context.getDatabase().updateCell(tableName, columnName, rowNumber, text);
                    }).setValidationFunction(
                            name -> (context.getDatabase().isValidValue(tableName, columnName, name)))
                    ));
            }
        }
        
        return new ResizableGrid(context, new ResizableGridController(context, 1, 5), items);
    }

    @Override
    public void onTableDataChanged() {
        System.out.println("Table changed: " + context.getDatabase().getTables());
        if (context.getDatabase().getTables().contains(tableName))
            setState(() -> {
            });
        else {
            context.getDatabase().removeTableDataChangeListener(tableName, this);
            closeWindow.run();
        }
    }
}
