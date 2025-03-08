package dataBase;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

public class dataBase {
    private Map<String, Table> tables = new HashMap<>();
    
    public void createTable(String name) {
        if (tables.containsKey(name)) throw new Error("Table already exists");

        Table table = new Table();
        tables.put(name, table);
    }

    public Set<String> getTables() {
        return tables.keySet();
    }

    // Methods for the current table

    public void deleteTable(String tableName) {
        tables.remove(tableName);
    }

    public void editTableName(String oldName, String newName) {
        if (tables.containsKey(newName)) throw new Error("Table already exists");

        Table table = tables.get("oldName");
        tables.put(newName, table);
        tables.remove(oldName);
    }

    public void addColumn(String tableName, columnType type, boolean allowBlank) {
        tables.get(tableName).createColumn(type, allowBlank);
    }
}
