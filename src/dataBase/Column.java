package dataBase;

public class Column {
    private columnType type;
    private boolean allowBlank = false;

    public Column(columnType type, boolean allowBlank) {
        this.type = type;
        this.allowBlank = allowBlank;
    }

    public columnType getType() {
        return type;
    }

    public boolean getAllowBlank() {
        return allowBlank;
    }
}
