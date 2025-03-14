package database;

import java.util.ArrayList;

/**
 * Represents a column in a database table, containing multiple cells and having
 * a specific data type.
 */
public class Column {
    private ColumnType type;
    private boolean allowBlank = false;
    private ArrayList<Cell> cells;
    private String defaultValue;
    private boolean validDefaultValue = true;

    /**
     * Constructs a new Column with the default type of STRING and initializes
     * cells.
     */
    public Column() {
        this.type = ColumnType.STRING;
        resetDefaultValue();
        this.cells = new ArrayList<>();
    }

    /**
     * Gets the type of data stored in this column.
     *
     * @return the current ColumnType of this column.
     */
    public ColumnType getType() {
        return type;
    }

    /**
     * Registers a cell to this column.
     *
     * @param cell the Cell object to be added to the column.
     */
    public void registerCell(Cell cell) {
        this.cells.add(cell);
    }

    /**
     * Checks whether blank values are allowed in this column.
     *
     * @return true if blank values are allowed, false otherwise.
     */
    public boolean getAllowBlank() {
        return allowBlank;
    }

    /**
     * Changes the data type of the column and updates existing cells accordingly.
     *
     * @param type the new ColumnType to set.
     */
    public void editColumnType(ColumnType type) {
        this.type = type;
        // resetDefaultValue();
        if (type == ColumnType.BOOLEAN && !(allowBlank && defaultValue.equals("")) && !defaultValue.equals("TRUE")
                && !defaultValue.equals("FALSE")) {
            defaultValue = "TRUE";
        }
        for (Cell cell : this.cells) {
            cell.setValue(cell.getValue()); // Ensures cells conform to the new type
        }
    }

    /**
     * Sets whether blank values are allowed in this column.
     *
     * @param allowBlank true to allow blank values, false otherwise.
     */
    public void setAllowBlank(boolean allowBlank) {
        this.allowBlank = allowBlank;
        resetDefaultValue();
    }

    /**
     * Retrieves all cells stored in this column.
     *
     * @return an ArrayList of Cell objects contained in the column.
     */
    public ArrayList<Cell> getCells() {
        return cells;
    }

    /**
     * Resets the default value of the column based on its type and whether blank
     * values are allowed.
     */
    public void resetDefaultValue() {
        if (getAllowBlank())
            return;
        switch (getType()) {
            case INTEGER:
                defaultValue = "0";
                break;
            case STRING:
                defaultValue = "";
                break;
            case BOOLEAN:
                defaultValue = "false";
                break;
            case EMAIL:
                defaultValue = "@";
                break;
        }
    }

    /**
     * Sets the default value for this column.
     *
     * @param defaultValue the default value to be set.
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        checkValidDefaultValue();
    }

    /**
     * Retrieves the default value of this column.
     *
     * @return the current default value as a String.
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * Cycles through the different column types in the order: STRING -> INTEGER ->
     * BOOLEAN -> EMAIL -> STRING.
     */
    public void toggleColumnType() {
        if (type == ColumnType.STRING) {
            editColumnType(ColumnType.INTEGER);
        } else if (type == ColumnType.INTEGER) {
            editColumnType(ColumnType.BOOLEAN);
        } else if (type == ColumnType.BOOLEAN) {
            editColumnType(ColumnType.EMAIL);
        } else if (type == ColumnType.EMAIL) {
            editColumnType(ColumnType.STRING);
        }
    }

    /**
     * Checks if the default value is valid for the column type.
     */
    private void checkValidDefaultValue() {
        switch (getType()) {
            case INTEGER:
                try {
                    Integer.parseInt(defaultValue);
                } catch (NumberFormatException e) {
                    validDefaultValue = false;
                }
                break;

            case STRING:
                validDefaultValue = true;
                break;
            case BOOLEAN:
                switch (defaultValue.toUpperCase()) {
                    case "TRUE":
                    case "FALSE":
                        validDefaultValue = true;
                        break;
                    default:
                        validDefaultValue = false;
                }
                break;
            case EMAIL:
                if (defaultValue.contains("@") && defaultValue.contains(".")) {
                    validDefaultValue = true;
                } else {
                    validDefaultValue = false;
                }
                break;
            default:
                throw new Error("Column type not recognized or implemented");
        }
    }

    /**
     * Returns whether the default value is valid for the column type.
     *
     * @return true if the default value is valid, false otherwise.
     */
    public boolean getValidDefaultValue() {
        return validDefaultValue;
    }

    public boolean isValidValue(String value) {
        if (allowBlank && value.equals(""))
            return true;
        switch (getType()) {
            case INTEGER:
                try {
                    Integer.parseInt(value);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            case STRING:
                return true;
            case BOOLEAN:
                return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false");
            case EMAIL:
                return value.contains("@");
            default:
                throw new Error("Column type not recognized or implemented");
        }
    }
}
