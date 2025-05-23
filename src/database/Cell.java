package database;

public class Cell {
    private String value = "";

    public Cell(Column column) {
        value = column.getDefaultValue();
    }

    public String getValue() {
        return value;
    }

    Action updateValue(final String value) {
        if (value == null)
            throw new IllegalArgumentException("value cannot be null");
        final String oldValue = this.value;
        final String newValue = value;
        return new Action(() -> {
            System.out.println("UNDO TO " + oldValue);
            this.value = oldValue;
        }, () -> {
            System.out.println("REDO TO " + newValue);
            this.value = newValue;
        });
    }
}
