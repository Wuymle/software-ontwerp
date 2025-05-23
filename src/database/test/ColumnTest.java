package database.test;

import database.Column;
import database.ColumnType;
import database.Action;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

class ColumnTest {

    private Column column;
    private static Action ACTION_NONE_INSTANCE;

    // Helper method to invoke package-private methods on Column
    private Action invokeColumnMethod(String methodName, Object... args) throws Exception {
        Class<?>[] argTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof ColumnType) argTypes[i] = ColumnType.class;
            else if (args[i] instanceof Boolean) argTypes[i] = boolean.class;
            else argTypes[i] = args[i].getClass();
        }
        Method method = Column.class.getDeclaredMethod(methodName, argTypes);
        method.setAccessible(true);
        return (Action) method.invoke(column, args);
    }

    // Helper method to invoke package-private boolean method on Column
    private boolean invokeColumnBooleanMethod(String methodName, Object... args) throws Exception {
        Class<?>[] argTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            argTypes[i] = args[i].getClass();
        }
        Method method = Column.class.getDeclaredMethod(methodName, argTypes);
        method.setAccessible(true);
        return (boolean) method.invoke(column, args);
    }

    @BeforeEach
    void setUp() throws Exception {
        Constructor<Column> constructor = Column.class.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);
        column = constructor.newInstance("TestColumn");

        // Initialize ACTION_NONE_INSTANCE via reflection once
        if (ACTION_NONE_INSTANCE == null) {
            Field noneField = Action.class.getDeclaredField("NONE");
            noneField.setAccessible(true);
            ACTION_NONE_INSTANCE = (Action) noneField.get(null);
        }
    }

    @Test
    void testColumnCreation() {
        assertEquals("TestColumn", column.getName());
        assertEquals(ColumnType.STRING, column.getType());
        assertTrue(column.getAllowBlank());
        assertEquals("", column.getDefaultValue());
    }

    // --- Name Tests ---
    @Test
    void testUpdateName() throws Exception {
        Action updateAction = invokeColumnMethod("updateName", "NewName");
        updateAction.redo();
        assertEquals("NewName", column.getName());
    }

    @Test
    void testUpdateNameUndo() throws Exception {
        Action updateAction = invokeColumnMethod("updateName", "NewName");
        updateAction.redo();
        updateAction.undo();
        assertEquals("TestColumn", column.getName());
    }

    @Test
    void testUpdateNameToNull() {
        assertThrows(Exception.class, () -> invokeColumnMethod("updateName", (String)null));
    }

    @Test
    void testUpdateNameToEmpty() {
        assertThrows(Exception.class, () -> invokeColumnMethod("updateName", ""));
    }

    @Test
    void testUpdateNameToSameName() throws Exception {
        Action updateAction = invokeColumnMethod("updateName", "TestColumn");
        assertSame(ACTION_NONE_INSTANCE, updateAction);
        assertEquals("TestColumn", column.getName());
    }

    // --- Type Tests ---
    @Test
    void testUpdateType() throws Exception {
        Action updateAction = invokeColumnMethod("updateType", ColumnType.INTEGER);
        updateAction.redo();
        assertEquals(ColumnType.INTEGER, column.getType());
    }

    @Test
    void testUpdateTypeUndo() throws Exception {
        Action updateAction = invokeColumnMethod("updateType", ColumnType.INTEGER);
        updateAction.redo();
        updateAction.undo();
        assertEquals(ColumnType.STRING, column.getType());
    }

    @Test
    void testUpdateTypeToNull() {
        assertThrows(Exception.class, () -> invokeColumnMethod("updateType", (ColumnType)null));
    }

    // --- Default Value Tests ---
    @Test
    void testUpdateDefaultValue() throws Exception {
        Action updateAction = invokeColumnMethod("updateDefaultValue", "NewDefault");
        updateAction.redo();
        assertEquals("NewDefault", column.getDefaultValue());
    }

    @Test
    void testUpdateDefaultValueUndo() throws Exception {
        Action updateAction = invokeColumnMethod("updateDefaultValue", "NewDefault");
        updateAction.redo();
        updateAction.undo();
        assertEquals("", column.getDefaultValue());
    }

    @Test
    void testUpdateDefaultValueToNull() {
        assertThrows(Exception.class, () -> invokeColumnMethod("updateDefaultValue", (String)null));
    }

    // --- Allow Blank Tests ---
    @Test
    void testUpdateAllowBlank() throws Exception {
        Action updateAction = invokeColumnMethod("updateAllowBlank", false);
        updateAction.redo();
        assertFalse(column.getAllowBlank());
    }

    @Test
    void testUpdateAllowBlankUndo() throws Exception {
        Action updateAction = invokeColumnMethod("updateAllowBlank", false);
        updateAction.redo();
        updateAction.undo();
        assertTrue(column.getAllowBlank());
    }

    @Test
    void testUpdateAllowBlankToSame() throws Exception {
        Action updateAction = invokeColumnMethod("updateAllowBlank", true);
        assertSame(ACTION_NONE_INSTANCE, updateAction);
        assertTrue(column.getAllowBlank());
    }

    // --- Allow Cell Value Tests ---
    @Test
    void testAllowCellValueString() throws Exception {
        assertTrue(invokeColumnBooleanMethod("allowCellValue", "anyString"));
        assertTrue(invokeColumnBooleanMethod("allowCellValue", ""));
    }

    @Test
    void testAllowCellValueInteger() throws Exception {
        invokeColumnMethod("updateType", ColumnType.INTEGER).redo();
        assertTrue(invokeColumnBooleanMethod("allowCellValue", "123"));
        assertTrue(invokeColumnBooleanMethod("allowCellValue", "-5"));
        assertTrue(invokeColumnBooleanMethod("allowCellValue", "")); // Blank allowed by default
        assertFalse(invokeColumnBooleanMethod("allowCellValue", "abc"));
        assertFalse(invokeColumnBooleanMethod("allowCellValue", "1.2"));
    }

    @Test
    void testAllowCellValueBoolean() throws Exception {
        invokeColumnMethod("updateType", ColumnType.BOOLEAN).redo();
        assertTrue(invokeColumnBooleanMethod("allowCellValue", "true"));
        assertTrue(invokeColumnBooleanMethod("allowCellValue", "false"));
        assertTrue(invokeColumnBooleanMethod("allowCellValue", "TRUE"));
        assertTrue(invokeColumnBooleanMethod("allowCellValue", "")); // Blank allowed by default
        assertFalse(invokeColumnBooleanMethod("allowCellValue", "yes"));
    }

    @Test
    void testAllowCellValueEmail() throws Exception {
        invokeColumnMethod("updateType", ColumnType.EMAIL).redo();
        assertTrue(invokeColumnBooleanMethod("allowCellValue", "test@example.com"));
        assertTrue(invokeColumnBooleanMethod("allowCellValue", "")); // Blank allowed by default
        assertFalse(invokeColumnBooleanMethod("allowCellValue", "testexample.com"));
        assertFalse(invokeColumnBooleanMethod("allowCellValue", "test@examplecom"));
    }

    @Test
    void testAllowCellValueBlankNotAllowed() throws Exception {
        invokeColumnMethod("updateAllowBlank", false).redo();
        assertTrue(invokeColumnBooleanMethod("allowCellValue", "abc"));
        assertFalse(invokeColumnBooleanMethod("allowCellValue", ""));
    }

    @Test
    void testAllowCellValueNull() {
        assertThrows(Exception.class, () -> invokeColumnBooleanMethod("allowCellValue", (String)null));
    }
}
