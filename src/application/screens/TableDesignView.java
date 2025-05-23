package application.screens;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import application.DatabaseAppContext;
import application.resources.Style;
import application.widgets.Header;
import application.widgets.ValueCell;
import clutter.abstractwidgets.Widget;
import clutter.core.Decoration;
import clutter.core.ResizableGridController;
import clutter.core.ScrollController;
import clutter.core.ResizableGridController.ResizableGridSubscriber;
import clutter.decoratedwidgets.Text;
import clutter.inputwidgets.CheckBox;
import clutter.inputwidgets.Clickable;
import clutter.inputwidgets.CycleButton;
import clutter.inputwidgets.InputText;
import clutter.layoutwidgets.Center;
import clutter.layoutwidgets.Column;
import clutter.layoutwidgets.ConstrainedBox;
import clutter.layoutwidgets.GrowToFit;
import clutter.layoutwidgets.NullWidget;
import clutter.layoutwidgets.Padding;
import clutter.layoutwidgets.ResizableGrid;
import clutter.layoutwidgets.ScrollableView;
import clutter.layoutwidgets.enums.Alignment;
import newdatabase.ColumnType;
import newdatabase.Table;
import newdatabase.Table.TableDesignChangeListener;

/**
 * A screen that represents the table design mode view.
 */
public class TableDesignView extends DatabaseScreen
        implements TableDesignChangeListener, ResizableGridSubscriber {
    Table table;
    List<newdatabase.Column> selectedColumns = new ArrayList<newdatabase.Column>();
    Consumer<Table> onOpenRowsView;
    Consumer<Void> onClose;
    final ScrollController scrollController = new ScrollController(context);
    final ResizableGridController resizableGridController;
    // private String[] columnTypes =
    // Arrays.stream(ColumnType.values()).map(v -> v.toString()).toArray(String[]::new);


    /**
     * Constructor for the table design mode view.
     * 
     * @param context The context of the application.
     */
    public TableDesignView(DatabaseAppContext context, Table table,
            ResizableGridController resizableGridController, Consumer<Table> onOpenRowsView,
            Consumer<Void> onClose) {
        super(context);
        this.table = table;
        table.addTableDesignChangeListener(this);
        this.onOpenRowsView = onOpenRowsView;
        this.onClose = onClose;
        this.resizableGridController = resizableGridController;
        resizableGridController.addSubscriber(this);
        table.addTableDesignChangeListener(this);
    }

    /**
     * Builds the table design mode view.
     * 
     * @return The table design mode view.
     */
    @Override
    public Widget build() {
        return new Column(new Header(context, table.getName() + ": Design mode"),
                new ScrollableView(context,
                        new GrowToFit(new Column(buildGrid(),
                                new GrowToFit(new Clickable(new ConstrainedBox().setMinHeight(50),
                                        () -> setState(() -> table.createColumn()), 2)))),
                        scrollController)).setCrossAxisAlignment(Alignment.STRETCH);
    }

    private Widget buildGrid() {
        List<Widget> items = new ArrayList<Widget>();
        items.addAll(List.of(new NullWidget().setDecoration(Style.decorationHeader),
                new Padding(new Text("Column name").setFontSize(20).setFontColor(Style.white))
                        .all(5).setDecoration(Style.decorationHeader),
                new Padding(new Text("Column type").setFontSize(20).setFontColor(Style.white))
                        .all(5).setDecoration(Style.decorationHeader),
                new Padding(new Text("Allow blank").setFontSize(20).setFontColor(Style.white))
                        .all(5).setDecoration(Style.decorationHeader),
                new Padding(new Text("Default value").setFontSize(20).setFontColor(Style.white))
                        .all(5).setDecoration(Style.decorationHeader)));

        for (newdatabase.Column column : table.getColumns()) {
            items.addAll(List.of(
                    // Selection checkbox
                    new Center(new CheckBox(context, checked -> {
                        if (checked)
                            selectedColumns.add(column);
                        else
                            selectedColumns.remove(column);
                    }, selectedColumns.contains(column))),
                    // Column name
                    new Padding(new Padding(new InputText(context, column.getName(), text -> {
                        table.updateColumnName(column, text);
                    }).setValidationFunction(name -> table.allowUpdateColumnName(column, name)))
                            .all(5).setDecoration(Style.decorationInput)).all(5),
                    // Column type
                    new Padding(new Padding(new CycleButton<ColumnType>(context,
                            ColumnType.values(),
                            Arrays.asList(ColumnType.values()).indexOf(column.getType()),
                            type -> table.updateColumnType(column, type))
                                    .setValidationFunction(
                                            type -> table.allowUpdateColumnType(column, type))
                                    .setDecoration(new Decoration().setColor(Color.gray)
                                            .setFillAlpha(0.5f))).all(5)
                                                    .setDecoration(Style.decorationInput)).all(5),
                    // Allow blank checkbox
                    new Center(new CheckBox(context, column.getAllowBlank(),
                            allowBlank -> table.updateColumnAllowBlank(column, allowBlank))
                                    .setValidationFunction(
                                            b -> table.allowUpdateColumnAllowBlank(column, b))),
                    // Default value
                    new ValueCell(context, column, column.getDefaultValue(),
                            text -> table.updateColumnDefaultValue(column, text),
                            text -> table.allowUpdateColumnDefaultValue(column, text))));

            // Dit is de gedecoreerde code van ValueCell, maar breekt textInput om een of andere
            // reden. Het werkt wel in formsView en TableRows
            // new Padding(new GrowToFit(new Padding(new ValueCell(context,
            // context.getDatabase().getColumnType(tableName, columnName),
            // context.getDatabase().columnAllowBlank(tableName, columnName),
            // context.getDatabase().getDefaultColumnValue(tableName, columnName),
            // text -> context.getDatabase().updateDefaultColumnValue(tableName,
            // columnName, text),
            // text -> context.getDatabase().isValidValue(tableName, columnName,
            // text))).all(5).setDecoration(Style.decorationInput))).all(5)));

        }

        return new Padding(new ResizableGrid(context, resizableGridController, items)
                .setDecoration(Style.decorationPanel)).setDecoration(Style.decorationPanel2);
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
                            selectedColumns.forEach(table::deleteColumn);
                            selectedColumns.clear();
                        });
                        return true;

                    case KeyEvent.VK_ESCAPE:
                        return true;

                    case KeyEvent.VK_ENTER: {
                        if ((modifiers & KeyEvent.CTRL_DOWN_MASK) != 0) {
                            onOpenRowsView.accept(table);
                            return true;
                        }
                        return false;
                    }

                    default:
                        return false;
                }
            default:
                return false;
        }
    }

    @Override
    public void onColumnResize() {
        setState(() -> {
        });
    }

    @Override
    public void onTableDesignChanged(Table table) {
        System.out.println("Table design changed: " + table.getName());
        if (context.getDatabase().getTables().contains(this.table))
            setState(() -> {
            });
        else {
            System.out.println("closing because table is deleted");
            close();
        }
    }

    @Override
    public void close() {
        table.removeTableDesignChangeListener(this);
        onClose.accept(null);
    }
}
