package newdatabase;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



class ColumnTest {
    // Helper to create a Column with test Action/ColumnType
    static class TestColumn extends Column {
        TestColumn(String name) {
            super(name);
        }
    }

    @Test
    void testGetName() {
        Column col = new TestColumn("foo");
        assertEquals("foo", col.getName());
    }

    @Test
    void testUpdateNameValid() {
        Column col = new TestColumn("foo");
        Action action = col.updateName("bar");
        assertNotNull(action);
        assertNotSame(Action.NONE, action);
        action.redo();
        assertEquals("bar", col.getName());
        action.undo();
        assertEquals("foo", col.getName());
    }

    @Test
    void testUpdateNameSame() {
        Column col = new TestColumn("foo");
        Action action = col.updateName("foo");
        assertSame(Action.NONE, action);
    }

    @Test
    void testUpdateNameNullOrEmpty() {
        Column col = new TestColumn("foo");
        assertThrows(IllegalArgumentException.class, () -> col.updateName(null));
        assertThrows(IllegalArgumentException.class, () -> col.updateName(""));
    }

    @Test
    void testGetTypeDefault() {
        Column col = new TestColumn("foo");
        assertTrue(ColumnType.STRING.equals(col.getType()));
    }

    @Test
    void testUpdateTypeValid() {
        Column col = new TestColumn("foo");
        Action action = col.updateType(ColumnType.INTEGER);
        assertNotNull(action);
        action.redo();
        assertEquals(ColumnType.INTEGER, col.getType());
        action.undo();
        assertEquals(ColumnType.STRING, col.getType());
    }

    @Test
    void testUpdateTypeNull() {
        Column col = new TestColumn("foo");
        assertThrows(IllegalArgumentException.class, () -> col.updateType(null));
    }

    @Test
    void testGetDefaultValue() {
        Column col = new TestColumn("foo");
        assertEquals("", col.getDefaultValue());
    }

    @Test
    void testUpdateDefaultValueValid() {
        Column col = new TestColumn("foo");
        Action action = col.updateDefaultValue("abc");
        assertNotNull(action);
        action.redo();
        assertEquals("abc", col.getDefaultValue());
        action.undo();
        assertEquals("", col.getDefaultValue());
    }

    @Test
    void testUpdateDefaultValueNull() {
        Column col = new TestColumn("foo");
        assertThrows(IllegalArgumentException.class, () -> col.updateDefaultValue(null));
    }

    @Test
    void testGetAllowBlankDefault() {
        Column col = new TestColumn("foo");
        assertTrue(col.getAllowBlank());
    }

    @Test
    void testUpdateAllowBlankTrueToFalse() {
        Column col = new TestColumn("foo");
        Action action = col.updateAllowBlank(false);
        assertNotNull(action);
        action.redo();
        assertFalse(col.getAllowBlank());
        action.undo();
        assertTrue(col.getAllowBlank());
    }

    @Test
    void testUpdateAllowBlankNoChange() {
        Column col = new TestColumn("foo");
        Action action = col.updateAllowBlank(true);
        assertSame(Action.NONE, action);
    }

    @Test
    void testAllowCellValueNull() {
        Column col = new TestColumn("foo");
        assertThrows(IllegalArgumentException.class, () -> col.allowCellValue(null));
    }

    @Test
    void testAllowCellValueEmptyString() {
        Column col = new TestColumn("foo");
        assertTrue(col.allowCellValue(""));
        col.updateAllowBlank(false).redo();
        assertFalse(col.allowCellValue(""));
    }

    @Test
    void testAllowCellValueWithType() {
        Column col = new TestColumn("foo");
        // Default type is STRING, always true
        assertTrue(col.allowCellValue("abc"));
        col.updateType(ColumnType.INTEGER).redo();
        assertTrue(col.allowCellValue("123"));
        assertFalse(col.allowCellValue("abc"));
    }
}
