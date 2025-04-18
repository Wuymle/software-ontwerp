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
    public void testUpdateColumnType() {
        Column column = new Column();
        column.updateColumnType(ColumnType.INTEGER);
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
        column.setDefaultValue("test");
        column.setAllowBlank(false);
        assertFalse(column.getAllowBlank());
    }

    @Test
    public void testIsValidValue() {
        Column column = new Column();
        column.updateColumnType(ColumnType.INTEGER);
        assertTrue(column.isValidValue("123", ColumnType.INTEGER));
        assertFalse(column.isValidValue("abc", ColumnType.INTEGER));

        column.updateColumnType(ColumnType.BOOLEAN);
        assertTrue(column.isValidValue("true", ColumnType.BOOLEAN));
        assertFalse(column.isValidValue("yes", ColumnType.BOOLEAN));

        column.updateColumnType(ColumnType.EMAIL);
        assertTrue(column.isValidValue("test@example.com", ColumnType.EMAIL));
        assertFalse(column.isValidValue("invalid-email", ColumnType.EMAIL));
    }

    @Test
    public void testResetDefaultValue() {
        Column column = new Column();
        column.setDefaultValue("test");
        column.setAllowBlank(false);
        column.updateColumnType(ColumnType.INTEGER);
        column.resetDefaultValue();
        assertEquals("0", column.getDefaultValue());

        column.updateColumnType(ColumnType.BOOLEAN);
        column.resetDefaultValue();
        assertEquals("false", column.getDefaultValue());
    }

    @Test
    public void testSetDefaultValueInvalidInteger() {
        Column column = new Column();
        column.updateColumnType(ColumnType.INTEGER);
        assertThrows(Error.class, () -> column.setDefaultValue("notANumber"));
    }

    @Test
    public void testSetDefaultValueInvalidBoolean() {
        Column column = new Column();
        column.updateColumnType(ColumnType.BOOLEAN);
        assertThrows(Error.class, () -> column.setDefaultValue("maybe"));
    }

    @Test
    public void testSetDefaultValueInvalidEmail() {
        Column column = new Column();
        column.updateColumnType(ColumnType.EMAIL);
        assertThrows(Error.class, () -> column.setDefaultValue("invalidemail"));
    }

    @Test
    public void testSetAllowBlankFalseFailsDueToDefaultValue() {
        Column column = new Column(); // defaultValue is ""
        assertThrows(Error.class, () -> column.setAllowBlank(false));
    }

    @Test
    public void testSetAllowBlankFalseFailsDueToBlankCell() {
        Column column = new Column(); // defaultValue = "" (leeg)
        Cell cell = new Cell(column); // cel krijgt "" als waarde
        column.registerCell(cell);    // voeg lege cel toe
        assertThrows(Error.class, () -> column.setAllowBlank(false));
    }

    @Test
    public void testIsValidAllowBlankValueTrueAlwaysReturnsTrue() {
        Column column = new Column();
        assertTrue(column.isValidAllowBlankValue(true));
    }

    @Test
    public void testIsValidAllowBlankValueFalseWithBlankDefault() {
        Column column = new Column();
        column.setDefaultValue("");
        assertFalse(column.isValidAllowBlankValue(false));
    }

    @Test
    public void testIsValidColumnTypeReturnsFalseIfCellInvalid() {
        Column column = new Column();
        column.setDefaultValue("abc"); // valid string
        Cell cell = new Cell(column);
        column.registerCell(cell);
        assertFalse(column.isValidColumnType(ColumnType.INTEGER));
    }
}