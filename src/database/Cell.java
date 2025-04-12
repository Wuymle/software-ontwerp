package database;

/**
 * Represents a cell in a table, associated with a specific column.
 * A cell holds a value and checks if the value is valid according to the column
 * type.
 */
public class Cell {
    private Column column;
    private String value = "";

    /**
     * Constructs a Cell object associated with a given column.
     * Initializes the cell's value to the default value based on the column type.
     *
     * @param column The column this cell belongs to.
     */
    public Cell(Column column) {
        this.column = column;
        setDefault();
    }

    /**
     * Sets the value of the cell.
     * The input is a string that will be validated according to the column type.
     *
     * @param value The value to be set for this cell.
     */
    public void setValue(String value) {
        if (column.isValidValue(value,column.getType())) {
            this.value = value;
        } else {
            throw new IllegalArgumentException("Invalid value for cell: " + value);
        }
    }

    /**
     * Sets the value of the cell to the default value based on the column type.
     * If the column allows blank values, the value is set to null.
     */
    public void setDefault() {
        this.value = column.getDefaultValue();
        // if (column.getAllowBlank()) {
        // this.value = "";
        // return;
        // }

        // switch (column.getType()) {
        // case INTEGER:
        // this.value = "0";
        // break;
        // case STRING:
        // this.value = "";
        // break;
        // case BOOLEAN:
        // this.value = "false";
        // break;
        // case EMAIL:
        // this.value = "@";
        // break;
        // }
    }

    /**
     * Returns the current value of the cell.
     *
     * @return The value of the cell as a string.
     */
    public String getValue() {
        return value;
    }

    /**
     * Returns the column associated with this cell.
     *
     * @return The column this cell belongs to.
     */
    public Column getColumn() {
        return column;
    }
}
