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
    public void testColumnAllowBlank() {}

    @Test
    public void testAddColumn() {}

    @Test
    public void testAddRow() {}

    @Test
    public void testDeleteRow() {}

    @Test
    public void testDeleteRows() {}

    @Test
    public void testDeleteColumn() {}

    @Test
    public void testUpdateCell() {}

    @Test
    public void testGetCell() {}

    @Test
    public void testGetColumnNames() {}

    @Test
    public void testGetRows() {}

    @Test
    public void testGetRow() {}

    @Test
    public void testGetColumn() {}

    @Test
    public void testUpdateColumnType() {}

    @Test
    public void testUpdateColumnName() {}

    @Test
    public void testGetColumnType() {}

    @Test
    public void testGetDefaultColumnValue() {}

    @Test
    public void testUpdateDefaultColumnValue() {}

    @Test
    public void testToggleColumnType() {}

    @Test
    public void testSetColumnAllowBlank() {}

    @Test
    public void testIsValidValue() {}

    @Test
    public void testIsValidAllowBlankValue() {}

    @Test
    public void testIsValidColumnType() {}

    @Test
    public void testAddTableDesignChangeListener() {}

    @Test
    public void testRemoveTableDesignChangeListener() {}

    @Test
    public void testAddTableNameChangeListener() {}

    @Test
    public void testRemoveTableNameChangeListener() {}

    @Test
    public void testAddTableDataChangeListener() {}

    @Test
    public void testRemoveTableDataChangeListener() {}
}
