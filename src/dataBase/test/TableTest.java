package database.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import database.Cell;
import database.Column;
import database.ColumnType;
import database.Table;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

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
        ArrayList<String> columns = table.getColumns();
        assertEquals(2, columns.size());
        assertTrue(columns.contains("Name"));
        assertTrue(columns.contains("Age"));
    }

    @Test
    public void testGetColumnType() {
        table.createColumn("Name", ColumnType.STRING, true);
        assertEquals(ColumnType.STRING, table.getColumnType("Name"));
    }

    @Test
    public void testGetRows() {
        table.createColumn("Name", ColumnType.STRING, true);
        table.createRow();
        table.createRow();
        assertEquals(2, table.getRows().size());
    }

    @Test
    public void testGetRow() {
        table.createColumn("Name", ColumnType.STRING, true);
        table.createRow();
        table.editCell("Name", 0, "John Doe");
        ArrayList<Object> row = table.getRow(0);
        assertEquals(1, row.size());
        assertEquals("John Doe", row.get(0));
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
        assertEquals(ColumnType.INTEGER, table.getColumnType("Age"));
    }
}