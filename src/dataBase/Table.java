package dataBase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Table {
    private Map<String, Column> columns;
    private ArrayList<Row> rows;

    public Table() {
        columns = new LinkedHashMap<>(); // Changed to LinkedHashMapged to LinkedHashMap
        rows = new ArrayList<>();
    }

    public void createColumn(String name, columnType type, boolean allowBlank) {
        Column newColumn = new Column(type, allowBlank);
        for (Row row : rows) {
            row.createCell(newColumn);
        }
        columns.put(name, newColumn);
    }

    public void createRow() {
        Row newRow = new Row();
        newRow.createCells(columns.values()); // Pass the collection of columns
        rows.add(newRow);
    }
    
    public ArrayList<String> getColumns() {
        return new ArrayList<>(columns.keySet());
    }

    public ArrayList<Object> getRows() {
        ArrayList<Object> rowValues = new ArrayList<>();

        for (int i = 0; i < rows.size(); i++) {
            rowValues.add(getRow(i));
        }

        return rowValues;
    }

    public ArrayList<Object> getRow(int index) {
        ArrayList<Object> row = new ArrayList<>();

        for (Cell cell : rows.get(index).getCells()) {
            row.add(cell.getValue());
        }

        return row;
    }

    public void deleteRow(int index){
        rows.remove(index);
    }

    public void deleteColumn(String name){
        columns.remove(name);

        for (Row row : rows) {
            row.deleteCell(new ArrayList<>(columns.keySet()).indexOf(name));
        }
    }

    public Cell getCell(String columnName, int rowIndex){
        return rows.get(rowIndex).getCells().get(new ArrayList<>(columns.keySet()).indexOf(columnName));
    }

    public void editCell(String columnName, int rowIndex, String value){
        rows.get(rowIndex).getCells().get(new ArrayList<>(columns.keySet()).indexOf(columnName)).setValue(value);
    }
}