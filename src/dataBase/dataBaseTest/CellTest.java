package dataBase.dataBaseTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import dataBase.Cell;
import dataBase.Column;
import dataBase.columnType;

public class CellTest {

    private Column integerColumn;
    private Column stringColumn;
    private Column booleanColumn;
    private Column blankAllowedColumn;

    @BeforeEach
    public void setUp() {
        integerColumn = new Column(columnType.INTEGER, false);
        stringColumn = new Column(columnType.STRING, false);
        booleanColumn = new Column(columnType.BOOLEAN, false);
        blankAllowedColumn = new Column(columnType.STRING, true);
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

    @Test
    public void testSetValueEmail() {
        Column emailColumn = new Column(columnType.EMAIL, false);
        Cell cell = new Cell(emailColumn);
        cell.setValue("test@example.com");
        assertEquals("test@example.com", cell.getValue());
        assertTrue(cell.isValid());
    }

    @Test
    public void testSetValueInvalidEmail() {
        Column emailColumn = new Column(columnType.EMAIL, false);
        Cell cell = new Cell(emailColumn);
        cell.setValue("invalidEmail");
        assertFalse(cell.isValid());
    }

    @Test
    public void testSetDefaultEmailWithoutBlankAllowed() {
        Column emailColumn = new Column(columnType.EMAIL, false);
        Cell cell = new Cell(emailColumn);
        assertEquals("@", cell.getValue());
    }

    @Test
    public void testSetDefaultEmailWithBlankAllowed() {
        Column emailColumn = new Column(columnType.EMAIL, true);
        Cell cell = new Cell(emailColumn);
        assertNull(cell.getValue());
    }
}