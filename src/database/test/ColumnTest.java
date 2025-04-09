package database.test;

import database.Column;
import database.ColumnType;
import database.Cell;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class ColumnTest {

    @Test
    public void testDefaultConstructor() {
        Column column = new Column();
        assertEquals(ColumnType.STRING, column.getType());
        assertTrue(column.getAllowBlank());
        assertEquals("", column.getDefaultValue());
        assertTrue(column.getCells().isEmpty());
    }

    @Test
    public void testSetAndGetDefaultValue() {
        Column column = new Column();
        column.setDefaultValue("test");
        assertEquals("test", column.getDefaultValue());
    }

    @Test
    public void testEditColumnType() {
        Column column = new Column();
        column.editColumnType(ColumnType.INTEGER);
        assertEquals(ColumnType.INTEGER, column.getType());
    }

    @Test
    public void testToggleColumnType() {
        Column column = new Column();
        column.toggleColumnType();
        assertEquals(ColumnType.INTEGER, column.getType());
        column.toggleColumnType();
        assertEquals(ColumnType.BOOLEAN, column.getType());
        column.toggleColumnType();
        assertEquals(ColumnType.EMAIL, column.getType());
        column.toggleColumnType();
        assertEquals(ColumnType.STRING, column.getType());
    }

    @Test
    public void testRegisterCell() {
        Column column = new Column();
        Cell cell = new Cell(column);
        column.registerCell(cell);
        assertEquals(1, column.getCells().size());
        assertEquals(cell, column.getCells().get(0));
    }

    @Test
    public void testDeleteCell() {
        Column column = new Column();
        Cell cell1 = new Cell(column);
        Cell cell2 = new Cell(column);
        column.registerCell(cell1);
        column.registerCell(cell2);
        column.deleteCell(0);
        assertEquals(1, column.getCells().size());
        assertEquals(cell2, column.getCells().get(0));
    }

    @Test
    public void testSetAllowBlank() {
        Column column = new Column();
        column.setAllowBlank(false);
        assertFalse(column.getAllowBlank());
    }

    @Test
    public void testIsValidValue() {
        Column column = new Column();
        column.editColumnType(ColumnType.INTEGER);
        assertTrue(column.isValidValue("123"));
        assertFalse(column.isValidValue("abc"));

        column.editColumnType(ColumnType.BOOLEAN);
        assertTrue(column.isValidValue("true"));
        assertFalse(column.isValidValue("yes"));

        column.editColumnType(ColumnType.EMAIL);
        assertTrue(column.isValidValue("test@example.com"));
        assertFalse(column.isValidValue("invalid-email"));
    }

    @Test
    public void testResetDefaultValue() {
        Column column = new Column();
        column.setAllowBlank(false);
        column.editColumnType(ColumnType.INTEGER);
        column.resetDefaultValue();
        assertEquals("0", column.getDefaultValue());

        column.editColumnType(ColumnType.BOOLEAN);
        column.resetDefaultValue();
        assertEquals("false", column.getDefaultValue());
    }
}