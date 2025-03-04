package dataBase;

public class Column {
    private columnType type;
    private Cell[] cells;

    public Column(columnType type) {
        this.type = type;
    }
}
