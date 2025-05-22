package newdatabase;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class CellTest {

    @Test
    void testInitialValueIsEmptyString() {
        Cell cell = new Cell();
        assertEquals("", cell.getValue());
    }

    @Test
    void testUpdateValueSetsValue() {
        Cell cell = new Cell();
        cell.updateValue("test");
        assertEquals("test", cell.getValue());
    }

    @Test
    void testUpdateValueWithEmptyString() {
        Cell cell = new Cell();
        cell.updateValue("");
        assertEquals("", cell.getValue());
    }

    @Test
    void testUpdateValueWithNullThrowsException() {
        Cell cell = new Cell();
        assertThrows(IllegalArgumentException.class, () -> cell.updateValue(null));
    }

    @Test
    void testUpdateValueMultipleTimes() {
        Cell cell = new Cell();
        cell.updateValue("first");
        assertEquals("first", cell.getValue());
        cell.updateValue("second");
        assertEquals("second", cell.getValue());
    }
}
