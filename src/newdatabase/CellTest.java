package newdatabase;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



class CellTest {

    static class DummyColumn extends Column {
        private final String defaultValue;

        DummyColumn(String defaultValue) {
            super("dummy");
            this.defaultValue = defaultValue;
        }

        @Override
        public String getDefaultValue() {
            return defaultValue;
        }
    }

    @Test
    void constructor_setsValueToColumnDefault() {
        DummyColumn column = new DummyColumn("abc");
        Cell cell = new Cell(column);
        assertEquals("abc", cell.getValue());
    }

    @Test
    void getValue_returnsCurrentValue() {
        DummyColumn column = new DummyColumn("xyz");
        Cell cell = new Cell(column);
        assertEquals("xyz", cell.getValue());
    }

    @Test
    void updateValue_returnsActionThatUpdatesAndUndoesValue() {
        DummyColumn column = new DummyColumn("init");
        Cell cell = new Cell(column);

        Action action = cell.updateValue("newVal");
        // Value should not change until redo is called
        assertEquals("init", cell.getValue());

        action.redo();
        assertEquals("newVal", cell.getValue());

        action.undo();
        assertEquals("init", cell.getValue());
    }

    @Test
    void updateValue_withNull_throwsException() {
        DummyColumn column = new DummyColumn("init");
        Cell cell = new Cell(column);

        assertThrows(IllegalArgumentException.class, () -> cell.updateValue(null));
    }

    @Test
    void updateValue_multipleTimes_actionsAreIndependent() {
        DummyColumn column = new DummyColumn("a");
        Cell cell = new Cell(column);
        Action action1 = cell.updateValue("b");
        
        action1.redo();
        assertEquals("b", cell.getValue());
        
        Action action2 = cell.updateValue("c");

        action2.redo();
        assertEquals("c", cell.getValue());

        action2.undo();
        assertEquals("b", cell.getValue());
        
        action1.undo();
        assertEquals("a", cell.getValue());
    }

    @Test
    void updateValue_single_actionsAreIndependent() {
        DummyColumn column = new DummyColumn("a");
        Cell cell = new Cell(column);
        Action action1 = cell.updateValue("b");

        action1.redo();
        assertEquals("b", cell.getValue());

        action1.undo();
        assertEquals("a", cell.getValue());
    }
}
