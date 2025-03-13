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
        db.createTable();
    }

    @Test
    public void testCreateTable() {
        assertEquals(1, db.getTables().size());
    }

    @Test
    public void testDeleteTable() {
        db.deleteTable("Table1");
        assertEquals(0, db.getTables().size());
    }

    @Test
    public void testEditTableName() {
        db.editTableName("Table1", "NewTable");
        assertTrue(db.getTables().contains("NewTable"));
        assertFalse(db.getTables().contains("Table1"));
    }

    @Test
    public void testAddColumn() {
        db.addColumn("Table1");
        assertTrue(db.getColumnNames("Table1").contains("Column1"));
    }

    @Test
    public void testAddRow() {
        db.addRow("Table1");
        assertEquals(1, db.getRows("Table1").size());
    }

    @Test
    public void testDeleteRow() {
        db.addRow("Table1");
        db.deleteRow("Table1", 0);
        assertEquals(0, db.getRows("Table1").size());
    }

    @Test
    public void testDeleteColumn() {
        db.addColumn("Table1");
        db.deleteColumn("Table1", "Column1");
        assertFalse(db.getColumnNames("Table1").contains("Column1"));
    }

    @Test
    public void testEditCell() {
        db.addColumn("Table1");
        db.addRow("Table1");
        db.editCell("Table1", "Column1", 0, "TestValue");
        assertEquals("TestValue", db.getCell("Table1", "Column1", 0));
    }

    @Test
    public void testIsCellValid() {
        db.addColumn("Table1");
        db.addRow("Table1");
        db.editCell("Table1", "Column1", 0, "TestValue");
        assertTrue(db.isCellValid("Table1", "Column1", 0));
    }

    @Test
    public void testEditColumnType() {
        db.addColumn("Table1");
        db.editColumnType("Table1", "Column1", ColumnType.INTEGER);
        assertEquals(ColumnType.INTEGER, db.getColumnType("Table1", "Column1"));
    }

    @Test
    public void testEditDefaultColumnValue() {
        db.addColumn("Table1");
        db.editDefaultColumnValue("Table1", "Column1", "DefaultValue");
        assertEquals("DefaultValue", db.getDefaultColumnValue("Table1", "Column1"));
    }

    @Test
    public void testToggleColumnType() {
        db.addColumn("Table1");
        db.toggleColumnType("Table1", "Column1");
        assertEquals(ColumnType.INTEGER, db.getColumnType("Table1", "Column1"));
    }
}