package newdatabase;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ColumnTest {

    @Test
    void constructor_setsNameAndDefaults() {
        Column col = new Column("Test");
        assertEquals("Test", col.getName());
        assertEquals(ColumnType.STRING, col.getType());
        assertTrue(col.getAllowBlank());
        assertEquals("", col.getDefaultValue());
    }

    @Test
    void updateName_validName_updatesName() {
        Column col = new Column("Old");
        col.updateName("New");
        assertEquals("New", col.getName());
    }

    @Test
    void updateName_nullOrEmpty_throws() {
        Column col = new Column("Old");
        assertThrows(IllegalArgumentException.class, () -> col.updateName(null));
        assertThrows(IllegalArgumentException.class, () -> col.updateName(""));
    }

    @Test
    void updateType_validType_updatesType() {
        Column col = new Column("Col");
        col.updateType(ColumnType.STRING);
        assertEquals(ColumnType.STRING, col.getType());
    }

    @Test
    void updateType_null_throws() {
        Column col = new Column("Col");
        assertThrows(IllegalArgumentException.class, () -> col.updateType(null));
    }

    @Test
    void updateDefaultValue_validValue_updates() {
        Column col = new Column("Col");
        col.updateDefaultValue("abc");
        assertEquals("abc", col.getDefaultValue());
    }

    @Test
    void updateDefaultValue_null_throws() {
        Column col = new Column("Col");
        assertThrows(IllegalArgumentException.class, () -> col.updateDefaultValue(null));
    }

    @Test
    void updateAllowBlank_changesValue() {
        Column col = new Column("Col");
        col.updateAllowBlank(false);
        assertFalse(col.getAllowBlank());
        col.updateAllowBlank(true);
        assertTrue(col.getAllowBlank());
    }

    @Test
    void updateAllowBlank_sameValue_noChange() {
        Column col = new Column("Col");
        col.updateAllowBlank(true); // default is true, should do nothing
        assertTrue(col.getAllowBlank());
    }

    @Test
    void allowCellValue_null_throws() {
        Column col = new Column("Col");
        assertThrows(IllegalArgumentException.class, () -> col.allowCellValue(null));
    }

    @Test
    void allowCellValue_emptyString_returnsAllowBlank() {
        Column col = new Column("Col");
        col.updateAllowBlank(false);
        assertFalse(col.allowCellValue(""));
        col.updateAllowBlank(true);
        assertTrue(col.allowCellValue(""));
    }

    @Test
    void allowCellValue_delegatesToType() {
        Column col = new Column("Col");
        col.updateType(ColumnType.STRING);
        assertTrue(col.allowCellValue("abc"));
    }
}
