package database.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import database.Cell;
import database.ColumnType;
import database.Table;

public class TableTest {
    private Table table;

    @BeforeEach
    public void setUp() {
        table = new Table();
    }

    @Test
    public void testCreateColumn() {
        table.createColumn("Name", ColumnType.STRING, true);
        assertEquals(1, table.getColumns().size());
        assertTrue(table.getColumns().contains("Name"));
    }

    @Test
    public void testCreateRow() {
        table.createColumn("Name", ColumnType.STRING, true);
        table.createRow();
        assertEquals(1, table.getRows().size());
    }

    @Test
    public void testGetColumns() {
        table.createColumn("Name", ColumnType.STRING, true);
        table.createColumn("Age", ColumnType.INTEGER, false);
        assertEquals(2, table.getColumns().size());
        assertTrue(table.getColumns().contains("Name"));
        assertTrue(table.getColumns().contains("Age"));
    }

    @Test
    public void testGetRows() {
        table.createColumn("Name", ColumnType.STRING, true);
        table.createRow();
        table.editCell("Name", 0, "John Doe");
        assertEquals(1, table.getRows().size());
        assertEquals("John Doe", table.getRow(0).get(0));
    }

    @Test
    public void testDeleteRow() {
        table.createColumn("Name", ColumnType.STRING, true);
        table.createRow();
        table.deleteRow(0);
        assertEquals(0, table.getRows().size());
    }

    @Test
    public void testDeleteColumn() {
        table.createColumn("Name", ColumnType.STRING, true);
        table.createRow();
        table.deleteColumn("Name");
        assertEquals(0, table.getColumns().size());
    }

    @Test
    public void testGetCell() {
        table.createColumn("Name", ColumnType.STRING, true);
        table.createRow();
        table.editCell("Name", 0, "John Doe");
        Cell cell = table.getCell("Name", 0);
        assertEquals("John Doe", cell.getValue());
    }

    @Test
    public void testEditCell() {
        table.createColumn("Name", ColumnType.STRING, true);
        table.createRow();
        table.editCell("Name", 0, "John Doe");
        assertEquals("John Doe", table.getCell("Name", 0).getValue());
    }

    @Test
    public void testEditColumnName() {
        table.createColumn("Name", ColumnType.STRING, true);
        table.editColumnName("Name", "FullName");
        assertTrue(table.getColumns().contains("FullName"));
        assertFalse(table.getColumns().contains("Name"));
    }

    @Test
    public void testEditColumnType() {
        table.createColumn("Age", ColumnType.STRING, true);
        table.editColumnType("Age", ColumnType.INTEGER);
        // Assuming Column class has a method getColumnType()
        assertEquals(ColumnType.INTEGER, table.getColumnType("Age"));
    }
}