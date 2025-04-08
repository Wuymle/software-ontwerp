package database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a table in a database, containing multiple columns and rows.
 */
public class Table {
    private Map<String, Column> columns;
    private ArrayList<Row> rows;

    /**
     * Constructs a new Table with an empty column collection and row list.
     */
    public Table() {
        columns = new LinkedHashMap<>(); // Changed to LinkedHashMapged to LinkedHashMap
        rows = new ArrayList<>();
    }

    /**
     * Generates an incremental number for a new column name.
     */
    private int columnCounter = 1;

    /**
     * Creates a new column in the table with a default name and type.
     */
    public void createColumn() {
        Column newColumn = new Column();
        for (Row row : rows) {
            row.createCell(newColumn);
        }
        String columnName = "Column" + columnCounter++;
        while (getColumns().contains(columnName)) {
            columnName = "Column" + columnCounter++;
        }
        columns.put(columnName, newColumn);
    }

    /**
     * Creates a new column in the table with the given name and type.
     *
     * @param name the name of the new column.
     * @param type the type of the new column.
     */
    public void createRow() {
        Row newRow = new Row();
        newRow.createCells(new ArrayList<>(columns.values())); // Pass the collection of columns
        rows.add(newRow);
    }

    /**
     * Retrieves the set of column names in the table.
     *
     * @return a set of column names.
     */
    public ArrayList<String> getColumns() {
        return new ArrayList<>(columns.keySet());
    }

    /**
     * Retrieves a list of all the values of a column.
     * 
     * @param columnName the column to retrieve the values from.
     * @return list of column values
     */
    public ArrayList<String> getColumn(String columnName) {
        Column column = columns.get(columnName);
        ArrayList<String> result = new ArrayList<String>();

        for (Cell cell : column.getCells()) {
            result.add(cell.getValue());
        }

        return result;
    }

    /**
     * Retrieves the set of row indices in the table.
     *
     * @return a set of row indices.
     */
    public ColumnType getColumnType(String name) {
        if (!columns.containsKey(name)) {
            throw new Error("Column does not exist");
        }
        return columns.get(name).getType();
    }

    /**
     * Retrieves the set of row indices in the table.
     *
     * @return a set of row indices.
     */
    public ArrayList<ArrayList<String>> getRows() {
        ArrayList<ArrayList<String>> rowValues = new ArrayList<>();

        for (int i = 0; i < rows.size(); i++) {
            rowValues.add(getRow(i));
        }

        return rowValues;
    }

    /**
     * Retrieves the values of a row in the table.
     *
     * @param index the index of the row to retrieve.
     * @return a list of values in the row.
     */
    public ArrayList<String> getRow(int index) {
        ArrayList<String> row = new ArrayList<>();

        for (Cell cell : rows.get(index).getCells()) {
            row.add(cell.getValue());
        }

        return row;
    }

    /**
     * Deletes a row from the table at the specified index.
     *
     * @param index the index of the row to delete.
     */
    public void deleteRow(int index) {
        rows.remove(index);
        columns.values().forEach(column -> column.deleteCell(index));
    }

    public void deleteRows(ArrayList<Integer> indices) {
        Collections.sort(indices);
        for (int i = 0; i < indices.size(); i++) {
            deleteRow(indices.get(i));
            indices = new ArrayList<Integer>(indices.stream().map(index -> index - 1).toList());
        }
    }

    /**
     * Deletes a column from the table with the specified name.
     *
     * @param name the name of the column to delete.
     */
    public void deleteColumn(String name) {
        if (!columns.containsKey(name)) {
            throw new Error("Column does not exist");
        }

        for (Row row : rows) {
            row.deleteCell(new ArrayList<>(columns.keySet()).indexOf(name));
        }

        columns.remove(name);
    }

    /**
     * Retrieves the default value of a column.
     *
     * @param name the name of the column to retrieve the default value for.
     * @return the default value of the column.
     */
    public boolean columnAllowBlank(String name) {
        return columns.get(name).getAllowBlank();
    }

    /**
     * Retrieves the default value of a column.
     *
     * @param name the name of the column to retrieve the default value for.
     * @return the default value of the column.
     */
    public Cell getCell(String columnName, int rowIndex) {
        return rows.get(rowIndex).getCells().get(new ArrayList<>(columns.keySet()).indexOf(columnName));
    }

    /**
     * Edits the value of a cell in the table.
     *
     * @param columnName the name of the column containing the cell.
     * @param rowIndex   the index of the row containing the cell.
     * @param value      the new value for the cell.
     */
    public void editCell(String columnName, int rowIndex, String value) {
        rows.get(rowIndex).getCells().get(new ArrayList<>(columns.keySet()).indexOf(columnName)).setValue(value);
    }

    /**
     * Edits the name of a column in the table.
     *
     * @param oldname the current name of the column.
     * @param newName the new name for the column.
     */
    public void editColumnName(String oldname, String newName) {
        if (!columns.containsKey(oldname)) {
            throw new Error("Column does not exist");
        }

        Column column = columns.get(oldname);
        columns.remove(oldname);
        columns.put(newName, column);
    }

    /**
     * Edits the type of a column in the table.
     *
     * @param name the name of the column to edit.
     * @param type the new type for the column.
     */
    public void editColumnType(String name, ColumnType type) {
        if (!columns.containsKey(name)) {
            throw new Error("Column does not exist");
        }

        columns.get(name).editColumnType(type);
    }

    /**
     * Edits the default value of a column in the table.
     *
     * @param name  the name of the column to edit.
     * @param value the new default value for the column.
     */
    public void editDefaultColumnValue(String name, String value) {
        if (!columns.containsKey(name)) {
            throw new Error("Column does not exist");
        }

        columns.get(name).setDefaultValue(value);
    }

    /**
     * Toggles the type of a column in the table.
     *
     * @param name the name of the column to toggle.
     */
    public void toggleColumnType(String name) {
        if (!columns.containsKey(name)) {
            throw new Error("Column does not exist");
        }

        columns.get(name).toggleColumnType();
    }

    /**
     * Retrieves the default value of a column in the table.
     *
     * @param name the name of the column to retrieve the default value of.
     * @return the default value of the column.
     */
    public String getDefaultColumnValue(String name) {
        if (!columns.containsKey(name)) {
            throw new Error("Column does not exist");
        }

        return columns.get(name).getDefaultValue();
    }

    /**
     * Edit the allow blank state of the column.
     * 
     * @param name       name of the column to edit.
     * @param allowBlank value to set.
     */
    public void setColumnAllowBlank(String name, boolean allowBlank) {
        if (!columns.containsKey(name)) {
            throw new Error("Column does not exist");
        }
        columns.get(name).setAllowBlank(allowBlank);
    }

    public boolean isValidValue(String columnName, String value) {
        if (!columns.containsKey(columnName)) {
            throw new Error("Column does not exist");
        }

        return columns.get(columnName).isValidValue(value);
    }

    public boolean isValidAllowBlankValue(String columnName, boolean value) {
        if (!columns.containsKey(columnName)) {
            throw new Error("Column does not exist");
        }

        return columns.get(columnName).isValidAllowBlankValue(value);
    }
}