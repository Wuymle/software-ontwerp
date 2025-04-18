package database.test;

import database.Database;
import database.Table;
import database.Column;
import database.ColumnType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import database.ColumnType;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.Set;

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
        Set<String> tableNames = database.getTables();
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
}
