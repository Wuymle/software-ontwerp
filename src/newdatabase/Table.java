package newdatabase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Table extends DatabaseObject {
    private String name;
    private int columnCount = 0;
    private Set<Column> columns = new HashSet<>();
    private Set<Row> rows = new HashSet<>();

    Table(History history, String name) {
        super(history);
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("arguments cannot be null or empty");
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Set<Column> getColumns() {
        return columns;
    }

    public Set<Row> getRows() {
        return rows;
    }

    Action updateName(String newName) {
        if (newName == null || newName.isEmpty())
            throw new IllegalArgumentException("arguments cannot be null or empty");
        final String oldName = name;
        return new Action(() -> name = oldName, () -> name = newName);
    }

    private boolean columnNameExists(String columnName) {
        for (Column column : columns) {
            if (column.getName().equals(columnName))
                return true;
        }
        return false;
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
        history.record(new ActionList(actions));
    }

    public void createRow() {
        Row row = new Row(history);
        final List<Action> actions = new ArrayList<>();
        actions.add(new Action(() -> rows.remove(row), () -> rows.add(row)));
        actions.addAll(columns.stream().map(row::createCell).toList());
        history.record(new ActionList(actions));
    }

    public void deleteColumn(Column column) {
        if (column == null || !columns.contains(column))
            throw new IllegalArgumentException("Column does not exist");
        final List<Action> actions = new ArrayList<>();
        actions.add(new Action(() -> columns.add(column), () -> columns.remove(column)));
        actions.addAll(rows.stream().map(row -> row.deleteCell(column)).toList());
        history.record(new ActionList(actions));
    }

    public void deleteRow(Row row) {
        if (row == null || !rows.contains(row))
            throw new IllegalArgumentException("Row does not exist");
        history.record(_deleteRow(row));
    }

    public void deleteRows(Set<Row> rows) {
        final List<Action> actions = new ArrayList<>();
        actions.addAll(rows.stream().map(this::_deleteRow).toList());
        history.record(new ActionList(actions));
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
        history.record(column.updateName(newName));
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
        return true;
    }

    public void updateColumnType(Column column, ColumnType type) {
        if (column == null || !columns.contains(column))
            throw new IllegalArgumentException("Column does not exist");
        if (type == null)
            throw new IllegalArgumentException("Column type cannot be null");
        if (!allowUpdateColumnType(column, type))
            throw new IllegalArgumentException("Invalid column type");
        history.record(column.updateType(type));
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
        history.record(column.updateDefaultValue(defaultValue));
    }

    public boolean allowUpdateColumnAllowBlank(Column column, boolean allowBlank) {
        if (column == null || !columns.contains(column))
            throw new IllegalArgumentException("Column does not exist");
        for (Row row : rows) {
            if (row.getCell(column).getValue() != "")
                return false;
        }
        return column.getDefaultValue() == "";
    }

    public void updateColumnAllowBlank(Column column, boolean allowBlank) {
        if (column == null || !columns.contains(column))
            throw new IllegalArgumentException("Column does not exist");
        if (!allowUpdateColumnAllowBlank(column, allowBlank))
            throw new IllegalArgumentException("Invalid column allow blank");
        history.record(column.updateAllowBlank(allowBlank));
    }

    private Action _deleteRow(Row row) {
        if (row == null || !rows.contains(row))
            throw new IllegalArgumentException("Row does not exist");
        return new Action(() -> rows.add(row), () -> rows.remove(row));
    }
}
