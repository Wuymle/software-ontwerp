package database.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import database.Column;
import database.Row;

public class RowTest {
    private Row row;
    private Column column1;
    private Column column2;
    private ArrayList<Column> columns;

    @BeforeEach
    public void setUp() {
        row = new Row();
        column1 = new Column();
        column2 = new Column();
        columns = new ArrayList<>();
        columns.add(column1);
        columns.add(column2);
    }

    @Test
    public void testCreateCell() {
        row.createCell(column1);
        assertEquals(1, row.getCells().size());
        assertEquals(column1, row.getCells().get(0).getColumn());
    }

    @Test
    public void testCreateCells() {
        row.createCells(columns);
        assertEquals(2, row.getCells().size());
        assertEquals(column1, row.getCells().get(0).getColumn());
        assertEquals(column2, row.getCells().get(1).getColumn());
    }

    @Test
    public void testDeleteCell() {
        row.createCells(columns);
        row.deleteCell(0);
        assertEquals(1, row.getCells().size());
        assertEquals(column2, row.getCells().get(0).getColumn());
    }

    @Test
    public void testDeleteCellInvalidIndex() {
        row.createCells(columns);
        assertThrows(IndexOutOfBoundsException.class, () -> {
            row.deleteCell(2);
        });
    }
}