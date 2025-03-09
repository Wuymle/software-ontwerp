package dataBase;

import java.util.ArrayList;

public class Row {
    private ArrayList<Cell> cells = new ArrayList<Cell>();

    public void createCell(Column column) {
        Cell newCell = new Cell(column);
        cells.add(newCell);
        column.registerCell(newCell);
    }

    public void createCells(ArrayList<Column> columns) {
        for (Column column : columns) {
            createCell(column);
        }
    }

    public void deleteCell(int index){
        cells.remove(index);
    }


    public ArrayList<Cell> getCells() {
        return cells;
    }
}
