package newdatabase;

public class Cell {
    private String value = "";

    public String getValue() {
        return value;
    }

    void updateValue(String value) {
        if (value == null)
            throw new IllegalArgumentException("value cannot be null");
        this.value = value;
    }
}
