package database;

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

    Action updateName(String newName) {
        if (newName == null || newName.isEmpty())
            throw new IllegalArgumentException("arguments cannot be null or empty");
        if (newName.equals(this.name))
            return Action.NONE;
        final String oldName = this.name;
        return new Action(() -> {
            this.name = oldName;
        }, () -> {
            this.name = newName;
        });
    }

    public ColumnType getType() {
        return type;
    }

    Action updateType(ColumnType newType) {
        if (newType == null)
            throw new IllegalArgumentException("arguments cannot be null");
        final ColumnType oldType = this.type;
        return new Action(() -> {
            this.type = oldType;
        }, () -> {
            this.type = newType;
        });
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    Action updateDefaultValue(String defaultValue) {
        if (defaultValue == null)
            throw new IllegalArgumentException("arguments cannot be null");
        final String oldValue = this.defaultValue;
        return new Action(() -> {
            this.defaultValue = oldValue;
        }, () -> {
            this.defaultValue = defaultValue;
        });
    }

    public boolean getAllowBlank() {
        return allowBlank;
    }

    Action updateAllowBlank(boolean allowBlank) {
        if (allowBlank == this.allowBlank)
            return Action.NONE;
        final boolean oldAllowBlank = this.allowBlank;
        return new Action(() -> {
            this.allowBlank = oldAllowBlank;
        }, () -> {
            this.allowBlank = allowBlank;
        });

    }

    boolean allowCellValue(String value) {
        if (value == null)
            throw new IllegalArgumentException("value cannot be null");
        if (value == "")
            return allowBlank;
        return type.allowCellValue(value);
    }
}
