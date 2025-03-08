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

    @BeforeEach
    public void setUp() {
        integerColumn = new Column(columnType.INTEGER, false);
        stringColumn = new Column(columnType.STRING, true);
        booleanColumn = new Column(columnType.BOOLEAN, false);
    }

    @Test
    public void testSetValueInteger() {
        Cell cell = new Cell(integerColumn);
        assertTrue(cell.setValue("123"));
        assertEquals(123, cell.getValue());
    }

    @Test
    public void testSetValueString() {
        Cell cell = new Cell(stringColumn);
        assertTrue(cell.setValue("test"));
        assertEquals("test", cell.getValue());
    }

    @Test
    public void testSetValueBooleanTrue() {
        Cell cell = new Cell(booleanColumn);
        assertTrue(cell.setValue("true"));
        assertEquals(true, cell.getValue());
    }

    @Test
    public void testSetValueBooleanFalse() {
        Cell cell = new Cell(booleanColumn);
        assertTrue(cell.setValue("false"));
        assertEquals(false, cell.getValue());
    }

    @Test
    public void testSetValueInvalidInteger() {
        Cell cell = new Cell(integerColumn);
        assertThrows(Error.class, () -> cell.setValue("abc"));
    }

    @Test
    public void testSetValueInvalidBoolean() {
        Cell cell = new Cell(booleanColumn);
        assertThrows(Error.class, () -> cell.setValue("notABoolean"));
    }

    @Test
    public void testSetDefaultInteger() {
        Cell cell = new Cell(integerColumn);
        assertEquals(0, cell.getValue());
    }

    @Test
    public void testSetDefaultString() {
        Cell cell = new Cell(stringColumn);
        assertNull(cell.getValue());
    }

    @Test
    public void testSetDefaultBoolean() {
        Cell cell = new Cell(booleanColumn);
        assertEquals(false, cell.getValue());
    }
}