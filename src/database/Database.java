package database;

import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;

/**
 * Represents a simple database system that manages tables and their columns and rows.
 */
public class Database {
    public interface TableDesignChangeListener {
        void onTableChanged();
    }

    public interface TableNameChangeListener {
        void onTableNameChanged();
    }

    public interface TableDataChangeListener {
        void onTableDataChanged();
    }

    private Map<String, Table> tables;

    private Map<String, Set<TableDesignChangeListener>> tableDesignChangeListeners =
            new HashMap<>();

    private Set<TableNameChangeListener> tableNameChangeListeners = new HashSet<>();

    private Map<String, Set<TableDataChangeListener>> tableDataChangeListeners = new HashMap<>();

    private int tableCounter = 1;

    private History history;

    /**
     * Constructs a new database with an empty table collection.
     */
    public Database() {
        tables = new HashMap<String, Table>();
        history = new History();
    }

    public void addTableDesignChangeListener(String tableName, TableDesignChangeListener listener) {
        tableDesignChangeListeners.computeIfAbsent(tableName, a -> new HashSet<>()).add(listener);
    }

    public void removeTableDesignChangeListener(String tableName,
            TableDesignChangeListener listener) {
        tableDesignChangeListeners.computeIfPresent(tableName,
                (a, v) -> v.remove(listener) && v.isEmpty() ? null : v);
    }

    private void notifyTableDesignChangeListeners(String tableName) {
        Set<TableDesignChangeListener> listeners = tableDesignChangeListeners.get(tableName);
        if (listeners != null) {
            listeners.forEach(TableDesignChangeListener::onTableChanged);
        }
    }

    public void addTableNameChangeListener(TableNameChangeListener listener) {
        tableNameChangeListeners.add(listener);
    }

    public void removeTableNameChangeListener(TableNameChangeListener listener) {
        tableNameChangeListeners.remove(listener);
    }

    private void notifyTableNameChangeListeners() {
        tableNameChangeListeners.forEach(TableNameChangeListener::onTableNameChanged);
    }

    public void addTableDataChangeListener(String tableName, TableDataChangeListener listener) {
        tableDataChangeListeners.computeIfAbsent(tableName, a -> new HashSet<>()).add(listener);
    }

    public void removeTableDataChangeListener(String tableName, TableDataChangeListener listener) {
        tableDataChangeListeners.computeIfPresent(tableName,
                (a, v) -> v.remove(listener) && v.isEmpty() ? null : v);
    }

    private void notifyTableDataChangeListeners(String tableName) {
        Set<TableDataChangeListener> listeners = tableDataChangeListeners.get(tableName);
        if (listeners != null) {
            listeners.forEach(TableDataChangeListener::onTableDataChanged);
        }
    }

    /** 
     * Undo the last operation in the database
     */
    public void undo() {
        history.undo();
    }

    /**
     * Redo the last undone operation in the database
     */
    public void redo() {
        history.redo();
    }

    /**
     * Checks if there are any operations that can be undone or redone.
     * 
     * @return true if there are operations that can be undone or redone, false otherwise.
     */
    public boolean canUndo() {
        return history.canUndo();
    }

    /**
     * Checks if there are any operations that can be redone.
     * 
     * @return true if there are operations that can be redone, false otherwise.
     */
    public boolean canRedo() {
        return history.canRedo();
    }

    /**
     * Creates a new table with a unique name and adds it to the database.
     */
    public void createTable() {
        String tempTableName = "Table" + tableCounter++;
        while (tables.containsKey(tempTableName)) {
            tempTableName = "Table" + tableCounter++;
        }
        final String tableName = tempTableName;
        tables.put(tableName, new Table());

        history.record(new Action(() -> {
            tables.remove(tableName);
            tableCounter--;
            notifyTableNameChangeListeners();
            notifyTableDesignChangeListeners(tableName);
            notifyTableDataChangeListeners(tableName);
        }, () -> {
            tables.put(tableName, new Table());
            tableCounter++;
            notifyTableNameChangeListeners();
        }));

        notifyTableNameChangeListeners();
    }

    /**
     * Retrieves the set of table names in the database.
     * 
     * @return a set of table names.
     */
    public ArrayList<String> getTables() {
        ArrayList<String> tableNames = new ArrayList<>(tables.keySet());
        tableNames.sort(String::compareTo);
        return tableNames;
    }

    /**
     * Deletes a table from the database.
     * 
     * @param tableName the name of the table to delete.
     */
    public void deleteTable(String tableName) {
        if (tables.remove(tableName) == null)
            throw new Error("Table does not exist");

        history.record(new Action(() -> {
            tables.put(tableName, new Table());
            notifyTableNameChangeListeners();
        }, () -> {
            tables.remove(tableName);
            notifyTableNameChangeListeners();
            notifyTableDesignChangeListeners(tableName);
            notifyTableDataChangeListeners(tableName);
        }));
        
        tableCounter--;
        notifyTableNameChangeListeners();
        notifyTableDesignChangeListeners(tableName);
        notifyTableDataChangeListeners(tableName);
    }

    /**
     * Renames an existing table in the database.
     *
     * @param oldName the current name of the table.
     * @param newName the new name for the table.
     * @throws Error if the old table name does not exist or the new name already exists.
     */
    public void updateTableName(String oldName, String newName) {
        if (!tables.containsKey(oldName))
            throw new Error("Table does not exist");
        if (oldName.equals(newName))
            return;
        if (tables.containsKey(newName))
            throw new Error("Table already exists");
        if (newName.isEmpty())
            throw new Error("Table name cannot be empty");

        tables.put(newName, tables.get(oldName));
        tables.remove(oldName);

        history.record(new Action(() -> {
            tables.put(oldName, tables.get(newName));
            tables.remove(newName);
            notifyTableNameChangeListeners();
        }, () -> {
            tables.put(newName, tables.get(oldName));
            tables.remove(oldName);
            notifyTableNameChangeListeners();
        }));

        notifyTableNameChangeListeners();
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
     * @param tableName the name of the table to check.
     * @param columnName the name of the column to check.
     * @return true if the column name is valid, false otherwise.
     */
    public boolean isValidColumnName(String tableName, String columnName) {
        if (!tables.containsKey(tableName)) 
            throw new Error("Table does not exist");
        return !tables.get(tableName).getColumns().contains(columnName);
    }

    /**
     * Checks if a column allows blank values.
     * 
     * @param tableName the name of the table to check.
     * @param columnName the name of the column to check.
     * @return true if the column allows blank values, false otherwise.
     */
    public boolean columnAllowBlank(String tableName, String columnName) {
        if (!tables.containsKey(tableName)) 
            throw new Error("Table does not exist");
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
            
        history.record(new Action(() -> {
            tables.get(tableName).deleteColumn(tables.get(tableName).getColumns().get(
                tables.get(tableName).getColumns().size() - 1));
                notifyTableDesignChangeListeners(tableName);
                notifyTableDataChangeListeners(tableName);
            }, () -> {
                tables.get(tableName).createColumn();
                notifyTableDesignChangeListeners(tableName);
                notifyTableDataChangeListeners(tableName);
        }));
                
        tables.get(tableName).createColumn();
        notifyTableDesignChangeListeners(tableName);
        notifyTableDataChangeListeners(tableName);
    }

    /**
     * Adds a new row to a table.
     * 
     * @param tableName the name of the table to add the row to.
     * @throws Error if the table does not exist.
     */
    public void addRow(String tableName) {
        if (!tables.containsKey(tableName))
            throw new Error("Table does not exist");
        tables.get(tableName).createRow();

        history.record(new Action(() -> {
            tables.get(tableName).deleteRow(tables.get(tableName).getRows().size() - 1);
            notifyTableDataChangeListeners(tableName);
        }, () -> {
            tables.get(tableName).createRow();
            notifyTableDataChangeListeners(tableName);
        }));

        notifyTableDataChangeListeners(tableName);
    }

    /**
     * Deletes a row from a table.
     * 
     * @param tableName the name of the table to delete the row from.
     * @param index the index of the row to delete.
     * @throws Error if the table does not exist.
     */
    public void deleteRow(String tableName, int index) {
        if (!tables.containsKey(tableName))
            throw new Error("Table does not exist");
        if (index < 0 || index >= tables.get(tableName).getRows().size())
            throw new Error("Row index out of bounds");
        
        final ArrayList<String> deletedRow = tables.get(tableName).getRow(index);
        tables.get(tableName).deleteRow(index);

        history.record(new Action(() -> {
            tables.get(tableName).createRow(deletedRow);
            notifyTableDataChangeListeners(tableName);
        }, () -> {
            tables.get(tableName).deleteRow(index);
            notifyTableDataChangeListeners(tableName);
        }));            
            
        notifyTableDataChangeListeners(tableName);
    }

    public void deleteRows(String tableName, ArrayList<Integer> indices) {
        if (!tables.containsKey(tableName))
            throw new Error("Table does not exist");
        
        final ArrayList<ArrayList<String>> deletedRows = new ArrayList<>();
        for (int i = 0; i < indices.size(); i++) {
            if (indices.get(i) < 0 || indices.get(i) >= tables.get(tableName).getRows().size())
                throw new Error("Row index out of bounds");
            deletedRows.add(tables.get(tableName).getRow(indices.get(i)));
        }

        tables.get(tableName).deleteRows(indices);

        history.record(new Action(() -> {
            for (int i = 0; i < deletedRows.size(); i++) {
                tables.get(tableName).createRow(deletedRows.get(i));
            }
            notifyTableDataChangeListeners(tableName);
        }, () -> {
            tables.get(tableName).deleteRows(indices);
            notifyTableDataChangeListeners(tableName);
        }));
            
        notifyTableDataChangeListeners(tableName);
    }

    /**
     * Deletes a column from a table.
     * 
     * @param tableName the name of the table to delete the column from.
     * @param columnName the name of the column to delete.
     * @throws Error if the table does not exist.
     */
    public void deleteColumn(String tableName, String columnName) {
        if (!tables.containsKey(tableName))
            throw new Error("Table does not exist");
        if (!tables.get(tableName).getColumns().contains(columnName))
            throw new Error("Column does not exist");

        final ArrayList<String> deletedColumnValues = tables.get(tableName).getColumn(columnName);
        final ColumnType deletedColumnType = tables.get(tableName).getColumnType(columnName);
        final String deletedColumnDefaultValue = tables.get(tableName).getDefaultColumnValue(columnName);
        final boolean deletedColumnAllowBlank = tables.get(tableName).columnAllowBlank(columnName);
        
        tables.get(tableName).deleteColumn(columnName);
        
        history.record(new Action(() -> {
            tables.get(tableName).createColumn(columnName, deletedColumnType, deletedColumnAllowBlank, deletedColumnDefaultValue, deletedColumnValues);
            notifyTableDesignChangeListeners(tableName);
            notifyTableDataChangeListeners(tableName);
        }, () -> {
            tables.get(tableName).deleteColumn(columnName);
            notifyTableDesignChangeListeners(tableName);
            notifyTableDataChangeListeners(tableName);
        }));

        notifyTableDesignChangeListeners(tableName);
        notifyTableDataChangeListeners(tableName);
    }

    /**
     * Edits a cell in a table.
     * 
     * @param tableName the name of the table containing the cell.
     * @param columnName the name of the column containing the cell.
     * @param rowIndex the index of the row containing the cell.
     * @param value the new value for the cell.
     */
    public void updateCell(String tableName, String columnName, int rowIndex, String value) {
        if (!tables.containsKey(tableName))
            throw new Error("Table does not exist");
        if (!tables.get(tableName).getColumns().contains(columnName))
            throw new Error("Column does not exist");
        if (rowIndex < 0 || rowIndex >= tables.get(tableName).getRows().size())
            throw new Error("Row index out of bounds");

        final String oldValue = tables.get(tableName).getCell(columnName, rowIndex).getValue();
        
        tables.get(tableName).updateCell(columnName, rowIndex, value);

        history.record(new Action(() -> {
            tables.get(tableName).updateCell(columnName, rowIndex, oldValue);
            notifyTableDataChangeListeners(tableName);
        }, () -> {
            tables.get(tableName).updateCell(columnName, rowIndex, value);
            notifyTableDataChangeListeners(tableName);
        }));

        notifyTableDataChangeListeners(tableName);
    }

    /**
     * Retrieves the value of a cell in a table.
     * 
     * @param tableName the name of the table containing the cell.
     * @param columnName the name of the column containing the cell.
     * @param rowIndex the index of the row containing the cell.
     * @return the value of the cell.
     */
    public String getCell(String tableName, String columnName, int rowIndex) {
        if (!tables.containsKey(tableName))
            throw new Error("Table does not exist");
        if (!tables.get(tableName).getColumns().contains(columnName))
            throw new Error("Column does not exist");
        if (rowIndex < 0 || rowIndex >= tables.get(tableName).getRows().size())
            throw new Error("Row index out of bounds");
        if (tables.get(tableName).getCell(columnName, rowIndex) == null)
            throw new Error("Cell does not exist");
        return tables.get(tableName).getCell(columnName, rowIndex).getValue();
    }

    /**
     * Retrieves the column names of a table.
     * 
     * @param tableName the name of the table to retrieve the column names from.
     * @return an ArrayList of column names.
     */
    public ArrayList<String> getColumnNames(String tableName) {
        if (!tables.containsKey(tableName))
            throw new Error("Table does not exist");
        return tables.get(tableName).getColumns();
    }

    /**
     * Retrieves the rows of a table.
     * 
     * @param tableName the name of the table to retrieve the rows from.
     * @return an ArrayList of rows.
     */
    public ArrayList<ArrayList<String>> getRows(String tableName) {
        if (!tables.containsKey(tableName))
            throw new Error("Table does not exist");
        return tables.get(tableName).getRows();
    }

    /**
     * Retrieves a row from a table.
     * 
     * @param tableName the name of the table to retrieve the row from.
     * @param index the index of the row to retrieve.
     * @return an ArrayList of row values.
     */
    public ArrayList<String> getRow(String tableName, int index) {
        if (!tables.containsKey(tableName))
            throw new Error("Table does not exist");
        if (index < 0 || index >= tables.get(tableName).getRows().size())
            throw new Error("Row index out of bounds");
        return tables.get(tableName).getRow(index);
    }

    /**
     * Retrieves a list of all the values of a column.
     * 
     * @param tableName the name of the table to retrieve the column from.
     * @param columnName the column to retrieve the values from.
     * @return list of column values
     */
    public ArrayList<String> getColumn(String tableName, String columnName) {
        if (!tables.containsKey(tableName))
            throw new Error("Table does not exist");
        if (!tables.get(tableName).getColumns().contains(columnName))
            throw new Error("Column does not exist");
        return tables.get(tableName).getColumn(columnName);
    }

    /**
     * Retrieves the type of a column in a table.
     * 
     * @param tableName the name of the table containing the column.
     * @param columnName the name of the column to retrieve the type of.
     * @return the type of the column.
     */
    public void updateColumnType(String tableName, String columnName, ColumnType type) {
        if (!tables.containsKey(tableName))
            throw new Error("Table does not exist");
        if (!tables.get(tableName).getColumns().contains(columnName))
            throw new Error("Column does not exist");
        if (type == null)
            throw new Error("Column type cannot be null");

        final ColumnType oldType = tables.get(tableName).getColumnType(columnName);
        
        tables.get(tableName).updateColumnType(columnName, type);

        history.record(new Action(() -> {
            tables.get(tableName).updateColumnType(columnName, oldType);
            notifyTableDesignChangeListeners(tableName);
        }, () -> {
            tables.get(tableName).updateColumnType(columnName, type);
            notifyTableDesignChangeListeners(tableName);
        }));

        notifyTableDesignChangeListeners(tableName);
    }

    /**
     * Retrieves the default value of a column in a table.
     * 
     * @param tableName the name of the table containing the column.
     * @param columnName the name of the column to retrieve the default value of.
     * @return the default value of the column.
     */
    public void updateColumnName(String tableName, String oldName, String newName) {
        if (!tables.containsKey(tableName))
            throw new Error("Table does not exist");
        if (!tables.get(tableName).getColumns().contains(oldName))
            throw new Error("Column does not exist");
        if (tables.get(tableName).getColumns().contains(newName) && !oldName.equals(newName))
            throw new Error("Column already exists");
            
        tables.get(tableName).updateColumnName(oldName, newName);
        
        history.record(new Action(() -> {
            tables.get(tableName).updateColumnName(newName, oldName);
            notifyTableDesignChangeListeners(tableName);
        }, () -> {
            tables.get(tableName).updateColumnName(oldName, newName);
            notifyTableDesignChangeListeners(tableName);
        }));

        notifyTableDesignChangeListeners(tableName);
        notifyTableDataChangeListeners(tableName);
    }

    /**
     * Retrieves the default value of a column in a table.
     * 
     * @param tableName the name of the table containing the column.
     * @param columnName the name of the column to retrieve the default value of.
     * @return the default value of the column.
     */
    public ColumnType getColumnType(String tableName, String columnName) {
        if (!tables.containsKey(tableName))
            throw new Error("Table does not exist");
        if (!tables.get(tableName).getColumns().contains(columnName))
            throw new Error("Column does not exist");
        if (tables.get(tableName).getColumn(columnName) == null)
            throw new Error("Column does not exist");
        return tables.get(tableName).getColumnType(columnName);
    }

    /**
     * Retrieves the default value of a column in a table.
     * 
     * @param tableName the name of the table containing the column.
     * @param columnName the name of the column to retrieve the default value of.
     * @return the default value of the column.
     */
    public String getDefaultColumnValue(String tableName, String columnName) {
        if (!tables.containsKey(tableName))
            throw new Error("Table does not exist");
        if (!tables.get(tableName).getColumns().contains(columnName))
            throw new Error("Column does not exist");
        if (tables.get(tableName).getColumn(columnName) == null)
            throw new Error("Column does not exist");
        if (tables.get(tableName).getDefaultColumnValue(columnName) == null)
            throw new Error("Column does not exist");
        return tables.get(tableName).getDefaultColumnValue(columnName);
    }

    /**
     * Retrieves the default value of a column in a table.
     * 
     * @param tableName the name of the table containing the column.
     * @param columnName the name of the column to retrieve the default value of.
     * @return the default value of the column.
     */
    public void updateDefaultColumnValue(String tableName, String columnName, String value) {
        if (!tables.containsKey(tableName))
            throw new Error("Table does not exist");
        if (!tables.get(tableName).getColumns().contains(columnName))
            throw new Error("Column does not exist");
        if (value == null)
            throw new Error("Default value cannot be null");
        if (tables.get(tableName).getColumn(columnName) == null)
            throw new Error("Column does not exist");

        final String oldValue = tables.get(tableName).getDefaultColumnValue(columnName);
        
        tables.get(tableName).updateDefaultColumnValue(columnName, value);
        
        history.record(new Action(() -> {
            tables.get(tableName).updateDefaultColumnValue(columnName, oldValue);
            notifyTableDesignChangeListeners(tableName);
        }, () -> {
            tables.get(tableName).updateDefaultColumnValue(columnName, value);
            notifyTableDesignChangeListeners(tableName);
        }));

        notifyTableDesignChangeListeners(tableName);
    }

    /**
     * Retrieves the default value of a column in a table.
     * 
     * @param tableName the name of the table containing the column.
     * @param columnName the name of the column to retrieve the default value of.
     * @return the default value of the column.
     */
    public void toggleColumnType(String tableName, String columnName) {
        if (!tables.containsKey(tableName))
            throw new Error("Table does not exist");
        if (!tables.get(tableName).getColumns().contains(columnName))
            throw new Error("Column does not exist");
        if (tables.get(tableName).getColumn(columnName) == null)
            throw new Error("Column does not exist");
        
        final String oldDefaultValue = tables.get(tableName).getDefaultColumnValue(columnName);
        tables.get(tableName).toggleColumnType(columnName);

        history.record(new Action(() -> {
            tables.get(tableName).unToggleColumnType(columnName);
            tables.get(tableName).updateDefaultColumnValue(columnName, oldDefaultValue);
            notifyTableDesignChangeListeners(tableName);
        }, () -> {
            tables.get(tableName).toggleColumnType(columnName);
            notifyTableDesignChangeListeners(tableName);
        }));

        notifyTableDesignChangeListeners(tableName);
    }

    /**
     * Sets the allow blank state of a column.
     * 
     * @param tableName the name of the table containing the column.
     * @param columnName the name of the column to retrieve the default value of.
     * @param allowBlank the value to set the allow blank state to.
     */
    public void setColumnAllowBlank(String tableName, String columnName, boolean allowBlank) {
        if (!tables.containsKey(tableName))
            throw new Error("Table does not exist");
        if (!tables.get(tableName).getColumns().contains(columnName))
            throw new Error("Column does not exist");
        if (tables.get(tableName).getColumn(columnName) == null)
            throw new Error("Column does not exist");

        final boolean oldAllowBlank = tables.get(tableName).columnAllowBlank(columnName);
        
        tables.get(tableName).setColumnAllowBlank(columnName, allowBlank);
        
        history.record(new Action(() -> {
            tables.get(tableName).setColumnAllowBlank(columnName, oldAllowBlank);
            notifyTableDesignChangeListeners(tableName);
        }, () -> {
            tables.get(tableName).setColumnAllowBlank(columnName, allowBlank);
            notifyTableDesignChangeListeners(tableName);
        }));

        notifyTableDesignChangeListeners(tableName);
    }

    public boolean isValidValue(String tableName, String columnName, String value) {
        if (!tables.containsKey(tableName))
            throw new Error("Table does not exist");
        if (!tables.get(tableName).getColumns().contains(columnName))
            throw new Error("Column does not exist");
        if (tables.get(tableName).getColumn(columnName) == null)
            throw new Error("Column does not exist");
        if (value == null)
            throw new Error("Value cannot be null");
        return tables.get(tableName).isValidValue(columnName, value);
    }

    public boolean isValidAllowBlankValue(String tableName, String columnName, boolean value) {
        if (!tables.containsKey(tableName))
            throw new Error("Table does not exist");
        if (!tables.get(tableName).getColumns().contains(columnName))
            throw new Error("Column does not exist");
        if (tables.get(tableName).getColumn(columnName) == null)
            throw new Error("Column does not exist");
        return tables.get(tableName).isValidAllowBlankValue(columnName, value);
    }

    public boolean isValidColumnType(String tableName, String columnName, ColumnType type) {
        if (!tables.containsKey(tableName))
            throw new Error("Table does not exist");
        if (!tables.get(tableName).getColumns().contains(columnName))
            throw new Error("Column does not exist");
        if (tables.get(tableName).getColumn(columnName) == null)
            throw new Error("Column does not exist");
        if (type == null)
            throw new Error("Column type cannot be null");
        return tables.get(tableName).isValidColumnType(columnName, type);
    }
}
