package database.test;

import database.Database;
import database.ColumnType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {
    private Database database;

    @BeforeEach
    public void setUp() {
        database = new Database();
        database.createTable();
        database.addColumn("Table1");
        database.addRow("Table1");
    }

    @Test
    public void testChangeColumnType() {
        database.updateCell("Table1", "Column1", 0, "Gyqttt");
        assertFalse(database.isValidColumnType("Table1", "Column1", ColumnType.INTEGER));
        assertThrows(Error.class,
                () -> database.updateColumnType("Table1", "Column1", ColumnType.INTEGER));
    }

    @Test
    public void testChangeColumnType2() {
        database.updateCell("Table1", "Column1", 0, "123");
        assertDoesNotThrow(
                () -> database.updateColumnType("Table1", "Column1", ColumnType.INTEGER));
    }

    @Test
    public void testRenameTableAndAccess() {
        // Add a unique value to identify this table
        database.updateCell("Table1", "Column1", 0, "UniqueTestValue");

        // Rename the table
        database.updateTableName("Table1", "RenamedTable");

        // Check that the old name is no longer in the database
        ArrayList<String> tableNames = database.getTables();
        assertFalse(tableNames.contains("Table1"), "Old table name should no longer exist");
        assertTrue(tableNames.contains("RenamedTable"), "New table name should exist");

        // Try to access the renamed table and verify its content
        ArrayList<String> columnNames = database.getColumnNames("RenamedTable");
        assertNotNull(columnNames, "Should be able to access column names from renamed table");
        assertTrue(columnNames.contains("Column1"), "Column1 should exist in renamed table");

        // Check we can access the cell data from the renamed table
        String cellValue = database.getCell("RenamedTable", "Column1", 0);
        assertEquals("UniqueTestValue", cellValue, "Cell value should be preserved after renaming");
    }

    @Test
    public void testCreateAndDeleteTable() {
        database.createTable(); // Table2
        assertTrue(database.getTables().contains("Table2"));
        database.deleteTable("Table2");
        assertFalse(database.getTables().contains("Table2"));
    }

    @Test
    public void testUpdateTableName() {
        database.updateTableName("Table1", "RenamedTable");
        assertTrue(database.getTables().contains("RenamedTable"));
    }

    @Test
    public void testUpdateTableNameFails() {
        assertThrows(Error.class, () -> database.updateTableName("NonExisting", "NewTable"));
        database.createTable(); // Table2
        assertThrows(Error.class, () -> database.updateTableName("Table1", "Table2")); // duplicate
    }

    @Test
    public void testIsValidTableName() {
        assertFalse(database.isValidTableName("Table1"));
        assertTrue(database.isValidTableName("NewTable"));
    }

    @Test
    public void testIsValidColumnName() {
        assertTrue(database.isValidColumnName("Table1", "NonExistingColumn"));
    }

    @Test
    public void testColumnAllowBlank() {
        assertTrue(database.columnAllowBlank("Table1", "Column1"));
    }

    @Test
    public void testAddRowAndDeleteRow() {
        database.addRow("Table1");
        database.deleteRow("Table1", 1);
        assertEquals(1, database.getRows("Table1").size());
    }



    @Test
    public void testAddColumnAndDeleteColumn() {
        database.addColumn("Table1");
        database.deleteColumn("Table1", "Column2");
        assertEquals(1, database.getColumnNames("Table1").size());
    }

    @Test
    public void testUpdateCellAndGetCell() {
        database.updateCell("Table1", "Column1", 0, "abc");
        assertEquals("abc", database.getCell("Table1", "Column1", 0));
    }

    @Test
    public void testGetColumnNames() {
        assertEquals(1, database.getColumnNames("Table1").size());
    }

    @Test
    public void testGetRowsAndRowAndColumn() {
        assertNotNull(database.getRows("Table1"));
        assertNotNull(database.getRow("Table1", 0));
        assertNotNull(database.getColumn("Table1", "Column1"));
    }

    @Test
    public void testGetColumnTypeAndDefaultValue() {
        assertEquals(ColumnType.STRING, database.getColumnType("Table1", "Column1"));
        assertEquals("", database.getDefaultColumnValue("Table1", "Column1"));
    }

    @Test
    public void testUpdateColumnNameAndDefaultValue() {
        database.updateColumnName("Table1", "Column1", "NewCol");
        database.updateDefaultColumnValue("Table1", "NewCol", "default");
        assertEquals("default", database.getDefaultColumnValue("Table1", "NewCol"));
    }

    @Test
    public void testToggleColumnType() {
        database.toggleColumnType("Table1", "Column1");
        assertEquals(ColumnType.INTEGER, database.getColumnType("Table1", "Column1"));
    }


    @Test
    public void testIsValidValueAndAllowBlankValue() {
        assertTrue(database.isValidValue("Table1", "Column1", "abc"));
        assertTrue(database.isValidAllowBlankValue("Table1", "Column1", true));
    }

    @Test
    public void testUndoRedoCreateTable() {
        int initialTableCount = database.getTables().size();
        database.createTable(); // Table2
        assertEquals(initialTableCount + 1, database.getTables().size());
        assertTrue(database.canUndo());
        
        database.undo();
        assertEquals(initialTableCount, database.getTables().size());
        assertTrue(database.canRedo());
        
        database.redo();
        assertEquals(initialTableCount + 1, database.getTables().size());
    }

    @Test
    public void testUndoRedoDeleteTable() {
        String tableName = database.getTables().get(0); // Get Table1
        int initialTableCount = database.getTables().size();
        database.deleteTable(tableName);
        assertEquals(initialTableCount - 1, database.getTables().size());
        
        database.undo();
        assertEquals(initialTableCount, database.getTables().size());
        assertTrue(database.getTables().contains(tableName));
        
        database.redo();
        assertEquals(initialTableCount - 1, database.getTables().size());
        assertFalse(database.getTables().contains(tableName));
    }

    @Test
    public void testUndoRedoAddColumn() {
        int initialColumnCount = database.getColumnNames("Table1").size();
        database.addColumn("Table1");
        assertEquals(initialColumnCount + 1, database.getColumnNames("Table1").size());
        
        database.undo();
        assertEquals(initialColumnCount, database.getColumnNames("Table1").size());
        
        database.redo();
        assertEquals(initialColumnCount + 1, database.getColumnNames("Table1").size());
    }

    @Test
    public void testUndoRedoUpdateCell() {
        String initialValue = database.getCell("Table1", "Column1", 0);
        String newValue = "test_value";
        database.updateCell("Table1", "Column1", 0, newValue);
        assertEquals(newValue, database.getCell("Table1", "Column1", 0));
        
        database.undo();
        assertEquals(initialValue, database.getCell("Table1", "Column1", 0));
        
        database.redo();
        assertEquals(newValue, database.getCell("Table1", "Column1", 0));
    }

    @Test
    public void testUndoRedoUpdateTableName() {
        String oldName = "Table1";
        String newName = "RenamedTable";
        database.updateTableName(oldName, newName);
        assertTrue(database.getTables().contains(newName));
        assertFalse(database.getTables().contains(oldName));
        
        database.undo();
        assertTrue(database.getTables().contains(oldName));
        assertFalse(database.getTables().contains(newName));
        
        database.redo();
        assertTrue(database.getTables().contains(newName));
        assertFalse(database.getTables().contains(oldName));
    }

    @Test
    public void testUndoRedoUpdateColumnType() {
        database.updateCell("Table1", "Column1", 0, "123");
        assertEquals(ColumnType.STRING, database.getColumnType("Table1", "Column1"));
        
        database.updateColumnType("Table1", "Column1", ColumnType.INTEGER);
        assertEquals(ColumnType.INTEGER, database.getColumnType("Table1", "Column1"));
        
        database.undo();
        assertEquals(ColumnType.STRING, database.getColumnType("Table1", "Column1"));
        
        database.redo();
        assertEquals(ColumnType.INTEGER, database.getColumnType("Table1", "Column1"));
    }

    @Test
    public void testCanUndoRedoStatus() {
        Database db = new Database();

        assertFalse(db.canUndo());
        assertFalse(db.canRedo());
        
        db.createTable();
        assertTrue(db.canUndo());
        assertFalse(db.canRedo());
        
        db.undo();
        assertFalse(db.canUndo());
        assertTrue(db.canRedo());
        
        db.redo();
        assertTrue(db.canUndo());
        assertFalse(db.canRedo());
    }

    @Test
    public void testUndoRedoMultipleOperations() {
        // Perform multiple operations
        database.addColumn("Table1"); // Column2
        database.addRow("Table1"); // Row 1
        database.updateCell("Table1", "Column2", 1, "test");
        System.out.println(database.getColumnNames("Table1"));
        // Undo all operations
        database.undo(); // Undo update cell
        assertEquals("", database.getCell("Table1", "Column2", 1));

        database.undo(); // Undo add row
        database.undo(); // Undo add column
        
                System.out.println(database.getColumnNames("Table1"));

        // Verify original state
        assertEquals(1, database.getColumnNames("Table1").size());
        assertEquals(1, database.getRows("Table1").size());
        
        // Redo all operations
        database.redo(); // Redo add column
                System.out.println(database.getColumnNames("Table1"));

        database.redo(); // Redo add row
        database.redo(); // Redo update cell
        
        // Verify final state
        assertEquals(2, database.getColumnNames("Table1").size());
        assertEquals(2, database.getRows("Table1").size());
        assertEquals("test", database.getCell("Table1", "Column2", 1));
    }
}
