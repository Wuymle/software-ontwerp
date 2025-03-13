package database;

/**
 * Represents a cell in a database table associated with a specific column.
 */
public class Cell {
    private Column column;
    private String value = null;
    private boolean valid = true;

    /**
     * Constructs a new Cell associated with the specified column.
     * The cell's value is set to its default value based on the column's settings.
     *
     * @param column The column associated with this cell.
     */
    public Cell(Column column) {
        this.column = column;
        setDefault();
    }

    /**
     * Sets the value of this cell. The input is a string that will be validated
     * and converted based on the associated column's type.
     *
     * @param value The string value to set.
     */
    public void setValue(String value) {
        this.value = value;
        checkValid();
    }

    /**
     * Checks if the current value is valid according to the column's type.
     * Sets the valid flag accordingly.
     */
    private void checkValid() {
        switch (column.getType()) {
            case INTEGER:
                try {
                    Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    valid = false;
                }
                break;

            case STRING:
                valid = true;
                break;

            case BOOLEAN:
                switch (value.toUpperCase()) {
                    case "TRUE":
                    case "FALSE":
                        valid = true;
                        break;
                    default:
                        valid = false;
                }
                break;

            case EMAIL:
                valid = value.contains("@") && value.contains(".");
                break;

            default:
                throw new Error("Column type not recognized or implemented");
        }
    }

    /**
     * Returns whether the current value of the cell is valid.
     *
     * @return true if the value is valid; false otherwise.
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Sets the cell's value to the default value based on the column type and
     * whether the column allows blank values.
     */
    public void setDefault() {
        if (column.getAllowBlank()) {
            this.value = null;
            return;
        }

        switch (column.getType()) {
            case INTEGER:
                this.value = "0";
                break;
            case STRING:
                this.value = "";
                break;
            case BOOLEAN:
                this.value = "false";
                break;
            case EMAIL:
                this.value = "@";
                break;
        }
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
     * @return The column of this cell.
     */
    public Column getColumn() {
        return column;
    }
}
