package newdatabase;

import java.util.HashSet;
import java.util.Set;

public class Database extends DatabaseObject {
    Database() {
        super(new History());
    }

    private Set<Table> tables = new HashSet<>();
    private int tableCount = 0;

    public Set<Table> getTables() {
        return tables;
    }

    public void undo() {
        history.undo();
    }

    public void redo() {
        history.redo();
    }

    public boolean canUndo() {
        return history.canUndo();
    }

    public boolean canRedo() {
        return history.canRedo();
    }

    public void createTable() {
        String tableName = "Table" + tableCount++;
        while (tableExists(tableName)) {
            tableName = "Table" + tableCount++;
        }
        final Table table = new Table(history, tableName);
        history.record(new Action(() -> tables.remove(table), () -> tables.add(table)));
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
        history.record(table.updateName(newName));
    }

    public void deleteTable(Table table) {
        if (table == null || !tables.contains(table))
            throw new IllegalArgumentException("Table does not exist");
        history.record(new Action(() -> tables.add(table), () -> tables.remove(table)));
    }

    private boolean tableExists(String tableName) {
        for (Table table : tables) {
            if (table.getName().equals(tableName))
                return true;
        }
        return false;
    }
}
