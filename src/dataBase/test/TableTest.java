package database.test;

import database.Table;
import database.ColumnType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TableTest {
    private Table table;

    @BeforeEach
    public void setUp() {
        table = new Table();
    }

    @Test
    public void testCreateColumn() {
        table.createColumn();
        assertEquals(1, table.getColumns().size());
    }

    @Test
    public void testCreateRow() {
        table.createColumn();
        table.createRow();
        assertEquals(1, table.getRows().size());
    }

    @Test
    public void testGetColumns() {
        table.createColumn();
        assertTrue(table.getColumns().contains("Column1"));
    }

    @Test
    public void testGetColumn() {
        table.createColumn();
        table.createRow();
        assertEquals(1, table.getColumn("Column1").size());
    }

    @Test
    public void testGetColumnType() {
        table.createColumn();
        assertEquals(ColumnType.STRING, table.getColumnType("Column1"));
    }

    @Test
    public void testGetRows() {
        table.createColumn();
        table.createRow();
        assertEquals(1, table.getRows().size());
    }

    @Test
    public void testGetRow() {
        table.createColumn();
        table.createRow();
        assertEquals(1, table.getRow(0).size());
    }

    @Test
    public void testDeleteRow() {
        table.createColumn();
        table.createRow();
        table.deleteRow(0);
        assertEquals(0, table.getRows().size());
    }

    @Test
    public void testDeleteColumn() {
        table.createColumn();
        table.deleteColumn("Column1");
        assertEquals(0, table.getColumns().size());
    }

    @Test
    public void testColumnAllowBlank() {
        table.createColumn();
        assertFalse(table.columnAllowBlank("Column1"));
    }

    @Test
    public void testGetCell() {
        table.createColumn();
        table.createRow();
        assertNotNull(table.getCell("Column1", 0));
    }

    @Test
    public void testEditCell() {
        table.createColumn();
        table.createRow();
        table.editCell("Column1", 0, "newValue");
        assertEquals("newValue", table.getCell("Column1", 0).getValue());
    }

    @Test
    public void testEditColumnName() {
        table.createColumn();
        table.editColumnName("Column1", "NewColumn");
        assertTrue(table.getColumns().contains("NewColumn"));
        assertFalse(table.getColumns().contains("Column1"));
    }

    @Test
    public void testEditColumnType() {
        table.createColumn();
        table.editColumnType("Column1", ColumnType.INTEGER);
        assertEquals(ColumnType.INTEGER, table.getColumnType("Column1"));
    }

    @Test
    public void testEditDefaultColumnValue() {
        table.createColumn();
        table.editDefaultColumnValue("Column1", "defaultValue");
        assertEquals("defaultValue", table.getDefaultColumnValue("Column1"));
    }

    @Test
    public void testToggleColumnType() {
        table.createColumn();
        table.toggleColumnType("Column1");
        assertEquals(ColumnType.INTEGER, table.getColumnType("Column1"));
    }

    @Test
    public void testGetDefaultColumnValue() {
        table.createColumn();
        assertEquals("", table.getDefaultColumnValue("Column1"));
    }
}