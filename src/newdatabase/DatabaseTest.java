package newdatabase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;



class DatabaseTest {

    private Database database;

    @BeforeEach
    void setUp() {
        database = new Database();
    }

    @Test
    void testFullDatabaseUsageScenario() {
        // Create three tables
        database.createTable();
        database.createTable();
        database.createTable();
        assertEquals(3, database.getTables().size());

        // Add columns and rows to each table
        for (Table table : database.getTables()) {
            table.createColumn();
            table.createColumn();
            // Get columns
            Column[] columns = table.getColumns().toArray(new Column[0]);
            // Rename columns
            table.updateColumnName(columns[0], "Name");
            table.updateColumnName(columns[1], "Age");

            table.createRow();
            table.createRow();
            // Get rows
            Row[] rows = table.getRows().toArray(new Row[0]);
            // Update cell values
            rows[0].updateCellValue(columns[0], "Alice");
            rows[0].updateCellValue(columns[1], "30");
            rows[1].updateCellValue(columns[0], "Bob");
            rows[1].updateCellValue(columns[1], "25");

            assertEquals(2, table.getRows().size());
            assertEquals(2, table.getColumns().size());
        }

        // Rename a table and check
        Table firstTable = database.getTables().iterator().next();
        String newTableName = "People";

        assertTrue(database.allowUpdateTableName(firstTable, newTableName));
        database.updateTableName(firstTable, newTableName);
        assertEquals(newTableName, firstTable.getName());

        // Delete a table and verify
        int beforeDelete = database.getTables().size();
        database.deleteTable(firstTable);
        assertEquals(beforeDelete - 1, database.getTables().size());

        // Try to update a deleted table (should throw)
        assertThrows(IllegalArgumentException.class,
                () -> database.updateTableName(firstTable, "AnotherName"));
    }

    @Test
    void testCreateCellAndGetCell() {
        Column column = new Column("Col1");
        Row row = new Row();
        row.createCell(column);
        assertNotNull(row.getCell(column));
        assertEquals("", row.getCell(column).getValue());
    }

    @Test
    void testCreateCellThrowsIfAlreadyExists() {
        Column column = new Column("Col1");
        Row row = new Row();
        row.createCell(column);
        assertThrows(IllegalArgumentException.class, () -> row.createCell(column));
    }

    @Test
    void testDeleteCellRemovesCell() {
        Column column = new Column("Col1");
        Row row = new Row();
        row.createCell(column);
        row.deleteCell(column);
        assertNull(row.getCell(column));
    }

    @Test
    void testDeleteCellThrowsIfColumnDoesNotExist() {
        Column column = new Column("Col1");
        Row row = new Row();
        assertThrows(IllegalArgumentException.class, () -> row.deleteCell(column));
    }

    @Test
    void testUpdateCellValueSuccess() {
        Column column = new Column("Col1");
        Row row = new Row();
        row.createCell(column);
        row.updateCellValue(column, "abc");
        assertEquals("abc", row.getCell(column).getValue());
    }

    @Test
    void testUpdateCellValueThrowsIfColumnNull() {
        Row row = new Row();
        assertThrows(IllegalArgumentException.class, () -> row.updateCellValue(null, "abc"));
    }

    @Test
    void testUpdateCellValueThrowsIfCellDoesNotExist() {
        Column column = new Column("Col1");
        Row row = new Row();
        assertThrows(IllegalArgumentException.class, () -> row.updateCellValue(column, "abc"));
    }

    @Test
    void testAllowUpdateCellValueThrowsIfColumnNull() {
        Row row = new Row();
        assertThrows(IllegalArgumentException.class, () -> row.allowUpdateCellValue(null, "abc"));
    }

    @Test
    void testAllowUpdateCellValueThrowsIfCellDoesNotExist() {
        Column column = new Column("Col1");
        Row row = new Row();
        assertThrows(IllegalArgumentException.class, () -> row.allowUpdateCellValue(column, "abc"));
    }

    @Test
    void testCellUpdateValueThrowsIfNull() {
        Cell cell = new Cell();
        assertThrows(IllegalArgumentException.class, () -> cell.updateValue(null));
    }

    @Test
    void testColumnUpdateNameThrowsIfNullOrEmpty() {
        Column column = new Column("Col1");
        assertThrows(IllegalArgumentException.class, () -> column.updateName(null));
        assertThrows(IllegalArgumentException.class, () -> column.updateName(""));
    }

    @Test
    void testColumnUpdateTypeThrowsIfNull() {
        Column column = new Column("Col1");
        assertThrows(IllegalArgumentException.class, () -> column.updateType(null));
    }

    @Test
    void testColumnUpdateDefaultValueThrowsIfNull() {
        Column column = new Column("Col1");
        assertThrows(IllegalArgumentException.class, () -> column.updateDefaultValue(null));
    }

    @Test
    void testColumnAllowCellValueThrowsIfNull() {
        Column column = new Column("Col1");
        assertThrows(IllegalArgumentException.class, () -> column.allowCellValue(null));
    }

    @Test
    void testCreateTableAddsTable() {
        database.createTable();
        Set<Table> tables = database.getTables();
        assertEquals(1, tables.size());
        Table table = tables.iterator().next();
        assertTrue(table.getName().startsWith("Table"));
    }

    @Test
    void testCreateTableUniqueNames() {
        database.createTable();
        database.createTable();
        database.createTable();
        Set<String> names = new java.util.HashSet<>();
        for (Table t : database.getTables()) {
            assertTrue(names.add(t.getName()), "Duplicate table name found");
        }
    }

    @Test
    void testDeleteTableRemovesTable() {
        database.createTable();
        Table table = database.getTables().iterator().next();
        database.deleteTable(table);
        assertTrue(database.getTables().isEmpty());
    }

    @Test
    void testDeleteTableThrowsIfNotExists() {
        Table fakeTable = new Table("Fake");
        assertThrows(IllegalArgumentException.class, () -> database.deleteTable(fakeTable));
    }

    @Test
    void testUpdateTableNameSuccess() {
        database.createTable();
        Table table = database.getTables().iterator().next();
        String newName = "NewTableName";
        assertTrue(database.allowUpdateTableName(table, newName));
        database.updateTableName(table, newName);
        assertEquals(newName, table.getName());
    }

    @Test
    void testUpdateTableNameFailsIfNameExists() {
        database.createTable();
        database.createTable();
        Table[] tables = database.getTables().toArray(new Table[0]);
        String existingName = tables[0].getName();
        assertFalse(database.allowUpdateTableName(tables[1], existingName));
        assertThrows(IllegalArgumentException.class,
                () -> database.updateTableName(tables[1], existingName));
    }

    @Test
    void testUpdateTableNameFailsIfNullOrEmpty() {
        database.createTable();
        Table table = database.getTables().iterator().next();
        assertFalse(database.allowUpdateTableName(table, null));
        assertFalse(database.allowUpdateTableName(table, ""));
        assertThrows(IllegalArgumentException.class, () -> database.updateTableName(table, null));
        assertThrows(IllegalArgumentException.class, () -> database.updateTableName(table, ""));
    }

    @Test
    void testAllowUpdateTableNameThrowsIfTableNotExists() {
        Table fakeTable = new Table("Fake");
        assertThrows(IllegalArgumentException.class,
                () -> database.allowUpdateTableName(fakeTable, "SomeName"));
    }

    @Test
    void testUpdateTableNameThrowsIfTableNotExists() {
        Table fakeTable = new Table("Fake");
        assertThrows(IllegalArgumentException.class,
                () -> database.updateTableName(fakeTable, "SomeName"));
    }

    @Test
    void testDeleteTableThrowsIfNull() {
        assertThrows(IllegalArgumentException.class, () -> database.deleteTable(null));
    }

    @Test
    void testUpdateTableNameThrowsIfNull() {
        assertThrows(IllegalArgumentException.class,
                () -> database.updateTableName(null, "SomeName"));
    }

    @Test
    void testAllowUpdateTableNameThrowsIfNull() {
        assertThrows(IllegalArgumentException.class,
                () -> database.allowUpdateTableName(null, "SomeName"));
    }
}
