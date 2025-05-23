package database.test;

import database.Cell;
import database.Column;
import database.Action;
import database.ColumnType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

class CellTest {

    private Column column;
    private Cell cell;

    // Helper to create Column instance via reflection
    private Column createColumnInstance(String name) throws Exception {
        Constructor<Column> columnConstructor = Column.class.getDeclaredConstructor(String.class);
        columnConstructor.setAccessible(true);
        return columnConstructor.newInstance(name);
    }

    // Helper to invoke package-private updateDefaultValue on Column
    private Action invokeColumnUpdateDefaultValue(Column col, String defaultValue) throws Exception {
        Method method = Column.class.getDeclaredMethod("updateDefaultValue", String.class);
        method.setAccessible(true);
        return (Action) method.invoke(col, defaultValue);
    }

    // Helper to invoke package-private updateValue on Cell
    private Action invokeCellUpdateValue(Cell c, String value) throws Exception {
        Method method = Cell.class.getDeclaredMethod("updateValue", String.class);
        method.setAccessible(true);
        return (Action) method.invoke(c, value);
    }

    @BeforeEach
    void setUp() throws Exception {
        column = createColumnInstance("TestColumn");
        invokeColumnUpdateDefaultValue(column, "Default").redo();
        cell = new Cell(column); // Cell constructor is public
    }

    @Test
    void testCellInitialization() {
        assertEquals("Default", cell.getValue());
    }

    @Test
    void testCellInitializationEmptyDefault() throws Exception {
        Column col = createColumnInstance("Col2"); // Default is ""
        // No need to call updateDefaultValue if we want the default empty string
        Cell cell2 = new Cell(col);
        assertEquals("", cell2.getValue());
    }

    @Test
    void testUpdateValue_PackagePrivate_Direct() throws Exception {
        String newValue = "NewDirectValue";
        Action updateAction = invokeCellUpdateValue(cell, newValue);
        assertNotNull(updateAction);

        updateAction.redo();
        assertEquals(newValue, cell.getValue(), "Cell value should be updated after redo.");

        updateAction.undo();
        assertEquals("Default", cell.getValue(), "Cell value should revert after undo.");
    }

    @Test
    void testUpdateValue_PackagePrivate_NullArgument() throws Exception {
        try {
            invokeCellUpdateValue(cell, null);
            fail("Should throw IllegalArgumentException or similar via InvocationTargetException");
        } catch (InvocationTargetException e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException, "Expected cause to be IllegalArgumentException");
            assertEquals("value cannot be null", e.getCause().getMessage());
        }
    }

    @Test
    void testUpdateValueUndo() throws Exception {
        invokeCellUpdateValue(cell, "InitialValue").redo();
        Action updateAction = invokeCellUpdateValue(cell, "NewValue");
        updateAction.redo();
        updateAction.undo();
        assertEquals("InitialValue", cell.getValue());
    }

    @Test
    void testUpdateValueRedo() throws Exception {
        invokeCellUpdateValue(cell, "InitialValue").redo();
        Action updateAction = invokeCellUpdateValue(cell, "NewValue");
        updateAction.redo();
        updateAction.undo();
        updateAction.redo();
        assertEquals("NewValue", cell.getValue());
    }

    @Test
    void testUpdateValueToEmpty() throws Exception {
        invokeCellUpdateValue(cell, "NotEmpty").redo();
        Action updateAction = invokeCellUpdateValue(cell, "");
        updateAction.redo();
        assertEquals("", cell.getValue());
    }

    @Test
    void testUpdateValueFromEmpty() throws Exception {
        Column col = createColumnInstance("ColEmpty");
        Cell cellEmptyStart = new Cell(col);
        assertEquals("", cellEmptyStart.getValue());
        Action updateAction = invokeCellUpdateValue(cellEmptyStart, "NotEmpty");
        updateAction.redo();
        assertEquals("NotEmpty", cellEmptyStart.getValue());
    }

    @Test
    void testUpdateValueNull_OriginalTest() {
        // This test now uses the reflective helper
        assertThrows(InvocationTargetException.class, () -> invokeCellUpdateValue(cell, null),
                "Expected InvocationTargetException wrapping IllegalArgumentException");
        try {
            invokeCellUpdateValue(cell, null);
        } catch (InvocationTargetException e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
            assertEquals("value cannot be null", e.getCause().getMessage());
        } catch (Exception e) {
            fail("Unexpected exception type: " + e.getClass().getName());
        }
    }
}
