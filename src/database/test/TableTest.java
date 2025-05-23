package database.test;

import database.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

class TableTest {

    private History history;
    private Table table;
    private String initialTableName = "TestTable";

    // Helper to create Table instance via reflection for package-private constructor
    private Table createTableInstance(History hist, String name) throws Exception {
        Constructor<Table> tableConstructor = Table.class.getDeclaredConstructor(History.class, String.class);
        tableConstructor.setAccessible(true);
        return tableConstructor.newInstance(hist, name);
    }

    // Helper to create Column instance via reflection
    private Column createColumnInstance(String name) throws Exception {
        Constructor<Column> columnConstructor = Column.class.getDeclaredConstructor(String.class);
        columnConstructor.setAccessible(true);
        return columnConstructor.newInstance(name);
    }
 
    // Helper to invoke package-private updateName on Table
    private Action invokeTableUpdateName(Table t, String newName) throws Exception {
        Method method = Table.class.getDeclaredMethod("updateName", String.class);
        method.setAccessible(true);
        return (Action) method.invoke(t, newName);
    }

    // Helper to invoke package-private _deleteRow on Table
    private Action invokeTableDeleteRowInternal(Table t, Row r) throws Exception {
        Method method = Table.class.getDeclaredMethod("_deleteRow", Row.class);
        method.setAccessible(true);
        return (Action) method.invoke(t, r);
    }
    
    // Helper to get a column by name
    private Column getColumnByName(Table t, String name) {
        return t.getColumns().stream().filter(c -> c.getName().equals(name)).findFirst().orElse(null);
    }

    @BeforeEach
    void setUp() throws Exception {
        history = new History();
        table = createTableInstance(history, initialTableName);
    }

    @Test
    void testTableCreation() {
        assertEquals(initialTableName, table.getName());
        assertTrue(table.getColumns().isEmpty());
        assertTrue(table.getRows().isEmpty());
        assertNotNull(history);
    }

    @Test
    void testTableCreationNullName() {
        Exception exception = assertThrows(Exception.class, () -> createTableInstance(history, null));
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
    }

    @Test
    void testTableCreationEmptyName() {
        Exception exception = assertThrows(Exception.class, () -> createTableInstance(history, ""));
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
    }

    @Test
    void testCreateColumn() {
        AtomicBoolean designListenerCalled = new AtomicBoolean(false);
        AtomicBoolean rowsListenerCalled = new AtomicBoolean(false);
        table.addTableDesignChangeListener(t -> designListenerCalled.set(true));
        table.addTableRowsChangeListener(() -> rowsListenerCalled.set(true));

        table.createColumn();
        assertEquals(1, table.getColumns().size());
        Column createdColumn = table.getColumns().iterator().next();
        assertTrue(createdColumn.getName().startsWith("Column"));
        assertTrue(designListenerCalled.get());
        assertTrue(rowsListenerCalled.get()); // Rows listener also called due to cell creation

        // Test undo
        history.undo();
        assertTrue(table.getColumns().isEmpty());
    }

    @Test
    void testCreateMultipleColumnsEnsureUniqueNames() {
        table.createColumn(); // Column0
        table.createColumn(); // Column1
        assertEquals(2, table.getColumns().size());
        Set<String> names = new HashSet<>();
        for (Column col : table.getColumns()) {
            names.add(col.getName());
        }
        assertEquals(2, names.size());
    }

    @Test
    void testCreateRow() {
        AtomicBoolean rowsListenerCalled = new AtomicBoolean(false);
        table.addTableRowsChangeListener(() -> rowsListenerCalled.set(true));
        table.createColumn(); // Need at least one column for cells in a row
        rowsListenerCalled.set(false); // Reset after column creation

        table.createRow();
        assertEquals(1, table.getRows().size());
        Row createdRow = table.getRows().get(0);
        assertNotNull(createdRow);
        // Check if cell was created for the column
        Column col = table.getColumns().iterator().next();
        assertNotNull(createdRow.getCell(col));
        assertTrue(rowsListenerCalled.get());

        // Test undo
        history.undo();
        assertTrue(table.getRows().isEmpty());
    }

    @Test
    void testDeleteColumn() throws Exception {
        table.createColumn();
        Column columnToDelete = table.getColumns().iterator().next();
        table.createRow(); // Add a row to ensure cells are also handled

        AtomicBoolean designListenerCalled = new AtomicBoolean(false);
        table.addTableDesignChangeListener(t -> designListenerCalled.set(true));

        table.deleteColumn(columnToDelete);
        assertTrue(table.getColumns().isEmpty());
        // Check if cell was removed from the row
        if (!table.getRows().isEmpty()) {
            assertNull(table.getRows().get(0).getCell(columnToDelete));
        }
        assertTrue(designListenerCalled.get());

        // Test undo
        history.undo();
        assertEquals(1, table.getColumns().size());
        assertSame(columnToDelete, table.getColumns().iterator().next());
    }
    
    @Test
    void testDeleteColumnNull() {
        assertThrows(IllegalArgumentException.class, () -> table.deleteColumn(null));
    }

    @Test
    void testDeleteColumnNotExists() throws Exception {
        Column nonExistentCol = createColumnInstance("ghost");
        assertThrows(IllegalArgumentException.class, () -> table.deleteColumn(nonExistentCol));
    }

    @Test
    void testDeleteRow() {
        table.createColumn();
        table.createRow();
        Row rowToDelete = table.getRows().get(0);
        AtomicBoolean rowsListenerCalled = new AtomicBoolean(false);
        table.addTableRowsChangeListener(() -> rowsListenerCalled.set(true));

        table.deleteRow(rowToDelete);
        assertTrue(table.getRows().isEmpty());
        assertTrue(rowsListenerCalled.get());

        // Test undo
        history.undo();
        assertEquals(1, table.getRows().size());
        assertSame(rowToDelete, table.getRows().get(0));
    }

    @Test
    void testDeleteRowNull() {
         assertThrows(IllegalArgumentException.class, () -> table.deleteRow(null));
    }

    @Test
    void testDeleteRowNotExists() throws Exception {
        History tempHistory = new History();
        Constructor<Row> rowConstructor = Row.class.getDeclaredConstructor(History.class);
        rowConstructor.setAccessible(true);
        Row nonExistentRow = rowConstructor.newInstance(tempHistory);
        assertThrows(IllegalArgumentException.class, () -> table.deleteRow(nonExistentRow));
    }

    @Test
    void testDeleteRows() {
        table.createColumn();
        table.createRow(); // row1
        table.createRow(); // row2
        Set<Row> rowsToDelete = new HashSet<>(table.getRows());
        assertEquals(2, rowsToDelete.size());

        AtomicBoolean listenerCalled = new AtomicBoolean(false);
        table.addTableRowsChangeListener(() -> listenerCalled.set(true));

        table.deleteRows(rowsToDelete);
        assertTrue(table.getRows().isEmpty());
        assertTrue(listenerCalled.get());

        history.undo();
        assertEquals(2, table.getRows().size());
    }

    @Test
    void testUpdateColumnName() throws Exception {
        table.createColumn();
        Column columnToUpdate = getColumnByName(table, "Column0");
        String oldName = columnToUpdate.getName();
        String newName = "RenamedColumn";

        AtomicBoolean designListenerCalled = new AtomicBoolean(false);
        table.addTableDesignChangeListener(t -> designListenerCalled.set(true));

        table.updateColumnName(columnToUpdate, newName);
        assertEquals(newName, columnToUpdate.getName());
        assertTrue(designListenerCalled.get());

        history.undo();
        assertEquals(oldName, columnToUpdate.getName());
    }

    @Test
    void testUpdateColumnNameNullColumn() {
        assertThrows(IllegalArgumentException.class, () -> table.updateColumnName(null, "NewName"));
    }

    @Test
    void testUpdateColumnNameNullName() {
        table.createColumn();
        Column col = getColumnByName(table, "Column0");
        assertThrows(IllegalArgumentException.class, () -> table.updateColumnName(col, null));
    }

    @Test
    void testUpdateColumnNameToEmpty() {
        table.createColumn();
        Column col = getColumnByName(table, "Column0");
        assertThrows(IllegalArgumentException.class, () -> table.updateColumnName(col, ""));
    }

    @Test
    void testUpdateColumnNameToExisting() {
        table.createColumn(); // Column0
        table.createColumn(); // Column1
        Column col0 = getColumnByName(table, "Column0");
        Column col1 = getColumnByName(table, "Column1");
        assertThrows(IllegalArgumentException.class, () -> table.updateColumnName(col1, "Column0"));
    }
    
    @Test
    void testUpdateColumnType() throws Exception {
        table.createColumn();
        Column columnToUpdate = getColumnByName(table, "Column0");
        ColumnType oldType = columnToUpdate.getType();
        ColumnType newType = ColumnType.INTEGER;

        // Add a row with a compatible value for STRING, then change type
        table.createRow();
        Row row = table.getRows().get(0);
        row.updateCellValue(columnToUpdate, "123"); // Valid for INTEGER too

        AtomicBoolean designListenerCalled = new AtomicBoolean(false);
        table.addTableDesignChangeListener(t -> designListenerCalled.set(true));

        assertTrue(table.allowUpdateColumnType(columnToUpdate, newType), "Update to INTEGER should be allowed");
        table.updateColumnType(columnToUpdate, newType);
        assertEquals(newType, columnToUpdate.getType());
        assertTrue(designListenerCalled.get());

        history.undo();
        assertEquals(oldType, columnToUpdate.getType());
    }

    @Test
    void testUpdateColumnTypeInvalidData() {
        table.createColumn();
        Column col = getColumnByName(table, "Column0");
        table.createRow();
        Row row = table.getRows().get(0);
        row.updateCellValue(col, "not-an-int");
        assertFalse(table.allowUpdateColumnType(col, ColumnType.INTEGER));
        assertThrows(IllegalArgumentException.class, () -> table.updateColumnType(col, ColumnType.INTEGER));
    }

    @Test
    void testUpdateColumnDefaultValue() throws Exception {
        table.createColumn();
        Column columnToUpdate = getColumnByName(table, "Column0");
        String oldDefault = columnToUpdate.getDefaultValue();
        String newDefault = "NewDefaultVal";

        AtomicBoolean designListenerCalled = new AtomicBoolean(false);
        table.addTableDesignChangeListener(t -> designListenerCalled.set(true));

        table.updateColumnDefaultValue(columnToUpdate, newDefault);
        assertEquals(newDefault, columnToUpdate.getDefaultValue());
        assertTrue(designListenerCalled.get());

        history.undo();
        assertEquals(oldDefault, columnToUpdate.getDefaultValue());
    }

    @Test
    void testUpdateColumnAllowBlankWithExistingBlankValue() {
        table.createColumn();
        Column col = getColumnByName(table, "Column0");
        table.createRow();
        Row row = table.getRows().get(0);
        row.updateCellValue(col, ""); // Set cell to blank

        assertFalse(table.allowUpdateColumnAllowBlank(col, false));
        assertThrows(IllegalArgumentException.class, () -> table.updateColumnAllowBlank(col, false));
    }

    @Test
    void testInternalUpdateName() throws Exception {
        String newTableName = "NewInternalName";
        Action updateNameAction = invokeTableUpdateName(table, newTableName);
        AtomicBoolean designListenerCalled = new AtomicBoolean(false);
        AtomicBoolean rowsListenerCalled = new AtomicBoolean(false);
        table.addTableDesignChangeListener(t -> designListenerCalled.set(true));
        table.addTableRowsChangeListener(() -> rowsListenerCalled.set(true));
        
        updateNameAction.redo(); // Apply the change
        assertEquals(newTableName, table.getName());
        assertTrue(designListenerCalled.get());
        assertTrue(rowsListenerCalled.get());

        updateNameAction.undo();
        assertEquals(initialTableName, table.getName());
    }

    @Test
    void testInternalUpdateNameNullOrEmpty() {
        assertThrows(Exception.class, () -> invokeTableUpdateName(table, null));
        assertThrows(Exception.class, () -> invokeTableUpdateName(table, ""));
    }

    @Test
    void testInternal_deleteRow() throws Exception {
        table.createColumn();
        table.createRow();
        Row rowToDelete = table.getRows().get(0);
        Action deleteAction = invokeTableDeleteRowInternal(table, rowToDelete);

        deleteAction.redo();
        assertTrue(table.getRows().isEmpty());

        deleteAction.undo();
        assertEquals(1, table.getRows().size());
        assertSame(rowToDelete, table.getRows().get(0));
    }

    @Test
    void testInternal_deleteRowNullOrNotExists() throws Exception {
        assertThrows(Exception.class, () -> invokeTableDeleteRowInternal(table, null));
        History tempHistory = new History();
        Constructor<Row> rowConstructor = Row.class.getDeclaredConstructor(History.class);
        rowConstructor.setAccessible(true);
        Row nonExistentRow = rowConstructor.newInstance(tempHistory);
        assertThrows(Exception.class, () -> invokeTableDeleteRowInternal(table, nonExistentRow));
    }

    @Test
    void testAddRemoveDesignListener() {
        AtomicBoolean called = new AtomicBoolean(false);
        Table.TableDesignChangeListener listener = t -> called.set(true);
        table.addTableDesignChangeListener(listener);
        table.createColumn();
        assertTrue(called.get());
        called.set(false);
        table.removeTableDesignChangeListener(listener);
        table.createColumn();
        assertFalse(called.get());
    }

    @Test
    void testAddRemoveRowsListener() {
        AtomicBoolean called = new AtomicBoolean(false);
        Table.TableRowsChangeListener listener = () -> called.set(true);
        table.addTableRowsChangeListener(listener);
        table.createRow();
        assertTrue(called.get());
        called.set(false);
        table.removeTableRowsChangeListener(listener);
        table.createRow();
        assertFalse(called.get());
    }

    @Test
    void testNotifyTableDesignChanged() throws Exception {
        AtomicReference<Table> notifiedTable = new AtomicReference<>();
        Table.TableDesignChangeListener listener = t -> notifiedTable.set(t);
        table.addTableDesignChangeListener(listener);

        Method notifyMethod = Table.class.getDeclaredMethod("notifyTableDesignChanged");
        notifyMethod.setAccessible(true);
        notifyMethod.invoke(table);
        assertSame(table, notifiedTable.get());
    }

    @Test
    void testNotifyTableRowsChanged() throws Exception {
        AtomicBoolean notified = new AtomicBoolean(false);
        Table.TableRowsChangeListener listener = () -> notified.set(true);
        table.addTableRowsChangeListener(listener);

        Method notifyMethod = Table.class.getDeclaredMethod("notifyTableRowsChanged");
        notifyMethod.setAccessible(true);
        notifyMethod.invoke(table);
        assertTrue(notified.get());
    }
    
    @Test
    void testOnTableRowsChangedCallback() {
        // This tests the TableRowChangeListener interface method implemented by Table itself
        // It should propagate to its own listeners
        AtomicBoolean tableRowsListenerCalledOnTable = new AtomicBoolean(false);
        table.addTableRowsChangeListener(() -> tableRowsListenerCalledOnTable.set(true));

        // Simulate a row change that would call onTableRowsChanged on the Table instance
        // For example, if a Row object (that Table listens to) calls its listener.
        // We can directly call it here for testing the propagation.
        table.onTableRowsChanged(); 
        assertTrue(tableRowsListenerCalledOnTable.get());
    }
}
