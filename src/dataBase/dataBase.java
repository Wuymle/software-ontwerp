package dataBase;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

public class dataBase {
    private Map<String, Table> tables = new HashMap<>();

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

    public void addColumn(String tableName, columnType type, boolean allowBlank) {
        if (!tables.containsKey(tableName))
            throw new Error("Table does not exist");
        tables.get(tableName).createColumn(type, allowBlank);
    }
}
