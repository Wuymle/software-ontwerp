package dataBase.test;

import dataBase.Cell;
import dataBase.Column;
import dataBase.ColumnType;
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
        assertNull(cell.getValue());
    }

    @Test
    public void testSetValueInteger() {
        column.editColumnType(ColumnType.INTEGER);
        cell.setValue("123");
        assertEquals("123", cell.getValue());
        assertTrue(cell.isValid());
    }

    @Test
    public void testSetValueInvalidInteger() {
        column.editColumnType(ColumnType.INTEGER);
        cell.setValue("abc");
        assertFalse(cell.isValid());
    }

    @Test
    public void testSetValueString() {
        column.editColumnType(ColumnType.STRING);
        cell.setValue("test");
        assertEquals("test", cell.getValue());
        assertTrue(cell.isValid());
    }

    @Test
    public void testSetValueBooleanTrue() {
        column.editColumnType(ColumnType.BOOLEAN);
        cell.setValue("true");
        assertEquals("true", cell.getValue());
        assertTrue(cell.isValid());
    }

    @Test
    public void testSetValueBooleanFalse() {
        column.editColumnType(ColumnType.BOOLEAN);
        cell.setValue("false");
        assertEquals("false", cell.getValue());
        assertTrue(cell.isValid());
    }

    @Test
    public void testSetValueInvalidBoolean() {
        column.editColumnType(ColumnType.BOOLEAN);
        cell.setValue("notABoolean");
        assertFalse(cell.isValid());
    }

    @Test
    public void testSetValueEmail() {
        column.editColumnType(ColumnType.EMAIL);
        cell.setValue("test@example.com");
        assertEquals("test@example.com", cell.getValue());
        assertTrue(cell.isValid());
    }

    @Test
    public void testSetValueInvalidEmail() {
        column.editColumnType(ColumnType.EMAIL);
        cell.setValue("invalidEmail");
        assertFalse(cell.isValid());
    }

    @Test
    public void testSetDefaultAllowBlank() {
        column.editColumnType(ColumnType.STRING);
        column.setAllowBlank(true);
        cell.setDefault();
        assertNull(cell.getValue());
    }

    @Test
    public void testSetDefaultNotAllowBlank() {
        column.editColumnType(ColumnType.STRING);
        column.setAllowBlank(false);
        cell.setDefault();
        assertEquals("", cell.getValue());
    }

    @Test
    public void testSetDefaultInteger() {
        column.editColumnType(ColumnType.INTEGER);
        column.setAllowBlank(false);
        cell.setDefault();
        assertEquals("0", cell.getValue());
    }

    @Test
    public void testSetDefaultBoolean() {
        column.editColumnType(ColumnType.BOOLEAN);
        column.setAllowBlank(false);
        cell.setDefault();
        assertEquals("false", cell.getValue());
    }

    @Test
    public void testSetDefaultEmail() {
        column.editColumnType(ColumnType.EMAIL);
        column.setAllowBlank(false);
        cell.setDefault();
        assertEquals("@", cell.getValue());
    }
}