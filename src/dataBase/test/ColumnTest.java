package database.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import database.Cell;
import database.Column;
import database.ColumnType;

public class ColumnTest {

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
    public void testGetType() {
        assertEquals(ColumnType.INTEGER, integerColumn.getType());
        assertEquals(ColumnType.STRING, stringColumn.getType());
        assertEquals(ColumnType.BOOLEAN, booleanColumn.getType());
    }

    @Test
    public void testGetAllowBlank() {
        assertFalse(integerColumn.getAllowBlank());
        assertTrue(blankAllowedColumn.getAllowBlank());
    }

    @Test
    public void testRegisterCell() {
        Cell cell = new Cell(integerColumn);
        integerColumn.registerCell(cell);
        assertEquals(1, integerColumn.getCells().size());
    }

    @Test
    public void testEditColumnType() {
        Cell cell = new Cell(integerColumn);
        integerColumn.registerCell(cell);
        integerColumn.editColumnType(ColumnType.STRING);
        assertEquals(ColumnType.STRING, integerColumn.getType());
        assertTrue(cell.isValid());
    }

    @Test
    public void testEditColumnTypeInvalid() {
        Cell cell = new Cell(integerColumn);
        integerColumn.registerCell(cell);
        cell.setValue("abc");
        integerColumn.editColumnType(ColumnType.INTEGER);
        assertFalse(cell.isValid());
    }
}