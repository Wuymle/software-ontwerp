package database;

public enum ColumnType {
    /** Represents a string type column. */
    STRING,
    /** Represents an integer type column. */
    INTEGER,
    /** Represents a boolean type column. */
    BOOLEAN,
    /** Represents an email type column. */
    EMAIL;

    boolean allowCellValue(String value) {
        if (value == null)
            throw new IllegalArgumentException("value cannot be null");
        if (value == "")
            return true;
        switch (this) {
            case STRING:
                return true;
            case INTEGER:
                try {
                    Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    return false;
                }
                return true;
            case BOOLEAN:
                for (BooleanValues.WithBlank boolVal : BooleanValues.WithBlank.values()) {
                    if (boolVal.name().equalsIgnoreCase(value)) {
                        return true;
                    }
                }
                return false;
            case EMAIL:
                return value.contains("@") && value.contains(".");
            default:
                throw new IllegalArgumentException("Invalid column type");
        }
    }
};
