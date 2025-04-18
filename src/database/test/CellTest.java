
package database.test;

import database.Cell;
import database.Column;
import database.ColumnType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    @Test
    void testSetValueValid() {
        Column column = new Column();
        column.updateColumnType(ColumnType.STRING);
        Cell cell = new Cell(column);

        assertDoesNotThrow(() -> cell.setValue("Valid String"));
        assertEquals("Valid String", cell.getValue());
    }

    @Test
    void testSetValueValidInt() {
        Column column = new Column();
        column.updateColumnType(ColumnType.INTEGER);
        Cell cell = new Cell(column);

        assertDoesNotThrow(() -> cell.setValue("123"));
        assertEquals("123", cell.getValue());
    }

    @Test
    void testSetValueValidBool() {
        Column column = new Column();
        column.updateColumnType(ColumnType.BOOLEAN);
        Cell cell = new Cell(column);

        assertDoesNotThrow(() -> cell.setValue("false"));
        assertEquals("false", cell.getValue());
    }

    @Test
    void testSetValueValidEmail() {
        Column column = new Column();
        column.updateColumnType(ColumnType.EMAIL);
        Cell cell = new Cell(column);

        assertDoesNotThrow(() -> cell.setValue("123@kak"));
        assertEquals("123@kak", cell.getValue());
    }

    @Test
    void testSetValueInvalid() {
        Column column = new Column();
        column.updateColumnType(ColumnType.INTEGER);
        Cell cell = new Cell(column);

        assertThrows(IllegalArgumentException.class, () -> cell.setValue("Invalid Integer"));
    }

    @Test
    void testSetValueInvalidBool() {
        Column column = new Column();
        column.updateColumnType(ColumnType.BOOLEAN);
        Cell cell = new Cell(column);

        assertThrows(IllegalArgumentException.class, () -> cell.setValue("Invalid Integer"));
    }

    @Test
    void testSetValueInvalidEmail() {
        Column column = new Column();
        column.updateColumnType(ColumnType.EMAIL);
        Cell cell = new Cell(column);

        assertThrows(IllegalArgumentException.class, () -> cell.setValue("Invalid Integer"));
    }

    @Test
    void testSetDefaultValue() {
        Column column = new Column();
        column.updateColumnType(ColumnType.BOOLEAN);
        column.setDefaultValue("TRUE");
        Cell cell = new Cell(column);

        assertEquals("TRUE", cell.getValue());
    }

    @Test
    void testGetColumn() {
        Column column = new Column();
        Cell cell = new Cell(column);

        assertEquals(column, cell.getColumn());
    }

    @Test
    void testSetValueWithAllowBlank() {
        Column column = new Column();
        column.setAllowBlank(true);
        Cell cell = new Cell(column);

        assertDoesNotThrow(() -> cell.setValue(""));
        assertEquals("", cell.getValue());
    }

    @Test
    void testSetValueWithoutAllowBlank() {
        Column column = new Column();
        column.setDefaultValue("Default Value");
        column.setAllowBlank(false);
        column.updateColumnType(ColumnType.STRING);
        Cell cell = new Cell(column);

        assertThrows(IllegalArgumentException.class, () -> cell.setValue(""));
    }

    @Test
    void testSetDefaultString() {
        Column column = new Column();
        column.updateColumnType(ColumnType.STRING);
        column.setDefaultValue("abc");
        Cell cell = new Cell(column);
        assertEquals("abc", cell.getValue());
    }

    @Test
    void testSetDefaultInteger() {
        Column column = new Column();
        column.updateColumnType(ColumnType.INTEGER);
        column.setDefaultValue("123");
        Cell cell = new Cell(column);
        assertEquals("123", cell.getValue());
    }

    @Test
    void testSetDefaultBoolean() {
        Column column = new Column();
        column.updateColumnType(ColumnType.BOOLEAN);
        column.setDefaultValue("false");
        Cell cell = new Cell(column);
        assertEquals("false", cell.getValue());
    }

    @Test
    void testSetDefaultEmail() {
        Column column = new Column();
        column.updateColumnType(ColumnType.EMAIL);
        column.setDefaultValue("test@example.com");
        Cell cell = new Cell(column);
        assertEquals("test@example.com", cell.getValue());
    }

    
}
