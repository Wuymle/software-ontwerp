package database.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import database.dataBase;
import database.ColumnType;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {

    private dataBase db;

    @BeforeEach
    public void setUp() {
        db = new dataBase();
        db.createTable("TestTable");
    }

    @Test
    public void testCreateTable() {
        db.createTable("NewTable");
        assertTrue(db.getTables().contains("NewTable"));
    }

    @Test
    public void testDeleteTable() {
        db.deleteTable("TestTable");
        assertFalse(db.getTables().contains("TestTable"));
    }

    @Test
    public void testEditTableName() {
        db.editTableName("TestTable", "RenamedTable");
        assertTrue(db.getTables().contains("RenamedTable"));
        assertFalse(db.getTables().contains("TestTable"));
    }

    @Test
    public void testAddColumn() {
        db.addColumn("TestTable", "NewColumn");
        assertTrue(db.getColumnNames("TestTable").contains("NewColumn"));
    }

    @Test
    public void testDeleteColumn() {
        db.addColumn("TestTable", "ColumnToDelete");
        db.deleteColumn("TestTable", "ColumnToDelete");
        assertFalse(db.getColumnNames("TestTable").contains("ColumnToDelete"));
    }

    @Test
    public void testAddRow() {
        db.addRow("TestTable");
        assertEquals(1, db.getRows("TestTable").size());
    }

    @Test
    public void testDeleteRow() {
        db.addRow("TestTable");
        db.deleteRow("TestTable", 0);
        assertEquals(0, db.getRows("TestTable").size());
    }

    @Test
    public void testEditCell() {
        db.addColumn("TestTable", "Name");
        db.addRow("TestTable");
        db.editCell("TestTable", "Name", 0, "John Doe");
        assertEquals("John Doe", db.getCell("TestTable", "Name", 0));
    }

    @Test
    public void testEditColumnType() {
        db.addColumn("TestTable", "Age");
        db.editColumnType("TestTable", "Age", ColumnType.INTEGER);
        assertEquals(ColumnType.INTEGER, db.getColumnType("TestTable", "Age"));
    }

    @Test
    public void testEditDefaultColumnValue() {
        db.addColumn("TestTable", "Status");
        db.editDefaultColumnValue("TestTable", "Status", "Active");
        assertEquals("Active", db.getDefaultColumnValue("TestTable", "Status"));
    }

    @Test
    public void testToggleColumnType() {
        db.addColumn("TestTable", "ToggleColumn");
        db.toggleColumnType("TestTable", "ToggleColumn");
        assertEquals(ColumnType.INTEGER, db.getColumnType("TestTable", "ToggleColumn"));
    }

    @Test
    public void testIsCellValid() {
        db.addColumn("TestTable", "Age");
        db.addRow("TestTable");
        db.editCell("TestTable", "Age", 0, "25");
        assertTrue(db.isCellValid("TestTable", "Age", 0));
    }

    @Test
    public void testColumnAllowBlank() {
        db.addColumn("TestTable", "OptionalColumn");
        assertFalse(db.columnAllowBlank("TestTable", "OptionalColumn"));
    }
}