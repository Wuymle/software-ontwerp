package newdatabase;

import java.util.HashSet;
import java.util.Set;

public class Database {
    private Set<Table> tables = new HashSet<>();
    private int tableCount = 0;

    public void createTable() {
        String tableName = "Table" + tableCount++;
        while (tableExists(tableName)) {
            tableName = "Table" + tableCount++;
        }
        tables.add(new Table(tableName));
    }

    private boolean tableExists(String tableName) {
        for (Table table : tables) {
            if (table.getName().equals(tableName))
                return true;
        }
        return false;
    }

    public Set<Table> getTables() {
        return tables;
    }

    public boolean allowUpdateTableName(Table table, String newName) {
        if (table == null || !tables.contains(table)) 
            throw new IllegalArgumentException("Table does not exist");
        return newName != null && !newName.isEmpty() && !tableExists(newName);
    }

    public void updateTableName(Table table, String newName) {
        if (table == null || !tables.contains(table))
            throw new IllegalArgumentException("Table does not exist");
        if (!allowUpdateTableName(table, newName))
            throw new IllegalArgumentException("Table name already exists");
        table.updateName(newName);
    }

    public void deleteTable(Table table) {
        if (table == null || !tables.contains(table))
            throw new IllegalArgumentException("Table does not exist");
        tables.remove(table);
    }
}
