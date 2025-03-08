package dataBase;

import java.util.ArrayList;

public class Table {
    private ArrayList<Column> columns;
    private ArrayList<Row> rows;

    public Table() {
        columns = new ArrayList<Column>();
        rows = new ArrayList<Row>();
    }

    public void createColumn(columnType type, boolean allowBlank) {
        Column newColumn = new Column(type, allowBlank);
        for (Row row : rows) {
            row.createCell(newColumn);
        }
        columns.add(newColumn);
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