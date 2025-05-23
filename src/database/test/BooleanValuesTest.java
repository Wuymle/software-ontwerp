package database.test;

import database.BooleanValues;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BooleanValuesTest {

    @Test
    void testWithBlankToString() {
        assertEquals("true", BooleanValues.WithBlank.TRUE.toString());
        assertEquals("false", BooleanValues.WithBlank.FALSE.toString());
        assertEquals("", BooleanValues.WithBlank.BLANK.toString());
    }

    @Test
    void testWithBlankFromString() {
        assertEquals(BooleanValues.WithBlank.TRUE, BooleanValues.WithBlank.fromString("true"));
        assertEquals(BooleanValues.WithBlank.FALSE, BooleanValues.WithBlank.fromString("false"));
        assertEquals(BooleanValues.WithBlank.BLANK, BooleanValues.WithBlank.fromString(""));
        assertEquals(BooleanValues.WithBlank.TRUE, BooleanValues.WithBlank.fromString("TRUE"));
        assertEquals(BooleanValues.WithBlank.FALSE, BooleanValues.WithBlank.fromString("FALSE"));
    }

    @Test
    void testWithBlankFromStringInvalid() {
        assertThrows(IllegalArgumentException.class, () -> BooleanValues.WithBlank.fromString("invalid"));
    }

    @Test
    void testWithoutBlankToString() {
        assertEquals("true", BooleanValues.WithoutBlank.TRUE.toString());
        assertEquals("false", BooleanValues.WithoutBlank.FALSE.toString());
    }

    @Test
    void testWithoutBlankFromString() {
        assertEquals(BooleanValues.WithoutBlank.TRUE, BooleanValues.WithoutBlank.fromString("true"));
        assertEquals(BooleanValues.WithoutBlank.FALSE, BooleanValues.WithoutBlank.fromString("false"));
        assertEquals(BooleanValues.WithoutBlank.TRUE, BooleanValues.WithoutBlank.fromString("TRUE"));
        assertEquals(BooleanValues.WithoutBlank.FALSE, BooleanValues.WithoutBlank.fromString("FALSE"));
    }

    @Test
    void testWithoutBlankFromStringInvalid() {
        assertThrows(IllegalArgumentException.class, () -> BooleanValues.WithoutBlank.fromString("invalid"));
        assertThrows(IllegalArgumentException.class, () -> BooleanValues.WithoutBlank.fromString(""));
    }
}
