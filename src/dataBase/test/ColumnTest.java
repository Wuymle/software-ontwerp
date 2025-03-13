package database.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import database.Cell;
import database.Column;
import database.ColumnType;

import static org.junit.jupiter.api.Assertions.*;

public class ColumnTest {

    private Column column;

    @BeforeEach
    public void setUp() {
        column = new Column();
    }

    @Test
    public void testDefaultColumnType() {
        assertEquals(ColumnType.STRING, column.getType());
    }

    @Test
    public void testDefaultAllowBlank() {
        assertFalse(column.getAllowBlank());
    }

    @Test
    public void testRegisterCell() {
        Cell cell = new Cell(column);
        column.registerCell(cell);
        assertEquals(1, column.getCells().size());
        assertEquals(cell, column.getCells().get(0));
    }

    @Test
    public void testEditColumnType() {
        column.editColumnType(ColumnType.INTEGER);
        assertEquals(ColumnType.INTEGER, column.getType());
    }

    @Test
    public void testEditColumnTypeUpdatesCells() {
        Cell cell1 = new Cell(column);
        Cell cell2 = new Cell(column);
        column.registerCell(cell1);
        column.registerCell(cell2);

        cell1.setValue("123");
        cell2.setValue("456");

        column.editColumnType(ColumnType.INTEGER);

        assertTrue(cell1.isValid());
        assertTrue(cell2.isValid());
    }

    @Test
    public void testEditColumnTypeInvalidatesCells() {
        Cell cell1 = new Cell(column);
        Cell cell2 = new Cell(column);
        column.registerCell(cell1);
        column.registerCell(cell2);

        cell1.setValue("123");
        cell2.setValue("abc");

        column.editColumnType(ColumnType.INTEGER);

        assertTrue(cell1.isValid());
        assertFalse(cell2.isValid());
    }

    @Test
    public void testGetCells() {
        Cell cell1 = new Cell(column);
        Cell cell2 = new Cell(column);
        column.registerCell(cell1);
        column.registerCell(cell2);

        assertEquals(2, column.getCells().size());
        assertTrue(column.getCells().contains(cell1));
        assertTrue(column.getCells().contains(cell2));
    }

    @Test
    public void testDefaultValueForString() {
        column.editColumnType(ColumnType.STRING);
        column.setAllowBlank(false);
        column.resetDefaultValue();
        assertEquals("", column.getDefaultValue());
    }

    @Test
    public void testDefaultValueForInteger() {
        column.editColumnType(ColumnType.INTEGER);
        column.setAllowBlank(false);
        column.resetDefaultValue();
        assertEquals("0", column.getDefaultValue());
    }

    @Test
    public void testDefaultValueForBoolean() {
        column.editColumnType(ColumnType.BOOLEAN);
        column.setAllowBlank(false);
        column.resetDefaultValue();
        assertEquals("false", column.getDefaultValue());
    }

    @Test
    public void testDefaultValueForEmail() {
        column.editColumnType(ColumnType.EMAIL);
        column.setAllowBlank(false);
        column.resetDefaultValue();
        assertEquals("@", column.getDefaultValue());
    }

    @Test
    public void testDefaultValueWhenAllowBlank() {
        column.setAllowBlank(true);
        column.resetDefaultValue();
        assertNull(column.getDefaultValue());
    }

    @Test
    public void testToggleColumnTypeFromStringToInteger() {
        column.editColumnType(ColumnType.STRING);
        column.toggleColumnType();
        assertEquals(ColumnType.INTEGER, column.getType());
    }

    @Test
    public void testToggleColumnTypeFromIntegerToBoolean() {
        column.editColumnType(ColumnType.INTEGER);
        column.toggleColumnType();
        assertEquals(ColumnType.BOOLEAN, column.getType());
    }

    @Test
    public void testToggleColumnTypeFromBooleanToEmail() {
        column.editColumnType(ColumnType.BOOLEAN);
        column.toggleColumnType();
        assertEquals(ColumnType.EMAIL, column.getType());
    }

    @Test
    public void testToggleColumnTypeFromEmailToString() {
        column.editColumnType(ColumnType.EMAIL);
        column.toggleColumnType();
        assertEquals(ColumnType.STRING, column.getType());
    }

    @Test
    public void testToggleColumnTypeUpdatesCells() {
        Cell cell1 = new Cell(column);
        Cell cell2 = new Cell(column);
        column.registerCell(cell1);
        column.registerCell(cell2);

        cell1.setValue("123");
        cell2.setValue("456");

        column.toggleColumnType(); // STRING to INTEGER

        assertTrue(cell1.isValid());
        assertTrue(cell2.isValid());

        column.toggleColumnType(); // INTEGER to BOOLEAN

        assertFalse(cell1.isValid());
        assertFalse(cell2.isValid());
    }

    @Test
    public void testValidDefaultValueForString() {
        column.editColumnType(ColumnType.STRING);
        column.setDefaultValue("validString");
        assertTrue(column.getValidDefaultValue());
    }

    @Test
    public void testInvalidDefaultValueForString() {
        column.editColumnType(ColumnType.STRING);
        column.setDefaultValue(null);
        assertTrue(column.getValidDefaultValue());
    }

    @Test
    public void testValidDefaultValueForInteger() {
        column.editColumnType(ColumnType.INTEGER);
        column.setDefaultValue("123");
        assertTrue(column.getValidDefaultValue());
    }

    @Test
    public void testInvalidDefaultValueForInteger() {
        column.editColumnType(ColumnType.INTEGER);
        column.setDefaultValue("invalidInteger");
        assertFalse(column.getValidDefaultValue());
    }

    @Test
    public void testValidDefaultValueForBoolean() {
        column.editColumnType(ColumnType.BOOLEAN);
        column.setDefaultValue("true");
        assertTrue(column.getValidDefaultValue());
    }

    @Test
    public void testInvalidDefaultValueForBoolean() {
        column.editColumnType(ColumnType.BOOLEAN);
        column.setDefaultValue("notABoolean");
        assertFalse(column.getValidDefaultValue());
    }

    @Test
    public void testValidDefaultValueForEmail() {
        column.editColumnType(ColumnType.EMAIL);
        column.setDefaultValue("test@example.com");
        assertTrue(column.getValidDefaultValue());
    }

    @Test
    public void testInvalidDefaultValueForEmail() {
        column.editColumnType(ColumnType.EMAIL);
        column.setDefaultValue("invalidEmail");
        assertFalse(column.getValidDefaultValue());
    }
}
