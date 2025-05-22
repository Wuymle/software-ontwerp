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
import database.ColumnType;
import database.Database.TableDesignChangeListener;

/**
 * A screen that represents the table design mode view.
 */
public class TableDesignView extends DatabaseScreen
        implements TableDesignChangeListener, ResizableGridSubscriber {
    String tableName;
    List<String> selectedColumns = new ArrayList<String>();
    Consumer<String> onOpenRowsView;
    Consumer<Void> onClose;
    final ScrollController scrollController = new ScrollController(context);
    final ResizableGridController resizableGridController;
    private final String[] COLUMN_TYPES = {"STRING", "INTEGER", "BOOLEAN", "EMAIL"};


    /**
     * Constructor for the table design mode view.
     * 
     * @param context The context of the application.
     */
    public TableDesignView(DatabaseAppContext context, String tableName,
            ResizableGridController resizableGridController, Consumer<String> onOpenRowsView,
            Consumer<Void> onClose) {
        super(context);
        this.tableName = tableName;
        context.getDatabase().addTableDesignChangeListener(tableName, this);
        this.onOpenRowsView = onOpenRowsView;
        this.onClose = onClose;
        this.resizableGridController = resizableGridController;
        resizableGridController.addSubscriber(this);
    }

    /**
     * Builds the table design mode view.
     * 
     * @return The table design mode view.
     */
    @Override
    public Widget build() {
        return new Column(new Header(context, tableName + ": design mode"),
                new ScrollableView(context, new GrowToFit(new Column(buildGrid(),
                        new GrowToFit(new Clickable(new ConstrainedBox().setMinHeight(50),
                                () -> setState(() -> context.getDatabase().addColumn(tableName)),
                                2)))),
                        scrollController)).setCrossAxisAlignment(Alignment.STRETCH);
    }

    private Widget buildGrid() {
        List<Widget> items = new ArrayList<Widget>();
        items.addAll(List.of(new NullWidget(),
                new Padding(new Text("Column name").setFontSize(20)).all(5),
                new Padding(new Text("Column type").setFontSize(20)).all(5),
                new Padding(new Text("Allow blank").setFontSize(20)).all(5),
                new Padding(new Text("Default value").setFontSize(20)).all(5)));

        for (String columnName : context.getDatabase().getColumnNames(tableName)) {
            items.addAll(List.of(
                    // Selection checkbox
                    new Center(new CheckBox(context, checked -> {
                        if (checked)
                            selectedColumns.add(columnName);
                        else
                            selectedColumns.remove(columnName);
                    })),
                    // Column name
                    new InputText(context, columnName, text -> {
                        context.getDatabase().updateColumnName(tableName, columnName, text);
                    }).setValidationFunction(
                            name -> !(context.getDatabase().getColumnNames(tableName).contains(name)
                                    && name != columnName && !name.isEmpty())),
                    // Column type
                    new CycleButton(context, COLUMN_TYPES,
                            Arrays.asList(COLUMN_TYPES)
                                    .indexOf(context.getDatabase()
                                            .getColumnType(tableName, columnName).name()),
                            type -> context.getDatabase().updateColumnType(tableName, columnName,
                                    ColumnType.valueOf(type)))
                                            .setValidationFunction(type -> context.getDatabase()
                                                    .isValidColumnType(tableName, columnName,
                                                            ColumnType.valueOf(type))),
                    // Allow blank checkbox
                    new Center(new CheckBox(context,
                            context.getDatabase().columnAllowBlank(tableName, columnName),
                            allowBlank -> context.getDatabase().setColumnAllowBlank(tableName,
                                    columnName, allowBlank)).setValidationFunction(
                                            b -> context.getDatabase().isValidAllowBlankValue(
                                                    tableName, columnName, b))),
                    // Default value
                    new ValueCell(context,
                            context.getDatabase().getColumnType(tableName, columnName),
                            context.getDatabase().columnAllowBlank(tableName, columnName),
                            context.getDatabase().getDefaultColumnValue(tableName, columnName),
                            text -> context.getDatabase().updateDefaultColumnValue(tableName,
                                    columnName, text),
                            text -> context.getDatabase().isValidValue(tableName, columnName,
                                    text))));

        }

        return new ResizableGrid(context, resizableGridController, items);
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

                    case KeyEvent.VK_ENTER: {
                        if ((modifiers & KeyEvent.CTRL_DOWN_MASK) != 0) {
                            onOpenRowsView.accept(tableName);
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
    public void onTableChanged() {
        if (context.getDatabase().getTables().contains(tableName))
            setState(() -> {
            });
        else {
            context.getDatabase().removeTableDesignChangeListener(tableName, this);
            onClose.accept(null);
        }
    }

    @Override
    public void onColumnResize() {
        setState(() -> {
        });
    }
}
