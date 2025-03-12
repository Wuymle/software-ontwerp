package database.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import database.Table;
import database.ColumnType;
import static org.junit.jupiter.api.Assertions.*;

public class TableTest {

    private Table table;

    @BeforeEach
    public void setUp() {
        table = new Table();
    }

    @Test
    public void testCreateColumn() {
        table.createColumn("NewColumn");
        assertTrue(table.getColumns().contains("NewColumn"));
    }

    @Test
    public void testCreateRow() {
        table.createColumn("Column1");
        table.createRow();
        assertEquals(1, table.getRows().size());
    }

    @Test
    public void testGetColumnType() {
        table.createColumn("Column1");
        assertEquals(ColumnType.STRING, table.getColumnType("Column1"));
    }

    @Test
    public void testDeleteRow() {
        table.createColumn("Column1");
        table.createRow();
        table.deleteRow(0);
        assertEquals(0, table.getRows().size());
    }

    @Test
    public void testDeleteColumn() {
        table.createColumn("Column1");
        table.createRow();
        table.deleteColumn("Column1");
        assertFalse(table.getColumns().contains("Column1"));
    }

    @Test
    public void testColumnAllowBlank() {
        table.createColumn("Column1");
        assertFalse(table.columnAllowBlank("Column1"));
    }

    @Test
    public void testGetCell() {
        table.createColumn("Column1");
        table.createRow();
        assertNotNull(table.getCell("Column1", 0));
    }

    @Test
    public void testEditCell() {
        table.createColumn("Column1");
        table.createRow();
        table.editCell("Column1", 0, "NewValue");
        assertEquals("NewValue", table.getCell("Column1", 0).getValue());
    }

    @Test
    public void testEditColumnName() {
        table.createColumn("OldName");
        table.editColumnName("OldName", "NewName");
        assertTrue(table.getColumns().contains("NewName"));
        assertFalse(table.getColumns().contains("OldName"));
    }

    @Test
    public void testEditColumnType() {
        table.createColumn("Column1");
        table.editColumnType("Column1", ColumnType.INTEGER);
        assertEquals(ColumnType.INTEGER, table.getColumnType("Column1"));
    }

    @Test
    public void testEditDefaultColumnValue() {
        table.createColumn("Column1");
        table.editDefaultColumnValue("Column1", "DefaultValue");
        assertEquals("DefaultValue", table.getDefaultColumnValue("Column1"));
    }

    @Test
    public void testToggleColumnType() {
        table.createColumn("Column1");
        table.toggleColumnType("Column1");
        assertEquals(ColumnType.INTEGER, table.getColumnType("Column1"));
    }

    @Test
    public void testGetDefaultColumnValue() {
        table.createColumn("Column1");
        assertEquals("", table.getDefaultColumnValue("Column1"));
    }
}