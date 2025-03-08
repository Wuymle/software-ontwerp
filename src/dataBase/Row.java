package dataBase;

import java.util.ArrayList;

public class Row {
    private ArrayList<Cell> cells = new ArrayList<Cell>();

    public void createCell(Column column) {
        cells.add(new Cell(column));
    }

    public void createCells(ArrayList<Column> columns) {
        for (Column column : columns) {
            createCell(column);
        }
    }

    public ArrayList<Cell> getCells() {
        return cells;
    }
}
