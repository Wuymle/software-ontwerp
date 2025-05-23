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
import newdatabase.Row;
import newdatabase.Table;
import newdatabase.Table.TableRowsChangeListener;

/**
 * A screen that represents the table design mode view.
 */
public class TableFormsView extends DatabaseScreen implements TableRowsChangeListener {
    Integer rowNumber;
    Table table;
    List<String> selectedColumns = new ArrayList<String>();
    Consumer<Void> onClose;
    final ScrollController scrollController = new ScrollController(context);

    /**
     * Constructor for the table design mode view.
     * 
     * @param context The context of the application.
     */
    public TableFormsView(DatabaseAppContext context, Table table, Consumer<Void> onClose) {
        super(context);
        this.rowNumber = 0;
        this.table = table;
        table.addTableRowsChangeListener(this);
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
                            if (rowNumber < table.getRows().size())
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
                        if ((modifiers & KeyEvent.CTRL_DOWN_MASK) == 0)
                            return false;

                        setState(() -> {
                            if (rowNumber < table.getRows().size())
                                table.deleteRow(null);
                        });
                        return true;
                    case KeyEvent.VK_N:
                        if ((modifiers & KeyEvent.CTRL_DOWN_MASK) == 0)
                            return false;
                        setState(() -> {
                            table.createRow();
                            rowNumber = table.getRows().size() - 1;
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
                        table.getName() + " Row " + String.valueOf(rowNumber + 1) + ": form mode"),
                new ScrollableView(context, buildGrid(), scrollController))
                        .setCrossAxisAlignment(Alignment.STRETCH).setDecoration(Style.background);
    }

    private Widget buildGrid() {
        List<Widget> items = new ArrayList<Widget>();
        List<Row> rows = table.getRows();

        items.addAll(List.of(
                new Padding(new Text("Column Name").setFontColor(Style.white).setFontSize(18))
                        .all(10).setHorizontalAlignment(Alignment.END)
                        .setDecoration(Style.formsViewHeader),
                new Padding(new Text("Cell Value").setFontColor(Style.white).setFontSize(18))
                        .all(10).setDecoration(Style.formsViewHeader)));

        if (rowNumber >= rows.size())
            return new Text("No rows found");
        Row row = rows.get(rowNumber);
        for (newdatabase.Column column : table.getColumns()) {
            items.addAll(List.of(new Center(new Padding(new Text(column.getName()).setFontSize(17)).all(10)),
                    new Padding(new GrowToFit(new Padding(new GrowToFit(new ValueCell(context, column, row.getCell(column).getValue(),
                            text -> row.updateCellValue(column, text),
                            text -> row.allowUpdateCellValue(column, text)))).all(10))
                                    .setDecoration(Style.formsViewValue)).all(10)));
        }

        return new Center(
                new Grid(2, Orientation.VERTICAL, false, items).setDecoration(Style.formsViewGrid));
    }

    @Override
    public void onTableRowsChanged() {
        if (context.getDatabase().getTables().contains(table))
            setState(() -> {
            });
        else {
            close();
        }
    }

    @Override
    public void close() {
        table.removeTableRowsChangeListener(this);
        onClose.accept(null);
    }
}
