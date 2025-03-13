package database.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import database.ColumnType;
import database.Database;

public class DatabaseTest {

    private Database db;

    @BeforeEach
    public void setUp() {
        db = new Database();
    }

    @Test
    public void testCreateTable() {
        db.createTable("Users");
        assertTrue(db.getTables().contains("Users"));
    }

    @Test
    public void testDeleteTable() {
        db.createTable("Users");
        db.deleteTable("Users");
        assertFalse(db.getTables().contains("Users"));
    }

    @Test
    public void testEditTableName() {
        db.createTable("Users");
        db.editTableName("Users", "Customers");
        assertTrue(db.getTables().contains("Customers"));
        assertFalse(db.getTables().contains("Users"));
    }

    @Test
    public void testAddColumn() {
        db.createTable("Users");
        db.addColumn("Users", "Name", ColumnType.STRING, true);
        assertTrue(db.getColumnNames("Users").contains("Name"));
    }

    @Test
    public void testAddRow() {
        db.createTable("Users");
        db.addColumn("Users", "Name", ColumnType.STRING, true);
        db.addRow("Users");
        assertEquals(1, db.getRows("Users").size());
    }

    @Test
    public void testDeleteRow() {
        db.createTable("Users");
        db.addColumn("Users", "Name", ColumnType.STRING, true);
        db.addRow("Users");
        db.deleteRow("Users", 0);
        assertEquals(0, db.getRows("Users").size());
    }

    @Test
    public void testDeleteColumn() {
        db.createTable("Users");
        db.addColumn("Users", "Name", ColumnType.STRING, true);
        db.deleteColumn("Users", "Name");
        assertFalse(db.getColumnNames("Users").contains("Name"));
    }

    @Test
    public void testEditCell() {
        db.createTable("Users");
        db.addColumn("Users", "Name", ColumnType.STRING, true);
        db.addRow("Users");
        db.editCell("Users", "Name", 0, "John Doe");
        assertEquals("John Doe", db.getCell("Users", "Name", 0));
    }

    @Test
    public void testIsCellValid() {
        db.createTable("Users");
        db.addColumn("Users", "Age", ColumnType.INTEGER, false);
        db.addRow("Users");
        db.editCell("Users", "Age", 0, "25");
        assertTrue(db.isCellValid("Users", "Age", 0));
    }

    @Test
    public void testEditColumnType() {
        db.createTable("Users");
        db.addColumn("Users", "Age", ColumnType.STRING, true);
        db.editColumnType("Users", "Age", ColumnType.INTEGER);
        assertEquals(ColumnType.INTEGER, db.getColumnType("Users", "Age"));
    }

    @Test
    public void testEditColumnName() {
        db.createTable("Users");
        db.addColumn("Users", "Name", ColumnType.STRING, true);
        db.editColumnName("Users", "Name", "FullName");
        assertTrue(db.getColumnNames("Users").contains("FullName"));
        assertFalse(db.getColumnNames("Users").contains("Name"));
    }
}