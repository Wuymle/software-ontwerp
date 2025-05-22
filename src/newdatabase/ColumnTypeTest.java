package newdatabase;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



class ColumnTypeTest {

    @Test
    void testAllowCellValue_StringType() {
        assertTrue(ColumnType.STRING.allowCellValue("hello"));
        assertTrue(ColumnType.STRING.allowCellValue(""));
        assertTrue(ColumnType.STRING.allowCellValue("123"));
        assertTrue(ColumnType.STRING.allowCellValue("true"));
        assertTrue(ColumnType.STRING.allowCellValue("test@example.com"));
    }

    @Test
    void testAllowCellValue_IntegerType() {
        assertTrue(ColumnType.INTEGER.allowCellValue("123"));
        assertTrue(ColumnType.INTEGER.allowCellValue("-456"));
        assertTrue(ColumnType.INTEGER.allowCellValue("0"));
        assertTrue(ColumnType.INTEGER.allowCellValue(""));
        assertFalse(ColumnType.INTEGER.allowCellValue("abc"));
        assertFalse(ColumnType.INTEGER.allowCellValue("12.3"));
        assertFalse(ColumnType.INTEGER.allowCellValue("123abc"));
    }

    @Test
    void testAllowCellValue_BooleanType() {
        assertTrue(ColumnType.BOOLEAN.allowCellValue("true"));
        assertTrue(ColumnType.BOOLEAN.allowCellValue("false"));
        assertTrue(ColumnType.BOOLEAN.allowCellValue("TRUE"));
        assertTrue(ColumnType.BOOLEAN.allowCellValue("False"));
        assertTrue(ColumnType.BOOLEAN.allowCellValue(""));
        assertFalse(ColumnType.BOOLEAN.allowCellValue("yes"));
        assertFalse(ColumnType.BOOLEAN.allowCellValue("0"));
        assertFalse(ColumnType.BOOLEAN.allowCellValue("1"));
    }

    @Test
    void testAllowCellValue_EmailType() {
        assertTrue(ColumnType.EMAIL.allowCellValue("test@example.com"));
        assertTrue(ColumnType.EMAIL.allowCellValue("a@b.c"));
        assertTrue(ColumnType.EMAIL.allowCellValue(""));
        assertTrue(ColumnType.EMAIL.allowCellValue("test@.com"));
        assertFalse(ColumnType.EMAIL.allowCellValue("testexample.com"));
        assertFalse(ColumnType.EMAIL.allowCellValue("test@com"));
        assertFalse(ColumnType.EMAIL.allowCellValue("test.com"));
    }

    @Test
    void testAllowCellValue_NullValueThrows() {
        for (ColumnType type : ColumnType.values()) {
            assertThrows(IllegalArgumentException.class, () -> type.allowCellValue(null));
        }
    }
}
