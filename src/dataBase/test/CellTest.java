package database.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import database.Cell;
import database.Column;
import database.ColumnType;

public class CellTest {

    private Column integerColumn;
    private Column stringColumn;
    private Column booleanColumn;
    private Column blankAllowedColumn;

    @BeforeEach
    public void setUp() {
        integerColumn = new Column(ColumnType.INTEGER, false);
        stringColumn = new Column(ColumnType.STRING, false);
        booleanColumn = new Column(ColumnType.BOOLEAN, false);
        blankAllowedColumn = new Column(ColumnType.STRING, true);
    }

    @Test
    public void testSetValueInteger() {
        Cell cell = new Cell(integerColumn);
        cell.setValue("123");
        assertEquals("123", cell.getValue());
        assertTrue(cell.isValid());
    }

    @Test
    public void testSetValueInvalidInteger() {
        Cell cell = new Cell(integerColumn);
        cell.setValue("abc");
        assertFalse(cell.isValid());
    }

    @Test
    public void testSetValueString() {
        Cell cell = new Cell(stringColumn);
        cell.setValue("hello");
        assertEquals("hello", cell.getValue());
        assertTrue(cell.isValid());
    }

    @Test
    public void testSetValueBooleanTrue() {
        Cell cell = new Cell(booleanColumn);
        cell.setValue("true");
        assertEquals("true", cell.getValue());
        assertTrue(cell.isValid());
    }

    @Test
    public void testSetValueBooleanFalse() {
        Cell cell = new Cell(booleanColumn);
        cell.setValue("false");
        assertEquals("false", cell.getValue());
        assertTrue(cell.isValid());
    }

    @Test
    public void testSetValueInvalidBoolean() {
        Cell cell = new Cell(booleanColumn);
        cell.setValue("notABoolean");
        assertFalse(cell.isValid());
    }

    @Test
    public void testSetDefaultWithBlankAllowed() {
        Cell cell = new Cell(blankAllowedColumn);
        assertNull(cell.getValue());
    }

    @Test
    public void testSetDefaultWithoutBlankAllowed() {
        Cell cell = new Cell(integerColumn);
        assertEquals("0", cell.getValue());
    }

    @Test
    public void testSetDefaultStringWithoutBlankAllowed() {
        Cell cell = new Cell(stringColumn);
        assertEquals("", cell.getValue());
    }

    @Test
    public void testSetDefaultBooleanWithoutBlankAllowed() {
        Cell cell = new Cell(booleanColumn);
        assertEquals("false", cell.getValue());
    }
}