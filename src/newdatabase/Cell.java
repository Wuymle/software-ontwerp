package newdatabase;

public class Cell {
    private String value = "";

    public Cell(Column column) {
        value = column.getDefaultValue();
    }

    public String getValue() {
        return value;
    }

    Action updateValue(String value) {
        if (value == null)
            throw new IllegalArgumentException("value cannot be null");
        final String oldValue = this.value;
        return new Action(() -> {
            this.value = oldValue;
        }, () -> {
            this.value = value;
        });
    }
}
