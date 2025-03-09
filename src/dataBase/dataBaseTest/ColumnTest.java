package dataBase.dataBaseTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import dataBase.Column;
import dataBase.Cell;
import dataBase.columnType;

public class ColumnTest {

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
    public void testGetType() {
        assertEquals(columnType.INTEGER, integerColumn.getType());
        assertEquals(columnType.STRING, stringColumn.getType());
        assertEquals(columnType.BOOLEAN, booleanColumn.getType());
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
        integerColumn.editColumnType(columnType.STRING);
        assertEquals(columnType.STRING, integerColumn.getType());
        assertTrue(cell.isValid());
    }

    @Test
    public void testEditColumnTypeInvalid() {
        Cell cell = new Cell(integerColumn);
        integerColumn.registerCell(cell);
        cell.setValue("abc");
        integerColumn.editColumnType(columnType.INTEGER);
        assertFalse(cell.isValid());
    }
}