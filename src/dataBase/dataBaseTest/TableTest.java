package dataBase.dataBaseTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import dataBase.Table;
import dataBase.Row;
import dataBase.columnType;

public class TableTest {

    private Table table;

    @BeforeEach
    public void setUp() {
        table = new Table();
    }

    @Test
    public void testCreateColumn() {
        table.createColumn(columnType.INTEGER, false);
        assertEquals(1, table.getColumns().size());
        assertEquals(columnType.INTEGER, table.getColumns().get(0).getType());
        assertFalse(table.getColumns().get(0).getAllowBlank());
    }

    @Test
    public void testCreateRow() {
        table.createColumn(columnType.STRING, true);
        table.createRow();
        assertEquals(1, table.getRows().size());
        assertEquals(1, table.getRows().get(0).getCells().size());
        assertEquals(columnType.STRING, table.getRows().get(0).getCells().get(0).getColumn().getType());
    }

    @Test
    public void testAddMultipleColumnsAndRows() {
        table.createColumn(columnType.INTEGER, false);
        table.createColumn(columnType.STRING, true);
        table.createRow();
        table.createRow();

        assertEquals(2, table.getColumns().size());
        assertEquals(2, table.getRows().size());

        for (Row row : table.getRows()) {
            assertEquals(2, row.getCells().size());
            assertEquals(columnType.INTEGER, row.getCells().get(0).getColumn().getType());
            assertEquals(columnType.STRING, row.getCells().get(1).getColumn().getType());
        }
    }

    @Test
    public void testGetColumns() {
        table.createColumn(columnType.BOOLEAN, false);
        assertEquals(1, table.getColumns().size());
        assertEquals(columnType.BOOLEAN, table.getColumns().get(0).getType());
    }

    @Test
    public void testGetRows() {
        table.createColumn(columnType.INTEGER, false);
        table.createRow();
        assertEquals(1, table.getRows().size());
        assertEquals(columnType.INTEGER, table.getRows().get(0).getCells().get(0).getColumn().getType());
    }
}