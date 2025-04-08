
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
        column.editColumnType(ColumnType.STRING);
        Cell cell = new Cell(column);

        assertDoesNotThrow(() -> cell.setValue("Valid String"));
        assertEquals("Valid String", cell.getValue());
    }

    @Test
    void testSetValueValidInt() {
        Column column = new Column();
        column.editColumnType(ColumnType.INTEGER);
        Cell cell = new Cell(column);

        assertDoesNotThrow(() -> cell.setValue("123"));
        assertEquals("123", cell.getValue());
    }

    @Test
    void testSetValueValidBool() {
        Column column = new Column();
        column.editColumnType(ColumnType.BOOLEAN);
        Cell cell = new Cell(column);

        assertDoesNotThrow(() -> cell.setValue("false"));
        assertEquals("false", cell.getValue());
    }

    @Test
    void testSetValueValidEmail() {
        Column column = new Column();
        column.editColumnType(ColumnType.EMAIL);
        Cell cell = new Cell(column);

        assertDoesNotThrow(() -> cell.setValue("123@kak"));
        assertEquals("123@kak", cell.getValue());
    }

    @Test
    void testSetValueInvalid() {
        Column column = new Column();
        column.editColumnType(ColumnType.INTEGER);
        Cell cell = new Cell(column);

        assertThrows(IllegalArgumentException.class, () -> cell.setValue("Invalid Integer"));
    }

    @Test
    void testSetValueInvalidBool() {
        Column column = new Column();
        column.editColumnType(ColumnType.BOOLEAN);
        Cell cell = new Cell(column);

        assertThrows(IllegalArgumentException.class, () -> cell.setValue("Invalid Integer"));
    }

    @Test
    void testSetValueInvalidEmail() {
        Column column = new Column();
        column.editColumnType(ColumnType.EMAIL);
        Cell cell = new Cell(column);

        assertThrows(IllegalArgumentException.class, () -> cell.setValue("Invalid Integer"));
    }

    @Test
    void testSetDefaultValue() {
        Column column = new Column();
        column.editColumnType(ColumnType.BOOLEAN);
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
        column.setAllowBlank(false);
        column.editColumnType(ColumnType.STRING);
        Cell cell = new Cell(column);

        assertThrows(IllegalArgumentException.class, () -> cell.setValue(""));
    }
}
