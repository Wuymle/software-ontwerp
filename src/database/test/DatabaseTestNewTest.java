package database.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import database.Database;

public class DatabaseTestNewTest {
    Database database;

    @BeforeEach
    public void setUp() {
        database = new Database();

    }

    @Test
    public void testCreateTable() {
        database.createTable();
        assertEquals(1, database.getTables().size(), "Table should be created successfully");
        assertTrue(database.getTables().contains("Table1"), "Table name should be Table1");
    }

    @Test
    public void testGetTables() {
        database.createTable();
        assertEquals(1, database.getTables().size(), "Table should be created successfully");
        assertTrue(database.getTables().contains("Table1"), "Table name should be Table1");
        database.createTable();
        assertEquals(2, database.getTables().size(), "Table should be created successfully");
        assertTrue(database.getTables().contains("Table2"), "Table name should be Table2");
    }

    @Test
    public void testDeleteTable() {
        database.createTable();
        assertEquals(1, database.getTables().size(), "Table should be created successfully");
        database.deleteTable("Table1");
        assertEquals(0, database.getTables().size(), "Table should be deleted successfully");
    }

    @Test
    public void testUpdateTableName() {
        database.createTable();
        assertEquals(1, database.getTables().size(), "Table should be created successfully");
        database.updateTableName("Table1", "NewTableName");
        assertEquals(1, database.getTables().size(), "Table should still exist after renaming");
        assertTrue(database.getTables().contains("NewTableName"), "Table name should be updated to NewTableName");
    }

    @Test
    public void testIsValidTableName() {
        database.createTable();
        assertFalse(database.isValidTableName("Table1"), "Table name should be invalid because it already exists");
        assertTrue(database.isValidTableName("OtherTable"), "Table name should be valid because it does not exist");
    }

    @Test
    public void testIsValidColumnName() {
        database.createTable();
        assertTrue(database.isValidColumnName("Table1", "NewColumn"),
                "Column name should be valid because it does not exist");
        database.addColumn("Table1");
        assertFalse(database.isValidColumnName("Table1", "Column1"),
                "Column name should be invalid because it already exists in Table1");
    }

    @Test
    public void testColumnAllowBlank() {

    }

    @Test
    public void testAddColumn() {
        // TODO: Implement test
    }

    @Test
    public void testAddRow() {
        // TODO: Implement test
    }

    @Test
    public void testDeleteRow() {
        // TODO: Implement test
    }

    @Test
    public void testDeleteRows() {
        // TODO: Implement test
    }

    @Test
    public void testDeleteColumn() {
        // TODO: Implement test
    }

    @Test
    public void testUpdateCell() {
        // TODO: Implement test
    }

    @Test
    public void testGetCell() {
        // TODO: Implement test
    }

    @Test
    public void testGetColumnNames() {
        // TODO: Implement test
    }

    @Test
    public void testGetRows() {
        // TODO: Implement test
    }

    @Test
    public void testGetRow() {
        // TODO: Implement test
    }

    @Test
    public void testGetColumn() {
        // TODO: Implement test
    }

    @Test
    public void testUpdateColumnType() {
        // TODO: Implement test
    }

    @Test
    public void testUpdateColumnName() {
        // TODO: Implement test
    }

    @Test
    public void testGetColumnType() {
        // TODO: Implement test
    }

    @Test
    public void testGetDefaultColumnValue() {
        // TODO: Implement test
    }

    @Test
    public void testUpdateDefaultColumnValue() {
        // TODO: Implement test
    }

    @Test
    public void testToggleColumnType() {
        // TODO: Implement test
    }

    @Test
    public void testSetColumnAllowBlank() {
        // TODO: Implement test
    }

    @Test
    public void testIsValidValue() {
        // TODO: Implement test
    }

    @Test
    public void testIsValidAllowBlankValue() {
        // TODO: Implement test
    }

    @Test
    public void testIsValidColumnType() {
        // TODO: Implement test
    }

    @Test
    public void testAddTableDesignChangeListener() {
        // TODO: Implement test
    }

    @Test
    public void testRemoveTableDesignChangeListener() {
        // TODO: Implement test
    }

    @Test
    public void testAddTableNameChangeListener() {
        // TODO: Implement test
    }

    @Test
    public void testRemoveTableNameChangeListener() {
        // TODO: Implement test
    }

    @Test
    public void testAddTableDataChangeListener() {
        // TODO: Implement test
    }

    @Test
    public void testRemoveTableDataChangeListener() {
        // TODO: Implement test
    }
}
