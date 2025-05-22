package newdatabase;

import java.util.HashSet;
import java.util.Set;

public class Table {
    private String name;
    private int columnCount = 0;
    private Set<Column> columns = new HashSet<>();
    private Set<Row> rows = new HashSet<>();

    Table(String name) {
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

    void updateName(String newName) {
        if (newName == null || newName.isEmpty())
            throw new IllegalArgumentException("arguments cannot be null or empty");
        this.name = newName;
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
        columns.add(column);
        rows.forEach(row -> row.createCell(column));
    }

    public void createRow() {
        Row row = new Row();
        columns.forEach(row::createCell);
        rows.add(row);
    }

    public void deleteColumn(Column column) {
        if (column == null || !columns.contains(column))
            throw new IllegalArgumentException("Column does not exist");
        columns.remove(column);
        rows.forEach(row -> row.deleteCell(column));
    }

    public void deleteRow(Row row) {
        if (row == null || !rows.contains(row))
            throw new IllegalArgumentException("Row does not exist");
        rows.remove(row);
    }

    public void deleteRows(Set<Row> rows) {
        rows.forEach(this::deleteRow);
    }

    public boolean allowUpdateColumnName(Column column, String newName) {
        if (column == null || !columns.contains(column))
            throw new IllegalArgumentException("Column does not exist");
        if (newName == null)
            throw new IllegalArgumentException("arguments cannot be null");
        return (column.getName().equals(newName)) || !(newName.isEmpty() || columnNameExists(newName));
    }

    public void updateColumnName(Column column, String newName) {
        if (column == null || !columns.contains(column))
            throw new IllegalArgumentException("Column does not exist");
        if (!allowUpdateColumnName(column, newName))
            throw new IllegalArgumentException("Column name already exists");
        if (column.getName().equals(newName))
            return;
        column.updateName(newName);
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
        column.updateType(type);
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
        column.updateDefaultValue(defaultValue);
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
        column.updateAllowBlank(allowBlank);
    }
}
