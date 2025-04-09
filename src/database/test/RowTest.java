package database.test;

import database.Row;
import database.Column;
import database.Cell;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class RowTest {

    @Test
    public void testCreateCell() {
        Row row = new Row();
        Column column = new Column();
        row.createCell(column);

        assertEquals(1, row.getCells().size());
        assertEquals(column, row.getCells().get(0).getColumn());
    }

    @Test
    public void testCreateCells() {
        Row row = new Row();
        ArrayList<Column> columns = new ArrayList<>();
        columns.add(new Column());
        columns.add(new Column());

        row.createCells(columns);

        assertEquals(2, row.getCells().size());
        assertEquals(columns.get(0), row.getCells().get(0).getColumn());
        assertEquals(columns.get(1), row.getCells().get(1).getColumn());
    }

    @Test
    public void testDeleteCell() {
        Row row = new Row();
        Column column1 = new Column();
        Column column2 = new Column();
        row.createCell(column1);
        row.createCell(column2);

        row.deleteCell(0);

        assertEquals(1, row.getCells().size());
        assertEquals(column2, row.getCells().get(0).getColumn());
    }

    @Test
    public void testGetCells() {
        Row row = new Row();
        Column column = new Column();
        row.createCell(column);

        ArrayList<Cell> cells = row.getCells();

        assertEquals(1, cells.size());
        assertEquals(column, cells.get(0).getColumn());
    }
}
