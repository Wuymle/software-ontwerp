package dataBase;

public class Cell {
    private Column column;
    private Object value = null;

    public Cell(Column column) {
        this.column = column;
        setDefault();
    }

    // Set the value of a cell. The input is a string that will be converted to the
    // correct column type. The method returns true when the value is set correctly.
    // Otherwise false
    public boolean setValue(String value) {
        if (column.getType() == columnType.INTEGER) {
            try {
                this.value = Integer.parseInt(value);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        } else if (column.getType() == columnType.STRING) {
            this.value = value;
            return true;
        } else if (column.getType() == columnType.BOOLEAN) {
            if (value.toUpperCase().equals("TRUE")) {
                this.value = true;
                return true;
            } else if (value.toUpperCase().equals("FALSE")) {
                this.value = false;
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    // Set the value of a cell to the default value depending on wheter column
    // allows blank values or not
    public void setDefault() {
        if (column.getAllowBlank()) {
            this.value = null;
        } else {
            if (column.getType() == columnType.INTEGER) {
                this.value = 0;
            } else if (column.getType() == columnType.STRING) {
                this.value = "";
            } else if (column.getType() == columnType.BOOLEAN) {
                this.value = false;
            }
        }
    }

    public Object getValue() {
        return value;
    }

    public Column getColumn() {
        return column;
    }
}
