package database;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;

/**
 * Represents a simple database system that manages tables and their columns and
 * rows.
 */
public class Database {
    private Map<String, Table> tables;

    /**
     * Constructs a new database with an empty table collection.
     */
    public Database() {
        tables = new HashMap<String, Table>();
    }

    private int tableCounter = 1;

    /**
     * Creates a new table with a unique name and adds it to the database.
     */
    public void createTable() {
        String tableName = "Table" + tableCounter++;
        while (tables.containsKey(tableName)) {
            tableName = "Table" + tableCounter++;
        }
        tables.put(tableName, new Table());
    }

    /**
     * Retrieves the set of table names in the database.
     * 
     * @return a set of table names.
     */
    public Set<String> getTables() {
        return tables.keySet();
    }

    /**
     * Deletes a table from the database.
     * 
     * @param tableName the name of the table to delete.
     */
    public void deleteTable(String tableName) {
        tables.remove(tableName);
    }

    /**
     * Renames an existing table in the database.
     *
     * @param oldName the current name of the table.
     * @param newName the new name for the table.
     * @throws Error if the old table name does not exist or the new name already
     *               exists.
     */
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

    /**
     * Checks if a table name is valid.
     * 
     * @param tableName the name of the table to check.
     * @return true if the table name is valid, false otherwise.
     */
    public boolean isValidTableName(String tableName) {
        return !tables.containsKey(tableName);
    }

    /**
     * Checks if a column name is valid for a table.
     * 
     * @param tableName  the name of the table to check.
     * @param columnName the name of the column to check.
     * @return true if the column name is valid, false otherwise.
     */
    public boolean isValidColumnName(String tableName, String columnName) {
        return !tables.get(tableName).getColumns().contains(columnName);
    }

    /**
     * Checks if a column allows blank values.
     * 
     * @param tableName  the name of the table to check.
     * @param columnName the name of the column to check.
     * @return true if the column allows blank values, false otherwise.
     */
    public boolean columnAllowBlank(String tableName, String columnName) {
        return tables.get(tableName).columnAllowBlank(columnName);
    }

    /**
     * Adds a new column to a table.
     * 
     * @param tableName the name of the table to add the column to.
     * @throws Error if the table does not exist.
     */
    public void addColumn(String tableName) {
        if (!tables.containsKey(tableName))
            throw new Error("Table does not exist");
        tables.get(tableName).createColumn();
    }

    /**
     * Adds a new row to a table.
     * 
     * @param tableName the name of the table to add the row to.
     * @throws Error if the table does not exist.
     */
    public void addRow(String tableName) {
        tables.get(tableName).createRow();
    }

    /**
     * Deletes a row from a table.
     * 
     * @param tableName the name of the table to delete the row from.
     * @param index     the index of the row to delete.
     * @throws Error if the table does not exist.
     */
    public void deleteRow(String tableName, int index) {
        tables.get(tableName).deleteRow(index);
    }

    /**
     * Deletes a column from a table.
     * 
     * @param tableName  the name of the table to delete the column from.
     * @param columnName the name of the column to delete.
     * @throws Error if the table does not exist.
     */
    public void deleteColumn(String tableName, String columnName) {
        tables.get(tableName).deleteColumn(columnName);
    }

    /**
     * Edits a cell in a table.
     * 
     * @param tableName  the name of the table containing the cell.
     * @param columnName the name of the column containing the cell.
     * @param rowIndex   the index of the row containing the cell.
     * @param value      the new value for the cell.
     */
    public void editCell(String tableName, String columnName, int rowIndex, String value) {
        tables.get(tableName).editCell(columnName, rowIndex, value);
    }

    /**
     * Retrieves the value of a cell in a table.
     * 
     * @param tableName  the name of the table containing the cell.
     * @param columnName the name of the column containing the cell.
     * @param rowIndex   the index of the row containing the cell.
     * @return the value of the cell.
     */
    public String getCell(String tableName, String columnName, int rowIndex) {
        return tables.get(tableName).getCell(columnName, rowIndex).getValue();
    }

    /**
     * Checks if a cell in a table is valid.
     * 
     * @param tableName  the name of the table containing the cell.
     * @param columnName the name of the column containing the cell.
     * @param rowIndex   the index of the row containing the cell.
     * @return true if the cell is valid, false otherwise.
     */
    public boolean isCellValid(String tableName, String columnName, int rowIndex) {
        return tables.get(tableName).getCell(columnName, rowIndex).isValid();
    }

    /**
     * Retrieves the column names of a table.
     * 
     * @param tableName the name of the table to retrieve the column names from.
     * @return an ArrayList of column names.
     */
    public ArrayList<String> getColumnNames(String tableName) {
        return tables.get(tableName).getColumns();
    }

    /**
     * Retrieves the rows of a table.
     * 
     * @param tableName the name of the table to retrieve the rows from.
     * @return an ArrayList of rows.
     */
    public ArrayList<ArrayList<String>> getRows(String tableName) {
        return tables.get(tableName).getRows();
    }

    /**
     * Retrieves a row from a table.
     * 
     * @param tableName the name of the table to retrieve the row from.
     * @param index     the index of the row to retrieve.
     * @return an ArrayList of row values.
     */
    public ArrayList<String> getRow(String tableName, int index) {
        return tables.get(tableName).getRow(index);
    }

    /**
     * Retrieves a list of all the values of a column.
     * 
     * @param tableName  the name of the table to retrieve the column from.
     * @param columnName the column to retrieve the values from.
     * @return list of column values
     */
    public ArrayList<String> getColumn(String tableName, String columnName) {
        return tables.get(tableName).getColumn(columnName);
    }

    /**
     * Retrieves the type of a column in a table.
     * 
     * @param tableName  the name of the table containing the column.
     * @param columnName the name of the column to retrieve the type of.
     * @return the type of the column.
     */
    public void editColumnType(String tableName, String columnName, ColumnType type) {
        tables.get(tableName).editColumnType(columnName, type);
    }

    /**
     * Retrieves the default value of a column in a table.
     * 
     * @param tableName  the name of the table containing the column.
     * @param columnName the name of the column to retrieve the default value of.
     * @return the default value of the column.
     */
    public void editColumnName(String tableName, String oldName, String newName) {
        tables.get(tableName).editColumnName(oldName, newName);
    }

    /**
     * Retrieves the default value of a column in a table.
     * 
     * @param tableName  the name of the table containing the column.
     * @param columnName the name of the column to retrieve the default value of.
     * @return the default value of the column.
     */
    public ColumnType getColumnType(String tableName, String columnName) {
        return tables.get(tableName).getColumnType(columnName);
    }

    /**
     * Retrieves the default value of a column in a table.
     * 
     * @param tableName  the name of the table containing the column.
     * @param columnName the name of the column to retrieve the default value of.
     * @return the default value of the column.
     */
    public String getDefaultColumnValue(String tableName, String columnName) {
        return tables.get(tableName).getDefaultColumnValue(columnName);
    }

    /**
     * Retrieves the default value of a column in a table.
     * 
     * @param tableName  the name of the table containing the column.
     * @param columnName the name of the column to retrieve the default value of.
     * @return the default value of the column.
     */
    public void editDefaultColumnValue(String tableName, String columnName, String value) {
        tables.get(tableName).editDefaultColumnValue(columnName, value);
    }

    /**
     * Retrieves the default value of a column in a table.
     * 
     * @param tableName  the name of the table containing the column.
     * @param columnName the name of the column to retrieve the default value of.
     * @return the default value of the column.
     */
    public void toggleColumnType(String tableName, String columnName) {
        tables.get(tableName).toggleColumnType(columnName);
    }

    /**
     * Sets the allow blank state of a column.
     * 
     * @param tableName  the name of the table containing the column.
     * @param columnName the name of the column to retrieve the default value of.
     * @param allowBlank the value to set the allow blank state to.
     */
    public void setColumnAllowBlank(String tableName, String columnName, boolean allowBlank) {
        tables.get(tableName).setColumnAllowBlank(columnName, allowBlank);
    }

    /**
     * Retrieves the default value of a column in a table.
     * 
     * @param tableName  the name of the table containing the column.
     * @param columnName the name of the column to retrieve the default value of.
     * @return the default value of the column.
     */
    public boolean getValidDefaultValue(String tableName, String columnName) {
        return tables.get(tableName).getValidDefaultValue(columnName);
    }

    public boolean isValidColumnValue(String tableName, String columnName, String value) {
        return tables.get(tableName).isValidColumnValue(columnName, value);
    }
}