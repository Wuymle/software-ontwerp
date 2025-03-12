package dataBase.dataBaseTest;

import dataBase.dataBase;
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
        db.addColumn("Users", "Name", columnType.STRING, true);
        assertTrue(db.getColumnNames("Users").contains("Name"));
    }

    @Test
    public void testAddRow() {
        db.createTable("Users");
        db.addColumn("Users", "Name", columnType.STRING, true);
        db.addRow("Users");
        assertEquals(1, db.getRows("Users").size());
    }

    @Test
    public void testDeleteRow() {
        db.createTable("Users");
        db.addColumn("Users", "Name", columnType.STRING, true);
        db.addRow("Users");
        db.deleteRow("Users", 0);
        assertEquals(0, db.getRows("Users").size());
    }

    @Test
    public void testDeleteColumn() {
        db.createTable("Users");
        db.addColumn("Users", "Name", columnType.STRING, true);
        db.deleteColumn("Users", "Name");
        assertFalse(db.getColumnNames("Users").contains("Name"));
    }

    @Test
    public void testEditCell() {
        db.createTable("Users");
        db.addColumn("Users", "Name", columnType.STRING, true);
        db.addRow("Users");
        db.editCell("Users", "Name", 0, "John Doe");
        assertEquals("John Doe", db.getCell("Users", "Name", 0));
    }

    @Test
    public void testIsCellValid() {
        db.createTable("Users");
        db.addColumn("Users", "Age", columnType.INTEGER, false);
        db.addRow("Users");
        db.editCell("Users", "Age", 0, "25");
        assertTrue(db.isCellValid("Users", "Age", 0));
    }

    @Test
    public void testEditColumnType() {
        db.createTable("Users");
        db.addColumn("Users", "Age", columnType.STRING, true);
        db.editColumnType("Users", "Age", columnType.INTEGER);
        assertEquals(columnType.INTEGER, db.getColumnType("Users", "Age"));
    }

    @Test
    public void testEditColumnName() {
        db.createTable("Users");
        db.addColumn("Users", "Name", columnType.STRING, true);
        db.editColumnName("Users", "Name", "FullName");
        assertTrue(db.getColumnNames("Users").contains("FullName"));
        assertFalse(db.getColumnNames("Users").contains("Name"));
    }
}