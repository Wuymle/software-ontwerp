package database;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;

public class Database {
    private Map<String, Table> tables;

    public Database() {
        tables = new HashMap<String, Table>();
    }

    private int tableCounter = 1;

    public void createTable() {
        String tableName = "Table" + tableCounter++;
        while (tables.containsKey(tableName)) {
            tableName = "Table" + tableCounter++;
        }
        tables.put(tableName, new Table());
    }

    public Set<String> getTables() {
        return tables.keySet();
    }

    // Methods for the current table

    public void deleteTable(String tableName) {
        tables.remove(tableName);
    }

    public void editTableName(String oldName, String newName) {
        if (!tables.containsKey(oldName))
            throw new Error("Table does not exist");
        if (oldName.equals(newName))
            return;
        if (tables.containsKey(newName))
            throw new Error("Table already exists");

        tables.put(newName, tables.get("oldName"));
        tables.remove(oldName);
    }

    public boolean isValidTableName(String tableName) {
        return !tables.containsKey(tableName);
    }

    public boolean isValidColumnName(String tableName, String columnName) {
        return !tables.get(tableName).getColumns().contains(columnName);
    }

    public boolean columnAllowBlank(String tableName, String columnName) {
        return tables.get(tableName).columnAllowBlank(columnName);
    }

    private int columnCounter = 1;

    public void addColumn(String tableName) {
        if (!tables.containsKey(tableName))
            throw new Error("Table does not exist");
        String columnName = "Column" + columnCounter++;
        tables.get(tableName).createColumn(columnName);
    }

    public void addRow(String tableName) {
        tables.get(tableName).createRow();
    }

    public void deleteRow(String tableName, int index) {
        tables.get(tableName).deleteRow(index);
    }

    public void deleteColumn(String tableName, String columnName) {
        tables.get(tableName).deleteColumn(columnName);
    }

    public void editCell(String tableName, String columnName, int rowIndex, String value) {
        tables.get(tableName).editCell(columnName, rowIndex, value);
    }

    public Object getCell(String tableName, String columnName, int rowIndex) {
        return tables.get(tableName).getCell(columnName, rowIndex).getValue();
    }

    public boolean isCellValid(String tableName, String columnName, int rowIndex) {
        return tables.get(tableName).getCell(columnName, rowIndex).isValid();
    }

    public ArrayList<String> getColumnNames(String tableName) {
        return tables.get(tableName).getColumns();
    }

    public ArrayList<ArrayList<String>> getRows(String tableName) {
        return tables.get(tableName).getRows();
    }

    public ArrayList<String> getRow(String tableName, int index) {
        return tables.get(tableName).getRow(index);
    }

    public void editColumnType(String tableName, String columnName, ColumnType type) {
        tables.get(tableName).editColumnType(columnName, type);
    }

    public void editColumnName(String tableName, String oldName, String newName) {
        tables.get(tableName).editColumnName(oldName, newName);
    }

    public ColumnType getColumnType(String tableName, String columnName) {
        return tables.get(tableName).getColumnType(columnName);
    }

    public String getDefaultColumnValue(String tableName, String columnName) {
        return tables.get(tableName).getDefaultColumnValue(columnName);
    }

    public void editDefaultColumnValue(String tableName, String columnName, String value) {
        tables.get(tableName).editDefaultColumnValue(columnName, value);
    }

    public void toggleColumnType(String tableName, String columnName) {
        tables.get(tableName).toggleColumnType(columnName);
    }
}