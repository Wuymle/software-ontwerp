package database.test;

import database.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

class RowTest {

    private History history;
    private Row row;
    private Column column1;
    private Column column2;

    // Helper to create Column instance via reflection
    private Column createColumnInstance(String name) throws Exception {
        Constructor<Column> columnConstructor = Column.class.getDeclaredConstructor(String.class);
        columnConstructor.setAccessible(true);
        return columnConstructor.newInstance(name);
    }

    // Helper to invoke package-private Row(History) constructor
    private Row createRowInstance(History hist) throws Exception {
        Constructor<Row> rowConstructor = Row.class.getDeclaredConstructor(History.class);
        rowConstructor.setAccessible(true);
        return rowConstructor.newInstance(hist);
    }

    // Helper to invoke package-private createCell on Row
    private Action invokeRowCreateCell(Row r, Column col) throws Exception {
        Method method = Row.class.getDeclaredMethod("createCell", Column.class);
        method.setAccessible(true);
        return (Action) method.invoke(r, col);
    }

    // Helper to invoke package-private deleteCell on Row
    private Action invokeRowDeleteCell(Row r, Column col) throws Exception {
        Method method = Row.class.getDeclaredMethod("deleteCell", Column.class);
        method.setAccessible(true);
        return (Action) method.invoke(r, col);
    }

    @BeforeEach
    void setUp() throws Exception {
        history = new History();
        row = createRowInstance(history);
        column1 = createColumnInstance("Col1");
        column2 = createColumnInstance("Col2");

        // Add cells to the row for testing getCell and updateCellValue
        invokeRowCreateCell(row, column1).redo();
        invokeRowCreateCell(row, column2).redo();
    }

    @Test
    void testRowCreation() {
        assertNotNull(row);
    }

    @Test
    void testCreateCell() throws Exception {
        Column newColumn = createColumnInstance("NewCol");
        Action createAction = invokeRowCreateCell(row, newColumn);
        assertNotNull(createAction);

        createAction.redo();
        assertNotNull(row.getCell(newColumn));
        assertEquals(newColumn.getDefaultValue(), row.getCell(newColumn).getValue());

        createAction.undo();
        assertNull(row.getCell(newColumn));
    }

    @Test
    void testCreateCellNullColumn() {
        Exception exception = assertThrows(Exception.class, () -> {
            invokeRowCreateCell(row, null);
        });
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        assertEquals("column cannot be null", exception.getCause().getMessage());
    }

    @Test
    void testCreateCellAlreadyExists() throws Exception {
        Exception exception = assertThrows(Exception.class, () -> {
            invokeRowCreateCell(row, column1).redo(); // column1 already added in setup
        });
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        assertEquals("Cell already exists", exception.getCause().getMessage());
    }

    @Test
    void testDeleteCell() throws Exception {
        assertNotNull(row.getCell(column1));
        Action deleteAction = invokeRowDeleteCell(row, column1);
        assertNotNull(deleteAction);

        deleteAction.redo();
        assertNull(row.getCell(column1));

        deleteAction.undo();
        assertNotNull(row.getCell(column1));
    }

    @Test
    void testDeleteCellNullColumn() {
        Exception exception = assertThrows(Exception.class, () -> {
            invokeRowDeleteCell(row, null);
        });
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        assertEquals("column does not exist", exception.getCause().getMessage()); // Or specific message
    }

    @Test
    void testDeleteCellColumnNotExists() throws Exception {
        Column nonExistentColumn = createColumnInstance("NonExistent");
        Exception exception = assertThrows(Exception.class, () -> {
            invokeRowDeleteCell(row, nonExistentColumn);
        });
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        assertEquals("column does not exist", exception.getCause().getMessage());
    }

    @Test
    void testGetCell() {
        assertNotNull(row.getCell(column1));
        assertNotNull(row.getCell(column2));
    }

    @Test
    void testGetCellNullColumn() {
        assertThrows(IllegalArgumentException.class, () -> row.getCell(null));
    }

    @Test
    void testGetCellNonExistent() throws Exception {
        Column nonExistentColumn = createColumnInstance("NonExistent");
        assertNull(row.getCell(nonExistentColumn));
    }

    @Test
    void testUpdateCellValue() {
        AtomicBoolean listenerCalled = new AtomicBoolean(false);
        row.addTableRowChangeListener(() -> listenerCalled.set(true));

        String newValue = "NewValue";
        row.updateCellValue(column1, newValue);
        assertEquals(newValue, row.getCell(column1).getValue());
        assertTrue(listenerCalled.get());

        // Test undo
        history.undo();
        assertEquals(column1.getDefaultValue(), row.getCell(column1).getValue());
    }

    @Test
    void testUpdateCellValueNullColumn() {
        assertThrows(IllegalArgumentException.class, () -> row.updateCellValue(null, "anyValue"));
    }

    @Test
    void testAllowUpdateCellValueNullColumn() {
        assertThrows(IllegalArgumentException.class, () -> row.allowUpdateCellValue(null, "value"));
    }

    @Test
    void testAllowUpdateCellValueColumnNotExists() throws Exception {
        Column nonExistentColumn = createColumnInstance("NonExistent");
        assertThrows(IllegalArgumentException.class, () -> row.allowUpdateCellValue(nonExistentColumn, "value"));
    }

    @Test
    void testAddRemoveTableRowChangeListener() {
        AtomicBoolean listenerCalled = new AtomicBoolean(false);
        Row.TableRowChangeListener listener = () -> listenerCalled.set(true);

        row.addTableRowChangeListener(listener);
        row.updateCellValue(column1, "test");
        assertTrue(listenerCalled.get());

        listenerCalled.set(false);
        row.removeTableRowChangeListener(listener);
        row.updateCellValue(column1, "anotherTest");
        assertFalse(listenerCalled.get());
    }

    @Test
    void testAddNullListener() {
        assertThrows(IllegalArgumentException.class, () -> row.addTableRowChangeListener(null));
    }

    @Test
    void testRemoveNullListener() {
        assertThrows(IllegalArgumentException.class, () -> row.removeTableRowChangeListener(null));
    }
}
