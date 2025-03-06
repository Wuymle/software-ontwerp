package dataBase.dataBaseTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import dataBase.Cell;
import dataBase.Column;
import dataBase.columnType;

public class CellTest {

        private Column integerColumn;
        private Column stringColumn;
        private Column booleanColumn;
        private Column allowBlankColumn;

        @BeforeEach
        public void setUp() {
            integerColumn = new Column(columnType.INTEGER, false);
            stringColumn = new Column(columnType.STRING, false);
            booleanColumn = new Column(columnType.BOOLEAN, false);
            allowBlankColumn = new Column(columnType.STRING, true);
        }

        @Test
        public void testSetValueInteger() {
            Cell cell = new Cell(integerColumn);
            assertTrue(cell.setValue("123"));
            assertEquals(123, cell.getValue());
            assertFalse(cell.setValue("abc"));
        }

        @Test
        public void testSetValueString() {
            Cell cell = new Cell(stringColumn);
            assertTrue(cell.setValue("hello"));
            assertEquals("hello", cell.getValue());
        }

        @Test
        public void testSetValueBoolean() {
            Cell cell = new Cell(booleanColumn);
            assertTrue(cell.setValue("true"));
            assertEquals(true, cell.getValue());
            assertTrue(cell.setValue("false"));
            assertEquals(false, cell.getValue());
            assertFalse(cell.setValue("notABoolean"));
        }

        @Test
        public void testSetDefault() {
            Cell cell = new Cell(integerColumn);
            assertEquals(0, cell.getValue());

            cell = new Cell(stringColumn);
            assertEquals("", cell.getValue());

            cell = new Cell(booleanColumn);
            assertEquals(false, cell.getValue());

            cell = new Cell(allowBlankColumn);
            assertNull(cell.getValue());
        }

        @Test
        public void testGetValue() {
            Cell cell = new Cell(stringColumn);
            cell.setValue("test");
            assertEquals("test", cell.getValue());
        }

        @Test
        public void testGetColumn() {
            Cell cell = new Cell(integerColumn);
            assertEquals(integerColumn, cell.getColumn());
    }
}
