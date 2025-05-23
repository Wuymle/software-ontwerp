package database.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import database.Column;
import database.ColumnType;
import database.Row;
import database.Table;

class TableTest {

    private Table table;

    @BeforeEach
    void setUp() {
        table = new Table("TestTable");
    }

    @Test
    void testRowColumnCellConsistencyOnCreateAndDelete() {
        Table t = new Table("T");
        // Add multiple columns and rows
        t.createColumn();
        t.createColumn();
        t.createRow();
        t.createRow();

        // For every row, there should be a cell for every column
        for (Row r : t.getRows()) {
            for (Column c : t.getColumns()) {
                assertNotNull(r.getCell(c), "Cell should exist for every column in every row");
            }
        }

        // Remove a column and check cells are removed
        Column toRemove = t.getColumns().iterator().next();
        t.deleteColumn(toRemove);
        for (Row r : t.getRows()) {
            assertThrows(IllegalArgumentException.class, () -> r.deleteCell(toRemove));
            assertEquals(null, r.getCell(toRemove),
                    "Cell should be removed when column is deleted");
        }

        // Remove a row and check it's gone
        Row toRemoveRow = t.getRows().iterator().next();
        t.deleteRow(toRemoveRow);
        assertFalse(t.getRows().contains(toRemoveRow), "Row should be removed from table");
    }

    @Test
    void testCellConsistencyAfterMultipleColumnAndRowOperations() {
        Table t = new Table("T");
        t.createColumn();
        t.createColumn();
        t.createRow();
        t.createRow();

        // Remove all columns
        for (Column c : new HashSet<>(t.getColumns())) {
            t.deleteColumn(c);
        }
        for (Row r : t.getRows()) {
            assertTrue(t.getColumns().stream().allMatch(c -> r.getCell(c) == null),
                    "No cells should exist after all columns are deleted");
        }

        // Add columns again
        t.createColumn();
        t.createColumn();
        // Add a row
        t.createRow();
        // All rows should have cells for all columns
        for (Row r : t.getRows()) {
            for (Column c : t.getColumns()) {
                assertNotNull(r.getCell(c), "Cell should exist after re-adding columns and rows");
            }
        }
    }

    @Test
    void testNoCellLeakOrOrphanCells() {
        Table t = new Table("T");
        t.createColumn();
        t.createRow();
        Row r = t.getRows().iterator().next();
        t.getColumns().iterator().next();

        // Delete row, then add column, ensure no exception and no orphan cells
        t.deleteRow(r);
        try {
            t.createColumn();
        } catch (Exception ex) {
            fail("Should not throw when adding column after deleting all rows");
        }
        // Add row again, should have cells for all columns
        t.createRow();
        Row r2 = t.getRows().iterator().next();
        for (Column col : t.getColumns()) {
            assertNotNull(r2.getCell(col), "New row should have cells for all columns");
        }
    }

    @Test
    void testCellConsistencyAfterDeleteRows() {
        Table t = new Table("T");
        t.createColumn();
        t.createRow();
        t.createRow();
        Set<Row> allRows = new HashSet<>(t.getRows());
        t.deleteRows(allRows);
        assertTrue(t.getRows().isEmpty(), "All rows should be deleted");
        // Add a column after deleting all rows
        t.createColumn();
        // Add a row, should have cells for all columns
        t.createRow();
        Row r = t.getRows().iterator().next();
        for (Column c : t.getColumns()) {
            assertNotNull(r.getCell(c),
                    "Row should have cells for all columns after deleting and re-adding");
        }
    }

    @Test
    void constructorThrowsOnNullOrEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> new Table(null));
        assertThrows(IllegalArgumentException.class, () -> new Table(""));
    }

    @Test
    void getNameReturnsCorrectName() {
        assertEquals("TestTable", table.getName());
    }

    @Test
    void updateNameWorksAndThrowsOnInvalid() {
        table.updateName("NewName");
        assertEquals("NewName", table.getName());
        assertThrows(IllegalArgumentException.class, () -> table.updateName(null));
        assertThrows(IllegalArgumentException.class, () -> table.updateName(""));
    }

    @Test
    void createColumnAddsColumnAndCreatesCells() {
        Table t = new Table("T");
        t.createRow();
        assertEquals(1, t.getRows().size());
        t.createColumn();
        assertEquals(1, t.getColumns().size());
        Row r = t.getRows().iterator().next();
        Column c = t.getColumns().iterator().next();
        assertNotNull(r.getCell(c));
    }

    @Test
    void createRowAddsRowAndCreatesCells() {
        Table t = new Table("T");
        t.createColumn();
        assertEquals(1, t.getColumns().size());
        t.createRow();
        assertEquals(1, t.getRows().size());
        Row r = t.getRows().iterator().next();
        Column c = t.getColumns().iterator().next();
        assertNotNull(r.getCell(c));
    }

    @Test
    void deleteColumnRemovesColumnAndCells() {
        Table t = new Table("T");
        t.createColumn();
        t.createRow();
        Column c = t.getColumns().iterator().next();
        Row r = t.getRows().iterator().next();
        assertNotNull(r.getCell(c));
        t.deleteColumn(c);
        assertFalse(t.getColumns().contains(c));
        assertThrows(IllegalArgumentException.class, () -> t.deleteColumn(null));
        assertThrows(IllegalArgumentException.class,
                () -> t.deleteColumn(new Column("NonExistent")));
    }

    @Test
    void deleteRowRemovesRow() {
        Table t = new Table("T");
        t.createRow();
        Row r = t.getRows().iterator().next();
        t.deleteRow(r);
        assertFalse(t.getRows().contains(r));
        assertThrows(IllegalArgumentException.class, () -> t.deleteRow(null));
        assertThrows(IllegalArgumentException.class, () -> t.deleteRow(new Row()));
    }

    @Test
    void deleteRowsRemovesMultipleRows() {
        Table t = new Table("T");
        t.createRow();
        t.createRow();
        Set<Row> toDelete = new HashSet<>(t.getRows());
        t.deleteRows(toDelete);
        assertTrue(t.getRows().isEmpty());
    }

    @Test
    void allowUpdateColumnNameWorks() {
        Table t = new Table("T");
        t.createColumn();
        Column c = t.getColumns().iterator().next();
        assertTrue(t.allowUpdateColumnName(c, c.getName()));
        assertTrue(t.allowUpdateColumnName(c, "UniqueName"));
        t.createColumn();
        Column c2 = t.getColumns().stream().filter(col -> !col.equals(c)).findFirst().get();
        assertFalse(t.allowUpdateColumnName(c, c2.getName()));
        assertThrows(IllegalArgumentException.class, () -> t.allowUpdateColumnName(null, "X"));
        assertThrows(IllegalArgumentException.class, () -> t.allowUpdateColumnName(c, null));
    }

    @Test
    void updateColumnNameWorksAndThrows() {
        Table t = new Table("T");
        t.createColumn();
        Column c = t.getColumns().iterator().next();
        t.updateColumnName(c, "NewName");
        assertEquals("NewName", c.getName());
        assertThrows(IllegalArgumentException.class, () -> t.updateColumnName(null, "X"));
        t.createColumn();
        Column c2 = t.getColumns().stream().filter(col -> !col.equals(c)).findFirst().get();
        assertThrows(IllegalArgumentException.class, () -> t.updateColumnName(c, c2.getName()));
    }

    @Test
    void allowUpdateColumnTypeWorks() {
        Table t = new Table("T");
        t.createColumn();
        Column c = t.getColumns().iterator().next();
        ColumnType type = ColumnType.INTEGER;
        assertTrue(t.allowUpdateColumnType(c, type));
        assertThrows(IllegalArgumentException.class, () -> t.allowUpdateColumnType(null, type));
        assertThrows(IllegalArgumentException.class, () -> t.allowUpdateColumnType(c, null));
    }

    @Test
    void updateColumnTypeWorksAndThrows() {
        Table t = new Table("T");
        t.createColumn();
        Column c = t.getColumns().iterator().next();
        ColumnType type = ColumnType.INTEGER;
        t.updateColumnType(c, type);
        assertThrows(IllegalArgumentException.class, () -> t.updateColumnType(null, type));
        assertThrows(IllegalArgumentException.class, () -> t.updateColumnType(c, null));
    }

    @Test
    void allowUpdateColumnDefaultValueWorks() {
        Table t = new Table("T");
        t.createColumn();
        Column c = t.getColumns().iterator().next();
        // Assume allowCellValue returns true for any value
        assertTrue(t.allowUpdateColumnDefaultValue(c, "default"));
        assertThrows(IllegalArgumentException.class,
                () -> t.allowUpdateColumnDefaultValue(null, "x"));
        assertThrows(IllegalArgumentException.class,
                () -> t.allowUpdateColumnDefaultValue(c, null));
    }

    @Test
    void updateColumnDefaultValueWorksAndThrows() {
        Table t = new Table("T");
        t.createColumn();
        Column c = t.getColumns().iterator().next();
        t.updateColumnDefaultValue(c, "default");
        assertThrows(IllegalArgumentException.class, () -> t.updateColumnDefaultValue(null, "x"));
        assertThrows(IllegalArgumentException.class, () -> t.updateColumnDefaultValue(c, null));
    }

    @Test
    void allowUpdateColumnAllowBlankWorks() {
        Table t = new Table("T");
        t.createColumn();
        Column c = t.getColumns().iterator().next();
        t.updateColumnDefaultValue(c, "");
        t.createRow();
        Row r = t.getRows().iterator().next();
        r.getCell(c).updateValue("");
        assertTrue(t.allowUpdateColumnAllowBlank(c, true));
        assertThrows(IllegalArgumentException.class,
                () -> t.allowUpdateColumnAllowBlank(null, true));
    }

    @Test
    void updateColumnAllowBlankWorksAndThrows() {
        Table t = new Table("T");
        t.createColumn();
        Column c = t.getColumns().iterator().next();
        t.updateColumnDefaultValue(c, "");
        t.createRow();
        Row r = t.getRows().iterator().next();
        r.getCell(c).updateValue("");
        t.updateColumnAllowBlank(c, true);
        assertThrows(IllegalArgumentException.class, () -> t.updateColumnAllowBlank(null, true));
    }
}
