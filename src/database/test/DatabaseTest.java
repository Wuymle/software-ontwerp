package database.test;

import database.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {

    private Database database;

    @BeforeEach
    void setUp() {
        database = new Database();
    }

    @Test
    void testDatabaseCreation() {
        assertNotNull(database.getTables());
        assertTrue(database.getTables().isEmpty());
        assertNotNull(database.getHistory());
    }

    @Test
    void testCreateTable() {
        AtomicBoolean listenerCalled = new AtomicBoolean(false);
        database.addTablesChangeListener(() -> listenerCalled.set(true));

        database.createTable();
        assertEquals(1, database.getTables().size());
        Table table = database.getTables().iterator().next();
        assertTrue(table.getName().startsWith("Table"));
        assertTrue(listenerCalled.get());
    }

    @Test
    void testCreateTableMultiple() {
        database.createTable();
        database.createTable();
        assertEquals(2, database.getTables().size());
        // Check for unique names (basic check)
        String name1 = database.getTables().iterator().next().getName();
        String name2 = database.getTables().stream().filter(t -> !t.getName().equals(name1)).findFirst().get().getName();
        assertNotEquals(name1, name2);
    }

    @Test
    void testDeleteTable() {
        AtomicBoolean listenerCalled = new AtomicBoolean(false);
        database.addTablesChangeListener(() -> listenerCalled.set(true));

        database.createTable();
        Table table = database.getTables().iterator().next();
        listenerCalled.set(false); // Reset after creation

        database.deleteTable(table);
        assertTrue(database.getTables().isEmpty());
        assertTrue(listenerCalled.get());
    }

    @Test
    void testDeleteTableNull() {
        assertThrows(IllegalArgumentException.class, () -> database.deleteTable(null));
    }

    @Test
    void testUpdateTableName() {
        AtomicBoolean listenerCalled = new AtomicBoolean(false);
        database.addTablesChangeListener(() -> listenerCalled.set(true));
        database.createTable();
        Table table = database.getTables().iterator().next();
        listenerCalled.set(false);

        String oldName = table.getName();
        String newName = "NewTableName";
        database.updateTableName(table, newName);
        assertEquals(newName, table.getName());
        assertTrue(listenerCalled.get());

        // Test undo
        database.getHistory().undo();
        assertEquals(oldName, table.getName());
    }

    @Test
    void testUpdateTableNameToExisting() {
        database.createTable(); // Table0
        database.createTable(); // Table1
        Table table1 = database.getTables().stream().filter(t -> t.getName().equals("Table0")).findFirst().get();
        Table table2 = database.getTables().stream().filter(t -> t.getName().equals("Table1")).findFirst().get();

        assertThrows(IllegalArgumentException.class, () -> database.updateTableName(table2, "Table0"));
    }

    @Test
    void testUpdateTableNameNullTable() {
        assertThrows(IllegalArgumentException.class, () -> database.updateTableName(null, "NewName"));
    }

    @Test
    void testUpdateTableNameNullName() {
        database.createTable();
        Table table = database.getTables().iterator().next();
        assertThrows(IllegalArgumentException.class, () -> database.updateTableName(table, null));
    }

    @Test
    void testUpdateTableNameEmptyName() {
        database.createTable();
        Table table = database.getTables().iterator().next();
        assertThrows(IllegalArgumentException.class, () -> database.updateTableName(table, ""));
    }

    @Test
    void testAllowUpdateTableName() {
        database.createTable();
        Table table = database.getTables().iterator().next();
        assertTrue(database.allowUpdateTableName(table, "NewName"));
        assertTrue(database.allowUpdateTableName(table, table.getName())); // Same name allowed
    }

    @Test
    void testAllowUpdateTableNameExisting() {
        database.createTable(); // Table0
        database.createTable(); // Table1
        Table table1 = database.getTables().stream().filter(t -> t.getName().equals("Table0")).findFirst().get();
        Table table2 = database.getTables().stream().filter(t -> t.getName().equals("Table1")).findFirst().get();
        assertFalse(database.allowUpdateTableName(table2, "Table0"));
    }

    @Test
    void testAllowUpdateTableNameNullTable() {
        assertThrows(IllegalArgumentException.class, () -> database.allowUpdateTableName(null, "NewName"));
    }

    @Test
    void testAllowUpdateTableNameNullName() {
        database.createTable();
        Table table = database.getTables().iterator().next();
        assertFalse(database.allowUpdateTableName(table, null));
    }

    @Test
    void testAllowUpdateTableNameEmptyName() {
        database.createTable();
        Table table = database.getTables().iterator().next();
        assertFalse(database.allowUpdateTableName(table, ""));
    }

    @Test
    void testAddRemoveTablesChangeListener() {
        AtomicBoolean listenerCalled = new AtomicBoolean(false);
        Database.TablesChangeListener listener = () -> listenerCalled.set(true);

        database.addTablesChangeListener(listener);
        database.createTable();
        assertTrue(listenerCalled.get());

        listenerCalled.set(false);
        database.removeTablesChangeListener(listener);
        database.createTable(); // Create another one
        assertFalse(listenerCalled.get()); // Should not be called after removal
    }

    @Test
    void testAddNullTablesChangeListener() {
        assertThrows(IllegalArgumentException.class, () -> database.addTablesChangeListener(null));
    }

    @Test
    void testRemoveNullTablesChangeListener() {
        assertThrows(IllegalArgumentException.class, () -> database.removeTablesChangeListener(null));
    }

    @Test
    void testUndoRedoCreateTable() {
        database.createTable();
        assertEquals(1, database.getTables().size());
        String tableName = database.getTables().iterator().next().getName();

        database.getHistory().undo();
        assertTrue(database.getTables().isEmpty());

        database.getHistory().redo();
        assertEquals(1, database.getTables().size());
        assertEquals(tableName, database.getTables().iterator().next().getName());
    }

    @Test
    void testUndoRedoDeleteTable() {
        database.createTable();
        Table table = database.getTables().iterator().next();
        database.deleteTable(table);
        assertTrue(database.getTables().isEmpty());

        database.getHistory().undo();
        assertEquals(1, database.getTables().size());
        assertSame(table, database.getTables().iterator().next());

        database.getHistory().redo();
        assertTrue(database.getTables().isEmpty());
    }
}
