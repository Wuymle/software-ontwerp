package database;

import java.util.ArrayList;

/**
 * Represents a column in a database table, containing multiple cells and having
 * a specific data type.
 */
public class Column {
    private ColumnType type;
    private boolean allowBlank = true;
    private ArrayList<Cell> cells;
    private String defaultValue = "";

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
     * Retrieves all cells stored in this column.
     *
     * @return an ArrayList of Cell objects contained in the column.
     */
    public ArrayList<Cell> getCells() {
        return cells;
    }

    /**
     * Registers a cell to this column.
     *
     * @param cell the Cell object to be added to the column.
     */
    public void registerCell(Cell cell) {
        if (!isValidValue(cell.getValue(), type)) {
            throw new Error("Invalid cell value for column type: " + type);
        }
        this.cells.add(cell);
    }

    /**
     * Deletes a cell from this column at the specified index.
     * 
     * @param index the index of the cell to be deleted.
     * @throws IndexOutOfBoundsException if the index is out of range.
     */
    public void deleteCell(int index) {
        cells.remove(index);
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
     * Changes the data type of the column and updates existing cells accordingly.
     *
     * @param type the new ColumnType to set.
     */
    public void updateColumnType(ColumnType type) {
        if (!isValidColumnType(type)) throw new Error("Invalid column type for existing cells");
    
        defaultValue = makeDefaultTypeValid(this.type, type, defaultValue);
        System.out.println(defaultValue);
        this.type = type;
    }

    /**
     * Converts the default value to a valid format based on the old and new column
     * types. Tries to convert the default value to the new type if possible.
     *
     * @param oldType the original ColumnType of the column.
     * @param newType the new ColumnType to be set.
     * @param value   the default value to be converted.
     * @return a valid default value as a String.
     */
    private String makeDefaultTypeValid(ColumnType oldType, ColumnType newType, String value) {
        if (value.equals("")) {
            return ""; // No conversion needed for empty string
        }
        if (oldType == newType) {
            return value; // No conversion needed if types are the same
        }

        switch (oldType) {
            case ColumnType.STRING:
                switch (newType) {
                    case INTEGER:
                        try {
                            return Integer.parseInt(value) + "";
                        } catch (NumberFormatException e) {
                            return "0";
                        }
                    case BOOLEAN:
                        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                            return value;
                        } else {
                            return "false";
                        }
                    case EMAIL:
                        if (value.contains("@") && value.contains(".")) {
                            return value;
                        } else {
                            return "@";
                        }
                    default:
                        return value;
                }            

            case ColumnType.INTEGER:
                switch (newType) {
                    case STRING:
                        return value + "";
                    case BOOLEAN:
                    if (value.equals("0") || value.equals("1")) {
                            return value.equals("0") ? "FALSE" : "TRUE";
                        } else {
                            return "FALSE";
                        }
                    case EMAIL:
                        return "@";
                    default:
                        return value;
                }
                            

            case ColumnType.BOOLEAN:
                switch (newType) {
                    case STRING:
                        return value.equals("true") ? "TRUE" : "FALSE";
                    case INTEGER:
                        return value.equals("true") ? "1" : "0";
                    case EMAIL:
                        return "@";
                    default:
                        return value;
                }
            
            case ColumnType.EMAIL:
                switch (newType) {
                    case STRING:
                        return value;
                    case INTEGER:
                        return "0";
                    case BOOLEAN:
                        return "false";
                    default:
                        return value;
                }
            default:
                return value;
            }
        }

    /**
     * Checks if the column type is valid for all cells in the column.
     *
     * @param type the ColumnType to check against.
     * @return true if all cells are valid for the new type, false otherwise.
     */
    public boolean isValidColumnType(ColumnType type){
        for (Cell cell : this.cells) {
            if (!isValidValue(cell.getValue(), type)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Cycles through the different column types in the order: STRING -> INTEGER ->
     * BOOLEAN -> EMAIL -> STRING.
     */
    public void toggleColumnType() {
        if (type == ColumnType.STRING) {
            updateColumnType(ColumnType.INTEGER);
        } else if (type == ColumnType.INTEGER) {
            updateColumnType(ColumnType.BOOLEAN);
        } else if (type == ColumnType.BOOLEAN) {
            updateColumnType(ColumnType.EMAIL);
        } else if (type == ColumnType.EMAIL) {
            updateColumnType(ColumnType.STRING);
        }
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
     * Sets whether blank values are allowed in this column.
     *
     * @param allowBlank true to allow blank values, false otherwise.
     */
    public void setAllowBlank(boolean allowBlank) {
        if (!isValidAllowBlankValue(allowBlank)) {
            throw new Error("Invalid allowBlank value. Either a cell is blank or the default value is blank, when allowBlank=" + allowBlank);
        }
        this.allowBlank = allowBlank;
    }

    /**
     * Checks if the allowBlank value is valid based on the current default value
     * and cell values.
     *
     * @param value the new allowBlank value to check.
     * @return true if the value is valid, false otherwise.
     */
    public boolean isValidAllowBlankValue(boolean value) {
        if (value)
            return true;
        if (defaultValue.equals("") && !value)
            return false;
        for (Cell cell : cells) {
            if (cell.getValue().equals(""))
                return false;
        }
        return true;
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
     * Sets the default value for this column.
     *
     * @param defaultValue the default value to be set.
     */
    public void setDefaultValue(String defaultValue) {
        if (!isValidDefaultValue(defaultValue)) {
            throw new Error("Invalid default value for column type: " + getType());
        }
        this.defaultValue = defaultValue;
    }

    /**
     * Checks if the provided default value is valid for the current column type.
     *
     * @param defaultValue the default value to check.
     * @return true if the default value is valid, false otherwise.
     */
    private boolean isValidDefaultValue(String defaultValue) {
        if (defaultValue.equals("")) {
            return allowBlank; // If blank values are allowed, default value can be empty
        }

        switch (getType()) {
            case INTEGER:
                try {
                    Integer.parseInt(defaultValue);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }

            case STRING:
                return true;
            case BOOLEAN:
                return (defaultValue.equalsIgnoreCase("true") || defaultValue.equalsIgnoreCase("false"));
            case EMAIL:
                return (defaultValue.contains("@") && defaultValue.contains("."));
            default:
                throw new Error("Column type not recognized or implemented");
        }
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
     * Overloaded function for a given value and type.
     */
    public boolean isValidValue(String value, ColumnType type) {
        if (value.equals(""))
            return allowBlank;
        switch (type) {
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
