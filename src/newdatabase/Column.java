package newdatabase;

public class Column {
    private String name;
    private ColumnType type = ColumnType.STRING;
    private boolean allowBlank = true;
    private String defaultValue = "";

    Column(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    void updateName(String newName) {
        if (newName == null || newName.isEmpty())
            throw new IllegalArgumentException("arguments cannot be null or empty");
        this.name = newName;
    }

    public ColumnType getType() {
        return type;
    }

    void updateType(ColumnType newType) {
        if (newType == null)
            throw new IllegalArgumentException("arguments cannot be null");
        this.type = newType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    void updateDefaultValue(String defaultValue) {
        if (defaultValue == null)
            throw new IllegalArgumentException("arguments cannot be null");
        this.defaultValue = defaultValue;
    }

    public boolean getAllowBlank() {
        return allowBlank;
    }

    void updateAllowBlank(boolean allowBlank) {
        if (allowBlank == this.allowBlank)
            return;
        this.allowBlank = allowBlank;
    }

    boolean allowCellValue(String value) {
        if (value == null)
            throw new IllegalArgumentException("value cannot be null");
        if (value == "")
            return allowBlank;
        return type.allowCellValue(value);
    }
}
