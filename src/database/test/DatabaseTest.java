package database.test;

import database.Database;
import database.Table;
import database.Column;
import database.ColumnType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import database.ColumnType;

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
}
