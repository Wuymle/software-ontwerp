package newdatabase;

import java.util.HashSet;
import java.util.Set;

public class Database extends DatabaseObject {
    public interface TablesChangeListener {
        void onTablesChanged();
    }

    private Set<TablesChangeListener> tablesChangeListeners = new HashSet<>();

    private Set<Table> tables = new HashSet<>();

    private int tableCount = 0;

    public Database() {
        super(new History());
    }

    public void addTablesChangeListener(TablesChangeListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("listener cannot be null");
        tablesChangeListeners.add(listener);
    }

    public void removeTablesChangeListener(TablesChangeListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("listener cannot be null");
        tablesChangeListeners.remove(listener);
    }

    public Set<Table> getTables() {
        return tables;
    }

    public History getHistory() {
        return history;
    }

    public void createTable() {
        String tableName = "Table" + tableCount++;
        while (tableExists(tableName)) {
            tableName = "Table" + tableCount++;
        }
        final Table table = new Table(history, tableName);
        history.record(new Action(() -> tables.remove(table), () -> tables.add(table)).setCallback(
                () -> tablesChangeListeners.forEach(TablesChangeListener::onTablesChanged)));
    }

    public boolean allowUpdateTableName(Table table, String newName) {
        if (table == null || !tables.contains(table))
            throw new IllegalArgumentException("Table does not exist");
        return table.getName().equals(newName)
                || (newName != null && !newName.isEmpty() && !tableExists(newName));
    }

    public void updateTableName(Table table, String newName) {
        if (table == null || !tables.contains(table))
            throw new IllegalArgumentException("Table does not exist");
        if (!allowUpdateTableName(table, newName))
            throw new IllegalArgumentException("Table name already exists");
        history.record(table.updateName(newName).setCallback(
                () -> tablesChangeListeners.forEach(TablesChangeListener::onTablesChanged)));
    }

    public void deleteTable(Table table) {
        if (table == null || !tables.contains(table))
            throw new IllegalArgumentException("Table does not exist");
        history.record(new Action(() -> tables.add(table), () -> tables.remove(table))
                .setCallback(
                        () -> tablesChangeListeners.forEach(TablesChangeListener::onTablesChanged))
                .setCallback(() -> {
                    table.notifyTableDesignChanged();
                    table.notifyTableRowsChanged();
                }));
    }

    private boolean tableExists(String tableName) {
        for (Table table : tables) {
            if (table.getName().equals(tableName))
                return true;
        }
        return false;
    }
}
