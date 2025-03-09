package dataBase.dataBaseTest;

import dataBase.Row;
import dataBase.Column;
import dataBase.Cell;
import dataBase.columnType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class RowTest {
    private Row row;
    private Column integerColumn;
    private Column stringColumn;
    private Column booleanColumn;

    @BeforeEach
    public void setUp() {
        row = new Row();
        integerColumn = new Column(columnType.INTEGER, false);
        stringColumn = new Column(columnType.STRING, false);
        booleanColumn = new Column(columnType.BOOLEAN, false);
    }

    @Test
    public void testCreateCell() {
        row.createCell(integerColumn);
        assertEquals(1, row.getCells().size());
        assertEquals(integerColumn, row.getCells().get(0).getColumn());
    }

    @Test
    public void testCreateCells() {
        ArrayList<Column> columns = new ArrayList<>();
        columns.add(integerColumn);
        columns.add(stringColumn);
        columns.add(booleanColumn);
        row.createCells(columns);
        assertEquals(3, row.getCells().size());
        assertEquals(integerColumn, row.getCells().get(0).getColumn());
        assertEquals(stringColumn, row.getCells().get(1).getColumn());
        assertEquals(booleanColumn, row.getCells().get(2).getColumn());
    }

    @Test
    public void testDeleteCell() {
        row.createCell(integerColumn);
        row.createCell(stringColumn);
        row.deleteCell(0);
        assertEquals(1, row.getCells().size());
        assertEquals(stringColumn, row.getCells().get(0).getColumn());
    }

    @Test
    public void testGetCells() {
        row.createCell(integerColumn);
        row.createCell(stringColumn);
        row.createCell(booleanColumn);
        ArrayList<Cell> cells = row.getCells();
        assertEquals(3, cells.size());
        assertEquals(integerColumn, cells.get(0).getColumn());
        assertEquals(stringColumn, cells.get(1).getColumn());
        assertEquals(booleanColumn, cells.get(2).getColumn());
    }
}