package dataBase;

import java.util.ArrayList;

public class Row {
    ArrayList<Cell> cells;

    public Row() {
        cells = new ArrayList<Cell>();
    }

    public void addCell(Cell cell) {
        cells.add(cell);
    }

    public void createCell(Column column) {
        Cell newCell = new Cell(column);
        addCell(newCell);
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
