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
        db.createTable("TestTable");
        assertTrue(db.getTables().contains("TestTable"));
    }

    @Test
    public void testDeleteTable() {
        db.createTable("TestTable");
        db.deleteTable("TestTable");
        assertFalse(db.getTables().contains("TestTable"));
    }

    @Test
    public void testEditTableName() {
        db.createTable("OldName");
        db.editTableName("OldName", "NewName");
        assertTrue(db.getTables().contains("NewName"));
        assertFalse(db.getTables().contains("OldName"));
    }

    @Test
    public void testAddColumn() {
        db.createTable("TestTable");
        db.addColumn("TestTable", "Name", ColumnType.STRING, true);
        assertEquals(1, db.getColumnNames("TestTable").size());
        assertTrue(db.getColumnNames("TestTable").contains("Name"));
    }

    @Test
    public void testAddRow() {
        db.createTable("TestTable");
        db.addColumn("TestTable", "Name", ColumnType.STRING, true);
        db.addRow("TestTable");
        assertEquals(1, db.getRows("TestTable").size());
    }

    @Test
    public void testDeleteRow() {
        db.createTable("TestTable");
        db.addColumn("TestTable", "Name", ColumnType.STRING, true);
        db.addRow("TestTable");
        db.deleteRow("TestTable", 0);
        assertEquals(0, db.getRows("TestTable").size());
    }

    @Test
    public void testDeleteColumn() {
        db.createTable("TestTable");
        db.addColumn("TestTable", "Name", ColumnType.STRING, true);
        db.deleteColumn("TestTable", "Name");
        assertEquals(0, db.getColumnNames("TestTable").size());
    }

    @Test
    public void testEditCell() {
        db.createTable("TestTable");
        db.addColumn("TestTable", "Name", ColumnType.STRING, true);
        db.addRow("TestTable");
        db.editCell("TestTable", "Name", 0, "John Doe");
        assertEquals("John Doe", db.getCell("TestTable", "Name", 0));
    }

    @Test
    public void testEditColumnType() {
        db.createTable("TestTable");
        db.addColumn("TestTable", "Age", ColumnType.STRING, true);
        db.editColumnType("TestTable", "Age", ColumnType.INTEGER);
        assertEquals(ColumnType.INTEGER, db.getColumnType("TestTable", "Age"));
    }

    @Test
    public void testEditColumnName() {
        db.createTable("TestTable");
        db.addColumn("TestTable", "Name", ColumnType.STRING, true);
        db.editColumnName("TestTable", "Name", "FullName");
        assertTrue(db.getColumnNames("TestTable").contains("FullName"));
        assertFalse(db.getColumnNames("TestTable").contains("Name"));
    }
}