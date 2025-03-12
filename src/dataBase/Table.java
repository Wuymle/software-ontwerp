package dataBase;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Table {
    private Map<String, Column> columns;
    private ArrayList<Row> rows;

    public Table() {
        columns = new LinkedHashMap<>(); // Changed to LinkedHashMapged to LinkedHashMap
        rows = new ArrayList<>();
    }

    public void createColumn(String name, columnType type, boolean allowBlank) {
        Column newColumn = new Column();
        for (Row row : rows) {
            row.createCell(newColumn);
        }
        columns.put(name, newColumn);
    }

    public void createRow() {
        Row newRow = new Row();
        newRow.createCells(new ArrayList<>(columns.values())); // Pass the collection of columns
        rows.add(newRow);
    }

    public ArrayList<String> getColumns() {
        return new ArrayList<>(columns.keySet());
    }

    public columnType getColumnType(String name) {
        if (!columns.containsKey(name)) {
            throw new Error("Column does not exist");
        }
        return columns.get(name).getType();
    }

    public ArrayList<Object> getRows() {
        ArrayList<Object> rowValues = new ArrayList<>();

        for (int i = 0; i < rows.size(); i++) {
            rowValues.add(getRow(i));
        }

        return rowValues;
    }

    public ArrayList<Object> getRow(int index) {
        ArrayList<Object> row = new ArrayList<>();

        for (Cell cell : rows.get(index).getCells()) {
            row.add(cell.getValue());
        }

        return row;
    }

    public void deleteRow(int index) {
        rows.remove(index);
    }

    public void deleteColumn(String name) {
        if (!columns.containsKey(name)) {
            throw new Error("Column does not exist");
        }

        for (Row row : rows) {
            row.deleteCell(new ArrayList<>(columns.keySet()).indexOf(name));
        }

        columns.remove(name);
    }

    public boolean columnAllowBlank(String name) {
        return columns.get(name).getAllowBlank();
    }

    public Cell getCell(String columnName, int rowIndex) {
        return rows.get(rowIndex).getCells().get(new ArrayList<>(columns.keySet()).indexOf(columnName));
    }

    public void editCell(String columnName, int rowIndex, String value) {
        rows.get(rowIndex).getCells().get(new ArrayList<>(columns.keySet()).indexOf(columnName)).setValue(value);
    }

    public void editColumnName(String oldname, String newName) {
        if (!columns.containsKey(oldname)) {
            throw new Error("Column does not exist");
        }

        Column column = columns.get(oldname);
        columns.remove(oldname);
        columns.put(newName, column);
    }

    public void editColumnType(String name, columnType type) {
        if (!columns.containsKey(name)) {
            throw new Error("Column does not exist");
        }

        columns.get(name).editColumnType(type);
    }
}