package application.screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import application.DatabaseAppContext;
import application.resources.Style;
import application.widgets.Header;
import application.widgets.ValueCell;
import clutter.abstractwidgets.Widget;
import clutter.core.Orientation;
import clutter.core.ScrollController;
import clutter.decoratedwidgets.Text;
import clutter.layoutwidgets.Box;
import clutter.layoutwidgets.Center;
import clutter.layoutwidgets.Column;
import clutter.layoutwidgets.Grid;
import clutter.layoutwidgets.GrowToFit;
import clutter.layoutwidgets.Padding;
import clutter.layoutwidgets.ScrollableView;
import clutter.layoutwidgets.enums.Alignment;
import database.Database.TableDataChangeListener;

/**
 * A screen that represents the table design mode view.
 */
public class TableFormsView extends DatabaseScreen implements TableDataChangeListener {
    Integer rowNumber;
    String tableName;
    List<String> selectedColumns = new ArrayList<String>();
    Consumer<Void> onClose;
    final ScrollController scrollController = new ScrollController(context);

    /**
     * Constructor for the table design mode view.
     * 
     * @param context The context of the application.
     */
    public TableFormsView(DatabaseAppContext context, String tableName, Consumer<Void> onClose) {
        super(context);
        this.rowNumber = 0;
        this.tableName = tableName;
        context.getDatabase().addTableDataChangeListener(tableName, this);
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
                    case KeyEvent.VK_PAGE_UP:
                        setState(() -> {
                            if (rowNumber < context.getDatabase().getRows(tableName).size())
                                rowNumber++;
                        });
                        return true;
                    case KeyEvent.VK_PAGE_DOWN:
                        setState(() -> {
                            if (rowNumber > 0)
                                rowNumber--;
                        });
                        return true;
                    case KeyEvent.VK_D:
                        if ((modifiers & KeyEvent.CTRL_DOWN_MASK) == 0) return false;

                        setState(() -> {
                            if (rowNumber < context.getDatabase().getRows(tableName).size())
                                context.getDatabase().deleteRow(tableName, rowNumber);
                        });
                        return true;
                    case KeyEvent.VK_N:
                        if ((modifiers & KeyEvent.CTRL_DOWN_MASK) == 0) return false;
                        setState(() -> {
                            context.getDatabase().addRow(tableName);
                            rowNumber = context.getDatabase().getRows(tableName).size() - 1;
                        });
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
        return new Column(
                new Header(context,
                        tableName + " Row " + String.valueOf(rowNumber + 1) + ": form mode"),
                new ScrollableView(context,
                        // new GrowToFit(new Center(
                                    buildGrid()
                                    // ))
                                    ,
                        scrollController)).setCrossAxisAlignment(Alignment.STRETCH);
    }

    private Widget buildGrid() {
        List<Widget> items = new ArrayList<Widget>();
        ArrayList<ArrayList<String>> rows = context.getDatabase().getRows(tableName);
        List<String> columnNames = context.getDatabase().getColumnNames(tableName);

        items.addAll(List.of(
                // Column name
                new Padding(new Text("Column Name").setFontColor(Style.white)).all(5).setDecoration(Style.decorationHeader),
                new Padding(new Text("Cell Value").setFontColor(Style.white)).all(5).setDecoration(Style.decorationHeader)));

        if (rowNumber < rows.size()) {
            List<String> row = rows.get(rowNumber);
            int size = Math.min(row.size(), columnNames.size());
            for (int i = 0; i < size; i++) {
                String columnValue = row.get(i);
                String columnName = columnNames.get(i);

                items.addAll(List.of(
                        // Column name
                        new Center(new Padding(new Text(columnName)).all(5).setDecoration(Style.decorationText)),

                        new Padding(new GrowToFit(
                                new Padding(new ValueCell(context,
                                                        context.getDatabase().getColumnType(tableName, columnName),
                                                        context.getDatabase().columnAllowBlank(tableName, columnName), 
                                                        columnValue, 
                                                        text -> {
                                                                    if (rowNumber < context.getDatabase().getRows(tableName).size())
                                                                    {
                                                                        context.getDatabase().updateCell(tableName, columnName, rowNumber, text);
                                                                    }
                                                                },
                                                        name -> (context.getDatabase().isValidValue(tableName, columnName, name))))
                                                .all(5)
                                                .setDecoration(Style.decorationInput))).vertical(5)
                                                                .horizontal(10)));
            }
        }

        return new Box(new Grid(2, Orientation.VERTICAL, false, items).setDecoration(Style.decorationPanel))
                                .setDecoration(Style.decorationPanel2);
    }

    @Override
    public void onTableDataChanged() {
        System.out.println("Table changed: " + context.getDatabase().getTables());
        if (context.getDatabase().getTables().contains(tableName))
            setState(() -> {
            });
        else {
            context.getDatabase().removeTableDataChangeListener(tableName, this);
            onClose.accept(null);
        }
    }

    
    

}
