package dataBase;

import java.util.ArrayList;

public class Table {
    ArrayList<Column> columns;
    ArrayList<Row> rows;

    public void createColumn(columnType type, boolean allowBlank) {
        columns.add(new Column(type, allowBlank));
    }

    public void createRow() {
        rows.add(new Row());
    }
}