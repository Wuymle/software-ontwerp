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
    public void testGetAndSetDefaultValue() {
        Column column = new Column();
        column.setDefaultValue("test");
        assertEquals("test", column.getDefaultValue());

        // Test invalid default values
        assertThrows(Error.class, () -> {
            Column intColumn = new Column();
            intColumn.updateColumnType(ColumnType.INTEGER);
            intColumn.setDefaultValue("not-an-integer");
        });

        assertThrows(Error.class, () -> {
            Column boolColumn = new Column();
            boolColumn.updateColumnType(ColumnType.BOOLEAN);
            boolColumn.setDefaultValue("not-a-boolean");
        });

        assertThrows(Error.class, () -> {
            Column emailColumn = new Column();
            emailColumn.updateColumnType(ColumnType.EMAIL);
            emailColumn.setDefaultValue("not-an-email");
        });
    }

    @Test
    public void testUpdateColumnType() {
        Column column = new Column();
        column.updateColumnType(ColumnType.INTEGER);
        assertEquals(ColumnType.INTEGER, column.getType());

        column.updateColumnType(ColumnType.BOOLEAN);
        assertEquals(ColumnType.BOOLEAN, column.getType());

        column.updateColumnType(ColumnType.EMAIL);
        assertEquals(ColumnType.EMAIL, column.getType());

        column.updateColumnType(ColumnType.STRING);
        assertEquals(ColumnType.STRING, column.getType());
    }

    @Test
    public void testToggleColumnType() {
        Column column = new Column();
        assertEquals(ColumnType.STRING, column.getType());

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

        // Test registering invalid cells
        assertThrows(IllegalArgumentException.class, () -> {
            Column intColumn = new Column();
            intColumn.updateColumnType(ColumnType.INTEGER);
            Cell invalidCell = new Cell(intColumn);
            invalidCell.setValue("not-an-integer");
            intColumn.registerCell(invalidCell); // Should throw error
        });
    }

    @Test
    public void testDeleteCell() {
        Column column = new Column();
        Cell cell1 = new Cell(column);
        Cell cell2 = new Cell(column);
        column.registerCell(cell1);
        column.registerCell(cell2);

        assertEquals(2, column.getCells().size());

        column.deleteCell(0);
        assertEquals(1, column.getCells().size());
        assertEquals(cell2, column.getCells().get(0));
    }

    @Test
    public void testIsValidAllowBlankValue() {
        Column column = new Column();

        // True is always valid
        assertTrue(column.isValidAllowBlankValue(true));

        // False is only valid if default value is not blank and no cells are blank
        column.setDefaultValue("test");
        assertTrue(column.isValidAllowBlankValue(false));

        // False is invalid if default value is blank
        column.setDefaultValue("");
        assertFalse(column.isValidAllowBlankValue(false));
    }
}
