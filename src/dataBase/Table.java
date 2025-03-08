package dataBase;

import java.util.ArrayList;

public class Table {
    private ArrayList<Column> columns = new ArrayList<Column>();
    private ArrayList<Row> rows = new ArrayList<Row>();

    public void createColumn(columnType type, boolean allowBlank) {
        Column newColumn = new Column(type, allowBlank);
        columns.add(newColumn);
        for (Row row : rows) {
            row.createCell(newColumn);
        }
    }

    public void createRow() {
        Row newRow = new Row();
        newRow.createCells(columns);
        rows.add(newRow);
    }

    public ArrayList<Column> getColumns() {
        return columns;
    }

    public ArrayList<Row> getRows() {
        return rows;
    }
}