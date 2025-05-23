package database.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import database.Column;
import database.Row;
import static org.junit.jupiter.api.Assertions.*;



class RowTest {

    private Row row;
    private Column column;
    private Column otherColumn;

    @Test
    void testCreateCellWithOtherColumn() {
        row.createCell(otherColumn);
        assertNotNull(row.getCell(otherColumn));
        assertNull(row.getCell(column));
    }

    @Test
    void testDeleteCellWithOtherColumn() {
        row.createCell(otherColumn);
        row.deleteCell(otherColumn);
        assertNull(row.getCell(otherColumn));
    }

    @Test
    void testCreateCellBothColumns() {
        row.createCell(column);
        row.createCell(otherColumn);
        assertNotNull(row.getCell(column));
        assertNotNull(row.getCell(otherColumn));
    }

    @Test
    void testDeleteCellDoesNotAffectOtherColumn() {
        row.createCell(column);
        row.createCell(otherColumn);
        row.deleteCell(column);
        assertNull(row.getCell(column));
        assertNotNull(row.getCell(otherColumn));
    }

    @Test
    void testUpdateCellValueWithOtherColumn() {
        Row testRow = new Row() {
            @Override
            public boolean allowUpdateCellValue(Column c, String v) {
                return true;
            }
        };
        testRow.createCell(otherColumn);
        testRow.updateCellValue(otherColumn, "otherValue");
        assertEquals("otherValue", testRow.getCell(otherColumn).getValue());
    }

    @Test
    void testAllowUpdateCellValueWithOtherColumn() {
        assertTrue(row.allowUpdateCellValue(otherColumn, "someValue"));
    }

    @Test
    void testDeleteCellNonExistingOtherColumnThrows() {
        assertThrows(IllegalArgumentException.class, () -> row.deleteCell(otherColumn));
    }

    @Test
    void testGetCellReturnsNullForOtherColumnIfNotCreated() {
        assertNull(row.getCell(otherColumn));
    }

    @BeforeEach
    void setUp() {
        row = new Row();
        column = new Column("col1");
        otherColumn = new Column("col2");
    }

    @Test
    void testCreateCellAndGetCell() {
        row.createCell(column);
        assertNotNull(row.getCell(column));
    }

    @Test
    void testCreateCellWithNullColumnThrows() {
        assertThrows(IllegalArgumentException.class, () -> row.createCell(null));
    }

    @Test
    void testCreateCellAlreadyExistsThrows() {
        row.createCell(column);
        assertThrows(IllegalArgumentException.class, () -> row.createCell(column));
    }

    @Test
    void testGetCellWithNullColumnThrows() {
        assertThrows(IllegalArgumentException.class, () -> row.getCell(null));
    }

    @Test
    void testDeleteCellRemovesCell() {
        row.createCell(column);
        row.deleteCell(column);
        assertNull(row.getCell(column));
    }

    @Test
    void testDeleteCellWithNullColumnThrows() {
        assertThrows(IllegalArgumentException.class, () -> row.deleteCell(null));
    }

    @Test
    void testDeleteCellNonExistingThrows() {
        assertThrows(IllegalArgumentException.class, () -> row.deleteCell(column));
    }

    @Test
    void testAllowUpdateCellValueWithNullColumnThrows() {
        assertThrows(IllegalArgumentException.class, () -> row.allowUpdateCellValue(null, "value"));
    }

    @Test
    void testAllowUpdateCellValueWithExistingCellThrows() {
        row.createCell(column);
        assertThrows(IllegalArgumentException.class,
                () -> row.allowUpdateCellValue(column, "value"));
    }

    @Test
    void testAllowUpdateCellValueDelegatesToColumn() {
        assertTrue(row.allowUpdateCellValue(column, "valid"));
        // You may want to mock Column.allowCellValue for more advanced tests
    }

    @Test
    void testUpdateCellValueWithNullColumnThrows() {
        assertThrows(IllegalArgumentException.class, () -> row.updateCellValue(null, "value"));
    }

    @Test
    void testUpdateCellValueWithInvalidValueThrows() {
        row.createCell(column);
        // Override allowUpdateCellValue to return false
        Row testRow = new Row() {
            @Override
            public boolean allowUpdateCellValue(Column c, String v) {
                return false;
            }
        };
        testRow.createCell(column);
        assertThrows(IllegalArgumentException.class,
                () -> testRow.updateCellValue(column, "invalid"));
    }

    @Test
    void testUpdateCellValueUpdatesCell() {
        row.createCell(column);
        // Assume allowUpdateCellValue returns true
        Row testRow = new Row() {
            @Override
            public boolean allowUpdateCellValue(Column c, String v) {
                return true;
            }
        };
        testRow.createCell(column);
        testRow.updateCellValue(column, "newValue");
        assertEquals("newValue", testRow.getCell(column).getValue());
    }
}
