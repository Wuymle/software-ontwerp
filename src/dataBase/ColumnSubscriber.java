package dataBase;

public interface ColumnSubscriber {
    void onChange(columnType type, boolean allowBlanks);
}
