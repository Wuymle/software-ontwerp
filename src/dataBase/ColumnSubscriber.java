package database;

public interface ColumnSubscriber {
    void onChange(ColumnType type, boolean allowBlanks);
}
