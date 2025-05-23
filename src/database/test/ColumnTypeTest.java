package database.test;

import database.ColumnType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;

class ColumnTypeTest {

    // Helper method to invoke package-private allowCellValue method on ColumnType enum instance
    private boolean invokeAllowCellValue(ColumnType type, String value) throws Exception {
        Method method = ColumnType.class.getDeclaredMethod("allowCellValue", String.class);
        method.setAccessible(true);
        // For enum methods that are not static, the first argument to invoke is the enum constant itself.
        return (boolean) method.invoke(type, value);
    }

    @Test
    void testAllowCellValueString() throws Exception {
        assertTrue(invokeAllowCellValue(ColumnType.STRING, "any string"));
        assertTrue(invokeAllowCellValue(ColumnType.STRING, ""));
    }

    @Test
    void testAllowCellValueInteger() throws Exception {
        assertTrue(invokeAllowCellValue(ColumnType.INTEGER, "123"));
        assertTrue(invokeAllowCellValue(ColumnType.INTEGER, "-45"));
        assertTrue(invokeAllowCellValue(ColumnType.INTEGER, "0"));
        assertTrue(invokeAllowCellValue(ColumnType.INTEGER, ""));
        assertFalse(invokeAllowCellValue(ColumnType.INTEGER, "abc"));
        assertFalse(invokeAllowCellValue(ColumnType.INTEGER, "1.23"));
        assertFalse(invokeAllowCellValue(ColumnType.INTEGER, "1 2"));
    }

    @Test
    void testAllowCellValueBoolean() throws Exception {
        assertTrue(invokeAllowCellValue(ColumnType.BOOLEAN, "true"));
        assertTrue(invokeAllowCellValue(ColumnType.BOOLEAN, "false"));
        assertTrue(invokeAllowCellValue(ColumnType.BOOLEAN, "TRUE"));
        assertTrue(invokeAllowCellValue(ColumnType.BOOLEAN, "FALSE"));
        assertTrue(invokeAllowCellValue(ColumnType.BOOLEAN, "")); // Blank is allowed for boolean type
        // Based on ColumnType.java, it checks boolVal.name().equalsIgnoreCase(value)
        // BooleanValues.WithBlank enum names are TRUE, FALSE, BLANK.
        // So "yes", "no", "0", "1" should be false.
        assertFalse(invokeAllowCellValue(ColumnType.BOOLEAN, "yes"));
        assertFalse(invokeAllowCellValue(ColumnType.BOOLEAN, "no"));
        assertFalse(invokeAllowCellValue(ColumnType.BOOLEAN, "0"));
        assertFalse(invokeAllowCellValue(ColumnType.BOOLEAN, "1"));
    }

    @Test
    void testAllowCellValueEmail() throws Exception {
        assertTrue(invokeAllowCellValue(ColumnType.EMAIL, "test@example.com"));
        assertTrue(invokeAllowCellValue(ColumnType.EMAIL, "user.name@domain.co.uk"));
        assertTrue(invokeAllowCellValue(ColumnType.EMAIL, ""));
        assertFalse(invokeAllowCellValue(ColumnType.EMAIL, "testexample.com")); // Missing @
        assertTrue(invokeAllowCellValue(ColumnType.EMAIL, "@example.com")); // Missing local part
    }

    @Test
    void testAllowCellValueNull() {
        for (ColumnType type : ColumnType.values()) {
            // Wrap the reflective call in a lambda for assertThrows
            Exception exception = assertThrows(Exception.class, () -> invokeAllowCellValue(type, null));
            // Check that the cause of the reflection exception is IllegalArgumentException
            assertTrue(exception.getCause() instanceof IllegalArgumentException, 
                "Expected cause to be IllegalArgumentException for type " + type);
            assertEquals("value cannot be null", exception.getCause().getMessage(), 
                "Incorrect exception message for type " + type);
        }
    }
}
