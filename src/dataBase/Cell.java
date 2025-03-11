package database;

public class Cell {
    private Column column;
    private String value = null;
    private boolean valid = true;

    public Cell(Column column) {
        this.column = column;
        setDefault();
    }

    // Set the value of a cell. The input is a string that will be converted to the
    // correct column type. The method returns true when the value is set correctly.
    // Otherwise false
    public void setValue(String value) {
        this.value = value;
        checkValid();
    }

    private void checkValid() {
        switch (column.getType()) {
            case INTEGER:
                try {
                    Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    valid = false;
                    // throw new Error("Value was not an integer, but column type is integer");
                }
                break;

            case STRING:
                valid = true;
                break;
            case BOOLEAN:
                switch (value.toUpperCase()) {
                    case "TRUE":
                        valid = true;
                        break;
                    case "FALSE":
                        valid = true;
                        break;
                    default:
                        valid = false;
                        // throw new Error("Value was not a valid boolean, but column type is boolean");
                }
                break;
            default:
                throw new Error("Column type not recognized or implemented");
            case EMAIL:
                if (value.contains("@") && value.contains(".")) {
                    valid = true;
                } else {
                    valid = false;
                }
        }
    }

    public boolean isValid() {
        return valid;
    }

    // Set the value of a cell to the default value depending on wheter column
    // allows blank values or not
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

    public String getValue() {
        return value;
    }

    public Column getColumn() {
        return column;
    }
}
