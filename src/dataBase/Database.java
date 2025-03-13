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

    public void createTable(String tableName) {
        if (tables.containsKey(tableName))
            throw new Error("Table already exists");
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
        if (tables.containsKey(newName))
            throw new Error("Table already exists");

        tables.put(newName, tables.get("oldName"));
        tables.remove(oldName);
    }

    public void addColumn(String tableName, String columnName, ColumnType type, boolean allowBlank) {
        if (!tables.containsKey(tableName))
            throw new Error("Table does not exist");
        tables.get(tableName).createColumn(columnName, type, allowBlank);
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

    public ArrayList<String> getColumnNames(String tableName) {
        return tables.get(tableName).getColumns();
    }

    public ArrayList<Object> getRows(String tableName) {
        return tables.get(tableName).getRows();
    }

    public ArrayList<Object> getRow(String tableName, int index) {
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
}
