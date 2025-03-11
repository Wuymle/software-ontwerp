package dataBase.dataBaseTest;

import dataBase.dataBase;
import dataBase.Table;
import dataBase.columnType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {
    private dataBase db;

    @BeforeEach
    public void setUp() {
        db = new dataBase();
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
        db.addColumn("TestTable", "Name", columnType.STRING, true);
        assertEquals(1, db.getColumnNames("TestTable").size());
        assertTrue(db.getColumnNames("TestTable").contains("Name"));
    }

    @Test
    public void testAddRow() {
        db.createTable("TestTable");
        db.addColumn("TestTable", "Name", columnType.STRING, true);
        db.addRow("TestTable");
        assertEquals(1, db.getRows("TestTable").size());
    }

    @Test
    public void testDeleteRow() {
        db.createTable("TestTable");
        db.addColumn("TestTable", "Name", columnType.STRING, true);
        db.addRow("TestTable");
        db.deleteRow("TestTable", 0);
        assertEquals(0, db.getRows("TestTable").size());
    }

    @Test
    public void testDeleteColumn() {
        db.createTable("TestTable");
        db.addColumn("TestTable", "Name", columnType.STRING, true);
        db.deleteColumn("TestTable", "Name");
        assertEquals(0, db.getColumnNames("TestTable").size());
    }

    @Test
    public void testEditCell() {
        db.createTable("TestTable");
        db.addColumn("TestTable", "Name", columnType.STRING, true);
        db.addRow("TestTable");
        db.editCell("TestTable", "Name", 0, "John Doe");
        assertEquals("John Doe", db.getCell("TestTable", "Name", 0));
    }

    @Test
    public void testEditColumnType() {
        db.createTable("TestTable");
        db.addColumn("TestTable", "Age", columnType.STRING, true);
        db.editColumnType("TestTable", "Age", columnType.INTEGER);
        assertEquals(columnType.INTEGER, db.getColumnType("TestTable", "Age"));
    }

    @Test
    public void testEditColumnName() {
        db.createTable("TestTable");
        db.addColumn("TestTable", "Name", columnType.STRING, true);
        db.editColumnName("TestTable", "Name", "FullName");
        assertTrue(db.getColumnNames("TestTable").contains("FullName"));
        assertFalse(db.getColumnNames("TestTable").contains("Name"));
    }

    @Test
    public void testEditTableNameWithSameName() {
        db.createTable("SameName");
        db.editTableName("SameName", "SameName");
        assertTrue(db.getTables().contains("SameName"));
    }

    @Test
    public void testIsValidTableName() {
        db.createTable("TestTable");
        assertFalse(db.isValidTableName("TestTable"));
        assertTrue(db.isValidTableName("NewTable"));
    }

    @Test
    public void testIsCellValid() {
        db.createTable("TestTable");
        db.addColumn("TestTable", "Age", columnType.INTEGER, false);
        db.addRow("TestTable");
        db.editCell("TestTable", "Age", 0, "25");
        assertTrue(db.isCellValid("TestTable", "Age", 0));
    }

    @Test
    public void testIsCellValidInvalidInteger() {
        db.createTable("TestTable");
        db.addColumn("TestTable", "Age", columnType.INTEGER, false);
        db.addRow("TestTable");
        db.editCell("TestTable", "Age", 0, "invalid");
        assertFalse(db.isCellValid("TestTable", "Age", 0));
    }

    @Test
    public void testIsCellValidString() {
        db.createTable("TestTable");
        db.addColumn("TestTable", "Name", columnType.STRING, false);
        db.addRow("TestTable");
        db.editCell("TestTable", "Name", 0, "John Doe");
        assertTrue(db.isCellValid("TestTable", "Name", 0));
    }

    @Test
    public void testIsCellValidBooleanTrue() {
        db.createTable("TestTable");
        db.addColumn("TestTable", "Active", columnType.BOOLEAN, false);
        db.addRow("TestTable");
        db.editCell("TestTable", "Active", 0, "true");
        assertTrue(db.isCellValid("TestTable", "Active", 0));
    }

    @Test
    public void testIsCellValidBooleanFalse() {
        db.createTable("TestTable");
        db.addColumn("TestTable", "Active", columnType.BOOLEAN, false);
        db.addRow("TestTable");
        db.editCell("TestTable", "Active", 0, "false");
        assertTrue(db.isCellValid("TestTable", "Active", 0));
    }

    @Test
    public void testIsCellValidInvalidBoolean() {
        db.createTable("TestTable");
        db.addColumn("TestTable", "Active", columnType.BOOLEAN, false);
        db.addRow("TestTable");
        db.editCell("TestTable", "Active", 0, "notABoolean");
        assertFalse(db.isCellValid("TestTable", "Active", 0));
    }
}