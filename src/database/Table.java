package database;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import database.Row.TableRowChangeListener;

public class Table extends DatabaseObject implements TableRowChangeListener {

    public interface TableDesignChangeListener {
        void onTableDesignChanged(Table table);
    }

    public interface TableRowsChangeListener {
        void onTableRowsChanged();
    }

    private Set<TableDesignChangeListener> tableDesignChangeListeners = new HashSet<>();

    private Set<TableRowsChangeListener> tableRowsChangeListeners = new HashSet<>();


    void notifyTableDesignChanged() {
        new HashSet<>(tableDesignChangeListeners).forEach(listener -> listener.onTableDesignChanged(this));
    }

    void notifyTableRowsChanged() {
        new HashSet<>(tableRowsChangeListeners).forEach(TableRowsChangeListener::onTableRowsChanged);
    }

    private String name;

    private int columnCount = 0;

    private Set<Column> columns = new HashSet<>();
    private List<Row> rows = new ArrayList<>();


    Table(History history, String name) {
        super(history);
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("arguments cannot be null or empty");
        this.name = name;
    }

    public void addTableDesignChangeListener(TableDesignChangeListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("listener cannot be null");
        tableDesignChangeListeners.add(listener);
    }

    public void removeTableDesignChangeListener(TableDesignChangeListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("listener cannot be null");
        tableDesignChangeListeners.remove(listener);
    }

    public void addTableRowsChangeListener(TableRowsChangeListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("listener cannot be null");
        tableRowsChangeListeners.add(listener);
    }

    public void removeTableRowsChangeListener(TableRowsChangeListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("listener cannot be null");
        tableRowsChangeListeners.remove(listener);
    }



    public String getName() {
        return name;
    }

    public Set<Column> getColumns() {
        return columns;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void createColumn() {
        String columnName = "Column" + columnCount++;
        while (columnNameExists(columnName)) {
            columnName = "Column" + columnCount++;
        }
        Column column = new Column(columnName);
        final List<Action> actions = new ArrayList<>();
        actions.add(new Action(() -> columns.remove(column), () -> columns.add(column)));
        actions.addAll(rows.stream().map(row -> row.createCell(column)).toList());
        history.record(new ActionList(actions).setCallback(() -> {
            notifyTableDesignChanged();
            tableRowsChangeListeners.forEach(TableRowsChangeListener::onTableRowsChanged);
        }));
    }

    public void createRow() {
        Row row = new Row(history);
        row.addTableRowChangeListener(this);
        final List<Action> actions = new ArrayList<>();
        actions.add(new Action(() -> rows.remove(row), () -> rows.add(row)));
        actions.addAll(columns.stream().map(row::createCell).toList());
        history.record(new ActionList(actions).setCallback(() -> tableRowsChangeListeners
                .forEach(TableRowsChangeListener::onTableRowsChanged)));
    }

    public void deleteColumn(Column column) {
        if (column == null || !columns.contains(column))
            throw new IllegalArgumentException("Column does not exist");
        final List<Action> actions = new ArrayList<>();
        actions.add(new Action(() -> columns.add(column), () -> columns.remove(column)));
        actions.addAll(rows.stream().map(row -> row.deleteCell(column)).toList());
        history.record(new ActionList(actions).setCallback(() -> {
            notifyTableDesignChanged();
            tableRowsChangeListeners.forEach(TableRowsChangeListener::onTableRowsChanged);
        }));
    }

    public void deleteRow(Row row) {
        if (row == null || !rows.contains(row))
            throw new IllegalArgumentException("Row does not exist");
        history.record(_deleteRow(row).setCallback(() -> tableRowsChangeListeners
                .forEach(TableRowsChangeListener::onTableRowsChanged)));
    }

    public void deleteRows(Set<Row> rows) {
        final List<Action> actions = new ArrayList<>();
        actions.addAll(rows.stream().map(this::_deleteRow).toList());
        history.record(new ActionList(actions).setCallback(() -> tableRowsChangeListeners
                .forEach(TableRowsChangeListener::onTableRowsChanged)));
    }

    public boolean allowUpdateColumnName(Column column, String newName) {
        if (column == null || !columns.contains(column))
            throw new IllegalArgumentException("Column does not exist");
        if (newName == null)
            throw new IllegalArgumentException("arguments cannot be null");
        return (column.getName().equals(newName))
                || !(newName.isEmpty() || columnNameExists(newName));
    }

    public void updateColumnName(Column column, String newName) {
        if (column == null || !columns.contains(column))
            throw new IllegalArgumentException("Column does not exist");
        if (!allowUpdateColumnName(column, newName))
            throw new IllegalArgumentException("Column name already exists");
        if (column.getName().equals(newName))
            return;
        history.record(column.updateName(newName).setCallback(() -> {
            notifyTableDesignChanged();
            tableRowsChangeListeners.forEach(TableRowsChangeListener::onTableRowsChanged);
        }));
    }

    public boolean allowUpdateColumnType(Column column, ColumnType type) {
        if (column == null || !columns.contains(column))
            throw new IllegalArgumentException("Column does not exist");
        if (type == null)
            throw new IllegalArgumentException("Column type cannot be null");
        for (Row row : rows) {
            String value = row.getCell(column).getValue();
            if ((value == "" && !column.getAllowBlank()) || !type.allowCellValue(value))
                return false;
        }
        return !((column.getDefaultValue() == "" && !column.getAllowBlank())
                || !type.allowCellValue(column.getDefaultValue()));
    }

    public void updateColumnType(Column column, ColumnType type) {
        if (column == null || !columns.contains(column))
            throw new IllegalArgumentException("Column does not exist");
        if (type == null)
            throw new IllegalArgumentException("Column type cannot be null");
        if (!allowUpdateColumnType(column, type))
            throw new IllegalArgumentException("Invalid column type");
        history.record(column.updateType(type).setCallback(() -> {
            notifyTableDesignChanged();
            tableRowsChangeListeners.forEach(TableRowsChangeListener::onTableRowsChanged);
        }));
    }

    public boolean allowUpdateColumnDefaultValue(Column column, String defaultValue) {
        if (column == null || !columns.contains(column))
            throw new IllegalArgumentException("Column does not exist");
        if (defaultValue == null)
            throw new IllegalArgumentException("Default value cannot be null");
        return column.allowCellValue(defaultValue);
    }

    public void updateColumnDefaultValue(Column column, String defaultValue) {
        if (column == null || !columns.contains(column))
            throw new IllegalArgumentException("Column does not exist");
        if (defaultValue == null)
            throw new IllegalArgumentException("Default value cannot be null");
        if (!allowUpdateColumnDefaultValue(column, defaultValue))
            throw new IllegalArgumentException("Invalid column default value");
        history.record(column.updateDefaultValue(defaultValue)
                .setCallback(this::notifyTableDesignChanged));
    }

    public boolean allowUpdateColumnAllowBlank(Column column, boolean allowBlank) {
        if (column == null || !columns.contains(column))
            throw new IllegalArgumentException("Column does not exist");
        if (allowBlank)
            return true;
        for (Row row : rows) {
            if (row.getCell(column).getValue() == "")
                return false;
        }
        return column.getDefaultValue() != "";
    }

    public void updateColumnAllowBlank(Column column, boolean allowBlank) {
        if (column == null || !columns.contains(column))
            throw new IllegalArgumentException("Column does not exist");
        if (!allowUpdateColumnAllowBlank(column, allowBlank))
            throw new IllegalArgumentException("Invalid column allow blank");
        history.record(
                column.updateAllowBlank(allowBlank).setCallback(this::notifyTableDesignChanged));
    }

    Action updateName(String newName) {
        if (newName == null || newName.isEmpty())
            throw new IllegalArgumentException("arguments cannot be null or empty");
        final String oldName = name;
        return new Action(() -> name = oldName, () -> name = newName).setCallback(() -> {
            notifyTableDesignChanged();
            tableRowsChangeListeners.forEach(TableRowsChangeListener::onTableRowsChanged);
        });
    }

    private boolean columnNameExists(String columnName) {
        for (Column column : columns) {
            if (column.getName().equals(columnName))
                return true;
        }
        return false;
    }

    private Action _deleteRow(Row row) {
        if (row == null || !rows.contains(row))
            throw new IllegalArgumentException("Row does not exist");
        return new Action(() -> rows.add(row), () -> rows.remove(row));
    }

    @Override
    public void onTableRowsChanged() {
        tableRowsChangeListeners.forEach(TableRowsChangeListener::onTableRowsChanged);
    }
}
