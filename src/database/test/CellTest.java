package database.test;

import database.Cell;
import database.Column;
import database.ColumnType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CellTest {

    private Column column;
    private Cell cell;

    @BeforeEach
    public void setUp() {
        column = new Column();
        cell = new Cell(column);
    }

    @Test
    public void testDefaultCellValue() {
        assertEquals("", cell.getValue());
    }

    @Test
    public void testSetValue() {
        cell.setValue("testValue");
        assertEquals("testValue", cell.getValue());
    }

    @Test
    public void testIsValidForString() {
        column.editColumnType(ColumnType.STRING);
        cell.setValue("validString");
        assertTrue(cell.isValid());
    }

    @Test
    public void testIsValidForInteger() {
        column.editColumnType(ColumnType.INTEGER);
        cell.setValue("123");
        assertTrue(cell.isValid());
    }

    @Test
    public void testIsInvalidForInteger() {
        column.editColumnType(ColumnType.INTEGER);
        cell.setValue("invalidInteger");
        assertFalse(cell.isValid());
    }

    @Test
    public void testIsValidForBoolean() {
        column.editColumnType(ColumnType.BOOLEAN);
        cell.setValue("TRUE");
        assertTrue(cell.isValid());
        cell.setValue("FALSE");
        assertTrue(cell.isValid());
    }

    @Test
    public void testIsInvalidForBoolean() {
        column.editColumnType(ColumnType.BOOLEAN);
        cell.setValue("notABoolean");
        assertFalse(cell.isValid());
    }

    @Test
    public void testIsValidForEmail() {
        column.editColumnType(ColumnType.EMAIL);
        cell.setValue("test@example.com");
        assertTrue(cell.isValid());
    }

    @Test
    public void testIsInvalidForEmail() {
        column.editColumnType(ColumnType.EMAIL);
        cell.setValue("invalidEmail");
        assertFalse(cell.isValid());
    }

    @Test
    public void testGetColumn() {
        assertEquals(column, cell.getColumn());
    }
}